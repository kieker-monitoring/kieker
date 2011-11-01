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

import java.util.Arrays;
import java.util.Collection;
import java.util.PriorityQueue;

import kieker.analysis.reader.AbstractMonitoringReader;
import kieker.analysis.util.PropertyMap;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.DummyMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

/**
 * Filesystem reader which reads from multiple directories simultaneously ordered by the logging timestamp.
 * TODO: check correct handling of errors!
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public class FSReader extends AbstractMonitoringReader implements IMonitoringRecordReceiver {

	/**
	 * Semicolon-separated list of directories
	 */
	public static final String PROP_NAME_INPUTDIRS = "inputDirs";
	public static final IMonitoringRecord EOF = new DummyMonitoringRecord();

	private static final Log LOG = LogFactory.getLog(FSReader.class);

	private String[] inputDirs;
	private final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType;
	private final PriorityQueue<IMonitoringRecord> recordQueue;
	private volatile boolean running = true;

	/**
	 * 
	 * @param inputDirs
	 * @param readOnlyRecordsOfType
	 *            select only records of this type; null selects all
	 */
	public FSReader(final String[] inputDirs, final Collection<Class<? extends IMonitoringRecord>> readOnlyRecordsOfType) {
		this.readOnlyRecordsOfType = readOnlyRecordsOfType;
		if (inputDirs != null) {
			this.inputDirs = Arrays.copyOf(inputDirs, inputDirs.length);
			this.recordQueue = new PriorityQueue<IMonitoringRecord>(inputDirs.length);
		} else {
			this.inputDirs = null;
			this.recordQueue = new PriorityQueue<IMonitoringRecord>();
		}
	}

	/**
	 * 
	 * @param inputDirs
	 */
	public FSReader(final String[] inputDirs) {
		this(inputDirs, null);
	}

	/**
	 * Default constructor used for construction by reflection.
	 */
	public FSReader() {
		this(null, null);
	}

	/**
	 * Initializes the reader based on the given key/value pair initString. For the key {@value #PROP_NAME_INPUTDIRS}, the method expects a list of input directories
	 * separated by semicolon.
	 * 
	 * Example: <code>inputDirs=dir0;...;dir1</code>
	 */
	@Override
	public boolean init(final String initString) {
		final PropertyMap propertyMap = new PropertyMap(initString, "|", "=");
		final String dirList = propertyMap.getProperty(FSReader.PROP_NAME_INPUTDIRS);
		if (dirList == null) {
			FSReader.LOG.error("Missing value for property " + FSReader.PROP_NAME_INPUTDIRS);
			return false;
		}
		this.inputDirs = dirList.split(";");
		return true;
	}

	@Override
	public void terminate() {
		FSReader.LOG.info("Shutting down reader.");
		this.running = false;
	}

	@Override
	public boolean read() {
		// start all reader
		for (final String inputDir : this.inputDirs) {
			new Thread(new FSDirectoryReader(inputDir, this, this.readOnlyRecordsOfType)).start(); // NOPMD (new in loop)
		}
		// consume incoming records
		int readingReaders = this.inputDirs.length;
		while (readingReaders > 0) {
			synchronized (this.recordQueue) { // with newMonitoringRecord()
				while (this.recordQueue.size() < readingReaders) {
					try {
						this.recordQueue.wait();
					} catch (final InterruptedException ex) {
						// ignore InterruptedException
					}
				}
			}
			final IMonitoringRecord record = this.recordQueue.remove();
			synchronized (record) { // with newMonitoringRecord()
				record.notifyAll();
			}
			if (record == FSReader.EOF) {
				readingReaders--;
			} else {
				this.deliverRecord(record);
			}
		}
		return true;
	}

	/**
	 * This method is called for each new record by each ReaderThread.
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		synchronized (record) { // with read()
			synchronized (this.recordQueue) { // with read()
				this.recordQueue.add(record);
				this.recordQueue.notifyAll();
			}
			try {
				record.wait();
			} catch (final InterruptedException ex) {
				// ignore InterruptedException
			}
		}
		return this.running;
	}
}
