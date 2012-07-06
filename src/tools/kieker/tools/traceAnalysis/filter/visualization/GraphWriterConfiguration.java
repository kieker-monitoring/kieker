/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.filter.visualization;

import kieker.common.configuration.Configuration;

/**
 * Configuration class for the graph writer plugin (see {@link GraphWriterPlugin}).
 * 
 * @author Holger Knoche
 * 
 */
public class GraphWriterConfiguration extends Configuration {

	private static final long serialVersionUID = 7069737251772871133L;

	/**
	 * Name of the configuration property containing the output file name.
	 */
	private static final String CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME = "dotOutputFn";
	/**
	 * Name of the configuration property containing the output path name.
	 */
	private static final String CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME = "outputPath";
	/**
	 * Name of the configuration property indicating that weights should be included.
	 */
	private static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	/**
	 * Name of the configuration property indicating that short labels should be used.
	 */
	private static final String CONFIG_PROPERTY_NAME_SHORTLABELS = "shortLabels";
	/**
	 * Name of the configuration property indicating that self-loops should be displayed.
	 */
	private static final String CONFIG_PROPERTY_NAME_SELFLOOPS = "selfLoops";

	public String getOutputFileName() {
		return this.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME);
	}

	public void setOutputFileName(final String outputFileName) {
		this.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME, outputFileName);
	}

	public String getOutputPath() {
		return this.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME);
	}

	public void setOutputPath(final String outputPath) {
		this.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, outputPath);
	}

	public boolean doIncludeWeights() {
		return this.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
	}

	public void setIncludeWeights(final boolean value) {
		this.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, String.valueOf(value));
	}

	public boolean doUseShortLabels() {
		return this.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
	}

	public void setUseShortLabels(final boolean value) {
		this.setProperty(CONFIG_PROPERTY_NAME_SHORTLABELS, String.valueOf(value));
	}

	public boolean doPlotLoops() {
		return this.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);
	}

	public void setPlotLoops(final boolean value) {
		this.setProperty(CONFIG_PROPERTY_NAME_SELFLOOPS, String.valueOf(value));
	}
}
