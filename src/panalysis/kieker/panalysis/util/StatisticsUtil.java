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
package kieker.panalysis.util;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import kieker.panalysis.examples.throughput.TimestampObject;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class StatisticsUtil {

	/**
	 * @since 1.10
	 */
	private StatisticsUtil() {
		// utility class
	}

	public static void printStatistics(final long overallDurationInNs, final List<TimestampObject> timestampObjects) {
		System.out.println("Duration: " + TimeUnit.NANOSECONDS.toMillis(overallDurationInNs) + " ms");

		final long[] sortedDurationsInNs = new long[timestampObjects.size() / 2];
		long minDurationInNs = Long.MAX_VALUE;
		long maxDurationInNs = Long.MIN_VALUE;
		long sumInNs = 0;
		for (int i = timestampObjects.size() / 2; i < timestampObjects.size(); i++) {
			final TimestampObject timestampObject = timestampObjects.get(i);
			final long durationInNs = timestampObject.getStopTimestamp() - timestampObject.getStartTimestamp();
			sortedDurationsInNs[i - (timestampObjects.size() / 2)] = durationInNs;
			minDurationInNs = Math.min(durationInNs, minDurationInNs);
			maxDurationInNs = Math.max(durationInNs, maxDurationInNs);
			sumInNs += durationInNs;
		}

		Arrays.sort(sortedDurationsInNs);

		final Map<Double, Long> quintileValues = new LinkedHashMap<Double, Long>();
		final double[] quintiles = { 0.00, 0.25, 0.50, 0.75, 1.00 };
		for (final double quintile : quintiles) {
			final int index = (int) ((sortedDurationsInNs.length - 1) * quintile);
			quintileValues.put(quintile, sortedDurationsInNs[index]);
		}

		System.out.println("min: " + TimeUnit.NANOSECONDS.toMicros(minDurationInNs) + " 탎");
		System.out.println("max: " + TimeUnit.NANOSECONDS.toMicros(maxDurationInNs) + " 탎");
		final long avgDurInNs = sumInNs / (timestampObjects.size() / 2);
		System.out.println("avg duration: " + TimeUnit.NANOSECONDS.toMicros(avgDurInNs) + " 탎");

		for (final Entry<Double, Long> entry : quintileValues.entrySet()) {
			System.out.println((entry.getKey() * 100) + " % : " + TimeUnit.NANOSECONDS.toMicros(entry.getValue()) + " 탎");
		}

		final double z = 1.96; // for alpha = 0.05
		final double variance = MathUtil.getVariance(sortedDurationsInNs, avgDurInNs);
		final long confidenceWidthInNs = (long) MathUtil.getConfidenceWidth(z, variance, sortedDurationsInNs.length);

		System.out.println("confidenceWidth: " + confidenceWidthInNs + " ns");
		System.out.println("[" + TimeUnit.NANOSECONDS.toMicros(avgDurInNs - confidenceWidthInNs) + " 탎, "
				+ TimeUnit.NANOSECONDS.toMicros(avgDurInNs + confidenceWidthInNs) + " 탎]");
	}
}
