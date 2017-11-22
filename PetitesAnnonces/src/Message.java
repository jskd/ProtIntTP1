import java.util.*;

/**
 * Classe Message
 * Permet de d'utiliser des messages formatés plus simple d'utilisations
 *
 * @author Lefranc Joaquim, Skoda Jérôme
 */
public class Message{

	private String SEPARATOR = "#";

	// Champs du message (Protocole)
	private ProtocoleToken prefix = null;
	private ProtocoleToken mode = null;
	private int id_src = 0;
	private int id_dst = 0;
	private int id_annonce = 0;
	private String annonce_titre = "";
	private String annonce_contenu = "";
	private double annonce_prix = 0;
	private String client_message = "";
	private int nb_anno = 0;

	/**
	 * Constructeur par defaut
	 */
	public Message(){

	}

	/**
	 * Constructeur
	 *
	 * Construit un message à partir d'une string
	 * @param mess Message à construire
	 * @throws MalformedMsgException Lance une exception si le message est malformé
	 */
	public Message(String mess) throws MalformedMsgException{
		//ArrayList<String> argv = new ArrayList<String>(Arrays.asList(mess.split("\\s+")));
		ArrayList<String> argv = new ArrayList<String>(Arrays.asList(mess.split(SEPARATOR)));

		try{
			this.prefix = ProtocoleToken.valueOf(argv.get(0));

			switch(prefix){
				case BYE:
					this.mode = ProtocoleToken.TCP;
				break;

				case ANNO:
					this.mode = ProtocoleToken.TCP;
					this.id_annonce = Integer.parseInt(argv.get(1));
					this.id_src = Integer.parseInt(argv.get(2));
					this.annonce_titre = argv.get(3);
					this.annonce_contenu = argv.get(4);
					this.annonce_prix = Double.parseDouble(argv.get(5));
				break;

				case MESS:
					this.mode = ProtocoleToken.TCP;
					this.id_src = Integer.parseInt(argv.get(1));
					this.id_dst = Integer.parseInt(argv.get(2));
					this.client_message = argv.get(3);
				break;

				case LIST:
					this.nb_anno = Integer.parseInt(argv.get(1));
				break;

				case DELETE:
					this.id_annonce = Integer.parseInt(argv.get(1));
				break;
			}

		}catch (Exception e){
			throw new MalformedMsgException();
		}
	}

	/**
	 * Retourne le message sous forme de string
	 * @return Représentation du message en string
	 */
	public String toString(){
		String mess = "";

		switch(prefix){
			case BYE:
				this.mode = ProtocoleToken.TCP;
				mess = String.format("%s",prefix);
			break;

			case ANNO:
				mess = String.format("%s%s%s%s%s%s%s%s%s%s%s",
					prefix, SEPARATOR, id_annonce, SEPARATOR, id_src, SEPARATOR, 
					annonce_titre, SEPARATOR, annonce_contenu, SEPARATOR, annonce_prix);
			break;

			case MESS:
				this.mode = ProtocoleToken.TCP;
				mess = String.format("%s%s%s%s%s%s%s",
					prefix, SEPARATOR, id_src, SEPARATOR, id_dst, SEPARATOR, client_message);
			break;

			case LIST:
				mess = String.format("%s%s%s", prefix, SEPARATOR, this.nb_anno);
			break;

			case DELETE:
				mess = String.format("%s%s%s", prefix, SEPARATOR, this.id_annonce);
			break;
		}
			
		return mess+((this.mode == ProtocoleToken.TCP) ? "\n" : "");
	}

	/**
	 * Affichage le message
	 */
	public void print(){
		String mess = String.format("#########################################\n"
				+" - TYPE : %s\n"
				+"#########################################\n",
				prefix);
	
		System.out.println(mess);
	}

	/**
	 * Modifie le prefixe
	 * @param pref Nouveau prefixe
	 */

	public void setMode(ProtocoleToken mode){
		this.mode = mode;
	}

	public void setPrefix(ProtocoleToken pref){
		this.prefix = pref;
	}

	public void setId_Src(int id){
		this.id_src = id;
	}

	public void setId_Dst(int id){
		this.id_dst = id;
	}

	public void setId_Annonce(int id){
		this.id_annonce = id;
	}

	public void setAnnonce_Titre(String titre){
		this.annonce_titre = titre;
	}

	public void setAnnonce_Contenu(String contenu){
		this.annonce_contenu = contenu;
	}

	public void setAnnonce_Prix(double prix){
		this.annonce_prix = prix;
	}

	public void setClientMessage(String str){
		this.client_message = str;
	}

	public void setNbAnno(int nb_anno){
		this.nb_anno = nb_anno;
	}

	/**
	 * Retourne le prefixe
	 * @return prefix
	 */

	public ProtocoleToken getMode(){
		return this.mode;
	}

	public ProtocoleToken getPrefix(){
		return this.prefix;
	}

	public int getId_Src(){
		return this.id_src;
	}

	public int getId_Dst(){
		return this.id_dst;
	}

	public int getId_Annonce(){
		return this.id_annonce;
	}

	public String getAnnonce_Titre(){
		return this.annonce_titre;
	}

	public String getAnnonce_Contenu(){
		return this.annonce_contenu;
	}

	public double getAnnonce_Prix(){
		return this.annonce_prix;
	}

	public String getClientMessage(){
		return this.client_message;
	}

	public int getNbAnno(){
		return this.nb_anno;
	}
}
