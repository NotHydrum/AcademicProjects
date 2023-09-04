//Grupo: 17
//Alunos:
//  Tiago Fernandes - 55246
//  Henrique Catarino - 56278
//  Miguel Nunes - 56338

#include <ifaddrs.h>
#include <netdb.h>
#include <netinet/in.h>
#include <pthread.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <signal.h>
#include <unistd.h>

#include "zookeeper/zookeeper.h"

#include "network_server.h"
#include "network_server-private.h"
#include "tree_skel.h"
#include "tree_skel-private.h"

struct tree_t *root;
int number_threads;
pthread_t *threads;
int last_assigned;
struct op_proc *proc;
struct request_t *queue_head;
pthread_mutex_t queue_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_cond_t cond = PTHREAD_COND_INITIALIZER;
pthread_mutex_t tree_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t proc_mutex = PTHREAD_MUTEX_INITIALIZER;
pthread_mutex_t assign_mutex = PTHREAD_MUTEX_INITIALIZER;
int stop_skel;
zhandle_t *zookeeper;
int zoo_connected;
char* zoo_node_id;
char* next_server;
int next_server_socket;

int tree_skel_init(int N) {
    stop_skel = 0;
    root = tree_create();
    last_assigned = 1;
    proc = malloc(sizeof(struct op_proc));
    queue_head = NULL;
    number_threads = N;
    threads = malloc(number_threads * sizeof(pthread_t));
    if (root != NULL && proc != NULL && threads != NULL) {
        proc->max_proc = 0;
        proc->in_progress = malloc(N * sizeof(int));
        if (proc->in_progress != NULL) {
            for (int i = 0; i < number_threads; i++) {
                int *i_ptr = &i;
                proc->in_progress[i] = 0;
                pthread_create(&threads[i], NULL, process_request, i_ptr);
            }
            return 0;
        }
    }
    return -1;
}

void connection_watcher(zhandle_t *zzh, int type, int state, const char *path, void *watcherCtx) {
    if (type == ZOO_SESSION_EVENT) {
        if (state == ZOO_CONNECTED_STATE) {
            zoo_connected = 1;
        }
        else {
            zoo_connected = 0;
        }
    }
}

void free_string_vector(struct String_vector *vector) {
    if (vector->data != NULL) {
        for (int i = 0; i < vector->count; ++i) {
            free(vector->data[i]);
        }
        free(vector->data);
    }
    free(vector);
}

void children_watcher(zhandle_t *zzh, int type, int state, const char *path, void *watcherCtx) {
    struct String_vector *children = malloc(sizeof(struct String_vector));
    char *buffer = malloc(99 * sizeof(char));
    if (state == ZOO_CONNECTED_STATE && type == ZOO_CHILD_EVENT && children != NULL &&
            zoo_wget_children(zookeeper, "/chain", children_watcher,
                              watcherCtx, children) == ZOK && buffer != NULL) {
        char temp[99];
        for(int i = 0; i < children->count; i++){
            for(int f = 0; f < children->count - 1 - i; f++){
                if(strcmp(children->data[f], children->data[f + 1]) > 0){
                    strcpy(temp, children->data[f]);
                    strcpy(children->data[f], children->data[f + 1]);
                    strcpy(children->data[f + 1], temp);
                }
            }
        }
        next_server = NULL;
        for (int i = 0; i < children->count; i++) {
            char *child = malloc((strlen("/chain/") + strlen(children->data[i]) + 1) * sizeof(char));
            strcpy(child, "/chain/");
            strcat(child, children->data[i]);
            if (strcmp(child, zoo_node_id) == 0 && i + 1 < children->count) {
                int buffer_len = 99 * sizeof(char);
                char *next_child = malloc(
                        (strlen("/chain/") + strlen(children->data[i + 1]) + 1) * sizeof(char));
                strcpy(next_child, "/chain/");
                strcat(next_child, children->data[i + 1]);
                if (zoo_get(zookeeper, next_child, 0, buffer,
                            &buffer_len, NULL) == ZOK) {
                    int previous_server_socket = next_server_socket;
                    next_server_socket = server_connect(buffer);
                    close(previous_server_socket);
                }
                free(child);
                free(next_child);
                break;
            }
            else if (strcmp(child, zoo_node_id) == 0) {
                free(child);
                break;
            }
            free(child);
        }
    }
    free_string_vector(children);
}

