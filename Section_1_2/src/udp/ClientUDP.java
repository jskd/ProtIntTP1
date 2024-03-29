import java.net.*;
import java.util.Scanner;

public class ClientUDP {
  public static void main(String args[]) throws Exception {
    Scanner inFromUser = new Scanner(System.in);

    DatagramSocket clientSocket = new DatagramSocket();
    InetAddress IPAddress = InetAddress.getByName("localhost");

    byte[] sendData = new byte[1024];
    byte[] receiveData;

    while(true){
      receiveData = new byte[1024];

      String sentence = inFromUser.nextLine();
      sendData = sentence.getBytes();

      DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 9876);
      clientSocket.send(sendPacket);

      DatagramPacket receivePacket = new
        DatagramPacket(receiveData, receiveData.length);
      clientSocket.receive(receivePacket);

      sentence = new String(receivePacket.getData());
      System.out.println(sentence);
    }

  }
}
