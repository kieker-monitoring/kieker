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

package kieker.common.namedRecordPipe;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.IMonitoringRecord;

/**
 * This implementation represents a simple pipe that can be used by readers and writers to transfer monitoring records.
 *
 * @author Andre van Hoorn
 *
 * @since 1.3
 */
public final class Pipe {
	private static final Logger LOGGER = LoggerFactory.getLogger(Pipe.class);

	private final String name;
	private volatile IPipeReader pipeReader;
	private volatile boolean closed;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param name
	 *            The name of the pipe.
	 */
	public Pipe(final String name) {
		this.name = name;
	}

	/**
	 * Sets the pipe reader to a new value. The pipe reader will be informed about new records.
	 *
	 * @param pipeReader
	 *            The new pipe reader.
	 */
	public void setPipeReader(final IPipeReader pipeReader) {
		this.pipeReader = pipeReader;
		LOGGER.debug("PipeReader initialized");
	}

	/**
	 * Delivers the name of this pipe.
	 *
	 * @return The name of the pipe.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Passes the monitoring record to the registered pipe reader.
	 *
	 * @param monitoringRecord
	 *            The monitoring record to write into the pipe.
	 *
	 * @return true on success; false otherwise.
	 */
	public boolean writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		if (this.closed) {
			LOGGER.error("trying to write to closed pipe");
			return false;
		}
		if (this.pipeReader == null) {
			LOGGER.error("pipeReader is null, i.e., no pipe reader has been registered.");
			return false;
		}

		return this.pipeReader.newMonitoringRecord(monitoringRecord);
	}

	/**
	 * This method closes the pipe and notifies the pipe reader about this event as well.
	 */
	public void close() {
		this.closed = true;
		if (this.pipeReader != null) {
			this.pipeReader.notifyPipeClosed();
		}
	}
}
