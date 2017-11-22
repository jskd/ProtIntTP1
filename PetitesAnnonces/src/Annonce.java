import java.util.*;

public class Annonce{
	private int id_annonce;
	private int id_client;
	private String titre;
	private String contenu;
	private double prix;

	public Annonce(String titre, String contenu, double prix){
		this.id_annonce = Integer.parseInt(Tools.getRandomIdent());
		this.id_client = 0;
		this.titre = titre;
		this.contenu = contenu;
		this.prix = prix;
	}

	public Annonce(Message mess){
		this.id_client = mess.getId_Src();
		this.id_annonce = mess.getId_Annonce();
		this.titre = mess.getAnnonce_Titre();
		this.contenu = mess.getAnnonce_Contenu();
		this.prix = mess.getAnnonce_Prix();
	}

	public Message toMessage(){
		Message mess = new Message();
		mess.setPrefix(ProtocoleToken.ANNO);
		mess.setId_Src(this.id_client);
		mess.setId_Annonce(this.id_annonce);
		mess.setAnnonce_Titre(this.titre);
		mess.setAnnonce_Contenu(this.contenu);
		mess.setAnnonce_Prix(this.prix);
		return mess;
	}

	public int getIdAnnonce(){
		return this.id_annonce;
	}

	public int getIdClient(){
		return this.id_client;
	}

	public String getTitre(){
		return this.titre;
	}

	public String getContenu(){
		return this.contenu;
	}

	public double getPrix(){
		return this.prix;
	}

	public void setIdAnnonce(int id){
		this.id_annonce = id;
	}
	
	public void setIdClient(int id){
		this.id_client = id;
	}

	public void setTitre(String titre){
		this.titre = titre;
	}

	public void setContenu(String contenu){
		this.contenu = contenu;
	}

	public void setPrix(int prix){
		this.prix = prix;
	}
}