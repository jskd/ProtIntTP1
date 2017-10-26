CC = gcc
CFLAGS = -Wall

all: client server

client: client.o tutor.o
	$(CC) -o $@ $^

server: server.o tutor.o
	$(CC) -o $@ $^

server.o: tutor.h
client.o: tutor.h
tutor.o: tutor.h

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