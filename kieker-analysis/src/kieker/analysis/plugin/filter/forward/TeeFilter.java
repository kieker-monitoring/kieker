/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.filter.forward;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;

/**
 * This filter has exactly one input port and one output port.
 *
 * A simple message is printed to a configurable stream and all objects are forwarded to the output port.
 *
 * @author Matthias Rohr, Jan Waller
 *
 * @since 1.2
 */
@Plugin(description = "A filter to print the object to a configured stream", outputPorts = @OutputPort(name = TeeFilter.OUTPUT_PORT_NAME_RELAYED_EVENTS, description = "Provides each incoming object", eventTypes = {
	Object.class }), configuration = {
		@Property(name = TeeFilter.CONFIG_PROPERTY_NAME_STREAM, defaultValue = TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT, description = "The name of the stream used to print the incoming data (special values are STDOUT, STDERR, STDlog, and NULL; "
				+ "other values are interpreted as filenames)."),
		@Property(name = TeeFilter.CONFIG_PROPERTY_NAME_ENCODING, defaultValue = TeeFilter.CONFIG_PROPERTY_VALUE_DEFAULT_ENCODING, description = "The used encoding for the selected stream."),
		@Property(name = TeeFilter.CONFIG_PROPERTY_NAME_APPEND, defaultValue = TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_APPEND, description = "Decides whether the filter appends to the stream in the case of a file or not.")
	})
public final class TeeFilter extends AbstractFilterPlugin {

	/** The name of the input port for incoming events. */
	public static final String INPUT_PORT_NAME_EVENTS = "receivedEvents";
	/** The name of the output port delivering the incoming events. */
	public static final String OUTPUT_PORT_NAME_RELAYED_EVENTS = "relayedEvents";
	/** The name of the property determining the stream in which the incoming data will be printed. */
	public static final String CONFIG_PROPERTY_NAME_STREAM = "stream";
	/** The name of the property determining the used encoding. */
	public static final String CONFIG_PROPERTY_NAME_ENCODING = "characterEncoding";
	/** The name of the property determining whether or not the stream appends or overwrites to files. */
	public static final String CONFIG_PROPERTY_NAME_APPEND = "append";

	/** The value of the stream property which determines that the filter uses the standard output. */
	public static final String CONFIG_PROPERTY_VALUE_STREAM_STDOUT = "STDOUT";
	/** The value of the stream property which determines that the filter uses the standard error output. */
	public static final String CONFIG_PROPERTY_VALUE_STREAM_STDERR = "STDERR";
	/** The value of the stream property which determines that the filter uses the standard log. */
	public static final String CONFIG_PROPERTY_VALUE_STREAM_STDLOG = "STDlog";
	/** The value of the stream property which determines that the filter doesn't print anything. */
	public static final String CONFIG_PROPERTY_VALUE_STREAM_NULL = "NULL";
	/** The default value of the encoding property which determines that the filter uses utf-8. */
	public static final String CONFIG_PROPERTY_VALUE_DEFAULT_ENCODING = "UTF-8";
	/** The default value of the stream property which determines that the filter appends or overwrites a file. */
	public static final String CONFIG_PROPERTY_VALUE_STREAM_APPEND = "true";

	private final PrintStream printStream;
	private final String printStreamName;
	private final boolean active;
	private final boolean append;
	private final String encoding;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public TeeFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Get the name of the stream.
		final String printStreamNameConfig = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_STREAM);
		// Get the encoding.
		this.encoding = this.configuration.getStringProperty(CONFIG_PROPERTY_NAME_ENCODING);

		// Decide which stream to be used - but remember the name!
		if (CONFIG_PROPERTY_VALUE_STREAM_STDLOG.equals(printStreamNameConfig)) {
			this.printStream = null; // NOPMD (null)
			this.printStreamName = null; // NOPMD (null)
			this.active = true;
			this.append = false;
		} else if (CONFIG_PROPERTY_VALUE_STREAM_STDOUT.equals(printStreamNameConfig)) {
			this.printStream = System.out;
			this.printStreamName = null; // NOPMD (null)
			this.active = true;
			this.append = false;
		} else if (CONFIG_PROPERTY_VALUE_STREAM_STDERR.equals(printStreamNameConfig)) {
			this.printStream = System.err;
			this.printStreamName = null; // NOPMD (null)
			this.active = true;
			this.append = false;
		} else if (CONFIG_PROPERTY_VALUE_STREAM_NULL.equals(printStreamNameConfig)) {
			this.printStream = null; // NOPMD (null)
			this.printStreamName = null; // NOPMD (null)
			this.active = false;
			this.append = false;
		} else {
			this.append = configuration.getBooleanProperty(CONFIG_PROPERTY_NAME_APPEND);
			PrintStream tmpPrintStream;
			try {
				tmpPrintStream = new PrintStream(new FileOutputStream(printStreamNameConfig, this.append), false, this.encoding);
			} catch (final UnsupportedEncodingException ex) {
				this.OLDlogger.error("Failed to initialize " + printStreamNameConfig, ex);
				tmpPrintStream = null; // NOPMD (null)
			} catch (final FileNotFoundException ex) {
				this.OLDlogger.error("Failed to initialize " + printStreamNameConfig, ex);
				tmpPrintStream = null; // NOPMD (null)
			}
			this.printStream = tmpPrintStream;
			this.printStreamName = printStreamNameConfig;
			this.active = true;
		}
	}

	@Override
	public final void terminate(final boolean error) {
		if ((this.printStream != null) && (this.printStream != System.out) && (this.printStream != System.err)) {
			this.printStream.close();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_ENCODING, this.encoding);
		configuration.setProperty(CONFIG_PROPERTY_NAME_APPEND, Boolean.toString(this.append));
		// We reverse the if-decisions within the constructor.
		if (this.printStream == null) {
			if (this.active) {
				configuration.setProperty(CONFIG_PROPERTY_NAME_STREAM, CONFIG_PROPERTY_VALUE_STREAM_STDLOG);
			} else {
				configuration.setProperty(CONFIG_PROPERTY_NAME_STREAM, CONFIG_PROPERTY_VALUE_STREAM_NULL);
			}
		} else if (this.printStream == System.out) {
			configuration.setProperty(CONFIG_PROPERTY_NAME_STREAM, CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
		} else if (this.printStream == System.err) {
			configuration.setProperty(CONFIG_PROPERTY_NAME_STREAM, CONFIG_PROPERTY_VALUE_STREAM_STDERR);
		} else {
			configuration.setProperty(CONFIG_PROPERTY_NAME_STREAM, this.printStreamName);
		}
		return configuration;
	}

	/**
	 * This method is the input port of the filter receiving incoming objects. Every object will be printed into a stream (based on the configuration) before the
	 * filter sends it to the output port.
	 *
	 * @param object
	 *            The new object.
	 */
	@InputPort(name = INPUT_PORT_NAME_EVENTS, description = "Receives incoming objects to be logged and forwarded", eventTypes = { Object.class })
	public final void inputEvent(final Object object) {
		if (this.active) {
			final StringBuilder sb = new StringBuilder(128);
			sb.append(this.getName());
			sb.append('(').append(object.getClass().getSimpleName()).append(") ").append(object.toString());
			final String record = sb.toString();
			if (this.printStream != null) {
				this.printStream.println(record);
			} else {
				this.OLDlogger.info(record);
			}
		}
		super.deliver(OUTPUT_PORT_NAME_RELAYED_EVENTS, object);
	}
}
