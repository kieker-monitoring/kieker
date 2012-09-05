/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.evaluation.benchmark;

import java.util.concurrent.CountDownLatch;

import kieker.evaluation.monitoredApplication.MonitoredClass;

/**
 * @author Jan Waller
 */
public final class BenchmarkingThread extends Thread {

	private final MonitoredClass mc;
	private final CountDownLatch doneSignal;
	private final int totalCalls;
	private final long methodTime;
	private final int recursionDepth;
	private final long[] timings;

	public BenchmarkingThread(final MonitoredClass mc, final int totalCalls, final long methodTime, final int recursionDepth, final CountDownLatch doneSignal) {
		super();
		this.mc = mc;
		this.doneSignal = doneSignal;
		this.totalCalls = totalCalls;
		this.methodTime = methodTime;
		this.recursionDepth = recursionDepth;
		this.timings = new long[totalCalls];
	}

	public final long[] getTimings() {
		synchronized (this) {
			return this.timings.clone();
		}
	}

	@Override
	public final void run() {
		long start_ns;
		long stop_ns;
		for (int i = 0; i < this.totalCalls; i++) {
			start_ns = System.nanoTime();
			this.mc.monitoredMethod(this.methodTime, this.recursionDepth);
			stop_ns = System.nanoTime();
			this.timings[i] = stop_ns - start_ns;
			if ((i % 100000) == 0) {
				System.out.println(i); // NOPMD (System.out)
			}
		}
		this.doneSignal.countDown();
	}

}
