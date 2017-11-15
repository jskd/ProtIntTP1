import java.io.*;
import java.net.*;

public class ServeurUDP {
  public static void main(String args[]) throws Exception {

    DatagramSocket serverSocket = new DatagramSocket(9876);

    byte[] sendData = new byte[1024];
    byte[] receiveData;
    
    while (true) {
      receiveData = new byte[1024];

      DatagramPacket receivePacket = new
        DatagramPacket(receiveData, receiveData.length);
      serverSocket.receive(receivePacket);

      String sentence = new String(receivePacket.getData());
      InetAddress ip_client = receivePacket.getAddress();
      int port_client = receivePacket.getPort();

      System.out.println(sentence + " from " + ip_client + ":" + port_client);

      String rep = "BIEN RECU";
      sendData = rep.getBytes();

      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ip_client, port_client);
      serverSocket.send(sendPacket);

    }
  }
}
