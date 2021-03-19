/**
 */
package kieker.model.analysismodel.trace.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.trace.OperationCall;
import kieker.model.analysismodel.trace.Trace;
import kieker.model.analysismodel.trace.TracePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.trace.impl.TraceImpl#getTraceID <em>Trace ID</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.TraceImpl#getRootOperationCall <em>Root Operation Call</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TraceImpl extends MinimalEObjectImpl.Container implements Trace {
	/**
	 * The default value of the '{@link #getTraceID() <em>Trace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getTraceID()
	 * @generated
	 * @ordered
	 */
	protected static final long TRACE_ID_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getTraceID() <em>Trace ID</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getTraceID()
	 * @generated
	 * @ordered
	 */
	protected long traceID = TRACE_ID_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRootOperationCall() <em>Root Operation Call</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getRootOperationCall()
	 * @generated
	 * @ordered
	 */
	protected OperationCall rootOperationCall;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected TraceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.TRACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public long getTraceID() {
		return traceID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setTraceID(long newTraceID) {
		long oldTraceID = traceID;
		traceID = newTraceID;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.TRACE__TRACE_ID, oldTraceID, traceID));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public OperationCall getRootOperationCall() {
		return rootOperationCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetRootOperationCall(OperationCall newRootOperationCall, NotificationChain msgs) {
		OperationCall oldRootOperationCall = rootOperationCall;
		rootOperationCall = newRootOperationCall;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.TRACE__ROOT_OPERATION_CALL, oldRootOperationCall,
					newRootOperationCall);
			if (msgs == null)
				msgs = notification;
			else
				msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setRootOperationCall(OperationCall newRootOperationCall) {
		if (newRootOperationCall != rootOperationCall) {
			NotificationChain msgs = null;
			if (rootOperationCall != null)
				msgs = ((InternalEObject) rootOperationCall).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TracePackage.TRACE__ROOT_OPERATION_CALL, null, msgs);
			if (newRootOperationCall != null)
				msgs = ((InternalEObject) newRootOperationCall).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TracePackage.TRACE__ROOT_OPERATION_CALL, null, msgs);
			msgs = basicSetRootOperationCall(newRootOperationCall, msgs);
			if (msgs != null)
				msgs.dispatch();
		} else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.TRACE__ROOT_OPERATION_CALL, newRootOperationCall, newRootOperationCall));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			return basicSetRootOperationCall(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			return getTraceID();
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			return getRootOperationCall();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			setTraceID((Long) newValue);
			return;
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			setRootOperationCall((OperationCall) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			setTraceID(TRACE_ID_EDEFAULT);
			return;
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			setRootOperationCall((OperationCall) null);
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			return traceID != TRACE_ID_EDEFAULT;
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			return rootOperationCall != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy())
			return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (traceID: ");
		result.append(traceID);
		result.append(')');
		return result.toString();
	}

} // TraceImpl
