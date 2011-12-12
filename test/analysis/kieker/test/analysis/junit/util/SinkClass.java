package kieker.test.analysis.junit.util;

import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.port.AInputPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;

public class SinkClass extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "doJob";
	private final ConcurrentLinkedQueue<Execution> lst = new ConcurrentLinkedQueue<Execution>();

	public SinkClass(final Configuration configuration) {
		super(configuration);
	}

	@Override
	public boolean execute() {
		return false;
	}

	@Override
	public void terminate(final boolean error) {}

	@Override
	protected Configuration getDefaultConfiguration() {
		return null;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return null;
	}

	@AInputPort(eventTypes = { Execution.class })
	public void doJob(final Object data) {
		this.lst.add((Execution) data);
	}

	public ConcurrentLinkedQueue<Execution> getList() {
		return this.lst;
	}
}
