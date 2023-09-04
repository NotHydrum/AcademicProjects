#include <stdio.h>
#include <stdlib.h>
#include <time.h>

#include "main.h"

void write_line(FILE* file, char* line) {
    if (fputs(line, file) == EOF) {
        perror( "fputs" );
        exit(16);
    }
}

void write_process_stats(FILE* file, struct main_data* data) {
    write_line(file, "Process Statistics:\n");
    for (int i = 0; i < data->n_restaurants; i++) {
        char line[99];
        sprintf(line, "\tRestaurant %i prepared %i requests\n", i, data->restaurant_stats[i]);
        write_line(file, line);
    }
    for (int i = 0; i < data->n_drivers; i++) {
        char line[99];
        sprintf(line, "\tDriver %i delivered %i requests\n", i, data->driver_stats[i]);
        write_line(file, line);
    }
    for (int i = 0; i < data->n_clients; i++) {
        char line[99];
        sprintf(line, "\tClient %i received %i requests\n", i, data->client_stats[i]);
        write_line(file, line);
    }
    write_line(file, "\n");
}

void write_request_stats(FILE* file, struct main_data* data, int request) {
    char line[999];
    if (data->results[4 * request + 3].id == request) {
        struct tm *start_values = localtime(&data->results[4 * request + 3].start_time.tv_sec);
        struct tm *rest_values = localtime(&data->results[4 * request + 3].rest_time.tv_sec);
        struct tm *driver_values = localtime(&data->results[4 * request + 3].driver_time.tv_sec);
        struct tm *client_values = localtime(&data->results[4 * request + 3].client_end_time.tv_sec);
        int total_time_millis = (int) ((data->results[4 * request + 3].client_end_time.tv_nsec -
                data->results[4 * request + 3].start_time.tv_nsec) / 1.0E6);
        int total_time_secs;
        if (total_time_millis < 0) {
            total_time_millis = 1000 + total_time_millis;
            total_time_secs = client_values->tm_sec - start_values->tm_sec - 1;
        }
        else {
            total_time_secs = client_values->tm_sec - client_values->tm_sec;
        }
        sprintf(line, "Request: %i\nStatus: %c\nRestaurant ID: %i\nDriver ID: %i\nClient ID: %i\n"
                      "Created: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Restaurant time: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Driver time: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Client time (end): %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Total time: %i.%.3i\n",
                request, data->results[4 * request + 3].status,
                data->results[4 * request + 3].receiving_rest, data->results[4 * request + 3].receiving_driver,
                data->results[4 * request + 3].receiving_client, 1900 + start_values->tm_year,
                1 + start_values->tm_mon, start_values->tm_mday, start_values->tm_hour, start_values->tm_min,
                start_values->tm_sec, (int) (data->results[4 * request + 3].start_time.tv_nsec / 1.0E6),
                1900 + rest_values->tm_year, 1 + rest_values->tm_mon, rest_values->tm_mday,
                rest_values->tm_hour, rest_values->tm_min, rest_values->tm_sec,
                (int) (data->results[4 * request + 3].rest_time.tv_nsec / 1.0E6),
                1900 + driver_values->tm_year, 1 + driver_values->tm_mon,
                driver_values->tm_mday, driver_values->tm_hour, driver_values->tm_min, driver_values->tm_sec,
                (int) (data->results[4 * request + 3].driver_time.tv_nsec / 1.0E6),
                1900 + client_values->tm_year, 1 + client_values->tm_mon, client_values->tm_mday,
                client_values->tm_hour, client_values->tm_min, client_values->tm_sec,
                (int) (data->results[4 * request + 3].client_end_time.tv_nsec / 1.0E6),
                total_time_secs, total_time_millis);
        write_line(file, line);
    }
    else if (data->results[4 * request + 2].id == request) {
        struct tm *start_values = localtime(&data->results[4 * request + 2].start_time.tv_sec);
        struct tm *rest_values = localtime(&data->results[4 * request + 2].rest_time.tv_sec);
        struct tm *driver_values = localtime(&data->results[4 * request + 2].driver_time.tv_sec);
        sprintf(line, "Request: %i\nStatus: %c\nRestaurant ID: %i\nDriver ID: %i\nClient ID: N/A\n"
                      "Created: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Restaurant time: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Driver time: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Client time (end): N/A\nTotal time: N/A\n",
                request, data->results[4 * request + 2].status,
                data->results[4 * request + 2].receiving_rest, data->results[4 * request + 2].receiving_driver,
                1900 + start_values->tm_year, 1 + start_values->tm_mon, start_values->tm_mday,
                start_values->tm_hour, start_values->tm_min, start_values->tm_sec,
                (int) (data->results[4 * request + 2].start_time.tv_nsec / 1.0E6), 1900 + rest_values->tm_year,
                1 + rest_values->tm_mon, rest_values->tm_mday, rest_values->tm_hour, rest_values->tm_min,
                rest_values->tm_sec, (int) (data->results[4 * request + 2].rest_time.tv_nsec / 1.0E6),
                1900 + driver_values->tm_year, 1 + driver_values->tm_mon, driver_values->tm_mday,
                driver_values->tm_hour, driver_values->tm_min, driver_values->tm_sec,
                (int) (data->results[4 * request + 2].driver_time.tv_nsec / 1.0E6));
        write_line(file, line);
    }
    else if (data->results[4 * request + 1].id == request) {
        struct tm *start_values = localtime(&data->results[4 * request + 1].start_time.tv_sec);
        struct tm *rest_values = localtime(&data->results[4 * request + 1].rest_time.tv_sec);
        sprintf(line, "Request: %i\nStatus: %c\nRestaurant ID: %i\nDriver ID: N/A\nClient ID: N/A\n"
                      "Created: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Restaurant time: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i\n"
                      "Driver time: N/A\nClient time (end): N/A\nTotal time: N/A\n",
                request, data->results[4 * request + 1].status, data->results[4 * request + 1].receiving_rest,
                1900 + start_values->tm_year, 1 + start_values->tm_mon, start_values->tm_mday,
                start_values->tm_hour, start_values->tm_min, start_values->tm_sec,
                (int) (data->results[4 * request + 1].start_time.tv_nsec / 1.0E6), 1900 + rest_values->tm_year,
                1 + rest_values->tm_mon, rest_values->tm_mday, rest_values->tm_hour, rest_values->tm_min,
                rest_values->tm_sec, (int) (data->results[4 * request + 1].rest_time.tv_nsec / 1.0E6));
        write_line(file, line);
    }
    else {
        struct tm *start_values = localtime(&data->results[4 * request].start_time.tv_sec);
        sprintf(line, "Request: %i\nStatus: %c\nRestaurant ID: N/A\nDriver ID: N/A\nClient ID: N/A\n"
                      "Created: %i-%.2i-%.2i %.2i:%.2i:%.2i.%.3i"
                      "\nRestaurant time: N/A\nDriver time: N/A\nClient time (end): N/A\nTotal time: N/A\n",
                request, data->results[4 * request].status, 1900 + start_values->tm_year,
                1 + start_values->tm_mon, start_values->tm_mday, start_values->tm_hour, start_values->tm_min,
                start_values->tm_sec, (int) (data->results[4 * request + 1].start_time.tv_nsec / 1.0E6));
        write_line(file, line);
    }
}

void write_stats(FILE* file, struct main_data* data, int* op_counter) {
    write_process_stats(file, data);
    if (*op_counter > 0) {
        write_line(file, "Request Statistics:\n");
    }
    for (int i = 0; i < *op_counter; i++) {
        write_request_stats(file, data, i);
    }
}