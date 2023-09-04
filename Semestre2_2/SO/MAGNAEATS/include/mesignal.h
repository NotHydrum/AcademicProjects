#ifndef MESIGNAL_H_GUARD
#define MESIGNAL_H_GUARD

//sets time and interruption alarms
void set_alarms(struct main_data* data, struct communication_buffers* buffers, struct semaphores* sems,
        int* op_counter, int timer);

//time alarm (SIGALRM) handler
void sig_handler();

//interruption alarm (SIGINT) handler
void stop_handler();

//prints stats to stdout
void print_stats();

#endif
