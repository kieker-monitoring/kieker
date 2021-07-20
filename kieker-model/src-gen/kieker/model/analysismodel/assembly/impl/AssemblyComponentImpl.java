/**
 */
package kieker.model.analysismodel.assembly.impl;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyStorage;

import kieker.model.analysismodel.type.ComponentType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

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
 * An implementation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getAssemblyOperations <em>Assembly Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getComponentType <em>Component Type</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getAssemblyStorages <em>Assembly Storages</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getSignature <em>Signature</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyComponentImpl extends MinimalEObjectImpl.Container implements AssemblyComponent {
	/**
	 * The cached value of the '{@link #getAssemblyOperations() <em>Assembly Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssemblyOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyOperation> assemblyOperations;

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
	 * The cached value of the '{@link #getAssemblyStorages() <em>Assembly Storages</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssemblyStorages()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyStorage> assemblyStorages;

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
	public EMap<String, AssemblyOperation> getAssemblyOperations() {
		if (assemblyOperations == null) {
			assemblyOperations = new EcoreEMap<String,AssemblyOperation>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY, EStringToAssemblyOperationMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS);
		}
		return assemblyOperations;
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
	public EMap<String, AssemblyStorage> getAssemblyStorages() {
		if (assemblyStorages == null) {
			assemblyStorages = new EcoreEMap<String,AssemblyStorage>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY, EStringToAssemblyStorageMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_STORAGES);
		}
		return assemblyStorages;
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
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE, oldSignature, signature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
				return ((InternalEList<?>)getAssemblyOperations()).basicRemove(otherEnd, msgs);
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_STORAGES:
				return ((InternalEList<?>)getAssemblyStorages()).basicRemove(otherEnd, msgs);
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
				if (coreType) return getAssemblyOperations();
				else return getAssemblyOperations().map();
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				if (resolve) return getComponentType();
				return basicGetComponentType();
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_STORAGES:
				if (coreType) return getAssemblyStorages();
				else return getAssemblyStorages().map();
			case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
				((EStructuralFeature.Setting)getAssemblyOperations()).set(newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				setComponentType((ComponentType)newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_STORAGES:
				((EStructuralFeature.Setting)getAssemblyStorages()).set(newValue);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
				getAssemblyOperations().clear();
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				setComponentType((ComponentType)null);
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_STORAGES:
				getAssemblyStorages().clear();
				return;
			case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
				return assemblyOperations != null && !assemblyOperations.isEmpty();
			case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
				return componentType != null;
			case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_STORAGES:
				return assemblyStorages != null && !assemblyStorages.isEmpty();
			case AssemblyPackage.ASSEMBLY_COMPONENT__SIGNATURE:
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
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (signature: ");
		result.append(signature);
		result.append(')');
		return result.toString();
	}

} //AssemblyComponentImpl
