//Zoe Konrad
//HW: 1
//rcs:konraz
//op sys hw1
//Due Wednesday 2/12
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h>
#include <dirent.h> //direcotry structs
#include <sys/stat.h>
#include <errno.h>
extern int errno;
void printPermission(struct stat statp) {
  if (statp.st_mode & S_IFDIR) {
    printf("[d");
  } 
  else if (statp.st_mode & S_IFLNK)//is a link
    printf("[l");
  else {
    printf("[-" );
  }//User Privelages
  if (statp.st_mode & S_IRUSR) {
    printf("r");
  } else {
    printf("-");
  }
  if (statp.st_mode & S_IWUSR) {
    printf("w");
  } else {
    printf("-");
  } 
  if (statp.st_mode & S_IXUSR) {
    printf("x");
  } else {
    printf("-");
  }//Group Privelages
  if (statp.st_mode & S_IRGRP) {
    printf("r");
  } else {
    printf("-");
  }
  if (statp.st_mode & S_IWGRP) {
    printf("w");
  } else {
    printf("-");
  } 
  if (statp.st_mode & S_IXGRP) {
    printf("x"); 
  } else {
    printf("-");
  }//Other's Privelages
  if (statp.st_mode & S_IROTH) {
    printf("r");
  } else {
    printf("-");
  } 
  if (statp.st_mode & S_IWOTH) {
    printf("w");
  } else {
    printf("-");
  } 
  if (statp.st_mode & S_IXOTH) {
    printf("x");
  } else {
    printf("-");
  }
  printf("]");
}
int printFilesWithPermisions(char* directoryName, int indent, char* fileName) {
  DIR * directory = opendir(directoryName);
  struct dirent *currentFile;
  struct stat statp;
  char fileNameString[1024];
  if (directory == NULL) {
    perror("failed to open directory");
    return -1;
  }
  while (( currentFile = readdir(directory))) {
    if (!(strcmp(currentFile->d_name,".") && strcmp(currentFile->d_name,".."))) {
      continue;
    }
    strcpy(fileNameString,directoryName);
    strcat(fileNameString,"/");
    strcat(fileNameString,currentFile->d_name);
    if(stat(fileNameString,&statp)) {
      perror("stat failed");
      return -1;
    } if (S_ISREG(statp.st_mode) && (!strlen(fileName) || !strcmp(currentFile->d_name,fileName))) {
      int i;
      for (i = 0; i < indent; i++) {
        printf("    ");
      } printPermission(statp);
      printf("%s (%d bytes)\n", currentFile->d_name, (int)statp.st_size);
    } else if (!S_ISREG(statp.st_mode)) {
      int i;
      for(i = 0; i < indent; i++) {
        printf("    ");
      } printPermission(statp);
      printf("%s (sub-directory)\n", currentFile->d_name);
      printFilesWithPermisions(fileNameString, indent + 1, fileName);
    }
  }
  if (errno!=0) {
    perror("readdir failed");
    return -1;
  } if (closedir(directory)) {
    perror("closedir failed");
    return -1;
  } 
  return 0;
}
int printFiles(char* directoryName, int indent, char* fileName) {
  DIR * directory = opendir(directoryName);
  struct dirent *currentFile;
  struct stat statp;
  char fileNameString[1024];
  if (directory == NULL) {
    perror("failed to open directory");
    return -1;
  }
  while (( currentFile = readdir(directory))) {
    if (!(strcmp(currentFile->d_name,".") && strcmp(currentFile->d_name,".."))) {
      continue;
    }
    strcpy(fileNameString,directoryName);
    strcat(fileNameString,"/");
    strcat(fileNameString,currentFile->d_name);
    if(stat(fileNameString,&statp)) {
      perror("stat failed");
      return -1;
    } if (S_ISREG(statp.st_mode)) {
      int i;
      for (i = 0; i < indent; i++) {
        printf("    ");
      } printf("%s (%d bytes)\n", currentFile->d_name, (int)statp.st_size);
    } else if (!S_ISREG(statp.st_mode)) {
      int i;
      for(i = 0; i < indent; i++) {
        printf("    ");
      } printf("%s (subdirectory)\n", currentFile->d_name);
      printFiles(fileNameString, indent + 1, fileName);
    }
  }
  if (errno!=0) {
    perror("readdir failed");
    return -1;
  } if (closedir(directory)) {
    perror("closedir failed");
    return -1;
  } 
  return 0;
}
int printFilesPrefixMatch(char* directoryName, int indent, char* fileName) {
  DIR * directory = opendir(directoryName);
  struct dirent *currentFile;
  struct stat statp;
  char fileNameString[1024];
  if (directory == NULL) {
    perror("failed to open directory");
    return -1;
  }
  while (( currentFile = readdir(directory))) {
    if (!(strcmp(currentFile->d_name,".") && strcmp(currentFile->d_name,".."))) {
      continue;
    }
    strcpy(fileNameString,directoryName);
    strcat(fileNameString,"/");
    strcat(fileNameString,currentFile->d_name);
    if(stat(fileNameString,&statp)) {
      perror("stat failed");
      return -1;
    }
    if (S_ISREG(statp.st_mode)) {
        int flag;
        char* currentFiles=currentFile->d_name;
        char * result =strstr(currentFiles,fileName);
        flag =0;
        if(result!=NULL) {
          flag =1;
        }
        if(flag==1){
          int k;
          for (k = 0; k< indent; k++) {
            printf("    ");
          } printPermission(statp);
          printf("%s (%d bytes)\n", currentFile->d_name, (int)statp.st_size);
        }
    }  else if (!S_ISREG(statp.st_mode)) {
      int i;
      for(i = 0; i < indent; i++) {
        printf("    ");
      } printPermission(statp);
      printf("%s (sub-directory)\n", currentFile->d_name);
      printFilesPrefixMatch(fileNameString, indent + 1, fileName);
    }
  }
  if (errno!=0) {
    perror("readdir failed");
    return -1;
  } if (closedir(directory)) {
    perror("closedir failed");
    return -1;
  } 
  return 0;
}
int main (int argc, char *argv[]) {
  if (argc==2) { 
    if( strcmp("-m", argv[1]) == 0 ) {
    return printFilesWithPermisions(".", 0, ""); 
    }
  } else if(argc ==3) { 
    return printFilesPrefixMatch(".", 0 , argv[2]);
  } else if (argc==1) {
    return printFiles(".", 0, "");
  } else {
    printf("usage ./<filename> <string> to match against\n" );
    return -1;
  }
  return 0;
}
