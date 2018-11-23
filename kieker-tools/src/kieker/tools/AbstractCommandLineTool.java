/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.util.CLIHelpFormatter;
import kieker.tools.util.ToolsUtil;

/**
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 *
 * @deprecated since 1.15 remove after porting all tools to JCommander
 */
@Deprecated
public abstract class AbstractCommandLineTool {

	public static final String CMD_OPT_NAME_HELP_LONG = "help";
	public static final String CMD_OPT_NAME_HELP_SHORT = "h";

	public static final String CMD_OPT_NAME_VERBOSE_LONG = "verbose";
	public static final String CMD_OPT_NAME_VERBOSE_SHORT = "v";

	public static final String CMD_OPT_NAME_DEBUG_LONG = "debug";
	public static final String CMD_OPT_NAME_DEBUG_SHORT = "d";

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractCommandLineTool.class);

	private final boolean useSystemExit;

	/**
	 * Create an abstract commandline tool.
	 *
	 * @param useSystemExit
	 *            true to indicated exit can be used
	 */
	public AbstractCommandLineTool(final boolean useSystemExit) {
		this.useSystemExit = useSystemExit;
	}

	/**
	 * Start analysis.
	 *
	 * @param args
	 *            parameters
	 */
	public final void start(final String[] args) {
		final Options options = new Options();

		this.addDefaultOptions(options);
		this.addAdditionalOptions(options);

		final CommandLine commandLine = this.parseCommandLineArguments(options, args);

		boolean success;
		if (null != commandLine) {
			this.initializeLogger(commandLine);

			if (commandLine.hasOption(CMD_OPT_NAME_HELP_SHORT)) {
				this.printUsage(options);
				System.exit(0);
			}

			// Using && instead of & should make sure that performTask is not executed when the readPropertiesFromCommandLine method returns false
			success = this.readPropertiesFromCommandLine(commandLine);

			if (!success) {
				LOGGER.info("Use the option `--{}` for usage information", CMD_OPT_NAME_HELP_LONG);
			} else {
				success = this.performTask();
			}

		} else {
			success = false;
		}

		LOGGER.info("See 'kieker.log' for details");
		if (!success && this.useSystemExit) {
			System.exit(1);
		}
	}

	private void initializeLogger(final CommandLine commandLine) {
		if (commandLine.hasOption('d')) {
			ToolsUtil.loadDebugLogger();
		} else if (commandLine.hasOption('v')) {
			ToolsUtil.loadVerboseLogger();
		} else {
			ToolsUtil.loadDefaultLogger();
		}
	}

	private void printUsage(final Options options) {
		final HelpFormatter formatter = this.getHelpFormatter();
		formatter.printHelp(this.getClass().getName(), options, true);
	}

	private void addDefaultOptions(final Options options) {
		options.addOption(new Option("v", "verbose", false, "verbosely prints additional information"));
		options.addOption(new Option("d", "debug", false, "prints additional debug information"));
		options.addOption(new Option(CMD_OPT_NAME_HELP_SHORT, CMD_OPT_NAME_HELP_LONG, false,
				"prints the usage information for the tool, including available options"));
	}

	private CommandLine parseCommandLineArguments(final Options options, final String[] arguments) {
		final BasicParser parser = new BasicParser();

		try {
			return parser.parse(options, arguments);
		} catch (final ParseException ex) {
			// Note that we append ex.getMessage() to the log message on purpose to improve the
			// logging output on the console.
			LOGGER.error("An error occurred while parsing the command line arguments: {}", ex.getMessage(), ex);
			LOGGER.info("Use the option `--{}` for usage information", CMD_OPT_NAME_HELP_LONG);

			return null;
		}
	}

	/**
	 * Add additional options.
	 *
	 * @param options
	 *            options
	 */
	protected abstract void addAdditionalOptions(Options options);

	/**
	 * Read properties from command line.
	 *
	 * @param commandLine
	 *            command line handler
	 * @return true on success
	 */
	protected abstract boolean readPropertiesFromCommandLine(CommandLine commandLine);

	/**
	 * Perform task.
	 *
	 * @return true on success
	 */
	protected abstract boolean performTask();

	/**
	 * Format usage output.
	 * 
	 * @return the formatter
	 */
	protected HelpFormatter getHelpFormatter() {
		return new CLIHelpFormatter();
	}

}
