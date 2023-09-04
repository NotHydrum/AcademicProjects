//Grupo: 17
//Alunos:
//  Tiago Fernandes - 55246
//  Henrique Catarino - 56278
//  Miguel Nunes - 56338

#include <stdlib.h>
#include <string.h>
#include <time.h>
#include <unistd.h>

#include "zookeeper/zookeeper.h"

#include "client_stub.h"
#include "client_stub-private.h"
#include "network_client.h"

void connection_watcher(zhandle_t *zzh, int type, int state, const char *path, void *watcherCtx) {
    if (type == ZOO_SESSION_EVENT) {
        if (state == ZOO_CONNECTED_STATE) {
            ((struct rtree_t*)watcherCtx)->zoo_connected = 1;
        }
        else {
            ((struct rtree_t*)watcherCtx)->zoo_connected = 0;
        }
    }
}

int separate_address_port(char *address_port, char *address, char *port) {
    port = strchr(address_port, ':');
    address = malloc((strlen(address_port) - strlen(port) + 1) * sizeof(char));
    if (address == NULL) {
        return -1;
    }
    memset(address, '\0', strlen(address_port) - strlen(port) + 1);
    strncpy(address, address_port, strlen(address_port) - strlen(port));
    memmove(port, port + 1, strlen(port)); //deletes ":" from port
    return 0;
}

void children_watcher(zhandle_t *zzh, int type, int state, const char *path, void *watcherCtx) {
    struct rtree_t *rtree = (struct rtree_t*)watcherCtx;
    get_address_ports(rtree);
    network_connect(rtree);
}

struct rtree_t *rtree_connect(const char *address_port) {
    struct rtree_t *rtree = malloc(sizeof(struct rtree_t));
    if (rtree == NULL) {
        return NULL;
    }
    rtree->zoo_connected = 0;
    rtree->zookeeper = zookeeper_init(address_port, connection_watcher,
                               10000, 0, rtree, 0);
    printf("Connecting to Zookeeper...\n");
    while (1) {
        if (rtree->zoo_connected) {
            printf("Connection to Zookeeper established.\n");
            break;
        }
    }
    rtree->head_socket = -1;
    rtree->tail_socket = -1;
    if (get_address_ports(rtree) == 0 && network_connect(rtree) == 0) {
        return rtree;
    }
    else {
        free(rtree);
        return NULL;
    }
}

int get_address_ports(struct rtree_t *rtree) {
    struct String_vector *servers = malloc(sizeof(struct String_vector));
    if (servers == NULL || zoo_wget_children(rtree->zookeeper, "/chain", children_watcher,
                                             rtree, servers) != ZOK) {
        return -1;
    }
    char temp[99];
    for(int i = 0; i < servers->count; i++){
        for(int f = 0; f < servers->count - 1 - i; f++){
            if(strcmp(servers->data[f], servers->data[f + 1]) > 0){
                strcpy(temp, servers->data[f]);
                strcpy(servers->data[f], servers->data[f + 1]);
                strcpy(servers->data[f + 1], temp);
            }
        }
    }
    if (servers->count <= 0) {
        return -1;
    }
    char *buffer = malloc(99 * sizeof(char));
    if (buffer == NULL) {
        return -1;
    }
    int buffer_len = 99 * sizeof(char);
    char *head_server = malloc((strlen("/chain/") + strlen(servers->data[0]) + 1) * sizeof(char));
    if (head_server == NULL) {
        return -1;
    }
    strcpy(head_server, "/chain/");
    strcat(head_server, servers->data[0]);
    if (zoo_get(rtree->zookeeper, head_server, 0, buffer,
                &buffer_len, NULL) != ZOK) {
        return -1;
    }
    char *head_address_port = malloc((strlen(buffer) + 1) * sizeof(char));
    strcpy(head_address_port, buffer);
    rtree->head_port = strchr(head_address_port, ':');
    rtree->head_address = malloc(
            (strlen(head_address_port) - strlen(rtree->head_port) + 1) * sizeof(char));
    if (rtree->head_address == NULL) {
        return -1;
    }
    memset(rtree->head_address, '\0', strlen(head_address_port) - strlen(rtree->head_port) + 1);
    strncpy(rtree->head_address, head_address_port,
            strlen(head_address_port) - strlen(rtree->head_port));
    memmove(rtree->head_port, rtree->head_port + 1,
            strlen(rtree->head_port)); //deletes ":" from port
    free(buffer);
    buffer = malloc(99 * sizeof(char));
    if (buffer == NULL) {
        return -1;
    }
    char *tail_server = malloc((strlen("/chain/") +
                                strlen(servers->data[servers->count - 1]) + 1) * sizeof(char));
    if (tail_server == NULL) {
        return -1;
    }
    strcpy(tail_server, "/chain/");
    strcat(tail_server, servers->data[servers->count - 1]);
    if (zoo_get(rtree->zookeeper, tail_server, 0, buffer,
                &buffer_len, NULL) != ZOK) {
        return -1;
    }
    char *tail_address_port = malloc((strlen(buffer) + 1) * sizeof(char));
    strcpy(tail_address_port, buffer);
    rtree->tail_port = strchr(tail_address_port, ':');
    rtree->tail_address = malloc(
            (strlen(tail_address_port) - strlen(rtree->tail_port) + 1) * sizeof(char));
    if (rtree->tail_address == NULL) {
        return -1;
    }
    memset(rtree->tail_address, '\0', strlen(tail_address_port) - strlen(rtree->tail_port) + 1);
    strncpy(rtree->tail_address, tail_address_port,
            strlen(tail_address_port) - strlen(rtree->tail_port));
    memmove(rtree->tail_port, rtree->tail_port + 1,
            strlen(rtree->tail_port)); //deletes ":" from port
    return 0;
}

