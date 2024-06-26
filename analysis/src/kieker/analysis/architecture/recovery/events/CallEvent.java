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
package kieker.analysis.architecture.recovery.events;

import java.time.Duration;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class CallEvent {

	private final OperationEvent caller;
	private final OperationEvent callee;
	private final Duration duration;

	public CallEvent(final OperationEvent caller, final OperationEvent callee, final Duration duration) {
		this.caller = caller;
		this.callee = callee;
		this.duration = duration;
	}

	public OperationEvent getCaller() {
		return this.caller;
	}

	public OperationEvent getCallee() {
		return this.callee;
	}

	public Duration getDuration() {
		return this.duration;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof CallEvent) {
			final CallEvent event = (CallEvent) obj;
			return this.getCaller().equals(event.getCaller()) && this.getCallee().equals(event.getCallee()) && this.duration.equals(event.getDuration());
		} else {
			return super.equals(obj);
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s -> %s (%d)", this.getCaller().toString(), this.getCallee().toString(), this.duration.getNano());
	}
}
