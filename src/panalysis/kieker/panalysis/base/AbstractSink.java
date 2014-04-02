package kieker.panalysis.base;

public abstract class AbstractSink<InputPort extends Enum<InputPort>> extends AbstractFilter<InputPort, AbstractSink.OUTPUT_PORT> {

	static enum OUTPUT_PORT {
		DUMMY // sink stages have not any output ports
	}

	public AbstractSink(final Class<InputPort> inputEnumType) {
		super(inputEnumType, OUTPUT_PORT.class);
	}

}
