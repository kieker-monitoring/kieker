/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.filesystem.map.StringMappingFileWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.7
 */
public class BinaryZipWriterThread extends AbstractZipWriterThread {
	private static final Log LOG = LogFactory.getLog(BinaryZipWriterThread.class);

	private final DataOutputStream out;

	public BinaryZipWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final StringMappingFileWriter mappingFileWriter, final String path, final int maxEntriesInFile, final int bufferSize, final int level)
			throws IOException {
		super(monitoringController, writeQueue, mappingFileWriter, path, maxEntriesInFile, level);
		super.fileExtension = ".bin";
		this.out = new DataOutputStream(new BufferedOutputStream(super.zipOutputStream, bufferSize));
	}

	@Override
	protected void write(final IMonitoringRecord monitoringRecord) throws IOException {
		this.out.writeInt(this.monitoringController.getIdForString(monitoringRecord.getClass().getName()));
		this.out.writeLong(monitoringRecord.getLoggingTimestamp());
		final Object[] recordFields = monitoringRecord.toArray();
		for (int i = 0; i < recordFields.length; i++) {
			if (recordFields[i] == null) {
				final Class<?>[] recordTypes = monitoringRecord.getValueTypes();
				if (recordTypes[i] == String.class) {
					this.out.writeInt(this.monitoringController.getIdForString(""));
				} else if ((recordTypes[i] == int.class) || (recordTypes[i] == Integer.class)) {
					this.out.writeInt(0);
				} else if ((recordTypes[i] == long.class) || (recordTypes[i] == Long.class)) {
					this.out.writeLong(0L);
				} else if ((recordTypes[i] == float.class) || (recordTypes[i] == Float.class)) {
					this.out.writeFloat(0);
				} else if ((recordTypes[i] == double.class) || (recordTypes[i] == Double.class)) {
					this.out.writeDouble(0);
				} else if ((recordTypes[i] == byte.class) || (recordTypes[i] == Byte.class)) {
					this.out.writeByte(0);
				} else if ((recordTypes[i] == short.class) || (recordTypes[i] == Short.class)) {
					this.out.writeShort(0);
				} else if ((recordTypes[i] == boolean.class) || (recordTypes[i] == Boolean.class)) {
					this.out.writeBoolean(false);
				} else {
					LOG.warn("Record with unsupported recordField of type " + recordFields[i].getClass());
					this.out.writeByte((byte) 0);
				}
			} else if (recordFields[i] instanceof String) {
				this.out.writeInt(this.monitoringController.getIdForString((String) recordFields[i]));
			} else if (recordFields[i] instanceof Integer) {
				this.out.writeInt((Integer) recordFields[i]);
			} else if (recordFields[i] instanceof Long) {
				this.out.writeLong((Long) recordFields[i]);
			} else if (recordFields[i] instanceof Float) {
				this.out.writeFloat((Float) recordFields[i]);
			} else if (recordFields[i] instanceof Double) {
				this.out.writeDouble((Double) recordFields[i]);
			} else if (recordFields[i] instanceof Byte) {
				this.out.writeByte((Byte) recordFields[i]);
			} else if (recordFields[i] instanceof Short) {
				this.out.writeShort((Short) recordFields[i]);
			} else if (recordFields[i] instanceof Boolean) {
				this.out.writeBoolean((Boolean) recordFields[i]);
			} else {
				LOG.warn("Record with unsupported recordField of type " + recordFields[i].getClass());
				this.out.writeByte((byte) 0);
			}
		}
	}

	@Override
	protected void cleanupForNextEntry() throws IOException {
		this.out.flush();
	}

	@Override
	protected void cleanupFinal() throws IOException {
		this.out.close();
	}
}
