#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <dirent.h>
#include <sys/stat.h>
#include <pthread.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>
long long total_bytes = 0;
int total_files = 0;
int restore = 0;
pthread_mutex_t mutex = PTHREAD_MUTEX_INITIALIZER;
struct arg_struct {
	struct dirent * current;
	struct stat buf;
};
void * mybackup (void  *arguments) {
	if (restore == 0) {
		pthread_mutex_lock( &mutex );
		struct arg_struct *args = arguments;
  		printf( "[THREAD %u] Backing up ", (unsigned int)pthread_self());
		printf( "%s...\n", args->current->d_name );

		char *st = args->current->d_name;
		char st1[100];
		strcpy(st1, st);
		char st2[100] = "./.mybackup/";
		strcat(st1, ".bak");
		strcat(st2, st1);
		struct stat sti;
		if (stat (st2, &sti) == 0) {
			//check if needs update
			//http://stackoverflow.com/questions/10446526/get-last-modified-time-of-file-in-linux
			printf("[THREAD %u] WARNING: %s exists (overwriting!)\n", (unsigned int)pthread_self(),st1);
		}
		pthread_mutex_unlock( &mutex );
		int readFile = open(st, O_RDONLY);
		int writeFile = open(st2, O_CREAT | O_TRUNC | O_WRONLY, 0660);
		void *buffer = malloc(sizeof(void) * args->buf.st_size);
		read(readFile,buffer,args->buf.st_size);
		write(writeFile,buffer,args->buf.st_size);
		close(readFile);
		close(writeFile);
		printf( "[THREAD %u] Copied ", (unsigned int)pthread_self());
		printf( "%lld bytes from %s to %s\n", (long long)args->buf.st_size, st, st1 );
		total_files++;
		total_bytes += args->buf.st_size;
	} else {
		pthread_mutex_lock( &mutex );
		struct arg_struct *args = arguments;
		char *str= args->current->d_name;
		 char new[100];
		char st[100];
		strcpy(st, str);
		int size = (int)strlen(str);
	  	strncpy(new, st, size - 4);
	  	printf( "[THREAD %u] Restoring ", (unsigned int)pthread_self());
		printf( "%s...\n", new );
		total_bytes += args->buf.st_size;
		char st1[100] = "./.mybackup/";
		char st2[100] = "./";
		strcat(st1, st);
		strcat(st2, new);
		pthread_mutex_unlock( &mutex );
		int fileread = open(st1, O_RDONLY);
		void * buffer = malloc(sizeof(void) * args->buf.st_size);
		read(fileread,buffer,args->buf.st_size);
		int filewrite = open(st2, O_CREAT | O_TRUNC | O_WRONLY, 0660);
		write(filewrite,buffer,args->buf.st_size);
		close(filewrite);
		close(fileread);
		printf( "[THREAD %u] Copied ", (unsigned int)pthread_self());
		printf( "%lld bytes from %s to %s\n", (long long)args->buf.st_size, str, new );
		total_files++;
	}
	return NULL;
} 
int main (int argc, char **argv) {
    if (argc == 2) {
		if ((strcmp(argv[1], "-r") == 0)) {
		    restore = 1;
		} else {
			printf("Invalid Command\n");
			return EXIT_FAILURE;
		}
    }
	struct stat st;
	if (stat("./.mybackup", &st) != 0) {
		mkdir("./.mybackup", S_IRWXU | S_IRWXG | S_IRWXO);
	}
	int children = 0;
	int thread, i;
	pthread_t tid[100];
	char * current_path = ".";
	if (restore == 1) {
	  current_path = "./.mybackup";
	}
	DIR * dir = opendir((char*)current_path); 
	if (dir == NULL) {
	    perror("opendir() Failed");
	    return EXIT_FAILURE;
	}
	struct dirent *current; 
	int rc = 0;
	while ((current = readdir(dir)) != NULL) {
	    struct stat buf;
	    char new[10000];
	    strcpy(new, current_path);
	    strcat(new, "/");
	    strcat(new, (char*)current->d_name);
	    rc = stat(new, &buf);
	    if (rc < 0) {
			perror("stat() Failed");
			return 1;
	    } else {
			if (S_ISREG(buf.st_mode))
			{
				struct arg_struct *args;
				args = (struct arg_struct *)malloc(sizeof(struct arg_struct));
				args->current = current;
				args->buf = buf;
				thread = pthread_create(&tid[children], NULL, mybackup, (void *)args);
				if ( thread != 0 ) {
	      				perror( "MAIN: Could not create child thread" );
	    		}
				children++;
			}
	    }
	}
	for (i = 0; i < children; i++) {
		pthread_join(tid[i], NULL);
	}
	if (restore == 0) {
		printf("Successfully backed up %d files (%lld bytes)\n", total_files, total_bytes);
	} else {
		printf("Successfully restored %d files (%lld bytes)\n", total_files, total_bytes);		
	}
	return 0;
}