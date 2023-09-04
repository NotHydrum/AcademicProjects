//Grupo: 17
//Alunos:
//  Tiago Fernandes - 55246
//  Henrique Catarino - 56278
//  Miguel Nunes - 56338

#include <arpa/inet.h>
#include <netdb.h>
#include <signal.h>
#include <sys/socket.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "client_stub-private.h"
#include "network_client.h"

const int MAX_MSG_SIZE = 256 * 1024; //256KB

void sigpipe_handler(int sig) {}

int network_connect(struct rtree_t *rtree) {
    if (rtree->head_socket != -1) {
        close(rtree->head_socket);
    }
    rtree->head_socket = socket(AF_INET, SOCK_STREAM, 0);
    struct addrinfo **head_info = malloc(sizeof(struct addrinfo **));
    if (head_info == NULL) {
        return -1;
    }
    getaddrinfo(rtree->head_address, rtree->head_port, NULL, head_info);
    if (head_info[0] == NULL || head_info[0]->ai_addr == NULL || rtree->head_socket == -1) {
        return -1;
    }
    if (rtree->tail_socket != -1) {
        close(rtree->tail_socket);
    }
    rtree->tail_socket = socket(AF_INET, SOCK_STREAM, 0);
    struct addrinfo **tail_info = malloc(sizeof(struct addrinfo **));
    if (tail_info == NULL) {
        return -1;
    }
    getaddrinfo(rtree->tail_address, rtree->tail_port, NULL, tail_info);
    if (tail_info[0] == NULL || tail_info[0]->ai_addr == NULL || rtree->tail_socket == -1) {
        return -1;
    }
    int optval = 1;
    if (connect(rtree->head_socket, head_info[0]->ai_addr,
            sizeof(struct sockaddr)) == -1 || setsockopt(rtree->head_socket, SOL_SOCKET,
            SO_REUSEADDR, &optval, sizeof(int)) == -1 ||
            connect(rtree->tail_socket, tail_info[0]->ai_addr,
                    sizeof(struct sockaddr)) == -1 || setsockopt(rtree->tail_socket, SOL_SOCKET,
                    SO_REUSEADDR, &optval, sizeof(int)) == -1) {
        freeaddrinfo(*head_info);
        free(head_info);
        freeaddrinfo(*tail_info);
        free(tail_info);
        return -1;
    }
    signal(SIGPIPE, sigpipe_handler);
    freeaddrinfo(*tail_info);
    free(tail_info);
    return 0;
}

MessageT *network_send_receive(struct rtree_t *rtree, MessageT *msg) {
    int sock;
    if (msg->opcode == MESSAGE_T__OPCODE__OP_PUT || msg->opcode == MESSAGE_T__OPCODE__OP_DEL) {
        sock = rtree->head_socket;
    }
    else {
        sock = rtree->tail_socket;
    }
    int packed_size = message_t__get_packed_size(msg);
    uint8_t buffer[packed_size];
    message_t__pack(msg, buffer);
    if (send(sock, buffer, packed_size, 0) == -1) {
        return NULL;
    }
    if (msg->opcode != MESSAGE_T__OPCODE__OP_PUT && msg->opcode != MESSAGE_T__OPCODE__OP_DEL) {
        message_t__free_unpacked(msg, NULL);
    }
    uint8_t buffer2[MAX_MSG_SIZE];
    int size = recv(sock, buffer2, sizeof(buffer2), 0);
    if (size == -1 || size == 0) {
        return NULL;
    }
    return message_t__unpack(NULL, size, buffer2);
}

int network_close(struct rtree_t * rtree) {
    if (close(rtree->head_socket) == 0 && close(rtree->tail_socket) == 0) {
        return 0;
    }
    return -1;
}