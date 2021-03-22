/**
 */
package kieker.model.analysismodel.deployment.impl;

import java.lang.reflect.InvocationTargetException;

import kieker.model.analysismodel.assembly.AssemblyStorage;

import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentPackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Storage</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedStorageImpl#getAssemblyOperation <em>Assembly Operation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedStorageImpl extends MinimalEObjectImpl.Container implements DeployedStorage {
	/**
	 * The cached value of the '{@link #getAssemblyOperation() <em>Assembly Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssemblyOperation()
	 * @generated
	 * @ordered
	 */
	protected AssemblyStorage assemblyOperation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedStorageImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_STORAGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyStorage getAssemblyOperation() {
		if (assemblyOperation != null && assemblyOperation.eIsProxy()) {
			InternalEObject oldAssemblyOperation = (InternalEObject)assemblyOperation;
			assemblyOperation = (AssemblyStorage)eResolveProxy(oldAssemblyOperation);
			if (assemblyOperation != oldAssemblyOperation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_OPERATION, oldAssemblyOperation, assemblyOperation));
			}
		}
		return assemblyOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyStorage basicGetAssemblyOperation() {
		return assemblyOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAssemblyOperation(AssemblyStorage newAssemblyOperation) {
		AssemblyStorage oldAssemblyOperation = assemblyOperation;
		assemblyOperation = newAssemblyOperation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_OPERATION, oldAssemblyOperation, assemblyOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeployedComponent getComponent() {
		org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (DeployedComponent) containerContainer ;
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
			case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_OPERATION:
				if (resolve) return getAssemblyOperation();
				return basicGetAssemblyOperation();
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
			case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_OPERATION:
				setAssemblyOperation((AssemblyStorage)newValue);
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
			case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_OPERATION:
				setAssemblyOperation((AssemblyStorage)null);
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
			case DeploymentPackage.DEPLOYED_STORAGE__ASSEMBLY_OPERATION:
				return assemblyOperation != null;
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
			case DeploymentPackage.DEPLOYED_STORAGE___GET_COMPONENT:
				return getComponent();
		}
		return super.eInvoke(operationID, arguments);
	}

} //DeployedStorageImpl
