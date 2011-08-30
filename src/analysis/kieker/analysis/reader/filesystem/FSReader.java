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

package kieker.analysis.reader.filesystem;

import java.util.Collection;
import java.util.StringTokenizer;

import kieker.analysis.plugin.MonitoringRecordConsumerException;
import kieker.analysis.reader.AbstractMonitoringReader;
import kieker.analysis.util.PropertyMap;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Filesystem reader which reads from multiple directories simultaneously
 * ordered by the logging timestamp.
 * 
 * @author Andre van Hoorn
 */
public class FSReader extends AbstractMonitoringReader {

	private static final Log log = LogFactory.getLog(FSReader.class);
	/*
	 * Semicolon-separated list of directories
	 */
	public static final String PROP_NAME_INPUTDIRS = "inputDirs";
	private String[] inputDirs = null;

	public FSReader(final String[] inputDirs) {
		this(inputDirs, null);
	}

	private final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType;

	/**
	 * 
	 * @param inputDirs
	 * @param readOnlyRecordsOfType
	 *            select only records of this type; null selects all
	 */
	public FSReader(
			final String[] inputDirs,
			final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType) {
		this.inputDirs = inputDirs;
		this.readOnlyRecordsOfType = readOnlyRecordsOfType;
	}

	/** Default constructor used for construction by reflection. */
	public FSReader() {
		this.readOnlyRecordsOfType = null;
	}

	/**
	 * Acts as a consumer to the FSDirectoryReader and delegates incoming
	 * records to the {@link #delegator}
	 */
	private FSReaderCons concurrentConsumer;

	/**
	 * Receives records from the {@link #concurrentConsumer} and delegates them
	 * to the registered consumers via the
	 * {@link #deliverRecord(IMonitoringRecord)} method.
	 */
	private final IMonitoringRecordReceiver delegator = new IMonitoringRecordReceiver() {

		@Override
		public boolean newMonitoringRecord(final IMonitoringRecord record) {
			return FSReader.this.deliverRecord(record);
		}
	};

	@Override
	public boolean read() {
		this.concurrentConsumer = new FSReaderCons(this.delegator,
				this.inputDirs, this.readOnlyRecordsOfType);
		boolean success = false;
		try {
			success = this.concurrentConsumer.execute();
		} catch (final MonitoringRecordConsumerException ex) {
			FSReader.log.error("RecordConsumerExecutionException occured", ex);
			success = false;
		}
		return success;
	}

	/**
	 * Initializes the reader based on the given key/value pair initString. For
	 * the key {@value #PROP_NAME_INPUTDIRS}, the method expects a list of input
	 * directories separated by semicolon.
	 * <p>
	 * 
	 * Example: <code>inputDirs=dir0;...;dir1</code>
	 */
	@Override
	public boolean init(final String initString) {
		String dirList = null;
		try {
			/* throws IllegalArgumentException: */
			final PropertyMap propertyMap = new PropertyMap(initString, "|",
					"=");
			dirList = propertyMap.getProperty(FSReader.PROP_NAME_INPUTDIRS);

			if (dirList == null) {
				FSReader.log.error("Missing value for property "
						+ FSReader.PROP_NAME_INPUTDIRS);
				return false;
			} // parse inputDir property value

			final StringTokenizer dirNameTokenizer = new StringTokenizer(
					dirList, ";");
			this.inputDirs = new String[dirNameTokenizer.countTokens()];
			for (int i = 0; dirNameTokenizer.hasMoreTokens(); i++) {
				this.inputDirs[i] = dirNameTokenizer.nextToken().trim();
			}
		} catch (final Exception exc) {
			FSReader.log.error("Error parsing list of input directories'"
					+ dirList + "':" + exc.getMessage(), exc);
			return false;
		}
		return true;
	}
}