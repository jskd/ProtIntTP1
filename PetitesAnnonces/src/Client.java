import java.io.*;
import java.net.*;
import java.util.*;

public class Client {
	private static final int MAX_SIZE_MSG = 2048;
	private static String argl; // Ligne de commande brute
  private static ArrayList<String> argv = new ArrayList<String>(); // Liste des arguments
  private static Scanner sc = new Scanner(System.in);

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
		System.out.print("[s]Send [q]Quit : ");
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
		//System.out.print(String.format("[RECEIVED TCP] %s", msg));

		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case WELC:
				tcp_sendMsg(ProtocoleToken.NEWC);
			break;
		}
	}

	/**
   * Interpretation du message UDP multicast
   * @param msg Message 
   * @throws IOException Lance une exception en cas de problème
   */
	public void diff_readMessage(Message msg) throws IOException{
		//System.out.print(String.format("[RECEIVED UDP] %s \n", msg));

		// Comportements définis en fonction du prefixe
		switch(msg.getPrefix()){
			case LIST:
				System.out.println("\n -> List received.");
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

	public static void main(String[] args) {
		(new Client()).start();
	}
}
