import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Random;


public class Serveur{
	private boolean DEBUG = false;
	public static int port = 1027;
	public static HashMap<ClientService, LinkedList<Annonce>> clients;
	private ServerSocket srvSock;

	public Serveur(String debug){
		this.DEBUG = Boolean.valueOf(debug);
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
					ClientService client = new ClientService(clientSock, this.DEBUG);
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
			if(args.length >= 1){
				(new Serveur(args[0])).start();
			}
			else{
				System.out.println("Usage: Serveur <debug>");
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}


