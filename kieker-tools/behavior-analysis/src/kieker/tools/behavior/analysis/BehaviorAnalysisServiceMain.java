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
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.beust.jcommander.JCommander;

import kieker.analysis.behavior.acceptance.matcher.EAcceptanceMode;
import kieker.analysis.behavior.clustering.IParameterWeighting;
import kieker.analysis.behavior.signature.processor.ITraceSignatureProcessor;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;
import kieker.tools.source.LogsReaderCompositeStage;

/**
 *
 * @author Lars Jürgensen
 *
 */
public final class BehaviorAnalysisServiceMain
		extends AbstractService<BehaviorAnalysisConfiguration, BehaviorAnalysisSettings> {

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
		java.lang.System.exit(new BehaviorAnalysisServiceMain().run("Service Behavior Analysis",
				"service-behavior-analysis", args, new BehaviorAnalysisSettings()));
	}

	@Override
	protected File getConfigurationFile() {
		return this.settings.getConfigurationFile();
	}

	@Override
	protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
			final JCommander commander) {
		final String userSessionTimeout = configuration.getStringProperty(ConfigurationKeys.USER_SESSION_TIMEOUT, null);
		if (userSessionTimeout == null) {
			this.settings.setUserSessionTimeout(null);
		} else {
			this.settings.setUserSessionTimeout(Long.parseLong(userSessionTimeout));
		}

		final String clusterOutputFile = configuration.getStringProperty(ConfigurationKeys.CLUSTER_OUTPUT_FILE);
		if (clusterOutputFile != null) {
			this.settings.setClusterOutputPath(Paths.get(clusterOutputFile));
		}

		final String medoidsOutputFile = configuration.getStringProperty(ConfigurationKeys.MEDOIDS_OUTPUT_FILE);
		if (medoidsOutputFile != null) {
			this.settings.setMedoidOutputPath(Paths.get(medoidsOutputFile));
		}

		/** For SessionAcceptanceFilter. */
		this.settings.setClassSignatureAcceptancePatterns(
				this.readSignatures(configuration.getStringProperty(ConfigurationKeys.CLASS_SIGNATURE_ACCEPTANCE_MATCHER_FILE), "class signature patterns",
						commander));
		this.settings.setOperationSignatureAcceptancePatterns(
				this.readSignatures(configuration.getStringProperty(ConfigurationKeys.OPERATION_SIGNATURE_ACCEPTANCE_MATCHER_FILE), "operation signature patterns",
						commander));
		this.settings.setAcceptanceMatcherMode(
				configuration.getEnumProperty(ConfigurationKeys.SIGNATURE_ACCEPTANCE_MATCHER_MODE, EAcceptanceMode.class, EAcceptanceMode.NORMAL));

		/** For TraceSignatureProcessor. */
		this.settings.setTraceSignatureProcessor(ParameterEvaluationUtils.createFromConfiguration(
				ITraceSignatureProcessor.class, configuration, ConfigurationKeys.TRACE_SIGNATURE_PROCESSOR,
				"No signature cleanup rewriter specified."));

		this.settings.setClusteringDistance(configuration.getDoubleProperty(ConfigurationKeys.EPSILON, 10));

		this.settings.setMinPts(configuration.getIntProperty(ConfigurationKeys.MIN_PTS, 20));

		this.settings.setMaxAmount(configuration.getIntProperty(ConfigurationKeys.MAX_MODEL_AMOUNT, -1));

		this.settings.setNodeInsertCost(configuration.getDoubleProperty(ConfigurationKeys.NODE_INSERTION_COST, 10));
		this.settings.setEdgeInsertCost(configuration.getDoubleProperty(ConfigurationKeys.EDGE_INSERTION_COST, 5));
		this.settings.setEventGroupInsertCost(configuration.getDoubleProperty(ConfigurationKeys.EVENT_GROUP_INSERTION_COST, 4));

		this.settings.setWeighting(ParameterEvaluationUtils.createFromConfiguration(IParameterWeighting.class, configuration,
				ConfigurationKeys.PARAMETER_WEIGHTING, "missing parameter weighting function."));

		final String[] directoryNames = configuration.getStringArrayProperty(LogsReaderCompositeStage.LOG_DIRECTORIES, ":");
		this.settings.setDirectories(new ArrayList<>(directoryNames.length));

		for (final String name : directoryNames) {
			final File directory = new File(name);
			if (ParameterEvaluationUtils.checkDirectory(directory, "log file", commander)) {
				this.settings.getDirectories().add(directory);
			} else {
				this.logger.error("Log directory {} cannot be read or does not exist.", name);
			}
		}
		if (this.settings.getDirectories().size() == 0) {
			this.logger.error("No log files found.");
			return false;
		}

		this.settings
				.setDataBufferSize(configuration.getIntProperty(LogsReaderCompositeStage.DATA_BUFFER_SIZE, 8192));
		this.settings.setVerbose(configuration.getBooleanProperty(LogsReaderCompositeStage.VERBOSE, false));

		if (this.settings.getTraceSignatureProcessor() == null) {
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

		}
		return patterns;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return ParameterEvaluationUtils.isFileReadable(this.getConfigurationFile(), "configuration file", commander);
	}

	@Override
	protected void shutdownService() {}

}
