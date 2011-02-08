package kieker.monitoring.writer.filesystem;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.IMonitoringController;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.util.async.AbstractAsyncThread;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
public final class AsyncFsWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(AsyncFsWriter.class);

	private static final String PREFIX = "kieker.monitoring.writer.filesystem.AsyncFsWriter.";
	private static final String PATH = PREFIX + "customStoragePath";
	private static final String TEMP = PREFIX + "storeInJavaIoTmpdir";
	private static final String QUEUESIZE = PREFIX + "QueueSize";
	private static final String BEHAVIOR = PREFIX + "QueueFullBehavior";

	// internal variables
	private final FsWriterThread worker;
	private final BlockingQueue<IMonitoringRecord> blockingQueue;
	private final int queueFullBehavior;

	public AsyncFsWriter(IMonitoringController ctrl, Configuration configuration) {
		super(ctrl, configuration);
		String path;
		if (this.configuration.getBooleanProperty(TEMP)) {
			path = System.getProperty("java.io.tmpdir");
		} else {
			path = this.configuration.getStringProperty(PATH);
		}
		File f = new File(path);
		if (!f.isDirectory()) {
			AsyncFsWriter.log.error("'" + path + "' is not a directory.");
			throw new IllegalArgumentException("'" + path + "' is not a directory.");
		}
		final String ctrlName = this.ctrl.getHostName() + "-" + this.ctrl.getName();

		final DateFormat m_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
		m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = m_ISO8601UTC.format(new java.util.Date());
		path = path + File.separatorChar + "tpmon-" + dateStr + "-UTC-" + ctrlName + File.separatorChar;
		f = new File(path);
		if (!f.mkdir()) {
			AsyncFsWriter.log.error("Failed to create directory '" + path + "'");
			throw new IllegalArgumentException("Failed to create directory '" + path + "'");
		}

		final String mappingFileFn = path + File.separatorChar + "tpmon.map";
		final MappingFileWriter mappingFileWriter;
		try {
			mappingFileWriter = new MappingFileWriter(mappingFileFn);
		} catch (final Exception ex) {
			AsyncFsWriter.log.error("Failed to create mapping file '" + mappingFileFn + "'", ex);
			throw new IllegalArgumentException("Failed to create mapping file '" + mappingFileFn + "'", ex);
		}

		final int queueFullBehavior = this.configuration.getIntProperty(BEHAVIOR);
		if ((queueFullBehavior < 0) || (queueFullBehavior > 2)) {
			AsyncFsWriter.log.warn("Unknown value '" + queueFullBehavior + "' for " + BEHAVIOR + "; using default value 0");
			this.queueFullBehavior = 0;
		} else {
			this.queueFullBehavior = queueFullBehavior;
		}

		this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(this.configuration.getIntProperty(QUEUESIZE));
		this.worker = new FsWriterThread(this.ctrl, this.blockingQueue, mappingFileWriter, path + File.separatorChar + "tpmon");
		this.worker.setDaemon(true); // might lead to inconsistent data due to harsh shutdown
		this.worker.start();
	}

	@Override
	public void terminate() {
		this.worker.initShutdown();
		while (!this.worker.isFinished()) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ex) {
				// we should be able to ignore an interrupted sleep.
			}
			AsyncFsWriter.log.info("shutdown delayed - Worker is busy ... waiting additional 0.5 seconds");
		}
		AsyncFsWriter.log.info("Writer: AsyncFsWriter shutdown complete");
	}

	/**
	 * This method is not synchronized!
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			switch (this.queueFullBehavior) {
			case 1: // blocks when queue full
				this.blockingQueue.put(monitoringRecord);
				break;
			case 2: // does nothing if queue is full
				this.blockingQueue.offer(monitoringRecord);
				break;
			default: // tries to add immediately (error if full)
				this.blockingQueue.add(monitoringRecord);
				break;
			}
		} catch (final Exception ex) {
			AsyncFsWriter.log.error(" insertMonitoringData() failed: Exception: " + ex);
			return false;
		}
		return true;
	}
}

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
final class FsWriterThread extends AbstractAsyncThread {

	// configuration parameters
	private static final int maxEntriesInFile = 25000;

	// internal variables
	private final String filenamePrefix;
	private final MappingFileWriter mappingFileWriter;
	private PrintWriter pos = null;
	private int entriesInCurrentFileCounter = maxEntriesInFile + 1; // Force to initialize first file!

	public FsWriterThread(final IMonitoringController ctrl, final BlockingQueue<IMonitoringRecord> writeQueue, final MappingFileWriter mappingFileWriter, final String filenamePrefix) {
		super(ctrl, writeQueue);
		this.filenamePrefix = filenamePrefix;
		this.mappingFileWriter = mappingFileWriter;
	}

	// TODO: keep track of record type ID mapping!
	/**
	 * Note that it's not necessary to synchronize this method since
	 * a file is written at most by one thread.
	 * 
	 * @throws java.io.IOException
	 */
	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws IOException {
		final Object[] recordFields = monitoringRecord.toArray();
		final int LAST_FIELD_INDEX = recordFields.length - 1;
		// check if file exists and is not full
		this.prepareFile(); // may throw FileNotFoundException

		this.pos.write("$");
		this.pos.write(Integer.toString((this.mappingFileWriter.idForRecordTypeClass(monitoringRecord.getClass()))));
		this.pos.write(';');
		this.pos.write(Long.toString(monitoringRecord.getLoggingTimestamp()));
		if (LAST_FIELD_INDEX > 0) {
			this.pos.write(';');
		}
		for (int i = 0; i <= LAST_FIELD_INDEX; i++) {
			final Object val = recordFields[i];
			// TODO: assert that val!=null and provide suitable log msg if null
			this.pos.write(val.toString());
			if (i < LAST_FIELD_INDEX) {
				this.pos.write(';');
			}
		}
		this.pos.println();
		this.pos.flush();
	}

	/**
	 * Determines and sets a new Filename
	 */
	private final void prepareFile() throws FileNotFoundException {
		if (this.entriesInCurrentFileCounter++ > FsWriterThread.maxEntriesInFile) {
			if (this.pos != null) {
				this.pos.close();
			}
			this.entriesInCurrentFileCounter = 0;

			final DateFormat m_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
			m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			final String dateStr = m_ISO8601UTC.format(new java.util.Date());
			// TODO: where does this number come from?
			// final int random = (new Random()).nextInt(100);
			final String filename = this.filenamePrefix + "-" + dateStr + "-UTC-" + this.getName() + ".dat";
			// log.debug("** " + java.util.Calendar.getInstance().currentTimeNanos().toString() + " new filename: " + filename);
			this.pos = new PrintWriter(new DataOutputStream(new BufferedOutputStream(new FileOutputStream(filename))));
			this.pos.flush();
		}
	}
}
