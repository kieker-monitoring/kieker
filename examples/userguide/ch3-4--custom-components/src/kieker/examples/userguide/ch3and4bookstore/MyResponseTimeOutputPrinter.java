package kieker.examples.userguide.ch3and4bookstore;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

@Plugin(name = "Reponse time printer", description = "Prints the response time on the standard output")
public class MyResponseTimeOutputPrinter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_EVENTS = "newEvent";
	public static final String CONFIG_PROPERTY_NAME_VALID_OUTPUT = "validOutput";

	private final boolean validOutput;

	public MyResponseTimeOutputPrinter(final Configuration configuration) {
		super(configuration);

		this.validOutput = configuration.getBooleanProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT);
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

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, Boolean.toString(true));

		return configuration;
	}

	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, Boolean.toString(this.validOutput));

		return configuration;
	}

}
