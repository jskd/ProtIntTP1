import java.io.*;
import java.net.*;
import java.util.*;

public class ClientTCP {
	public static void main(String[] args) {
		String servAddr = "0.0.0.0", name = "Client";
		int servPort = 1027;
		
		Scanner sc = new Scanner(System.in);
		System.out.println("# Connecting to server...");

		try {
			Socket sock = new Socket(servAddr, servPort);
			System.out.println(" -> Connection established.");
			
			try {
				BufferedReader br = new BufferedReader(
						new InputStreamReader(
								sock.getInputStream()));
				PrintWriter pw = new PrintWriter(
						new OutputStreamWriter(
								sock.getOutputStream()));

				String rep = "";

				while (true) {
					rep = br.readLine();
					System.out.println(" [RECEIVED] : " + rep);

					/*
					String msg  = sc.nextLine();
					pw.println(msg);
					pw.flush();
					System.out.println(" [SENDED] : " + msg);
					*/
					
					/*
					try{
						String msg = sc.nextLine();   
						if (msg.equals("quit")){	
							break;
						}
					}catch (NoSuchElementException e){ // Exception levé lors de Ctrl+D
						System.out.println("Bye.");
						break;
					}
					*/
					if(rep.equals("quit")) break;
				}

				System.out.println("# Disconnecting...");
				pw.close();
				br.close();
				sock.close();
				System.out.println(" -> Connection closed.");
			} catch (Exception e) {
				sock.close();
				System.exit(0);
			}
		} catch (Exception e) {
			System.out.println("Connection failed.");
		}
		sc.close();
	}
}
