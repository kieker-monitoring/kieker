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
package kieker.analysis.source.time;

import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.common.record.misc.TimestampRecord;

import teetime.framework.AbstractProducerStage;

/**
 * This plugin provides the current (system) time in regular intervals. The time is delivered to the two output ports as both a timestamp and a
 * {@link TimestampRecord} instance.<br>
 * <br>
 *
 * The reader can be configured to emit an arbitrary amount of signals. It can also be configured to emit an infinite amount of signals.<br>
 * <br>
 *
 * The sent timestamps are created using {@link System#nanoTime()} as a time source, which is being converted to the global time unit (as defined in the
 * configuration from the given {@link IProjectContext}).
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class TimeSourceStage extends AbstractProducerStage<Long> {

	private final long delay;

	private final TimeUnit timeunit = TimeUnit.NANOSECONDS;
	private final long numberOfImpulses;
	private final boolean infinite;

	public TimeSourceStage(final long delay, final Long numberOfImpulses) {
		super();
		this.delay = delay;
		this.numberOfImpulses = (numberOfImpulses == null ? 0 : numberOfImpulses);
		this.infinite = numberOfImpulses == null;
	}

	@Override
	protected void execute() throws Exception {
		while (this.repeatEvent()) {
			final long timestamp = this.timeunit.convert(System.nanoTime(), TimeUnit.NANOSECONDS);
			this.outputPort.send(timestamp);
			Thread.sleep(this.delay / 1000 / 1000);
		}
		this.workCompleted();
	}

	private boolean repeatEvent() {
		if (this.shouldBeTerminated()) {
			return false;
		} else if (this.infinite) {
			return true;
		} else {
			return this.numberOfImpulses > 0;
		}
	}

}
