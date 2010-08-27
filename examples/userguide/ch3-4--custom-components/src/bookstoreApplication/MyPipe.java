package bookstoreApplication;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyPipe {
	private final String pipeName;
	private final LinkedBlockingQueue<Object[]> storage =
                new LinkedBlockingQueue<Object[]>();

	public MyPipe(final String pipeName) {
		this.pipeName = pipeName;
	}

	public String getPipeName() {
		return pipeName;
	}

	public void put(final Object[] obj) throws InterruptedException {
		storage.put(obj);
	}

	public Object[] poll(final long timeout) throws InterruptedException {
		return storage.poll(timeout, TimeUnit.SECONDS);
	}

}
