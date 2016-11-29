/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.reader;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * This abstract class defines the basic structure of each reader. Each reader has its reader logic separated
 * from the actual TeeTime stage. The reader logic is mainly based on the original Kieker stages.
 *
 * @author Lars Bluemke
 *
 * @since 1.13
 */
public abstract class AbstractReader extends AbstractProducerStage<IMonitoringRecord> {

	protected IReaderLogic readerLogic;

	@Override
	protected void execute() {
		this.readerLogic.read();
	}

	/** Terminates the reader logic by returning from read method. */
	public void terminate(final boolean error) {
		this.readerLogic.terminate(error);
		this.terminate();
	}

	/** Called from reader logic to send the read records to the output port */
	public void deliverRecord(final IMonitoringRecord monitoringRecord) {
		this.outputPort.send(monitoringRecord);
	}

}
