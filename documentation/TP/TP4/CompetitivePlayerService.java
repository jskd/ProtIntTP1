import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.Random;

public class CompetitivePlayerService implements Runnable{

	private Socket socket;
	private int number;

	public CompetitivePlayerService(Socket s, int number){
		this.socket = s;
		this.number = number;
	}

	@Override
	public void run(){
		try{
			System.out.println("New connection stablished with " + socket.getInetAddress().getHostName() + " (port " + socket.getPort() + "), trying to communicate...");
			BufferedReader comBR = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter comPW = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			int guess = -1;
			String str = "";
			String name = comBR.readLine();
			System.out.println("Player identified as " + name + ".");

			comPW.println("!");
			comPW.flush();

			while (true) {
				System.out.print("Waiting for a guess...");
				str = comBR.readLine();
				System.out.println(" " + str + ".");
				if (str.equals("quit")) break;
				try {
					guess = Integer.parseInt(str);
				} catch (NumberFormatException nfe) {
					comPW.println("?");
					comPW.flush();
					continue;
				}
				if (guess < number) {
					comPW.println("+");
				}
				else if (guess > number)
					comPW.println("-");
				else if (guess == number)
					break;
				comPW.flush();
			}

			if (guess == number) {
				System.out.println("The guess was correct.");
				comPW.println("=");
			} else if (str.toUpperCase().equals("QUIT")) {
				comPW.println(".");
				comPW.println(number);
				comPW.println("someone");
			} else {
				System.out.println("Bad input.");
				comPW.println("*");
			}
			
			comPW.flush();
			System.out.println("Closing connection...");
			comBR.close();
			comPW.close();
			socket.close();
			System.out.println("Connection closed.\n");
		}catch (Exception e){
			System.out.println(e);
		}
	}
}