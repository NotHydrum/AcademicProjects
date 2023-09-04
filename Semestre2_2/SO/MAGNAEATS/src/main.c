#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

#include "process.h"
#include "configuration.h"
#include "metime.h"
#include "log.h"
#include "mesignal.h"
#include "stats.h"

FILE* log_file = NULL;

FILE* stats_file = NULL;

int alarm_time;

int main(int argc, char *argv[]) {
    struct main_data* data = create_dynamic_memory(sizeof(struct main_data));
    struct communication_buffers* buffers = create_dynamic_memory(sizeof(struct communication_buffers));
    struct semaphores* sems = create_dynamic_memory(sizeof(struct semaphores));
    sems->main_rest = create_dynamic_memory(sizeof(struct prodcons));
    sems->rest_driv = create_dynamic_memory(sizeof(struct prodcons));
    sems->driv_cli = create_dynamic_memory(sizeof(struct prodcons));

    main_args(argc, argv, data);
    create_dynamic_memory_buffers(data);
    create_shared_memory_buffers(data, buffers);
    create_semaphores(data, sems);
    launch_processes(buffers, data, sems);
    user_interaction(buffers, data, sems);
}

void main_args(int argc, char* argv[], struct main_data* data) {
    if (argc != 2) {
        printf("Use: ./bin/magnaeats <config_file>\n");
        printf("Example: ./bin/magnaeats config.txt\n");
        exit(1);
    }
    FILE* config_file = open_file_r(argv[1]);
    read_config_file(config_file, data, &log_file, &stats_file,
                     &alarm_time);
    close_file(config_file);
}

void create_dynamic_memory_buffers(struct main_data* data) {
    data->restaurant_pids = create_dynamic_memory(sizeof(int[data->n_restaurants]));
    data->driver_pids = create_dynamic_memory(sizeof(int[data->n_drivers]));
    data->client_pids = create_dynamic_memory(sizeof(int[data->n_clients]));
    data->restaurant_stats = create_dynamic_memory(sizeof(int[data->n_restaurants]));
    data->driver_stats = create_dynamic_memory(sizeof(int[data->n_drivers]));
    data->client_stats = create_dynamic_memory(sizeof(int[data->n_clients]));
}

void create_shared_memory_buffers(struct main_data* data, struct communication_buffers* buffers) {
    buffers->main_rest = create_shared_memory("main_rest",
                                              sizeof(struct rnd_access_buffer));
    buffers->rest_driv = create_shared_memory("rest_driv",
                                              sizeof(struct circular_buffer));
    buffers->driv_cli = create_shared_memory("driv_cli",
                                             sizeof(struct rnd_access_buffer));
    buffers->main_rest->ptrs = create_shared_memory("main_rest_ptrs",
                                                    sizeof(struct pointers[data->buffers_size]));
    buffers->main_rest->buffer = create_shared_memory("main_rest_buffer",
                                                      sizeof(struct operation[data->buffers_size]));
    buffers->rest_driv->ptrs = create_shared_memory("rest_driv_ptrs",
                                                    sizeof(struct pointers[data->buffers_size]));
    buffers->rest_driv->buffer = create_shared_memory("rest_driv_buffer",
                                                      sizeof(struct operation[data->buffers_size]));
    buffers->driv_cli->ptrs = create_shared_memory("driv_cli_ptrs",
                                                    sizeof(struct pointers[data->buffers_size]));
    buffers->driv_cli->buffer = create_shared_memory("driv_cli_buffer",
                                                      sizeof(struct operation[data->buffers_size]));

    data->results = create_shared_memory("results", sizeof(int[4 * data->max_ops]));
    for (int i = 0; i < 4 * data->max_ops; i++) {
        data->results[i].id = -1;
    }
    data->terminate = create_shared_memory("terminate", sizeof(int));
}

void launch_processes(struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    for (int i = 0; i < data->n_restaurants; i++) {
        data->restaurant_pids[i] = launch_restaurant(i, buffers, data, sems);
    }
    for (int i = 0; i < data->n_drivers; i++) {
        data->driver_pids[i] = launch_driver(i, buffers, data, sems);
    }
    for (int i = 0; i < data->n_clients; i++) {
        data->client_pids[i] = launch_client(i, buffers, data, sems);
    }
}

void user_interaction(struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int stop = 0;
    int counter = 0;
    set_alarms(data, buffers, sems, &counter, alarm_time);
    char help[] = "Possible actions:\n"
                  "request <client> <restaurant> <dish> - Creates an order for <dish> to restaurant"
                  " <restaurant>, for client <client>;\n"
                  "status <id> - Check status for order with id <id>;\n"
                  "stop - Stops MagnaEats execution;\n"
                  "help - Displays this menu.\n";
    printf("%s", help);
    while (!stop && !*data->terminate) {
        printf("Action: ");
        char action[100] = "";
        scanf("%s", action);
        if (*data->terminate) {
            //program terminated, do nothing
        }
        else if (strcmp(action, "request") == 0) {
            create_request(&counter, buffers, data, sems);
        }
        else if (strcmp(action, "status") == 0) {
            read_status(data, sems);
        }
        else if (strcmp(action, "stop") == 0) {
            write_log(log_file, "stop");
            stop = 1;
        }
        else if (strcmp(action, "help") == 0) {
            write_log(log_file, "help");
            printf("%s", help);
        }
        else {
            printf("Invalid interaction. Type \"help\" to display the menu.\n");
        }
    }
    if (!*data->terminate) { //if terminate is 1 then stop_execution has already been called by SIGINT
        stop_execution(data, buffers, sems);
    }
}