int zookeeper_connect(char *address_port, char *server_port) {
    zoo_connected = 0;
    zookeeper = zookeeper_init(address_port, connection_watcher,
                               10000, 0, 0, 0);
    if (zookeeper == NULL) {
        return -1;
    }
    printf("Connecting to Zookeeper...\n");
    while (1) {
        if (zoo_connected) {
            printf("Connection to Zookeeper established.\n");
            break;
        }
    }
    int exists = zoo_exists(zookeeper, "/chain", 0, NULL);
    if (!(exists == ZOK || exists == ZNONODE)) {
        return -1;
    }
    if (exists == ZNONODE) {
        int result = zoo_create(zookeeper, "/chain", NULL, 0, &ZOO_OPEN_ACL_UNSAFE,
                                0, NULL, 0);
        if (!(result == ZOK || result == ZNODEEXISTS)) {
            return -1;
        }
    }
    struct ifaddrs *ifaddr;
    if (getifaddrs(&ifaddr) == -1) {
        return -1;
    }
    char *address = NULL;
    for (struct ifaddrs *ifa = ifaddr; ifa != NULL; ifa = ifa->ifa_next) {
        if (ifa->ifa_addr != NULL && ifa->ifa_addr->sa_family != AF_PACKET && strcmp(ifa->ifa_name, "lo") != 0) {
            address = malloc(99 * sizeof(char));
            if (address == NULL) {
                freeifaddrs(ifaddr);
                return -1;
            }
            if (getnameinfo(ifa->ifa_addr,
                            (ifa->ifa_addr->sa_family == AF_INET) ?
                            sizeof(struct sockaddr_in) : sizeof(struct sockaddr_in6),
                            address, 99,NULL, 0,
                            NI_NUMERICHOST) == -1) {
                free(address);
                freeifaddrs(ifaddr);
                return -1;
            }
            break;
        }
    }
    freeifaddrs(ifaddr);
    if (address == NULL) {
        return -1;
    }
    char *metadata = malloc(99 * sizeof(char));
    if (metadata == NULL) {
        return -1;
    }
    strcpy(metadata, address);
    strcat(metadata, ":");
    strcat(metadata, server_port);
    free(address);
    char *buffer = malloc(99 * sizeof(char));
    if (buffer == NULL) {
        return -1;
    }
    if (zoo_create(zookeeper, "/chain/node", metadata,
                   (strlen(metadata) + 1) * sizeof(char),
                   &ZOO_READ_ACL_UNSAFE, ZOO_EPHEMERAL | ZOO_SEQUENCE,
                   buffer, 99 * sizeof(char)) != ZOK) {
        free(buffer);
        free(metadata);
        return -1;
    }
    free(metadata);
    zoo_node_id = malloc((strlen(buffer) + 1) * sizeof(char));
    strcpy(zoo_node_id, buffer);
    struct String_vector *children = malloc(sizeof(struct String_vector));
    if (children == NULL) {
        free(buffer);
        return -1;
    }
    if (zoo_wget_children(zookeeper, "/chain", children_watcher,
                          NULL, children) != ZOK) {
        free(buffer);
        free_string_vector(children);
        return -1;
    }
    char temp[99];
    for(int i = 0; i < children->count; i++){
        for(int f = 0; f < children->count - 1 - i; f++){
            if(strcmp(children->data[f], children->data[f + 1]) > 0){
                strcpy(temp, children->data[f]);
                strcpy(children->data[f], children->data[f + 1]);
                strcpy(children->data[f + 1], temp);
            }
        }
    }
    next_server = NULL;
    for (int i = 0; i < children->count; i++) {
        char *child = malloc((strlen("/chain/") + strlen(children->data[i]) + 1) * sizeof(char));
        strcpy(child, "/chain/");
        strcat(child, children->data[i]);
        if (strcmp(child, zoo_node_id) == 0 && i + 1 < children->count) {
            int buffer_len = 99 * sizeof(char);
            char *next_child = malloc(
                    (strlen("/chain/") + strlen(children->data[i + 1]) + 1) * sizeof(char));
            strcpy(next_child, "/chain/");
            strcat(next_child, children->data[i + 1]);
            if (zoo_get(zookeeper, next_child, 0, buffer,
                        &buffer_len, NULL) == ZOK) {
                next_server_socket = server_connect(buffer);
                if (next_server_socket != -1) {
                    free(buffer);
                    free_string_vector(children);
                    free(child);
                    free(next_child);
                    return 0;
                }
            }
            free(child);
            free(next_child);
            break;
        }
        else if (strcmp(child, zoo_node_id) == 0) {
            free(buffer);
            free_string_vector(children);
            free(child);
            return 0;
        }
        free(child);
    }
    free(buffer);
    free_string_vector(children);
    return -1;
}

