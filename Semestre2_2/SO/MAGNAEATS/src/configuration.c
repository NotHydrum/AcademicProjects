#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "main.h"
#include "log.h"

FILE* open_file_r(char* file_name) {
    FILE* file = fopen(file_name, "r");
    if (file == NULL) {
        perror("Error: fopen\n");
        exit(11);
    }
    return file;
}

void read_config_file(FILE* file, struct main_data* data, FILE** log_file, FILE** stats_file,
                      int* alarm_time) {
    char* line[8];
    for (int i = 0; i < 8; i++) {
        line[i] = malloc(99 * sizeof(char));
    }
    for (int i = 0; i < 8; i++) {
        fgets(line[i], 99, file);
    }
    data->max_ops = atoi(line[0]);
    data->buffers_size = atoi(line[1]);
    data->n_restaurants = atoi(line[2]);
    data->n_drivers = atoi(line[3]);
    data->n_clients = atoi(line[4]);
    char* filename = malloc(99 * sizeof(char));
    strncpy(filename, line[5], strlen(line[5]) - 1);
    *log_file = open_file_w(filename);
    strncpy(filename, line[6], strlen(line[6]) - 1);
    *stats_file = open_file_w(filename);
    *alarm_time = atoi(line[7]);
    for (int i = 0; i < 8; i++) {
        free(line[i]);
    }
}

void close_file(FILE* file) {
    if (fclose(file) != 0) {
        perror("Error: fclose\n");
        exit(12);
    }
}