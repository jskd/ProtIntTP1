/**
 * Enumeration ProtocoleToken
 * - Contient les tokens du protocole
 *
 * @author Lefranc Joaquim, Skoda Jérôme
 */

public enum ProtocoleToken{
	WELC("WELC"),
	NEWC("NEWC"),
	ACKC("ACKC"),
	ANNO("ANNO"),
	LIST("LIST"),
	BYE("BYE"),

	TCP("TCP"),
	UDP("UDP");

	private String name = "";

	ProtocoleToken(String name){
		this.name = name;	
	}

	public String toString(){
		return this.name;
	}
}