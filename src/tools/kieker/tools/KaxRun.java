/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import kieker.analysis.AnalysisController;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * A simple execution of Analysis Configurations.
 * 
 * @author Jan Waller
 */
public final class KaxRun {
	private static final Log LOG = LogFactory.getLog(KaxRun.class);

	private KaxRun() {}

	/**
	 * Starts an AnalysisController with a .kax file.
	 * 
	 * @param args
	 *            the name of the .kax file
	 */
	public static final void main(final String[] args) {
		// create cmdline options
		final Options options = new Options();
		final Option inputOption = new Option("i", "input", true, "the analysis project file (.kax) loaded");
		inputOption.setRequired(true);
		inputOption.setArgName("filename");
		options.addOption(inputOption);

		// parse cmdline options
		final String kaxFilename;
		try {
			final CommandLineParser parser = new BasicParser();
			final CommandLine line = parser.parse(options, args);
			kaxFilename = line.getOptionValue('i');
		} catch (final ParseException ex) {
			System.out.println(ex.getMessage()); // NOPMD (System.out)
			final HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(KaxRun.class.getName(), options, true);
			return;
		}

		// start tool
		try {
			final AnalysisController ctrl = new AnalysisController(new File(kaxFilename));
			ctrl.run();
		} catch (final Exception ex) { // NOPMD NOCS (log all errors)
			LOG.error("Error", ex);
		}
	}
}
