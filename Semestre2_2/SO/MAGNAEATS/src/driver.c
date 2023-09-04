#include "main.h"
#include "metime.h"

void driver_receive_operation(struct operation* op, struct communication_buffers* buffers,
        struct main_data* data, struct semaphores* sems) {
    consume_begin(sems->rest_driv);
    read_rest_driver_buffer(buffers->rest_driv, data->buffers_size, op);
}

void driver_process_operation(struct operation* op, int driver_id, struct main_data* data, int* counter,
        struct semaphores* sems) {
    op->receiving_driver = driver_id;
    op->status = 'D';
    *counter = *counter + 1;
    semaphore_mutex_lock(sems->results_mutex);
    data->results[4 * op->id + 2] = *op;
    semaphore_mutex_unlock(sems->results_mutex);
}

void driver_send_answer(struct operation* op, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    produce_begin(sems->driv_cli);
    write_driver_client_buffer(buffers->driv_cli, data->buffers_size, op);
    produce_end(sems->driv_cli);
}

int execute_driver(int driver_id, struct communication_buffers* buffers, struct main_data* data,
        struct semaphores* sems) {
    int counter = 0;
    struct operation op = {-1};
    while (*data->terminate == 0) {
        driver_receive_operation(&op, buffers, data, sems);
        if (op.id != -1) {
            consume_end(sems->rest_driv);
            get_time(&op.driver_time);
            driver_process_operation(&op, driver_id, data, &counter, sems);
            if (op.requesting_client < data->n_clients) { //this if is to avoid unnecessarily writing to buffer
                                                          //operations which would never be consumed by any
                                                          //client but would keep processes awake due to
                                                          //something existing in said buffer
                driver_send_answer(&op, buffers, data, sems);
            }
        }
        else {
            produce_end(sems->rest_driv);
        }
    }
    return counter;
}

