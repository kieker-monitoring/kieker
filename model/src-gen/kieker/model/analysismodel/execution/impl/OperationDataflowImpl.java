/**
 */
package kieker.model.analysismodel.execution.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.OperationDataflow;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Dataflow</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.impl.OperationDataflowImpl#getCaller <em>Caller</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.OperationDataflowImpl#getCallee <em>Callee</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.OperationDataflowImpl#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationDataflowImpl extends MinimalEObjectImpl.Container implements OperationDataflow {
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
	 * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected static final EDirection DIRECTION_EDEFAULT = EDirection.READ;

	/**
	 * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected EDirection direction = DIRECTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OperationDataflowImpl() {
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
		return ExecutionPackage.Literals.OPERATION_DATAFLOW;
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
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.OPERATION_DATAFLOW__CALLER, oldCaller, this.caller));
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.OPERATION_DATAFLOW__CALLER, oldCaller, this.caller));
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
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.OPERATION_DATAFLOW__CALLEE, oldCallee, this.callee));
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.OPERATION_DATAFLOW__CALLEE, oldCallee, this.callee));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EDirection getDirection() {
		return this.direction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDirection(final EDirection newDirection) {
		final EDirection oldDirection = this.direction;
		this.direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.OPERATION_DATAFLOW__DIRECTION, oldDirection, this.direction));
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
		case ExecutionPackage.OPERATION_DATAFLOW__CALLER:
			if (resolve) {
				return this.getCaller();
			}
			return this.basicGetCaller();
		case ExecutionPackage.OPERATION_DATAFLOW__CALLEE:
			if (resolve) {
				return this.getCallee();
			}
			return this.basicGetCallee();
		case ExecutionPackage.OPERATION_DATAFLOW__DIRECTION:
			return this.getDirection();
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
		case ExecutionPackage.OPERATION_DATAFLOW__CALLER:
			this.setCaller((DeployedOperation) newValue);
			return;
		case ExecutionPackage.OPERATION_DATAFLOW__CALLEE:
			this.setCallee((DeployedOperation) newValue);
			return;
		case ExecutionPackage.OPERATION_DATAFLOW__DIRECTION:
			this.setDirection((EDirection) newValue);
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
		case ExecutionPackage.OPERATION_DATAFLOW__CALLER:
			this.setCaller((DeployedOperation) null);
			return;
		case ExecutionPackage.OPERATION_DATAFLOW__CALLEE:
			this.setCallee((DeployedOperation) null);
			return;
		case ExecutionPackage.OPERATION_DATAFLOW__DIRECTION:
			this.setDirection(DIRECTION_EDEFAULT);
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
		case ExecutionPackage.OPERATION_DATAFLOW__CALLER:
			return this.caller != null;
		case ExecutionPackage.OPERATION_DATAFLOW__CALLEE:
			return this.callee != null;
		case ExecutionPackage.OPERATION_DATAFLOW__DIRECTION:
			return this.direction != DIRECTION_EDEFAULT;
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
		result.append(" (direction: ");
		result.append(this.direction);
		result.append(')');
		return result.toString();
	}

} // OperationDataflowImpl
