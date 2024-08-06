/**
 * 
 */
package kieker.tools.oteltransformer;

import com.beust.jcommander.JCommander;

import java.nio.file.Path;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.common.AbstractService;

/**
 * @author David Georg Reichelt, Reiner Jung
 *
 */
public class RecordReceiverMain extends AbstractService<OpenTelemetryExportConfiguration, Settings> {

	private final Settings parameter = new Settings();

	private RecordReceiverMain() {}

	public static void main(final String[] args) {
		final RecordReceiverMain main = new RecordReceiverMain();
		System.exit(main.run("Replayer", "replayer", args));
	}

	public int run(final String title, final String label, final String[] args) {
		final int result = super.run(title, label, args, this.parameter);
		return result;
	}

	@Override
	protected OpenTelemetryExportConfiguration createTeetimeConfiguration() throws ConfigurationException {
		final kieker.common.configuration.Configuration configuration;
		if (parameter.getKiekerMonitoringProperties() != null) {
			configuration = ConfigurationFactory.createConfigurationFromFile(parameter.getKiekerMonitoringProperties());
		} else {
			configuration = ConfigurationFactory.createDefaultConfiguration();
		}
		return new OpenTelemetryExportConfiguration(parameter.getListenPort(), 8192, configuration);
	}

	@Override
	protected Path getConfigurationPath() {
		return this.parameter.getKiekerMonitoringProperties();
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		return true;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing special to shutdown
	}
}
