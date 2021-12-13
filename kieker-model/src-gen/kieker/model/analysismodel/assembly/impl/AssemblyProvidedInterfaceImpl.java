/**
 */
package kieker.model.analysismodel.assembly.impl;

import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;

import kieker.model.analysismodel.type.ProvidedInterfaceType;

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
 * An implementation of the model object '<em><b>Provided Interface</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl#getProvidedOperations <em>Provided Operations</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl#getProvidedInterfaceType <em>Provided Interface Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyProvidedInterfaceImpl extends MinimalEObjectImpl.Container implements AssemblyProvidedInterface {
	/**
	 * The cached value of the '{@link #getProvidedOperations() <em>Provided Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyOperation> providedOperations;

	/**
	 * The cached value of the '{@link #getProvidedInterfaceType() <em>Provided Interface Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedInterfaceType()
	 * @generated
	 * @ordered
	 */
	protected ProvidedInterfaceType providedInterfaceType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssemblyProvidedInterfaceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AssemblyPackage.Literals.ASSEMBLY_PROVIDED_INTERFACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, AssemblyOperation> getProvidedOperations() {
		if (providedOperations == null) {
			providedOperations = new EcoreEMap<String,AssemblyOperation>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY, EStringToAssemblyOperationMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS);
		}
		return providedOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType getProvidedInterfaceType() {
		if (providedInterfaceType != null && providedInterfaceType.eIsProxy()) {
			InternalEObject oldProvidedInterfaceType = (InternalEObject)providedInterfaceType;
			providedInterfaceType = (ProvidedInterfaceType)eResolveProxy(oldProvidedInterfaceType);
			if (providedInterfaceType != oldProvidedInterfaceType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE, oldProvidedInterfaceType, providedInterfaceType));
			}
		}
		return providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProvidedInterfaceType basicGetProvidedInterfaceType() {
		return providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProvidedInterfaceType(ProvidedInterfaceType newProvidedInterfaceType) {
		ProvidedInterfaceType oldProvidedInterfaceType = providedInterfaceType;
		providedInterfaceType = newProvidedInterfaceType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE, oldProvidedInterfaceType, providedInterfaceType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS:
				return ((InternalEList<?>)getProvidedOperations()).basicRemove(otherEnd, msgs);
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS:
				if (coreType) return getProvidedOperations();
				else return getProvidedOperations().map();
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				if (resolve) return getProvidedInterfaceType();
				return basicGetProvidedInterfaceType();
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS:
				((EStructuralFeature.Setting)getProvidedOperations()).set(newValue);
				return;
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				setProvidedInterfaceType((ProvidedInterfaceType)newValue);
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS:
				getProvidedOperations().clear();
				return;
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				setProvidedInterfaceType((ProvidedInterfaceType)null);
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_OPERATIONS:
				return providedOperations != null && !providedOperations.isEmpty();
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__PROVIDED_INTERFACE_TYPE:
				return providedInterfaceType != null;
		}
		return super.eIsSet(featureID);
	}

} //AssemblyProvidedInterfaceImpl
