#include <stdio.h>
#include <stdlib.h>
#include <time.h>

void get_time(struct timespec *timer) {
    if (clock_gettime(CLOCK_REALTIME, timer) == -1) {
        perror( "clock_gettime" );
        exit(13);
    }
}