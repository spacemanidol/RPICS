//Zoe Konrad
//rcs:konraz
#include <sys/errno.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <sys/stat.h>
#include <sys/select.h>
#include <sys/syscall.h>

#include <stdio.h>
#include <unistd.h>
#include <string.h>
#include <stdlib.h>

#include <netinet/in.h>
#include <arpa/inet.h>
#include <pthread.h>
#include <fcntl.h>
#include <sched.h>
#include <signal.h>
#include <semaphore.h>

pid_t gettid(void);
extern int errno;
#define BUFFER_SIZE 200000
#define NTHREADS 10

char msg[1024] = "ACK\n";
sem_t thread_sem[NTHREADS];
unsigned int client_s;

void doesStorageExist() {
    FILE * file;
    if ((file = fopen(".storage", "r")) != NULL) {
        fclose(file);
    } else {
        char cmd[100];
        sprintf(cmd,"mkdir .storage");
        system(cmd);
    }
}

int fileCheck(char * fileName) {
    char location[1024];
    strcat(location, ".storage/");
    strcat(location, fileName);
    FILE * fd;
    if ((fd = fopen(location, "r")) != NULL) {
        fclose(fd);
        return 0;
    }
    return 1;
}

void parseTheInput(char * inBuf, char * command, char * fileName, int fileSize) {
    char * test;
    char * size;
    test = strtok (inBuf, " ");
    command = test;
    test = strtok(NULL, " ");
    fileName = test;
    if (strcmp(command, "READ") == 0) {
        return;
    }
    test = strtok(NULL, " ");
    size = test;
    test = strtok(NULL, " ");
    strcpy(inBuf, test);
    fileSize = atoi(size);
}

void addFile(char * fileName, int i, int bytes, unsigned char buffer[BUFFER_SIZE],int * newSock) {
    if (fileCheck(fileName) == 0) {
        msg[0] = '\0';
        strcat(msg, "EXISTS\n");
    } else {
        char tempFileName[1024];
        int readin = 0;
        tempFileName[0] = '\0';
        strcat(tempFileName, "add_");
        strcat(tempFileName, fileName);
        FILE * file = fopen(tempFileName, "w+");
        if (file == NULL) {
            msg[0] = '\0';
            strcat(msg, "Failed to write file\n");
            send(* newSock, msg, strlen(msg), 0);
            perror("open()");
        }
        i=0;
        for (i=0;buffer[i]!='\n'; i++) {}
        ++i;
        char temp;
        for (;i < BUFFER_SIZE || readin < bytes; readin++) {
            temp = buffer[i];
            if (temp=='\0' || i == bytes){
                break;
            }
            fprintf(file, "%c", temp);
            i++;
        }
        fclose(file);
        char cmd[1024];//to move the file i have created as a temp to my extra secret lair in .storage
        cmd[0] = '\0';
        strcat(cmd, "mv ");
        strcat(cmd, tempFileName);
        strcat(cmd, " ./.storage/");
        strcat(cmd, fileName);
        system(cmd);
        printf("[thread %ld] Transferred file (%d bytes)\n", syscall(SYS_gettid), readin);
    }
    printf("[thread %ld] Sent: %s", syscall(SYS_gettid), msg);
    if (send(* newSock, msg, strlen(msg), 0) < strlen(msg)) {
        perror("Write()");
    }
}

void deleteFile(char * fileName, int * newSock) {
    if(fileCheck(fileName) == 1) {
        msg[0] = '\0';
        strcat(msg, "File Doesnt Exist\n");
    } else {
        char cmd2[1024];
        cmd2[0] = '\0';
        strcat(cmd2, "rm ./.storage/");
        strcat(cmd2, fileName);
        system(cmd2);
        char temp[1024];
        temp[0] ='\0';
        strcat(cmd2, ".storage/");
        strcat(cmd2, fileName);
        //add error message if cant delete file
        FILE * file = fopen(temp, "r+");
        msg[0] = '\0';
        if (file != NULL) {
            strcat(msg, "Failed to DELETE file\n");
            perror("delete()");
        } else {
            strcat(msg, "ACK\n");
        }
        printf("[thread %ld] Delete \"%s\" file \n", syscall(SYS_gettid), fileName);
    }
    printf("[thread %ld] Sent: %s", syscall(SYS_gettid), msg);
    if (send(* newSock, msg, strlen(msg), 0) < strlen(msg)) {
        perror("Write()");
    }
}

