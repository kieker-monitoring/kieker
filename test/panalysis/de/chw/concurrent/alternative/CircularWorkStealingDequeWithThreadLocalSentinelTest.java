package de.chw.concurrent.alternative;

import org.hamcrest.number.OrderingComparison;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import de.chw.util.StopWatch;

public class CircularWorkStealingDequeWithThreadLocalSentinelTest {
	private StopWatch stopWatch;

	@Before
	public void before() {
		this.stopWatch = new StopWatch();
	}

	@Test
	public void measureManyEmptyPulls() {
		final CircularWorkStealingDequeWithThreadLocalSentinel<Object> deque = new CircularWorkStealingDequeWithThreadLocalSentinel<Object>();

		final int numIterations = UntypedCircularWorkStealingDequeTest.NUM_ITERATIONS;
		this.stopWatch.start();
		for (int i = 0; i < numIterations; i++) {
			deque.popBottom();
			// if (returnValue.getState() != State.EMPTY) {
			// returnValue.getValue();
			// }
		}
		this.stopWatch.end();

		Assert.assertThat(this.stopWatch.getDuration(), OrderingComparison.lessThan(UntypedCircularWorkStealingDequeTest.EXPECTED_DURATION));
	}
}
