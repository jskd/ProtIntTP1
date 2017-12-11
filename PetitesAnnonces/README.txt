#**************************************************#
# Name : Section 1 et 2 du TP
# Version : 0.0.1
# Auteurs : 
#**************************************************#

MAKEFILE RULES :
----------------------------------------------------
 - make : Compile dans bin/
 - make jar : Compile le Serveur.jar et Client.jar dans build/dist/

 - make runserv debug=(true/false) : Execute le serveur
 - make runcli ip=0.0.0.0 debug=(true/false) : Execute le client, ip du serveur à spécifier

 - make runjars debug=(true/false) : Execute le serveur (JAR)
 - make runjarc ip=0.0.0.0 debug=(true/false) : Execute le client, ip du serveur à spécifier (JAR)

 - make tar : Archive le projet
 - make clean : Supprime les .class
----------------------------------------------------