#include "main.h"
#include "metime.h"

void restaurant_receive_operation(struct operation* op, int rest_id, struct communication_buffers* buffers,
        struct main_data* data, struct semaphores* sems) {
    consume_begin(sems->main_rest);
    read_main_rest_buffer(buffers->main_rest, rest_id, data->buffers_size, op);
}

void restaurant_process_operation(struct operation* op, int rest_id, struct main_data* data, int* counter,
        struct semaphores* sems) {
    op->receiving_rest = rest_id;
    op->status = 'R';
    *counter = *counter + 1;
    semaphore_mutex_lock(sems->results_mutex);
    data->results[4 * op->id + 1] = *op;
    semaphore_mutex_unlock(sems->results_mutex);
}

void restaurant_forward_operation(struct operation* op, struct communication_buffers* buffers,
        struct main_data* data, struct semaphores* sems) {
    produce_begin(sems->rest_driv);
    write_rest_driver_buffer(buffers->rest_driv, data->buffers_size, op);
    produce_end(sems->rest_driv);
}

int execute_restaurant(int rest_id, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int counter = 0;
    struct operation op = {-1};
    while (*data->terminate == 0) {
        restaurant_receive_operation(&op, rest_id, buffers, data, sems);
        if (op.id != -1) {
            consume_end(sems->main_rest);
            get_time(&op.rest_time);
            restaurant_process_operation(&op, rest_id, data, &counter, sems);
            restaurant_forward_operation(&op, buffers, data, sems);
        }
        else {
            produce_end(sems->main_rest);
        }
    }
    return counter;
}

