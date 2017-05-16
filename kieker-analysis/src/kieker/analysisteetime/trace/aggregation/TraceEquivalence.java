package kieker.analysisteetime.trace.aggregation;

import java.util.Objects;

import com.google.common.base.Equivalence;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;

/**
 * Wrapper class for a {@link Trace} to provide adequate {@code equals()} and {@code hashCode()}
 * methods.
 *
 * With this class, two {@link Traces}s are considered equal iff both the root {@link OperationCall} are
 * considered equal using the {@link OperationCallEquivalence}.
 *
 * @author Sören Henning
 *
 */
// TODO Java Doc
public class TraceEquivalence extends Equivalence<Trace> {

	private final OperationCallEquivalence operationCallEquivalence;

	public TraceEquivalence(final boolean considerFailed) {
		this.operationCallEquivalence = new OperationCallEquivalence(considerFailed);
	}

	public TraceEquivalence(final OperationCallEquivalence operationCallEquivalence) {
		this.operationCallEquivalence = operationCallEquivalence;
	}

	@Override
	protected boolean doEquivalent(final Trace traceA, final Trace traceB) {
		return this.operationCallEquivalence.equivalent(traceA.getRootOperationCall(), traceB.getRootOperationCall());
	}

	@Override
	protected int doHash(final Trace trace) {
		return Objects.hash(trace);
	}

}
