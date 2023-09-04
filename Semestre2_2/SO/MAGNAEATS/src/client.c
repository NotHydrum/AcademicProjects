#include "main.h"
#include "metime.h"

void client_get_operation(struct operation* op, int client_id, struct communication_buffers* buffers,
        struct main_data* data, struct semaphores* sems) {
    consume_begin(sems->driv_cli);
    read_driver_client_buffer(buffers->driv_cli, client_id, data->buffers_size, op);
}

void client_process_operation(struct operation* op, int client_id, struct main_data* data, int* counter,
        struct semaphores* sems) {
    op->receiving_client = client_id;
    op->status = 'C';
    *counter = *counter + 1;
    semaphore_mutex_lock(sems->results_mutex);
    data->results[4 * op->id + 3] = *op;
    semaphore_mutex_unlock(sems->results_mutex);
}

int execute_client(int client_id, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int counter = 0;
    struct operation op = {-1};
    while (*data->terminate == 0) {
        client_get_operation(&op, client_id, buffers, data, sems);
        if (op.id != -1) {
            consume_end(sems->driv_cli);
            get_time(&op.client_end_time);
            client_process_operation(&op, client_id, data, &counter, sems);
        }
        else {
            produce_end(sems->driv_cli);
        }
    }
    return counter;
}

