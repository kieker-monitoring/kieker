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
package kieker.analysis.behavior.clustering;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.behavior.TestHelper;
import kieker.analysis.behavior.model.BehaviorModel;

/**
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class GraphEditDistanceTest { // NOCS constructor

	private final GraphEditDistance ged = new GraphEditDistance();

	@Test
	public void distanceToIdenticalModelIsZeroTest() {
		BehaviorModel model1 = TestHelper.createBehaviorModelA();
		BehaviorModel model2 = TestHelper.createBehaviorModelA();

		MatcherAssert.assertThat(this.ged.calculate(model1, model2), Matchers.is(0.0));

		model1 = TestHelper.createBehaviorModelB();
		model2 = TestHelper.createBehaviorModelB();

		MatcherAssert.assertThat(this.ged.calculate(model1, model2), Matchers.is(0.0));

		model1 = TestHelper.createBehaviorModelC();
		model2 = TestHelper.createBehaviorModelC();

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
		BehaviorModel model1 = TestHelper.createBehaviorModelA();
		BehaviorModel model2 = TestHelper.createBehaviorModelB();

		double result1 = this.ged.calculate(model1, model2);
		double result2 = this.ged.calculate(model2, model1);

		Assert.assertEquals(result1, result2);

		model1 = TestHelper.createBehaviorModelA();
		model2 = TestHelper.createBehaviorModelC();

		result1 = this.ged.calculate(model1, model2);
		result2 = this.ged.calculate(model2, model1);

		Assert.assertEquals(result1, result2);

		model1 = TestHelper.createBehaviorModelA();
		model2 = TestHelper.createBehaviorModelD();

		result1 = this.ged.calculate(model1, model2);
		result2 = this.ged.calculate(model2, model1);

		Assert.assertEquals(result1, result2);

		model1 = TestHelper.createBehaviorModelA();
		model2 = TestHelper.createBehaviorModelE();

		result1 = this.ged.calculate(model1, model2);
		result2 = this.ged.calculate(model2, model1);

		Assert.assertEquals(result1, result2);
	}

	@Test
	public void valueRelevantTest() {
		final BehaviorModel model1 = TestHelper.createBehaviorModelA();
		final BehaviorModel model2 = TestHelper.createBehaviorModelB();
		Assert.assertTrue(this.ged.calculate(model1, model2) > 0);
	}

	@Test
	public void parameterRelevantTest() {
		final BehaviorModel model1 = TestHelper.createBehaviorModelA();
		final BehaviorModel model2 = TestHelper.createBehaviorModelC();
		Assert.assertTrue(this.ged.calculate(model1, model2) > 0);
	}

	@Test
	public void edgeRelevantTest() {
		final BehaviorModel model1 = TestHelper.createBehaviorModelA();
		final BehaviorModel model2 = TestHelper.createBehaviorModelD();
		Assert.assertTrue(this.ged.calculate(model1, model2) > 0);
	}

	@Test
	public void nodeRelevantTest() {
		final BehaviorModel model1 = TestHelper.createBehaviorModelA();
		final BehaviorModel model2 = TestHelper.createBehaviorModelE();
		Assert.assertTrue(this.ged.calculate(model1, model2) > 0);
	}

}
