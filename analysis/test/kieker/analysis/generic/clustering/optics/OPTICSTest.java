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
package kieker.analysis.generic.clustering.optics;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.generic.clustering.ClusteringHelper;
import kieker.analysis.generic.clustering.mtree.MTree;
import kieker.analysis.generic.graph.clustering.OPTICSDataGED;

public class OPTICSTest {

	private static final double MAX_DISTANCE = 2.0;
	private static final int MIN_PT_S = 1;

	@Test
	public void test() {
		final MTree<OpticsData<Integer>> mtree = new MTree<>(ClusteringHelper.opticsIntegerDistanceFunction(),
				ClusteringHelper.opticsIntegerSplitFunction());
		final List<OpticsData<Integer>> models = new ArrayList<>();
		final OPTICSDataGED<Integer> ged = new OPTICSDataGED<>(ClusteringHelper.integerDistanceFunction());
		for (final int value : new int[] { 1, 2, 2, 3, 3, 4, 5 }) {
			models.add(new OpticsData<>(value, ged));
		}

		final OPTICS<Integer> optics = new OPTICS<>(mtree, MAX_DISTANCE, MIN_PT_S, models);
		final List<OpticsData<Integer>> results = optics.calculate();

		Assert.assertEquals(7, results.size());
	}

}
