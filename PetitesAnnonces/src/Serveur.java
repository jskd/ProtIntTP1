import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;


public class Serveur{
	public static int port = 1027;
	public static HashMap<ClientService, LinkedList<Annonce>> clients;
	private ServerSocket srvSock;

	public Serveur(){
		clients = new HashMap<ClientService, LinkedList<Annonce>>();	
	}

	public void start(){
		try{	
			srvSock = new ServerSocket(port);
			System.out.println("# Starting serveur...");
			String hostname = srvSock.getInetAddress().getHostName();
			String ip = Tools.getLocalIP();
			String localport = String.valueOf(srvSock.getLocalPort());	
			System.out.println(" -> Serveur listening at " + ip + ":" + localport); 

			while (true){
				try{
					
					Socket clientSock = srvSock.accept();
					ClientService client = new ClientService(clientSock);
					clients.put(client, new LinkedList<Annonce>());
					
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

	public static void main(String[] args){
		try{
			(new Serveur()).start();
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}


