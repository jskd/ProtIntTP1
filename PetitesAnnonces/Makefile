JAVAC=javac
JVM = java

SRC_PATH = src/
BIN_PATH = bin/

SERV = Serveur
CLIENT = Client

NAME = Annonces_Lefranc_Skoda

all:
	$(JAVAC) $(SRC_PATH)*.java -d $(BIN_PATH)

clean:
	rm -f $(BIN_PATH)*.class

runserv:
	$(JVM) -classpath $(BIN_PATH) $(SERV)

runcli:
	$(JVM) -classpath $(BIN_PATH) $(CLIENT) ${ip}

runjar:
	$(JVM) -jar build/dist/$(NAME_JAR).jar

jar: all
	jar cvmf META-INF/MANIFEST.MF build/dist/$(NAME_JAR).jar -C $(BIN_PATH) .

tar: clean
	tar cf $(NAME).tar Makefile README.txt bin/ src/

doc:
	javadoc -encoding utf8 -docencoding utf8 -charset utf8 -d javadoc -author $(SRC_PATH)*.java


