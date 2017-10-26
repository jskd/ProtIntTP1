CC = gcc
CFLAGS = -Wall

all: client server

client: client.o 
	$(CC) -o $@ $^

server: server.o
	$(CC) -o $@ $^


%.o: %.c
	$(CC) -o $@ -c $< $(CFLAGS)

clean:
	rm -rf *.o

mrproper: clean
	rm -rf $(EXEC)

run_server:
	./server

run_client:
	./client