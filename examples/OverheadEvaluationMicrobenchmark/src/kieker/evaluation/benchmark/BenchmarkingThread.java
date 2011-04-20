package kieker.evaluation.benchmark;

import java.util.concurrent.CountDownLatch;

import kieker.evaluation.monitoredApplication.MonitoredClass;

public final class BenchmarkingThread extends Thread {

	private final MonitoredClass mc;
	private final CountDownLatch doneSignal;
	private final int recordedCalls;
	private final int totalCalls;
	private final long methodTime;
	private final int recursionDepth;
	private final long[] timings;
	private int j = 0;
	
	public final synchronized int getIndexOfTimings() {
		return j;
	}

	public final synchronized long[] getTimings() {
		return timings;
	}

	public BenchmarkingThread(final MonitoredClass mc, final int totalCalls, final int recordedCalls, final long methodTime, final int recursionDepth, final CountDownLatch doneSignal) {
		super();
		this.mc = mc;
		this.doneSignal = doneSignal;
		this.totalCalls = totalCalls;
		this.recordedCalls = recordedCalls;
		this.methodTime = methodTime;
		this.recursionDepth = recursionDepth;
		this.timings = new long[recordedCalls];
	}

	@Override
	public final void run() {
		long start_ns, stop_ns;
		for (int i = 0; i < totalCalls; i++) {
			start_ns = System.nanoTime();
			mc.monitoredMethod(methodTime, recursionDepth);
			stop_ns = System.nanoTime();
			timings[j] = stop_ns - start_ns;
			j = (j + 1) % recordedCalls;
		}
		doneSignal.countDown();
	}

}
