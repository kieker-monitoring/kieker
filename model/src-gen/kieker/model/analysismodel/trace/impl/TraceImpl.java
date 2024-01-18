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
	@Override
	public long getTraceID() {
		return this.traceID;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setTraceID(final long newTraceID) {
		final long oldTraceID = this.traceID;
		this.traceID = newTraceID;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.TRACE__TRACE_ID, oldTraceID, this.traceID));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationCall getRootOperationCall() {
		return this.rootOperationCall;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetRootOperationCall(final OperationCall newRootOperationCall, NotificationChain msgs) {
		final OperationCall oldRootOperationCall = this.rootOperationCall;
		this.rootOperationCall = newRootOperationCall;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.TRACE__ROOT_OPERATION_CALL, oldRootOperationCall,
					newRootOperationCall);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setRootOperationCall(final OperationCall newRootOperationCall) {
		if (newRootOperationCall != this.rootOperationCall) {
			NotificationChain msgs = null;
			if (this.rootOperationCall != null) {
				msgs = ((InternalEObject) this.rootOperationCall).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TracePackage.TRACE__ROOT_OPERATION_CALL, null, msgs);
			}
			if (newRootOperationCall != null) {
				msgs = ((InternalEObject) newRootOperationCall).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TracePackage.TRACE__ROOT_OPERATION_CALL, null, msgs);
			}
			msgs = this.basicSetRootOperationCall(newRootOperationCall, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.TRACE__ROOT_OPERATION_CALL, newRootOperationCall, newRootOperationCall));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			return this.basicSetRootOperationCall(null, msgs);
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
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			return this.getTraceID();
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			return this.getRootOperationCall();
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
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			this.setTraceID((Long) newValue);
			return;
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			this.setRootOperationCall((OperationCall) newValue);
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
	public void eUnset(final int featureID) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			this.setTraceID(TRACE_ID_EDEFAULT);
			return;
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			this.setRootOperationCall((OperationCall) null);
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
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case TracePackage.TRACE__TRACE_ID:
			return this.traceID != TRACE_ID_EDEFAULT;
		case TracePackage.TRACE__ROOT_OPERATION_CALL:
			return this.rootOperationCall != null;
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
		if (this.eIsProxy()) {
			return super.toString();
		}

		final StringBuilder result = new StringBuilder(super.toString());
		result.append(" (traceID: ");
		result.append(this.traceID);
		result.append(')');
		return result.toString();
	}

} // TraceImpl
