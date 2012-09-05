/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.MappingFileWriter;

/**
 * @author Jan Waller
 */
public class BinaryFsWriterThread extends AbstractFsWriterThread {
	private static final Log LOG = LogFactory.getLog(BinaryFsWriterThread.class);

	private DataOutputStream out;

	public BinaryFsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile) {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile);
		this.fileExtension = ".bin";
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
		this.out.writeInt(this.monitoringController.getIdForString(monitoringRecord.getClass().getName()));
		this.out.writeLong(monitoringRecord.getLoggingTimestamp());
		// two steps
		// first check for conformity
		for (final Object recordField : monitoringRecord.toArray()) {
			if ((recordField instanceof String)
					|| (recordField instanceof Integer)
					|| (recordField instanceof Long)
					|| (recordField instanceof Float)
					|| (recordField instanceof Double)
					|| (recordField instanceof Byte)
					|| (recordField instanceof Short)
					|| (recordField instanceof Boolean)) {
				continue;
			} else if (recordField == null) {
				LOG.warn("Unable to write record with null value: " + monitoringRecord.getClass().getSimpleName());
				return; // skip record
			} else {
				LOG.warn("Unable to write record with recordField of type " + recordField.getClass());
				return; // skip record
			}
		}
		// second write it
		for (final Object recordField : monitoringRecord.toArray()) {
			if (recordField instanceof String) {
				this.out.writeInt(this.monitoringController.getIdForString((String) recordField));
			} else if (recordField instanceof Integer) {
				this.out.writeInt((Integer) recordField);
			} else if (recordField instanceof Long) {
				this.out.writeLong((Long) recordField);
			} else if (recordField instanceof Float) {
				this.out.writeFloat((Float) recordField);
			} else if (recordField instanceof Double) {
				this.out.writeDouble((Double) recordField);
			} else if (recordField instanceof Byte) {
				this.out.writeByte((Byte) recordField);
			} else if (recordField instanceof Short) {
				this.out.writeShort((Short) recordField);
			} else if (recordField instanceof Boolean) {
				this.out.writeBoolean((Boolean) recordField);
			} else if (recordField == null) {
				LOG.warn("Unable to write record with null value.");
			} else {
				LOG.warn("Unable to write record with recordField of type " + recordField.getClass());
				this.out.writeByte((byte) 0);
			}
		}
	}

	@Override
	protected void prepareFile() throws IOException {
		if (this.out != null) {
			this.out.close();
		}
		this.out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(this.getFilename())));
	}

	@Override
	protected void cleanup() {
		if (this.out != null) {
			try {
				this.out.close();
			} catch (final IOException ex) {
				LOG.error("Failed to close channel.", ex);
			}
		}
	}
}
