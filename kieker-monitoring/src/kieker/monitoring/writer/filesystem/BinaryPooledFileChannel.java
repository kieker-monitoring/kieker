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

import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import org.slf4j.Logger;

import kieker.monitoring.writer.WriterUtil;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 *
 * @deprecated 1.14 should be removed in 1.15 replaced by new FileWriter API
 */
@Deprecated
class BinaryPooledFileChannel extends AbstractPooledFileChannel<ByteBuffer> {

	private final WritableByteChannel channel;

	private long bytesWritten;

	public BinaryPooledFileChannel(final OutputStream outputStream, final ByteBuffer buffer) {
		super(buffer);
		this.channel = Channels.newChannel(outputStream);
	}

	@Override
	public long getBytesWritten() {
		return this.bytesWritten;
	}

	@Override
	public void flush(final Logger logger) {
		this.bytesWritten += WriterUtil.flushBuffer(this.getBuffer(), this.channel, logger);
	}

	@Override
	public void close(final Logger logger) {
		this.flush(logger);
		WriterUtil.close(this.channel, logger);
	}
}
