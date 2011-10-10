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

package kieker.monitoring.writer.namedRecordPipe;

import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 */
public final class PipeWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = PipeWriter.class.getName() + ".";
	public static final String CONFIG_PIPENAME = PipeWriter.PREFIX + "pipeName"; // NOCS (afterPREFIX)
	private static final Log LOG = LogFactory.getLog(PipeWriter.class);

	private final Pipe pipe;

	public PipeWriter(final Configuration configuration) {
		super(configuration);
		final String pipeName = this.configuration.getStringProperty(PipeWriter.CONFIG_PIPENAME);
		if (pipeName.isEmpty()) {
			final String errorMsg = "Invalid or missing value for property '" + PipeWriter.CONFIG_PIPENAME + "': '" + pipeName + "'";
			PipeWriter.LOG.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
		this.pipe = Broker.getInstance().acquirePipe(pipeName);
	}

	@Override
	public final void terminate() {
		if (this.pipe != null) {
			this.pipe.close();
		}
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		return this.pipe.writeMonitoringRecord(monitoringRecord);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tConnected to pipe: '");
		sb.append(this.pipe.getName());
		sb.append("'");
		return sb.toString();
	}

	/**
	 * Nothing to do
	 */
	@Override
	protected void init() {
		// nothing to do
	}
}
