package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author Bjoern Weissenfels
 */
public class UpdateThread extends Observable implements Runnable{
	
	private long timeout;
	private boolean terminated;
	private List<Observer> observer;
	
	public UpdateThread(long timeout){
		this.timeout = timeout;
		this.observer = Collections.synchronizedList(new ArrayList<Observer>());
		this.terminated = false;
	}
	
	@Override
	public void run(){
		System.out.println("a"+this.observer.size());
		while(!this.terminated){
			System.out.println("c"+this.observer.size());
			try {
				this.wait(this.timeout);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("d"+this.observer.size());
			this.notifyObservers();
		}
	}
	
	public void terminate(){
		this.terminated = true;
	}

}
