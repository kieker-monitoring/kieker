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
public class GraphWriterConfiguration {
	/**
	 * Name of the configuration property containing the output file name.
	 */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME = "dotOutputFn";
	/**
	 * Name of the configuration property containing the output path name.
	 */
	public static final String CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME = "outputPath";
	/**
	 * Name of the configuration property indicating that weights should be included.
	 */
	public static final String CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS = "includeWeights";
	/**
	 * Name of the configuration property indicating that short labels should be used.
	 */
	public static final String CONFIG_PROPERTY_NAME_SHORTLABELS = "shortLabels";
	/**
	 * Name of the configuration property indicating that self-loops should be displayed.
	 */
	public static final String CONFIG_PROPERTY_NAME_SELFLOOPS = "selfLoops";

	private final Configuration configuration;

	public GraphWriterConfiguration() {
		this.configuration = new Configuration();
	}

	public GraphWriterConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	public Configuration getConfiguration() {
		return this.configuration;
	}

	public String getOutputFileName() {
		return this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME);
	}

	public void setOutputFileName(final String outputFileName) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME, outputFileName);
	}

	public String getOutputPath() {
		return this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME);
	}

	public void setOutputPath(final String outputPath) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, outputPath);
	}

	public boolean doIncludeWeights() {
		return this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
	}

	public void setIncludeWeights(final boolean value) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, String.valueOf(value));
	}

	public boolean doUseShortLabels() {
		return this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
	}

	public void setUseShortLabels(final boolean value) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_SHORTLABELS, String.valueOf(value));
	}

	public boolean doPlotLoops() {
		return this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);
	}

	public void setPlotLoops(final boolean value) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_SELFLOOPS, String.valueOf(value));
	}

}
