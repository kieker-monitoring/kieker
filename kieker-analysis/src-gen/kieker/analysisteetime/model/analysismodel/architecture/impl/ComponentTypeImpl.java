/**
 */
package kieker.analysisteetime.model.analysismodel.architecture.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage;
import kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot;
import kieker.analysisteetime.model.analysismodel.architecture.ComponentType;
import kieker.analysisteetime.model.analysismodel.architecture.OperationType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Component Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl#getSignature <em>Signature</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl#getArchitectureRoot <em>Architecture Root</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl#getProvidedOperations <em>Provided Operations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentTypeImpl extends MinimalEObjectImpl.Container implements ComponentType {
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
	 * The cached value of the '{@link #getProvidedOperations() <em>Provided Operations</em>}' containment reference list.
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
			eNotify(new ENotificationImpl(this, Notification.SET, ArchitecturePackage.COMPONENT_TYPE__SIGNATURE, oldSignature, signature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ArchitectureRoot getArchitectureRoot() {
		if (eContainerFeatureID() != ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT) return null;
		return (ArchitectureRoot)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetArchitectureRoot(ArchitectureRoot newArchitectureRoot, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newArchitectureRoot, ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setArchitectureRoot(ArchitectureRoot newArchitectureRoot) {
		if (newArchitectureRoot != eInternalContainer() || (eContainerFeatureID() != ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT && newArchitectureRoot != null)) {
			if (EcoreUtil.isAncestor(this, newArchitectureRoot))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
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
	@Override
	public EList<OperationType> getProvidedOperations() {
		if (providedOperations == null) {
			providedOperations = new EObjectContainmentWithInverseEList<OperationType>(OperationType.class, this, ArchitecturePackage.COMPONENT_TYPE__PROVIDED_OPERATIONS, ArchitecturePackage.OPERATION_TYPE__COMPONENT_TYPE);
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
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
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
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				return eInternalContainer().eInverseRemove(this, ArchitecturePackage.ARCHITECTURE_ROOT__COMPONENT_TYPES, ArchitectureRoot.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ArchitecturePackage.COMPONENT_TYPE__SIGNATURE:
				return getSignature();
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				return getArchitectureRoot();
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
			case ArchitecturePackage.COMPONENT_TYPE__SIGNATURE:
				setSignature((String)newValue);
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
			case ArchitecturePackage.COMPONENT_TYPE__SIGNATURE:
				setSignature(SIGNATURE_EDEFAULT);
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
			case ArchitecturePackage.COMPONENT_TYPE__SIGNATURE:
				return SIGNATURE_EDEFAULT == null ? signature != null : !SIGNATURE_EDEFAULT.equals(signature);
			case ArchitecturePackage.COMPONENT_TYPE__ARCHITECTURE_ROOT:
				return getArchitectureRoot() != null;
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
		result.append(" (signature: ");
		result.append(signature);
		result.append(')');
		return result.toString();
	}

} // ComponentTypeImpl
