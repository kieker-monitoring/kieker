/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.configuration.Configuration;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 *
 * @author Andre van Hoorn, Jan Waller, Robert von Massow
 *
 * @since 1.3
 */
public final class PipeWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = PipeWriter.class.getName() + ".";
	public static final String CONFIG_PIPENAME = PREFIX + "pipeName"; // NOCS (afterPREFIX)
	// private static final Log LOG = LogFactory.getLog(PipeWriter.class);

	private final Pipe pipe;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration used to initialize the pipe writer.
	 */
	public PipeWriter(final Configuration configuration) {
		super(configuration);
		final String pipeName = configuration.getStringProperty(CONFIG_PIPENAME);
		if (pipeName.length() == 0) {
			throw new IllegalArgumentException("Invalid or missing value for property '" + CONFIG_PIPENAME + "': '" + pipeName + "'");
		}
		this.pipe = Broker.INSTANCE.acquirePipe(pipeName);
	}

	/**
	 * Creates a new instance using an empty configuration.
	 */
	public PipeWriter(final String pipeName) {
		super(new Configuration());
		this.pipe = Broker.INSTANCE.acquirePipe(pipeName);
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(64);
		sb.append(super.toString()).append("\n\tConnected to pipe: '").append(this.pipe.getName()).append('\'');
		return sb.toString();
	}

	@Override
	public void onStarting() {
		// nothing to do
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		this.pipe.writeMonitoringRecord(record);
	}

	@Override
	public void onTerminating() {
		this.pipe.close();
	}
}
