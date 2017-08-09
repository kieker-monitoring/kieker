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

package kieker.monitoring.writer.dump;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

/**
 * A writer that does nothing but consuming records.
 *
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
public class DumpWriter extends AbstractMonitoringWriter {

	private static final Log LOG = LogFactory.getLog(DumpWriter.class);

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public DumpWriter(final Configuration configuration) {
		super(configuration);
		// configuration is not used
	}

	@Override
	public void onStarting() {
		LOG.info(this.getClass().getName() + " has been started.");
	}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		// consumes the record without further processing
	}

	@Override
	public void onTerminating() {
		LOG.info(this.getClass().getName() + " has shut down.");
	}
}
