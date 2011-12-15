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

import kieker.analysis.plugin.port.AInputPort;
import kieker.analysis.plugin.port.AOutputPort;
import kieker.analysis.plugin.port.APlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * This class has exactly one input port and one output port. An instance of this class receives only objects implementing the interface {@link IMonitoringRecord},
 * prints a simple message on the output stream that it received the object and delegates the object unmodified to the output port.
 * 
 * @author Matthias Rohr, Jan Waller
 */
@APlugin(outputPorts = {
	@AOutputPort(
			name = DummyRecordConsumer.OUTPUT_PORT_NAME,
			description = "Output port",
			eventTypes = { IMonitoringRecord.class })
})
public final class DummyRecordConsumer extends AbstractAnalysisPlugin {

	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	public static final String INPUT_PORT_NAME = "newMonitoringRecord";
	public static final String CONFIG_STREAM = DummyRecordConsumer.class.getName() + ".Stream";

	private final PrintStream printStream;
	private final String printStreamName;

	public DummyRecordConsumer(final Configuration configuration) throws FileNotFoundException {
		super(configuration);

		/* Get the name of the stream. */
		final String printStreamName = this.configuration.getStringProperty(DummyRecordConsumer.CONFIG_STREAM);

		/* Decide which stream to be used - but remember the name! */
		if ("STDOUT".equals(printStreamName)) {
			this.printStream = System.out;
			this.printStreamName = null;
		} else if ("STDERR".equals(printStreamName)) {
			this.printStream = System.err;
			this.printStreamName = null;
		} else {
			this.printStream = new PrintStream(new FileOutputStream(printStreamName));
			this.printStreamName = printStreamName;
		}
	}

	@Override
	protected final Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration(null);

		defaultConfiguration.setProperty(DummyRecordConsumer.CONFIG_STREAM, "STDOUT");

		return defaultConfiguration;
	}

	@AInputPort(
			description = "Input port",
			eventTypes = { IMonitoringRecord.class })
	public final void newMonitoringRecord(final Object monitoringRecord) {
		this.printStream.println("DummyRecordConsumer consumed (" + monitoringRecord.getClass().getSimpleName() + ") " + monitoringRecord);
		super.deliver(DummyRecordConsumer.OUTPUT_PORT_NAME, monitoringRecord);
	}

	@Override
	public final boolean execute() {
		this.printStream.println("DummyRecordConsumer.execute()");
		return true;
	}

	@Override
	public final void terminate(final boolean error) {
		if ((this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		/* We reverse the if-decisions within the constructor. */
		if (this.printStream == System.out) {
			configuration.setProperty(DummyRecordConsumer.CONFIG_STREAM, "STDOUT");
		} else if (this.printStream == System.err) {
			configuration.setProperty(DummyRecordConsumer.CONFIG_STREAM, "STDERR");
		} else {
			configuration.setProperty(DummyRecordConsumer.CONFIG_STREAM, this.printStreamName);
		}

		return configuration;
	}
}
