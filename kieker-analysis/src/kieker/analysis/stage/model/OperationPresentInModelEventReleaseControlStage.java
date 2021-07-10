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
package kieker.analysis.stage.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;

import teetime.stage.basic.AbstractFilter;

/**
 * Check for every incoming event whether the necessary model elements, here operations, exist.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationPresentInModelEventReleaseControlStage extends AbstractFilter<IFlowRecord> {

	private final DeploymentModel deploymentModel;
	private final List<AbstractOperationEvent> events = new ArrayList<>();
	private final Map<Long, TraceMetadata> traceMetadataMap = new ConcurrentHashMap<>();
	private final Map<Long, Long> traceOrderIndexMap = new ConcurrentHashMap<>();

	public OperationPresentInModelEventReleaseControlStage(final DeploymentModel deploymentModel) {
		this.deploymentModel = deploymentModel;
	}

	@Override
	protected void execute(final IFlowRecord element) throws Exception {
		if (element instanceof AbstractOperationEvent) {
			this.storeEvent((AbstractOperationEvent) element);
			this.sendEvent();
		} else {
			if (element instanceof TraceMetadata) {
				final TraceMetadata metadata = (TraceMetadata) element;
				this.traceMetadataMap.put(metadata.getTraceId(), metadata);
				this.traceOrderIndexMap.put(metadata.getTraceId(), 0L);
			}
			this.sendEvent();
			this.outputPort.send(element);
		}
	}

	private void sendEvent() {
		if (this.events.size() > 0) {
			final AbstractOperationEvent event = this.events.get(0);
			if (this.operationExists(event)) {
				this.outputPort.send(event);
				this.events.remove(0);
				this.sendEvent();
				if (this.traceOrderIndexMap.get(event.getTraceId()) == 0) {
					this.traceMetadataMap.remove(event.getTraceId());
				}
			}
		}
	}

	private boolean operationExists(final AbstractOperationEvent event) {
		final TraceMetadata metadata = this.traceMetadataMap.get(event.getTraceId());
		this.logger.info("event {} -- {} {}", this.traceOrderIndexMap.get(event.getTraceId()), event.toString(), metadata.toString());
		this.logger.info("deployments {}", this.deploymentModel.getDeploymentContexts().size());
		final DeploymentContext context = this.deploymentModel.getDeploymentContexts().get(metadata.getHostname());
		if (context != null) {
			final DeployedComponent component = context.getComponents().get(event.getClassSignature());
			if (component != null) {
				final DeployedOperation operation = component.getContainedOperations().get(event.getOperationSignature());
				return operation != null;
			}
		}
		return false;
	}

	private void storeEvent(final AbstractOperationEvent element) {
		if (element instanceof BeforeOperationEvent) {
			final Long value = this.traceOrderIndexMap.get(element.getTraceId()) + 1;
			this.traceOrderIndexMap.put(element.getTraceId(), value);
		} else if (element instanceof AfterOperationEvent) {
			final Long value = this.traceOrderIndexMap.get(element.getTraceId()) - 1;
			this.traceOrderIndexMap.put(element.getTraceId(), value);
		}
		this.events.add(element);
	}

}
