#ifndef CONFIGURATION_H_GUARD
#define CONFIGURATION_H_GUARD

//opens file in read-only mode
FILE* open_file_r(char* filename);

//reads given config file, initializes all configurable variables and opens log and stats files
void read_config_file(FILE* file, struct main_data* data, FILE** log_file, FILE** stats_file,
        int* alarm_time);

//closes given file
void close_file(FILE* file);

#endif