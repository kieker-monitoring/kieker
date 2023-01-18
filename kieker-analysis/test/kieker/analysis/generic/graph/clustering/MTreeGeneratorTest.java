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
import java.util.Iterator;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.common.graph.MutableNetwork;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.mtree.MTree;
import kieker.analysis.generic.graph.mtree.query.ResultItem;

import teetime.framework.test.StageTester;

/**
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class MTreeGeneratorTest { // NOCS constructor

	private MTreeGeneratorStage<MutableNetwork<INode, IEdge>> mtreeGenerator;
	private GraphEditDistance<INode, IEdge> mockGED;

	private MutableNetwork<INode, IEdge> model1;
	private MutableNetwork<INode, IEdge> model2;
	private MutableNetwork<INode, IEdge> model3;
	private MutableNetwork<INode, IEdge> model4;

	@Before
	public void setup() {
		this.mockGED = Mockito.mock(GraphEditDistance.class);

		this.mtreeGenerator = new MTreeGeneratorStage<>(this.mockGED);

		this.model1 = Mockito.mock(MutableNetwork.class);
		this.model2 = Mockito.mock(MutableNetwork.class);
		this.model3 = Mockito.mock(MutableNetwork.class);
		this.model4 = Mockito.mock(MutableNetwork.class);

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
		final List<MutableNetwork<INode, IEdge>> list = new ArrayList<>();
		final List<List<MutableNetwork<INode, IEdge>>> input = new ArrayList<>();
		input.add(list);

		final List<MTree<MutableNetwork<INode, IEdge>>> solutions = this.startAndGetSolutions(input);

		final Iterator<ResultItem<MutableNetwork<INode, IEdge>>> iter = solutions.get(0).getNearest(this.model1).iterator();

		MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
		MatcherAssert.assertThat(solutions.size(), Matchers.is(1));
	}

	@Test
	public void oneElementTest() {
		final List<MutableNetwork<INode, IEdge>> list = new ArrayList<>();
		list.add(this.model1);
		final List<List<MutableNetwork<INode, IEdge>>> input = new ArrayList<>();
		input.add(list);
		final List<MTree<MutableNetwork<INode, IEdge>>> solutions = this.startAndGetSolutions(input);

		final Iterator<ResultItem<MutableNetwork<INode, IEdge>>> iter = solutions.get(0).getNearest(this.model1).iterator();

		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model1));
		MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
		MatcherAssert.assertThat(solutions.size(), Matchers.is(1));

	}

	@Test
	public void fourElementTest() {
		final List<MutableNetwork<INode, IEdge>> list = new ArrayList<>();
		list.add(this.model1);
		list.add(this.model2);
		list.add(this.model3);
		list.add(this.model4);

		final List<List<MutableNetwork<INode, IEdge>>> input = new ArrayList<>();
		input.add(list);
		final List<MTree<MutableNetwork<INode, IEdge>>> solutions = this.startAndGetSolutions(input);

		Iterator<ResultItem<MutableNetwork<INode, IEdge>>> iter = solutions.get(0).getNearest(this.model1).iterator();

		// System.out.println(this.mockGED.calculate(iter.next().data, this.model1));
		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model1));
		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model2));
		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model3));
		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model4));

		MatcherAssert.assertThat(iter.hasNext(), Matchers.is(false));
		MatcherAssert.assertThat(solutions.size(), Matchers.is(1));

		iter = solutions.get(0).getNearest(this.model3).iterator();

		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model3));
		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model4));
		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model2));
		MatcherAssert.assertThat(iter.next().getData(), Matchers.is(this.model1));

	}

	private List<MTree<MutableNetwork<INode, IEdge>>> startAndGetSolutions(final List<List<MutableNetwork<INode, IEdge>>> input) {
		final List<MTree<MutableNetwork<INode, IEdge>>> solutions = new ArrayList<>();
		StageTester.test(this.mtreeGenerator).and().send(input).to(this.mtreeGenerator.getInputPort()).and()
				.receive(solutions).from(this.mtreeGenerator.getOutputPort()).start();
		return solutions;
	}
}
