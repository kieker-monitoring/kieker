/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.generic.clustering;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Ignore;
import org.junit.Test;

import kieker.analysis.generic.clustering.optics.OpticsData;

import teetime.framework.test.StageTester;

public class ExtractDBScanClustersStageTest {

	private static final double CLUSTER_DISTANCE = 1.0;

	@Ignore
	@Test
	public void test() { // NOPMD
		final ExtractDBScanClustersStage<Integer> stage = new ExtractDBScanClustersStage<>(CLUSTER_DISTANCE);
		final List<OpticsData<Integer>> cluster = this.createCluster();
		final Clustering<Integer> output = this.createOutput();
		StageTester.test(stage).and().send(cluster, null).to(stage.getInputPort()).start();

		MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(output));
	}

	private List<OpticsData<Integer>> createCluster() {
		return null;
	}

	private Clustering<Integer> createOutput() {
		return null;
	}

}
