/**
 */
package kieker.model.analysismodel.assembly.impl;

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

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.type.ComponentType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getAssemblyOperations <em>Assembly Operations</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyComponentImpl#getComponentType <em>Component Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyComponentImpl extends MinimalEObjectImpl.Container implements AssemblyComponent {
	/**
	 * The cached value of the '{@link #getAssemblyOperations() <em>Assembly Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getAssemblyOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyOperation> assemblyOperations;

	/**
	 * The cached value of the '{@link #getComponentType() <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getComponentType()
	 * @generated
	 * @ordered
	 */
	protected ComponentType componentType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected AssemblyComponentImpl() {
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
		return AssemblyPackage.Literals.ASSEMBLY_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public EMap<String, AssemblyOperation> getAssemblyOperations() {
		if (assemblyOperations == null) {
			assemblyOperations = new EcoreEMap<String, AssemblyOperation>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY,
					EStringToAssemblyOperationMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS);
		}
		return assemblyOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public ComponentType getComponentType() {
		if (componentType != null && componentType.eIsProxy()) {
			InternalEObject oldComponentType = (InternalEObject) componentType;
			componentType = (ComponentType) eResolveProxy(oldComponentType);
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
	 * 
	 * @generated
	 */
	public ComponentType basicGetComponentType() {
		return componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public void setComponentType(ComponentType newComponentType) {
		ComponentType oldComponentType = componentType;
		componentType = newComponentType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE, oldComponentType, componentType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
		case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
			return ((InternalEList<?>) getAssemblyOperations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
		case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
			if (coreType)
				return getAssemblyOperations();
			else
				return getAssemblyOperations().map();
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			if (resolve)
				return getComponentType();
			return basicGetComponentType();
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
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
		case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
			((EStructuralFeature.Setting) getAssemblyOperations()).set(newValue);
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			setComponentType((ComponentType) newValue);
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
	public void eUnset(int featureID) {
		switch (featureID) {
		case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
			getAssemblyOperations().clear();
			return;
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			setComponentType((ComponentType) null);
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
	public boolean eIsSet(int featureID) {
		switch (featureID) {
		case AssemblyPackage.ASSEMBLY_COMPONENT__ASSEMBLY_OPERATIONS:
			return assemblyOperations != null && !assemblyOperations.isEmpty();
		case AssemblyPackage.ASSEMBLY_COMPONENT__COMPONENT_TYPE:
			return componentType != null;
		}
		return super.eIsSet(featureID);
	}

} // AssemblyComponentImpl
