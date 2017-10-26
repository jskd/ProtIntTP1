#include <stdio.h>
#include <stdlib.h>
#include <string.h>

void printTab(int *tab, int size);

int main(int argc, char **argv){

	int *tab;
	int sizeTab = 0;

	int nextInt = 0;
	char buffer[64];

	while(1){
		scanf("%s", buffer);

		if(strcmp(buffer, "quit") == 0)
			break;

		tab = realloc(tab, (sizeTab+1) * sizeof(int));
		sizeTab++;

		tab[sizeTab-1] = atoi(buffer);
		printTab(tab, sizeTab);
	}

	free(tab);
	return 0;
}

void printTab(int *tab, int size){
	for(int i=0; i<size; i++){
		printf("[%d]", tab[i]);
	}

	printf("\n");
}