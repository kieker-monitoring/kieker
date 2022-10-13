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

import kieker.analysis.behavior.signature.processor.ITraceSignatureProcessor;
import kieker.common.exception.ConfigurationException;
import kieker.tools.common.AbstractService;
import kieker.tools.common.ParameterEvaluationUtils;

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
		return new BehaviorAnalysisConfiguration(this.parameterConfiguration, this.kiekerConfiguration);
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
		return this.parameterConfiguration.getConfigurationFile();
	}

	@Override
	protected boolean checkConfiguration(final kieker.common.configuration.Configuration configuration,
			final JCommander commander) {
		final String clusterOutputFile = configuration.getStringProperty(ConfigurationKeys.CLUSTER_OUTPUT_FILE);
		if (clusterOutputFile != null) {
			this.parameterConfiguration.setClusterOutputPath(Paths.get(clusterOutputFile));
		}

		final String medoidsOutputFile = configuration.getStringProperty(ConfigurationKeys.MEDOIDS_OUTPUT_FILE);
		if (medoidsOutputFile != null) {
			this.parameterConfiguration.setMedoidOutputPath(Paths.get(medoidsOutputFile));
		}

		/** For SessionAcceptanceFilter. */
		this.parameterConfiguration.setClassSignatureAcceptancePatterns(
				this.readSignatures(configuration.getStringProperty(ConfigurationKeys.CLASS_SIGNATURE_ACCEPTANCE_MATCHER_FILE), "class signature patterns",
						commander));
		this.parameterConfiguration.setOperationSignatureAcceptancePatterns(
				this.readSignatures(configuration.getStringProperty(ConfigurationKeys.OPERATION_SIGNATURE_ACCEPTANCE_MATCHER_FILE), "operation signature patterns",
						commander));
		// TODO make this an enumeration
		this.parameterConfiguration.setAcceptanceMatcherMode(configuration.getBooleanProperty(ConfigurationKeys.SIGNATURE_ACCEPTANCE_MATCHER_MODE, false));

		/** For TraceSignatureProcessor. */
		this.parameterConfiguration.setTraceSignatureProcessor(ParameterEvaluationUtils.createFromConfiguration(
				ITraceSignatureProcessor.class, configuration, ConfigurationKeys.TRACE_SIGNATURE_PROCESSOR,
				"No signature cleanup rewriter specified."));

		this.parameterConfiguration.setClusteringDistance(configuration.getDoubleProperty(ConfigurationKeys.EPSILON, 10));

		this.parameterConfiguration.setMinPts(configuration.getIntProperty(ConfigurationKeys.MIN_PTS, 20));

		this.parameterConfiguration.setMaxAmount(configuration.getIntProperty(ConfigurationKeys.MAX_MODEL_AMOUNT, -1));

		if (this.parameterConfiguration.getTraceSignatureProcessor() == null) {
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
