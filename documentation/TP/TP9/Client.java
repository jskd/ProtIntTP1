import java.net.*;
import java.io.*;
import java.util.*;

public class Client{

	public static void main(String[] args){

		Scanner sc = new Scanner(System.in);
		String mess = sc.nextLine();

		try{
			DatagramSocket dso = new DatagramSocket(6666);

			InetSocketAddress ai = new InetSocketAddress("127.0.0.1", 5555);
			DatagramPacket paquet = new DatagramPacket(mess.getBytes(), mess.length(), ai);
			dso.send(paquet);

			byte[] data = new byte[1024];
			paquet = new DatagramPacket(data, data.length);
			dso.receive(paquet);

			String echo = new String(paquet.getData(), 0, paquet.getLength());
			System.out.println(echo);

		}catch (Exception e){
			e.printStackTrace();
		}
	}
}