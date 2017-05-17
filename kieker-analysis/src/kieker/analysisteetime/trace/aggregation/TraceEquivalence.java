package kieker.analysisteetime.trace.aggregation;

import java.util.Objects;

import com.google.common.base.Equivalence;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;

/**
 * This class defines an equivalence for {@link Trace}s using the class {@link Equivalence}. Therefore,
 * it provides the ability to wrap {@link Trace}s with adequate {@code equals()} and {@code hashCode()}
 * methods using its {@link #wrap(Trace)} method.
 *
 * With this class, two {@link Traces}s are considered equal iff both the root {@link OperationCall} are
 * considered equal using the {@link OperationCallEquivalence} or a specified {@link Equivalence}.
 *
 * @author Sören Henning
 *
 */
public class TraceEquivalence extends Equivalence<Trace> {

	private final Equivalence<OperationCall> operationCallEquivalence;

	public TraceEquivalence(final boolean considerFailed) {
		this.operationCallEquivalence = new OperationCallEquivalence(considerFailed);
	}

	public TraceEquivalence(final Equivalence<OperationCall> operationCallEquivalence) {
		this.operationCallEquivalence = operationCallEquivalence;
	}

	@Override
	protected boolean doEquivalent(final Trace traceA, final Trace traceB) {
		// A and B are not the same object and are not nulls.
		return this.operationCallEquivalence.equivalent(traceA.getRootOperationCall(), traceB.getRootOperationCall());
	}

	@Override
	protected int doHash(final Trace trace) {
		return Objects.hash(trace);
	}

}
