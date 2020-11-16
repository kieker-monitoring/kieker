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
package kieker.analysis.stage;

import teetime.framework.OutputPort;

/**
 * Match the events by implementing interface.
 *
 * @author Reiner Jung
 *
 * @param <T>
 *            match class
 * @since 1.15
 */
public class ImplementsEventMatcher<T> implements IEventMatcher<T> {

	private final Class<T> type;
	private OutputPort<T> outputPort;
	private IEventMatcher<? extends Object> successor;

	/**
	 * Create an event matcher to match events for a specific interface.
	 *
	 * @param type
	 *            the class type to be matched
	 * @param successor
	 *            next matcher to use in case this one does not match
	 */
	public ImplementsEventMatcher(final Class<T> type, final IEventMatcher<? extends Object> successor) {
		this.type = type;
		this.successor = successor;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.iobserve.stages.general.IEventMatcher#match(kieker.common.record.IMonitoringRecord)
	 */
	@Override
	public <R extends Object> boolean matchEvent(final R event) {
		return this.type.isAssignableFrom(event.getClass());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.iobserve.stages.general.IEventMatcher#getOutputPort()
	 */
	@Override
	public OutputPort<T> getOutputPort() {
		return this.outputPort;
	}

	@Override
	public void setOutputPort(final OutputPort<T> outputPort) {
		this.outputPort = outputPort;
	}

	@Override
	public IEventMatcher<? extends Object> getNextMatcher() {
		return this.successor;
	}

	@Override
	public void setNextMatcher(final IEventMatcher<? extends Object> leaveEventMatcher) {
		this.successor = leaveEventMatcher;
	}

}
