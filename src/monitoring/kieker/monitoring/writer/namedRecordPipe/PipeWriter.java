package kieker.monitoring.writer.namedRecordPipe;

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

import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.PropertyMap;
import kieker.monitoring.writer.IMonitoringLogWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public final class PipeWriter implements IMonitoringLogWriter {
	private static final Log log = LogFactory.getLog(PipeWriter.class);

	public static final String PROPERTY_PIPE_NAME = "pipeName";
	private volatile Pipe pipe;

	/**
	 * 
	 * @param pipeName
	 */
	// TODO: throw Exception on error (e.g., )?
	public PipeWriter(final String pipeName) {
		this.initPipe(pipeName);
	}

	/**
	 * Initializes the {@link #pipe} and and {@link #pipeName} fields.
	 * 
	 * @param pipeName
	 * @return true on success; false otherwise
	 */
	private boolean initPipe(final String pipeName) {
		this.pipe = Broker.getInstance().acquirePipe(pipeName);
		if (this.pipe == null) {
			PipeWriter.log.error("Failed to get pipe with name:" + pipeName);
			return false;
		}
		PipeWriter.log.info("Connected to pipe '" + pipeName + "'" + " (" + this.pipe + ")");
		return true;
	}

	@Override
	public boolean init(final String initString) throws IllegalArgumentException {
		final PropertyMap propertyMap = new PropertyMap(initString, "|", "="); // throws IllegalArgumentException
		final String pipeName = propertyMap.getProperty(PipeWriter.PROPERTY_PIPE_NAME);
		if ((pipeName == null) || (pipeName.isEmpty())) {
			PipeWriter.log.error("Invalid or missing pipeName value for property '" + PipeWriter.PROPERTY_PIPE_NAME + "'");
			throw new IllegalArgumentException("Invalid or missing pipeName value:" + pipeName);
		}
		return this.initPipe(pipeName);
	}

	@Override
	public void terminate() {
		this.pipe.close();
	}

	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		return this.pipe.writeMonitoringRecord(monitoringRecord);
	}

	@Override
	public String getInfoString() {
		final StringBuilder strB = new StringBuilder();
		strB.append("pipeName : ");
		strB.append(this.pipe.getName());
		return strB.toString();
	}
}
