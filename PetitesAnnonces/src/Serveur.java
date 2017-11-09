import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.LinkedList;
import java.util.Random;


public class Serveur{
	public static int port = 1027;
	public static LinkedList<ClientService> clients;
	private ServerSocket srvSock;

	public Serveur(){
		clients = new LinkedList<ClientService>();	
	}

	public void start(){
		try{	
			srvSock = new ServerSocket(port);
			System.out.println("# Starting serveur...");
			String hostname = srvSock.getInetAddress().getHostName();
			String localport = String.valueOf(srvSock.getLocalPort());	
			System.out.println(" -> Serveur listening at " + hostname + ":" + localport); 

			while (true){
				try{
					
					Socket clientSock = srvSock.accept();
					ClientService client = new ClientService(clientSock);
					clients.add(client);
					(new Thread(client)).start();

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

	public static void main(String[] args){
		try{
			(new Serveur()).start();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}


