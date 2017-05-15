package kieker.analysisteetime.trace.aggregation;

import java.util.Objects;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;

/**
 * Wrapper class for a {@link Trace} to provide adequate {@code equals()} and {@code hashCode()}
 * methods.
 *
 * With this class, two {@link Traces}s are considered equal iff both the root {@link OperationCall} are
 * considered equal using the {@link OperationCallEqualityWrapper}.
 *
 * @author Sören Henning
 *
 */
public class TraceEqualityWrapper {

	private final Trace trace;
	private final boolean considerFailed;

	public TraceEqualityWrapper(final Trace trace, final boolean considerFailed) {
		this.trace = trace;
		this.considerFailed = considerFailed;
	}

	public Trace getTrace() {
		return this.trace;
	}

	@Override
	public int hashCode() {
		return Objects.hash(new OperationCallEqualityWrapper(this.trace.getRootOperationCall(), this.considerFailed));
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof TraceEqualityWrapper) {
			final TraceEqualityWrapper other = (TraceEqualityWrapper) obj;
			return Objects.equals(new OperationCallEqualityWrapper(this.trace.getRootOperationCall(), this.considerFailed),
					new OperationCallEqualityWrapper(other.trace.getRootOperationCall(), this.considerFailed));
		} else {
			return false;
		}
	}

}
