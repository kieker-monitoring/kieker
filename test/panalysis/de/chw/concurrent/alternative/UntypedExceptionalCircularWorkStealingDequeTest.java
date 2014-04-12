package de.chw.concurrent.alternative;

import org.hamcrest.number.OrderingComparison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.chw.concurrent.alternative.UntypedExceptionalCircularWorkStealingDeque;
import de.chw.concurrent.alternative.UntypedExceptionalCircularWorkStealingDeque.DequeIsEmptyException;
import de.chw.util.StopWatch;

@Ignore
public class UntypedExceptionalCircularWorkStealingDequeTest {

	private StopWatch stopWatch;

	@Before
	public void before() {
		this.stopWatch = new StopWatch();
	}

	@Test
	public void measureManyEmptyPulls() {
		final UntypedExceptionalCircularWorkStealingDeque deque = new UntypedExceptionalCircularWorkStealingDeque();

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

		Assert.assertThat(this.stopWatch.getDuration(), OrderingComparison.lessThan(UntypedCircularWorkStealingDequeTest.EXPECTED_DURATION));
	}
}
