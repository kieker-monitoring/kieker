/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.tools.behavior.analysis;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;
import kieker.tools.settings.ConfigurationParser;

/**
 * Main class for behavior analysis.
 *
 * @author Lars Jürgensen
 * @author Reiner Jung
 * @since 2.0.0
 */
public final class BehaviorAnalysisServiceMain
		extends AbstractService<BehaviorAnalysisConfiguration, BehaviorAnalysisSettings> {

	private static final String PREFIX = "kieker.tools.behavior";

	private static final String FULL_PREFIX = PREFIX + ".";

	private static final String CLASS_SIGNATURE_ACCEPTANCE_MATCHER_FILE = FULL_PREFIX + "classSignatureAcceptancePatternFile";

	private static final String OPERATION_SIGNATURE_ACCEPTANCE_MATCHER_FILE = FULL_PREFIX + "operationSignatureAcceptancePatternFile";

	/**
	 * Default constructor.
	 */
	private BehaviorAnalysisServiceMain() {
		// do nothing here
	}

	@Override
	protected BehaviorAnalysisConfiguration createTeetimeConfiguration() throws ConfigurationException {
		return new BehaviorAnalysisConfiguration(this.settings);
	}

	/**
	 * Main function.
	 *
	 * @param args
	 *            command line arguments.
	 */
	public static void main(final String[] args) {
		System.exit(new BehaviorAnalysisServiceMain().run("Service Behavior Analysis",
				"service-behavior-analysis", args, new BehaviorAnalysisSettings()));
	}

	@Override
	protected Path getConfigurationPath() {
		return this.settings.getConfigurationPath();
	}

	@Override
	protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
			final JCommander commander) {
		final ConfigurationParser parser = new ConfigurationParser(BehaviorAnalysisServiceMain.PREFIX, this.settings);

		try {
			parser.parse(configuration);
		} catch (final ParameterException e) {
			this.logger.error(e.getLocalizedMessage());
			return false;
		}

		/** For SessionAcceptanceFilter. */
		this.settings.setClassSignatureAcceptancePatterns(
				this.readSignatures(configuration.getStringProperty(BehaviorAnalysisServiceMain.CLASS_SIGNATURE_ACCEPTANCE_MATCHER_FILE),
						"class signature patterns", commander));
		this.settings.setOperationSignatureAcceptancePatterns(
				this.readSignatures(configuration.getStringProperty(BehaviorAnalysisServiceMain.OPERATION_SIGNATURE_ACCEPTANCE_MATCHER_FILE),
						"operation signature patterns", commander));

		if (this.settings.getDirectories().size() == 0) {
			this.logger.error("No log files found.");
			return false;
		}

		if (this.settings.getTraceSignatureProcessor() == null) {
			return false;
		}

		if ((this.settings.getClusterOutputPath() == null) && (this.settings.getMedoidOutputPath() == null)) {
			this.logger.error("You need to specify at least a cluster or a medoid output path.");
			return false;
		}

		return true;
	}

	private List<Pattern> readSignatures(final String filename, final String errorMessage, final JCommander commander) {
		final File file = new File(filename);
		if (ParameterEvaluationUtils.isFileReadable(file, errorMessage, commander)) {
			return this.readListFromFile(file);
		} else {
			return null;
		}
	}

	private List<Pattern> readListFromFile(final File signatureAcceptanceMatcherFile) {
		final List<Pattern> patterns = new ArrayList<>();
		try (final BufferedReader reader = Files.newBufferedReader(signatureAcceptanceMatcherFile.toPath())) {
			reader.lines().forEach(line -> patterns.add(Pattern.compile(line.trim())));
		} catch (final IOException e) {
			this.logger.error(String.format("Error reading signature patterns from %s", signatureAcceptanceMatcherFile.toString()));
		}
		return patterns;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return ParameterEvaluationUtils.isFileReadable(this.getConfigurationPath().toFile(), "configuration file", commander);
	}

	@Override
	protected void shutdownService() {
		// no special shutdown operations necessary
	}

}
