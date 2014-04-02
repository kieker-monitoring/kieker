package kieker.panalysis.base;

public abstract class AbstractSource<OutputPort extends Enum<OutputPort>> extends AbstractFilter<AbstractSource.INPUT_PORT, OutputPort> implements
		Source<OutputPort> {

	static protected enum INPUT_PORT {
		DUMMY // source stages have not any input ports
	}

	public AbstractSource(final Class<OutputPort> enumType) {
		super(INPUT_PORT.class, enumType);
	}

}
