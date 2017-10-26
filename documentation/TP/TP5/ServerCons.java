import java.net.*;
import java.io.*;

public class ServerCons implements Runnable{
	private int port = 6767;
	private ServerSocket socket;
	private BufferConcurrent buffer;

	public ServerCons(BufferConcurrent b){
		this.buffer = b;
	}

	public void run(){
		try{
			System.out.println("Lancement du socket consomateur...");
			socket = new ServerSocket(port);

			while(true){
				try{
					Socket comSock = socket.accept();

					BufferedReader br = new BufferedReader(new InputStreamReader(comSock.getInputStream()));
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(comSock.getOutputStream()));

					String str;
					while(true){
						str = buffer.retirer();
						pw.println(str);
						pw.flush();
					}
					
				}catch (Exception e){
					System.out.println("Probleme de connection avec le client.");
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

}