int rtree_disconnect(struct rtree_t *rtree) {
    if (network_close(rtree) == -1) {
        return -1;
    }
    zookeeper_close(rtree->zookeeper);
    free(rtree->head_address);
    free(rtree->tail_address);
    free(rtree);
    return 0;
}

int rtree_send_again(struct rtree_t *rtree, MessageT *msg) {
    MessageT *msg_rec = network_send_receive(rtree, msg);
    if (msg_rec != NULL && msg_rec->data != NULL &&
            ((msg->opcode == MESSAGE_T__OPCODE__OP_PUT && msg_rec->opcode == MESSAGE_T__OPCODE__OP_PUT + 1) ||
            (msg->opcode == MESSAGE_T__OPCODE__OP_DEL && msg_rec->opcode == MESSAGE_T__OPCODE__OP_DEL + 1))) {
        int op_n =   (uint32_t) msg_rec->data[0].data[0] << 0
                     | (uint32_t) msg_rec->data[0].data[1] << 8
                     | (uint32_t) msg_rec->data[0].data[2] << 16
                     | (uint32_t) msg_rec->data[0].data[3] << 24;
        message_t__free_unpacked(msg_rec, NULL);
        return op_n;
    }
    else if (msg_rec != NULL) {
        message_t__free_unpacked(msg_rec, NULL);
    }
    return -1;
}

int rtree_put(struct rtree_t *rtree, struct entry_t *entry) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_PUT;
    msg->c_type = MESSAGE_T__C_TYPE__CT_ENTRY;
    msg->n_data = 2;
    void *data = malloc(msg->n_data * sizeof(ProtobufCBinaryData));
    msg->data = data;
    if (msg->data == NULL) {
        return -1;
    }
    msg->data[0].len = (strlen(entry->key) + 1) * sizeof(char);
    msg->data[0].data = (uint8_t*)entry->key;
    msg->data[1].len = entry->value->datasize;
    msg->data[1].data = entry->value->data;
    MessageT *msg_rec = network_send_receive(rtree, msg);
    if (msg_rec != NULL && msg_rec->opcode == MESSAGE_T__OPCODE__OP_PUT + 1 && msg_rec->data != NULL) {
        int op_n =   (uint32_t)msg_rec->data[0].data[0] <<  0
                   | (uint32_t)msg_rec->data[0].data[1] <<  8
                   | (uint32_t)msg_rec->data[0].data[2] << 16
                   | (uint32_t)msg_rec->data[0].data[3] << 24;
        sleep(10);
        if (rtree_verify(rtree, op_n) == -1) {
            op_n = rtree_send_again(rtree, msg);
        }
        message_t__free_unpacked(msg, NULL);
        message_t__free_unpacked(msg_rec, NULL);
        return op_n;
    }
    else if (msg_rec == NULL) {
        int stop = time(NULL) + 10; //not using sleep() because the client is waiting for zookeeper
                                          //watcher and sleep() doesn't let watcher activate
        while (1) {
            if (time(NULL) >= stop)
                break;
        }
        int op_n = rtree_send_again(rtree, msg);
        message_t__free_unpacked(msg, NULL);
        return op_n;
    }
    message_t__free_unpacked(msg_rec, NULL);
    return -1;
}

