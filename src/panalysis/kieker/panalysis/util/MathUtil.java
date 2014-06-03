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

import java.util.List;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class MathUtil {

	private MathUtil() {
		// utility class
	}

	public static double getVariance(final List<Long> values, final long avgValue) {
		double sum = 0;
		for (final long val : values) {
			final long diff = val - avgValue;
			sum += (diff * diff) / (values.size() - 1);
		}
		return sum;
	}

	public static double getConfidenceWidth(final double z, final double variance, final long n) {
		return z * Math.sqrt(variance / n);
	}

	public static double getConfidenceWidth(final double z, final List<Long> values, final long avgValue) {
		final double variance = MathUtil.getVariance(values, avgValue);
		final double confidenceWidth = MathUtil.getConfidenceWidth(z, variance, values.size());
		return confidenceWidth;
	}
}
