#include <stdio.h>
#include <unistd.h>
#include <signal.h>

#include "main.h"

struct main_data* data_ptr;

struct communication_buffers* buffers_ptr;

struct semaphores* sems_ptr;

int* counter;

int alarm_timer;

void print_stats() {
    printf("\n");
    for (int i = 0; i < *counter; i++) {
        semaphore_mutex_lock(sems_ptr->results_mutex);
        if (data_ptr->results[4 * i + 3].id == i) {
            printf("request: %i status: C start: %ld receiving_rest: %i rest_time: %ld "
                   "receiving_driver: %i driver_time: %ld receiving_client: %i client_end_time: %ld\n", i,
                   data_ptr->results[4 * i + 3].start_time.tv_sec,
                   data_ptr->results[4 * i + 3].receiving_rest,
                   data_ptr->results[4 * i + 3].rest_time.tv_sec,
                   data_ptr->results[4 * i + 3].receiving_driver,
                   data_ptr->results[4 * i + 3].driver_time.tv_sec,
                   data_ptr->results[4 * i + 3].receiving_client,
                   data_ptr->results[4 * i + 3].client_end_time.tv_sec);
        }
        else if (data_ptr->results[4 * i + 2].id == i) {
            printf("request: %i status: D\n", i);
        }
        else if (data_ptr->results[4 * i + 1].id == i) {
            printf("request: %i status: R\n", i);
        }
        else if (data_ptr->results[4 * i].id == i) {
            printf("request: %i status: I\n", i);
        }
        semaphore_mutex_unlock(sems_ptr->results_mutex);
    }
}

void sig_handler() {
    print_stats();
    signal(SIGALRM,sig_handler);
    alarm(alarm_timer);
}

void stop_handler() {
    printf("\n");
    stop_execution(data_ptr, buffers_ptr, sems_ptr);
}

void set_alarms(struct main_data* data, struct communication_buffers* buffers, struct semaphores* sems,
        int* op_counter, int timer) {
    data_ptr = data;
    buffers_ptr = buffers;
    sems_ptr = sems;
    signal(SIGINT, stop_handler);
    counter = op_counter;
    alarm_timer = timer;
    signal(SIGALRM,sig_handler);
    alarm(alarm_timer);
}