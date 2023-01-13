package kieker.analysis.generic.graph.clustering;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import teetime.framework.test.StageTester;

public class DataCollectorStageTest {

	@Before
	public void setUp() throws Exception {}

	@Test
	public void testEventsNoLimits() {
		final DataCollectorStage<Integer> dataCollectorStage = new DataCollectorStage<>();
		StageTester.test(dataCollectorStage).and().send(1, 2, 3, 4, 5).to(dataCollectorStage.getDataInputPort()).start();
		final List<Integer> list = new ArrayList<>();
		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);
		Assert.assertThat(dataCollectorStage.getOpticsOutputPort(), StageTester.produces(list));
	}

	@Test
	public void testEventsWithSizeLimit() {
		final DataCollectorStage<Integer> dataCollectorStage = new DataCollectorStage<>(2);
		StageTester.test(dataCollectorStage).and().send(1, 2, 3, 4, 5).to(dataCollectorStage.getDataInputPort()).start();
		final List<Integer> list1 = new ArrayList<>();
		final List<Integer> list2 = new ArrayList<>();
		final List<Integer> list3 = new ArrayList<>();
		list1.add(1);
		list1.add(2);
		list2.add(3);
		list2.add(4);
		list3.add(5);
		Assert.assertThat(dataCollectorStage.getOpticsOutputPort(), StageTester.produces(list1, list2, list3));
	}

	@Test
	public void testEventsTimeLimit() {
		final DataCollectorStage<Integer> dataCollectorStage = new DataCollectorStage<>();
		StageTester.test(dataCollectorStage).and().send(0L).to(dataCollectorStage.getTimeTriggerInputPort())
				.and().send(1, 2, 3, 4, 5).to(dataCollectorStage.getDataInputPort()).start();
		final List<Integer> list1 = new ArrayList<>();
		final List<Integer> list2 = new ArrayList<>();
		list1.add(1);
		list2.add(2);
		list2.add(3);
		list2.add(4);
		list2.add(5);
		Assert.assertThat(dataCollectorStage.getOpticsOutputPort(), StageTester.produces(list1, list2));
	}

}
