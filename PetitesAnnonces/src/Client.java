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
  private LinkedList<Integer> mes_annonces; 

	private String servAddr = "0.0.0.0";
	private String name = "Client";

	private Socket socket;
	private int servPort = 1027;

	private String ip_multdif = "225.5.5.5";
	private int multdif_port = 4444;
	private MulticastSocket mso;

	private BufferedReader br;
	private PrintWriter pw;
	
	private Runnable tcp_listening;
	private Runnable multicast_listening;
	private boolean connected = false;

	public Client(String ip){
		this.servAddr = ip;
		this.annonces = new LinkedList<Annonce>();
		this.mes_annonces = new LinkedList<Integer>();
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
		System.out.print("\n[a]Annonce [ta]TestAnno [l]List [m]Message [d]Delete [q]Quit : ");
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

    	try{
    		System.out.print(" +Titre : ");
    		String title = sc.nextLine();
    		System.out.print(" +Contenu : ");
    		String content = sc.nextLine();
    		System.out.print(" +Prix : ");
    		Double prix = Double.parseDouble(sc.nextLine());

    		Annonce annonce = new Annonce(title, content, prix);
    		mes_annonces.add(annonce.getIdAnnonce());

    		Message mess = annonce.toMessage();
    		tcp_sendMsg(mess);

    		System.out.println("\n -> Annonce delivered !");

    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    else if(argv.get(0).equals("l")){

    	for(int i=0; i<annonces.size(); i++){
    		Annonce ann = annonces.get(i);
    		boolean ismine = mes_annonces.contains(ann.getIdAnnonce());

    		System.out.println(String.format("%s[%d from %d] \n  %s %s %s$",(ismine ? "[*]" : ""), ann.getIdAnnonce(), ann.getIdClient(), ann.getTitre(), ann.getContenu(),
    			ann.getPrix()));
    	}
    }
    else if(argv.get(0).equals("m")){

    	if(argv.size() > 1){
    		int id_client = Integer.parseInt(argv.get(1));

				System.out.print(" +Message : ");
				String client_message = sc.nextLine();

				Message mess = new Message();
				mess.setPrefix(ProtocoleToken.MESS);
				mess.setId_Dst(id_client);
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
    else if(argv.get(0).equals("ta")){

    	LinkedList<Annonce> annonces = new LinkedList<>();
    	annonces.add(new Annonce("Fender Telecaster", "ClassicVibe Custom", 489));	
    	annonces.add(new Annonce("Ford Mustang", "GT350 Shelby", 54300));  	
    	annonces.add(new Annonce("Luxury Chess", "Avec double dame.", 179));
    	
    	try{
    		for(int i=0; i<annonces.size(); i++){
	    		Message mess = annonces.get(i).toMessage();
	    		tcp_sendMsg(mess);
    		}

    		System.out.println(" -> Annonces de test envoyées.");
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    else if(argv.get(0).equals("d")){

    	System.out.println(" # My messages --------------");
    	LinkedList<Annonce> choices = new LinkedList<Annonce>();
    	int counter = 0;

    	for(int i=0; i<annonces.size(); i++){
    		Annonce ann = annonces.get(i);
    		boolean ismine = mes_annonces.contains(ann.getIdAnnonce());

    		if(ismine){
    			choices.add(ann);
	    		System.out.println(String.format("[%d] %s %s %s", 
	    			counter, ann.getTitre(), ann.getContenu(),ann.getPrix()));

	    		counter++;
    		}
    	}

    	System.out.print("\n> Choose annonce to delete : ");
    	int choice = Integer.parseInt(sc.nextLine());
    	if(choices.size() > 0 && choice < choices.size()){
    		int id_choice = choices.get(choice).getIdAnnonce();

    		for(int i=0; i<annonces.size(); i++){
    			Annonce ann = annonces.get(i);
    			if(ann.getIdAnnonce() == id_choice){
    				//annonces.remove(i);
    				Message mess = new Message();
						mess.setPrefix(ProtocoleToken.DELETE);
						mess.setId_Annonce(id_choice);

						try{
							tcp_sendMsg(mess);
						}catch(Exception e){
							e.printStackTrace();
						}
    				break;
    			}
    		}

    	}
    	else{
    		System.out.println("Error : Ce choix n'est pas possible.");
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
			case LIST:
				this.annonces.clear();
			break;
			case ANNO:
				Annonce annonce = new Annonce(msg);
				this.annonces.add(annonce);
			break;
			case MESS:
				System.out.println(String.format("\n[Message from %s] : %s ", msg.getId_Src(), msg.getClientMessage()));
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
				this.annonces.clear();
			break;
			case ANNO:
				Annonce annonce = new Annonce(msg);
				this.annonces.add(annonce);
			break;
			case DELETE:
				for(int i=0; i<annonces.size(); i++){
					if(annonces.get(i).getIdAnnonce() == msg.getId_Annonce()){
						mes_annonces.remove((Integer)msg.getId_Annonce());
						annonces.remove(i);
						break;
					}
				}
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
			case BYE:
				msg = new Message();
				msg.setPrefix(ProtocoleToken.BYE);
			break;
		}

		if(msg != null){
			msg.setMode(ProtocoleToken.TCP);
			pw.print(msg.toString());
			pw.flush();
		}
	}

	public void tcp_sendMsg(Message msg) throws IOException{
		if(msg != null){
			msg.setMode(ProtocoleToken.TCP);
			pw.print(msg.toString());
			pw.flush();
		}
	}

	public static void main(String[] args) {
		if(args.length >= 1){
			(new Client(args[0])).start();
		}
		else{
			System.out.println("Usage: Client <ip_serveur>");
		}
	}
}
