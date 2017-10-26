JAVAC=javac
JVM = java

SRC_PATH = src/
BIN_PATH = bin/

MAIN = Hello
NAME_JAR = Hello

all:
	$(JAVAC) $(SRC_PATH)*.java -d $(BIN_PATH)

clean:
	rm -f $(BIN_PATH)*.class

run:
	$(JVM) -classpath $(BIN_PATH) $(MAIN)

runjar:
	$(JVM) -jar build/dist/$(NAME_JAR).jar

jar: all
	jar cvmf META-INF/MANIFEST.MF build/dist/$(NAME_JAR).jar -C $(BIN_PATH) .

tar: clean
	tar cf $(NAME_JAR).tar Makefile README.txt META-INF/ bin/ src/  javadoc/

doc:
	javadoc -encoding utf8 -docencoding utf8 -charset utf8 -d javadoc -author $(SRC_PATH)*.java


