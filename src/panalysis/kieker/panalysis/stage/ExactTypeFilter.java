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
public class ExactTypeFilter<T> extends AbstractFilter<ExactTypeFilter<T>> {

	public final IInputPort<ExactTypeFilter<T>, Object> INPUT_OBJECT = this.createInputPort();

	public final IOutputPort<ExactTypeFilter<T>, Object> OUTPUT_MATCHING = this.createOutputPort();
	public final IOutputPort<ExactTypeFilter<T>, Object> OUTPUT_MISMATCHING = this.createOutputPort();

	private final Class<T> typeToFilter;

	/**
	 * @since 1.10
	 */
	private ExactTypeFilter(final Class<T> typeToFilter) {
		this.typeToFilter = typeToFilter;
	}

	/**
	 * @since 1.10
	 */
	public static <T> ExactTypeFilter<T> create(final Class<T> type) {
		return new ExactTypeFilter<T>(type);
	}

	/**
	 * @since 1.10
	 */
	@Override
	protected boolean execute(final Context<ExactTypeFilter<T>> context) {
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
