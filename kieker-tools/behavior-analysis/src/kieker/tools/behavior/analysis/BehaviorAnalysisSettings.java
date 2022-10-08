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

import com.beust.jcommander.Parameter;

import kieker.analysis.behavior.IEntryCallAcceptanceMatcher;
import kieker.analysis.behavior.ITraceSignatureCleanupRewriter;
import kieker.analysis.behavior.clustering.IModelGenerationFilterFactory;

/**
 *
 * @author Lars Jürgensen
 *
 */
public class BehaviorAnalysisSettings {

	@Parameter(names = { "-c", "--configuration" }, required = true, description = "Configuration file")
	private File configurationFile;

	private String outputUrl;
	private boolean returnMedoids;
	private boolean returnClustering;
	private IEntryCallAcceptanceMatcher entryCallAcceptanceMatcher;
	private ITraceSignatureCleanupRewriter traceSignatureCleanupRewriter;
	private IModelGenerationFilterFactory modelGenerationFilterFactory;

	private double clusteringDistance;

	private int minPts;

	private int maxAmount;

	public final File getConfigurationFile() {
		return this.configurationFile;
	}

	public final void setConfigurationFile(final File configurationFile) {
		this.configurationFile = configurationFile;
	}

	public void setOutputUrl(final String outputUrl) {
		this.outputUrl = outputUrl;
	}

	public String getOutputUrl() {
		return this.outputUrl;
	}

	public void setReturnMedoids(final boolean returnMedoids) {
		this.returnMedoids = returnMedoids;
	}

	public boolean isReturnMedoids() {
		return this.returnMedoids;
	}

	public void setReturnClustering(final boolean returnClustering) {
		this.returnClustering = returnClustering;
	}

	public boolean isReturnClustering() {
		return this.returnClustering;
	}

	public void setEntryCallAcceptanceMatcher(final IEntryCallAcceptanceMatcher entryCallAcceptanceMatcher) {
		this.entryCallAcceptanceMatcher = entryCallAcceptanceMatcher;
	}

	public IEntryCallAcceptanceMatcher getEntryCallAcceptanceMatcher() {
		return this.entryCallAcceptanceMatcher;
	}

	public void setTraceSignatureCleanupRewriter(final ITraceSignatureCleanupRewriter traceSignatureCleanupRewriter) {
		this.traceSignatureCleanupRewriter = traceSignatureCleanupRewriter;
	}

	public ITraceSignatureCleanupRewriter getTraceSignatureCleanupRewriter() {
		return this.traceSignatureCleanupRewriter;
	}

	public void setModelGenerationFilterFactory(final IModelGenerationFilterFactory modelGenerationFilterFactory) {
		this.modelGenerationFilterFactory = modelGenerationFilterFactory;
	}

	public IModelGenerationFilterFactory getModelGenerationFilterFactory() {
		return this.modelGenerationFilterFactory;
	}

	public void setClusteringDistance(final double clusteringDistance) {
		this.clusteringDistance = clusteringDistance;
	}

	public double getClusteringDistance() {
		return this.clusteringDistance;
	}

	public void setMinPts(int minPts) {
		this.minPts = minPts;
	}
	
	public int getMinPts() {
		return minPts;
	}

	public void setMaxAmount(int maxAmount) {
		this.maxAmount = maxAmount;
	}

	public int getMaxAmount() {
		return maxAmount;
	}
}