void create_request(int* op_counter, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int restaurant;
    int client;
    char dish[99];
    scanf("%i %i %s", &restaurant, &client, dish);
    char* log_args;
    sprintf(log_args, "request %i %i %s", restaurant, client, dish);
    write_log(log_file, log_args);
    if (*op_counter < data->max_ops) {
        struct operation op = {*op_counter, restaurant, client,
                strdup(dish), 'I', -1, -1, -1,
                0, 0, 0, 0, 0,
                0, 0, 0};
        get_time(&op.start_time);
        if (restaurant < data->n_restaurants) { //this if is to avoid unnecessarily writing to buffer
                                                //operations which would never be consumed by any restaurant
                                                //but would keep processes awake due to something existing in
                                                //said buffer
            produce_begin(sems->main_rest);
            write_main_rest_buffer(buffers->main_rest, data->buffers_size, &op);
            produce_end(sems->main_rest);
        }
        semaphore_mutex_lock(sems->results_mutex);
        data->results[4 * op.id] = op;
        semaphore_mutex_unlock(sems->results_mutex);
        printf("Request #%i created!\n", *op_counter);
        *op_counter = *op_counter + 1;
    }
    else {
        printf("Maximum number of operations reached, not requested.\n");
    }
}

void read_status(struct main_data* data, struct semaphores* sems) {
    int id;
    scanf("%i", &id);
    char* log_args = malloc(99 * sizeof(char));
    sprintf(log_args, "status %i", id);
    write_log(log_file, log_args);
    free(log_args);
    semaphore_mutex_lock(sems->results_mutex);
    if (id < data->max_ops && data->results[4 * id + 3].id == id) {
        printf("Request %i with state C requested by client %i for the dish %s to restaurant %i, "
               "made by restaurant %i, received by driver %i, has been delivered to client %i!\n",
               data->results[4 * id + 3].id, data->results[4 * id + 3].requesting_client,
               data->results[4 * id + 3].requested_dish, data->results[4 * id + 3].requested_rest,
               data->results[4 * id + 3].receiving_rest, data->results[4 * id + 3].receiving_driver,
               data->results[4 * id + 3].requesting_client);
    }
    else if (id < data->max_ops && data->results[4 * id + 2].id == id) {
        printf("Request %i with state D requested by client %i for the dish %s to restaurant %i, "
               "made by restaurant %i, received by driver %i, not yet delivered to client!\n",
               data->results[4 * id + 2].id, data->results[4 * id + 2].requesting_client,
               data->results[4 * id + 2].requested_dish, data->results[4 * id + 2].requested_rest,
               data->results[4 * id + 2].receiving_rest, data->results[4 * id + 2].receiving_driver);
    }
    else if (id < data->max_ops && data->results[4 * id + 1].id == id) {
        printf("Request %i with state R requested by client %i for the dish %s to restaurant %i, "
               "made by restaurant %i, not yet received by a driver!\n", data->results[4 * id + 1].id,
               data->results[4 * id + 1].requesting_client, data->results[4 * id + 1].requested_dish,
               data->results[4 * id + 1].requested_rest, data->results[4 * id + 1].receiving_rest);
    }
    else if (id < data->max_ops && data->results[4 * id].id == id) {
        printf("Request %i with state I requested by client %i for the dish %s to restaurant %i, "
               "not yet received by restaurant!\n", data->results[4 * id].id,
               data->results[4 * id].requesting_client, data->results[4 * id].requested_dish,
               data->results[4 * id].requested_rest);
    }
    else {
        printf("No request with id %i\n", id);
    }
    semaphore_mutex_unlock(sems->results_mutex);
}

void stop_execution(struct main_data* data, struct communication_buffers* buffers, struct semaphores* sems) {
    printf("Terminating program!\n");
    *data->terminate = 1;
    close_file(log_file);
    wakeup_processes(data, sems);
    wait_processes(data);
    destroy_semaphores(sems);
    write_statistics(data);
    destroy_dynamic_memory(sems->main_rest);
    destroy_dynamic_memory(sems->rest_driv);
    destroy_dynamic_memory(sems->driv_cli);
    destroy_dynamic_memory(sems);
    destroy_memory_buffers(data, buffers);
    exit(0); //force exit because if SIGINT is called the process will go back to original position
}

void wait_processes(struct main_data* data) {
    for (int i = 0; i < data->n_restaurants; i++) {
        data->restaurant_stats[i] = wait_process(data->restaurant_pids[i]);
    }
    for (int i = 0; i < data->n_drivers; i++) {
        data->driver_stats[i] = wait_process(data->driver_pids[i]);
    }
    for (int i = 0; i < data->n_clients; i++) {
        data->client_stats[i] = wait_process(data->client_pids[i]);
    }
}

