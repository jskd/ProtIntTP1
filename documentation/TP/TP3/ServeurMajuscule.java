import java.net.*;
import java.io.*;

public class ServeurMajuscule{
	public static void main(String args[]){
		try{

			ServerSocket serveur = new ServerSocket(3434);
			String host = InetAddress.getLocalHost().getHostName();
			System.out.println("Serveur running at "+host+":3434");

			while(true){
				Socket socket = serveur.accept();
				BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

				pw.print("Welcome to ServeurMajuscule :)\n");
				pw.flush();

				while(true){
					String mess = br.readLine();

					if(mess.equals(".")){
						pw.print(".\n");
						pw.flush();
						break;
					}
					else{
						pw.print(mess.toUpperCase()+"\n");
						pw.flush();
					}
				}

				br.close();
				pw.close();
				socket.close();
			}

		}catch (Exception e){
			System.out.println(e);
		}
	}
}