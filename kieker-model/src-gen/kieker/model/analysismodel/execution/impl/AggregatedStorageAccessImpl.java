/**
 */
package kieker.model.analysismodel.execution.impl;

import java.util.Collection;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;

import kieker.model.analysismodel.execution.AggregatedStorageAccess;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Aggregated Storage Access</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl#getStorage <em>Storage</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl#getCode <em>Code</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl#getSources <em>Sources</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl#getDirection <em>Direction</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AggregatedStorageAccessImpl extends MinimalEObjectImpl.Container implements AggregatedStorageAccess {
	/**
	 * The cached value of the '{@link #getStorage() <em>Storage</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorage()
	 * @generated
	 * @ordered
	 */
	protected DeployedStorage storage;

	/**
	 * The cached value of the '{@link #getCode() <em>Code</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCode()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation code;

	/**
	 * The cached value of the '{@link #getSources() <em>Sources</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSources()
	 * @generated
	 * @ordered
	 */
	protected EList<String> sources;

	/**
	 * The default value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected static final EDirection DIRECTION_EDEFAULT = EDirection.READ;

	/**
	 * The cached value of the '{@link #getDirection() <em>Direction</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDirection()
	 * @generated
	 * @ordered
	 */
	protected EDirection direction = DIRECTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AggregatedStorageAccessImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExecutionPackage.Literals.AGGREGATED_STORAGE_ACCESS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeployedStorage getStorage() {
		if (storage != null && storage.eIsProxy()) {
			InternalEObject oldStorage = (InternalEObject)storage;
			storage = (DeployedStorage)eResolveProxy(oldStorage);
			if (storage != oldStorage) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.AGGREGATED_STORAGE_ACCESS__STORAGE, oldStorage, storage));
			}
		}
		return storage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedStorage basicGetStorage() {
		return storage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStorage(DeployedStorage newStorage) {
		DeployedStorage oldStorage = storage;
		storage = newStorage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.AGGREGATED_STORAGE_ACCESS__STORAGE, oldStorage, storage));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeployedOperation getCode() {
		if (code != null && code.eIsProxy()) {
			InternalEObject oldCode = (InternalEObject)code;
			code = (DeployedOperation)eResolveProxy(oldCode);
			if (code != oldCode) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.AGGREGATED_STORAGE_ACCESS__CODE, oldCode, code));
			}
		}
		return code;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation basicGetCode() {
		return code;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCode(DeployedOperation newCode) {
		DeployedOperation oldCode = code;
		code = newCode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.AGGREGATED_STORAGE_ACCESS__CODE, oldCode, code));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<String> getSources() {
		if (sources == null) {
			sources = new EDataTypeUniqueEList<String>(String.class, this, ExecutionPackage.AGGREGATED_STORAGE_ACCESS__SOURCES);
		}
		return sources;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EDirection getDirection() {
		return direction;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDirection(EDirection newDirection) {
		EDirection oldDirection = direction;
		direction = newDirection == null ? DIRECTION_EDEFAULT : newDirection;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.AGGREGATED_STORAGE_ACCESS__DIRECTION, oldDirection, direction));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__STORAGE:
				if (resolve) return getStorage();
				return basicGetStorage();
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__CODE:
				if (resolve) return getCode();
				return basicGetCode();
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__SOURCES:
				return getSources();
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__DIRECTION:
				return getDirection();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__STORAGE:
				setStorage((DeployedStorage)newValue);
				return;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__CODE:
				setCode((DeployedOperation)newValue);
				return;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__SOURCES:
				getSources().clear();
				getSources().addAll((Collection<? extends String>)newValue);
				return;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__DIRECTION:
				setDirection((EDirection)newValue);
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
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__STORAGE:
				setStorage((DeployedStorage)null);
				return;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__CODE:
				setCode((DeployedOperation)null);
				return;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__SOURCES:
				getSources().clear();
				return;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__DIRECTION:
				setDirection(DIRECTION_EDEFAULT);
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
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__STORAGE:
				return storage != null;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__CODE:
				return code != null;
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__SOURCES:
				return sources != null && !sources.isEmpty();
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS__DIRECTION:
				return direction != DIRECTION_EDEFAULT;
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

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (sources: ");
		result.append(sources);
		result.append(", direction: ");
		result.append(direction);
		result.append(')');
		return result.toString();
	}

} //AggregatedStorageAccessImpl
