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

package kieker.monitoring.writernew;

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

	public WriterUtil() {
		// utility class
	}

	public static void flushBuffer(final ByteBuffer buffer, final WritableByteChannel writableChannel, final Log log) {
		buffer.flip();
		try {
			while (buffer.hasRemaining()) {
				writableChannel.write(buffer);
			}
			buffer.clear();
		} catch (final IOException e) {
			log.error("Error in writing to the channel.", e);
			WriterUtil.close(writableChannel, log);
		}
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
