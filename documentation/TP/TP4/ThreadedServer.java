import java.net.*;
import java.io.*;
import java.util.Random;

/**
 * @version 2016
 * TP4, code de depart (voir TP3, Exo 3)
 */
public class ThreadedServer {
	static int port = 10201;
	private static ServerSocket srvSock;

	public static void main(String[] args) {
		try {
			System.out.println("Establishing the server socket...");
			srvSock = new ServerSocket(port);
			System.out.println("Server socket established to " +
					srvSock.getInetAddress().getHostName() +
					" at port number " + 
					srvSock.getLocalPort() + ".\n");
			while (true) {
				try {
					Socket comSock = srvSock.accept();
					(new Thread(new PlayerService(comSock))).start();

				} catch (Exception e) {
					System.out.println("Problem with client.\n");
				}
			}
		} catch (Exception e) {
			System.out.println("Failed to create server socket:");
			e.printStackTrace();
		}
	}
}