import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientService implements Runnable{
	private Socket sock;

	public ClientService(Socket s){
		this.sock = s;
	}

	@Override
	public void run(){
		try{
			String hostname = sock.getInetAddress().getHostName();
			System.out.println("# New connection with " + hostname + ":" + sock.getPort());

			BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

			String msg = "";
			long begin = System.currentTimeMillis() / 1000;
			
			while (true){
				long now = System.currentTimeMillis() / 1000;

				if(now - begin >= 5){
					begin = now;
					
					msg = "Clients connected = " + ServeurTCP.nbClients();
					pw.println(msg);
					pw.flush();
					System.out.println(" [SENDED] : " + msg);
				}

				if(br.ready()){
					msg = br.readLine();
					System.out.println(" [RECEIVED] : " + msg);
				}
				if(msg.equals("QUIT")){
					pw.println("BYE");
					pw.flush();
					System.out.println(" [SENDED] : BYE");
					break;
				}
				
				Thread.sleep(200);
			}
			pw.flush();
			System.out.println("# Closing connection...");
			br.close();
			pw.close();
			sock.close();
			System.out.println(" -> Connection closed.");
		}catch (Exception e){
			System.out.println(e);
		}
	}
}
			


