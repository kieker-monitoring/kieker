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
import java.util.Collection;
import java.util.Collections;
import java.util.Properties;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.plugin.port.AbstractInputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.common.record.IMonitoringRecord;

/**
 * This class has exactly one input port named "in" and one output ports named
 * "out". An instance of this class receives only objects implementing the
 * interface {@link IMonitoringRecord} via "in", prints a simple message on the
 * output stream that they received the objects and delegates the object to the
 * output port.<br>
 * In other words: The plugin prints a message for every record and passes it
 * unmodified to the output.
 * 
 * @author Matthias Rohr, Jan Waller
 */
public final class DummyRecordConsumer extends AbstractAnalysisPlugin {
	public static final String CONFIG_STREAM = DummyRecordConsumer.class.getName() + ".Stream";

	private static final Collection<Class<?>> OUT_CLASSES = Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class }));
	private static final Collection<Class<?>> IN_CLASSES = Collections.unmodifiableCollection(new CopyOnWriteArrayList<Class<?>>(
			new Class<?>[] { IMonitoringRecord.class }));

	private final PrintStream printStream;
	private final String printStreamName;
	private final OutputPort output = new OutputPort("out", DummyRecordConsumer.OUT_CLASSES);
	private final AbstractInputPort input = new AbstractInputPort("in", DummyRecordConsumer.IN_CLASSES) {
		@Override
		public void newEvent(final Object event) {
			// TODO: very bad style: escaping this in constructor! this should be assigned in execute()
			DummyRecordConsumer.this.newMonitoringRecord((IMonitoringRecord) event);
			DummyRecordConsumer.this.output.deliver(event);
		}
	};

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

		/* Register the ports. */
		super.registerInputPort("in", this.input);
		super.registerOutputPort("out", this.output);
	}

	@Override
	protected final Properties getDefaultProperties() {
		final Properties defaultProperties = new Properties();
		defaultProperties.setProperty(DummyRecordConsumer.CONFIG_STREAM, "STDOUT");
		return defaultProperties;
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		this.printStream.println("DummyRecordConsumer consumed (" + monitoringRecord.getClass().getSimpleName() + ") " + monitoringRecord);
		return true;
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
