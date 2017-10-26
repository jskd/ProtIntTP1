#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int onlyNumbers(FILE* f);

int main(int argc, char **argv){

	char *name;
	int alire = 0;
	int pivot = 0;

	if(argc < 4){
		fprintf(stderr, "%s\n", "Usage: lire < fichier | -I > <alire> <pivot>");
		exit(1);
	}

	// Récuperation des arguments
	name = argv[1];
	alire = atoi(argv[2]);
	pivot = atoi(argv[3]);

	FILE* file;
	file = fopen(name, "r");

	if(file == NULL)
		exit(1);

	// Ne doit contenir que des nombres
	int res = onlyNumbers(file);
	if(res == 1)
		fprintf(stderr, "%s\n", "Le fichier ne respecte pas les règles.");
	else
		printf("%s\n", "Lecture réussie.");

	rewind(file);

	char buffer[alire];
	char ch[2048];

	while(fgets(buffer, alire+1, file) != NULL){
		strcat(ch, buffer);

		if(atoi(buffer) > pivot)
			break;
	}

	printf("%s\n", ch);
	printf("lus = %d ; lasts_char = %s\n", alire, buffer);

	fclose(file);
	exit(0);
}

int onlyNumbers(FILE* f){
	char buffer[1024];

	int error = 0;
	char *line = "";
	while((line = fgets(buffer, sizeof(buffer), f)) != NULL){

		int size = strlen(line);
		for(int i=0; i<size-1; i++){
			if( (buffer[i] + 0) > '9' || (buffer[i] + 0) < '0' ){
				error = 1;
				break;
			}	
		}

		if(error)
			return 1;
	}
	return 0;
}