struct data_t *rtree_get(struct rtree_t *rtree, char *key) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_GET;
    msg->c_type = MESSAGE_T__C_TYPE__CT_KEY;
    msg->n_data = 1;
    void *data = malloc(msg->n_data * sizeof(ProtobufCBinaryData));
    msg->data = data;
    msg->data[0].len = (strlen(key) + 1) * sizeof(char);
    msg->data[0].data = (uint8_t*)key;
    msg = network_send_receive(rtree, msg);
    if (msg != NULL && msg->opcode == MESSAGE_T__OPCODE__OP_GET + 1) {
        struct data_t *value = NULL;
        if (msg->data[0].len == 0) {
            value = data_create2(0, NULL);
        }
        else {
            value = data_create(msg->data[0].len);
            memcpy(value->data, msg->data[0].data, msg->data[0].len);
        }
        message_t__free_unpacked(msg, NULL);
        return value;
    }
    else if (msg != NULL) {
        message_t__free_unpacked(msg, NULL);
    }
    return NULL;
}

int rtree_del(struct rtree_t *rtree, char *key) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_DEL;
    msg->c_type = MESSAGE_T__C_TYPE__CT_KEY;
    msg->n_data = 1;
    void *data = malloc(msg->n_data * sizeof(ProtobufCBinaryData));
    msg->data = data;
    if (msg->data == NULL) {
        return -1;
    }
    msg->data[0].len = (strlen(key) + 1) * sizeof(char);
    msg->data[0].data = (uint8_t*)key;
    MessageT *msg_rec = network_send_receive(rtree, msg);
    if (msg_rec != NULL && msg_rec->opcode == MESSAGE_T__OPCODE__OP_DEL + 1 && msg_rec->data != NULL) {
        int op_n =   (uint32_t)msg_rec->data[0].data[0] <<  0
                   | (uint32_t)msg_rec->data[0].data[1] <<  8
                   | (uint32_t)msg_rec->data[0].data[2] << 16
                   | (uint32_t)msg_rec->data[0].data[3] << 24;
        sleep(10);
        if (rtree_verify(rtree, op_n) == -1) {
            op_n = rtree_send_again(rtree, msg);
        }
        message_t__free_unpacked(msg, NULL);
        message_t__free_unpacked(msg_rec, NULL);
        return op_n;
    }
    else if (msg_rec == NULL) {
        int stop = time(NULL) + 10; //not using sleep() because the client is waiting for zookeeper
                                          //watcher and sleep() doesn't let watcher activate
        while (1) {
            if (time(NULL) >= stop)
                break;
        }
        int op_n = rtree_send_again(rtree, msg);
        message_t__free_unpacked(msg, NULL);
        return op_n;
    }
    message_t__free_unpacked(msg_rec, NULL);
    return -1;
}

int rtree_size(struct rtree_t *rtree) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_SIZE;
    msg->c_type = MESSAGE_T__C_TYPE__CT_NONE;
    msg->n_data = 0;
    msg->data = NULL;
    msg = network_send_receive(rtree, msg);
    if (msg != NULL && msg->opcode == MESSAGE_T__OPCODE__OP_SIZE + 1 && msg->data != NULL) {
        int size =   (uint32_t)msg->data[0].data[0] <<  0
                   | (uint32_t)msg->data[0].data[1] <<  8
                   | (uint32_t)msg->data[0].data[2] << 16
                   | (uint32_t)msg->data[0].data[3] << 24;
        message_t__free_unpacked(msg, NULL);
        return size;
    }
    else if (msg != NULL) {
        message_t__free_unpacked(msg, NULL);
    }
    return -1;
}

