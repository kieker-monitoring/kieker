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
package kieker.analysis.generic.source.time;

import java.util.concurrent.TimeUnit;

import kieker.common.record.misc.TimestampRecord;

import teetime.framework.AbstractProducerStage;
import teetime.framework.OutputPort;

/**
 * This plugin provides the current (system) time in regular intervals. The time is delivered to the two output ports as both a timestamp and a
 * {@link TimestampRecord} instance.<br>
 * <br>
 *
 * The reader can be configured to emit an arbitrary amount of signals. It can also be configured to emit an infinite amount of signals.<br>
 * <br>
 *
 * The sent timestamps are created using {@link System#nanoTime()} as a time source, which is being converted to the global time unit.
 *
 * Note: This stage is only provided for porting purposes. It is highly recommended to use the @{link TimeSourceStage} instead.
 *
 * @author Nils Christian Ehmke
 * @author Reiner Jung -- teetime port
 *
 * @since 1.8
 */
public class TimeReaderStage extends AbstractProducerStage<Long> {

	private final OutputPort<TimestampRecord> timestampsRecordOutputPort = this.createOutputPort(TimestampRecord.class);

	private final long delay;

	private final TimeUnit timeunit = TimeUnit.NANOSECONDS;
	private long numberOfImpulses;
	private final boolean infinite;

	public TimeReaderStage(final long delay, final Long numberOfImpulses) {
		super();
		this.delay = delay;
		this.numberOfImpulses = numberOfImpulses;
		this.infinite = numberOfImpulses == null;
	}

	@Override
	protected void execute() throws Exception {
		while (this.repeatEvent()) {
			final long timestamp = this.timeunit.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
			this.outputPort.send(timestamp);
			this.timestampsRecordOutputPort.send(new TimestampRecord(timestamp));
			Thread.sleep(this.delay / 1000 / 1000);
			this.numberOfImpulses--;
		}
		this.workCompleted();
	}

	private boolean repeatEvent() {
		if (this.infinite) {
			return true;
		} else {
			return this.numberOfImpulses > 0;
		}
	}

	public OutputPort<TimestampRecord> getTimestampsRecordOutputPort() {
		return this.timestampsRecordOutputPort;
	}

}
