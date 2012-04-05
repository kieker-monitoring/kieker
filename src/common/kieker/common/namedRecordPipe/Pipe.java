/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Andre van Hoorn
 */
public final class Pipe {
	private static final Log LOG = LogFactory.getLog(Pipe.class);

	private final String name;
	private volatile IPipeReader pipeReader;
	private volatile boolean closed;

	public Pipe(final String name) {
		this.name = name;
	}

	public void setPipeReader(final IPipeReader pipeReader) {
		this.pipeReader = pipeReader;
		if (LOG.isDebugEnabled()) {
			LOG.debug("PipeReader initialized");
		}
	}

	public String getName() {
		return this.name;
	}

	/**
	 * Passe the monitoring record to the registered pipe reader.
	 * 
	 * @param monitoringRecord
	 */
	public boolean writeMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		if (this.closed) {
			LOG.error("trying to write to closed pipe");
			return false;
		}
		if (this.pipeReader == null) {
			LOG.error("pipeReader is null, i.e., no pipe reader has been registered.");
			return false;
		}

		return this.pipeReader.newMonitoringRecord(monitoringRecord);
	}

	public void close() {
		this.closed = true;
		if (this.pipeReader != null) {
			this.pipeReader.notifyPipeClosed();
		}
	}
}
