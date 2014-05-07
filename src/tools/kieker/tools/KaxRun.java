/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.util.CLIHelpFormatter;
import kieker.tools.util.ToolsUtil;

/**
 * A simple execution of Analysis Configurations.
 * 
 * @author Jan Waller
 * 
 * @since 1.5
 */
public final class KaxRun {

	private static final Log LOG = LogFactory.getLog(KaxRun.class);

	/**
	 * Private constructor to avoid instantiation.
	 */
	private KaxRun() {}

	/**
	 * Starts an AnalysisController with a .kax file.
	 * 
	 * @param args
	 *            The command line arguments (including the name of the .kax file in question).
	 */
	public static final void main(final String[] args) {
		final Options options = KaxRun.createCommandLineOptions();
		final String kaxFilename = KaxRun.parseParametersAndGetFileName(options, args);

		if (null != kaxFilename) {
			KaxRun.startAnalysis(kaxFilename);
		}

		LOG.info("See 'kieker.log' for details");
	}

	private static void startAnalysis(final String kaxFilename) {
		try {
			final AnalysisController ctrl = new AnalysisController(new File(kaxFilename));
			ctrl.run();
		} catch (final IOException ex) {
			LOG.error("The given file could not be loaded", ex);
		} catch (final AnalysisConfigurationException ex) {
			LOG.error("The given configuration file is invalid", ex);
		} catch (final Exception ex) { // NOPMD NOCS (log all errors)
			LOG.error("Error", ex);
		}
	}

	private static String parseParametersAndGetFileName(final Options options, final String[] args) {
		try {
			final CommandLineParser parser = new BasicParser();
			final CommandLine line = parser.parse(options, args);

			if (line.hasOption('d')) {
				ToolsUtil.loadDebugLogger();
			} else if (line.hasOption('v')) {
				ToolsUtil.loadVerboseLogger();
			}

			return line.getOptionValue('i');
		} catch (final ParseException ex) {
			LOG.error("An error occurred while parsing the parameters", ex);

			final HelpFormatter formatter = new CLIHelpFormatter();
			formatter.printHelp(KaxRun.class.getName(), options, true);

			return null;
		}
	}

	private static Options createCommandLineOptions() {
		final Options options = new Options();

		final Option inputOption = new Option("i", "input", true, "the analysis project file (.kax) loaded");
		final Option verboseOption = new Option("v", "verbose", false, "verbosely prints additional information");
		final Option debugOption = new Option("d", "debug", false, "prints additional debug information");

		inputOption.setRequired(true);
		inputOption.setArgName("filename");

		options.addOption(inputOption);
		options.addOption(verboseOption);
		options.addOption(debugOption);

		return options;
	}
}
