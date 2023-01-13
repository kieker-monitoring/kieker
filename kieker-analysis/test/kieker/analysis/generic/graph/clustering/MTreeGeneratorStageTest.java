package kieker.analysis.generic.graph.clustering;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import kieker.analysis.generic.graph.mtree.IDistanceFunction;

import teetime.framework.test.StageTester;

public class MTreeGeneratorStageTest {

	@Before
	public void setUp() throws Exception {}

	@Test
	public void testMTreeGeneratorStage() {
		final List<Integer> list = Arrays.asList(new Integer[] { 1, 2, 3, 4, 5, 10, 12, 15 });
		final IDistanceFunction<Integer> distanceFunction = new IDistanceFunction<Integer>() {

			@Override
			public double calculate(final Integer data1, final Integer data2) {
				return Math.abs((double) data1 - data2);
			}

		};

		final MTreeGeneratorStage<Integer> stage = new MTreeGeneratorStage<>(distanceFunction);
		StageTester.test(stage).and().send(list, list).to(stage.getInputPort()).start();
	}

}
