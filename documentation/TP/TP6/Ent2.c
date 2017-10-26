#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

int main(){

	struct sockaddr_in adress_sock;
	adress_sock.sin_family = AF_INET;
	adress_sock.sin_port = htons(6868);
	inet_aton("127.0.0.1", &adress_sock.sin_addr);

	int sock = socket(PF_INET, SOCK_STREAM, 0);
	printf("Connexion au serveur..\n");
	int r = connect(sock, (struct sockaddr *)&adress_sock, sizeof(struct sockaddr_in));

	if(r != -1){
		printf(" -> Connexion réussie.\n");
		printf("Envoi des informations..\n");
		char *mess = "127.0.0.1 6969\n";
		send(sock, mess, strlen(mess) * sizeof(char), 0);

		close(sock);
	}
	else{
		exit(1);
	}

	int serverSock = socket(PF_INET, SOCK_STREAM, 0);
	struct sockaddr_in adress_sock_2;
	adress_sock_2.sin_family = AF_INET;
	adress_sock_2.sin_port = htons(6969);
	adress_sock_2.sin_addr.s_addr = htonl(INADDR_ANY);

	int r2 = bind(serverSock, (struct sockaddr *)&adress_sock_2, sizeof(struct sockaddr_in));

	if(r2 == 0){
		r2 = listen(serverSock, 0);

		struct sockaddr_in caller;
		socklen_t size = sizeof(caller);
		printf("Attente d'une connexion..\n");
		int sock2 = accept(serverSock, (struct sockaddr *)&caller, &size);

		if(sock2 >= 0){

			while(1){
				char buff[100];
				int size_rec = read(sock2, buff, 99*sizeof(char));
				buff[size_rec] = '\0';
				printf("%s\n", buff);

				if(strcmp(buff, "CONFIRM\n") == 0){
					char *rep = "ACKCONFIRM\n";
					send(sock2, rep, strlen(rep) * sizeof(char), 0);
					printf("%s\n", "Fermeture de la connexion.");
					close(sock2);
					break;
				}
			}

		}
	}

	return 0;
}