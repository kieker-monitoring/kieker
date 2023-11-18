/**
 */
package kieker.model.analysismodel.execution.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.StorageDataflow;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Storage Dataflow</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.impl.StorageDataflowImpl#getStorage <em>Storage</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.StorageDataflowImpl#getCode <em>Code</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.StorageDataflowImpl#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StorageDataflowImpl extends MinimalEObjectImpl.Container implements StorageDataflow {
	/**
	 * The cached value of the '{@link #getStorage() <em>Storage</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStorage()
	 * @generated
	 * @ordered
	 */
	protected DeployedStorage storage;

	/**
	 * The cached value of the '{@link #getCode() <em>Code</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation code;

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
	protected StorageDataflowImpl() {
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
		return ExecutionPackage.Literals.STORAGE_DATAFLOW;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedStorage getStorage() {
		if ((this.storage != null) && this.storage.eIsProxy()) {
			final InternalEObject oldStorage = (InternalEObject) this.storage;
			this.storage = (DeployedStorage) this.eResolveProxy(oldStorage);
			if (this.storage != oldStorage) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.STORAGE_DATAFLOW__STORAGE, oldStorage, this.storage));
				}
			}
		}
		return this.storage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeployedStorage basicGetStorage() {
		return this.storage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStorage(final DeployedStorage newStorage) {
		final DeployedStorage oldStorage = this.storage;
		this.storage = newStorage;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.STORAGE_DATAFLOW__STORAGE, oldStorage, this.storage));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedOperation getCode() {
		if ((this.code != null) && this.code.eIsProxy()) {
			final InternalEObject oldCode = (InternalEObject) this.code;
			this.code = (DeployedOperation) this.eResolveProxy(oldCode);
			if (this.code != oldCode) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.STORAGE_DATAFLOW__CODE, oldCode, this.code));
				}
			}
		}
		return this.code;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeployedOperation basicGetCode() {
		return this.code;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCode(final DeployedOperation newCode) {
		final DeployedOperation oldCode = this.code;
		this.code = newCode;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.STORAGE_DATAFLOW__CODE, oldCode, this.code));
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.STORAGE_DATAFLOW__DIRECTION, oldDirection, this.direction));
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
		case ExecutionPackage.STORAGE_DATAFLOW__STORAGE:
			if (resolve) {
				return this.getStorage();
			}
			return this.basicGetStorage();
		case ExecutionPackage.STORAGE_DATAFLOW__CODE:
			if (resolve) {
				return this.getCode();
			}
			return this.basicGetCode();
		case ExecutionPackage.STORAGE_DATAFLOW__DIRECTION:
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
		case ExecutionPackage.STORAGE_DATAFLOW__STORAGE:
			this.setStorage((DeployedStorage) newValue);
			return;
		case ExecutionPackage.STORAGE_DATAFLOW__CODE:
			this.setCode((DeployedOperation) newValue);
			return;
		case ExecutionPackage.STORAGE_DATAFLOW__DIRECTION:
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
		case ExecutionPackage.STORAGE_DATAFLOW__STORAGE:
			this.setStorage((DeployedStorage) null);
			return;
		case ExecutionPackage.STORAGE_DATAFLOW__CODE:
			this.setCode((DeployedOperation) null);
			return;
		case ExecutionPackage.STORAGE_DATAFLOW__DIRECTION:
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
		case ExecutionPackage.STORAGE_DATAFLOW__STORAGE:
			return this.storage != null;
		case ExecutionPackage.STORAGE_DATAFLOW__CODE:
			return this.code != null;
		case ExecutionPackage.STORAGE_DATAFLOW__DIRECTION:
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

} // StorageDataflowImpl
