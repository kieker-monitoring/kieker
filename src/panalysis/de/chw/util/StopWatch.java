package de.chw.util;

public final class StopWatch {

	private long startTimeInMs;
	private long endTimeInMs;

	public final void start() {
		this.startTimeInMs = System.nanoTime();
	}

	public final void end() {
		this.endTimeInMs = System.nanoTime();
	}

	public final long getDuration() {
		return this.endTimeInMs - this.startTimeInMs;
	}
}
