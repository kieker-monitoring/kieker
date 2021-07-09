package kieker.analysis.stage.model.data;

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