int server_connect(char* address_port) {
    if (next_server != NULL) {
        free(next_server);
    }
    next_server = malloc((strlen(address_port) + 1) * sizeof(char));
    strcpy(next_server, address_port);
    char *add_port_cpy = malloc((strlen(address_port) + 1) * sizeof(char));
    if (add_port_cpy == NULL) {
        return -1;
    }
    strcpy(add_port_cpy, address_port);
    char* port = strrchr(add_port_cpy, ':');
    char* address = malloc((strlen(add_port_cpy) - strlen(port) + 1) * sizeof(char));
    if (address == NULL) {
        return -1;
    }
    memset(address, '\0', strlen(add_port_cpy) - strlen(port) + 1);
    strncpy(address, add_port_cpy, strlen(add_port_cpy) - strlen(port));
    memmove(port, port+1, strlen(port)); //deletes ":" from port
    int sock = socket(AF_INET, SOCK_STREAM, 0);
    struct addrinfo **info = malloc(sizeof(struct addrinfo **));
    if (info == NULL) {
        return -1;
    }
    getaddrinfo(address, port, NULL, info);
    struct sockaddr_in *sock_address = malloc(sizeof(struct sockaddr_in));
    if (info[0] != NULL && info[0]->ai_addr != NULL && sock_address != NULL && sock != -1) {
        memcpy(sock_address, info[0]->ai_addr, sizeof(struct sockaddr));
        int optval = 1;
        if (connect(sock, (struct sockaddr*)sock_address, sizeof(struct sockaddr))
            == -1 || setsockopt(sock,SOL_SOCKET,SO_REUSEADDR,&optval,
                                sizeof(int)) == -1) {
            freeaddrinfo(*info);
            free(info);
            return -1;
        }
        signal(SIGPIPE, sigpipe_handler);
        freeaddrinfo(*info);
        free(info);
        return sock;
    }
    else {
        return -1;
    }
}

void *process_request (void *params) {
    while (!stop_skel) { //loops until tree_skel_destroy is called
        pthread_mutex_lock(&queue_mutex);
        if (queue_head == NULL) {
            pthread_cond_wait(&cond, &queue_mutex);
        }
        if (stop_skel) {
            pthread_mutex_unlock(&queue_mutex);
            break;
        }
        struct request_t *request = queue_head;
        queue_head = queue_head->next_in_line;
        pthread_mutex_unlock(&queue_mutex);
        proc->in_progress[*(int *)params] = request->op_n;
        pthread_mutex_lock(&tree_mutex);
        if (request->op == 1) {
            tree_put(root, request->key, request->data);
        }
        else {
            tree_del(root, request->key);
        }
        pthread_mutex_unlock(&tree_mutex);
        pthread_mutex_lock(&proc_mutex);
        MessageT *msg = malloc(sizeof(MessageT));
        message_t__init(msg);
        if (msg != NULL && request->op == 1) {
            msg->opcode = MESSAGE_T__OPCODE__OP_PUT;
            msg->c_type = MESSAGE_T__C_TYPE__CT_ENTRY;
            msg->n_data = 2;
            msg->data = malloc(msg->n_data * sizeof(ProtobufCBinaryData));
            if (msg->data != NULL) {
                msg->data[0].len = (strlen(request->key) + 1) * sizeof(char);
                msg->data[0].data = (uint8_t *) request->key;
                msg->data[1].len = request->data->datasize;
                msg->data[1].data = request->data->data;
                network_send(next_server_socket, msg);
            }
            free(request->data);
        }
        else if (msg != NULL) {
            msg->opcode = MESSAGE_T__OPCODE__OP_DEL;
            msg->c_type = MESSAGE_T__C_TYPE__CT_KEY;
            msg->n_data = 1;
            msg->data = malloc(msg->n_data * sizeof(ProtobufCBinaryData));
            if (msg->data != NULL) {
                msg->data[0].len = (strlen(request->key) + 1) * sizeof(char);
                msg->data[0].data = (uint8_t *) request->key;
                network_send(next_server_socket, msg);
            }
        }
        if (request->op_n > proc->max_proc) {
            proc->max_proc = request->op_n;
        }
        pthread_mutex_unlock(&proc_mutex);
        proc->in_progress[*(int *)params] = 0;
        free(request);
    }
    return 0;
}

