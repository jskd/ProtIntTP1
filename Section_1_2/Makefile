JAVAC=javac
JVM = java

SRC_PATH = src/
BIN_PATH = bin/

SERV_TCP = ServeurTCP
CLIENT_TCP = ClientTCP

SERV_UDP = ServeurUDP
CLIENT_UDP = ClientUDP

NAME = TP1_Lefranc_Skoda

all:
	$(JAVAC) $(SRC_PATH)*/*.java -d $(BIN_PATH)

clean:
	rm -f $(BIN_PATH)*.class

runservtcp:
	$(JVM) -classpath $(BIN_PATH) $(SERV_TCP)

runclitcp:
	$(JVM) -classpath $(BIN_PATH) $(CLIENT_TCP)

runservudp:
	$(JVM) -classpath $(BIN_PATH) $(SERV_UDP)

runcliudp:
	$(JVM) -classpath $(BIN_PATH) $(CLIENT_UDP)

tar: clean
	tar cf $(NAME).tar Makefile README.txt rapport.txt bin/ src/



