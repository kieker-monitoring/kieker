package bookstoreApplication;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

public class MyPipe {
	private final String pipeName;
	private final LinkedBlockingQueue<PipeData> buffer =
                new LinkedBlockingQueue<PipeData>();

	public MyPipe(final String pipeName) {
		this.pipeName = pipeName;
	}

	public String getPipeName() {
		return this.pipeName;
	}

	public void put(final PipeData data) throws InterruptedException {
		this.buffer.put(data);
	}

	public PipeData poll(final long timeout) throws InterruptedException {
		return this.buffer.poll(timeout, TimeUnit.SECONDS);
	}

}
