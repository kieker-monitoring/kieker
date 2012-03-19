package kieker.test.analysis.junit.plugin;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.common.configuration.Configuration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * This is just a simple helper class which collects {@link Execution}s.
 * 
 * @author Nils Christian Ehmke
 * @version 1.0
 */
public class ExecutionSinkClass extends AbstractAnalysisPlugin {

	/**
	 * The name of the default input port.
	 */
	public static final String INPUT_PORT_NAME = "doJob";

	/**
	 * This list will contain the records this plugin received.
	 */
	private final CopyOnWriteArrayList<Execution> lst = new CopyOnWriteArrayList<Execution>();

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this plugin. It will not be used.
	 */
	public ExecutionSinkClass(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	@InputPort(
			name = ExecutionSinkClass.INPUT_PORT_NAME,
			eventTypes = { Execution.class })
	public void doJob(final Object data) {
		this.lst.add((Execution) data);
	}

	/**
	 * Delivers the list containing the received records.
	 * 
	 * @return The list with the records.
	 */
	public List<Execution> getExecutions() {
		return this.lst;
	}
}
