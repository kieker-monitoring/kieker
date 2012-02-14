/***************************************************************************
 * Copyright 2011 by
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

package kieker.analysis.plugin;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * This class has exactly one input port and one output port. An instance of this class receives only objects implementing the interface {@link IMonitoringRecord},
 * prints a simple message on the output stream that it received the object and delegates the object unmodified to the output port.
 * 
 * @author Matthias Rohr, Jan Waller
 */
@Plugin(outputPorts = {
	@OutputPort(
			name = TeeFilter.OUTPUT_PORT_NAME,
			description = "Output port",
			eventTypes = {})
})
public final class TeeFilter extends AbstractAnalysisPlugin {

	private static final Log LOG = LogFactory.getLog(TeeFilter.class);

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String INPUT_PORT_NAME = "newMonitoringRecord";
	public static final String CONFIG_STREAM = TeeFilter.class.getName() + ".Stream";

	public static final String CONFIG_STREAM_STDOUT = "STDOUT";
	public static final String CONFIG_STREAM_STDERR = "STDERR";
	public static final String CONFIG_STREAM_STDLOG = "STDLOG";

	private static final String ENCODING = "UTF-8";

	private final PrintStream printStream;
	private final String printStreamName;

	public TeeFilter(final Configuration configuration) throws FileNotFoundException, UnsupportedEncodingException {
		super(configuration);

		/* Get the name of the stream. */
		final String printStreamName = this.configuration.getStringProperty(TeeFilter.CONFIG_STREAM);

		/* Decide which stream to be used - but remember the name! */
		if (TeeFilter.CONFIG_STREAM_STDLOG.equals(printStreamName)) {
			this.printStream = null;
			this.printStreamName = null;
		} else if (TeeFilter.CONFIG_STREAM_STDOUT.equals(printStreamName)) {
			this.printStream = System.out;
			this.printStreamName = null;
		} else if (TeeFilter.CONFIG_STREAM_STDERR.equals(printStreamName)) {
			this.printStream = System.err;
			this.printStreamName = null;
		} else {
			this.printStream = new PrintStream(new FileOutputStream(printStreamName), false, TeeFilter.ENCODING);
			this.printStreamName = printStreamName;
		}
	}

	@InputPort(
			name = TeeFilter.INPUT_PORT_NAME,
			description = "Input port",
			eventTypes = {})
	public final void newMonitoringRecord(final Object monitoringRecord) {
		final StringBuilder sb = new StringBuilder(128);
		sb.append('(').append(monitoringRecord.getClass().getSimpleName()).append(") ").append(monitoringRecord.toString());
		final String record = sb.toString();
		if (this.printStream != null) {
			this.printStream.println(record);
		} else {
			TeeFilter.LOG.info(record);
		}
		super.deliver(TeeFilter.OUTPUT_PORT_NAME, monitoringRecord);
	}

	@Override
	public final void terminate(final boolean error) {
		if ((this.printStream != null) && (this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration(null);
		defaultConfiguration.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDOUT);
		return defaultConfiguration;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);
		/* We reverse the if-decisions within the constructor. */
		if (this.printStream == null) {
			configuration.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDLOG);
		} else if (this.printStream == System.out) {
			configuration.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDOUT);
		} else if (this.printStream == System.err) {
			configuration.setProperty(TeeFilter.CONFIG_STREAM, TeeFilter.CONFIG_STREAM_STDERR);
		} else {
			configuration.setProperty(TeeFilter.CONFIG_STREAM, this.printStreamName);
		}
		return configuration;
	}
}
