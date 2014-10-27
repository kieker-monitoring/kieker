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
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;

/**
 * @author Thomas Duellmann
 *
 * @since 1.11
 */

@Plugin(description = "A filter to read a timeseries from a file with one value per line.",
		outputPorts = @OutputPort(name = SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TS_POINTS, description = "Provides the read data from file as Timeseries.", eventTypes = { NamedDoubleTimeSeriesPoint.class }),
		configuration = {
			@Property(name = SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_INPUT_FILE, description = "", defaultValue = "inputTS.csv"),
			@Property(name = SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_ENCODING, description = "", defaultValue = "UTF-8"),
			@Property(name = SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_TS_INTERVAL, description = "", defaultValue = "1"),
		})
public class SimpleTimeSeriesFileReader extends AbstractReaderPlugin {

	private static final Log LOG = LogFactory.getLog(SimpleTimeSeriesFileReader.class);

	/**
	 * The file containing the input data.
	 */
	public static final String CONFIG_PROPERTY_NAME_INPUT_FILE = "inputfile";

	/**
	 * Text encoding of the input file.
	 */
	public static final String CONFIG_PROPERTY_NAME_ENCODING = "encoding";

	/**
	 * Interval between each timestamp that is read.
	 * E.g. if the data contains measurements each 15 minutes, this should be 15.
	 */
	public static final String CONFIG_PROPERTY_NAME_TS_INTERVAL = "tsinterval";

	/**
	 * The timeseries points that have been read by the reader.
	 */
	public static final String OUTPUT_PORT_NAME_TS_POINTS = "tspoints";

	private final String inputFile;
	private final String encoding;
	private final int tsInterval;

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
		this.encoding = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_ENCODING);
		this.tsInterval = this.configuration.getIntProperty(CONFIG_PROPERTY_NAME_TS_INTERVAL);
		this.readInputFile(this.inputFile, this.encoding);
	}

	public void readInputFile(final String inputFile, final String encoding)
	{
		this.readInputFile(inputFile, encoding);
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
		config.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_ENCODING, this.encoding);
		config.setProperty(SimpleTimeSeriesFileReader.CONFIG_PROPERTY_NAME_TS_INTERVAL, String.valueOf(this.tsInterval));
		return config;
	}

	@Override
	public boolean read() {
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		boolean result = true;

		try {
			fis = new FileInputStream(this.inputFile);
			isr = new InputStreamReader(fis, this.encoding);
			br = new BufferedReader(isr);

			// TimeSeriesPoint<Long> tsp;
			NamedDoubleTimeSeriesPoint ndtsp;
			LOG.info("Starting to read the file \"" + this.inputFile + "\" using encoding: " + this.encoding);

			// As we only have the values, we use a continouous timestamp starting at 1
			long timestamp = 0;
			while (br.ready()) {
				final String readLine = br.readLine();
				Double tsValue;
				try {
					tsValue = Double.valueOf(readLine);
					// tsp = new TimeSeriesPoint<Long>(timestamp, tsValue);
					ndtsp = new NamedDoubleTimeSeriesPoint(timestamp, tsValue, "experiment");
					super.deliver(SimpleTimeSeriesFileReader.OUTPUT_PORT_NAME_TS_POINTS, ndtsp);
					timestamp = timestamp + this.tsInterval;

				} catch (final NumberFormatException nfe) {
					// The current entry seems to be erroneous, so we skip it.
					LOG.warn("Could not convert the line \"" + readLine + "\" to long. Skipping it.");
					break;
				}
			}
		} catch (final FileNotFoundException fnfe) {
			LOG.error("Could not find the given file " + this.inputFile, fnfe);
			result = false;
		} catch (final IOException ioe) {
			LOG.error(ioe.getMessage(), ioe);
			result = false;
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
				result = false;
			}
		}
		return result;
	}
}
