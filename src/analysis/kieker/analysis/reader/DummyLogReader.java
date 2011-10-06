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

import java.util.logging.Level;
import java.util.logging.Logger;

import kieker.common.record.OperationExecutionRecord;

/**
 * This is a logReader for testing. It creates just some random data. All have
 * the same operation and component and some random wait time of approximately a
 * second.
 * 
 * @author matthias
 */
public class DummyLogReader extends AbstractMonitoringReader {
	private volatile boolean initShutdown = false;

	/**
	 * Produces dummy data about once each second
	 */
	@Override
	public boolean read() {
		while (!this.initShutdown) {
			final long startTime = System.nanoTime();

			// wait a bit
			final long sleeptime = Math.round((Math.random() * 750d) + 250d);
			try {
				Thread.sleep(sleeptime);
			} catch (final InterruptedException ex) {
				Logger.getLogger(DummyLogReader.class.getName()).log(Level.SEVERE, null, ex);
			}

			final OperationExecutionRecord testRecord = new OperationExecutionRecord("ComponentA", "OperationA", 1, startTime, System.nanoTime());
			this.deliverRecord(testRecord);
		}
		return true;
	}

	@Override
	public boolean init(final String initString) throws IllegalArgumentException {
		// nothing to do...
		return true;
	}

	@Override
	public void terminate() {
		this.initShutdown = true;
	}
}
