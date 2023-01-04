/**
 */
package kieker.model.analysismodel.execution.impl;

import kieker.model.analysismodel.deployment.DeployedOperation;

import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.execution.impl.InvocationImpl#getCaller <em>Caller</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.InvocationImpl#getCallee <em>Callee</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InvocationImpl extends MinimalEObjectImpl.Container implements Invocation {
	/**
	 * The cached value of the '{@link #getCaller() <em>Caller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCaller()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation caller;

	/**
	 * The cached value of the '{@link #getCallee() <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallee()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation callee;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected InvocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExecutionPackage.Literals.INVOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeployedOperation getCaller() {
		if (caller != null && caller.eIsProxy()) {
			InternalEObject oldCaller = (InternalEObject)caller;
			caller = (DeployedOperation)eResolveProxy(oldCaller);
			if (caller != oldCaller) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.INVOCATION__CALLER, oldCaller, caller));
			}
		}
		return caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation basicGetCaller() {
		return caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCaller(DeployedOperation newCaller) {
		DeployedOperation oldCaller = caller;
		caller = newCaller;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.INVOCATION__CALLER, oldCaller, caller));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeployedOperation getCallee() {
		if (callee != null && callee.eIsProxy()) {
			InternalEObject oldCallee = (InternalEObject)callee;
			callee = (DeployedOperation)eResolveProxy(oldCallee);
			if (callee != oldCallee) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.INVOCATION__CALLEE, oldCallee, callee));
			}
		}
		return callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation basicGetCallee() {
		return callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCallee(DeployedOperation newCallee) {
		DeployedOperation oldCallee = callee;
		callee = newCallee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.INVOCATION__CALLEE, oldCallee, callee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExecutionPackage.INVOCATION__CALLER:
				if (resolve) return getCaller();
				return basicGetCaller();
			case ExecutionPackage.INVOCATION__CALLEE:
				if (resolve) return getCallee();
				return basicGetCallee();
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
			case ExecutionPackage.INVOCATION__CALLER:
				setCaller((DeployedOperation)newValue);
				return;
			case ExecutionPackage.INVOCATION__CALLEE:
				setCallee((DeployedOperation)newValue);
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
			case ExecutionPackage.INVOCATION__CALLER:
				setCaller((DeployedOperation)null);
				return;
			case ExecutionPackage.INVOCATION__CALLEE:
				setCallee((DeployedOperation)null);
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
			case ExecutionPackage.INVOCATION__CALLER:
				return caller != null;
			case ExecutionPackage.INVOCATION__CALLEE:
				return callee != null;
		}
		return super.eIsSet(featureID);
	}

} //InvocationImpl
