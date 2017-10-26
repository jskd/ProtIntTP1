import java.net.*;
import java.io.*;
import java.util.Random;

public class CompetitiveServer{
	static int port = 4242;
	private static ServerSocket serverSock;
	private static GameServer game;

	public static void main(String args[]){
		try {
			System.out.println("Création du ServerSocket...");
			serverSock = new ServerSocket(port);
			System.out.println("Serveur running on " + serverSock.getInetAddress().getHostName() + " at port number " + serverSock.getLocalPort() + ".\n");
			
			System.out.println("Création du serveur de jeux..");
			game = new GameServer();

			while (true) {
				try {
					Socket socket = serverSock.accept();
					game.addPlayer(socket);

				} catch (Exception e) {
					System.out.println("Probleme avec le client.\n");
				}
			}
		} catch (Exception e) {
			System.out.println("Erreur lors du lancement du serveur.");
			e.printStackTrace();
		}
	}
}