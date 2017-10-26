import java.net.*;
import java.io.*;
import java.lang.*;
import java.util.LinkedList;
import java.util.Random;

public class GameServer{

	private LinkedList<Thread> players;
	private int number;

	public GameServer(){
		Random rand = new Random();
		this.number = rand.nextInt(100) + 1;
		this.players = new LinkedList<Thread>();
		System.out.println("The number chosen is " + number + ".");
	}

	public void addPlayer(Socket s){
		Thread player = new Thread(new CompetitivePlayerService(s, number));
		players.add(player);
		player.start();
	}

	public int nbPlayers(){
		return players.size();
	}
}