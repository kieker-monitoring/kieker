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

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

public class MyPipeWriter extends AbstractMonitoringWriter {

	public static final String CONFIG_PROPERTY_NAME_PIPE_NAME = MyPipeWriter.class.getName() + ".pipeName";

	private static final Log LOG = LogFactory.getLog(MyPipeWriter.class);

	private volatile MyPipe pipe;
	private final String pipeName;

	public MyPipeWriter(final Configuration configuration) {
		super(configuration);

		this.pipeName = configuration.getStringProperty(MyPipeWriter.CONFIG_PROPERTY_NAME_PIPE_NAME);
	}

	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		try {
			/* Just write the content of the record into the pipe. */
			this.pipe.put(new PipeData(record.getLoggingTimestamp(), record.toArray()));
		} catch (final InterruptedException e) {
			return false; // signal error
		}
		return true;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());

		configuration.setProperty(MyPipeWriter.CONFIG_PROPERTY_NAME_PIPE_NAME, "kieker-pipe");

		return configuration;
	}

	@Override
	protected void init() throws Exception {
		try {
			this.pipe = MyNamedPipeManager.getInstance().acquirePipe(this.pipeName);
		} catch (final Exception ex) {
			MyPipeWriter.LOG.error("Failed to acquire pipe '" + this.pipeName + "'", ex);
			throw ex;
		}
	}

	public void terminate() {
		// nothing to do
	}
}
