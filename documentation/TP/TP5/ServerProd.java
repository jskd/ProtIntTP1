import java.net.*;
import java.io.*;

public class ServerProd implements Runnable{
	private int port = 6868;
	private ServerSocket socket;
	private BufferConcurrent buffer;

	public ServerProd(BufferConcurrent b){
		this.buffer = b;
	}

	public void run(){
		try{
			System.out.println("Lancement du socket producteur...");
			socket = new ServerSocket(port);

			while(true){
				try{
					Socket comSock = socket.accept();

					BufferedReader br = new BufferedReader(new InputStreamReader(comSock.getInputStream()));
					PrintWriter pw = new PrintWriter(new OutputStreamWriter(comSock.getOutputStream()));

					while(true){
						String str = br.readLine();
						buffer.ajouter(str);
					}
					
				}catch (Exception e){
					System.out.println("Probleme de connection avec le client.");
					e.printStackTrace();
				}
			}

		}catch (Exception e){
			e.printStackTrace();
		}
	}

}