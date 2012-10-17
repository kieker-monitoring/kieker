/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

	/**
	 * Creates a new, empty graph writer configuration.
	 */
	public GraphWriterConfiguration() {
		this.configuration = new Configuration();
	}

	/**
	 * Creates a new graph writer configuration that wraps the given configuration.
	 * 
	 * @param configuration
	 *            The configuration to wrap
	 */
	public GraphWriterConfiguration(final Configuration configuration) {
		this.configuration = configuration;
	}

	/**
	 * Returns the configuration wrapped by this configuration.
	 * 
	 * @return See above
	 */
	public Configuration getConfiguration() {
		return this.configuration;
	}

	/**
	 * Returns the output file name specified by this configuration.
	 * 
	 * @return See above
	 */
	public String getOutputFileName() {
		return this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME); // never null
	}

	/**
	 * Sets the output file name specified by this configuration.
	 * 
	 * @param outputFileName
	 *            The output file name to set
	 */
	public void setOutputFileName(final String outputFileName) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_FILE_NAME, outputFileName);
	}

	/**
	 * Returns the output path specified by this configuration.
	 * 
	 * @return See above
	 */
	public String getOutputPath() {
		return this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME);
	}

	/**
	 * Sets the output path specified by this configuration.
	 * 
	 * @param outputPath
	 *            The output path to set
	 */
	public void setOutputPath(final String outputPath) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_OUTPUT_PATH_NAME, outputPath);
	}

	/**
	 * Returns whether edge weights should be included.
	 * 
	 * @return See above
	 */
	public boolean doIncludeWeights() {
		return this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS);
	}

	/**
	 * Specifies whether edge weights should be included.
	 * 
	 * @param value
	 *            The value to set
	 */
	public void setIncludeWeights(final boolean value) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_INCLUDE_WEIGHTS, String.valueOf(value));
	}

	/**
	 * Returns whether short labels should be used.
	 * 
	 * @return See above
	 */
	public boolean doUseShortLabels() {
		return this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SHORTLABELS);
	}

	/**
	 * Specifies whether short labels should be used.
	 * 
	 * @param value
	 *            THe value to set
	 */
	public void setUseShortLabels(final boolean value) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_SHORTLABELS, String.valueOf(value));
	}

	/**
	 * Returns whether immediate loops should be plotted.
	 * 
	 * @return See above
	 */
	public boolean doPlotLoops() {
		return this.configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_SELFLOOPS);
	}

	/**
	 * Specifies whether immediate loops should be plotted.
	 * 
	 * @param value
	 *            The value to set
	 */
	public void setPlotLoops(final boolean value) {
		this.configuration.setProperty(CONFIG_PROPERTY_NAME_SELFLOOPS, String.valueOf(value));
	}

}
