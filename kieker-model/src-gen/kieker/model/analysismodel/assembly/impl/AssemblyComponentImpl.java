/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.util.Collection;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.assembly.AssemblyStorage;

import kieker.model.analysismodel.type.ComponentType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getOperations <em>Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getComponentType <em>Component Type</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getStorages <em>Storages</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getContainedComponents <em>Contained Components</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getProvidedInterfaces <em>Provided Interfaces</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getRequiredInterfaces <em>Required Interfaces</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyComponentImpl extends MinimalEObjectImpl.Container implements AssemblyComponent {
	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyOperation> operations;

	/**
	 * The cached value of the '{@link #getComponentType() <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentType()
	 * @generated
	 * @ordered
	 */
	protected ComponentType componentType;

	/**
	 * The cached value of the '{@link #getStorages() <em>Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyStorage> storages;

	/**
	 * The cached value of the '{@link #getContainedComponents() <em>Contained Components</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedComponents()
	 * @generated
	 * @ordered
	 */
	protected EList<AssemblyComponent> containedComponents;

	/**
	 * The cached value of the '{@link #getProvidedInterfaces() <em>Provided Interfaces</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyProvidedInterface> providedInterfaces;

	/**
	 * The cached value of the '{@link #getRequiredInterfaces() <em>Required Interfaces</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequiredInterfaces()
	 * @generated
	 * @ordered
	 */
	protected EList<AssemblyRequiredInterface> requiredInterfaces;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssemblyComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AssemblyPackage.Literals.ASSEMBLY_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, AssemblyOperation> getOperations() {
		if (operations == null) {
			operations = new EcoreEMap<String,AssemblyOperation>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY, EStringToAssemblyOperationMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS);
		}
		return operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComponentType getComponentType() {
		if (componentType != null && componentType.eIsProxy()) {
			InternalEObject oldComponentType = (InternalEObject)componentType;
			componentType = (ComponentType)eResolveProxy(oldComponentType);
			if (componentType != oldComponentType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE, oldComponentType, componentType));
			}
		}
		return componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetComponentType() {
		return componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setComponentType(ComponentType newComponentType) {
		ComponentType oldComponentType = componentType;
		componentType = newComponentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE, oldComponentType, componentType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, AssemblyStorage> getStorages() {
		if (storages == null) {
			storages = new EcoreEMap<String,AssemblyStorage>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY, EStringToAssemblyStorageMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES);
		}
		return storages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AssemblyComponent> getContainedComponents() {
		if (containedComponents == null) {
			containedComponents = new EObjectResolvingEList<AssemblyComponent>(AssemblyComponent.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS);
		}
		return containedComponents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, AssemblyProvidedInterface> getProvidedInterfaces() {
		if (providedInterfaces == null) {
			providedInterfaces = new EcoreEMap<String,AssemblyProvidedInterface>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY, EStringToAssemblyProvidedInterfaceMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES);
		}
		return providedInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<AssemblyRequiredInterface> getRequiredInterfaces() {
		if (requiredInterfaces == null) {
			requiredInterfaces = new EObjectContainmentEList<AssemblyRequiredInterface>(AssemblyRequiredInterface.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES);
		}
		return requiredInterfaces;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
				return ((InternalEList<?>)getOperations()).basicRemove(otherEnd, msgs);
			case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
				return ((InternalEList<?>)getStorages()).basicRemove(otherEnd, msgs);
			case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
				return ((InternalEList<?>)getProvidedInterfaces()).basicRemove(otherEnd, msgs);
			case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
				return ((InternalEList<?>)getRequiredInterfaces()).basicRemove(otherEnd, msgs);
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
				if (coreType) return getOperations();
				else return getOperations().map();
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				if (resolve) return getComponentType();
				return basicGetComponentType();
			case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
				if (coreType) return getStorages();
				else return getStorages().map();
			case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
				return getContainedComponents();
			case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
				if (coreType) return getProvidedInterfaces();
				else return getProvidedInterfaces().map();
			case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
				return getRequiredInterfaces();
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
				((EStructuralFeature.Setting)getOperations()).set(newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				setComponentType((ComponentType)newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
				((EStructuralFeature.Setting)getStorages()).set(newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
				getContainedComponents().clear();
				getContainedComponents().addAll((Collection<? extends AssemblyComponent>)newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
				((EStructuralFeature.Setting)getProvidedInterfaces()).set(newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
				getRequiredInterfaces().clear();
				getRequiredInterfaces().addAll((Collection<? extends AssemblyRequiredInterface>)newValue);
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
				getOperations().clear();
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				setComponentType((ComponentType)null);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
				getStorages().clear();
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
				getContainedComponents().clear();
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
				getProvidedInterfaces().clear();
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
				getRequiredInterfaces().clear();
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__OPERATIONS:
				return operations != null && !operations.isEmpty();
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				return componentType != null;
			case AssemblyPackage.ASSEMBLY_COMPONENT__STORAGES:
				return storages != null && !storages.isEmpty();
			case AssemblyPackage.ASSEMBLY_COMPONENT__CONTAINED_COMPONENTS:
				return containedComponents != null && !containedComponents.isEmpty();
			case AssemblyPackage.ASSEMBLY_COMPONENT__PROVIDED_INTERFACES:
				return providedInterfaces != null && !providedInterfaces.isEmpty();
			case AssemblyPackage.ASSEMBLY_COMPONENT__REQUIRED_INTERFACES:
				return requiredInterfaces != null && !requiredInterfaces.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AssemblyComponentImpl
