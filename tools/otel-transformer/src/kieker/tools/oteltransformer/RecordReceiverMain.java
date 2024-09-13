/***************************************************************************
 * Copyright (C) 2024 Kieker Project (https://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.oteltransformer;

import java.nio.file.Path;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.tools.common.AbstractService;

/**
 * Receives Kieker records, to later on pass them to OpenTelemetry (like the MooBench record receiver).
 * 
 * @author David Georg Reichelt, Reiner Jung
 *
 */
public final class RecordReceiverMain extends AbstractService<OpenTelemetryExportConfiguration, Settings> {

	private final Settings parameter = new Settings();

	private RecordReceiverMain() {}

	public static void main(final String[] args) {
		final RecordReceiverMain main = new RecordReceiverMain();
		System.exit(main.run("OpenTelemetry Transformer", "transformer", args));
	}

	public int run(final String title, final String label, final String[] args) {
		final int result = super.run(title, label, args, this.parameter);
		return result;
	}

	@Override
	protected OpenTelemetryExportConfiguration createTeetimeConfiguration() throws ConfigurationException {
		final Configuration configuration;
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
