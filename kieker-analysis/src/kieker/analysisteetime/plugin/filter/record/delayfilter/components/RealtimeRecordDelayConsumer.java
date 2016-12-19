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

package kieker.analysisteetime.plugin.filter.record.delayfilter.components;

import java.util.concurrent.LinkedBlockingQueue;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractConsumerStage;

/**
 * Receives records at the input port and stores them in a queue for the {@link RealtimeRecordDelayProducer}.
 *
 * @author Lars Bluemke
 * @since 1.13
 */
public class RealtimeRecordDelayConsumer extends AbstractConsumerStage<IMonitoringRecord> {

	private final LinkedBlockingQueue<Object> recordQueue;
	private final Object endToken;

	public RealtimeRecordDelayConsumer(final LinkedBlockingQueue<Object> recordQueue, final Object endToken) {
		this.recordQueue = recordQueue;
		this.endToken = endToken;
	}

	@Override
	protected void execute(final IMonitoringRecord monitoringRecord) {
		this.recordQueue.add(monitoringRecord);
	}

	@Override
	public void onTerminating() throws Exception {
		this.recordQueue.add(this.endToken);
		super.onTerminating();
	}

}
