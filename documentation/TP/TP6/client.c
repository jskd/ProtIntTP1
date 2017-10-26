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

int main() {
  int sock = socket(PF_INET, SOCK_STREAM, 0); // (domain, type, protocol)
  // Déclarations des structures
  struct sockaddr_in *addressin;
  struct addrinfo *first_info;
  struct addrinfo hints;

  // Remplissage avec des 0 par défaut
  bzero(&hints,sizeof(struct addrinfo));
  // Définition des infos de famille et type de socket
  hints.ai_family = AF_INET;
  hints.ai_socktype = SOCK_STREAM;

  // Récupération des infos de connexion
  int r = getaddrinfo("lucien.informatique.univ-paris-diderot.fr", "13", &hints, &first_info);
  if(r == 0){
    if(first_info != NULL){

      addressin = (struct sockaddr_in *)first_info->ai_addr;
      int ret = connect(sock, (struct sockaddr *)addressin, (socklen_t)sizeof(struct sockaddr_in));

      if(ret == 0){
      	char buff[100];
      	int recu = read(sock, buff, 99*sizeof(char));
      	buff[recu] = '\0';
      	printf("Date : %s\n", buff);
      	struct in_addr tmp = addressin -> sin_addr;
      	char *res = inet_ntoa(tmp); // Convertion de l'adresse en string
      	printf("IP = %s\n", res);
      }
      else{
        printf("Probleme de connexion!\n");
      }
      close(sock);
    }
  }
  else{
    fprintf(stderr, "getaddrinfo: %s\n", gai_strerror(r));
    exit(1);
  }
  return 0;
}
