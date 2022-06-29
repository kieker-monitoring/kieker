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
package kieker.analysis.architecture.trace;

import java.util.Set;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Allows to filter Traces about their traceIds.
 *
 * This class has exactly one input port and two output ports. If the received object
 * contains the defined traceID, the object is delivered unmodified to the
 * matchingTraceIdOutputPort otherwise to the mismatchingTraceIdOutputPort.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke, Reiner Jung
 * @since 1.15
 */
public class TraceIdFilter extends AbstractConsumerStage<IMonitoringRecord> {

	private final boolean acceptAllTraces;
	private final Set<Long> selectedTraceIds;

	private final OutputPort<IMonitoringRecord> matchingTraceIdOutputPort = this.createOutputPort();
	private final OutputPort<IMonitoringRecord> mismatchingTraceIdOutputPort = this.createOutputPort();

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param acceptAllTraces
	 *            Determining whether to accept all traces, regardless of the given trace IDs.
	 * @param selectedTraceIds
	 *            Determining which trace IDs should be accepted by this filter.
	 */
	public TraceIdFilter(final boolean acceptAllTraces, final Set<Long> selectedTraceIds) {
		this.acceptAllTraces = acceptAllTraces;
		this.selectedTraceIds = selectedTraceIds;
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminatiing {}", this.getClass().getCanonicalName());
		super.onTerminating();
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		if (element instanceof OperationExecutionRecord) {
			this.process((OperationExecutionRecord) element);
		} else if (element instanceof ITraceRecord) {
			this.process((ITraceRecord) element);
		} else if (element instanceof TraceMetadata) {
			this.process((TraceMetadata) element);
		}
	}

	private void process(final TraceMetadata element) {
		if (this.acceptId(element.getTraceId())) {
			this.matchingTraceIdOutputPort.send(element);
		} else {
			this.mismatchingTraceIdOutputPort.send(element);
		}
	}

	private void process(final ITraceRecord element) {
		if (this.acceptId(element.getTraceId())) {
			this.matchingTraceIdOutputPort.send(element);
		} else {
			this.mismatchingTraceIdOutputPort.send(element);
		}
	}

	private void process(final OperationExecutionRecord element) {
		if (this.acceptId(element.getTraceId())) {
			this.matchingTraceIdOutputPort.send(element);
		} else {
			this.mismatchingTraceIdOutputPort.send(element);
		}
	}

	private final boolean acceptId(final long traceId) {
		return this.acceptAllTraces || this.selectedTraceIds.contains(traceId);
	}

	/** Returns the output port delivering the records with matching IDs. */
	public OutputPort<IMonitoringRecord> getMatchingTraceIdOutputPort() {
		return this.matchingTraceIdOutputPort;
	}

	/** Returns the output port delivering the records with the non matching IDs. */
	public OutputPort<IMonitoringRecord> getMismatchingTraceIdOutputPort() {
		return this.mismatchingTraceIdOutputPort;
	}

}
