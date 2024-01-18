/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.clustering.mtree;

import org.junit.Assert;
import org.junit.Test;

public class TrimmedAlgorithmTest {

	// 2 is the best candidate, while 3 has the same distance, it comes later so the first best candidate is selected
	private static final Object BEST_CANDIDATE = 2;

	@Test
	public void testCalculation() {
		final Integer[] models = { 1, 2, 3, 5 };
		final IDistanceFunction<Integer> distanceFunction = new IDistanceFunction<>() {

			@Override
			public double calculate(final Integer data1, final Integer data2) {
				return Math.abs(data1 - data2);
			}

		};
		final TrimmedAlgorithm<Integer> algorithm = new TrimmedAlgorithm<>(models, distanceFunction);
		Assert.assertEquals(BEST_CANDIDATE, algorithm.calculate());
	}

}
