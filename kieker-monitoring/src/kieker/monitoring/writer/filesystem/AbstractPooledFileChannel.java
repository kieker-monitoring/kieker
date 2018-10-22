/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import java.nio.Buffer;

import org.slf4j.Logger;

/**
 * @param <T>
 *            buffer type
 *
 * @author Reiner Jung
 *
 * @since 1.14
 *
 * @deprecated since 1.14 remove in 1.15 replaced by new FileWriter API
 */
@Deprecated
public abstract class AbstractPooledFileChannel<T extends Buffer> {

	private T buffer;

	public AbstractPooledFileChannel(final T buffer) {
		this.buffer = buffer;
	}

	public abstract long getBytesWritten();

	public abstract void flush(Logger logger);

	/**
	 * Flushes the buffer and closes the channel afterwards.
	 */
	public abstract void close(Logger writerLogger);

	protected T getBuffer() {
		return this.buffer;
	}

	protected void setBuffer(final T buffer) {
		this.buffer = buffer;
	}

}
