/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.beust.jcommander.JCommander;

import kieker.common.exception.ConfigurationException;
import kieker.common.util.filesystem.FSUtil;
import kieker.tools.common.AbstractLegacyTool;
import kieker.tools.common.ParameterEvaluationUtils;
import kieker.tools.settings.converters.DateConverter;

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
 * @deprecated since 1.15 ported to TeeTime
 */
@Deprecated
public final class TraceAnalysisToolMain extends AbstractLegacyTool<TraceAnalysisParameters> {

	/**
	 * Private constructor.
	 */
	private TraceAnalysisToolMain() {
		super();
	}

	/**
	 * Configure and execute the analysis.
	 *
	 * @param args
	 *            arguments
	 */
	public static void main(final String... args) {
		System.exit(new TraceAnalysisToolMain().run("TraceAnalysisTool", "traceAnalysisTool", args, new TraceAnalysisParameters())); // NOPMD
	}

	/**
	 * Run the application without calling exit. This is glue code for the TraceAnalysisGUI.
	 *
	 * @param args
	 *            arguments
	 */
	public static void runEmbedded(final String... args) {
		new TraceAnalysisToolMain().run("TraceAnalysisTool", "traceAnalysisTool", args, new TraceAnalysisParameters());
	}

	@Override
	protected int execute(final JCommander commander, final String label) throws ConfigurationException {
		final DateFormat dateFormat = new SimpleDateFormat(DateConverter.DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		if (this.settings.getIgnoreExecutionsBeforeDate() != null) {
			this.logger.info("Ignoring records before {} ({})", dateFormat.format(this.settings.getIgnoreExecutionsBeforeDate()),
					this.settings.getIgnoreExecutionsBeforeDate());
		}
		if (this.settings.getIgnoreExecutionsAfterDate() != null) {
			this.logger.info("Ignoring records after {} ({})", dateFormat.format(this.settings.getIgnoreExecutionsAfterDate()),
					this.settings.getIgnoreExecutionsAfterDate());
		}

		this.settings.dumpConfiguration(this.logger);

		if (new PerformAnalysis(this.logger, this.settings).dispatchTasks()) {
			this.logger.info("Analysis complete. See 'kieker.log' for details.");
			return AbstractLegacyTool.SUCCESS_EXIT_CODE;
		} else {
			this.logger.error("Analysis incomplete. See 'kieker.log' for details.");
			return AbstractLegacyTool.RUNTIME_ERROR;
		}
	}

	/** support for external configuration file. */
	@Override
	protected Path getConfigurationPath() {
		return null; // Trace analysis does not support configuration files yet
	}

	@Override
	protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration, final JCommander commander) {
		return true;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return this.checkInputDirs()
				&& ParameterEvaluationUtils.checkDirectory(this.settings.getOutputDir(), "Output", commander)
				&& this.selectOrFilterTraces();
	}

	@Override
	protected void shutdownService() {
		// nothing to do here
	}

	private boolean checkNotEmpty(final List<?> list) {
		if (list == null) {
			return false;
		} else {
			return !list.isEmpty();
		}
	}

	/**
	 * Check that selecting and filtering traces is not done at the same time.
	 *
	 * @return true if not both trace features have been requested
	 */
	private boolean selectOrFilterTraces() {
		if (this.checkNotEmpty(this.settings.getSelectTraces()) && this.checkNotEmpty(this.settings.getFilterTraces())) {
			this.logger.error("Trace Id selection and filtering are mutually exclusive");
			return false;
		} else if (this.settings.getSelectTraces() != null) {
			final int numSelectedTraces = this.settings.getSelectTraces().size();
			try {
				for (final Long idStr : this.settings.getSelectTraces()) {
					this.settings.getSelectedTraces().add(idStr);
				}
				this.logger.info("{} trace{} selected", numSelectedTraces, numSelectedTraces > 1 ? "s" : ""); // NOCS
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				this.logger.error("Failed to parse list of trace IDs: {}", this.settings.getSelectTraces().toArray().toString(), e);
				return false;
			}
		} else if (this.settings.getFilterTraces() != null) {
			this.settings.setInvertTraceIdFilter(true);
			final String[] traceIdList = this.settings.getFilterTraces().toArray(new String[this.settings.getFilterTraces().size()]);

			final int numSelectedTraces = traceIdList.length;
			try {
				for (final Long idStr : this.settings.getSelectTraces()) {
					this.settings.getSelectedTraces().add(idStr);
				}
				this.logger.info("{} trace{} filtered", numSelectedTraces, numSelectedTraces > 1 ? "s" : ""); // NOCS
			} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
				this.logger.error("Failed to parse list of trace IDs: {}", this.settings.getSelectTraces().toArray().toString(), e);
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
	private boolean checkInputDirs() {
		if (this.settings.getInputDirs() == null) {
			this.logger.error("No input directories specified.");
			return false;
		}
		for (final File inputDir : this.settings.getInputDirs()) {
			try {
				if (!inputDir.exists()) {
					this.logger.error("The specified input directory '{}' does not exist", inputDir.getCanonicalPath());
					return false;
				}
				if (!inputDir.isDirectory() && !inputDir.getAbsolutePath().endsWith(FSUtil.ZIP_FILE_EXTENSION)) {
					this.logger.error("The specified input directory '{}' is neither a directory nor a zip file", inputDir.getCanonicalPath());
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
						this.logger.error("The specified input directory '{}' is not a kieker log directory", inputDir.getCanonicalPath());
						return false;
					}
				}
			} catch (final IOException e) { // thrown by File.getCanonicalPath()
				this.logger.error("Error resolving name of input directory: '{}'", inputDir);
			}
		}

		return true;
	}

}
