/**
 */
package kieker.analysisteetime.model.analysismodel.trace.impl;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.TracePackage;
import kieker.analysisteetime.model.analysismodel.trace.TraceRoot;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.trace.impl.TraceRootImpl#getTraceID <em>Trace ID</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.trace.impl.TraceRootImpl#getRootOperationCall <em>Root Operation Call</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TraceRootImpl extends MinimalEObjectImpl.Container implements TraceRoot {
	/**
	 * The default value of the '{@link #getTraceID() <em>Trace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceID()
	 * @generated
	 * @ordered
	 */
	protected static final long TRACE_ID_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getTraceID() <em>Trace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceID()
	 * @generated
	 * @ordered
	 */
	protected long traceID = TRACE_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRootOperationCall() <em>Root Operation Call</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRootOperationCall()
	 * @generated
	 * @ordered
	 */
	protected OperationCall rootOperationCall;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TraceRootImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.TRACE_ROOT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public long getTraceID() {
		return traceID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTraceID(long newTraceID) {
		long oldTraceID = traceID;
		traceID = newTraceID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.TRACE_ROOT__TRACE_ID, oldTraceID, traceID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationCall getRootOperationCall() {
		if (rootOperationCall != null && rootOperationCall.eIsProxy()) {
			InternalEObject oldRootOperationCall = (InternalEObject)rootOperationCall;
			rootOperationCall = (OperationCall)eResolveProxy(oldRootOperationCall);
			if (rootOperationCall != oldRootOperationCall) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.TRACE_ROOT__ROOT_OPERATION_CALL, oldRootOperationCall, rootOperationCall));
			}
		}
		return rootOperationCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationCall basicGetRootOperationCall() {
		return rootOperationCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setRootOperationCall(OperationCall newRootOperationCall) {
		OperationCall oldRootOperationCall = rootOperationCall;
		rootOperationCall = newRootOperationCall;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.TRACE_ROOT__ROOT_OPERATION_CALL, oldRootOperationCall, rootOperationCall));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TracePackage.TRACE_ROOT__TRACE_ID:
				return getTraceID();
			case TracePackage.TRACE_ROOT__ROOT_OPERATION_CALL:
				if (resolve) return getRootOperationCall();
				return basicGetRootOperationCall();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case TracePackage.TRACE_ROOT__TRACE_ID:
				setTraceID((Long)newValue);
				return;
			case TracePackage.TRACE_ROOT__ROOT_OPERATION_CALL:
				setRootOperationCall((OperationCall)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case TracePackage.TRACE_ROOT__TRACE_ID:
				setTraceID(TRACE_ID_EDEFAULT);
				return;
			case TracePackage.TRACE_ROOT__ROOT_OPERATION_CALL:
				setRootOperationCall((OperationCall)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case TracePackage.TRACE_ROOT__TRACE_ID:
				return traceID != TRACE_ID_EDEFAULT;
			case TracePackage.TRACE_ROOT__ROOT_OPERATION_CALL:
				return rootOperationCall != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (traceID: ");
		result.append(traceID);
		result.append(')');
		return result.toString();
	}

} //TraceRootImpl
