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
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.beust.jcommander.Parameter;

import kieker.analysis.behavior.acceptance.matcher.EAcceptanceMode;
import kieker.analysis.behavior.clustering.IParameterWeighting;
import kieker.analysis.behavior.signature.processor.ITraceSignatureProcessor;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorAnalysisSettings {

	@Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")
	private File configurationFile;

	private Path clusterOutputPath;
	private Path medoidOutputPath;

	private List<Pattern> classSignatureAcceptancePatterns;
	private List<Pattern> operationSignatureAcceptancePatterns;
	private EAcceptanceMode acceptanceMatcherMode;
	private ITraceSignatureProcessor traceSignatureProcessor;

	private double clusteringDistance;

	private int minPts;

	private int maxAmount;

	private Long userSessionTimeout;

	private double nodeInsertCost;
	private double edgeInsertCost;
	private double eventGroupInsertCost;

	private IParameterWeighting weighting;

	private List<File> directories;

	private int dataBufferSize;

	private boolean verbose;

	public final File getConfigurationFile() {
		return this.configurationFile;
	}

	public final void setConfigurationFile(final File configurationFile) {
		this.configurationFile = configurationFile;
	}

	public Path getClusterOutputPath() {
		return this.clusterOutputPath;
	}

	public void setClusterOutputPath(final Path clusterOutputPath) {
		this.clusterOutputPath = clusterOutputPath;
	}

	public Path getMedoidOutputPath() {
		return this.medoidOutputPath;
	}

	public void setMedoidOutputPath(final Path medoidOutputPath) {
		this.medoidOutputPath = medoidOutputPath;
	}

	public void setTraceSignatureProcessor(final ITraceSignatureProcessor traceSignatureProcessor) {
		this.traceSignatureProcessor = traceSignatureProcessor;
	}

	public ITraceSignatureProcessor getTraceSignatureProcessor() {
		return this.traceSignatureProcessor;
	}

	public void setClusteringDistance(final double clusteringDistance) {
		this.clusteringDistance = clusteringDistance;
	}

	public double getClusteringDistance() {
		return this.clusteringDistance;
	}

	public void setMinPts(final int minPts) {
		this.minPts = minPts;
	}

	public int getMinPts() {
		return this.minPts;
	}

	public void setMaxAmount(final int maxAmount) {
		this.maxAmount = maxAmount;
	}

	public int getMaxAmount() {
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

	public IParameterWeighting getWeighting() {
		return this.weighting;
	}

	public void setWeighting(final IParameterWeighting weighting) {
		this.weighting = weighting;
	}

	public List<File> getDirectories() {
		return this.directories;
	}

	public void setDirectories(final ArrayList<File> directories) {
		this.directories = directories;
	}

	public int getDataBufferSize() {
		return this.dataBufferSize;
	}

	public void setDataBufferSize(final int dataBufferSize) {
		this.dataBufferSize = dataBufferSize;
	}

	public boolean isVerbose() {
		return this.verbose;
	}

	public void setVerbose(final boolean verbose) {
		this.verbose = verbose;
	}

}
