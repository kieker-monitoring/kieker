package de.chw.util;

public class StopWatch {

	private long startTimeInMs;
	private long endTimeInMs;

	public void start() {
		this.startTimeInMs = System.currentTimeMillis();
	}

	public void end() {
		this.endTimeInMs = System.currentTimeMillis();
	}

	public long getDuration() {
		return this.endTimeInMs - this.startTimeInMs;
	}
}
