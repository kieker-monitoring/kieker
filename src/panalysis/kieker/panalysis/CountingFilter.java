package kieker.panalysis;

import kieker.panalysis.base.AbstractFilter;

/**
 * @author Jan Waller, Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class CountingFilter extends AbstractFilter<CountingFilter.INPUT_PORT, CountingFilter.OUTPUT_PORT> {

	public static enum INPUT_PORT {
		INPUT_OBJECT, CURRENT_COUNT
	}

	public static enum OUTPUT_PORT {
		RELAYED_OBJECT, CURRENT_COUNT
	}

	public CountingFilter() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
	}

	public void execute() {
		final Object inputObject = super.take(INPUT_PORT.INPUT_OBJECT);
		final long count = (Long) super.take(INPUT_PORT.CURRENT_COUNT) + 1;

		super.put(OUTPUT_PORT.CURRENT_COUNT, count);
		super.put(OUTPUT_PORT.RELAYED_OBJECT, inputObject);
	}

}
