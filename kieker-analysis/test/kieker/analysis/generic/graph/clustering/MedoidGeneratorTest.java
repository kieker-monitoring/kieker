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
package kieker.analysis.generic.graph.clustering;

import java.util.HashSet;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import teetime.framework.test.StageTester;

public class MedoidGeneratorTest {

	@Test
	public void test() {
		final MedoidGenerator<Integer> generator = new MedoidGenerator<>(ClusteringHelper.integerDistanceFunction());
		final Clustering<Integer> input = this.createClustering();
		StageTester.test(generator).send(input).to(generator.getInputPort()).start();

		Assert.assertThat(generator.getOutputPort(), StageTester.produces(2, 3, 2));
	}

	private Clustering<Integer> createClustering() {
		final Clustering<Integer> clustering = new Clustering<>();
		clustering.addCluster(this.createIntegerSet(new int[] { 1, 2, 3, 4 }));
		clustering.addCluster(this.createIntegerSet(new int[] { 2, 3, 4 }));
		clustering.addCluster(this.createIntegerSet(new int[] { 1, 2, 3 }));

		return clustering;
	}

	private Set<Integer> createIntegerSet(final int[] values) {
		final Set<Integer> result = new HashSet<>();
		for (final int value : values) {
			result.add(value);
		}
		return result;
	}
}
