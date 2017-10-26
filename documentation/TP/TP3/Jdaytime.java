import java.net.Socket;
import java.lang.Process;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class Jdaytime{
	public static void main(String[] args){

		if(args.length >= 1){
			try{
				String host = args[0];

				Process process = Runtime.getRuntime().exec("telnet "+host+" 13");
				BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
				String line = stdout.readLine();

				while(line != null){
					System.out.println(line);
					line = stdout.readLine();
				}

				stdout.close();

			}catch (Exception e){
				System.err.println(e);
				System.exit(-1);
			}
		}
		else{
			System.err.println("Usage: Jdaytime <host>");
		}

	}
}