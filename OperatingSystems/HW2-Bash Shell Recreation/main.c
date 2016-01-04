#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/wait.h>
#define TRUE 1
#define MAX 100

char *argv[MAX/2];
char lines[MAX];
char *argv2[MAX/2];
char line[MAX];
int numberOfArguments;
int status;
pid_t pid, died;
char *path;
char history[MAX][1000];//A char of up to 1000 inputs that will serve to store the history
int inputed_commands;
char* home;

char** paths;
int num_paths;
void sigchld_handler(int signo)
{
    //see if any child processes have terminated.
    int status;
    pid_t pid;
    while ((pid = waitpid(-1, &status, WNOHANG)) > 0)
    {
        printf("[process %d completed]\n", pid);
    }
}


//Reads from the user line
void readLine(void) {
  int i = 0;
  while ((i < MAX) && ((line[i] = getchar()) != '\n')) {
      i++;
  }
  if (i == MAX) {
      line[MAX-1] = '\0';
      while (getchar() != '\n');
  } else {
      line[i] = '\0';
  }
  strcpy(lines, line);
  argv[0] = strtok(line, " ");
  numberOfArguments = 1;
  argv[numberOfArguments] = strtok(NULL, " ");
  while (argv[numberOfArguments] != NULL) {
      numberOfArguments++;
      argv[numberOfArguments] = strtok(NULL, " ");
  }
}
void readLines(char * lines) {
    argv[0] = strtok(lines, " ");
    numberOfArguments = 1;
    argv[numberOfArguments] = strtok(NULL, " ");
    while (argv[numberOfArguments] != NULL) {
    numberOfArguments++;
    argv[numberOfArguments] = strtok(NULL, " ");
  }
}

void print_history()
{
    int i;
    for(i=0;i<inputed_commands;i++)
    {
        printf("%03d\t",i+1);
        printf("%s\n",history[i]);
    }
}

int search_history(char *cLine)
{
    char *command_line = cLine + 1;
    int length = strnlen(cLine, 100);
    --length;
    int i;
    for (i = inputed_commands; i >= 0; --i)
    {
        //makes a new string the same length as the
        //string passed to this function in order to
        //compare them
        char *new = malloc(sizeof(char)*length+1);
        strncpy(new, history[i], length);
        new[length] = '\0';
        if(strcmp(command_line, new) == 0){
            printf(">>> %s\n", history[i]);
            command_line = history[i];
            readLines(command_line);
            return 0;
        }
    }
    printf("ERROR: no command with '%s' prefix in history\n", command_line);
    return 1;
}

//Splits one command with arguments into two commands with arguments
void extractCommand(char* symbol) {
  int i = 0;
  int count = 0;
  for (i = 0; argv[i] != NULL; i++) {
      if (!strcmp(argv[i], symbol)) {
         argv[i] = NULL;
         while (argv[i+1] != NULL) {
               argv2[count] = argv[i+1];
               argv[i+1] = NULL;
               i++;
               count++;
         }
      }
  }
} 
//see if the file can be read, if it cant then it
int can_write(char * str)
{
    FILE *file;
    if ((file = fopen(str, "r")) == NULL) {
        if (errno == ENOENT) {
            return 1;
        } else {
            // Check for other errors too, like EACCES and EISDIR
            return 1;
        }
    } else {
        fclose(file);
        return 0;
    }
    return 1;
}

void appendRedirection(void) {
    if ((pid = fork()) == -1) {
      perror("fork"); 
      exit(1);
    }
   if (pid == 0) {
    if(can_write(argv2[0])==1) {
      printf("ERROR: no such file to append to (%s)\n", argv2[0]);
    }
    else {
      execvp(argv[0], argv); 
      perror("execv");
    }
      exit(1);
  }
  if (pid != 0) {
      wait(NULL);
  }
}
//Redirects the standard output into a filename
void outRedirection(void) {
  if ((pid = fork()) == -1) {
      perror("fork"); 
      exit(1);
  }
  if (pid == 0) {
      
            if(can_write(argv2[0]) ==1)
                {
                    printf("ERROR: no such file read from (%s) creating",argv2[0]);
                }
        if((NULL==freopen(argv2[0],"w",stdout)))
          perror("Redirect Error");
      execvp(argv[0], argv); 
      perror("execv");
      exit(1);
  }
  if (pid != 0) {
      wait(NULL);
  }
}

//Reads from a file as standard input
void inRedirection(void) {
  if ((pid = fork()) == -1) {
      perror("fork"); 
      exit(1);
  }
  if (pid == 0) {
      close(0);
            if(can_write(argv2[0]) ==1)
                {
                    printf("ERROR: no such file write to (%s)",argv2[0]);
                }
      //open the file argv2[0] and use it as standard input
      open(argv2[0], O_RDONLY); 
      execv(argv[0], argv); 
      execvp(argv[0], argv); 
      perror("execv");
      exit(1);
  }
  if (pid != 0) {
      wait(NULL);
  }
}

 //Function where a child forks again and creates another child
void backgrounding(void) {
  signal(SIGCHLD, sigchld_handler);
  switch (pid = fork()) {
    case -1:
      // something went wrong
      printf("panic: can't fork\n");
      exit(-1);
    case 0:
      // this is the code the grandchild runs
      execv(argv[0], argv); 
      execvp(argv[0], argv); 
      printf("ERROR: command %s not found\n", argv[0]);
      exit(1);
    default:
      // this  is the code the child runs => it just exits, so
      // the call to wait() in the parent will return;
      // the grandchild will be adopted by the init process.
      printf( "[process running in background with pid %d]\n", pid );
      usleep(10000); // sleep 10000 micro seconds
      //printf("[process %d completed]\n", pid );
      exit(0);
  }
}

