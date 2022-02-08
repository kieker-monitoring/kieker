/**
 */
package kieker.model.collection.impl;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;

import kieker.model.collection.CollectionPackage;
import kieker.model.collection.Coupling;
import kieker.model.collection.OperationCollection;

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
 * An implementation of the model object '<em><b>Operation Collection</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getCaller <em>Caller</em>}</li>
 *   <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getCallee <em>Callee</em>}</li>
 *   <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getOperations <em>Operations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationCollectionImpl extends MinimalEObjectImpl.Container implements OperationCollection {
	/**
	 * The cached value of the '{@link #getCaller() <em>Caller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCaller()
	 * @generated
	 * @ordered
	 */
	protected ComponentType caller;

	/**
	 * The cached value of the '{@link #getCallee() <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCallee()
	 * @generated
	 * @ordered
	 */
	protected ComponentType callee;

	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<Coupling, EMap<String, OperationType>> operations;

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
	public ComponentType getCaller() {
		if (caller != null && caller.eIsProxy()) {
			InternalEObject oldCaller = (InternalEObject)caller;
			caller = (ComponentType)eResolveProxy(oldCaller);
			if (caller != oldCaller) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.OPERATION_COLLECTION__CALLER, oldCaller, caller));
			}
		}
		return caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetCaller() {
		return caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCaller(ComponentType newCaller) {
		ComponentType oldCaller = caller;
		caller = newCaller;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.OPERATION_COLLECTION__CALLER, oldCaller, caller));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComponentType getCallee() {
		if (callee != null && callee.eIsProxy()) {
			InternalEObject oldCallee = (InternalEObject)callee;
			callee = (ComponentType)eResolveProxy(oldCallee);
			if (callee != oldCallee) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.OPERATION_COLLECTION__CALLEE, oldCallee, callee));
			}
		}
		return callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetCallee() {
		return callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCallee(ComponentType newCallee) {
		ComponentType oldCallee = callee;
		callee = newCallee;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.OPERATION_COLLECTION__CALLEE, oldCallee, callee));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Coupling, EMap<String, OperationType>> getOperations() {
		if (operations == null) {
			operations = new EcoreEMap<Coupling,EMap<String, OperationType>>(CollectionPackage.Literals.COUPLING_TO_OPERATION_MAP, CouplingToOperationMapImpl.class, this, CollectionPackage.OPERATION_COLLECTION__OPERATIONS);
		}
		return operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
				return ((InternalEList<?>)getOperations()).basicRemove(otherEnd, msgs);
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
			case CollectionPackage.OPERATION_COLLECTION__CALLER:
				if (resolve) return getCaller();
				return basicGetCaller();
			case CollectionPackage.OPERATION_COLLECTION__CALLEE:
				if (resolve) return getCallee();
				return basicGetCallee();
			case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
				if (coreType) return getOperations();
				else return getOperations().map();
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
			case CollectionPackage.OPERATION_COLLECTION__CALLER:
				setCaller((ComponentType)newValue);
				return;
			case CollectionPackage.OPERATION_COLLECTION__CALLEE:
				setCallee((ComponentType)newValue);
				return;
			case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
				((EStructuralFeature.Setting)getOperations()).set(newValue);
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
			case CollectionPackage.OPERATION_COLLECTION__CALLER:
				setCaller((ComponentType)null);
				return;
			case CollectionPackage.OPERATION_COLLECTION__CALLEE:
				setCallee((ComponentType)null);
				return;
			case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
				getOperations().clear();
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
			case CollectionPackage.OPERATION_COLLECTION__CALLER:
				return caller != null;
			case CollectionPackage.OPERATION_COLLECTION__CALLEE:
				return callee != null;
			case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
				return operations != null && !operations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //OperationCollectionImpl
