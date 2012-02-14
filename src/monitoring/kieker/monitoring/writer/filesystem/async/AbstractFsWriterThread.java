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

package kieker.monitoring.writer.filesystem.async;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.internal.HashRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.filesystem.MappingFileWriter;

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
public abstract class AbstractFsWriterThread extends AbstractAsyncThread {

	protected String fileExtension = ".dat";

	// internal variables
	private final MappingFileWriter mappingFileWriter;
	private final String filenamePrefix;
	private final String path;
	private final int maxEntriesInFile;
	private int entriesInCurrentFileCounter;

	public AbstractFsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile) {
		super(monitoringController, writeQueue);
		this.mappingFileWriter = mappingFileWriter;
		this.path = new File(path).getAbsolutePath();
		this.filenamePrefix = path + File.separatorChar + "kieker";
		this.maxEntriesInFile = maxEntriesInFile;
		// Force to initialize first file!
		this.entriesInCurrentFileCounter = maxEntriesInFile;
	}

	protected final String getFilename() {
		final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String threadName = this.getName();
		final StringBuilder sb = new StringBuilder(this.filenamePrefix.length() + threadName.length() + this.fileExtension.length() + 28); // NOCS (MagicNumber)
		sb.append(this.filenamePrefix).append('-').append(dateFormat.format(new java.util.Date())).append("-UTC-").append(threadName).append(this.fileExtension); // NOPMD
		return sb.toString();
	}

	protected abstract void write(IMonitoringRecord monitoringRecord) throws IOException;

	protected abstract void prepareFile() throws IOException;

	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws Exception {
		if (monitoringRecord instanceof HashRecord) {
			this.mappingFileWriter.write((HashRecord) monitoringRecord);
		} else {
			if (++this.entriesInCurrentFileCounter > this.maxEntriesInFile) {
				this.entriesInCurrentFileCounter = 1;
				this.prepareFile();
			}
			this.write(monitoringRecord);
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("; Writing to Directory: '");
		sb.append(this.path);
		sb.append('\'');
		return sb.toString();
	}
}
