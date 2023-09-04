#ifndef LOG_H_GUARD
#define LOG_H_GUARD

//opens file in write-only mode
FILE* open_file_w(char* file_name);

//writes to given log file current time and given actions
void write_log(FILE* file, char* args);

#endif
