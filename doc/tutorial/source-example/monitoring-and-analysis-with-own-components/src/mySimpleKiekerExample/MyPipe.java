package mySimpleKiekerExample;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyPipe {
	private String pipeName;
	private LinkedBlockingQueue<Object[]> storage = new LinkedBlockingQueue<Object[]>();

	public MyPipe(String pipeName) {
		this.pipeName = pipeName;
	}

	public String getPipeName() {
		return pipeName;
	}

	public void put(Object[] obj) throws InterruptedException {
		storage.put(obj);
	}

	public Object[] poll(long timeout) throws InterruptedException {
		return storage.poll(timeout, TimeUnit.SECONDS);
	}

}
