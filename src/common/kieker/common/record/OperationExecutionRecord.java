/***************************************************************************
 * Copyright 2011 by
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

package kieker.common.record;

/**
 * String variables must not be null.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public final class OperationExecutionRecord extends AbstractMonitoringRecord {
	private static final String DEFAULT_VALUE = "N/A";
	private static final long serialVersionUID = 1180L;

	/** Used to identify the type of CSV records */
	// Not 10 because className and operationName are serialized to a single field (separated by '.')
	private static final int NUMRECORDFIELDS = 9;

	public volatile int experimentId = -1;
	public volatile String hostName = OperationExecutionRecord.DEFAULT_VALUE;
	public volatile String className = OperationExecutionRecord.DEFAULT_VALUE;
	public volatile String operationName = OperationExecutionRecord.DEFAULT_VALUE;
	public volatile String sessionId = OperationExecutionRecord.DEFAULT_VALUE;
	public volatile long traceId = -1;
	public volatile long tin = -1;
	public volatile long tout = -1;
	public volatile int eoi = -1;
	public volatile int ess = -1;

	/**
	 * Used by probes to store the return value of executed operations.
	 * The field is marked transient as it must not be serialized.
	 */
	public volatile transient Object retVal = null;

	/**
	 * Used by probes to intermediate information.
	 * The field is marked transient as it must not be serialized.
	 */
	public volatile transient boolean isEntryPoint = false;

	/**
	 * Returns an instance of OperationExecutionRecord.
	 * The member variables are initialized that way that only actually
	 * used variables must be updated.
	 */
	public OperationExecutionRecord() {}

	/**
	 * 
	 * @param componentName
	 * @param methodName
	 * @param traceId
	 */
	public OperationExecutionRecord(final String componentName, final String methodName, final long traceId) {
		this.className = componentName;
		this.operationName = methodName;
		this.traceId = traceId;
	}

	/**
	 * 
	 * @param componentName
	 * @param opName
	 * @param traceId
	 * @param tin
	 * @param tout
	 */
	public OperationExecutionRecord(final String componentName, final String opName, final long traceId, final long tin, final long tout) {
		this(componentName, opName, traceId);
		this.tin = tin;
		this.tout = tout;
	}

	/**
	 * 
	 * @param componentName
	 * @param opName
	 * @param tin
	 * @param tout
	 */
	public OperationExecutionRecord(final String componentName, final String opName, final long tin, final long tout) {
		this(componentName, opName, -1, tin, tout);
	}

	/**
	 * 
	 * @param componentName
	 * @param opName
	 * @param sessionId
	 * @param traceId
	 * @param tin
	 * @param tout
	 */
	public OperationExecutionRecord(final String componentName, final String opName, final String sessionId, final long traceId, final long tin, final long tout) {
		this(componentName, opName, traceId, tin, tout);
		this.sessionId = sessionId;
	}

	/**
	 * 
	 * @param componentName
	 * @param opName
	 * @param sessionId
	 * @param traceId
	 * @param tin
	 * @param tout
	 * @param vnName
	 * @param eoi
	 * @param ess
	 */
	public OperationExecutionRecord(final String componentName, final String opName, final String sessionId, final long traceId, final long tin, final long tout,
			final String vnName, final int eoi, final int ess) {
		this(componentName, opName, sessionId, traceId, tin, tout);
		this.hostName = vnName;
		this.eoi = eoi;
		this.ess = ess;
	}

	@Override
	public final Object[] toArray() {
		return new Object[] { this.experimentId, this.className + "." + this.operationName, (this.sessionId == null) ? "NULL" : this.sessionId, this.traceId, // NOCS
			this.tin, this.tout, (this.hostName == null) ? "NULLHOST" : this.hostName, this.eoi, this.ess }; // NOCS
	}

	@Override
	public final Class<?>[] getValueTypes() {
		return new Class[] { int.class, // experimentId
			String.class, // component + op
			String.class, // sessionId
			long.class, // traceId
			long.class, // tin
			long.class, // tout
			String.class, // hostName
			int.class, // eoi
			int.class // ess
		};
	}

	@Override
	public final void initFromArray(final Object[] values) throws IllegalArgumentException { // NOPMD by jwa on 20.09.11 14:30
		try {
			if (values.length != OperationExecutionRecord.NUMRECORDFIELDS) {
				throw new IllegalArgumentException("Expecting vector with " + OperationExecutionRecord.NUMRECORDFIELDS + " elements but found:" + values.length);
			}
			this.experimentId = (Integer) values[0];
			{ // divide name into component and operation name
				final String name = (String) values[1];
				final int posParen = name.lastIndexOf('(');
				int posDot;
				if (posParen != -1) {
					posDot = name.substring(0, posParen).lastIndexOf('.');
				} else {
					posDot = name.lastIndexOf('.');
				}
				if (posDot == -1) {
					this.className = "";
					this.operationName = name;
				} else {
					this.className = name.substring(0, posDot);
					this.operationName = name.substring(posDot + 1);
				}
			}
			this.sessionId = (String) values[2]; // NOCS
			this.traceId = (Long) values[3]; // NOCS
			this.tin = (Long) values[4]; // NOCS
			this.tout = (Long) values[5]; // NOCS
			this.hostName = (String) values[6]; // NOCS
			this.eoi = (Integer) values[7]; // NOCS
			this.ess = (Integer) values[8]; // NOCS
		} catch (final Exception exc) {
			throw new IllegalArgumentException("Failed to init", exc);
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1; // NOPMD
		result = (prime * result) + ((this.className == null) ? 0 : this.className.hashCode()); // NOCS
		result = (prime * result) + this.eoi;
		result = (prime * result) + this.ess;
		result = (prime * result) + this.experimentId;
		result = (prime * result) + ((this.hostName == null) ? 0 : this.hostName.hashCode()); // NOCS
		result = (prime * result) + ((this.operationName == null) ? 0 : this.operationName.hashCode()); // NOCS
		result = (prime * result) + ((this.sessionId == null) ? 0 : this.sessionId.hashCode()); // NOCS
		result = (prime * result) + (int) (this.tin ^ (this.tin >>> 32)); // NOCS
		result = (prime * result) + (int) (this.tout ^ (this.tout >>> 32)); // NOCS
		result = (prime * result) + (int) (this.traceId ^ (this.traceId >>> 32)); // NOCS
		return result;
	}

	/**
	 * Compares two records.
	 * 
	 * If one of the records contains null values for its variables,
	 * false is returned.
	 * 
	 * @param o
	 * @return true iff the compared records are equal.
	 */
	@Override
	public final boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof OperationExecutionRecord)) {
			return false;
		}
		final OperationExecutionRecord ro = (OperationExecutionRecord) o;
		try {
			return this.className.equals(ro.className) && (this.eoi == ro.eoi) && (this.ess == ro.ess)
					// not considering experimentId is this is seldomly used
					&& this.operationName.equals(ro.operationName) && this.sessionId.equals(ro.sessionId) && (this.tin == ro.tin) && (this.tout == ro.tout)
					&& (this.traceId == ro.traceId) && this.hostName.equals(ro.hostName);
		} catch (final NullPointerException ex) {
			// avoid logging!!!! Do something else instead! Records should not depend on logger!
			return false;
		}
	}

	/**
	 * @return the experimentId
	 */
	public final int getExperimentId() {
		return this.experimentId;
	}

	/**
	 * @param experimentId
	 *            the experimentId to set
	 */
	public final void setExperimentId(final int experimentId) {
		this.experimentId = experimentId;
	}

	/**
	 * @return the hostName
	 */
	public final String getHostName() {
		return this.hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public final void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the className
	 */
	public final String getClassName() {
		return this.className;
	}

	/**
	 * @param className
	 *            the className to set
	 */
	public final void setClassName(final String className) {
		this.className = className;
	}

	/**
	 * @return the operationName
	 */
	public final String getOperationName() {
		return this.operationName;
	}

	/**
	 * @param operationName
	 *            the operationName to set
	 */
	public final void setOperationName(final String operationName) {
		this.operationName = operationName;
	}

	/**
	 * @return the sessionId
	 */
	public final String getSessionId() {
		return this.sessionId;
	}

	/**
	 * @param sessionId
	 *            the sessionId to set
	 */
	public final void setSessionId(final String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * @return the traceId
	 */
	public final long getTraceId() {
		return this.traceId;
	}

	/**
	 * @param traceId
	 *            the traceId to set
	 */
	public final void setTraceId(final long traceId) {
		this.traceId = traceId;
	}

	/**
	 * @return the tin
	 */
	public final long getTin() {
		return this.tin;
	}

	/**
	 * @param tin
	 *            the tin to set
	 */
	public final void setTin(final long tin) {
		this.tin = tin;
	}

	/**
	 * @return the tout
	 */
	public final long getTout() {
		return this.tout;
	}

	/**
	 * @param tout
	 *            the tout to set
	 */
	public final void setTout(final long tout) {
		this.tout = tout;
	}

	/**
	 * @return the eoi
	 */
	public final int getEoi() {
		return this.eoi;
	}

	/**
	 * @param eoi
	 *            the eoi to set
	 */
	public final void setEoi(final int eoi) {
		this.eoi = eoi;
	}

	/**
	 * @return the ess
	 */
	public final int getEss() {
		return this.ess;
	}

	/**
	 * @param ess
	 *            the ess to set
	 */
	public final void setEss(final int ess) {
		this.ess = ess;
	}
}
