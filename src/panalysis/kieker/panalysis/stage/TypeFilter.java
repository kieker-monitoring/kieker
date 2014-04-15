package kieker.panalysis.stage;

import kieker.panalysis.base.AbstractDefaultFilter;
import kieker.panalysis.base.Context;
import kieker.panalysis.base.IInputPort;
import kieker.panalysis.base.IOutputPort;

/**
 * @author Jan Waller, Nils Christian Ehmke, Christian Wulf
 * 
 * @since 1.10
 */
public class TypeFilter<T> extends AbstractDefaultFilter<TypeFilter<T>> {

	public final IInputPort<TypeFilter<T>, Object> INPUT_OBJECT = this.createInputPort();

	public final IOutputPort<TypeFilter<T>, Object> OUTPUT_MATCHING = this.createOutputPort();
	public final IOutputPort<TypeFilter<T>, Object> OUTPUT_MISMATCHING = this.createOutputPort();

	private final Class<T> typeToFilter;

	/**
	 * @since 1.10
	 */
	private TypeFilter(final Class<T> typeToFilter) {
		this.typeToFilter = typeToFilter;
	}

	/**
	 * @since 1.10
	 */
	public static <T> TypeFilter<T> create(final Class<T> type) {
		return new TypeFilter<T>(type);
	}

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<TypeFilter<T>> context) {
		final Object token = this.tryTake(this.INPUT_OBJECT);
		if (token == null) {
			return false;
		}

		if (this.typeToFilter.isInstance(token)) {
			this.put(this.OUTPUT_MATCHING, token);
		} else {
			this.put(this.OUTPUT_MISMATCHING, token);
		}

		return true;
	}

}
