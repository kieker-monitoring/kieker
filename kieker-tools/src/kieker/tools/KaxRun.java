/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools;

import java.io.File;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * A simple execution of analysis configurations.
 *
 * @author Jan Waller, Nils Christian Ehmke
 *
 * @since 1.5
 */
public final class KaxRun extends AbstractCommandLineTool {

	private static final Log LOG = LogFactory.getLog(KaxRun.class);

	private String kaxFilename;

	private KaxRun() {
		super(true);
	}

	/**
	 * Starts the execution of a .kax file.
	 *
	 * @param args
	 *            The command line arguments (including the name of the .kax file in question).
	 */
	public static void main(final String[] args) {
		new KaxRun().start(args);
	}

	@Override
	protected void addAdditionalOptions(final Options options) {
		final Option inputOption = new Option("i", "input", true, "the analysis project file (.kax) loaded");
		inputOption.setArgName("filename");

		options.addOption(inputOption);
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		this.kaxFilename = commandLine.getOptionValue('i');

		return this.assertInputFileExists();
	}

	private boolean assertInputFileExists() {
		if (this.kaxFilename == null) {
			LOG.error("No input file configured");
			return false;
		}

		return true;
	}

	@Override
	protected boolean performTask() {
		boolean success = false;

		try {
			new AnalysisController(new File(this.kaxFilename)).run();
			success = true;
		} catch (final IOException ex) {
			LOG.error("The given file could not be loaded", ex);
		} catch (final AnalysisConfigurationException ex) {
			LOG.error("The given configuration file is invalid", ex);
		} catch (final Exception ex) { // NOPMD NOCS (log all errors)
			LOG.error("Error", ex);
		}

		return success;
	}

}
