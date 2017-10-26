import java.net.*;
import java.io.*;

public class Server{

	public static void main(String[] args){
		try{
			DatagramSocket dso = new DatagramSocket(5555);
			byte[] data = new byte[1024];

			DatagramPacket paquet = new DatagramPacket(data, data.length);

			while(true){
				dso.receive(paquet);
				String st = new String(paquet.getData(), 0, paquet.getLength());
				InetSocketAddress ia = (InetSocketAddress)paquet.getSocketAddress();


				String mess = ia.getHostName()+":"+ia.getPort()+" "+st;
				paquet = new DatagramPacket(mess.getBytes(), mess.length(), ia);
				dso.send(paquet);

			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}	