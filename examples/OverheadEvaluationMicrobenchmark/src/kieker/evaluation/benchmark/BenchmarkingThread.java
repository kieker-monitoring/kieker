package kieker.evaluation.benchmark;

import java.util.concurrent.CountDownLatch;

import kieker.evaluation.monitoredApplication.MonitoredClass;

public final class BenchmarkingThread extends Thread {

	private MonitoredClass mc;
	private CountDownLatch doneSignal;
	private int recordedCalls;
	private int totalCalls;
	private long methodTime;
	private int recursionDepth;
	private long[] timings;
	private int j = 0;
	
	public synchronized int getIndexOfTimings() {
		return j;
	}

	public synchronized long[] getTimings() {
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
		timings = new long[recordedCalls];
	}

	@Override
	public void run() {
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
