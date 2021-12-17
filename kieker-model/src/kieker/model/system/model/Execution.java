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

package kieker.model.system.model;

/**
 * This class represents an execution of an operation of an allocated component.
 * The class is used within the trace analysis tool.
 *
 * Note that no assumptions about the {@link java.util.concurrent.TimeUnit} used
 * for the timestamps are made
 * ({@link kieker.model.system.model.Execution#getTin()} and
 * {@link kieker.model.system.model.Execution#getTout()}).
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
public class Execution {

	/** This constant marks that an execution has no session ID. */
	public static final String NO_SESSION_ID = "<NOSESSIONID>";

	private final Operation operation;
	private final AllocationComponent allocationComponent;
	private final long traceId;
	private final String sessionId;
	private final int eoi;
	private final int ess;
	private final long tin;
	private final long tout;

	private final boolean assumed;

	/**
	 * Creates a new Execution instance.
	 *
	 * @param op
	 *            The operation of the execution.
	 * @param allocationComponent
	 *            The allocation component.
	 * @param traceId
	 *            The ID of the trace.
	 * @param sessionId
	 *            The ID of the session.
	 * @param eoi
	 *            The execution order index.
	 * @param ess
	 *            The execution stack size.
	 * @param tin
	 *            The timestamp the execution started.
	 * @param tout
	 *            The timestamp the execution finished.
	 * @param assumed
	 *            Determines whether the execution is assumed or
	 *            not.
	 */
	public Execution(final Operation op, final AllocationComponent allocationComponent, final long traceId,
			final String sessionId, final int eoi, final int ess, final long tin, final long tout,
			final boolean assumed) {
		if (op == null) {
			throw new NullPointerException("argument op must not be null");
		}
		if (allocationComponent == null) {
			throw new NullPointerException("argument allocationComponent must not be null");
		}
		if (sessionId == null) {
			throw new NullPointerException("argument sessionId must not be null");
		}

		this.operation = op;
		this.allocationComponent = allocationComponent;
		this.traceId = traceId;
		this.sessionId = sessionId;
		this.eoi = eoi;
		this.ess = ess;
		this.tin = tin;
		this.tout = tout;
		this.assumed = assumed;
	}

	/**
	 * Creates a new Execution instance. The sessionId is set to a default value.
	 *
	 * @param op
	 *            The operation of the execution.
	 * @param allocationComponent
	 *            The allocation component.
	 * @param traceId
	 *            The ID of the trace.
	 * @param eoi
	 *            The execution order index.
	 * @param ess
	 *            The execution stack size.
	 * @param tin
	 *            The timestamp the execution started.
	 * @param tout
	 *            The timestamp the execution finished.
	 * @param assumed
	 *            Determines whether the execution is assumed or
	 *            not.
	 */
	public Execution(final Operation op, final AllocationComponent allocationComponent, final long traceId,
			final int eoi, final int ess, final long tin, final long tout, final boolean assumed) {
		this(op, allocationComponent, traceId, Execution.NO_SESSION_ID, eoi, ess, tin, tout, assumed);
	}

	public final AllocationComponent getAllocationComponent() {
		return this.allocationComponent;
	}

	public final int getEoi() {
		return this.eoi;
	}

	public final int getEss() {
		return this.ess;
	}

	public final Operation getOperation() {
		return this.operation;
	}

	/**
	 * Returns the sessionId and a default sessionId if no sessionId assigned. The
	 * return value won't be null.
	 *
	 * @return the sessionId.
	 */
	public final String getSessionId() {
		return this.sessionId;
	}

	public final long getTin() {
		return this.tin;
	}

	public final long getTout() {
		return this.tout;
	}

	public final long getTraceId() {
		return this.traceId;
	}

	public boolean isAssumed() {
		return this.assumed;
	}

	/**
	 * Compares this {@link Execution} with the given object with respect to type
	 * and fields. All fields but the {@link #isAssumed()} are considered for
	 * comparison.
	 *
	 * @param obj
	 *            The object to be compared with this instance.
	 *
	 * @return true if and only if the current object and the given object have
	 *         equal values.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Execution)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final Execution other = (Execution) obj;
		return this.allocationComponent.equals(other.allocationComponent) && this.operation.equals(other.operation)
				&& this.sessionId.equals(other.sessionId) && (this.traceId == other.traceId) && (this.eoi == other.eoi)
				&& (this.ess == other.ess) && (this.tin == other.tin) && (this.tout == other.tout)
				&& (this.assumed == other.assumed);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = (43 * hash) + (this.operation != null ? this.operation.hashCode() : 0); // NOCS (inline ?)
		hash = (43 * hash) + (this.allocationComponent != null ? this.allocationComponent.hashCode() : 0); // NOCS
																											// (inline
																											// ?)
		hash = (43 * hash) + (int) (this.traceId ^ (this.traceId >>> 32));
		hash = (43 * hash) + (this.sessionId != null ? this.sessionId.hashCode() : 0); // NOCS (inline ?)
		hash = (43 * hash) + this.eoi;
		hash = (43 * hash) + this.ess;
		hash = (43 * hash) + (int) (this.tin ^ (this.tin >>> 32));
		hash = (43 * hash) + (int) (this.tout ^ (this.tout >>> 32));
		hash = (43 * hash) + (this.assumed ? 5643 : 5648); // NOCS (inline ?)
		return hash;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder(128);
		strBuild.append(this.traceId).append('[').append(this.eoi).append(',').append(this.ess).append("] ")
				.append(this.tin).append('-').append(this.tout).append(' ').append(this.allocationComponent.toString())
				.append('.').append(this.operation.getSignature().getName()).append(' ')
				.append((this.sessionId != null) ? this.sessionId : Execution.NO_SESSION_ID); // NOCS

		if (this.assumed) {
			strBuild.append(" (assumed)");
		}

		return strBuild.toString();
	}
}
