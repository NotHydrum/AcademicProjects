#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "metime.h"

FILE* open_file_w(char* file_name) {
    FILE* file = fopen(file_name, "w");
    if (file == NULL) {
        perror("Error: fopen\n");
        exit(14);
    }
    return file;
}

void write_log(FILE* file, char* args) {
    struct timespec time;
    get_time(&time);
    struct tm *values = localtime(&time.tv_sec);
    char line[99];
    sprintf(line, "%i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i %s\n", 1900 + values->tm_year,
            1 + values->tm_mon, values->tm_mday, values->tm_hour, values->tm_min, values->tm_sec,
            (int) (time.tv_nsec / 1.0E6), args);
    if (fputs(line, file) == EOF) {
        perror( "fputs" );
        exit(15);
    }
}
