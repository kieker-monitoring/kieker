package de.chw.concurrent.alternative;

import org.hamcrest.number.OrderingComparison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.chw.util.StopWatch;

public class UntypedCircularWorkStealingDequeTest {

	public static final int NUM_ITERATIONS = 100000000;
	public static final long EXPECTED_DURATION = 1100;

	private StopWatch stopWatch;

	@Before
	public void before() {
		this.stopWatch = new StopWatch();
	}

	@Test
	public void measureManyEmptyPulls() {
		final UntypedCircularWorkStealingDeque deque = new UntypedCircularWorkStealingDeque();

		final int numIterations = NUM_ITERATIONS;
		this.stopWatch.start();
		for (int i = 0; i < numIterations; i++) {
			deque.popBottom();
		}
		this.stopWatch.end();

		Assert.assertThat(this.stopWatch.getDuration(), OrderingComparison.lessThan(UntypedCircularWorkStealingDequeTest.EXPECTED_DURATION));
	}
}
