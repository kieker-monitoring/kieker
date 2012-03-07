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

package kieker.common.record.controlflow;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * String variables must not be null.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public final class OperationExecutionRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = -3963151278085958619L;
	private static final Class<?>[] TYPES = {
		int.class, // experimentId
		String.class, // operationSignature
		String.class, // sessionId
		long.class, // traceId
		long.class, // tin
		long.class, // tout
		String.class, // hostName
		int.class, // eoi
		int.class, // ess
	};
	private static final String DEFAULT_VALUE = "N/A";

	private volatile int experimentId = -1;
	private volatile String hostName = OperationExecutionRecord.DEFAULT_VALUE;
	private volatile String operationSignature = OperationExecutionRecord.DEFAULT_VALUE;
	private volatile String sessionId = OperationExecutionRecord.DEFAULT_VALUE;
	private volatile long traceId = -1;
	private volatile long tin = -1;
	private volatile long tout = -1;
	private volatile int eoi = -1;
	private volatile int ess = -1;

	/**
	 * Used by probes to store the return value of executed operations.
	 * The field is marked transient as it must not be serialized.
	 */
	private transient volatile Object retVal = null;

	/**
	 * Used by probes to intermediate information.
	 * The field is marked transient as it must not be serialized.
	 */
	private transient volatile boolean entryPoint = false;

	/**
	 * Returns an instance of OperationExecutionRecord.
	 * The member variables are initialized that way that only actually
	 * used variables must be updated.
	 */
	public OperationExecutionRecord() {
		// nothing to do
	}

	public OperationExecutionRecord(final String operationSignature, final long traceId) {
		this.operationSignature = operationSignature;
		this.traceId = traceId;
	}

	public OperationExecutionRecord(final String operationSignature, final long traceId, final long tin, final long tout) {
		this(operationSignature, traceId);
		this.tin = tin;
		this.tout = tout;
	}

	public OperationExecutionRecord(final String operationSignature, final long tin, final long tout) {
		this(operationSignature, -1, tin, tout);
	}

	// public OperationExecutionRecord(final String operationSignature, final String sessionId, final long traceId, final long tin, final long tout) {
	// this(operationSignature, traceId, tin, tout);
	// this.sessionId = sessionId;
	// }

	public OperationExecutionRecord(final String operationSignature, final String sessionId, final long traceId, final long tin, final long tout,
			final String vnName, final int eoi, final int ess) {
		this(operationSignature, sessionId, traceId, tin, tout);
		this.hostName = vnName;
		this.eoi = eoi;
		this.ess = ess;
	}

	@Deprecated
	public OperationExecutionRecord(final String componentName, final String methodName, final long traceId) {
		this.operationSignature = componentName + '.' + methodName;
		this.traceId = traceId;
	}

	@Deprecated
	public OperationExecutionRecord(final String componentName, final String opName, final long traceId, final long tin, final long tout) {
		this(componentName, opName, traceId);
		this.tin = tin;
		this.tout = tout;
	}

	@Deprecated
	public OperationExecutionRecord(final String componentName, final String opName, final long tin, final long tout) {
		this(componentName, opName, -1, tin, tout);
	}

	@Deprecated
	public OperationExecutionRecord(final String componentName, final String opName, final String sessionId, final long traceId, final long tin, final long tout) {
		this(componentName, opName, traceId, tin, tout);
		this.sessionId = sessionId;
	}

	@Deprecated
	public OperationExecutionRecord(final String componentName, final String opName, final String sessionId, final long traceId, final long tin, final long tout,
			final String vnName, final int eoi, final int ess) {
		this(componentName, opName, sessionId, traceId, tin, tout);
		this.hostName = vnName;
		this.eoi = eoi;
		this.ess = ess;
	}

	public OperationExecutionRecord(final Object[] values) {
		AbstractMonitoringRecord.checkArray(values, OperationExecutionRecord.TYPES);
		try {
			this.experimentId = (Integer) values[0];
			this.operationSignature = (String) values[1]; // NOCS
			this.sessionId = (String) values[2]; // NOCS
			this.traceId = (Long) values[3]; // NOCS
			this.tin = (Long) values[4]; // NOCS
			this.tout = (Long) values[5]; // NOCS
			this.hostName = (String) values[6]; // NOCS
			this.eoi = (Integer) values[7]; // NOCS
			this.ess = (Integer) values[8]; // NOCS
		} catch (final Exception exc) { // NOCS (IllegalCatchCheck) // NOPMD
			throw new IllegalArgumentException("Failed to init", exc);
		}
	}

	@Override
	public final Object[] toArray() {
		return new Object[] {
			this.experimentId,
			this.operationSignature,
			(this.sessionId == null) ? "NULL" : this.sessionId, // NOCS
			this.traceId,
			this.tin,
			this.tout,
			(this.hostName == null) ? "NULLHOST" : this.hostName, // NOCS
			this.eoi,
			this.ess, };
	}

	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?>[] getValueTypes() {
		return OperationExecutionRecord.TYPES.clone();
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

	public String getOperationSignature() {
		return this.operationSignature;
	}

	public void setOperationSignature(final String operationSignature) {
		this.operationSignature = operationSignature;
	}

	/**
	 * @return the className
	 */
	@Deprecated
	public final String getClassName() {
		final String name = this.getOperationSignature();
		final int posParen = name.lastIndexOf('(');
		int posDot;
		if (posParen != -1) {
			posDot = name.substring(0, posParen).lastIndexOf('.');
		} else {
			posDot = name.lastIndexOf('.');
		}
		if (posDot == -1) {
			return "";
		} else {
			final String firstpart = name.substring(0, posDot);
			final int posSpace = firstpart.lastIndexOf(' ');
			if (posSpace == -1) {
				return firstpart;
			} else {
				return firstpart.substring(posSpace + 1, firstpart.length());
			}
		}
	}

	/**
	 * @param className
	 *            the className to set
	 */
	@Deprecated
	public final void setClassName(final String className) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the operationName
	 */
	@Deprecated
	public final String getOperationName() {
		final String name = this.getOperationSignature();
		final int posParen = name.lastIndexOf('(');
		int posDot;
		if (posParen != -1) {
			posDot = name.substring(0, posParen).lastIndexOf('.');
		} else {
			posDot = name.lastIndexOf('.');
		}
		if (posDot == -1) {
			return name;
		} else {
			return name.substring(posDot + 1);
		}
	}

	/**
	 * @param operationName
	 *            the operationName to set
	 */
	@Deprecated
	public final void setOperationName(final String operationName) {
		throw new UnsupportedOperationException();
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

	public Object getRetVal() {
		return this.retVal;
	}

	public void setRetVal(final Object retVal) {
		this.retVal = retVal;
	}

	public boolean isEntryPoint() {
		return this.entryPoint;
	}

	public void setEntryPoint(final boolean entryPoint) {
		this.entryPoint = entryPoint;
	}
}
