/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.code;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.OperationCallEvent;

import teetime.stage.basic.AbstractTransformation;

/**
 * This filter processes @{link OperationCallEvent} events and transforms them to mini traces which
 * are send out.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class ProduceBeforeAndAfterEventsFromOperationCallsStage
		extends AbstractTransformation<IMonitoringRecord, IFlowRecord> {

	private long traceId = 1;
	private long now;
	private final String hostname;

	public ProduceBeforeAndAfterEventsFromOperationCallsStage(final String hostname) {
		this.hostname = hostname;
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		if (element instanceof OperationCallEvent) {
			final OperationCallEvent operation = (OperationCallEvent) element;
			this.outputPort.send(new TraceMetadata(this.traceId++, 0, "", this.hostname, 0, 0));
			this.outputPort.send(new BeforeOperationEvent(this.now++, this.traceId, 0, operation.getSourceOperation(),
					operation.getSourceComponent()));
			this.outputPort.send(new BeforeOperationEvent(this.now++, this.traceId, 1, operation.getTargetOperation(),
					operation.getTargetComponent()));
			this.outputPort.send(new AfterOperationEvent(this.now++, this.traceId, 2, operation.getTargetOperation(),
					operation.getTargetComponent()));
			this.outputPort.send(new AfterOperationEvent(this.now++, this.traceId, 3, operation.getSourceOperation(),
					operation.getSourceComponent()));
		} else if (element instanceof IFlowRecord) {
			this.outputPort.send((IFlowRecord) element);
		}
	}

}
