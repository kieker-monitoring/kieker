/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring;

import java.io.IOException;
import java.nio.file.Path;

import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class RestructuringMain extends AbstractService<TeetimeConfiguration, Settings> {

	public static void main(final String[] args) {
		final RestructuringMain main = new RestructuringMain();
		try {
			final int exitCode = main.run("architecture model operations", "restructure", args, new Settings());
			System.exit(exitCode);
		} catch (final IllegalArgumentException e) {
			LoggerFactory.getLogger(RestructuringMain.class).error("Configuration error: {}", e.getLocalizedMessage());
			System.exit(1);
		}
	}

	@Override
	protected TeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
		try {
			return new TeetimeConfiguration(this.settings);
		} catch (final IOException e) {
			throw new ConfigurationException(e);
		}
	}

	@Override
	protected Path getConfigurationPath() {
		// TODO Auto-generated method stub
		return null;
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
		// No special operation necessary.
	}
}
