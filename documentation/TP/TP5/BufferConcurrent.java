import java.util.LinkedList;

public class BufferConcurrent{

	private LinkedList<String> liste;

	public BufferConcurrent(){
		liste = new LinkedList<String>();
	}

	public synchronized void ajouter(String str){
		liste.add(str);
		notifyAll();
	}

	public synchronized String retirer(){
		while(liste.size() == 0){
			try{
				wait();
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
		return liste.removeFirst();
	}
}