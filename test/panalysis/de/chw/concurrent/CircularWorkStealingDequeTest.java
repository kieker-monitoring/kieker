package de.chw.concurrent;

import org.hamcrest.number.OrderingComparison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.chw.concurrent.CircularWorkStealingDeque.DequeIsEmptyException;
import de.chw.util.StopWatch;

public class CircularWorkStealingDequeTest {

	private StopWatch stopWatch;

	@Before
	public void before() {
		this.stopWatch = new StopWatch();
	}

	@Test
	public void measureManyEmptyPulls() {
		final CircularWorkStealingDeque<Object> deque = new CircularWorkStealingDeque<Object>();

		final int numIterations = UntypedCircularWorkStealingDequeTest.NUM_ITERATIONS;
		this.stopWatch.start();
		for (int i = 0; i < numIterations; i++) {
			try {
				deque.popBottom();
			} catch (final DequeIsEmptyException e) {
				// do not handle; we just want to compare the performance of throwing a preallocated exception vs. returning special values
			}
		}
		this.stopWatch.end();

		Assert.assertThat(this.stopWatch.getDuration(), OrderingComparison.lessThan(1100l));
	}
}