void readFile(char * fileName,int *newsock) {
    int final = 0;
    if(fileCheck(fileName) == 1) {
        msg[0] = '\0';
        strcat(msg, "NO SUCH FILE");
    } else{
        int c;
        int size = 0;
        char path[1024];
        path[0] = '\0';
        strcat(path, ".storage/");
        strcat(path, fileName);
        FILE * file;
        if ((file = fopen(path, "r")) == NULL) {
            perror("file open()");
        }
        while ((c = fgetc(file)) != EOF) {
            size += 1;
        }
        fclose(file);
        final = size;
        char msg2[size + 1024];
        msg2[0] = '\0';
        strcat(msg2, "ACK ");
        char temp[1024];
        temp[0] = '\0';
        sprintf(temp, "%d", size);
        strcat(msg2, temp);
        strcat(msg2, "\n\0");
        if ((file = fopen(path, "r")) == NULL) {
            perror("file open()");
        }
        size = strlen(msg2);
        while ((c = fgetc(file)) != EOF) {
            msg2[size] = c;
            size++;
        }
        fclose(file);
        msg2[size] = '\0';
        printf("[thread %ld] Sent: ACK %d\n", syscall(SYS_gettid), final);
        printf("[thread %ld] Transferred file (%d bytes)\n", syscall(SYS_gettid), final);
        if (send( *newsock, msg2, strlen( msg2 ), 0 ) < strlen( msg2 ) ) {
            perror( "Write()" );     
        }
    }
    if (send( *newsock, msg, strlen( msg ), 0 ) < strlen( msg ) ) {
        perror( "Write()" );     
    }
}
void * solveinput(void *arg) {
    int returnmessage = 0;
    int i = 0;
    unsigned char buffer[BUFFER_SIZE];
    int * newsock = (int *)arg;
    char fileName[1024];
    int bytessize = 0;
    while(1) {
        char c;
        ssize_t x = recv(*newsock, &c, 1, MSG_PEEK);
        if (x > 0) {
            returnmessage = recv(*newsock, buffer, BUFFER_SIZE - 1, 0);
            if (returnmessage < 1) {
                perror("recv()");
            } else {
                buffer[returnmessage] = '\0';
                unsigned char input[BUFFER_SIZE];
                for(i = 0; buffer[i] != '\n'; i++) {
                    input[i] =  buffer[i];
                }
                input[i]='\0';
                char command[10];
                for(i = 0; input[i] != ' '; i++) {
                    command[i] = input[i];
                }
                command[i] = '\0';
                int temp=0;
                i++;
                for(;input[i] == ' ' || input[i] != '\n'; i++) {
                    fileName[temp] = input[i];
                    if (fileName[temp] == ' ') {
                        break;
                    }
                    temp++;
                    if (temp > 1024) {
                        break;
                    }
                }
                fileName[temp] = '\0';
                temp = 0;
                i++;
                char sizetemp[1024];
                sizetemp[temp] = '\0';
                bytessize = atoi(sizetemp);
                i++;
                printf("[thread %ld] Rcvd: %s\n", syscall(SYS_gettid), input);
                if (strcmp(command, "ADD") == 0) {
                    addFile(fileName, i, bytessize, buffer, newsock);
                } else if(strcmp(command, "DELETE") == 0){
                    deleteFile(fileName, newsock);
                } else if(strcmp(command, "READ") == 0){
                    readFile(fileName, newsock);
                } else {
                    msg[0] = '\0';
                    strcat(msg, "Error invalid command: ");
                    strcat(msg, command);
                    strcat(msg,"\n");
                    if (send( *newsock, msg, strlen( msg ), 0 ) < strlen( msg ) ) {
                         perror( "Write()" );     
                    }
                    printf("[thread %ld] Sent: %s\n", syscall(SYS_gettid), msg);
                }
            }
        } else if (x == 0) {
            printf("[thread %ld] Client closed its socket....terminating\n", syscall(SYS_gettid));
            break;       
        } else {
            perror("recv()");
            break;
        }
    }
    return NULL;
}
int main(int argc, char * argv[]){
    if(argc!=2) {
        perror("usage ./<filename> port \n");
        exit(0);
    }
    unsigned short port = atoi(argv[1]);
    if(port <= 7999|| port >= 9001) {
        perror("pick a valid port number with a range of 8000 to 9000 DAWG\n");
        exit(0);
    }
    doesStorageExist();
    int sock, newsock, len, pid;
    unsigned int fromlen;
    struct sockaddr_in server_addr;
    struct sockaddr_in client_addr;
    sock = socket(PF_INET, SOCK_STREAM, 0);
    if (sock < 0) {
        perror("socket()");
        exit(0);
    }
    server_addr.sin_family = PF_INET;
    server_addr.sin_addr.s_addr = INADDR_ANY;
    server_addr.sin_port = htons(port);
    len = sizeof(server_addr);
   
    if (bind(sock, (struct sockaddr *)&server_addr, len) < 0) {
        perror("bind()");
        exit(0);
    }
    printf("Started file-server\n");
    fromlen = sizeof(client_addr);
    listen(sock, 5);
    printf("Listening on port %d\n", port);
    while (1) {
        newsock = accept(sock, (struct sockaddr *)&client_addr, &fromlen);
        fflush(NULL);
        pid = fork();
        if (pid == 0) {
            printf("Received incoming connection from %s\n", inet_ntoa((struct in_addr)client_addr.sin_addr));
            pthread_t tid;
            int err;
            err = pthread_create(&(tid), NULL, solveinput, &newsock);
            if (err != 0) {
                perror("pthread_create()");
            } else {
                sleep( 5 );
            }   
        }
    }
    exit(1);
}