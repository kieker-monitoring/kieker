/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.log.replayer;

import java.io.File;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

/**
 * Collector main class.
 *
 * @author Reiner Jung
 * @since 1.16
 */
public final class ReplayerMain extends AbstractService<TeetimeConfiguration, Settings> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReplayerMain.class);

	private TeetimeConfiguration configuration;
	private final Settings parameter = new Settings();

	/**
	 * This is a simple main class which does not need to be instantiated.
	 */
	private ReplayerMain() {

	}

	/**
	 * Configure and execute the TCP Kieker data collector.
	 *
	 * @param args
	 *            arguments are ignored
	 */
	public static void main(final String[] args) {
		final ReplayerMain main = new ReplayerMain();
		System.exit(main.run("Replayer", "replayer", args));
	}

	/**
	 * Runner method.
	 *
	 * @param title
	 *            application title
	 * @param label
	 *            logging label
	 * @param args
	 *            command line arguments
	 * @return returns exit code
	 */
	public int run(final String title, final String label, final String[] args) {
		final int result = super.run(title, label, args, this.parameter);
		if ((this.configuration != null) && LOGGER.isInfoEnabled()) {
			ReplayerMain.LOGGER.info("Records send {}", this.configuration.getCounter().getNumElementsPassed());
		}
		return result;
	}

	@Override
	protected TeetimeConfiguration createTeetimeConfiguration() {
		return new TeetimeConfiguration(this.parameter);
	}

	@Override
	protected boolean checkParameters(final JCommander commander) {
		boolean worked = true;
		for (final File file : this.parameter.getDataLocation()) {
			worked &= ParameterEvaluationUtils.checkDirectory(file, "Output Kieker directory",
					commander);
		}
		return worked;
	}

	@Override
	protected Path getConfigurationPath() {
		return this.parameter.getKiekerMonitoringProperties();
	}

	@Override
	protected boolean checkConfiguration(final kieker.common.configuration.Configuration kiekerConfiguration,
			final JCommander commander) {
		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing special to shutdown
	}

}
