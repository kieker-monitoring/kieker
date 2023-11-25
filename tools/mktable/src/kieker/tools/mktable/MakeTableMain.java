/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mktable;

import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;

/**
 * Read CSV mappings for operation movements and create an LaTeX-table output sorted by a column.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class MakeTableMain extends AbstractService<TeetimeConfiguration, Settings> {

	/** logger for all tools. */
	protected final Logger logger = LoggerFactory.getLogger(this.getClass().getCanonicalName()); // NOPMD

	public static void main(final String[] args) {
		final MakeTableMain main = new MakeTableMain();
		try {
			final int exitCode = main.run("make table", "mktable", args, new Settings());
			System.exit(exitCode);
		} catch (final IllegalArgumentException e) {
			LoggerFactory.getLogger(MakeTableMain.class).error("Configuration error: {}", e.getLocalizedMessage());
			System.exit(1);
		}
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return true;
	}

	@Override
	protected TeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
		return new TeetimeConfiguration(this.settings);
	}

	@Override
	protected Path getConfigurationPath() {
		// we do not use a configuration file
		return null;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing to do here
	}

}
