import java.net.*;
import java.io.*;

public class ClientCons{

	public static void main(String[] args){
		String servAddr = "127.0.0.1";
		int servPort = 6767;	
		
		System.out.println("Connexion au serveur ...");

		try {
			Socket sock = new Socket(servAddr, servPort);
			System.out.println("Connexion établie.");

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
				
				while(true){
					String chaine = br.readLine();
					System.out.println("Consomation : " + chaine);
				}
				
			}
			catch (Exception e) {
				System.out.println("Communication failed. Quitting...");
				sock.close();
				System.out.println("Connection closed. Try again later.");
				System.exit(0);
			}
		}
		catch (Exception e) {
			System.out.println("Connection failed. Try again later.");
		}
	}
}