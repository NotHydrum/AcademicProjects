//Grupo: 17
//Alunos:
//  Tiago Fernandes - 55246
//  Henrique Catarino - 56278
//  Miguel Nunes - 56338

#include <signal.h>
#include <stdio.h>

#include "network_server.h"
#include "network_server-private.h"
#include "tree_skel-private.h"

int main(int argc, char *argv[]) {
    signal(SIGINT, sigint_handler);
    if (argc == 3) {
        if (tree_skel_init(1) == 0) {
            short port;
            sscanf(argv[1], "%hd", &port);
            int sock = network_server_init(port);
            if (sock == -1) {
                printf("Error initializing server.\n");
            }
            else if (zookeeper_connect(argv[2], argv[1]) == -1) {
                printf("Error connecting to Zookeeper.\n");
            }
            else if(network_main_loop(sock) == -1) {
                printf("Error while connected to client.\n");
            }
            network_server_close();
            tree_skel_destroy();
        }
        else {
            printf("Error initializing data tree.\n");
        }
    }
    else {
        printf("Wrong arguments.\nUse \"tree-server <server_port> <zoo_address>:<zoo_port>\"\n");
    }
}