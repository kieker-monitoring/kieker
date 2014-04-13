package kieker.panalysis.stage;

import kieker.panalysis.base.AbstractDefaultFilter;
import kieker.panalysis.base.IInputPort;
import kieker.panalysis.base.IOutputPort;

public class TypeFilter<T> extends AbstractDefaultFilter<TypeFilter<T>> {

	public final IInputPort<TypeFilter<T>, T> INPUT_OBJECT = this.createInputPort();

	public final IOutputPort<TypeFilter<T>, T> RELAYED_OBJECT = this.createOutputPort();

	private final Class<T> type;

	private TypeFilter(final Class<T> type) {
		this.type = type;
	}

	public static <T> TypeFilter<T> create(final Class<T> type) {
		return new TypeFilter<T>(type);
	}

	public boolean execute() {
		final T token = this.tryTake(this.INPUT_OBJECT);
		if (token == null) {
			return false;
		}

		if (this.type.isAssignableFrom(token.getClass())) {
			this.put(this.RELAYED_OBJECT, token);
		} // else: skip token
		return true;
	}
}
