import java.io.*;
import java.net.*;

public class Ent1{
	private static int port = 6868;
	private static ServerSocket servSock;

	public static void main(String[] args){
		try{

			System.out.println("Lancement du serveur...");
			servSock = new ServerSocket(port);

			while(true){
				try{
					Socket sock = servSock.accept();

					BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));


					String[] parts;
					String line = br.readLine();

					if(line != null){
						parts = line.split(" ");
						System.out.println("IP = "+parts[0]+" Port = "+parts[1]);

						sock.close();

						sock = new Socket(parts[0], Integer.parseInt(parts[1]));

						br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
						pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

						pw.write("CONFIRM\n");
						pw.flush();

						while(true){
							line = br.readLine();
							System.out.println(line);

							if(line.equals("ACKCONFIRM") == true){
								System.out.println("Fermeture de la connexion");
								sock.close();
								break;
							}
						}
					}

				}catch (Exception e){
					e.printStackTrace();
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}