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

import java.util.ArrayList;
import java.util.List;
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
public class ThroughputTimestampAnalysisTest {

	private static final int NUM_OBJECTS_TO_CREATE = 1000000;

	@Before
	public void before() {
		System.setProperty(LogFactory.CUSTOM_LOGGER_JVM, "NONE");
	}

	@Test
	public void testWithManyObjects() {
		final StopWatch stopWatch = new StopWatch();
		final List<TimestampObject> timestampObjects = new ArrayList<TimestampObject>(NUM_OBJECTS_TO_CREATE);

		final ThroughputTimestampAnalysis analysis = new ThroughputTimestampAnalysis();
		analysis.setNumNoopFilters(100);
		analysis.setTimestampObjects(timestampObjects);
		analysis.setInput(NUM_OBJECTS_TO_CREATE, new Callable<TimestampObject>() {
			public TimestampObject call() throws Exception {
				return new TimestampObject();
			}
		});
		analysis.init();

		stopWatch.start();
		try {
			analysis.start();
		} finally {
			stopWatch.end();
		}

		System.out.println("Duration: " + (stopWatch.getDuration() / 1000000) + " ms");

		long minDuration = Long.MAX_VALUE;
		long maxDuration = Long.MIN_VALUE;
		long sum = 0;
		for (int i = timestampObjects.size() / 2; i < timestampObjects.size(); i++) {
			final TimestampObject timestampObject = timestampObjects.get(i);
			final long duration = timestampObject.getStopTimestamp() - timestampObject.getStartTimestamp();
			minDuration = Math.min(duration, minDuration);
			maxDuration = Math.max(duration, maxDuration);
			sum += duration;
		}

		System.out.println("min: " + (minDuration / 1000) + " µs");
		System.out.println("max: " + (maxDuration / 1000) + " µs");
		final long avgDur = sum / (timestampObjects.size() / 2);
		System.out.println("avg duration: " + (avgDur / 1000) + " µs");
	}

}
