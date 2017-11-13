import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	private boolean DEBUG = true;

	private static final int MAX_SIZE_MSG = 2048;
	private static String argl;
  private static ArrayList<String> argv = new ArrayList<String>();
  private static Scanner sc = new Scanner(System.in);

  private LinkedList<Annonce> annonces;

	private String servAddr = "0.0.0.0";
	private String name = "Client";

	private Socket socket;
	private int servPort = 1027;

	private String ip_multdif = "224.4.4.4";
	private int multdif_port = 4444;
	private MulticastSocket mso;

	private BufferedReader br;
	private PrintWriter pw;
	
	private Runnable tcp_listening;
	private Runnable multicast_listening;
	private boolean connected = false;

	public Client(){
		this.annonces = new LinkedList<Annonce>();
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

		/**
	   * Thread d'écoute multicast
	   */
		multicast_listening = new Runnable(){
			public void run(){
				while(true){
					try{

						// Attente d'un paquet sur le port UDP multicast
						byte[] data = new byte[MAX_SIZE_MSG];
						DatagramPacket paquet = new DatagramPacket(data, data.length);
						mso.receive(paquet);

						String st = new String(paquet.getData(), 0, paquet.getLength());
						InetSocketAddress isa = (InetSocketAddress)paquet.getSocketAddress();

						// Interpretation du message
						Message msg = new Message(st);
						diff_readMessage(msg);

					}catch (Exception e){
						break;
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

			this.mso = new MulticastSocket(this.multdif_port);
			this.mso.joinGroup(InetAddress.getByName(this.ip_multdif));

			System.out.println(" -> Connection established.");
			connected = true;

			(new Thread(tcp_listening)).start();
			(new Thread(multicast_listening)).start();

		} catch (Exception e) {
			System.out.println("Connection failed.");
			sc.close();
		}

		// Boucle d'execution des commandes
		while(true){
		  displayPrompt();
		  read_command();
		  tokenize_command();
		  execute_command();
		}
	}

	public static void displayPrompt(){
		System.out.print("[a]Annonce [l]List [m]Message [q]Quit : ");
	}

  /**
   * Stock la commande complete dans argl
   */
  public void read_command(){
    try{
        argl = sc.nextLine();   
    }catch (NoSuchElementException e){ // Exception levé lors de Ctrl+D
        argl = "q";
    }
  }

  /**
   * Découpe la commande en arguments distincts
   */
  public void tokenize_command(){
    argv = new ArrayList<String>(Arrays.asList(argl.split("\\s+")));
  }

 /**
   * Execute la commande contenue dans argl
   */
  public void execute_command(){
  	System.out.println("");

    if(argv.get(0).equals("q")){
      System.out.println("Bye.");

      try{
	      tcp_sendMsg(ProtocoleToken.BYE);
	      this.mso.leaveGroup(InetAddress.getByName(this.ip_multdif));
				this.mso.close();
				this.socket.close();
			}catch (Exception e){
				e.printStackTrace();
			}

			System.exit(0);
    }
    else if(argv.get(0).equals("a")){

    	Annonce annonce = new Annonce("Titre", "Blabla", 499);  	
    	Message mess = annonce.toMessage();

    	try{
    		tcp_sendMsg(mess);
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    else if(argv.get(0).equals("l")){
    	for(int i=0; i<annonces.size(); i++){
    		Annonce ann = annonces.get(i);
    		System.out.println(String.format("[%d] %s %s %s", i, ann.getTitre(), ann.getContenu(),
    			ann.getPrix()));
    	}
    }
    else if(argv.get(0).equals("m")){
    	if(argv.size() > 1){
    		System.out.print("Write message : ");
    		String client_message = sc.nextLine();

    		Message mess = new Message();
    		mess.setPrefix(ProtocoleToken.MESS);
    		mess.setId_Dst(Integer.parseInt(argv.get(1)));
    		mess.setClientMessage(client_message);

    		try{
	    		tcp_sendMsg(mess);
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
    	}
    	else{
    		System.out.println("Usage : m <id_client>");
    	}
    }
    else{
       System.out.format("Command %s doesn't exist.\n", argl);
    }
  }


	/**
   * Interpretation du message TCP
   * @param msg Message 
   * @throws IOException Lance une exception en cas de problème
   */
	public void tcp_readMessage(Message msg) throws IOException{
		if(DEBUG){
			System.out.print(String.format("\n[RECEIVED TCP] %s", msg));
		}

		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case WELC:
				tcp_sendMsg(ProtocoleToken.NEWC);
			break;

			case MESS:
			break;
		}
	}

	/**
   * Interpretation du message UDP multicast
   * @param msg Message 
   * @throws IOException Lance une exception en cas de problème
   */
	public void diff_readMessage(Message msg) throws IOException{
		if(DEBUG){
			System.out.print(String.format("\n[RECEIVED UDP] %s \n", msg));
		}

		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case LIST:
				System.out.println("\n -> List received.");
			break;

			case ANNO:
				Annonce annonce = new Annonce(msg);
				this.annonces.add(annonce);
			break;
		}

		displayPrompt();
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

	public void tcp_sendMsg(Message mess) throws IOException{
		if(mess != null){
			pw.print(mess.toString());
			pw.flush();
		}
	}

	public static void main(String[] args) {
		(new Client()).start();
	}
}
