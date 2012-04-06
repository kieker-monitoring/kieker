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
import kieker.common.util.ClassOperationSignaturePair;

/**
 * String variables must not be null.
 * 
 * @author Andre van Hoorn, Jan Waller
 */
public final class OperationExecutionRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {

	/**
	 * Constant to be used if no hostname required.
	 */
	public static final String NO_HOSTNAME = "<default-host>";

	/**
	 * Constant to be used if no session ID required.
	 */
	public static final String NO_SESSION_ID = "<no-session-id>";

	/**
	 * Constant to be used if no trace ID required.
	 */
	public static final long NO_TRACEID = -1;

	/**
	 * Constant to be used if no timestamp required.
	 */
	public static final long NO_TIMESTAMP = -1;

	/**
	 * Constant to be used if no eoi or ess required.
	 */
	public static final int NO_EOI_ESS = -1;

	/**
	 * This field should not be exported, because it makes little sense to have no associated operation
	 */
	private static final String NO_OPERATION_SIGNATURE = "noOperation";

	private static final long serialVersionUID = -3963151278085958619L;
	private static final Class<?>[] TYPES = {
		int.class, // experimentId
		String.class, // operationSignature
		String.class, // sessionId
		long.class, // traceId
		long.class, // tin
		long.class, // tout
		String.class, // hostname
		int.class, // eoi
		int.class, // ess
	};

	/**
	 * Will be removed in 1.6
	 */
	@Deprecated
	private volatile int experimentId = -1;

	// TODO: change all fields to final in 1.6
	private volatile String hostname = NO_HOSTNAME;
	private volatile String operationSignature = NO_OPERATION_SIGNATURE;
	private volatile String sessionId = NO_SESSION_ID;
	private volatile long traceId = NO_TRACEID;
	private volatile long tin = NO_TIMESTAMP;
	private volatile long tout = NO_TIMESTAMP;
	private volatile int eoi = NO_EOI_ESS;
	private volatile int ess = NO_EOI_ESS;

	/**
	 * Will be removed in 1.6
	 * 
	 * Used by probes to store the return value of executed operations.
	 * The field is marked transient as it must not be serialized.
	 */
	@Deprecated
	private transient volatile Object retVal = null;

	/**
	 * Will be removed in 1.6
	 * 
	 * Used by probes to intermediate information.
	 * The field is marked transient as it must not be serialized.
	 */
	@Deprecated
	private transient volatile boolean entryPoint = false;

	/**
	 * Returns an instance of OperationExecutionRecord.
	 * The member variables are initialized that way that only actually
	 * used variables must be updated.
	 * 
	 * @deprecated will be removed in Kieker 1.6
	 */
	@Deprecated
	public OperationExecutionRecord() {
		// nothing to do
	}

	/**
	 * Creates a new {@link OperationExecutionRecord} with the given parameters. The fields {@link #getRetVal()} is set
	 * to null; {@link #isEntryPoint()} is set to false.
	 * 
	 * @param operationSignature
	 *            an operation string, as defined in {@link kieker.common.util.ClassOperationSignaturePair#splitOperationSignatureStr(String)}; must not be null.
	 * @param sessionId
	 *            the session ID; must not be null, use {@link #NO_SESSION_ID} if no session ID desired.
	 * @param traceId
	 *            the trace ID; use {@link #NO_TRACEID} if no trace ID desired.
	 * @param tin
	 *            the execution start timestamp; use {@link #NO_TIMESTAMP} if no timestamp desired.
	 * @param tout
	 *            the execution stop timestamp; use {@link #NO_TIMESTAMP} if no timestamp desired.
	 * @param hostname
	 *            the host name; must not be null, use {@link #NO_HOSTNAME} if no host name desired.
	 * @param eoi
	 *            the execution order index (eoi); use {@link #NO_EOI_ESS} if no eoi desired.
	 * @param ess
	 *            the execution order index (ess); use {@link #NO_EOI_ESS} if no ess desired.
	 */
	public OperationExecutionRecord(final String operationSignature, final String sessionId, final long traceId, final long tin, final long tout,
			final String hostname, final int eoi, final int ess) {
		this.operationSignature = (operationSignature == null) ? NO_OPERATION_SIGNATURE : operationSignature; // NOCS
		this.traceId = traceId;
		this.tin = tin;
		this.tout = tout;
		this.sessionId = (sessionId == null) ? NO_SESSION_ID : sessionId; // NOCS
		this.hostname = (hostname == null) ? NO_HOSTNAME : hostname; // NOCS
		this.eoi = eoi;
		this.ess = ess;
	}

	/**
	 * 
	 * @param componentName
	 * @param methodName
	 * @param traceId
	 * 
	 * @deprecated will be removed in Kieker 1.6
	 */
	@Deprecated
	public OperationExecutionRecord(final String componentName, final String methodName, final long traceId) {
		this.operationSignature = componentName + '.' + methodName;
		this.traceId = traceId;
	}

