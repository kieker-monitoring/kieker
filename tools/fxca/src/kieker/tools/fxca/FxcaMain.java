/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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

/**
 * @author Henning Schnoor
 *
 */
package kieker.tools.fxca;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.beust.jcommander.JCommander;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.fxca.utils.IOUtils;

/**
 * Tool to process fxtran XML files into a calltable, operation-definitions and a notfound file.
 *
 * @author Henning Schnoor
 * @since 2.0.0
 */
public final class FxcaMain extends AbstractService<TeetimeConfiguration, Settings> {

	private static final Logger LOGGER = LoggerFactory.getLogger(FxcaMain.class);

	/**
	 * As suggested by PMD, make this a utility class that cannot be instantiated.
	 */
	private FxcaMain() {}

	public static void main(final String[] args) {
		final FxcaMain main = new FxcaMain();
		try {
			final int exitCode = main.run("fxtran code analysis", "fxca", args, new Settings());
			System.exit(exitCode);
		} catch (final IllegalArgumentException e) {
			LoggerFactory.getLogger(FxcaMain.class).error("Configuration error: {}", e.getLocalizedMessage());
			System.exit(1);
		}
	}

	@Override
	protected TeetimeConfiguration createTeetimeConfiguration() throws ConfigurationException {
		return new TeetimeConfiguration(this.settings);
	}

	@Override
	protected Path getConfigurationPath() {
		return null;
	}

	@Override
	protected boolean checkConfiguration(final Configuration configuration, final JCommander commander) {
		return true;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		IOUtils.createDirectory(this.settings.getOutputDirectoryPath());

		final Predicate<Path> useDirectory = IOUtils.IS_DIRECTORY;

		final List<Path> directories = new ArrayList<>();
		if (this.settings.isFlat()) {
			directories.addAll(this.settings.getInputDirectoryPaths());
		} else {
			for (final Path rootPath : this.settings.getInputDirectoryPaths()) {
				try {
					directories.addAll(IOUtils.pathsInDirectory(rootPath, useDirectory, useDirectory, true));
				} catch (final IOException e) {
					FxcaMain.LOGGER.error("Error scanning directory {}: {} ", rootPath, e.getLocalizedMessage()); // NOPMD
				}
			}
		}

		return true;
	}

	@Override
	protected void shutdownService() {
		// nothing special to be done here
	}
}
