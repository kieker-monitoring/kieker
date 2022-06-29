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
package kieker.analysis.architecture.recovery.data;

import java.time.Duration;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.Tuple;

/**
 * Contains a tuple of DeployedOperation for the execution model and a duration value.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationCallDurationEvent {

	private final Tuple<DeployedOperation, DeployedOperation> operationCall;
	private final Duration duration;

	public OperationCallDurationEvent(final Tuple<DeployedOperation, DeployedOperation> operationCall, final Duration duration) {
		this.operationCall = operationCall;
		this.duration = duration;
	}

	public Tuple<DeployedOperation, DeployedOperation> getOperationCall() {
		return this.operationCall;
	}

	public Duration getDuration() {
		return this.duration;
	}

}
