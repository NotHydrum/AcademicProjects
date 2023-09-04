//Grupo: 17
//Alunos:
//  Tiago Fernandes - 55246
//  Henrique Catarino - 56278
//  Miguel Nunes - 56338

#ifndef _CLIENT_STUB_PRIVATE_H
#define _CLIENT_STUB_PRIVATE_H

#include "zookeeper/zookeeper.h"

#include "sdmessage.pb-c.h"

struct rtree_t {
    zhandle_t *zookeeper;
    int zoo_connected;
    char *head_address;
    char *head_port;
    int head_socket;
    char *tail_address;
    char *tail_port;
    int tail_socket;
};

//gets the address and port of the head and tail of the server chain from zookeeper
int get_address_ports(struct rtree_t *rtree);

#endif
