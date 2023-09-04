#include <stdio.h>
#include <stdlib.h>
#include <sys/wait.h>
#include <unistd.h>

#include "restaurant.h"
#include "driver.h"
#include "client.h"

int launch_restaurant(int restaurant_id, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int pid = fork();
    if (pid == 0) {
        exit(execute_restaurant(restaurant_id, buffers, data, sems));
    }
    else if (pid == -1) {
        perror("Error: fork\n");
        exit(7);
    }
    else {
        return pid;
    }
}

int launch_driver(int driver_id, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int pid = fork();
    if (pid == 0) {
        exit(execute_driver(driver_id, buffers, data, sems));
    }
    else if (pid == -1) {
        perror("Error: fork\n");
        exit(7);
    }
    else {
        return pid;
    }
}

int launch_client(int client_id, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int pid = fork();
    if (pid == 0) {
        exit(execute_client(client_id, buffers, data, sems));
    }
    else if (pid == -1) {
        perror("Error: fork\n");
        exit(7);
    }
    else {
        return pid;
    }
}

int wait_process(int process_id) {
    int counter;
    waitpid(process_id, &counter, 0);
    return(WEXITSTATUS(counter));
}