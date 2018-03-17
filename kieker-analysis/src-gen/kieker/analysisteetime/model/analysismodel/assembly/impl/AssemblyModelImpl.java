/**
 */
package kieker.analysisteetime.model.analysismodel.assembly.impl;

import kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyModel;
import kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.assembly.impl.AssemblyModelImpl#getAssemblyComponents <em>Assembly Components</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyModelImpl extends MinimalEObjectImpl.Container implements AssemblyModel {
	/**
	 * The cached value of the '{@link #getAssemblyComponents() <em>Assembly Components</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAssemblyComponents()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, AssemblyComponent> assemblyComponents;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssemblyModelImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AssemblyPackage.Literals.ASSEMBLY_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EMap<String, AssemblyComponent> getAssemblyComponents() {
		if (assemblyComponents == null) {
			assemblyComponents = new EcoreEMap<String,AssemblyComponent>(AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY, EStringToAssemblyComponentMapEntryImpl.class, this, AssemblyPackage.ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS);
		}
		return assemblyComponents;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AssemblyPackage.ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS:
				return ((InternalEList<?>)getAssemblyComponents()).basicRemove(otherEnd, msgs);
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
			case AssemblyPackage.ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS:
				if (coreType) return getAssemblyComponents();
				else return getAssemblyComponents().map();
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
			case AssemblyPackage.ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS:
				((EStructuralFeature.Setting)getAssemblyComponents()).set(newValue);
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
			case AssemblyPackage.ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS:
				getAssemblyComponents().clear();
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
			case AssemblyPackage.ASSEMBLY_MODEL__ASSEMBLY_COMPONENTS:
				return assemblyComponents != null && !assemblyComponents.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //AssemblyModelImpl
