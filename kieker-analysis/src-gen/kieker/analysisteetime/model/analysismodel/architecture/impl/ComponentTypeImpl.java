/**
 */
package kieker.analysisteetime.model.analysismodel.architecture.impl;

import java.util.Collection;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl#getPackageName <em>Package Name</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl#getArchitectureRoot <em>Architecture Root</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl#getProvidedOperations <em>Provided Operations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentTypeImpl extends MinimalEObjectImpl.Container implements ComponentType {
	/**
	 * The default value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
	protected static final String PACKAGE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPackageName() <em>Package Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageName()
	 * @generated
	 * @ordered
	 */
	protected String packageName = PACKAGE_NAME_EDEFAULT;

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
	 * The cached value of the '{@link #getArchitectureRoot() <em>Architecture Root</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArchitectureRoot()
	 * @generated
	 * @ordered
	 */
	protected ArchitectureRoot architectureRoot;

	/**
	 * The cached value of the '{@link #getProvidedOperations() <em>Provided Operations</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvidedOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<OperationType> providedOperations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ComponentTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ArchitecturePackage.Literals.COMPONENT_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getPackageName() {
		return packageName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPackageName(String newPackageName) {
		String oldPackageName = packageName;
		packageName = newPackageName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArchitecturePackage.COMPONENT_TYPE__PACKAGE_NAME, oldPackageName, packageName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArchitecturePackage.COMPONENT_TYPE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitectureRoot getArchitectureRoot() {
		if (architectureRoot != null && architectureRoot.eIsProxy()) {
			InternalEObject oldArchitectureRoot = (InternalEObject)architectureRoot;
			architectureRoot = (ArchitectureRoot)eResolveProxy(oldArchitectureRoot);
			if (architectureRoot != oldArchitectureRoot) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT, oldArchitectureRoot, architectureRoot));
			}
		}
		return architectureRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ArchitectureRoot basicGetArchitectureRoot() {
		return architectureRoot;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetArchitectureRoot(ArchitectureRoot newArchitectureRoot, NotificationChain msgs) {
		ArchitectureRoot oldArchitectureRoot = architectureRoot;
		architectureRoot = newArchitectureRoot;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT, oldArchitectureRoot, newArchitectureRoot);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setArchitectureRoot(ArchitectureRoot newArchitectureRoot) {
		if (newArchitectureRoot != architectureRoot) {
			NotificationChain msgs = null;
			if (architectureRoot != null)
				msgs = ((InternalEObject)architectureRoot).eInverseRemove(this, ArchitecturePackage.ARCHITECTURE_ROOT__COMPONENT_TYPES, ArchitectureRoot.class, msgs);
			if (newArchitectureRoot != null)
				msgs = ((InternalEObject)newArchitectureRoot).eInverseAdd(this, ArchitecturePackage.ARCHITECTURE_ROOT__COMPONENT_TYPES, ArchitectureRoot.class, msgs);
			msgs = basicSetArchitectureRoot(newArchitectureRoot, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT, newArchitectureRoot, newArchitectureRoot));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<OperationType> getProvidedOperations() {
		if (providedOperations == null) {
			providedOperations = new EObjectWithInverseResolvingEList<OperationType>(OperationType.class, this, ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS, ArchitecturePackage.OPERATION_TYPE__COMPONENT_TYPE);
		}
		return providedOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				if (architectureRoot != null)
					msgs = ((InternalEObject)architectureRoot).eInverseRemove(this, ArchitecturePackage.ARCHITECTURE_ROOT__COMPONENT_TYPES, ArchitectureRoot.class, msgs);
				return basicSetArchitectureRoot((ArchitectureRoot)otherEnd, msgs);
			case ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getProvidedOperations()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				return basicSetArchitectureRoot(null, msgs);
			case ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
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
			case ArchitecturePackage.COMPONENT_TYPE__PACKAGE_NAME:
				return getPackageName();
			case ArchitecturePackage.COMPONENT_TYPE__NAME:
				return getName();
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				if (resolve) return getArchitectureRoot();
				return basicGetArchitectureRoot();
			case ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				return getProvidedOperations();
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
			case ArchitecturePackage.COMPONENT_TYPE__PACKAGE_NAME:
				setPackageName((String)newValue);
				return;
			case ArchitecturePackage.COMPONENT_TYPE__NAME:
				setName((String)newValue);
				return;
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				setArchitectureRoot((ArchitectureRoot)newValue);
				return;
			case ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				getProvidedOperations().clear();
				getProvidedOperations().addAll((Collection<? extends OperationType>)newValue);
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
			case ArchitecturePackage.COMPONENT_TYPE__PACKAGE_NAME:
				setPackageName(PACKAGE_NAME_EDEFAULT);
				return;
			case ArchitecturePackage.COMPONENT_TYPE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				setArchitectureRoot((ArchitectureRoot)null);
				return;
			case ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				getProvidedOperations().clear();
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
			case ArchitecturePackage.COMPONENT_TYPE__PACKAGE_NAME:
				return PACKAGE_NAME_EDEFAULT == null ? packageName != null : !PACKAGE_NAME_EDEFAULT.equals(packageName);
			case ArchitecturePackage.COMPONENT_TYPE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				return architectureRoot != null;
			case ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS:
				return providedOperations != null && !providedOperations.isEmpty();
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

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (packageName: ");
		result.append(packageName);
		result.append(", name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //ComponentTypeImpl
