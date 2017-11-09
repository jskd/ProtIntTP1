import java.io.*;
import java.net.*;
import java.util.*;

public class ClientTCP {

	private String servAddr = "0.0.0.0";
	private String name = "Client";

	private Socket socket;
	private int servPort = 1027;

	private BufferedReader br;
	private PrintWriter pw;
	private Scanner sc;

	private Runnable tcp_listening;
	private boolean connected = false;

	public ClientTCP(){

		/**
	   * Thread d'écoute TCP
	   */
		tcp_listening = new Runnable() {
			public void run(){
				while(true){
					try{
						while(connected){
							// Lecture des messages entrant
							String st_mess = br.readLine();
							Message msg = new Message(st_mess);
							// Interpretation du message
							tcp_readMessage(msg);
						}

						br.close();
						pw.close();
						socket.close();
						break;

					}catch (Exception e){
						connected = false;
						System.out.println(" -> Connection closed.");
					}
				}
			}
		};
	}

	public void start(){
		System.out.println("# Connecting to server...");

		try{

			socket = new Socket(servAddr, servPort);
			br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));		
			sc = new Scanner(System.in);

			System.out.println(" -> Connection established.");
			connected = true;

			(new Thread(tcp_listening)).start();

		} catch (Exception e) {
			System.out.println("Connection failed.");
			sc.close();
		}
	}


	/**
   * Interpretation du message TCP
   * @param msg Message 
   * @throws IOException Lance une exception en cas de problème
   */
	public void tcp_readMessage(Message msg) throws IOException{
		System.out.print(String.format("[RECEIVED] %s", msg));
		
		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case WELC:
					tcp_sendMsg(ProtocoleToken.NEWC);
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
	
	public static void displayPrompt(){
		System.out.print("[s]Send [q]Quit : ");
	}

	public static void main(String[] args) {
		(new ClientTCP()).start();
	}
}
