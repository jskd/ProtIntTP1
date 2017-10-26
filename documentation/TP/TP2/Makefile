CC = gcc 
CFLAGS = -Wall
EXEC = lire

all: $(EXEC)

$(EXEC): lire.o
	$(CC) $(CFLAGS) -o $@ $^

$(OBJ_FILES): %.o : %.c
	$(CC) $(CFLAGS) -c -o $@ $^

run:
	./$(EXEC)