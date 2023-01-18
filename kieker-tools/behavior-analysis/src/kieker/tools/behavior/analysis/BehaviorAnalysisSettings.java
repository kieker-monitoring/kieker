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
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.PathConverter;

import kieker.analysis.behavior.acceptance.matcher.AcceptanceModeConverter;
import kieker.analysis.behavior.acceptance.matcher.EAcceptanceMode;
import kieker.analysis.behavior.clustering.IParameterWeighting;
import kieker.analysis.behavior.clustering.NaiveParameterWeighting;
import kieker.analysis.behavior.clustering.ParameterWeightingConverter;
import kieker.analysis.behavior.signature.processor.ITraceSignatureProcessor;
import kieker.analysis.behavior.signature.processor.TraceSignatureProcessorConverter;
import kieker.tools.settings.ParentPathValueValidator;
import kieker.tools.settings.Setting;

/**
 * All settings of the behavior analysis.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public final class BehaviorAnalysisSettings { // NOPMD configuration class needs many fields

	@Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")
	private File configurationFile;

	@Setting(converter = PathConverter.class, validators = ParentPathValueValidator.class)
	private Path clusterOutputPath;
	@Setting(converter = PathConverter.class, validators = ParentPathValueValidator.class)
	private Path medoidOutputPath;

	private List<Pattern> classSignatureAcceptancePatterns;
	private List<Pattern> operationSignatureAcceptancePatterns;

	@Setting(converter = AcceptanceModeConverter.class)
	private EAcceptanceMode acceptanceMatcherMode;
	@Setting(converter = TraceSignatureProcessorConverter.class)
	private ITraceSignatureProcessor traceSignatureProcessor;

	@Setting
	private double clusteringDistance;

	@Setting
	private int minPts;

	@Setting
	private Integer maxAmount;

	@Setting
	private Long userSessionTimeout;

	@Setting
	private double nodeInsertCost;

	@Setting
	private double edgeInsertCost;

	@Setting
	private double eventGroupInsertCost;

	@Setting(converter = ParameterWeightingConverter.class)
	private IParameterWeighting parameterWeighting = new NaiveParameterWeighting();

	@Setting(variableArity = true, converter = FileConverter.class, required = true)
	private List<File> directories;

	@Setting
	private int dataBufferSize;

	@Setting
	private boolean verbose;

	public BehaviorAnalysisSettings() {
		// default constructor
	}

	public final File getConfigurationFile() {
		return this.configurationFile;
	}

	public final void setConfigurationFile(final File configurationFile) {
		this.configurationFile = configurationFile;
	}

	public Path getClusterOutputPath() {
		return this.clusterOutputPath;
	}

	public Path getMedoidOutputPath() {
		return this.medoidOutputPath;
	}

	public ITraceSignatureProcessor getTraceSignatureProcessor() {
		return this.traceSignatureProcessor;
	}

	public double getClusteringDistance() {
		return this.clusteringDistance;
	}

	public int getMinPts() {
		return this.minPts;
	}

	public Integer getMaxAmount() {
		return this.maxAmount;
	}

	public List<Pattern> getClassSignatureAcceptancePatterns() {
		return this.classSignatureAcceptancePatterns;
	}

	public void setClassSignatureAcceptancePatterns(final List<Pattern> classSignatureAcceptancePatterns) {
		this.classSignatureAcceptancePatterns = classSignatureAcceptancePatterns;
	}

	public List<Pattern> getOperationSignatureAcceptancePatterns() {
		return this.operationSignatureAcceptancePatterns;
	}

	public void setOperationSignatureAcceptancePatterns(final List<Pattern> operationSignatureAcceptancePatterns) {
		this.operationSignatureAcceptancePatterns = operationSignatureAcceptancePatterns;
	}

	public EAcceptanceMode getAcceptanceMatcherMode() {
		return this.acceptanceMatcherMode;
	}

	public void setAcceptanceMatcherMode(final EAcceptanceMode acceptanceMatcherMode) {
		this.acceptanceMatcherMode = acceptanceMatcherMode;
	}

	public Long getUserSessionTimeout() {
		return this.userSessionTimeout;
	}

	public void setUserSessionTimeout(final Long userSessionTimeout) {
		this.userSessionTimeout = userSessionTimeout;
	}

	public double getNodeInsertCost() {
		return this.nodeInsertCost;
	}

	public void setNodeInsertCost(final double nodeInsertCost) {
		this.nodeInsertCost = nodeInsertCost;
	}

	public double getEdgeInsertCost() {
		return this.edgeInsertCost;
	}

	public void setEdgeInsertCost(final double edgeInsertCost) {
		this.edgeInsertCost = edgeInsertCost;
	}

	public double getEventGroupInsertCost() {
		return this.eventGroupInsertCost;
	}

	public void setEventGroupInsertCost(final double eventGroupInsertCost) {
		this.eventGroupInsertCost = eventGroupInsertCost;
	}

	public IParameterWeighting getParameterWeighting() {
		return this.parameterWeighting;
	}

	public void setParameterWeighting(final IParameterWeighting parameterWeighting) {
		this.parameterWeighting = parameterWeighting;
	}

	public List<File> getDirectories() {
		return this.directories;
	}

	public int getDataBufferSize() {
		return this.dataBufferSize;
	}

	public boolean isVerbose() {
		return this.verbose;
	}

}
