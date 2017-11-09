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

	TCP("TCP"),
	UDP("UDP"),
	DIFF("DIFF"),
	RECEIVED("RECEIVED"),
	SENT("SENT");

	private String name = "";

	ProtocoleToken(String name){
		this.name = name;	
	}
}