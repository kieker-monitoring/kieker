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

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import kieker.analysis.generic.graph.mtree.IDistanceFunction;

import teetime.framework.test.StageTester;

public class MTreeGeneratorStageTest { // NOCS tests do not need constructors

	@Test
	public void testMTreeGeneratorStage() {
		final List<Integer> list = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 10, 12, 15 });
		final IDistanceFunction<Integer> distanceFunction = ClusteringHelper.integerDistanceFunction();
		final MTreeGeneratorStage<Integer> stage = new MTreeGeneratorStage<>(distanceFunction);
		StageTester.test(stage).and().send(list, list).to(stage.getInputPort()).start();
	}

}
