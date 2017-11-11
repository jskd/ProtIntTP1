import java.net.*;
import java.io.*;
import java.lang.*;

public class ClientService implements Runnable{
	private Socket socket;
	private String hostname;
	private int port;

	private String ip_multdif = "224.4.4.4";
	private int multdif_port = 4444;
	private DatagramSocket dso;

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
						break;

					}catch (Exception e){
						connected = false;
						System.out.println(" -> Connection closed.");
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
			this.dso = new DatagramSocket();
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

		System.out.print(String.format("[RECEIVED] %s", msg));

		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case WELC:
				tcp_sendMsg(ProtocoleToken.NEWC);
			break;

			case NEWC:
				tcp_sendMsg(ProtocoleToken.ACKC);
				diff_sendMsg(ProtocoleToken.LIST);
			break;

			case BYE:
				this.dso.close();
				this.socket.close();
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

			case ACKC:
				msg = new Message();
				msg.setPrefix(ProtocoleToken.ACKC);
			break;
		}

		if(msg != null){
			pw.print(msg.toString());
			pw.flush();
		}
	}

	/**
   * Envoi le message correspondant au token via UDP multicast
   * @param token Type de message à envoyer
   * @throws IOException Lance une exception en cas de problème
   */
	public void diff_sendMsg(ProtocoleToken token) throws IOException{

		Message msg = null; 

		switch(token){
			case LIST:
				msg = new Message();
				msg.setPrefix(ProtocoleToken.LIST);
			break;
		}

		if(msg != null){
			// Envoi du message
			InetSocketAddress isa = new InetSocketAddress(ip_multdif, multdif_port);
			DatagramPacket paquet = new DatagramPacket(msg.toString().getBytes(), msg.toString().length(), isa);
			dso.send(paquet);
		}
	}

}
			


