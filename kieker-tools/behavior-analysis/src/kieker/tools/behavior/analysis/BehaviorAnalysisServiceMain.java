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

import java.io.File;

import com.beust.jcommander.JCommander;

import kieker.analysis.behavior.IEntryCallAcceptanceMatcher;
import kieker.analysis.behavior.ITraceSignatureCleanupRewriter;
import kieker.analysis.behavior.clustering.IModelGenerationFilterFactory;
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
		final String outputUrl = configuration.getStringProperty(ConfigurationKeys.RESULT_URL, "");

		if ("".equals(outputUrl)) {
			this.logger.error("Initialization incomplete: No result dictionary specified.");
			return false;
		} else {
			this.parameterConfiguration.setOutputUrl(outputUrl);
		}

		this.parameterConfiguration.setReturnMedoids(configuration.getBooleanProperty(ConfigurationKeys.RETURN_MEDOIDS, true));

		this.parameterConfiguration.setReturnClustering(configuration.getBooleanProperty(ConfigurationKeys.RETURN_CLUSTERING, false));

		/** For SessionAcceptanceFilter. */
		this.parameterConfiguration.setEntryCallAcceptanceMatcher(ParameterEvaluationUtils.createFromConfiguration(
				IEntryCallAcceptanceMatcher.class, configuration, ConfigurationKeys.ENTRY_CALL_ACCEPTANCE_MATCHER,
				"No entry call acceptance matcher specified."));

		/** For TraceOperationsCleanupFilter */
		this.parameterConfiguration.setTraceSignatureCleanupRewriter(ParameterEvaluationUtils.createFromConfiguration(
				ITraceSignatureCleanupRewriter.class, configuration, ConfigurationKeys.TRACE_OPERATION_CLEANUP_REWRITER,
				"No signature cleanup rewriter specified."));

		/** For TSessionOperationsFilter */
		this.parameterConfiguration.setModelGenerationFilterFactory(ParameterEvaluationUtils.createFromConfiguration(
				IModelGenerationFilterFactory.class, configuration, ConfigurationKeys.ENTRY_CALL_FILTER_RULES_FACTORY,
				"No entry call filter rules factory specified."));

		this.parameterConfiguration.setClusteringDistance(configuration.getDoubleProperty(ConfigurationKeys.EPSILON, 10));

		this.parameterConfiguration.setMinPts(configuration.getIntProperty(ConfigurationKeys.MIN_PTS, 20));

		this.parameterConfiguration.setMaxAmount(configuration.getIntProperty(ConfigurationKeys.MAX_MODEL_AMOUNT, -1));

		if (this.parameterConfiguration.getEntryCallAcceptanceMatcher() == null
				|| this.parameterConfiguration.getTraceSignatureCleanupRewriter() == null
				|| this.parameterConfiguration.getModelGenerationFilterFactory() == null) {
			return false;
		}

		return true;
	}

	@Override
	protected boolean checkParameters(final JCommander commander) throws ConfigurationException {
		return ParameterEvaluationUtils.isFileReadable(this.getConfigurationFile(), "configuration file", commander);
	}

	@Override
	protected void shutdownService() {}

}
