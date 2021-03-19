/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.lang.reflect.InvocationTargetException;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyStorage;

import kieker.model.analysismodel.type.StorageType;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Storage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyStorageImpl#getOperationType <em>Operation Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyStorageImpl extends MinimalEObjectImpl.Container implements AssemblyStorage {
	/**
	 * The cached value of the '{@link #getOperationType() <em>Operation Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationType()
	 * @generated
	 * @ordered
	 */
	protected StorageType operationType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssemblyStorageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AssemblyPackage.Literals.ASSEMBLY_STORAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StorageType getOperationType() {
		if (operationType != null && operationType.eIsProxy()) {
			InternalEObject oldOperationType = (InternalEObject)operationType;
			operationType = (StorageType)eResolveProxy(oldOperationType);
			if (operationType != oldOperationType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_STORAGE__OPERATION_TYPE, oldOperationType, operationType));
			}
		}
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StorageType basicGetOperationType() {
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperationType(StorageType newOperationType) {
		StorageType oldOperationType = operationType;
		operationType = newOperationType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_STORAGE__OPERATION_TYPE, oldOperationType, operationType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyComponent getAssemblyComponent() {
		org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (AssemblyComponent) containerContainer ;
			}
		}
		return null;
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case AssemblyPackage.ASSEMBLY_STORAGE__OPERATION_TYPE:
				if (resolve) return getOperationType();
				return basicGetOperationType();
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
			case AssemblyPackage.ASSEMBLY_STORAGE__OPERATION_TYPE:
				setOperationType((StorageType)newValue);
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
			case AssemblyPackage.ASSEMBLY_STORAGE__OPERATION_TYPE:
				setOperationType((StorageType)null);
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
			case AssemblyPackage.ASSEMBLY_STORAGE__OPERATION_TYPE:
				return operationType != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case AssemblyPackage.ASSEMBLY_STORAGE___GET_ASSEMBLY_COMPONENT:
				return getAssemblyComponent();
		}
		return super.eInvoke(operationID, arguments);
	}

} //AssemblyStorageImpl
