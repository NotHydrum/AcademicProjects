#include <stdio.h>
#include <stdlib.h>
#include <semaphore.h>
#include <fcntl.h>

#include "synchronization.h"

sem_t * semaphore_create(char* name, int value) {
    sem_t *sem = sem_open(name, O_CREAT, 0xFFFFFFFF, value);
    if (sem == SEM_FAILED) {
        perror("Error: sem_open\n");
        exit(8);
    }
    return sem;
}

void semaphore_destroy(char* name, sem_t* semaphore) {
    if (sem_close(semaphore) == -1) {
        perror("Error: sem_close\n");
        exit(9);
    }
    if (sem_unlink(name) == -1) {
        perror("Error: sem_unlink\n");
        exit(10);
    }
}

void produce_begin(struct prodcons* pc) {
    semaphore_mutex_lock(pc->empty);
    semaphore_mutex_lock(pc->mutex);
}

void produce_end(struct prodcons* pc) {
    semaphore_mutex_unlock(pc->mutex);
    semaphore_mutex_unlock(pc->full);
}

void consume_begin(struct prodcons* pc) {
    semaphore_mutex_lock(pc->full);
    semaphore_mutex_lock(pc->mutex);
}

void consume_end(struct prodcons* pc) {
    semaphore_mutex_unlock(pc->mutex);
    semaphore_mutex_unlock(pc->empty);
}

void semaphore_mutex_lock(sem_t* sem) {
    if (sem_wait(sem) == -1){
        perror("sem_wait");
    }
}

void semaphore_mutex_unlock(sem_t* sem) {
    if (sem_post(sem) == -1){
        perror("sem_post");
    }
}
