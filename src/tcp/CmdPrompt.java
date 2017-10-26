import java.io.*;
import java.net.*;
import java.util.*;

public class CmdPrompt implements Runnable{
	private PrintWriter pw;

	public CmdPrompt(PrintWriter pw){
		this.pw = pw;
	}

	@Override
	public void run(){
		try{
			Scanner sc = new Scanner(System.in);

			while(true){
				String cmd = sc.nextLine();
				
				if(cmd.equals("q")){
					pw.println("QUIT");
					pw.flush();
					break;
				}
			}
		
		} catch (Exception e){
			System.out.println("Error");
			e.printStackTrace();
		}
	}


}
