import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientMajuscule{
	public static void main(String args[]){

		String host;
		int port;
		Scanner sc = new Scanner(System.in);

		System.out.println("-- Connexion au serveur --");
		System.out.println("Host :");
		host = sc.nextLine();
		System.out.println("Port :");
		port = sc.nextInt();

		try{
			Socket socket = new Socket(host, port);
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
 
			while(true){
				String mess = br.readLine();
				System.out.println(mess);

				if(mess.equals(".")){
					System.out.println("Fermeture.");
					break;
				}

				String rep = sc.nextLine();
				pw.print(rep+"\n");
				pw.flush();
			}

			pw.close();
			br.close();
			socket.close();
			
		}catch (Exception e){
			System.out.println(e);
		}
	}
}