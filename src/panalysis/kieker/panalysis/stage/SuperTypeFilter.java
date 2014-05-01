package kieker.panalysis.stage;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Jan Waller, Nils Christian Ehmke, Christian Wulf
 * 
 * @since 1.10
 */
public class SuperTypeFilter<T> extends AbstractFilter<SuperTypeFilter<T>> {

	public final IInputPort<SuperTypeFilter<T>, Object> INPUT_OBJECT = this.createInputPort();

	public final IOutputPort<SuperTypeFilter<T>, Object> OUTPUT_MATCHING = this.createOutputPort();
	public final IOutputPort<SuperTypeFilter<T>, Object> OUTPUT_MISMATCHING = this.createOutputPort();

	private final Class<T> typeToFilter;

	/**
	 * @since 1.10
	 */
	private SuperTypeFilter(final Class<T> typeToFilter) {
		this.typeToFilter = typeToFilter;
	}

	/**
	 * @since 1.10
	 */
	public static <T> SuperTypeFilter<T> create(final Class<T> type) {
		return new SuperTypeFilter<T>(type);
	}

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<SuperTypeFilter<T>> context) {
		final Object token = context.tryTake(this.INPUT_OBJECT);
		if (token == null) {
			return false;
		}

		if (this.typeToFilter.isAssignableFrom(token.getClass())) {
			context.put(this.OUTPUT_MATCHING, token);
		} else {
			context.put(this.OUTPUT_MISMATCHING, token);
		}

		return true;
	}

}
