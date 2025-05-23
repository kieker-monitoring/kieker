package kieker.tools.oteltransformer.receiver;

import java.nio.file.Path;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.common.AbstractService;
import kieker.tools.oteltransformer.Settings;

public class OtlpReceiverMain extends AbstractService<OtlpReceiverStageConfiguration, Settings> {
	private final Settings parameter = new Settings();

	private OtlpReceiverMain() {}

	public static void main(final String[] args) {
		final OtlpReceiverMain main = new OtlpReceiverMain();
		System.exit(main.run("OpenTelemetry Transformer", "transformer", args));
	}

	public int run(final String title, final String label, final String[] args) {
		final int result = super.run(title, label, args, this.parameter);
		return result;
	}

	@Override
	protected OtlpReceiverStageConfiguration createTeetimeConfiguration() throws ConfigurationException {
		final Configuration configuration;
		if (parameter.getKiekerMonitoringProperties() != null) {
			configuration = ConfigurationFactory.createConfigurationFromFile(parameter.getKiekerMonitoringProperties());
		} else {
			configuration = ConfigurationFactory.createDefaultConfiguration();
		}
		return new OtlpReceiverStageConfiguration(parameter.getListenPort(), configuration);
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
		// TODO Auto-generated method stub
		
	}
}
