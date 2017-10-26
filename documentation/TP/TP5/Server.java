import java.net.*;
import java.io.*;

public class Server{

	private static BufferConcurrent buffer = new BufferConcurrent();

	public static void main(String[] args){
		
		Thread t1 = new Thread(new ServerCons(buffer));
		Thread t2 = new Thread(new ServerProd(buffer));
		t1.start();
		t2.start();

	}
}