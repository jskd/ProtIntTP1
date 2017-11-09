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
				Thread cmdp = new Thread(new CmdPrompt(pw));
				cmdp.start();
			

				while (true) {
					rep = br.readLine();
					System.out.println("\n[RECEIVED] : " + rep);
					displayOptions();

					if(rep.equals("BYE")) break;
				}

				System.out.println("\n# Disconnecting...");
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
	
	public static void displayOptions(){
		System.out.print("[s]Send [q]Quit : ");
	}
}
