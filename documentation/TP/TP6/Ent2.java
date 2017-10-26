import java.io.*;
import java.net.*;

public class Ent2{
	private static int port = 6969;
	private static ServerSocket servSock;

	public static void main(String[] args){
		try{

			System.out.println("Lancement du serveur...");
			servSock = new ServerSocket(port);

			try{
				Socket sock = new Socket("127.0.0.1", 6868);
				System.out.println("Connexion établie.");

				try{
					BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

					pw.write(servSock.getInetAddress().getHostName()+" "+servSock.getLocalPort()+"\n");
					pw.flush();

					sock.close();

					sock = servSock.accept();

					br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
					pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

					String line = "";
					while(true){
						line = br.readLine();
						System.out.println(line);

						if(line.equals("CONFIRM") == true){
							pw.write("ACKCONFIRM\n");
							pw.flush();
							break;
						}
					}

					sock.close();
					servSock.close();

				}catch (Exception e){
					e.printStackTrace();
				}

			}catch (Exception e){
				e.printStackTrace();
			}

			servSock.close();

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}