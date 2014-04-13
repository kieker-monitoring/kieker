package kieker.panalysis;

import kieker.panalysis.base.AbstractFilter;

/**
 * @author Jan Waller, Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class TypeFilter extends AbstractFilter<TypeFilter.INPUT_PORT, TypeFilter.OUTPUT_PORT> {

	public static enum INPUT_PORT { // NOCS
		OBJECT
	}

	public static enum OUTPUT_PORT { // NOCS
		OUTPUT_MATCHING, OUTPUT_MISMATCHING
	}

	private final Class<?> typeToFilter;

	public TypeFilter(final Class<?> typeToFilter) {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
		this.typeToFilter = typeToFilter;
	}

	public boolean execute() {
		final Object object = this.tryTake(INPUT_PORT.OBJECT);
		if (object == null) {
			return false;
		}

		if (this.typeToFilter.isInstance(object)) {
			super.put(OUTPUT_PORT.OUTPUT_MATCHING, object);
		} else {
			super.put(OUTPUT_PORT.OUTPUT_MISMATCHING, object);
		}

		return true;
	}

}
