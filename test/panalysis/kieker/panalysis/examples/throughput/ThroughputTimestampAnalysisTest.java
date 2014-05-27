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
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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

	private static final int NUM_OBJECTS_TO_CREATE = 100000;

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

		final long[] sortedDurations = new long[timestampObjects.size() / 2];
		long minDuration = Long.MAX_VALUE;
		long maxDuration = Long.MIN_VALUE;
		long sum = 0;
		for (int i = timestampObjects.size() / 2; i < timestampObjects.size(); i++) {
			final TimestampObject timestampObject = timestampObjects.get(i);
			final long duration = timestampObject.getStopTimestamp() - timestampObject.getStartTimestamp();
			sortedDurations[i - (timestampObjects.size() / 2)] = duration;
			minDuration = Math.min(duration, minDuration);
			maxDuration = Math.max(duration, maxDuration);
			sum += duration;
		}

		Arrays.sort(sortedDurations);

		final Map<Double, Long> quintileValues = new LinkedHashMap<Double, Long>();
		final double[] quintiles = { 0.00, 0.25, 0.50, 0.75, 1.00 };
		for (final double quartile : quintiles) {
			final int index = (int) ((sortedDurations.length - 1) * quartile);
			quintileValues.put(quartile, sortedDurations[index]);
		}

		System.out.println("min: " + (minDuration / 1000) + " 탎");
		System.out.println("max: " + (maxDuration / 1000) + " 탎");
		final long avgDur = sum / (timestampObjects.size() / 2);
		System.out.println("avg duration: " + (avgDur / 1000) + " 탎");

		for (final Entry<Double, Long> entry : quintileValues.entrySet()) {
			System.out.println((entry.getKey() * 100) + " % : " + (entry.getValue() / 1000) + " 탎");
		}

		final double z = 1.96; // 0.975
		final double confidenceNiveau = 0.95;
		final double confidenceWidth = this.getConfidenceWidth(z, confidenceNiveau, this.getVariance(sortedDurations, avgDur), sortedDurations.length);
		System.out.println("[" + ((avgDur - confidenceWidth) / 1000) + " 탎," + ((avgDur + confidenceWidth) / 1000) + " 탎]");
	}

	public double getVariance(final long[] values, final long avgValue) {
		double sum = 0;
		for (final long val : values) {
			final long diff = val - avgValue;
			sum += (diff * 2) / (values.length - 1);
		}
		return sum;
	}

	//
	// public double getS(final long[] values, final long avgValue) {
	// final double variance = this.getVariance(values, avgValue);
	// return Math.sqrt(variance);
	// }

	public double getConfidenceWidth(final double z, final double confidenceNiveau, final double variance, final long n) {
		return z * (1 - (confidenceNiveau / 2)) * Math.sqrt(variance / n);
	}
}
