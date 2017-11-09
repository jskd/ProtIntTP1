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

	/**
	 * Retourne le prefixe
	 * @return prefix
	 */
	public ProtocoleToken getPrefix(){
		return this.prefix;
	}
}
