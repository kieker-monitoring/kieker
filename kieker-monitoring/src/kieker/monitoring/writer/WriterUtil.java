/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.writer;

import java.io.Closeable;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;

import kieker.common.logging.Log;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
public final class WriterUtil {

	private WriterUtil() {
		// utility class
	}

	/**
	 * @return the number of bytes written from the buffer to the channel
	 */
	public static long flushBuffer(final ByteBuffer buffer, final WritableByteChannel writableChannel, final Log log) {
		long bytesWritten = 0;

		buffer.flip();
		try {
			while (buffer.hasRemaining()) {
				bytesWritten += writableChannel.write(buffer);
			}
			buffer.clear();
		} catch (final IOException e) {
			log.error("Caught exception while writing to the channel.", e);
			WriterUtil.close(writableChannel, log);
		}

		return bytesWritten;
	}

	public static void close(final Closeable closeable, final Log log) {
		try {
			closeable.close();
		} catch (final IOException e) {
			if (log.isWarnEnabled()) {
				log.warn("Caught exception while closing '" + closeable.getClass() + "'.", e);
			}
		}
	}
}
