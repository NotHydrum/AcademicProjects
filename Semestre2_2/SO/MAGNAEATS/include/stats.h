#ifndef STATS_H_GUARD
#define STATS_H_GUARD

//writes stats to given stats file using other functions
void write_stats(FILE* file, struct main_data* data, int* op_counter);

//writes process stats to given stats file
void write_process_stats(FILE* file, struct main_data* data);

//writes request stats of given request to given stats file
void write_request_stats(FILE* file, struct main_data* data, int request);

//writes a single line to given file
void write_line(FILE* file, char* line);

#endif
