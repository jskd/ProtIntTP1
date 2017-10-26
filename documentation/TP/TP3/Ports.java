import java.net.Socket;

public class Ports{
	public static void main(String args[]){

		if(args.length >= 3){
			int debut = Integer.parseInt(args[0]);
			int fin = Integer.parseInt(args[1]);
			String host = args[2];

			for(int i=debut; i<=fin; i++){
				try{
					Socket socket = new Socket(host, i);
					socket.close();

					System.out.println(i+" - Disponible.");
				}catch (Exception e){
					System.err.println(i+" - Connexion refusée.");
				}
			}
		}
		else{
			System.err.println("Usage: Ports <int> <int> <host>");
		}
	}
}