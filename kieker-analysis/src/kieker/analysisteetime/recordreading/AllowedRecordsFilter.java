/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.recordreading;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.IFlowRecord;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.Counter;
import teetime.stage.InstanceOfFilter;

/**
 * @author Sören Henning
 */
public final class AllowedRecordsFilter extends CompositeStage {

	private final InstanceOfFilter<IMonitoringRecord, IFlowRecord> instanceOfFilter = new InstanceOfFilter<>(IFlowRecord.class);
	private final Counter<IFlowRecord> processedRecordsCounter = new Counter<>();
	private final Counter<IMonitoringRecord> ignoredRecordsCounter = new Counter<>();

	public AllowedRecordsFilter() {
		this.connectPorts(this.instanceOfFilter.getMatchedOutputPort(), this.processedRecordsCounter.getInputPort());
		this.connectPorts(this.instanceOfFilter.getMismatchedOutputPort(), this.ignoredRecordsCounter.getInputPort());

	}

	public InputPort<IMonitoringRecord> getInputPort() {
		return this.instanceOfFilter.getInputPort();
	}

	public OutputPort<IFlowRecord> getOutputPort() {
		return this.processedRecordsCounter.getOutputPort();
	}

	public int getProcessedRecords() {
		return this.processedRecordsCounter.getNumElementsPassed();
	}

	public int getIgnoredRecords() {
		return this.ignoredRecordsCounter.getNumElementsPassed();
	}

}
