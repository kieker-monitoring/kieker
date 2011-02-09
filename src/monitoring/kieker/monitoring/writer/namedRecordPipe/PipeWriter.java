package kieker.monitoring.writer.namedRecordPipe;

import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.IWriterController;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
/**
 * 
 * @author Andre van Hoorn
 */
public final class PipeWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(PipeWriter.class);

	private static final String PREFIX = PipeWriter.class.getName() + ".";
	private static final String PIPENAME = PREFIX + "pipeName";
	private final Pipe pipe;

	public PipeWriter(IWriterController ctrl, Configuration configuration) {
		super(ctrl, configuration);
		final String pipeName = this.configuration.getStringProperty(PIPENAME);
		if (pipeName.isEmpty()) {
			PipeWriter.log.error("Invalid or missing value for property '" + PIPENAME + "': '" + pipeName + "'");
			throw new IllegalArgumentException("Invalid or missing value for property '" + PIPENAME + "': '" + pipeName + "'");
		}
		this.pipe = Broker.getInstance().acquirePipe(pipeName);
		if (this.pipe == null) {
			PipeWriter.log.error("Failed to get pipe with name:" + pipeName);
			throw new IllegalArgumentException("Failed to get pipe with name:" + pipeName);
		}
	}

	@Override
	public final void terminate() {
		this.pipe.close();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		return this.pipe.writeMonitoringRecord(monitoringRecord);
	}

	@Override
	public String getInfoString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getInfoString());
		sb.append("\n\tConnected to pipe: '");
		sb.append(this.pipe.getName());
		sb.append("'");
		return sb.toString();
	}
}
