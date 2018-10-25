/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.trace.analysis;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.beust.jcommander.JCommander;

import kieker.common.util.filesystem.FSUtil;
import kieker.tools.common.AbstractToolMain;
import kieker.tools.common.CommandLineParameterEvaluation;
import kieker.tools.common.ConfigurationException;

/**
 * This is the main class to start the Kieker TraceAnalysisTool - the model synthesis and analysis tool to process the
 * monitoring data that comes from the instrumented system, or from a file that contains Kieker monitoring data. The
 * Kieker TraceAnalysisTool can produce output such as sequence diagrams, dependency graphs on demand. Alternatively it
 * can be used continuously for online performance analysis, anomaly detection or live visualization of system behavior.
 *
 * @author Andre van Hoorn, Matthias Rohr, Nils Christian Ehmke
 * @author Reiner Jung -- ported to new APIs
 *
 * @since 0.95a, 1.15
 */
public final class TraceAnalysisToolMain extends AbstractToolMain<TraceAnalysisConfiguration> {

	/**
	 * Private constructor.
	 */
	private TraceAnalysisToolMain() {
		super();
	}

	/**
	 * Configure and execute the splitter.
	 *
	 * @param args
	 *            arguments are ignored
	 */
	public static void main(final String[] args) {
		new TraceAnalysisToolMain().run("TraceAnalysisTool", "traceAnalysisTool", args, new TraceAnalysisConfiguration());
	}

	@Override
	protected void execute(final JCommander commander, final String label) throws ConfigurationException {
		final DateFormat dateFormat = new SimpleDateFormat(Constants.DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		if (this.settings.getIgnoreExecutionsBeforeDate() != null) {
			LOGGER.info("Ignoring records before {} ({})", dateFormat.format(this.settings.getIgnoreExecutionsBeforeDate()),
					this.settings.getIgnoreExecutionsBeforeDate());
		}
		if (this.settings.getIgnoreExecutionsAfterDate() != null) {
			LOGGER.info("Ignoring records after {} ({})", dateFormat.format(this.settings.getIgnoreExecutionsAfterDate()),
					this.settings.getIgnoreExecutionsAfterDate());
		}

		this.settings.dumpConfiguration(LOGGER);

		if (new PerformAnalysis(LOGGER, this.settings).dispatchTasks()) {
			LOGGER.info("Analysis complete. See 'kieker.log' for details.");
		} else {
			LOGGER.error("Analysis incomplete. See 'kieker.log' for details.");
			System.exit(1);
		}
	}

	/** support for external configuration file. */
	@Override
	protected File getConfigurationFile() {
		return null;
	}

	@Override
	protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration, final JCommander commander) {
		return true;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		try {
			return this.checkInputDirs(commander)
					&& CommandLineParameterEvaluation.checkDirectory(this.settings.getOutputDir(), "output", commander)
					&& this.selectOrFilterTraces();
		} catch (final IOException e) {
			throw new ConfigurationException(e);
		}
	}

	@Override
	protected void shutdownService() {
		// nothing to do here
	}

	private boolean checkNotEmpty(List<?> list) {
		if (list == null) {
			return false;
		} else if (list.isEmpty()) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Check that selecting and filtering traces is not done at the same time.
	 *
	 * @return true if not both trace features have been requested
	 */
	private boolean selectOrFilterTraces() {
		if (checkNotEmpty(this.settings.getSelectTraces()) && checkNotEmpty(this.settings.getFilterTraces())) {
			LOGGER.error("Trace Id selection and filtering are mutually exclusive");
			return false;
		} else if (!this.settings.getSelectTraces().isEmpty()) {
			final int numSelectedTraces = this.settings.getSelectTraces().size();
			try {
				for (final Long idStr : this.settings.getSelectTraces()) {
					this.settings.getSelectedTraces().add(idStr);
				}
				LOGGER.info("{} trace{} selected", numSelectedTraces, (numSelectedTraces > 1 ? "s" : "")); // NOCS
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				LOGGER.error("Failed to parse list of trace IDs: {}", this.settings.getSelectTraces().toArray().toString(), e);
				return false;
			}
		} else if (!this.settings.getFilterTraces().isEmpty()) {
			this.settings.setInvertTraceIdFilter(true);
			final String[] traceIdList = this.settings.getFilterTraces().toArray(new String[this.settings.getFilterTraces().size()]);

			final int numSelectedTraces = traceIdList.length;
			try {
				for (final Long idStr : this.settings.getSelectTraces()) {
					this.settings.getSelectedTraces().add(idStr);
				}
				LOGGER.info("{} trace{} filtered", numSelectedTraces, (numSelectedTraces > 1 ? "s" : "")); // NOCS
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				LOGGER.error("Failed to parse list of trace IDs: {}", this.settings.getSelectTraces().toArray().toString(), e);
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns if the specified input directories {@link #inputDirs} exist and that each one is a monitoring log. If
	 * this is not the case for one of the directories, an error message is printed to stderr.
	 *
	 * @return true if {@link #inputDirs} exist and are Kieker directories; false otherwise
	 */
	private boolean checkInputDirs(final JCommander commander) {
		for (final File inputDir : this.settings.getInputDirs()) {
			try {
				if (!inputDir.exists()) {
					LOGGER.error("The specified input directory '{}' does not exist", inputDir.getCanonicalPath());
					return false;
				}
				if (!inputDir.isDirectory() && !inputDir.getAbsolutePath().endsWith(FSUtil.ZIP_FILE_EXTENSION)) {
					LOGGER.error("The specified input directory '{}' is neither a directory nor a zip file", inputDir.getCanonicalPath());
					return false;
				}
				// check whether inputDirFile contains a (kieker|tpmon).map file; the latter for legacy reasons
				if (inputDir.isDirectory()) { // only check for dirs
					final File[] mapFiles = { new File(inputDir.getAbsolutePath() + File.separatorChar + FSUtil.MAP_FILENAME),
						new File(inputDir.getAbsolutePath() + File.separatorChar + FSUtil.LEGACY_MAP_FILENAME), };
					boolean mapFileExists = false;
					for (final File potentialMapFile : mapFiles) {
						if (potentialMapFile.isFile()) {
							mapFileExists = true;
							break;
						}
					}
					if (!mapFileExists) {
						LOGGER.error("The specified input directory '{}' is not a kieker log directory", inputDir.getCanonicalPath());
						return false;
					}
				}
			} catch (final IOException e) { // thrown by File.getCanonicalPath()
				LOGGER.error("Error resolving name of input directory: '{}'", inputDir);
			}
		}

		return true;
	}

}
