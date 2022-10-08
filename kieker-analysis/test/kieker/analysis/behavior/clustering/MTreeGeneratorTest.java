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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import kieker.analysis.behavior.clustering.GraphEditDistance;
import kieker.analysis.behavior.clustering.MTreeGeneratorStage;
import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.mtree.MTree;

import teetime.framework.test.StageTester;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class MTreeGeneratorTest {

	private MTreeGeneratorStage<BehaviorModel> mtreeGenerator;
	private GraphEditDistance mockGED;

	private BehaviorModel model1;
	private BehaviorModel model2;
	private BehaviorModel model3;
	private BehaviorModel model4;

	@Before
	public void setup() {
		this.mockGED = Mockito.mock(GraphEditDistance.class);

		this.mtreeGenerator = new MTreeGeneratorStage<>(this.mockGED);

		this.model1 = Mockito.mock(BehaviorModel.class);
		this.model2 = Mockito.mock(BehaviorModel.class);
		this.model3 = Mockito.mock(BehaviorModel.class);
		this.model4 = Mockito.mock(BehaviorModel.class);

		Mockito.when(this.mockGED.calculate(this.model1, this.model1)).thenReturn(0.0);
		Mockito.when(this.mockGED.calculate(this.model2, this.model2)).thenReturn(0.0);
		Mockito.when(this.mockGED.calculate(this.model3, this.model3)).thenReturn(0.0);
		Mockito.when(this.mockGED.calculate(this.model4, this.model4)).thenReturn(0.0);

		Mockito.when(this.mockGED.calculate(this.model1, this.model2)).thenReturn(1.0);
		Mockito.when(this.mockGED.calculate(this.model1, this.model3)).thenReturn(11.0);
		Mockito.when(this.mockGED.calculate(this.model1, this.model4)).thenReturn(12.0);

		Mockito.when(this.mockGED.calculate(this.model2, this.model1)).thenReturn(1.0);
		Mockito.when(this.mockGED.calculate(this.model2, this.model3)).thenReturn(10.0);
		Mockito.when(this.mockGED.calculate(this.model2, this.model4)).thenReturn(11.0);

		Mockito.when(this.mockGED.calculate(this.model3, this.model1)).thenReturn(11.0);
		Mockito.when(this.mockGED.calculate(this.model3, this.model2)).thenReturn(10.0);
		Mockito.when(this.mockGED.calculate(this.model3, this.model4)).thenReturn(1.0);

		Mockito.when(this.mockGED.calculate(this.model4, this.model1)).thenReturn(12.0);
		Mockito.when(this.mockGED.calculate(this.model4, this.model2)).thenReturn(11.0);
		Mockito.when(this.mockGED.calculate(this.model4, this.model3)).thenReturn(1.0);

	}

	@Test
	public void emptyTest() {
		final List<BehaviorModel> list = new ArrayList<>();
		final List<List<BehaviorModel>> input = new ArrayList<>();
		input.add(list);

		final List<MTree<BehaviorModel>> solutions = this.startAndGetSolutions(input);

		final Iterator<MTree<BehaviorModel>.ResultItem> iter = solutions.get(0).getNearest(this.model1).iterator();

		MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
		MatcherAssert.assertThat(solutions.size(), Matchers.is(1));
	}

	@Test
	public void oneElementTest() {
		final List<BehaviorModel> list = new ArrayList<>();
		list.add(this.model1);
		final List<List<BehaviorModel>> input = new ArrayList<>();
		input.add(list);
		final List<MTree<BehaviorModel>> solutions = this.startAndGetSolutions(input);

		final Iterator<MTree<BehaviorModel>.ResultItem> iter = solutions.get(0).getNearest(this.model1).iterator();

		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model1));
		MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
		MatcherAssert.assertThat(solutions.size(), Matchers.is(1));

	}

	@Test
	public void fourElementTest() {
		final List<BehaviorModel> list = new ArrayList<>();
		list.add(this.model1);
		list.add(this.model2);
		list.add(this.model3);
		list.add(this.model4);

		final List<List<BehaviorModel>> input = new ArrayList<>();
		input.add(list);
		final List<MTree<BehaviorModel>> solutions = this.startAndGetSolutions(input);

		Iterator<MTree<BehaviorModel>.ResultItem> iter = solutions.get(0).getNearest(this.model1).iterator();

		// System.out.println(this.mockGED.calculate(iter.next().data, this.model1));
		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model1));
		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model2));
		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model3));
		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model4));

		MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
		MatcherAssert.assertThat(solutions.size(), Matchers.is(1));

		iter = solutions.get(0).getNearest(this.model3).iterator();

		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model3));
		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model4));
		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model2));
		MatcherAssert.assertThat(iter.next().data, Matchers.is(this.model1));

	}

	private List<MTree<BehaviorModel>> startAndGetSolutions(final List<List<BehaviorModel>> input) {
		final List<MTree<BehaviorModel>> solutions = new ArrayList<>();
		StageTester.test(this.mtreeGenerator).and().send(input).to(this.mtreeGenerator.getInputPort()).and()
				.receive(solutions).from(this.mtreeGenerator.getOutputPort()).start();
		return solutions;
	}
}
