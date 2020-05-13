/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.filter.record.delayfilter;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import kieker.analysisteetime.plugin.filter.record.delayfilter.components.RealtimeRecordDelayConsumer;
import kieker.analysisteetime.plugin.filter.record.delayfilter.components.RealtimeRecordDelayProducer;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

/**
 * Forwards incoming {@link IMonitoringRecord}s with delays computed from the {@link kieker.common.record.IMonitoringRecord#getLoggingTimestamp()} value
 * (assumed to be in the configured resolution). For example, after initialization, if records with logging timestamps 3000 and 4500 nanos are received, the
 * first record is forwarded immediately; the second will be forwarded 1500 nanos later. The acceleration factor can be used to accelerate/slow down the
 * replay (default 1.0, which means no acceleration/slow down).<br>
 * The implementation with TeeTime consists of two components: At first a consumer stage (declared as passive) which receives incoming records and
 * stores them in a queue. At second a producer stage (always active) which takes the records from the queue and forwards them with the proper delay.
 * <br>
 * <b>Note:</b> We assume that the incoming records are ordered by their timestamps. The stage does not provide an ordering mechanism. Records that are received
 * "too late" are forwarded with a delay of 0 but stay "too late".
 *
 * @author Andre van Hoorn, Robert von Massow, Jan Waller, Lars Bluemke
 *
 * @since 1.6
 */
public class RealtimeRecordDelayFilter extends CompositeStage {

	private static final Object END_TOKEN = new Object();

	private final InputPort<IMonitoringRecord> inputPort;
	private final OutputPort<IMonitoringRecord> outputPort;

	private final RealtimeRecordDelayConsumer consumer; // NOPMD (could be replaced by a local variable)
	private final RealtimeRecordDelayProducer producer;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timeunit
	 *            The time unit to be used.
	 * @param accelerationFactor
	 *            Determines the replay speed.
	 */
	public RealtimeRecordDelayFilter(final TimeUnit timeunit, final double accelerationFactor) {

		final LinkedBlockingQueue<Object> recordQueue = new LinkedBlockingQueue<>();
		this.consumer = new RealtimeRecordDelayConsumer(recordQueue, END_TOKEN);
		this.producer = new RealtimeRecordDelayProducer(recordQueue, END_TOKEN, timeunit, accelerationFactor);

		this.inputPort = this.consumer.getInputPort();
		this.outputPort = this.producer.getOutputPort();
	}

	/**
	 * Returns the time bound for which a warning is displayed when the computed delay falls below -(time bound).
	 *
	 * @return negativeDelayWarningBound
	 */
	public long getNegativeDelayWarningBound() {
		return this.producer.getNegativeDelayWarningBound();
	}

	/**
	 * Sets the time bound for which a warning is displayed when the computed delay falls below -(time bound).
	 *
	 * @param negativeDelay
	 *            The chosen time bound.
	 * @param unit
	 *            Time unit of the chosen time bound.
	 */
	public void setNegativeDelayWarningBound(final long negativeDelay, final TimeUnit unit) {
		this.producer.setNegativeDelayWarningBound(negativeDelay, unit);
	}

	public InputPort<IMonitoringRecord> getInputPort() {
		return this.inputPort;
	}

	public OutputPort<IMonitoringRecord> getOutputPort() {
		return this.outputPort;
	}
}
