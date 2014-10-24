/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.analysis.opad;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.tslib.TimeSeriesPoint;

/**
 * @author Thomas Duellmann
 *
 * @since 1.11
 */

@Plugin(description = "A filter to read a timeseries from a file with one value per line.",
		outputPorts = @OutputPort(name = SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TIME_SERIES, description = "Provides the read data from file as Timeseries.", eventTypes = { TimeSeriesPoint.class }),
		configuration = {
			@Property(name = SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_INPUT_FILE, description = "", defaultValue = "inputTS.csv"),
		})
public class SimpleTimeSeriesFileReader extends AbstractFilterPlugin {

	private static final Log LOG = LogFactory.getLog(SimpleTimeSeriesFileReader.class);

	public static final String CONFIG_PROPERTY_NAME_INPUT_FILE = "inputfile";
	public static final String OUTPUT_PORT_NAME_TIME_SERIES = "timeseries";

	private final String inputFile;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public SimpleTimeSeriesFileReader(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.inputFile = this.configuration.getPathProperty(CONFIG_PROPERTY_NAME_INPUT_FILE);
		this.readInputFile(this.inputFile);
	}

	public void readInputFile(final String inputFile, final String encoding)
	{
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;

		try {
			fis = new FileInputStream(inputFile);
			isr = new InputStreamReader(fis, encoding);
			br = new BufferedReader(isr);

			TimeSeriesPoint<Long> tsp;

			LOG.info("Starting to read the file \"" + inputFile + "\" using encoding: " + encoding);

			// As we only have the values, we use a continouous timestamp starting at 1
			long timestamp = 1;
			while (br.ready()) {
				final String readLine = br.readLine();
				Long tsValue;
				try {
					tsValue = Long.valueOf(readLine);
					tsp = new TimeSeriesPoint<Long>(timestamp, tsValue);
					super.deliver(SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TIME_SERIES, tsp);
					timestamp++;

				} catch (final NumberFormatException nfe) {
					// The current entry seems to be erroneous, so we skip it.
					LOG.warn("Could not convert the line \"" + readLine + "\" to long. Skipping it.");
					break;
				}
			}
		} catch (final FileNotFoundException fnfe) {
			LOG.error("Could not find the given file " + this.inputFile, fnfe);
		} catch (final IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (isr != null) {
					isr.close();
				}
				if (fis != null) {
					fis.close();
				}
			} catch (final IOException e)
			{
			}
		}
	}

	public void readInputFile(final String inputFile) {
		this.readInputFile(inputFile, "UTF-8");
	}

	@Override
	public final void terminate(final boolean error) {

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration config = new Configuration();
		config.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_INPUT_FILE, this.inputFile);
		return config;
	}
}
