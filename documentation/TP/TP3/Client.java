import java.net.*;
import java.io.*;
import java.util.Scanner;

public class Client{
	public static void main(String args[]){

		try{

			String host = InetAddress.getLocalHost().getHostName();
			Socket socket = new Socket(host,3434);

			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			Scanner sc = new Scanner(System.in);

			while(true){
				String mess = br.readLine();

				if(mess.equals("=")){
					System.out.println("Bravo vous avez trouvé !");
					break;
				}

				System.out.println(mess);
				String rep = sc.nextLine();

				pw.print(rep+"\n");
				pw.flush();
			}

			pw.close();
			br.close();
			socket.close();

		}catch (Exception e){
			System.out.println(e);
		}

	}
}