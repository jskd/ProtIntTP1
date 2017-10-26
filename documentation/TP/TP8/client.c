#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/types.h>
#include <sys/uio.h>
#include <unistd.h>
#include <fcntl.h>
#include "tutor.h"

#define ARGSEP " \t\n"

int execute_command(char *cmd);
int tokenize_command(char* argl, char** argv);

int sock = 0;

int main() {

  struct sockaddr_in adress_sock;
  adress_sock.sin_family = AF_INET;
  adress_sock.sin_port = htons(_SRV_PORT);
  inet_aton("127.0.0.1", &adress_sock.sin_addr);

  sock = socket(PF_INET, SOCK_STREAM, 0);
  printf("Connexion au serveur..\n");
  int r = connect(sock, (struct sockaddr *)&adress_sock, sizeof(struct sockaddr_in));

  if(r == 0){    

    char buff[_RPLY_MAXMSGS * _MSG_MAXLENGTH];
    int recu = recv(sock, buff, ((_RPLY_MAXMSGS * _MSG_MAXLENGTH)-1)*sizeof(char), 0); 
    buff[recu] = '\0';
    printf("%s", buff);

    while(1){
      
      char cmd[_MSG_MAXLENGTH];
      printf("$ ");
      fgets(cmd, _MSG_MAXLENGTH, stdin);
      
      if(execute_command(cmd) == -1)
        break;
    }
  }
  else{
    printf("Probleme de connexion!\n");
  }
  close(sock);

  return 0;
}

int tokenize_command(char* argl, char** argv) {
  int i;
  argv[0] = strtok(argl, ARGSEP);
  for (i = 0; argv[i] != NULL; ++i)
      argv[i+1] = strtok(NULL, ARGSEP);
  return i;
}

int execute_command(char *cmd){
  int argc;            
  char* argv[_MSG_MAXLENGTH/2];
  argc = tokenize_command(cmd, argv);

  if(strcmp(argv[0], _CMD_LIST) == 0){
    send(sock, cmd, strlen(cmd)*sizeof(char), 0);

    char buff[_RPLY_MAXMSGS * _MSG_MAXLENGTH];
    int recu = recv(sock, buff, ((_RPLY_MAXMSGS * _MSG_MAXLENGTH)-1)*sizeof(char), 0); 
    buff[recu] = '\0';

    int nb_tutors = atoi(buff);

    char *confirm = "NB_RECEIVED";
    send(sock, confirm, _MSG_MAXLENGTH*sizeof(char), 0);

    for(int i=0; i<nb_tutors; i++){
      recu = recv(sock, buff, _MSG_MAXLENGTH*sizeof(char), 0); 
      buff[recu] = '\0';
      printf("-> %s", buff);
    }

    return 0;
  }
  if(strcmp(argv[0], _CMD_ACQ_I) == 0){
    return 0;
  }
  if(strcmp(argv[0], _CMD_ACQ_S) == 0){
    return 0;
  }
  if(strcmp(argv[0], _CMD_REL) == 0){
    return 0;
  }
  if(strcmp(argv[0], _CMD_QUIT) == 0){
    return -1;
  }
  return 0;
}