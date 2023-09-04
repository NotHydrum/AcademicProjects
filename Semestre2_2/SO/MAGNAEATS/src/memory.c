#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/mman.h>
#include <unistd.h>

#include "memory.h"

void* create_shared_memory(char* name, int size) {
    int descriptor = shm_open(name, O_RDWR | O_CREAT, 0777);
    if (descriptor == -1) {
        perror("Error: shm_open\n");
        exit(2);
    }
    int error = ftruncate(descriptor, size);
    if (error == -1) {
        perror("Error: ftruncate\n");
        exit(3);
    }
    void* pointer = mmap(NULL, size, PROT_EXEC | PROT_READ | PROT_WRITE,
                         MAP_SHARED, descriptor, 0);
    if (pointer == MAP_FAILED) {
        perror("Error: mmap\n");
        exit(4);
    }
    memset(pointer, 0, size);
    return pointer;
}

void* create_dynamic_memory(int size) {
    return memset(malloc(size), 0, size);
}

void destroy_shared_memory(char* name, void* ptr, int size) {
    int error = munmap(ptr, size);
    if (error == -1) {
        perror("Error: munmap\n");
        exit(5);
    }
    error = shm_unlink(name);
    if (error == -1) {
        perror("Error: shm_unlink\n");
        exit(6);
    }
}

void destroy_dynamic_memory(void* ptr) {
    free(ptr);
}

void write_main_rest_buffer(struct rnd_access_buffer* buffer, int buffer_size, struct operation* op) {
    int done = 0;
    for (int i = 0; i < buffer_size && !done; i++) {
        if (buffer->ptrs[i] == 0) {
            buffer->ptrs[i] = 1;
            buffer->buffer[i] = *op;
            done = 1;
        }
    }
}

void write_rest_driver_buffer(struct circular_buffer* buffer, int buffer_size, struct operation* op) {
    if (((buffer->ptrs->in + 1) % buffer_size) != buffer->ptrs->out % buffer_size) {
        buffer->buffer[buffer->ptrs->in % buffer_size] = *op;
        buffer->ptrs->in = buffer->ptrs->in + 1;
    }
}

void write_driver_client_buffer(struct rnd_access_buffer* buffer, int buffer_size, struct operation* op) {
    int done = 0;
    for (int i = 0; i < buffer_size && !done; i++) {
        if (buffer->ptrs[i] == 0) {
            buffer->ptrs[i] = 1;
            buffer->buffer[i] = *op;
            done = 1;
        }
    }
}

void read_main_rest_buffer(struct rnd_access_buffer* buffer, int rest_id, int buffer_size,
        struct operation* op) {
    int done = 0;
    for (int i = 0; i < buffer_size && !done; i++) {
        if (buffer->ptrs[i] == 1 && buffer->buffer[i].requested_rest == rest_id) {
            buffer->ptrs[i] = 0;
            *op = buffer->buffer[i];
            done = 1;
        }
    }
    if (!done) {
        op->id = -1;
    }
}

void read_rest_driver_buffer(struct circular_buffer* buffer, int buffer_size, struct operation* op) {
    if (buffer->ptrs->out < buffer->ptrs->in) {
        *op = buffer->buffer[buffer->ptrs->out % buffer_size];
        buffer->ptrs->out = buffer->ptrs->out + 1;
    }
    else {
        op->id = -1;
    }
    if (buffer->ptrs->out > buffer->ptrs->in) {
        buffer->ptrs->out = buffer->ptrs->in;
    }
}

void read_driver_client_buffer(struct rnd_access_buffer* buffer, int client_id, int buffer_size,
        struct operation* op) {
    int done = 0;
    for (int i = 0; i < buffer_size && !done; i++) {
        if (buffer->ptrs[i] == 1 && buffer->buffer[i].requesting_client == client_id) {
            buffer->ptrs[i] = 0;
            *op = buffer->buffer[i];
            done = 1;
        }
    }
    if (!done) {
        op->id = -1;
    }
}