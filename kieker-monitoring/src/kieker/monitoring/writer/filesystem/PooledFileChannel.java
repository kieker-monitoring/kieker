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
import java.nio.channels.WritableByteChannel;

import kieker.common.logging.Log;
import kieker.monitoring.writer.WriterUtil;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
class PooledFileChannel {

	private final WritableByteChannel channel;

	private long bytesWritten;

	public PooledFileChannel(final WritableByteChannel channel) {
		super();
		this.channel = channel;
	}

	public long getBytesWritten() {
		return this.bytesWritten;
	}

	public void flush(final ByteBuffer buffer, final Log log) {
		this.bytesWritten += WriterUtil.flushBuffer(buffer, this.channel, log);
	}

	/**
	 * Flushes the buffer and closes the channel afterwards.
	 */
	public void close(final ByteBuffer buffer, final Log log) {
		this.bytesWritten += WriterUtil.flushBuffer(buffer, this.channel, log);
		WriterUtil.close(this.channel, log);
	}
}
