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

import java.util.Iterator;
import java.util.Properties;

import kieker.analysis.configuration.Configuration;
import kieker.analysis.reader.AbstractReaderPlugin;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.configuration.AbstractConfiguration;
import kieker.common.record.IMonitoringRecord;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class MyPipeReader extends AbstractReaderPlugin {

	private static final Log log = LogFactory.getLog(MyPipeReader.class);

	private static final String PROPERTY_PIPE_NAME = MyPipeReader.class + ".pipeName";
	private static final String pipeName;
	private volatile MyPipe pipe;

	public MyPipeReader() {
		super(new Configuration(null));
	}

	public MyPipeReader(final Configuration configuration) {
		super(configuration);
		this.pipeName = "kieker-pipe";
	}

	public MyPipeReader(final String pipeName) {
		super(new Configuration(null));
		this.pipeName = pipeName;
		this.init(MyPipeReader.PROPERTY_PIPE_NAME + "=" + pipeName);
	}

	@Override
	public boolean init(final String initString) throws IllegalArgumentException {
		try {
			final PropertyMap propertyMap = new PropertyMap(initString, "|", "=");
			final String pipeName = propertyMap.getProperty(MyPipeReader.PROPERTY_PIPE_NAME);
			this.pipe = MyNamedPipeManager.getInstance().acquirePipe(pipeName);
		} catch (final Exception exc) {
			MyPipeReader.log.error("Failed to parse initString '" + initString
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
				this.deliverRecord(record);
			}
		} catch (final InterruptedException e) {
			return false; // signal error
		}
		return true;
	}

	@Override
	public void terminate() {
		// currently no termination code (could be refined)
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration(null);

		configuration.setProperty(PROPERTY_PIPE_NAME, "kieker-pipe");

		return configuration;
	}

	@Override
	protected Properties getDefaultProperties() {
		final Properties defaultProperties = new Properties();

		defaultProperties.setProperty(PROPERTY_PIPE_NAME, pipeName);

		return defaultProperties;
	}
}
