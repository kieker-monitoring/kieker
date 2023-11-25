/**
 */
package kieker.model.analysismodel.execution.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.impl.InvocationImpl#getCaller <em>Caller</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.InvocationImpl#getCallee <em>Callee</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InvocationImpl extends MinimalEObjectImpl.Container implements Invocation {
	/**
	 * The cached value of the '{@link #getCaller() <em>Caller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCaller()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation caller;

	/**
	 * The cached value of the '{@link #getCallee() <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCallee()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation callee;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected InvocationImpl() {
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
		return ExecutionPackage.Literals.INVOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedOperation getCaller() {
		if ((this.caller != null) && this.caller.eIsProxy()) {
			final InternalEObject oldCaller = (InternalEObject) this.caller;
			this.caller = (DeployedOperation) this.eResolveProxy(oldCaller);
			if (this.caller != oldCaller) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.INVOCATION__CALLER, oldCaller, this.caller));
				}
			}
		}
		return this.caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeployedOperation basicGetCaller() {
		return this.caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCaller(final DeployedOperation newCaller) {
		final DeployedOperation oldCaller = this.caller;
		this.caller = newCaller;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.INVOCATION__CALLER, oldCaller, this.caller));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedOperation getCallee() {
		if ((this.callee != null) && this.callee.eIsProxy()) {
			final InternalEObject oldCallee = (InternalEObject) this.callee;
			this.callee = (DeployedOperation) this.eResolveProxy(oldCallee);
			if (this.callee != oldCallee) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.INVOCATION__CALLEE, oldCallee, this.callee));
				}
			}
		}
		return this.callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeployedOperation basicGetCallee() {
		return this.callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCallee(final DeployedOperation newCallee) {
		final DeployedOperation oldCallee = this.callee;
		this.callee = newCallee;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.INVOCATION__CALLEE, oldCallee, this.callee));
		}
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
		case ExecutionPackage.INVOCATION__CALLER:
			if (resolve) {
				return this.getCaller();
			}
			return this.basicGetCaller();
		case ExecutionPackage.INVOCATION__CALLEE:
			if (resolve) {
				return this.getCallee();
			}
			return this.basicGetCallee();
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
		case ExecutionPackage.INVOCATION__CALLER:
			this.setCaller((DeployedOperation) newValue);
			return;
		case ExecutionPackage.INVOCATION__CALLEE:
			this.setCallee((DeployedOperation) newValue);
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
		case ExecutionPackage.INVOCATION__CALLER:
			this.setCaller((DeployedOperation) null);
			return;
		case ExecutionPackage.INVOCATION__CALLEE:
			this.setCallee((DeployedOperation) null);
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
		case ExecutionPackage.INVOCATION__CALLER:
			return this.caller != null;
		case ExecutionPackage.INVOCATION__CALLEE:
			return this.callee != null;
		}
		return super.eIsSet(featureID);
	}

} // InvocationImpl
