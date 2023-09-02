/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.behavior.TestHelper;
import kieker.analysis.exception.InternalErrorException;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.mtree.IDistanceFunction;
import kieker.analysis.generic.graph.mtree.MTree;

import teetime.framework.test.StageTester;

/**
 * Only tested with identical models. The evaluation showed it is able to cluster more complex data
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class OpticsStageTest { // NOCS test class does not need a constructor

	private MTree<OpticsData<MutableNetwork<INode, IEdge>>> mTree;
	private List<OpticsData<MutableNetwork<INode, IEdge>>> models;

	@Before
	public void setUp() throws InternalErrorException {
		final List<MutableNetwork<INode, IEdge>> behaviorModels = new ArrayList<>();

		behaviorModels.add(TestHelper.createBehaviorModelA());
		behaviorModels.add(TestHelper.createBehaviorModelA());
		behaviorModels.add(TestHelper.createBehaviorModelA());
		behaviorModels.add(TestHelper.createBehaviorModelA());
		behaviorModels.add(TestHelper.createBehaviorModelA());

		behaviorModels.add(TestHelper.createBehaviorModelD());
		behaviorModels.add(TestHelper.createBehaviorModelD());
		behaviorModels.add(TestHelper.createBehaviorModelD());
		behaviorModels.add(TestHelper.createBehaviorModelD());
		behaviorModels.add(TestHelper.createBehaviorModelD());

		// noise
		behaviorModels.add(TestHelper.createBehaviorModelE());
		behaviorModels.add(TestHelper.createBehaviorModelE());

		this.models = new ArrayList<>();

		final IDistanceFunction<MutableNetwork<INode, IEdge>> distanceFunction = new GraphEditDistance<>(new BasicCostFunction<>(1, 1));

		final OPTICSDataGED<MutableNetwork<INode, IEdge>> opticsGed = new OPTICSDataGED<>(distanceFunction);

		for (final MutableNetwork<INode, IEdge> model : behaviorModels) {
			this.models.add(new OpticsData<>(model, opticsGed));
		}

		this.mTree = TestHelper.generateMTree(this.models);
	}

	@Test
	public void test() { // NOPMD uses MAtcherAssert

		final List<OpticsData<MutableNetwork<INode, IEdge>>> opticsPlot = OpticsStageTest.runClusterStage(this.models, this.mTree);
		// check, that no models are lost
		MatcherAssert.assertThat(opticsPlot.size(), Matchers.is(12));

		final Clustering<MutableNetwork<INode, IEdge>> clustering = OpticsStageTest.runExtractionStage(opticsPlot);

		// two noise objects
		MatcherAssert.assertThat(clustering.getNoise().size(), Matchers.is(2));

		// four clusters
		MatcherAssert.assertThat(clustering.getClusters().size(), Matchers.is(2));

		// every cluster contain 5 elements
		for (final Set<MutableNetwork<INode, IEdge>> cluster : clustering.getClusters()) {
			MatcherAssert.assertThat(cluster.size(), Matchers.is(5));
		}
	}

	public static List<OpticsData<MutableNetwork<INode, IEdge>>> runClusterStage(final List<OpticsData<MutableNetwork<INode, IEdge>>> models,
			final MTree<OpticsData<MutableNetwork<INode, IEdge>>> mTree) {
		// prepare input
		final List<List<OpticsData<MutableNetwork<INode, IEdge>>>> modelsInputList = new ArrayList<>();
		modelsInputList.add(models);

		// prepare input
		final List<MTree<OpticsData<MutableNetwork<INode, IEdge>>>> mTreeInputList = new ArrayList<>();
		mTreeInputList.add(mTree);

		// these are the clustering arguments
		final OpticsStage<MutableNetwork<INode, IEdge>> optics = new OpticsStage<>(0, 4);

		final List<List<OpticsData<MutableNetwork<INode, IEdge>>>> solutions = new ArrayList<>();

		StageTester.test(optics).and().send(modelsInputList).to(optics.getModelsInputPort()).and().send(mTreeInputList)
				.to(optics.getMTreeInputPort()).and().receive(solutions).from(optics.getOutputPort()).start();

		return solutions.get(0);
	}

	public static Clustering<MutableNetwork<INode, IEdge>> runExtractionStage(final List<OpticsData<MutableNetwork<INode, IEdge>>> models) {
		// prepare input
		final List<List<OpticsData<MutableNetwork<INode, IEdge>>>> inputList = new ArrayList<>();
		inputList.add(models);

		// clustering distance = 0 => only identical objects should be in a cluster
		final ExtractDBScanClustersStage<MutableNetwork<INode, IEdge>> extraction = new ExtractDBScanClustersStage<>(0);

		final List<Clustering<MutableNetwork<INode, IEdge>>> solutions = new ArrayList<>();

		StageTester.test(extraction).and().send(inputList).to(extraction.getInputPort()).and().receive(solutions)
				.from(extraction.getOutputPort()).start();

		return solutions.get(0);
	}
}
