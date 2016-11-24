/**
 */
package kieker.analysisteetime.model.analysismodel.trace.impl;

import kieker.analysisteetime.model.analysismodel.trace.FailedOperationCall;
import kieker.analysisteetime.model.analysismodel.trace.TracePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Failed Operation Call</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.trace.impl.FailedOperationCallImpl#getFailedCause <em>Failed Cause</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FailedOperationCallImpl extends OperationCallImpl implements FailedOperationCall {
	/**
	 * The default value of the '{@link #getFailedCause() <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedCause()
	 * @generated
	 * @ordered
	 */
	protected static final String FAILED_CAUSE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFailedCause() <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedCause()
	 * @generated
	 * @ordered
	 */
	protected String failedCause = FAILED_CAUSE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FailedOperationCallImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.FAILED_OPERATION_CALL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFailedCause() {
		return failedCause;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFailedCause(String newFailedCause) {
		String oldFailedCause = failedCause;
		failedCause = newFailedCause;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.FAILED_OPERATION_CALL__FAILED_CAUSE, oldFailedCause, failedCause));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case TracePackage.FAILED_OPERATION_CALL__FAILED_CAUSE:
				return getFailedCause();
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
			case TracePackage.FAILED_OPERATION_CALL__FAILED_CAUSE:
				setFailedCause((String)newValue);
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
			case TracePackage.FAILED_OPERATION_CALL__FAILED_CAUSE:
				setFailedCause(FAILED_CAUSE_EDEFAULT);
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
			case TracePackage.FAILED_OPERATION_CALL__FAILED_CAUSE:
				return FAILED_CAUSE_EDEFAULT == null ? failedCause != null : !FAILED_CAUSE_EDEFAULT.equals(failedCause);
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
		result.append(" (failedCause: ");
		result.append(failedCause);
		result.append(')');
		return result.toString();
	}

} //FailedOperationCallImpl
