/**
 *  Demo Tool
 *
 */

#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <sys/time.h>

void handler(int signum)
{
    exit(1);
}

int main(int argc, char **argv)
{
    char buff[1024];
    char *token;
    int count = 0;
    struct timeval current_time;
    long current_millseconds;
    long save_millseconds;

    if(SIG_ERR == signal(SIGINT, handler)){
        printf("SIGNAL ERROR\n");
        return(0);
    }

    while(1)
    {
        gettimeofday(&current_time, NULL);
        save_millseconds = current_time.tv_sec * 1000 + current_time.tv_usec / 1000;
        printf("/common/fundamental/time/current,%ld\n", save_millseconds);
        while(1)
        {
            if(NULL == fgets(buff, 1024, stdin))return(0);
            printf("%s", buff);
            gettimeofday(&current_time, NULL);
            current_millseconds = current_time.tv_sec * 1000 + current_time.tv_usec / 1000;
            if(current_millseconds - save_millseconds > 15000)break;
        }
        printf(".\n");
        fflush(stdout);
    }

    return(0);
}

