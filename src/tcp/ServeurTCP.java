import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.LinkedList;
import java.util.Random;


public class ServeurTCP{
	static int port = 1027;
	private static ServerSocket srvSock;
	private static LinkedList<Thread> clients;

	public static void main(String[] args){
		clients = new LinkedList<Thread>();

		try{	
			System.out.println("# Starting serveur...");
			srvSock = new ServerSocket(port);
			String hostname = srvSock.getInetAddress().getHostName();
			String localport = String.valueOf(srvSock.getLocalPort());
			System.out.println(" -> Serveur listening at " + hostname + ":" + localport); 

			while (true){
				try{
					Socket clientSock = srvSock.accept();	
					Thread client = new Thread(new ClientService(clientSock));
					clients.add(client);
					client.start();
				} catch (Exception e){
					System.out.println("Error with client.");
				}
			}
		} catch (Exception e){
			System.out.println("Failled to create server socket.");
			e.printStackTrace();
		}
	}

	public static int nbClients(){
		return clients.size();
	}
}