//Connects the standard output of one command to standard input of another command
void piping(void) {
  int fd[2];
  if (pipe(fd) < 0)
     printf("Cannot get a pipe\n");
  if ((pid = fork()) == 0) {
     close(1);                 
     dup(fd[1]);
     close(fd[0]);
     execv(argv[0], argv); 
     execvp(argv[0], argv); 
  } else if (pid > 0) {
            close(0);
            dup(fd[0]);
            close(fd[1]);
            execv(argv[0], argv); 
            execvp(argv2[0], argv2);
         } else 
            printf("Unable to fork\n");                                         
}

//Executes a simple command or calls the piping or backgrounding function
void process(int type) {
  switch (pid = fork()) {
    case -1:
      printf("panic: can't fork\n");
      exit(-1);
    case 0: 
      if (type == 0) {
          execv(argv[0], argv);
          execvp(argv[0], argv);
          printf("ERROR: command '%s' not found\n", argv[0]);
          exit(0);
      } else if (type == 1)
          backgrounding();
      else if (type == 2)
          piping();
    default:
      died = wait(&status);
    }
}

//Sets a new environment variable or overwrites an existing environment variables
void setEnv(int n) {
  int returnValue;
  char* name;
  char* value;
  name = strtok(argv[n], "=");
  value = strtok(NULL, " ");
  returnValue = setenv(name, value, 1);
  if (returnValue == 0)
      printf("Environment set successfully\n");
  else
      printf("Error in setting environment\n");
}

//Deletes an environment variable from the environment
void unsetEnv(void) {
  int returnValue;
  returnValue = unsetenv(argv[1]);
  if (returnValue == 0)
      printf("Environment variable %s unset successfully\n", argv[1]);
  else
      printf("Error in unsetting environment\n");
}

//Changes the current directory
void changeDirectory(void) {
  setenv("OLDPWD", getenv("PWD"), 1);
  printf("Current directory: %s\n", getenv("PWD"));
}

void removeAppend(void) {
    int i;
    for (i = 0; argv[i] != '\0'; i++) {

      if (!strcmp(argv[i], "==>")) {
         argv[i]= ">";
      }
      if (!strcmp(argv[i], "-->")) {
         argv[i]= "a";
      }
      if (!strcmp(argv[i], "<==")) {
         argv[i]= "<";
      } 
  }
}
//Checks whether the array of pointers to strings contains a special symbol
char stringCompare(void) {
  int i;
  for (i = 0; argv[i] != '\0'; i++) {
      if (!strcmp(argv[i], ">"))
         return argv[i][0];
      if (!strcmp(argv[i], "<"))
         return argv[i][0];
      if (!strcmp(argv[i], "|"))
         return argv[i][0];
      if (!strcmp(argv[i], "&"))
         return argv[i][0];
      if(!strcmp(argv[i], "a")) {
         return argv[i][0];
      }
  }
  return '\0';
}
int main(void) { 
    //print out a welcome message and the contents of $MYPATH
printf( "Welcome to myshell\n" );
  char * my_path;
  my_path = getenv("MYPATH");
  if (my_path!=NULL) {
      char * cd = ":.";
      strcat(my_path,cd);
      printf ("($MYPATH is: %s)\n",my_path);
  }
  else {
      my_path = (char*) malloc(6*sizeof(char));
      my_path = ":.";
      printf ("($MYPATH is: %s)\n",my_path);
  }
  inputed_commands=0;

  while (TRUE) {
      int flagHistory = 0;
      printf("myshell$ ");
      readLine();
      //Continue if only the "Enter" key was pressed
      if (argv[0] == NULL)
         continue;
      //Exit from shell if command is equal "exit"
      if (!strcmp(argv[0], "exit")) {
        printf("bye\n");
        exit(0);
      }
      if (!strcmp(argv[0], "quit")){
        printf("bye\n");
        exit(0);
      }
      strcpy(history[inputed_commands],lines);
      inputed_commands++;
      if(!strcmp(argv[0], "history")) {
        flagHistory = 1;
        print_history();
        continue;
      }
      char temp = argv[0][0];
      if (temp == '!') {
         flagHistory = search_history(line);
      }
      
      removeAppend();
      //Check for environment related commands (i.e. export USER=test, USER=test, unset USER)
      if (!strcmp(argv[0], "export"))
        setEnv(1);
      else if (strchr(argv[0], '='))
        setEnv(0);
      else if (!strcmp(argv[0], "unset"))
        unsetEnv();
        
      //Calls the function for changing the current directory
      else if (!strcmp(argv[0], "cd"))
        changeDirectory();
      else if(flagHistory==1)
        continue;
      //Calls the function for redirection, piping, backgrounding or for executing commands 
      else switch (stringCompare()) {
              case '<':           
                        extractCommand("<");
                        inRedirection();
                        break;
              case '>':            
                        extractCommand(">");
                        outRedirection();
                        break;
              case 'a':            
                        extractCommand("a");
                        appendRedirection();
                        break;
              case '|':            
                        extractCommand("|");
                        process(2);
                        break;
                        
              case '&':            
                        extractCommand("&");
                        process(1);
                        break;
              
              default: process(0);
                       break;
       }
    }
    return 0;
}
