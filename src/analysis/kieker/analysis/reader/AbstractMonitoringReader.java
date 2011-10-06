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

package kieker.analysis.reader;

import java.util.Vector;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.IMonitoringRecordReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractMonitoringReader implements IMonitoringReader {

	private static final Log LOG = LogFactory.getLog(AbstractMonitoringReader.class);

	private final Vector<IMonitoringRecordReceiver> recordReceivers = new Vector<IMonitoringRecordReceiver>();

	@Override
	public final void addRecordReceiver(final IMonitoringRecordReceiver receiver) {
		this.recordReceivers.add(receiver);
	}

	/**
	 * Delivers the given record to the consumers that are registered for this
	 * type of records.
	 * 
	 * This method should be used by implementing classes.
	 * 
	 * @param monitoringRecord
	 *            the record
	 * @return true on success; false in case of an error.
	 * @throws LogReaderExecutionException
	 *             if an error occurs
	 */
	protected final boolean deliverRecord(final IMonitoringRecord record) {
		try {
			for (final IMonitoringRecordReceiver c : this.recordReceivers) {
				if (!c.newMonitoringRecord(record)) {
					AbstractMonitoringReader.LOG.error("Consumer returned with error");
					return false;
				}
			}
		} catch (final Exception ex) {
			AbstractMonitoringReader.LOG.fatal("Caught Exception while delivering record", ex);
			return false;
		}
		return true;
	}
}
