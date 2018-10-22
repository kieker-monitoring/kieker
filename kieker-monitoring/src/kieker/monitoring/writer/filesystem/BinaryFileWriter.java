/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.filesystem;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.BinaryValueSerializer;
import kieker.common.record.io.IValueSerializer;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;
import kieker.monitoring.writer.compression.ICompressionFilter;

/**
 * @author Jan Waller, Christian Wulf
 * @author Henning Schnoor - added XZ compression instead of zip (1.13)
 *
 * @since 1.9
 *
 * @deprecated since 1.14 remove in 1.15 replaced by new FileWriter API
 */
@Deprecated
@ReceiveUnfilteredConfiguration // required for using class KiekerLogFolder
public class BinaryFileWriter extends AbstractFileWriter<ByteBuffer, BinaryValueSerializer> {

	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryFileWriter.class);

	public BinaryFileWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected AbstractFileWriterPool<ByteBuffer> createFilePoolHandler(final String charsetName,
			final int maxEntriesInFile, final ICompressionFilter compressionFilter, final int maxAmountOfFiles,
			final int maxMegaBytesInFile) {
		this.setBuffer(ByteBuffer.allocateDirect(this.getBufferSize()));

		return new BinaryFileWriterPool(BinaryFileWriter.LOGGER, this.getLogFolder(), charsetName, maxEntriesInFile,
				compressionFilter, maxAmountOfFiles, maxMegaBytesInFile, this.getBuffer());
	}

	@Override
	protected IValueSerializer createSerializer() {
		return BinaryValueSerializer.create(this.getFileWriterPool().getBuffer(),
				this.getWriterRegistry());
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final AbstractPooledFileChannel<ByteBuffer> channel = this.getFileWriterPool().getFileWriter();

		final int recordId = super.registerMonitoringRecord(monitoringRecord);

		this.getFileWriterPool().requestBufferSpace(4 + 8 + monitoringRecord.getSize(), BinaryFileWriter.LOGGER);

		this.getBuffer().putInt(recordId);
		this.getBuffer().putLong(monitoringRecord.getLoggingTimestamp());

		monitoringRecord.serialize(this.getSerializer());

		if (this.isFlushLogFile()) {
			channel.flush(BinaryFileWriter.LOGGER);
		}
	}

}
