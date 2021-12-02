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
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl#getName <em>Name</em>}</li>
 *   <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyProvidedInterfaceImpl#getDeclaration <em>Declaration</em>}</li>
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
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getDeclaration() <em>Declaration</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeclaration()
	 * @generated
	 * @ordered
	 */
	protected ProvidedInterfaceType declaration;

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
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType getDeclaration() {
		if (declaration != null && declaration.eIsProxy()) {
			InternalEObject oldDeclaration = (InternalEObject)declaration;
			declaration = (ProvidedInterfaceType)eResolveProxy(oldDeclaration);
			if (declaration != oldDeclaration) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__DECLARATION, oldDeclaration, declaration));
			}
		}
		return declaration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ProvidedInterfaceType basicGetDeclaration() {
		return declaration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDeclaration(ProvidedInterfaceType newDeclaration) {
		ProvidedInterfaceType oldDeclaration = declaration;
		declaration = newDeclaration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__DECLARATION, oldDeclaration, declaration));
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__NAME:
				return getName();
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__DECLARATION:
				if (resolve) return getDeclaration();
				return basicGetDeclaration();
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__NAME:
				setName((String)newValue);
				return;
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__DECLARATION:
				setDeclaration((ProvidedInterfaceType)newValue);
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__DECLARATION:
				setDeclaration((ProvidedInterfaceType)null);
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
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE__DECLARATION:
				return declaration != null;
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //AssemblyProvidedInterfaceImpl
