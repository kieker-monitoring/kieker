package kieker.panalysis.concurrent;

import java.util.concurrent.atomic.AtomicLong;

import kieker.panalysis.base.Stage;

public class StageThread extends Thread {

	private final Stage stage;
	private final AtomicLong duration = new AtomicLong();

	public StageThread(final Stage stage) {
		this.stage = stage;
	}

	@Override
	public void run() {
		while (true) {
			final long start = System.currentTimeMillis();

			this.stage.execute();

			final long end = System.currentTimeMillis();
			this.duration.lazySet(end - start);
		}
	}

	public long getDuration() {
		return this.duration.get();
	}
}
