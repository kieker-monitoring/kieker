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

package kieker.analysisteetime.plugin.filter.select.traceidfilter.components;

import java.util.Set;
import java.util.TreeSet;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Abstract class which describes internal components of {@link TraceIdFilter}. Each concrete component filters the trace ids of a specific record type T.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.2
 *
 * @param <T>
 *            Type parameter for specific record types.
 */
public abstract class AbstractTraceIdFilter<T> extends AbstractConsumerStage<T> {

	private final boolean acceptAllTraces;
	private final Set<Long> selectedTraceIds;

	private final OutputPort<T> matchingTraceIdOutputPort = this.createOutputPort();
	private final OutputPort<T> mismatchingTraceIdOutputPort = this.createOutputPort();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param acceptAllTraces
	 *            Determining whether to accept all traces, regardless of the given trace IDs.
	 * @param selectedTraceIds
	 *            Determining which trace IDs should be accepted by this filter.
	 */
	public AbstractTraceIdFilter(final boolean acceptAllTraces, final Long[] selectedTraceIds) {

		this.acceptAllTraces = acceptAllTraces;
		this.selectedTraceIds = new TreeSet<>();
		for (final Long id : selectedTraceIds) {
			this.selectedTraceIds.add(id);
		}
	}

	private final boolean acceptId(final long traceId) {
		return this.acceptAllTraces || this.selectedTraceIds.contains(traceId);
	}

	@Override
	protected void execute(final T record) {
		if (this.acceptId(this.getRecordsTraceId(record))) {
			this.matchingTraceIdOutputPort.send(record);
		} else {
			this.mismatchingTraceIdOutputPort.send(record);
		}
	}

	protected abstract long getRecordsTraceId(T record);

	/** Returns the output port delivering the records with matching IDs. */
	public OutputPort<T> getMatchingTraceIdOutputPort() {
		return this.matchingTraceIdOutputPort;
	}

	/** Returns the output port delivering the records with the non matching IDs. */
	public OutputPort<T> getMismatchingTraceIdOutputPort() {
		return this.mismatchingTraceIdOutputPort;
	}

}
