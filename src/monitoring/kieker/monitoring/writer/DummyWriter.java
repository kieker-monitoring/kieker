/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;

/**
 * A writer that does nothing but consuming records.
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public class DummyWriter extends AbstractMonitoringWriter {
	private static final Log LOG = LogFactory.getLog(DummyWriter.class);

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this writer.
	 */
	public DummyWriter(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * This method consumes the record but does nothing with it.
	 * 
	 * @param record
	 *            The record to consume.
	 * 
	 * @return True.
	 */
	@Override
	public boolean newMonitoringRecord(final IMonitoringRecord record) {
		return true; // we don't care about incoming records
	}

	/**
	 * Terminates the writer.
	 */
	@Override
	public void terminate() {
		LOG.info(this.getClass().getName() + " shutting down");
	}

	@Override
	public void init() {
		// nothing to do
	}
}
