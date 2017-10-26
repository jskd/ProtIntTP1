#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <stdio.h>
#include <string.h>
#include <unistd.h>

int main() {
	// Création d'un socket
	int serverSock = socket(PF_INET, SOCK_STREAM, 0);
	struct sockaddr_in address_sock;

	address_sock.sin_family = AF_INET;
	address_sock.sin_port = htons(6868); // Convertion en big-endian htons for short int
	address_sock.sin_addr.s_addr = htonl(INADDR_ANY); // Convertion en big-endian htonl for long int

	// Liaison du socket à un port
	int r = bind(serverSock, (struct sockaddr *)&address_sock, sizeof(struct sockaddr_in));

	if(r == 0){
		r = listen(serverSock, 0); // Mise en écoute du socket

		while(1){
			char *token;
			struct sockaddr_in caller;
			socklen_t size = sizeof(caller);
			printf("Attente d'une connexion..\n");
			int sock = accept(serverSock, (struct sockaddr *)&caller, &size);

			if(sock >= 0){
				printf("-> Connexion du client réussie.\n");
				printf("Attente des informations..\n");
				//Nouveau client
				char buff[100];
				int recu = recv(sock, buff, 99*sizeof(char), 0);
				buff[recu]='\0';

				token = strtok(buff, " ");
				char *adresse_ip = token;
				token = strtok(NULL, " ");
				char *port = token;

				printf(" -> IP = %s Port = %s", adresse_ip, port);
				
				printf("Connexion au serveur..\n");
				// Création de la nouvelle connexion
				struct sockaddr_in adress_sock_2;
				adress_sock_2.sin_family = AF_INET;
				adress_sock_2.sin_port = htons(atoi(port));
				inet_aton(adresse_ip, &adress_sock_2.sin_addr);

				int sock2 = socket(PF_INET, SOCK_STREAM, 0);
				int r2 = connect(sock2, (struct sockaddr *)&adress_sock_2, sizeof(struct sockaddr_in));
				if(r2 != -1){
					close(sock);
					
					printf("-> Connexion réussie.\n");
					printf("Envoi de la confirmation..\n");
					char *mess = "CONFIRM\n";
					send(sock2, mess, strlen(mess)*sizeof(char), 0);

					while(1){
						char buff[100];
						int size_rec = read(sock2, buff, 99*sizeof(char));
						buff[size_rec] = '\0';
						printf("Caracteres recus : %d\n", size_rec);
						printf("Message : %s", buff);

						if(strcmp(buff, "ACKCONFIRM\n") == 0){
							printf("%s\n", "Fermeture de la connexion.");
							close(sock2);
							break;
						}
					}
				}
			}
		}
	}
	return 0;
}
