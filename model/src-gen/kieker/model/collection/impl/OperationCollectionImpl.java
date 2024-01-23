/**
 */
package kieker.model.collection.impl;

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

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.collection.CollectionPackage;
import kieker.model.collection.OperationCollection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getRequired <em>Required</em>}</li>
 *   <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getProvided <em>Provided</em>}</li>
 *   <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getCallees <em>Callees</em>}</li>
 *   <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getCallers <em>Callers</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationCollectionImpl extends MinimalEObjectImpl.Container implements OperationCollection {
	/**
	 * The cached value of the '{@link #getRequired() <em>Required</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequired()
	 * @generated
	 * @ordered
	 */
	protected ComponentType required;

	/**
	 * The cached value of the '{@link #getProvided() <em>Provided</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvided()
	 * @generated
	 * @ordered
	 */
	protected ComponentType provided;

	/**
	 * The cached value of the '{@link #getCallees() <em>Callees</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallees()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, OperationType> callees;

	/**
	 * The cached value of the '{@link #getCallers() <em>Callers</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallers()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, OperationType> callers;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationCollectionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CollectionPackage.Literals.OPERATION_COLLECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComponentType getRequired() {
		if (required != null && required.eIsProxy()) {
			InternalEObject oldRequired = (InternalEObject)required;
			required = (ComponentType)eResolveProxy(oldRequired);
			if (required != oldRequired) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.OPERATION_COLLECTION__REQUIRED, oldRequired, required));
			}
		}
		return required;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetRequired() {
		return required;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequired(ComponentType newRequired) {
		ComponentType oldRequired = required;
		required = newRequired;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.OPERATION_COLLECTION__REQUIRED, oldRequired, required));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComponentType getProvided() {
		if (provided != null && provided.eIsProxy()) {
			InternalEObject oldProvided = (InternalEObject)provided;
			provided = (ComponentType)eResolveProxy(oldProvided);
			if (provided != oldProvided) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.OPERATION_COLLECTION__PROVIDED, oldProvided, provided));
			}
		}
		return provided;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetProvided() {
		return provided;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProvided(ComponentType newProvided) {
		ComponentType oldProvided = provided;
		provided = newProvided;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.OPERATION_COLLECTION__PROVIDED, oldProvided, provided));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, OperationType> getCallees() {
		if (callees == null) {
			callees = new EcoreEMap<String,OperationType>(CollectionPackage.Literals.NAME_TO_OPERATION_MAP, NameToOperationMapImpl.class, this, CollectionPackage.OPERATION_COLLECTION__CALLEES);
		}
		return callees;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<String, OperationType> getCallers() {
		if (callers == null) {
			callers = new EcoreEMap<String,OperationType>(CollectionPackage.Literals.NAME_TO_OPERATION_MAP, NameToOperationMapImpl.class, this, CollectionPackage.OPERATION_COLLECTION__CALLERS);
		}
		return callers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CollectionPackage.OPERATION_COLLECTION__CALLEES:
				return ((InternalEList<?>)getCallees()).basicRemove(otherEnd, msgs);
			case CollectionPackage.OPERATION_COLLECTION__CALLERS:
				return ((InternalEList<?>)getCallers()).basicRemove(otherEnd, msgs);
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
			case CollectionPackage.OPERATION_COLLECTION__REQUIRED:
				if (resolve) return getRequired();
				return basicGetRequired();
			case CollectionPackage.OPERATION_COLLECTION__PROVIDED:
				if (resolve) return getProvided();
				return basicGetProvided();
			case CollectionPackage.OPERATION_COLLECTION__CALLEES:
				if (coreType) return getCallees();
				else return getCallees().map();
			case CollectionPackage.OPERATION_COLLECTION__CALLERS:
				if (coreType) return getCallers();
				else return getCallers().map();
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
			case CollectionPackage.OPERATION_COLLECTION__REQUIRED:
				setRequired((ComponentType)newValue);
				return;
			case CollectionPackage.OPERATION_COLLECTION__PROVIDED:
				setProvided((ComponentType)newValue);
				return;
			case CollectionPackage.OPERATION_COLLECTION__CALLEES:
				((EStructuralFeature.Setting)getCallees()).set(newValue);
				return;
			case CollectionPackage.OPERATION_COLLECTION__CALLERS:
				((EStructuralFeature.Setting)getCallers()).set(newValue);
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
			case CollectionPackage.OPERATION_COLLECTION__REQUIRED:
				setRequired((ComponentType)null);
				return;
			case CollectionPackage.OPERATION_COLLECTION__PROVIDED:
				setProvided((ComponentType)null);
				return;
			case CollectionPackage.OPERATION_COLLECTION__CALLEES:
				getCallees().clear();
				return;
			case CollectionPackage.OPERATION_COLLECTION__CALLERS:
				getCallers().clear();
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
			case CollectionPackage.OPERATION_COLLECTION__REQUIRED:
				return required != null;
			case CollectionPackage.OPERATION_COLLECTION__PROVIDED:
				return provided != null;
			case CollectionPackage.OPERATION_COLLECTION__CALLEES:
				return callees != null && !callees.isEmpty();
			case CollectionPackage.OPERATION_COLLECTION__CALLERS:
				return callers != null && !callers.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // OperationCollectionImpl
