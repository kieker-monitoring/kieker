package kieker.panalysis.base;

public abstract class Filter<OutputPort extends Enum<OutputPort>> extends Source<OutputPort> implements PortListener {

	public Filter(final long id, final Class<OutputPort> outputEnumType) {
		super(id, outputEnumType);
	}
}
