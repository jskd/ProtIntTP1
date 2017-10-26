import java.io.*;
import java.net.*;

public class ServeurUDP {
  public static void main(String args[]) throws Exception {
    DatagramSocket serverSocket = new DatagramSocket(9876);
    byte[] receiveData = new byte[1024];
    
    while (true) {
      DatagramPacket receivePacket = new
        DatagramPacket(receiveData, receiveData.length);
      serverSocket.receive(receivePacket);
      String sentence = new String(receivePacket.getData());
      System.out.println(sentence);
    }
  }
}
