import java.net.*;
import java.io.*;
import java.util.Random;

public class Serveur{
	public static void main(String args[]){

		try{
			ServerSocket serv = new ServerSocket(3434);
			Random rand = new Random();
			int number = rand.nextInt(100)+1;
				
			Socket socket = serv.accept();
			BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));

			pw.print("Entrez un nombre entre 1-100 :\n");
			pw.flush();

			while(true){
				String mess = br.readLine();
				if(mess == null)
					break;
				
				try{
					int test = Integer.parseInt(mess);

					if(test > number){
						pw.print("-\n");
						pw.flush();
					}
					else if(test < number){
						pw.print("+\n");
						pw.flush();
					}
					else{
						pw.print("=\n");
						pw.flush();
						break;
					}

				}catch (Exception e){
					pw.print("Ce n'est pas un nombre.\n");
					pw.flush();
				}
			}

			br.close();
			pw.close();
			socket.close();

		}catch (Exception e){
			System.out.println(e);
		}

	}
}