void recursive_free_request(struct request_t *request) {
    if (request != NULL) {
        if (request->next_in_line != NULL) {
            recursive_free_request(request->next_in_line);
        }
        if (request->data != NULL) {
            data_destroy(request->data);
        }
        free(request->key);
        free(request);
    }
}

void tree_skel_destroy() {
    stop_skel = 1;
    pthread_mutex_lock(&queue_mutex);
    pthread_cond_broadcast(&cond);
    pthread_mutex_unlock(&queue_mutex);
    for (int i = 0; i < number_threads; i++) {
        pthread_join(threads[i], NULL);
    }
    tree_destroy(root);
    free(threads);
    free(proc->in_progress);
    free(proc);
    zookeeper_close(zookeeper);
    free(zoo_node_id);
    free(next_server);
    recursive_free_request(queue_head);
}

int invoke(MessageT *msg) {
    if (root != NULL) {
        if (msg->opcode == MESSAGE_T__OPCODE__OP_SIZE && msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {
            msg->opcode = MESSAGE_T__OPCODE__OP_SIZE + 1;
            msg->c_type = MESSAGE_T__C_TYPE__CT_RESULT;
            pthread_mutex_lock(&tree_mutex);
            int size = tree_size(root);
            pthread_mutex_unlock(&tree_mutex);
            msg->n_data = 1;
            uint8_t *size_ptr = malloc(4 * sizeof(uint8_t));
            msg->data = malloc(sizeof(ProtobufCBinaryData));
            if (msg->data != NULL && size_ptr != NULL) {
                size_ptr[0] = (uint8_t)(size >>  0);
                size_ptr[1] = (uint8_t)(size >>  8);
                size_ptr[2] = (uint8_t)(size >> 16);
                size_ptr[3] = (uint8_t)(size >> 24);
                msg->data[0].len = 4 * sizeof(uint8_t);
                msg->data[0].data = size_ptr;
                return 0;
            }
        }
        else if (msg->opcode == MESSAGE_T__OPCODE__OP_HEIGHT && msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {
            msg->opcode = MESSAGE_T__OPCODE__OP_HEIGHT + 1;
            msg->c_type = MESSAGE_T__C_TYPE__CT_RESULT;
            pthread_mutex_lock(&tree_mutex);
            int height = tree_height(root);
            pthread_mutex_unlock(&tree_mutex);
            msg->n_data = 1;
            uint8_t *height_ptr = malloc(4 * sizeof(uint8_t));
            msg->data = malloc(sizeof(ProtobufCBinaryData));
            if (msg->data != NULL && height_ptr != NULL) {
                height_ptr[0] = (uint8_t)(height >>  0);
                height_ptr[1] = (uint8_t)(height >>  8);
                height_ptr[2] = (uint8_t)(height >> 16);
                height_ptr[3] = (uint8_t)(height >> 24);
                msg->data[0].len = 4 * sizeof(uint8_t);
                msg->data[0].data = height_ptr;
                return 0;
            }
        }
        else if (msg->opcode == MESSAGE_T__OPCODE__OP_DEL && msg->c_type == MESSAGE_T__C_TYPE__CT_KEY &&
                msg->n_data == 1) {
            struct request_t *request = malloc(sizeof(struct request_t));
            if (request != NULL) {
                pthread_mutex_lock(&assign_mutex);
                request->op_n = last_assigned;
                last_assigned++;
                pthread_mutex_unlock(&assign_mutex);
                int op_n = request->op_n;
                request->op = 0;
                request->key = (char*)msg->data[0].data;
                request->data = NULL;
                request->next_in_line = NULL;
                pthread_mutex_lock(&queue_mutex);
                if (queue_head == NULL) {
                    queue_head = request;
                }
                else {
                    struct request_t *queue_request = queue_head;
                    while (queue_request->next_in_line != NULL) {
                        queue_request = queue_request->next_in_line;
                    }
                    queue_request->next_in_line = malloc(sizeof(struct request_t));
                    *queue_request->next_in_line = *request;
                    free(request);
                }
                pthread_cond_signal(&cond);
                pthread_mutex_unlock(&queue_mutex);
                free(msg->data);
                msg->opcode = MESSAGE_T__OPCODE__OP_DEL + 1;
                msg->c_type = MESSAGE_T__C_TYPE__CT_RESULT;
                msg->n_data = 1;
                uint8_t *op_n_ptr = malloc(4 * sizeof(uint8_t));
                msg->data = malloc(sizeof(ProtobufCBinaryData));
                if (msg->data != NULL && op_n_ptr != NULL) {
                    op_n_ptr[0] = (uint8_t)(op_n >> 0);
                    op_n_ptr[1] = (uint8_t)(op_n >> 8);
                    op_n_ptr[2] = (uint8_t)(op_n >> 16);
                    op_n_ptr[3] = (uint8_t)(op_n >> 24);
                    msg->data[0].len = 4 * sizeof(uint8_t);
                    msg->data[0].data = op_n_ptr;
                    return 0;
                }
            }
        }
        else if (msg->opcode == MESSAGE_T__OPCODE__OP_GET && msg->c_type == MESSAGE_T__C_TYPE__CT_KEY &&
                msg->n_data == 1) {
            char *key = (char *)msg->data[0].data;
            msg->opcode = MESSAGE_T__OPCODE__OP_GET + 1;
            msg->c_type = MESSAGE_T__C_TYPE__CT_VALUE;
            msg->n_data = 1;
            pthread_mutex_lock(&tree_mutex);
            struct data_t *data = tree_get(root, key);
            pthread_mutex_unlock(&tree_mutex);
            free(msg->data[0].data);
            free(msg->data);
            msg->data = malloc(sizeof(ProtobufCBinaryData));
            if (data != NULL) {
                if (msg->data != NULL) {
                    msg->data[0].len = data->datasize;
                    msg->data[0].data = data->data;
                    free(data);
                    return 0;
                }
            }
            else {
                if (msg->data != NULL) {
                    msg->data[0].len = 0;
                    msg->data[0].data = NULL;
                    return 0;
                }
            }
        }
        else if (msg->opcode == MESSAGE_T__OPCODE__OP_PUT && msg->c_type == MESSAGE_T__C_TYPE__CT_ENTRY &&
                msg->n_data == 2) {
            struct data_t *value = malloc(sizeof(struct data_t));
            struct request_t *request = malloc(sizeof(struct request_t));
            if (value != NULL && request != NULL) {
                value->datasize = msg->data[1].len;
                value->data = msg->data[1].data;
                pthread_mutex_lock(&assign_mutex);
                request->op_n = last_assigned;
                last_assigned++;
                pthread_mutex_unlock(&assign_mutex);
                int op_n = request->op_n;
                request->op = 1;
                request->key = (char*)msg->data[0].data;
                request->data = value;
                request->next_in_line = NULL;
                pthread_mutex_lock(&queue_mutex);
                if (queue_head == NULL) {
                    queue_head = request;
                }
                else {
                    struct request_t *queue_request = queue_head;
                    while (queue_request->next_in_line != NULL) {
                        queue_request = queue_request->next_in_line;
                    }
                    queue_request->next_in_line = malloc(sizeof(struct request_t));
                    *queue_request->next_in_line = *request;
                    free(request);
                }
                pthread_cond_signal(&cond);
                pthread_mutex_unlock(&queue_mutex);
                free(msg->data);
                msg->opcode = MESSAGE_T__OPCODE__OP_PUT + 1;
                msg->c_type = MESSAGE_T__C_TYPE__CT_RESULT;
                msg->n_data = 1;
                uint8_t *op_n_ptr = malloc(4 * sizeof(uint8_t));
                msg->data = malloc(sizeof(ProtobufCBinaryData));
                if (msg->data != NULL && op_n_ptr != NULL) {
                    op_n_ptr[0] = (uint8_t)(op_n >> 0);
                    op_n_ptr[1] = (uint8_t)(op_n >> 8);
                    op_n_ptr[2] = (uint8_t)(op_n >> 16);
                    op_n_ptr[3] = (uint8_t)(op_n >> 24);
                    msg->data[0].len = 4 * sizeof(uint8_t);
                    msg->data[0].data = op_n_ptr;
                    return 0;
                }
            }
        }
        else if (msg->opcode == MESSAGE_T__OPCODE__OP_GETKEYS &&
                msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {
            pthread_mutex_lock(&tree_mutex);
            msg->n_data = tree_size(root);
            char **keys = tree_get_keys(root);
            pthread_mutex_unlock(&tree_mutex);
            msg->data = malloc(msg->n_data * sizeof(ProtobufCBinaryData));
            if (msg->data != NULL) {
                msg->opcode = MESSAGE_T__OPCODE__OP_GETKEYS + 1;
                msg->c_type = MESSAGE_T__C_TYPE__CT_KEYS;
                for (int i = 0; i < msg->n_data; i++) {
                    msg->data[i].len = (strlen(keys[i]) + 1) * sizeof(uint8_t);
                    msg->data[i].data = (uint8_t*)keys[i];
                }
                free(keys);
                return 0;
            }
        }
        else if (msg->opcode == MESSAGE_T__OPCODE__OP_GETVALUES &&
                msg->c_type == MESSAGE_T__C_TYPE__CT_NONE) {
            pthread_mutex_lock(&tree_mutex);
            msg->n_data = tree_size(root);
            void **values = tree_get_values(root);
            pthread_mutex_unlock(&tree_mutex);
            msg->data = malloc(msg->n_data * sizeof(ProtobufCBinaryData));
            if (msg->data != NULL) {
                msg->opcode = MESSAGE_T__OPCODE__OP_GETVALUES + 1;
                msg->c_type = MESSAGE_T__C_TYPE__CT_VALUES;
                for (int i = 0; i < msg->n_data; i++) {
                    struct data_t *value = (struct data_t *)values[i];
                    msg->data[i].len = value->datasize;
                    msg->data[i].data = value->data;
                    free(value);
                }
                free(values);
                return 0;
            }
        }
        else if (msg->opcode == MESSAGE_T__OPCODE__OP_VERIFY &&
                msg->c_type == MESSAGE_T__C_TYPE__CT_RESULT) {
            int result = verify(  (uint32_t)msg->data[0].data[0] <<  0
                                | (uint32_t)msg->data[0].data[1] <<  8
                                | (uint32_t)msg->data[0].data[2] << 16
                                | (uint32_t)msg->data[0].data[3] << 24);
            free(msg->data[0].data);
            free(msg->data);
            msg->opcode = MESSAGE_T__OPCODE__OP_VERIFY + 1;
            msg->c_type = MESSAGE_T__C_TYPE__CT_RESULT;
            msg->n_data = 1;
            uint8_t *result_ptr = malloc(4 * sizeof(uint8_t));
            msg->data = malloc(sizeof(ProtobufCBinaryData));
            if (msg->data != NULL && result_ptr != NULL) {
                result_ptr[0] = (uint8_t)(result >>  0);
                result_ptr[1] = (uint8_t)(result >>  8);
                result_ptr[2] = (uint8_t)(result >> 16);
                result_ptr[3] = (uint8_t)(result >> 24);
                msg->data[0].len = 4 * sizeof(uint8_t);
                msg->data[0].data = result_ptr;
                return 0;
            }
        }
    }
    msg->opcode = MESSAGE_T__OPCODE__OP_ERROR;
    msg->c_type = MESSAGE_T__C_TYPE__CT_NONE;
    msg->n_data = 0;
    msg->data = NULL;
    return -1;
}

int verify(int op_n) {
    if (op_n > 0 && op_n < last_assigned) {
        int found = 0;
        pthread_mutex_lock(&queue_mutex);
        struct request_t *request = queue_head;
        while (request != NULL && !found) {
            if (request->op_n == op_n) {
                found = 1;
            }
            request = request->next_in_line;
        }
        pthread_mutex_unlock(&queue_mutex);
        for (int i = 0; i < number_threads && !found; i++) {
            if (proc->in_progress[i] == op_n) {
                found = 1;
            }
        }
        if (found) {
            return -1;
        }
        else {
            return 0;
        }
    }
    return -1;
}