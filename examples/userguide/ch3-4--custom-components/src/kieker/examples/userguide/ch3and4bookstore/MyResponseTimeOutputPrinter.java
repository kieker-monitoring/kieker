package kieker.examples.userguide.ch3and4bookstore;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * This filter prints the response times of the incoming records on the standard output.
 * 
 * @author Nils Christian Ehmke
 */
@Plugin(name = "Reponse time printer", description = "Prints the response time on the standard output",
		configuration = {
			@Property(name = MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, defaultValue = "true")
		})
public class MyResponseTimeOutputPrinter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "newEvent";
	public static final String CONFIG_PROPERTY_NAME_VALID_OUTPUT = "validOutput";

	private final boolean validOutput;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 * 
	 * @since 1.7
	 */
	public MyResponseTimeOutputPrinter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.validOutput = configuration.getBooleanProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * 
	 * @deprecated
	 */
	@Deprecated
	public MyResponseTimeOutputPrinter(final Configuration configuration) {
		this(configuration, null);
	}

	@InputPort(name = MyResponseTimeOutputPrinter.INPUT_PORT_NAME_EVENTS, eventTypes = { MyResponseTimeRecord.class })
	public void newEvent(final Object event) {
		if (event instanceof MyResponseTimeRecord) {
			/* Write the content to the standard output stream. */
			final MyResponseTimeRecord myRecord = (MyResponseTimeRecord) event;

			String msg = this.validOutput ? "[Valid] " : "[Invalid] ";
			msg += myRecord.getLoggingTimestamp()
					+ ": " + myRecord.getClassName() + ", " + myRecord.getMethodName()
					+ ", " + myRecord.getResponseTimeNanos();

			System.out.println(msg);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see kieker.analysis.plugin.IPlugin#getCurrentConfiguration()
	 */
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, Boolean.toString(this.validOutput));

		return configuration;
	}

}
