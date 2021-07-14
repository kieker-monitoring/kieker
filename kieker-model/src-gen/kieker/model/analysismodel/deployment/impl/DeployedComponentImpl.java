/**
 */
package kieker.model.analysismodel.deployment.impl;

import java.lang.reflect.InvocationTargetException;

import kieker.model.analysismodel.assembly.AssemblyComponent;

import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getAssemblyComponent <em>Assembly Component</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getContainedOperations <em>Contained Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getContainedStorages <em>Contained Storages</em>}</li>
 *   <li>{@link kieker.model.analysismodel.deployment.impl.DeployedComponentImpl#getSignature <em>Signature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedComponentImpl extends MinimalEObjectImpl.Container implements DeployedComponent {
	/**
	 * The cached value of the '{@link #getAssemblyComponent() <em>Assembly Component</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssemblyComponent()
	 * @generated
	 * @ordered
	 */
	protected AssemblyComponent assemblyComponent;

	/**
	 * The cached value of the '{@link #getContainedOperations() <em>Contained Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedOperation> containedOperations;

	/**
	 * The cached value of the '{@link #getContainedStorages() <em>Contained Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, DeployedStorage> containedStorages;

	/**
	 * The default value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected static final String SIGNATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected String signature = SIGNATURE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyComponent getAssemblyComponent() {
		if (assemblyComponent != null && assemblyComponent.eIsProxy()) {
			InternalEObject oldAssemblyComponent = (InternalEObject)assemblyComponent;
			assemblyComponent = (AssemblyComponent)eResolveProxy(oldAssemblyComponent);
			if (assemblyComponent != oldAssemblyComponent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT, oldAssemblyComponent, assemblyComponent));
			}
		}
		return assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyComponent basicGetAssemblyComponent() {
		return assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAssemblyComponent(AssemblyComponent newAssemblyComponent) {
		AssemblyComponent oldAssemblyComponent = assemblyComponent;
		assemblyComponent = newAssemblyComponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT, oldAssemblyComponent, assemblyComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, DeployedOperation> getContainedOperations() {
		if (containedOperations == null) {
			containedOperations = new EcoreEMap<String,DeployedOperation>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_OPERATION_MAP_ENTRY, EStringToDeployedOperationMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS);
		}
		return containedOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, DeployedStorage> getContainedStorages() {
		if (containedStorages == null) {
			containedStorages = new EcoreEMap<String,DeployedStorage>(DeploymentPackage.Literals.ESTRING_TO_DEPLOYED_STORAGE_MAP_ENTRY, EStringToDeployedStorageMapEntryImpl.class, this, DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_STORAGES);
		}
		return containedStorages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSignature() {
		return signature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSignature(String newSignature) {
		String oldSignature = signature;
		signature = newSignature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE, oldSignature, signature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeploymentContext getDeploymentContext() {
		org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (DeploymentContext) containerContainer ;
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
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				return ((InternalEList<?>)getContainedOperations()).basicRemove(otherEnd, msgs);
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_STORAGES:
				return ((InternalEList<?>)getContainedStorages()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				if (resolve) return getAssemblyComponent();
				return basicGetAssemblyComponent();
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				if (coreType) return getContainedOperations();
				else return getContainedOperations().map();
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_STORAGES:
				if (coreType) return getContainedStorages();
				else return getContainedStorages().map();
			case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
				return getSignature();
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
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				setAssemblyComponent((AssemblyComponent)newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				((EStructuralFeature.Setting)getContainedOperations()).set(newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_STORAGES:
				((EStructuralFeature.Setting)getContainedStorages()).set(newValue);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
				setSignature((String)newValue);
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
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				setAssemblyComponent((AssemblyComponent)null);
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				getContainedOperations().clear();
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_STORAGES:
				getContainedStorages().clear();
				return;
			case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
				setSignature(SIGNATURE_EDEFAULT);
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
			case DeploymentPackage.DEPLOYED_COMPONENT__ASSEMBLY_COMPONENT:
				return assemblyComponent != null;
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_OPERATIONS:
				return containedOperations != null && !containedOperations.isEmpty();
			case DeploymentPackage.DEPLOYED_COMPONENT__CONTAINED_STORAGES:
				return containedStorages != null && !containedStorages.isEmpty();
			case DeploymentPackage.DEPLOYED_COMPONENT__SIGNATURE:
				return SIGNATURE_EDEFAULT == null ? signature != null : !SIGNATURE_EDEFAULT.equals(signature);
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
			case DeploymentPackage.DEPLOYED_COMPONENT___GET_DEPLOYMENT_CONTEXT:
				return getDeploymentContext();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (signature: ");
		result.append(signature);
		result.append(')');
		return result.toString();
	}

} //DeployedComponentImpl
