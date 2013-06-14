package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observer;

/**
 * @author Bjoern Weissenfels
 */
public class UpdateThread extends Thread{
	
	private long timeout;
	private List<Observer> observers;
	private volatile boolean terminated;
	
	public UpdateThread(long timeout){
		this.timeout = timeout;
		this.observers = Collections.synchronizedList(new ArrayList<Observer>());
		this.terminated = false;
	}
	
	@Override
	public void run(){
		while(!this.terminated){
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.notifyObservers();
		}
	}
	
	private void notifyObservers(){
		for(Observer o : this.observers){
			o.update(null, null);
		}
	}
	
	public void addObserver(Observer observer){
		if(!this.observers.contains(observer)){
			this.observers.add(observer);
		}
	}
	
	public void deleteObserver(Observer observer){
		this.observers.remove(observer);
	}
	
	public void terminate(){
		this.terminated = true;
	}

}
