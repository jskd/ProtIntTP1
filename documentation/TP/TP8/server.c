#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include "tutor.h"

#define ARGSEP " \t\n"

int execute_command(char *buff);
int tokenize_command(char* argl, char** argv);

int sock = 0;
struct tutor *tutors;

int main(int argc, char **argv){
	
	init_tutors("testlist.txt", &tutors);

	// Création d'un socket
	int serverSock = socket(PF_INET, SOCK_STREAM, 0);
	struct sockaddr_in address_sock;

	address_sock.sin_family = AF_INET;
	address_sock.sin_port = htons(_SRV_PORT); // Convertion en big-endian htons for short int
	address_sock.sin_addr.s_addr = htonl(INADDR_ANY); // Convertion en big-endian htonl for long int

	// Liaison du socket à un port
	int r = bind(serverSock, (struct sockaddr *)&address_sock, sizeof(struct sockaddr_in));

	if(r == 0){
		r = listen(serverSock, 0); // Mise en écoute du socket

		while(1){
			struct sockaddr_in caller;
			socklen_t size = sizeof(caller);
			printf("Attente d'une connexion..\n");
			sock = accept(serverSock, (struct sockaddr *)&caller, &size);

			if(sock >= 0){
				printf("-> Connexion du client réussie.\n");
				char *mess = " -> Connexion réussie\n";
				send(sock, mess, strlen(mess)*sizeof(char), 0);

				char cmd[_MSG_MAXLENGTH];
				while(1){
					int recu = recv(sock, cmd, (_MSG_MAXLENGTH - 1)*sizeof(char), 0);
					cmd[recu]='\0';
					if(recu == 0) break;

					if(execute_command(cmd) == -1)
						break;
				}
			}

			close(sock);
		}
	}
	close(serverSock);

	exit(0);
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
		printf("[%s]\n", argv[0]);

		if(sock >= 0){
			int nb_tutors = countTutors(&tutors);
			char mess[_MSG_MAXLENGTH];
			sprintf(mess, "%d\n", nb_tutors);
			// Envoi du nombre de tutors
			send(sock, mess, strlen(mess)*sizeof(char), 0);

			// Attente de la confirmation
			char buff[_RPLY_MAXMSGS * _MSG_MAXLENGTH];
			int recu = recv(sock, buff, ((_RPLY_MAXMSGS * _MSG_MAXLENGTH)-1)*sizeof(char), 0); 
			buff[recu] = '\0';

			if(strcmp(buff, "NB_RECEIVED") == 0){
				struct tutor *tmp = tutors;
				// Envoi des tutors <id> <subj>
				for(int i=0; i < nb_tutors; i++){
					char rep[_MSG_MAXLENGTH];
					sprintf(rep, "%s %s\n", tmp->id, tmp->subj);
					rep[_MSG_MAXLENGTH-1] = '\0';

					send(sock, rep, _MSG_MAXLENGTH*sizeof(char), 0);
					tmp = tmp->next;
				}
			}
		}
		return 0;
	}
	if(strcmp(argv[0], _CMD_ACQ_I) == 0){
		printf("[%s,%s]\n", argv[0], argv[1]);
		return 0;
	}
	if(strcmp(argv[0], _CMD_ACQ_S) == 0){
		printf("[%s,%s]\n", argv[0], argv[1]);
		return 0;
	}
	if(strcmp(argv[0], _CMD_REL) == 0){
		printf("[%s,%s,%s]\n", argv[0], argv[1], argv[2]);
		return 0;
	}
	if(strcmp(argv[0], _CMD_QUIT) == 0){
		return -1;
	}
	return 0;
}