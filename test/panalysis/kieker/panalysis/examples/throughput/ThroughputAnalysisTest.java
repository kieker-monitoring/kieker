/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.examples.throughput;

import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;

import de.chw.util.StopWatch;

import kieker.common.logging.LogFactory;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ThroughputAnalysisTest {

	private static final int NUM_OBJECTS_TO_CREATE = 100000;
	private static final int numRuns = 2;

	@Before
	public void before() {
		System.setProperty(LogFactory.CUSTOM_LOGGER_JVM, "NONE");
	}

	@Test
	public void testWithMultipleRuns() {
		final StopWatch stopWatch = new StopWatch();
		final long[] durations = new long[numRuns];

		for (int i = 0; i < numRuns; i++) {
			System.out.println("Run " + i);
			final ThroughputAnalysis<Object> analysis = new ThroughputAnalysis<Object>();
			analysis.setNumNoopFilters(800);
			analysis.setInput(NUM_OBJECTS_TO_CREATE, new Callable<Object>() {
				public Object call() throws Exception {
					return new Object();
				}
			});
			analysis.init();

			stopWatch.start();
			try {
				analysis.start();
			} finally {
				stopWatch.end();
			}

			durations[i] = stopWatch.getDuration();
		}

		// for (final long dur : durations) {
		// System.out.println("Duration: " + (dur / 1000) + " µs");
		// }

		long sum = 0;
		for (int i = durations.length / 2; i < durations.length; i++) {
			sum += durations[i];
		}

		final long avgDur = sum / (numRuns / 2);
		System.out.println("avg duration: " + (avgDur / 1000) + " µs");
	}

}
