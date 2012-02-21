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

package kieker.examples.userguide.ch3and4bookstore;

import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

@Plugin(outputPorts = {
	@OutputPort(eventTypes = { MyResponseTimeRecord.class }, name = MyPipeReader.OUTPUT_PORT_NAME)
})
public class MyPipeReader extends AbstractReaderPlugin {

	public static final String OUTPUT_PORT_NAME = "outputPort";
	private static final Log log = LogFactory.getLog(MyPipeReader.class);

	private static final String PROPERTY_PIPE_NAME = MyPipeReader.class + ".pipeName";
	private final String pipeName;
	private volatile MyPipe pipe;

	public MyPipeReader() {
		super(new Configuration());
		this.pipeName = "kieker-pipe";
		this.init();
	}

	public MyPipeReader(final Configuration configuration) {
		super(configuration);

		this.pipeName = configuration.getStringProperty(MyPipeReader.PROPERTY_PIPE_NAME);

		this.init();
	}

	public MyPipeReader(final String pipeName) {
		super(new Configuration());

		this.pipeName = pipeName;

		this.init();
	}

	public boolean init() {
		try {
			this.pipe = MyNamedPipeManager.getInstance().acquirePipe(this.pipeName);
		} catch (final Exception exc) {
			MyPipeReader.log.error("Failed to acquire pipe '" + this.pipeName
					+ "': " + exc.getMessage());
			return false;
		}
		return true;
	}

	@Override
	public boolean read() {
		try {
			PipeData data;
			/* Wait max. 4 seconds for the next data. */
			while ((data = this.pipe.poll(4)) != null) {
				/* Create new record, init from received array ... */
				final MyResponseTimeRecord record = new MyResponseTimeRecord();
				record.initFromArray(data.getRecordData());
				record.setLoggingTimestamp(data.getLoggingTimestamp());
				/* ...and delegate the task of delivering to the super class. */
				super.deliver(MyPipeReader.OUTPUT_PORT_NAME, record);
			}
		} catch (final InterruptedException e) {
			return false; // signal error
		}
		return true;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(MyPipeReader.PROPERTY_PIPE_NAME, this.pipeName);

		return configuration;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration defaultConfiguration = new Configuration();

		defaultConfiguration.setProperty(MyPipeReader.PROPERTY_PIPE_NAME, "kieker-pipe");

		return defaultConfiguration;
	}

	@Override
	public void terminate(final boolean error) {
		// nothing to do
	}
}
