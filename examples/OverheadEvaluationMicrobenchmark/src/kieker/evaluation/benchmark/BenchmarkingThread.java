package kieker.evaluation.benchmark;

import java.util.concurrent.CountDownLatch;

import kieker.evaluation.monitoredApplication.MonitoredClass;

public class BenchmarkingThread extends Thread {

	private MonitoredClass mc;
	private CountDownLatch doneSignal;
	private int recordedCalls;
	private int totalCalls;
	private long methodTime;
	private long[] timings;
	private int j = 0;

	public synchronized int getIndexOfTimings() {
		return j;
	}

	public synchronized long[] getTimings() {
		return timings;
	}

	public BenchmarkingThread(MonitoredClass mc, int totalCalls, int recordedCalls, long methodTime, CountDownLatch doneSignal) {
		super();
		this.mc = mc;
		this.doneSignal = doneSignal;
		this.totalCalls = totalCalls;
		this.recordedCalls = recordedCalls;
		this.methodTime = methodTime;
		timings = new long[recordedCalls];
	}

	@Override
	public void run() {
		long start_ns, stop_ns;
		for (int i = 0; i < totalCalls; i++) {
			start_ns = System.nanoTime();
			mc.monitoredMethod(methodTime);
			stop_ns = System.nanoTime();
			timings[j] = stop_ns - start_ns;
			j = (j + 1) % recordedCalls;
		}
		doneSignal.countDown();
	}

}