void write_statistics(struct main_data* data) {
    int counter = 0;
    for (int i = 0; i < data->max_ops; i++) {
        if (data->results[4 * i].status == 'I') {
            counter++;
        }
    }
    write_stats(stats_file, data, &counter);
    close_file(stats_file);
}

void destroy_memory_buffers(struct main_data* data, struct communication_buffers* buffers){
    destroy_dynamic_memory(data->restaurant_pids);
    destroy_dynamic_memory(data->client_pids);
    destroy_dynamic_memory(data->driver_pids);
    destroy_dynamic_memory(data->restaurant_stats);
    destroy_dynamic_memory(data->client_stats);
    destroy_dynamic_memory(data->driver_stats);
    destroy_shared_memory("driv_cli_buffer", buffers->driv_cli->buffer,
                          sizeof(struct operation[data->buffers_size]));
    destroy_shared_memory("driv_cli_ptrs", buffers->driv_cli->ptrs,
                          sizeof(struct pointers[data->buffers_size]));
    destroy_shared_memory("rest_driv_buffer", buffers->rest_driv->buffer,
                          sizeof(struct operation[data->buffers_size]));
    destroy_shared_memory("rest_driv_ptrs", buffers->rest_driv->ptrs,
                          sizeof(struct pointers[data->buffers_size]));
    destroy_shared_memory("main_rest_buffer", buffers->main_rest->buffer,
                          sizeof(struct operation[data->buffers_size]));
    destroy_shared_memory("main_rest_ptrs", buffers->main_rest->ptrs,
                          sizeof(struct pointers[data->buffers_size]));
    destroy_shared_memory("driv_cli", buffers->driv_cli,
                          sizeof(struct rnd_access_buffer));
    destroy_shared_memory("rest_driv", buffers->rest_driv,
                          sizeof(struct circular_buffer));
    destroy_shared_memory("main_rest", buffers->main_rest,
                          sizeof(struct rnd_access_buffer));
    destroy_dynamic_memory(data);
    destroy_dynamic_memory(buffers);
}

void create_semaphores(struct main_data* data, struct semaphores* sems) {
    int pid = getpid();
    char name[25];
    sprintf(name, "main_rest_full_%i", pid);
    sems->main_rest->full = semaphore_create(name, 0);
    sprintf(name, "main_rest_empty_%i", pid);
    sems->main_rest->empty = semaphore_create(name, data->buffers_size);
    sprintf(name, "main_rest_mutex_%i", pid);
    sems->main_rest->mutex = semaphore_create(name, 1);

    sprintf(name, "rest_driv_full_%i", pid);
    sems->rest_driv->full = semaphore_create(name, 0);
    sprintf(name, "rest_driv_empty_%i", pid);
    sems->rest_driv->empty = semaphore_create(name, data->buffers_size);
    sprintf(name, "rest_driv_mutex_%i", pid);
    sems->rest_driv->mutex = semaphore_create(name, 1);

    sprintf(name, "driv_cli_full_%i", pid);
    sems->driv_cli->full = semaphore_create(name, 0);
    sprintf(name, "driv_cli_empty_%i", pid);
    sems->driv_cli->empty = semaphore_create(name, data->buffers_size);
    sprintf(name, "driv_cli_mutex_%i", pid);
    sems->driv_cli->mutex = semaphore_create(name, 1);

    sprintf(name, "results_mutex_%i", pid);
    sems->results_mutex = semaphore_create(name, 1);
}

void wakeup_processes(struct main_data* data, struct semaphores* sems) {
    for (int i = 0; i < data->buffers_size; i++) {
        produce_end(sems->main_rest);
        produce_end(sems->rest_driv);
        produce_end(sems->driv_cli);
    }
}

void destroy_semaphores(struct semaphores* sems) {
    int pid = getpid();
    char name[25];
    sprintf(name, "main_rest_full_%i", pid);
    semaphore_destroy(name, sems->main_rest->full);
    sprintf(name, "main_rest_empty_%i", pid);
    semaphore_destroy(name, sems->main_rest->empty);
    sprintf(name, "main_rest_mutex_%i", pid);
    semaphore_destroy(name, sems->main_rest->mutex);

    sprintf(name, "rest_driv_full_%i", pid);
    semaphore_destroy(name, sems->rest_driv->full);
    sprintf(name, "rest_driv_empty_%i", pid);
    semaphore_destroy(name, sems->rest_driv->empty);
    sprintf(name, "rest_driv_mutex_%i", pid);
    semaphore_destroy(name, sems->rest_driv->mutex);

    sprintf(name, "driv_cli_full_%i", pid);
    semaphore_destroy(name, sems->driv_cli->full);
    sprintf(name, "driv_cli_empty_%i", pid);
    semaphore_destroy(name, sems->driv_cli->empty);
    sprintf(name, "driv_cli_mutex_%i", pid);
    semaphore_destroy(name, sems->driv_cli->mutex);

    sprintf(name, "results_mutex_%i", pid);
    semaphore_destroy(name, sems->results_mutex);
}
