/**
 * Enumeration ProtocoleToken
 * - Contient les tokens du protocole
 *
 * @author Lefranc Joaquim, Skoda Jérôme
 */

public enum ProtocoleToken{
	ANNO("ANNO"),
	LIST("LIST"),
	MESS("MESS"),
	DELETE("DELETE"),
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