#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <netdb.h>

int main(int argc, char** argv){

	int sock = socket(PF_INET, SOCK_DGRAM, 0);
	struct sockaddr_in address_sock;
	address_sock.sin_family = AF_INET;
	address_sock.sin_port = htons(5555);
	address_sock.sin_addr.s_addr = htonl(INADDR_ANY);
	int r = bind(sock, (struct sockaddr *)&address_sock, sizeof(struct sockaddr_in));

	struct sockaddr_in emeteur;
	socklen_t a = sizeof(emeteur);

	struct addrinfo *first_info;
	struct addrinfo hints;
	bzero(&hints, sizeof(struct addrinfo));
	hints.ai_family = AF_INET;
	hints.ai_socktype = SOCK_DGRAM;

	if(r == 0){
		char buff[1024];
		while(1){
			int rec = recvfrom(sock, buff, 1023, 0, (struct sockaddr *)&emeteur, &a);
			buff[rec] = '\0';

			char reponse[1024];
			sprintf(reponse, "%s:%d %s", inet_ntoa(emeteur.sin_addr), ntohs(emeteur.sin_port), buff);
			printf("%s", reponse);
			
			char port_emet[6];
			sprintf(port_emet, "%d", ntohs(emeteur.sin_port));

			getaddrinfo(inet_ntoa(emeteur.sin_addr), port_emet, &hints, &first_info);
			if(first_info != NULL){
				struct sockaddr *saddr = first_info->ai_addr;
				sendto(sock, reponse, strlen(reponse), 0, saddr, (socklen_t)sizeof(struct sockaddr_in));
			}
		}
	}

	exit(0);
}