import java.util.*;

/**
 * Classe Message
 * Permet de d'utiliser des messages formatés plus simple d'utilisations
 *
 * @author Lefranc Joaquim, Skoda Jérôme
 */
public class Message{

	// Champs du message (Protocole)
	private ProtocoleToken prefix = null;
	private ProtocoleToken mode = null;
	private int id_src = 0;
	private int id_dst = 0;

	private String annonce_titre = "";
	private String annonce_contenu = "";
	private int annonce_prix = 0;

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
		ArrayList<String> argv = new ArrayList<String>(Arrays.asList(mess.split("\\s")));

		try{
			this.prefix = ProtocoleToken.valueOf(argv.get(0));

			switch(prefix){
				case WELC:
					this.mode = ProtocoleToken.TCP;
				break;

				case NEWC:
					this.mode = ProtocoleToken.TCP;
				break;

				case ACKC:
					this.mode = ProtocoleToken.TCP;
				break;

				case BYE:
					this.mode = ProtocoleToken.TCP;
				break;

				case ANNO:
					this.mode = ProtocoleToken.TCP;
					this.id_src = Integer.parseInt(argv.get(1));
					this.annonce_titre = argv.get(2);
					this.annonce_contenu = argv.get(3);
					this.annonce_prix = Integer.parseInt(argv.get(4));
				break;

				case LIST:
					this.mode = ProtocoleToken.UDP;
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
			case WELC:
				this.mode = ProtocoleToken.TCP;
				mess = String.format("%s", prefix);
			break;

			case NEWC:
				this.mode = ProtocoleToken.TCP;
				mess = String.format("%s",prefix);
			break;

			case ACKC:
				this.mode = ProtocoleToken.TCP;
				mess = String.format("%s",prefix);
			break;

			case BYE:
				this.mode = ProtocoleToken.TCP;
				mess = String.format("%s",prefix);
			break;

			case ANNO:
				this.mode = ProtocoleToken.TCP;
				mess = String.format("%s %s %s %s %s",prefix, id_src, annonce_titre, annonce_contenu, annonce_prix);
			break;

			case LIST:
				this.mode = ProtocoleToken.UDP;
				mess = String.format("%s", prefix);
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
	public void setPrefix(ProtocoleToken pref){
		this.prefix = pref;
	}

	public void setId_Src(int id){
		this.id_src = id;
	}

	public void setId_Dst(int id){
		this.id_dst = id;
	}

	public void setAnnonce_Titre(String titre){
		this.annonce_titre = titre;
	}

	public void setAnnonce_Contenu(String contenu){
		this.annonce_contenu = contenu;
	}

	public void setAnnonce_Prix(int prix){
		this.annonce_prix = prix;
	}

	/**
	 * Retourne le prefixe
	 * @return prefix
	 */
	public ProtocoleToken getPrefix(){
		return this.prefix;
	}

	public int getId_Src(){
		return this.id_src;
	}

	public int getId_Dst(){
		return this.id_dst;
	}

	public String getAnnonce_Titre(){
		return this.annonce_titre;
	}

	public String getAnnonce_Contenu(){
		return this.annonce_contenu;
	}

	public int getAnnonce_Prix(){
		return this.annonce_prix;
	}
}
