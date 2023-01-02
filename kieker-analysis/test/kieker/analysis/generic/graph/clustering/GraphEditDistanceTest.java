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

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.behavior.TestHelper;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

/**
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class GraphEditDistanceTest { // NOCS constructor

	private final GraphEditDistance<INode, IEdge> ged = new GraphEditDistance<>(new BasicCostFunction<>(1, 1));

	@Test
	public void distanceToIdenticalModelIsZeroTest() {
		MutableNetwork<INode, IEdge> model1 = TestHelper.createBehaviorModelA();
		MutableNetwork<INode, IEdge> model2 = TestHelper.createBehaviorModelA();

		MatcherAssert.assertThat(this.ged.calculate(model1, model2), Matchers.is(0.0));

		model1 = TestHelper.createBehaviorModelD();
		model2 = TestHelper.createBehaviorModelD();

		MatcherAssert.assertThat(this.ged.calculate(model1, model2), Matchers.is(0.0));

		model1 = TestHelper.createBehaviorModelE();
		model2 = TestHelper.createBehaviorModelE();

		MatcherAssert.assertThat(this.ged.calculate(model1, model2), Matchers.is(0.0));
	}

	@Test
	public void symmetryTest() {
		MutableNetwork<INode, IEdge> model1 = TestHelper.createBehaviorModelA();
		MutableNetwork<INode, IEdge> model2 = TestHelper.createBehaviorModelD();

		double result1 = this.ged.calculate(model1, model2);
		double result2 = this.ged.calculate(model2, model1);

		Assert.assertEquals(result1, result2, 0.0001);

		model1 = TestHelper.createBehaviorModelA();
		model2 = TestHelper.createBehaviorModelE();

		result1 = this.ged.calculate(model1, model2);
		result2 = this.ged.calculate(model2, model1);

		Assert.assertEquals(result1, result2, 0.0001);
	}

	@Test
	public void edgeRelevantTest() {
		final MutableNetwork<INode, IEdge> model1 = TestHelper.createBehaviorModelA();
		final MutableNetwork<INode, IEdge> model2 = TestHelper.createBehaviorModelD();
		Assert.assertTrue(this.ged.calculate(model1, model2) > 0);
	}

	@Test
	public void nodeRelevantTest() {
		final MutableNetwork<INode, IEdge> model1 = TestHelper.createBehaviorModelA();
		final MutableNetwork<INode, IEdge> model2 = TestHelper.createBehaviorModelE();
		Assert.assertTrue(this.ged.calculate(model1, model2) > 0);
	}

}