int rtree_height(struct rtree_t *rtree) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_HEIGHT;
    msg->c_type = MESSAGE_T__C_TYPE__CT_NONE;
    msg->n_data = 0;
    msg->data = NULL;
    msg = network_send_receive(rtree, msg);
    if (msg != NULL && msg->opcode == MESSAGE_T__OPCODE__OP_HEIGHT + 1 && msg->data != NULL){
        int height =   (uint32_t)msg->data[0].data[0] <<  0
                     | (uint32_t)msg->data[0].data[1] <<  8
                     | (uint32_t)msg->data[0].data[2] << 16
                     | (uint32_t)msg->data[0].data[3] << 24;
        message_t__free_unpacked(msg, NULL);
        return height;
    }
    else if (msg != NULL) {
        message_t__free_unpacked(msg, NULL);
    }
    return -1;
}

char **rtree_get_keys(struct rtree_t *rtree) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_GETKEYS;
    msg->c_type = MESSAGE_T__C_TYPE__CT_NONE;
    msg->n_data = 0;
    msg->data = NULL;
    msg = network_send_receive(rtree, msg);
    if (msg != NULL && msg->opcode == MESSAGE_T__OPCODE__OP_GETKEYS + 1) {
        char** keys = malloc((msg->n_data + 1) * sizeof(char*));
        if (keys == NULL) {
            return NULL;
        }
        for (int i = 0; i < msg->n_data; i++) {
            keys[i] = malloc(msg->data[i].len);
            if (keys[i] == NULL) {
                return NULL;
            }
            strcpy(keys[i], (char*)msg->data[i].data);
        }
        keys[msg->n_data] = NULL;
        message_t__free_unpacked(msg, NULL);
        return keys;
    }
    else if (msg != NULL) {
        message_t__free_unpacked(msg, NULL);
    }
    return NULL;
}

void **rtree_get_values(struct rtree_t *rtree) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_GETVALUES;
    msg->c_type = MESSAGE_T__C_TYPE__CT_NONE;
    msg->n_data = 0;
    msg->data = NULL;
    msg = network_send_receive(rtree, msg);
    if (msg != NULL && msg->opcode == MESSAGE_T__OPCODE__OP_GETVALUES + 1){
        void** values = malloc((msg->n_data + 1) * sizeof(void*));
        if (values == NULL) {
            return NULL;
        }
        for (int i = 0; i < msg->n_data; i++) {
            values[i] = malloc(msg->data[i].len);
            if (values[i] == NULL) {
                return NULL;
            }
            memcpy(values[i], msg->data[i].data, msg->data[i].len);
        }
        values[msg->n_data] = NULL;
        message_t__free_unpacked(msg, NULL);
        return values;
    }
    else if (msg != NULL) {
        message_t__free_unpacked(msg, NULL);
    }
    return NULL;
}

int rtree_verify(struct rtree_t *rtree, int op_n) {
    MessageT *msg = malloc(sizeof(MessageT));
    message_t__init(msg);
    msg->opcode = MESSAGE_T__OPCODE__OP_VERIFY;
    msg->c_type = MESSAGE_T__C_TYPE__CT_RESULT;
    msg->n_data = 1;
    uint8_t *op_n_ptr = malloc(4 * sizeof(uint8_t));
    msg->data = malloc(sizeof(ProtobufCBinaryData));
    if (msg->data != NULL && op_n_ptr != NULL) {
        op_n_ptr[0] = (uint8_t)(op_n >>  0);
        op_n_ptr[1] = (uint8_t)(op_n >>  8);
        op_n_ptr[2] = (uint8_t)(op_n >> 16);
        op_n_ptr[3] = (uint8_t)(op_n >> 24);
        msg->data[0].len = 4 * sizeof(uint8_t);
        msg->data[0].data = op_n_ptr;
    }
    msg = network_send_receive(rtree, msg);
    if (msg != NULL && msg->opcode == MESSAGE_T__OPCODE__OP_VERIFY + 1 && msg->data != NULL){
        int done =   (uint32_t)msg->data[0].data[0] <<  0
                   | (uint32_t)msg->data[0].data[1] <<  8
                   | (uint32_t)msg->data[0].data[2] << 16
                   | (uint32_t)msg->data[0].data[3] << 24;
        message_t__free_unpacked(msg, NULL);
        return done;
    }
    else if (msg != NULL) {
        message_t__free_unpacked(msg, NULL);
    }
    return -1;
}