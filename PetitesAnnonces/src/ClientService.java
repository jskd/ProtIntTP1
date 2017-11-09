import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientService implements Runnable{
	private Socket socket;
	private String hostname;
	private int port;

	private BufferedReader br;
	private PrintWriter pw;

	private boolean connected = false;
	private Runnable tcp_listening;

	public ClientService(Socket s){
		this.socket = s;
		this.hostname = socket.getInetAddress().getHostName();
		this.port = socket.getPort();

		/**
	   * Thread d'écoute TCP
	   */
		tcp_listening = new Runnable() {
			public void run(){

				while(true){
					try{
						// Lecture des messages entrant
						while(connected){

							String st_mess = br.readLine();
							Message msg = new Message(st_mess);

							// Interpretation du message
							tcp_readMessage(msg);
						}

						br.close();
						pw.close();
						socket.close();

					}catch (Exception e){
						connected = false;
						System.out.println(" -> Connection closed.");
						break;
					}
				}
			}
		};
	}

	@Override
	public void run(){
		try{
			System.out.println("# New connection with " + hostname + ":" + port);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
			connected = true;

			(new Thread(tcp_listening)).start();
			tcp_sendMsg(ProtocoleToken.WELC);
			
		}catch (Exception e){
			System.out.println("Connection failed.");
		}
	}

	/**
   * Interpretation du message TCP
   * @param msg Message 
   * @throws IOException Lance une exception en cas de problème
   */
	public void tcp_readMessage(Message msg) throws IOException{

		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case WELC:
					tcp_sendMsg(ProtocoleToken.NEWC);
					System.out.println(String.format("[RECEIVED] %s", ProtocoleToken.WELC));
			break;
		}
	}

	/**
   * Permet d'envoyer un message via TCP
   * @param token Type de message à envoyer
   * @throws IOException Lance une exception en cas de problème
   */
	public void tcp_sendMsg(ProtocoleToken token) throws IOException{
		
		Message msg = null; 

		switch(token){
			case WELC:
				msg = new Message();
				msg.setPrefix(ProtocoleToken.WELC);
			break;

			case NEWC:
				msg = new Message();
				msg.setPrefix(ProtocoleToken.NEWC);
			break;
		}

		if(msg != null){
			pw.print(msg.toString());
			pw.flush();
		}
	}

}
			


