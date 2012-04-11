/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.systemModel;

/**
 * 
 * @author Andre van Hoorn
 */
public class Execution {
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
	 * @param allocationComponent
	 * @param traceId
	 * @param sessionId
	 * @param eoi
	 * @param ess
	 * @param tin
	 * @param tout
	 * 
	 * @throws NullPointerException
	 *             iff any of the passed objects is null.
	 */
	public Execution(final Operation op, final AllocationComponent allocationComponent, final long traceId, final String sessionId, final int eoi, final int ess,
			final long tin, final long tout, final boolean assumed) {
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
	 * @param allocationComponent
	 * @param traceId
	 * @param eoi
	 * @param ess
	 * @param tin
	 * @param tout
	 * 
	 * @throws NullPointerException
	 *             iff any of the passed objects is null.
	 */
	public Execution(final Operation op, final AllocationComponent allocationComponent, final long traceId, final int eoi, final int ess, final long tin,
			final long tout, final boolean assumed) {
		this(op, allocationComponent, traceId, NO_SESSION_ID, eoi, ess, tin, tout, assumed);
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
	 * Returns the sessionId and a default sessionId if no sessionId assigned.
	 * The return value won't be null.
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
	 * Compares this {@link Execution} with the given object with respect to type and fields. All
	 * fields but the {@link #isAssumed()} are considered for comparison.
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
		return this.allocationComponent.equals(other.allocationComponent) && this.operation.equals(other.operation) && this.sessionId.equals(other.sessionId)
				&& (this.traceId == other.traceId) && (this.eoi == other.eoi) && (this.ess == other.ess) && (this.tin == other.tin) && (this.tout == other.tout);
	}

	@Override
	public int hashCode() {
		int hash = 3;
		hash = (43 * hash) + (this.operation != null ? this.operation.hashCode() : 0); // NOCS (inline ?)
		hash = (43 * hash) + (this.allocationComponent != null ? this.allocationComponent.hashCode() : 0); // NOCS (inline ?)
		hash = (43 * hash) + (int) (this.traceId ^ (this.traceId >>> 32));
		hash = (43 * hash) + (this.sessionId != null ? this.sessionId.hashCode() : 0); // NOCS (inline ?)
		hash = (43 * hash) + this.eoi;
		hash = (43 * hash) + this.ess;
		hash = (43 * hash) + (int) (this.tin ^ (this.tin >>> 32));
		hash = (43 * hash) + (int) (this.tout ^ (this.tout >>> 32));
		return hash;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append(this.traceId);
		strBuild.append("[").append(this.eoi).append(",").append(this.ess).append("]").append(" ");
		strBuild.append(this.tin).append("-").append(this.tout).append(" ");
		strBuild.append(this.allocationComponent.toString()).append(".");
		strBuild.append(this.operation.getSignature().getName()).append(" ");

		strBuild.append((this.sessionId != null) ? this.sessionId : NO_SESSION_ID); // NOCS

		return strBuild.toString();
	}
}
