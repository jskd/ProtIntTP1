import java.net.*;
import java.io.*;
import java.util.Scanner;

public class ClientProd{

	public static void main(String[] args){
		String servAddr = "127.0.0.1";
		int servPort = 6868;	
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Connecting to server...");

		try {
			Socket sock = new Socket(servAddr, servPort);
			System.out.println("Connection established.");

			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(sock.getInputStream()));
				PrintWriter pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));

				while(true){
					pw.println(sc.nextLine());
					pw.flush();
				}
				
			}
			catch (Exception e) {
				System.out.println("Communication failed. Quitting...");
				sock.close();
				System.out.println("Connection closed. Try again later.");
				System.exit(0);
			}
		}
		catch (Exception e) {
			System.out.println("Connection failed. Try again later.");
		}
	}
}