	/**
	 * 
	 * @param componentName
	 * @param opName
	 * @param traceId
	 * @param tin
	 * @param tout
	 * 
	 * @deprecated will be removed in Kieker 1.6
	 */
	@Deprecated
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
	 * 
	 * @deprecated will be removed in Kieker 1.6
	 */
	@Deprecated
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
	 * 
	 * @deprecated will be removed in Kieker 1.6
	 */
	@Deprecated
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
	 * 
	 * @deprecated will be removed in Kieker 1.6
	 */
	@Deprecated
	public OperationExecutionRecord(final String componentName, final String opName, final String sessionId, final long traceId, final long tin, final long tout,
			final String vnName, final int eoi, final int ess) {
		this(componentName, opName, sessionId, traceId, tin, tout);
		this.hostname = vnName;
		this.eoi = eoi;
		this.ess = ess;
	}

	public OperationExecutionRecord(final Object[] values) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, TYPES); // throws IllegalArgumentException
		this.experimentId = (Integer) values[0];
		this.operationSignature = (String) values[1];
		this.sessionId = (String) values[2];
		this.traceId = (Long) values[3];
		this.tin = (Long) values[4];
		this.tout = (Long) values[5];
		this.hostname = (String) values[6];
		this.eoi = (Integer) values[7];
		this.ess = (Integer) values[8];
		// set transient values
		this.retVal = null; // NOPMD (null)
		this.entryPoint = false;
	}

	public final Object[] toArray() {
		// TODO: remove NULL checks in Kieker 1.6, as null values not possible due to checks in the constructor
		return new Object[] {
			this.experimentId,
			(this.operationSignature == null) ? NO_OPERATION_SIGNATURE : this.operationSignature, // NOCS
			(this.sessionId == null) ? NO_SESSION_ID : this.sessionId, // NOCS
			this.traceId,
			this.tin,
			this.tout,
			(this.hostname == null) ? NO_HOSTNAME : this.hostname, // NOCS
			this.eoi,
			this.ess, };
	}

	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public Class<?>[] getValueTypes() {
		return TYPES.clone();
	}

	/**
	 * @return the experimentId
	 * @deprecated Will be removed in 1.6
	 */
	@Deprecated
	public final int getExperimentId() {
		return this.experimentId;
	}

	/**
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param experimentId
	 *            the experimentId to set
	 */
	@Deprecated
	public final void setExperimentId(final int experimentId) {
		this.experimentId = experimentId;
	}

	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return this.hostname;
	}

	/**
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param hostname
	 *            the hostname to set
	 */
	@Deprecated
	public final void setHostname(final String hostname) {
		this.hostname = hostname;
	}

	public String getOperationSignature() {
		return this.operationSignature;
	}

	/**
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 *             A string can, for example, be created using {@link kieker.common.util.ClassOperationSignaturePair#toOperationSignatureString()}.
	 * 
	 * @param operationSignature
	 */
	@Deprecated
	public void setOperationSignature(final String operationSignature) {
		this.operationSignature = operationSignature;
	}

	/**
	 * @return the className
	 * 
	 * @deprecated will be removed in Kieker 1.6
	 */
	@Deprecated
	public final String getClassName() {
		final ClassOperationSignaturePair classOpPair = ClassOperationSignaturePair.splitOperationSignatureStr(this.operationSignature);
		return classOpPair.getFqClassname();
	}

	/**
	 * Use {@link #setOperationSignature(String)} instead.
	 * 
	 * @param className
	 *            the className to set
	 * 
	 * @deprecated will be removed in Kieker 1.6. Also, this class will become immutable in Kieker 1.6.
	 */
	@Deprecated
	public final void setClassName(final String className) {
		throw new UnsupportedOperationException();
	}

	/**
	 * @return the operationName
	 * 
	 * @deprecated will be removed in Kieker 1.6
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
	 * Use {@link #setOperationSignature(String)} instead.
	 * 
	 * @param operationName
	 *            the operationName to set
	 * 
	 * @deprecated will be removed in Kieker 1.6. Also, this class will become immutable in Kieker 1.6.
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
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param sessionId
	 *            the sessionId to set
	 */
	@Deprecated
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
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param traceId
	 *            the traceId to set
	 */
	@Deprecated
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
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param tin
	 *            the tin to set
	 */
	@Deprecated
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
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param tout
	 *            the tout to set
	 */
	@Deprecated
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
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param eoi
	 *            the eoi to set
	 */
	@Deprecated
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
	 * @deprecated this class will become immutable in Kieker 1.6
	 * 
	 * @param ess
	 *            the ess to set
	 */
	@Deprecated
	public final void setEss(final int ess) {
		this.ess = ess;
	}

	/**
	 * @deprecated remove in Kieker 1.6
	 */
	@Deprecated
	public Object getRetVal() {
		return this.retVal;
	}

	/**
	 * @deprecated remove in Kieker 1.6
	 * 
	 * @param retVal
	 */
	@Deprecated
	public void setRetVal(final Object retVal) {
		this.retVal = retVal;
	}

	/**
	 * @deprecated remove in Kieker 1.6
	 */
	@Deprecated
	public boolean isEntryPoint() {
		return this.entryPoint;
	}

	/**
	 * @deprecated remove in Kieker 1.6
	 * 
	 * @param entryPoint
	 */
	@Deprecated
	public void setEntryPoint(final boolean entryPoint) {
		this.entryPoint = entryPoint;
	}
}
