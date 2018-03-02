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

import java.nio.CharBuffer;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueSerializer;
import kieker.common.record.io.TextValueSerializer;
import kieker.monitoring.core.controller.ReceiveUnfilteredConfiguration;
import kieker.monitoring.writer.filesystem.compression.ICompressionFilter;

/**
 * @author Matthias Rohr, Robert von Massow, Andre van Hoorn, Jan Waller, Christian Wulf
 *
 * @since < 0.9
 *
 * @deprecated 1.14 should be removed in 1.15 replaced by new FileWriter API
 */
@Deprecated
@ReceiveUnfilteredConfiguration // required for using class KiekerLogFolder
public class AsciiFileWriter extends AbstractFileWriter<CharBuffer, TextValueSerializer> {

	private static final Log LOG = LogFactory.getLog(AsciiFileWriter.class);

	public AsciiFileWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected AbstractFileWriterPool<CharBuffer> createFilePoolHandler(final String charsetName, final int maxEntriesInFile,
			final ICompressionFilter compressionFilter,
			final int maxAmountOfFiles,
			final int maxMegaBytesInFile) {
		this.setBuffer(CharBuffer.allocate(this.getBufferSize()));

		return new AsciiFileWriterPool(LOG, this.getLogFolder(), charsetName, maxEntriesInFile,
				compressionFilter, maxAmountOfFiles, maxMegaBytesInFile, this.getBuffer());
	}

	@Override

	protected IValueSerializer createSerializer() {
		return TextValueSerializer.create(this.getBuffer());
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		final AbstractPooledFileChannel<CharBuffer> channel = this.getFileWriterPool().getFileWriter();

		final int recordId = super.registerMonitoringRecord(monitoringRecord);

		this.getBuffer().clear();

		final String header = String.format("$%d;%d", recordId, monitoringRecord.getLoggingTimestamp());

		this.getBuffer().put(header);

		monitoringRecord.serialize(this.getSerializer());

		// flush is necessary, as only flushed data is counted.
		// if (this.isFlushLogFile()) {
		channel.flush(LOG);
		// }
	}

}
