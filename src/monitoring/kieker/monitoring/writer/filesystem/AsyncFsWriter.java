package kieker.monitoring.writer.filesystem;

import java.awt.Stroke;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.IMonitoringWriter;

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
public final class AsyncFsWriter implements IMonitoringWriter {
	private static final Log log = LogFactory.getLog(AsyncFsWriter.class);

	// internal variables
	private final FsWriterThread worker;
	private final String storagePathBaseDir;
	private final String storagePathPostfix;
	private final int asyncRecordQueueSize;
	private final boolean blockOnFullQueue;
	private final BlockingQueue<IMonitoringRecord> blockingQueue;
	private final String storageDir; // full path
	private final static String defaultConstructionErrorMsg = 
		"Do not select this writer using the full-qualified classname. "
			+ "Use the the constant "
			+ ConfigurationConstants.WRITER_ASYNCFS
			+ " and the file system specific configuration properties.";

	public AsyncFsWriter() {
		throw new UnsupportedOperationException(AsyncFsWriter.defaultConstructionErrorMsg);
	}

	@Override
	public final boolean init(final String initString) {
		throw new UnsupportedOperationException(AsyncFsWriter.defaultConstructionErrorMsg);
	}

	@Override
	public void terminate() {
		worker.initShutdown();
		while (!worker.isFinished()) {
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
	 * 
	 * @param storagePathBaseDir
	 * @param storagePathPostfix
	 * @param asyncRecordQueueSize
	 * @param blockOnFullQueue
	 * @throws IOException 
	 */
	public AsyncFsWriter(final String storagePathBaseDir, final String storagePathPostfix, final int asyncRecordQueueSize, final boolean blockOnFullQueue) throws IOException {
		this.storagePathBaseDir = storagePathBaseDir;
		this.storagePathPostfix = storagePathPostfix;
		this.asyncRecordQueueSize = asyncRecordQueueSize;
		this.blockOnFullQueue = blockOnFullQueue;

		File f = new File(this.storagePathBaseDir);
		if (!f.isDirectory()) {
			AsyncFsWriter.log.error("Will abort init().");
			throw new IOException(this.storagePathBaseDir + " is not a directory");
		}

		final DateFormat m_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
		m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = m_ISO8601UTC.format(new java.util.Date());
		this.storageDir = this.storagePathBaseDir + "/tpmon-" + dateStr + "-UTC-" + this.storagePathPostfix + "/";
		f = new File(this.storageDir);
		if (!f.mkdir()) {
			AsyncFsWriter.log.error("Will abort init().");
			throw new IOException("Failed to create directory '" + this.storagePathBaseDir + "'");
		}
		AsyncFsWriter.log.debug("Directory for monitoring data: " + this.storageDir);

		this.blockingQueue = new ArrayBlockingQueue<IMonitoringRecord>(asyncRecordQueueSize);
		final String mappingFileFn = this.storageDir + File.separatorChar + "tpmon.map";
		worker = new FsWriterThread(this.blockingQueue, new MappingFileWriter(mappingFileFn), this.storageDir + "/tpmon");
		worker.setDaemon(true); // might lead to inconsistent data due to harsh shutdown
		worker.start();
	}

	/**
	 * This method is not synchronized.
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			if (this.blockOnFullQueue) {
				this.blockingQueue.offer(monitoringRecord); // blocks when queue full
			} else {
				this.blockingQueue.add(monitoringRecord); // tries to add immediately!
			}
		} catch (final Exception ex) {
			AsyncFsWriter.log.error(" insertMonitoringData() failed: Exception: " + ex);
			return false;
		}
		return true;
	}

	//TODO: needed?
	@Deprecated
	public final String getFilenamePrefix() {
		return this.storagePathBaseDir;
	}

	@Override
	public String getInfoString() {
		return "filenamePrefix :" + this.storagePathBaseDir + ", outputDirectory :" + this.storageDir
				+ ", asyncRecordQueueSize: " + this.asyncRecordQueueSize + ", blockOnFullQueue: " + this.blockOnFullQueue;
	}

}
