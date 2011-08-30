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

package bookstoreApplication;

import java.util.Properties;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

public class MyPipeWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = MyPipeWriter.class.getName() + ".";
	private static final String PROPERTY_PIPE_NAME = MyPipeWriter.PREFIX
			+ "pipeName";
	private volatile String pipeName;
	private volatile MyPipe pipe;

	public MyPipeWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
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
	protected Properties getDefaultProperties() {
		final Properties properties = new Properties(super.getDefaultProperties());
		properties.setProperty(MyPipeWriter.PROPERTY_PIPE_NAME, "myPipeName");
		return properties;
	}
	
	@Override
	protected void init() throws Exception {
		this.pipeName =
				this.configuration
						.getStringProperty(MyPipeWriter.PROPERTY_PIPE_NAME);
		this.pipe = MyNamedPipeManager.getInstance().acquirePipe(this.pipeName);
	}

	@Override
	public void terminate() { }
}
