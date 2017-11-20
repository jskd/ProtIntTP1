import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.*;

public class ClientService implements Runnable{
	private boolean DEBUG = false;
	private Socket socket;
	private String hostname;
	private int port;
	private int id;

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
		this.id = Integer.parseInt(Tools.getRandomIdent());

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
						//e.printStackTrace();
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

			tcp_sendListAnnonces();

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
		if(DEBUG){
			System.out.print(String.format("[RECEIVED TCP] %s", msg));
		}

		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case ANNO:
				Annonce annonce = new Annonce(msg);
				annonce.setIdClient(this.id);
				annonce.setIdAnnonce(Integer.parseInt(Tools.getRandomIdent()));
				Serveur.clients.get(this).add(annonce);

				diff_sendMsg(annonce.toMessage());
			break;

			case MESS:
				ClientService destinataire = null;

				// Recherche du destinataire du message
				Iterator it = Serveur.clients.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					ClientService current = (ClientService) pair.getKey();
					if(current.getId() == msg.getId_Dst()){
						destinataire = current;
						break;
					}
					it.remove();
				}

				if(destinataire != null){
					msg.setId_Dst(destinataire.getId());
					msg.setId_Src(this.getId());
					destinataire.tcp_sendMsg(msg);					
				}

			break;

			case BYE:
				Serveur.clients.remove(this);
				udp_sendListAnnonces();
				this.dso.close();
				this.socket.close();
			break;
		}
	}

	public void tcp_sendMsg(Message msg) throws IOException{
		if(msg != null){
			msg.setMode(ProtocoleToken.TCP);
			pw.print(msg.toString());
			pw.flush();
		}
	}

	public void diff_sendMsg(Message msg) throws IOException{
		if(msg != null){
			msg.setMode(ProtocoleToken.UDP);
			// Envoi du message
			InetSocketAddress isa = new InetSocketAddress(ip_multdif, multdif_port);
			DatagramPacket paquet = new DatagramPacket(msg.toString().getBytes(), msg.toString().length(), isa);
			dso.send(paquet);
		}
	}

	public void tcp_sendListAnnonces() throws IOException{
		Message msg = null;

		msg = new Message();
		msg.setMode(ProtocoleToken.TCP);
		msg.setPrefix(ProtocoleToken.LIST);
		tcp_sendMsg(msg);

		// Parcours des annonces
		Iterator it = Serveur.clients.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			LinkedList annonces = (LinkedList) pair.getValue();
			for(int i=0; i<annonces.size(); i++){
				msg = ((Annonce)annonces.get(i)).toMessage();

				if(msg != null){
					msg.setMode(ProtocoleToken.TCP);
					tcp_sendMsg(msg);
				}
			}
		}
	}

	public void udp_sendListAnnonces() throws IOException{
		Message msg = null;

		msg = new Message();
		msg.setMode(ProtocoleToken.UDP);
		msg.setPrefix(ProtocoleToken.LIST);
		diff_sendMsg(msg);

		// Parcours des annonces
		Iterator it = Serveur.clients.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry)it.next();
			LinkedList annonces = (LinkedList) pair.getValue();
			for(int i=0; i<annonces.size(); i++){
				msg = ((Annonce)annonces.get(i)).toMessage();

				if(msg != null){
					msg.setMode(ProtocoleToken.UDP);
					diff_sendMsg(msg);
				}
			}
		}
	}

	public int getId(){
		return this.id;
	}

}
			


