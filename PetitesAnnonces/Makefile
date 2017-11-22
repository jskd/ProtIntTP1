JAVAC=javac
JVM = java

SRC_PATH = src/
BIN_PATH = bin/

SERV = Serveur
CLIENT = Client

NAME = Annonces_Lefranc_Skoda

all:
	@mkdir -p $(BIN_PATH)
	$(JAVAC) $(SRC_PATH)*.java -d $(BIN_PATH)

clean:
	rm -f $(BIN_PATH)*.class

runserv:
	$(JVM) -classpath $(BIN_PATH) $(SERV)

runcli:
	$(JVM) -classpath $(BIN_PATH) $(CLIENT) ${ip}

runjars:
	$(JVM) -jar build/dist/$(SERV).jar

runjarc:
	$(JVM) -jar build/dist/$(CLIENT).jar ${ip}

jar: all
	@echo "Main-Class: Serveur\n" > META-INF/MANIFEST.MF
	jar cvmf META-INF/MANIFEST.MF build/dist/$(SERV).jar -C $(BIN_PATH) .
	@echo "Main-Class: Client\n" > META-INF/MANIFEST.MF
	jar cvmf META-INF/MANIFEST.MF build/dist/$(CLIENT).jar -C $(BIN_PATH) .

tar: clean
	tar cf $(NAME).tar Makefile README.txt bin/ src/

doc:
	javadoc -encoding utf8 -docencoding utf8 -charset utf8 -d javadoc -author $(SRC_PATH)*.java


