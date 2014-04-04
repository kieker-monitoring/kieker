package kieker.panalysis.composite;

import kieker.panalysis.CountingFilter;
import kieker.panalysis.base.IPipe;

public class CycledCountingFilter extends CountingFilter {

	public final static class INPUT_PORT { // mechanism to override the visibility of particular enum values of the super class
		public final static CountingFilter.INPUT_PORT INPUT_OBJECT = CountingFilter.INPUT_PORT.INPUT_OBJECT;
	}

	public final static class OUTPUT_PORT { // mechanism to override the visibility of particular enum values of the super class
		public final static CountingFilter.OUTPUT_PORT RELAYED_OBJECT = CountingFilter.OUTPUT_PORT.RELAYED_OBJECT;
	}

	public CycledCountingFilter(final IPipe countingPipe) {
		countingPipe.connect(this, CountingFilter.OUTPUT_PORT.NEW_COUNT, this,
				CountingFilter.INPUT_PORT.CURRENT_COUNT);
	}

}
