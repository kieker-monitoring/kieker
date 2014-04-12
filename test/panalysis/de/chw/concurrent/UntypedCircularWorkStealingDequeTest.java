package de.chw.concurrent;

import org.hamcrest.number.OrderingComparison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.chw.util.StopWatch;

public class UntypedCircularWorkStealingDequeTest {

	static final int NUM_ITERATIONS = 100000;

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

		Assert.assertThat(this.stopWatch.getDuration(), OrderingComparison.lessThan(1100l));
	}
}
