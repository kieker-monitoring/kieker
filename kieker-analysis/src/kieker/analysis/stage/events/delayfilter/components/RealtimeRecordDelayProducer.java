/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.stage.events.delayfilter.components;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * Gets records from a queue, calculates the delay for each records and forwards the records after the delay. As this stage extends {@link AbstractProducerStage} it
 * is always declared as active.
 *
 * @author Andre van Hoorn, Robert von Massow, Jan Waller, Lars Bluemke
 *
 * @since 1.13
 */
public class RealtimeRecordDelayProducer extends AbstractProducerStage<IMonitoringRecord> {

	public static final double ACCELERATION_FACTOR_DEFAULT = 1;

	private final LinkedBlockingQueue<Object> recordQueue;
	private final Object endToken;

	private final TimeUnit timeunit;
	private final TimerWithPrecision timer;
	private final double accelerationFactor;
	private long negativeDelayWarningBound;

	private volatile long startTime = -1;
	private volatile long firstLoggingTimestamp;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param recordQueue
	 *            Queue to pass records from {@link RealtimeRecordDelayConsumer} to {@link RealtimeRecordDelayProducer}.
	 * @param endToken
	 *            Simple Object to indicate that no more records are received and the stage can terminate.
	 * @param timeunit
	 *            The time unit to be used.
	 * @param accelerationFactor
	 *            Determines the replay speed.
	 */
	public RealtimeRecordDelayProducer(final LinkedBlockingQueue<Object> recordQueue, final Object endToken, final TimeUnit timeunit,
			final double accelerationFactor) {

		this.recordQueue = recordQueue;
		this.endToken = endToken;
		this.timeunit = timeunit;

		TimerWithPrecision tmpTimer;
		try {
			tmpTimer = TimerWithPrecision.valueOf(this.timeunit.toString());
		} catch (final IllegalArgumentException ex) {
			this.logger.warn(this.timeunit.toString() + " is no valid timer precision! Using MILLISECONDS instead.");
			tmpTimer = TimerWithPrecision.MILLISECONDS;
		}
		this.timer = tmpTimer;

		if (accelerationFactor <= 0.0) {
			this.logger.warn("Acceleration factor must be > 0. Using default: " + ACCELERATION_FACTOR_DEFAULT);
			this.accelerationFactor = 1;
		} else {
			this.accelerationFactor = accelerationFactor;
		}

		this.negativeDelayWarningBound = this.timeunit.convert(2, TimeUnit.SECONDS); // default 2 seconds
	}

	@Override
	protected void execute() {
		try {
			final Object element = this.recordQueue.take();

			if (element == this.endToken) {
				this.terminateStage();
			} else if (element instanceof IMonitoringRecord) {
				final IMonitoringRecord monitoringRecord = (IMonitoringRecord) element;

				final long currentTime = this.timer.getCurrentTime(this.timeunit);

				if (this.startTime == -1) { // init on first record
					this.firstLoggingTimestamp = monitoringRecord.getLoggingTimestamp();
					this.startTime = currentTime;
				}

				// Compute scheduling time
				long schedTimeFromNow = (long) (((monitoringRecord.getLoggingTimestamp() - this.firstLoggingTimestamp) // relative to 1st record
						/ this.accelerationFactor) // accelerate / slow down
						- (currentTime - this.startTime)); // subtract elapsed time

				if (schedTimeFromNow < -this.negativeDelayWarningBound) {
					final long schedTimeSeconds = TimeUnit.SECONDS.convert(schedTimeFromNow, this.timeunit);
					this.logger.warn("negative scheduling time: " + schedTimeFromNow + " (" + this.timeunit.toString() + ") / " + schedTimeSeconds
							+ " (seconds)-> scheduling with a delay of 0");
				}

				if (schedTimeFromNow < 0) {
					schedTimeFromNow = 0; // i.e., schedule immediately
				}

				Thread.sleep(TimeUnit.MILLISECONDS.convert(schedTimeFromNow, this.timeunit));

				this.outputPort.send(monitoringRecord);
			}

		} catch (final InterruptedException e) {
			this.logger.warn("Interrupted while waiting for next record.");
		}

	}

	/**
	 * Returns the time bound for which a warning is displayed when the computed delay falls below -(time bound).
	 *
	 * @return negativeDelayWarningBound
	 */
	public long getNegativeDelayWarningBound() {
		return this.negativeDelayWarningBound;
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
		this.negativeDelayWarningBound = this.timeunit.convert(negativeDelay, unit);
	}

	/**
	 * @author Jan Waller
	 */
	private static enum TimerWithPrecision {
		MILLISECONDS {
			@Override
			public long getCurrentTime(final TimeUnit timeunit) {
				return timeunit.convert(System.currentTimeMillis(), TimeUnit.MILLISECONDS);
			}

		},
		NANOSECONDS {
			@Override
			public long getCurrentTime(final TimeUnit timeunit) {
				return timeunit.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
			}
		};

		public abstract long getCurrentTime(TimeUnit timeunit);
	}
}
