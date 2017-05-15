package kieker.analysisteetime.trace.aggregation;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;

/**
 * Wrapper class for a {@link OperationCall} to provide adequate {@code equals()} and {@code hashCode()}
 * methods.
 *
 * With this class, two {@link OperationCall}s are considered equal iff both belong to the same
 * {@link DeployedOperation} and - optionally - have the same failed cause and if their children
 * are also considered equal (in the same order).
 *
 * @author Sören Henning
 *
 */
public class OperationCallEqualityWrapper {

	private final OperationCall operationCall;
	private final boolean considerFailed;

	public OperationCallEqualityWrapper(final OperationCall operationCall, final boolean considerFailed) {
		this.operationCall = operationCall;
		this.considerFailed = considerFailed;
	}

	public OperationCall getOperationCall() {
		return this.operationCall;
	}

	@Override
	public int hashCode() {
		final OperationCall thiz = this.operationCall;
		final List<OperationCallEqualityWrapper> children = this.wrapChildren(thiz);
		if (this.considerFailed) {
			return Objects.hash(thiz.getOperation(), thiz.isFailed(), thiz.getFailedCause(), children);
		} else {
			return Objects.hash(thiz.getOperation(), children);
		}
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof OperationCallEqualityWrapper) {
			final OperationCall thiz = this.operationCall;
			final OperationCall other = ((OperationCallEqualityWrapper) obj).getOperationCall();
			final List<OperationCallEqualityWrapper> thisChildren = this.wrapChildren(thiz);
			final List<OperationCallEqualityWrapper> otherChildren = this.wrapChildren(other);
			if (this.considerFailed) {
				return Objects.equals(thiz.getOperation(), other.getOperation())
						&& thiz.isFailed() == other.isFailed()
						&& Objects.equals(thiz.getFailedCause(), other.getFailedCause())
						&& Objects.equals(thisChildren, otherChildren);
			} else {
				return Objects.equals(thiz.getOperation(), other.getOperation())
						&& Objects.equals(thisChildren, otherChildren);
			}
		} else {
			return false;
		}
	}

	private List<OperationCallEqualityWrapper> wrapChildren(final OperationCall operationCall) {
		return operationCall.getChildren().stream()
				.map(c -> new OperationCallEqualityWrapper(c, this.considerFailed))
				.collect(Collectors.toList());
	}

}
