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
 * <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getCaller <em>Caller</em>}</li>
 * <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getCallee <em>Callee</em>}</li>
 * <li>{@link kieker.model.collection.impl.OperationCollectionImpl#getOperations <em>Operations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationCollectionImpl extends MinimalEObjectImpl.Container implements OperationCollection {
	/**
	 * The cached value of the '{@link #getCaller() <em>Caller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCaller()
	 * @generated
	 * @ordered
	 */
	protected ComponentType caller;

	/**
	 * The cached value of the '{@link #getCallee() <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCallee()
	 * @generated
	 * @ordered
	 */
	protected ComponentType callee;

	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, OperationType> operations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OperationCollectionImpl() {
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
		return CollectionPackage.Literals.OPERATION_COLLECTION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComponentType getCaller() {
		if ((this.caller != null) && this.caller.eIsProxy()) {
			final InternalEObject oldCaller = (InternalEObject) this.caller;
			this.caller = (ComponentType) this.eResolveProxy(oldCaller);
			if (this.caller != oldCaller) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.OPERATION_COLLECTION__CALLER, oldCaller, this.caller));
				}
			}
		}
		return this.caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ComponentType basicGetCaller() {
		return this.caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCaller(final ComponentType newCaller) {
		final ComponentType oldCaller = this.caller;
		this.caller = newCaller;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.OPERATION_COLLECTION__CALLER, oldCaller, this.caller));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComponentType getCallee() {
		if ((this.callee != null) && this.callee.eIsProxy()) {
			final InternalEObject oldCallee = (InternalEObject) this.callee;
			this.callee = (ComponentType) this.eResolveProxy(oldCallee);
			if (this.callee != oldCallee) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.OPERATION_COLLECTION__CALLEE, oldCallee, this.callee));
				}
			}
		}
		return this.callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ComponentType basicGetCallee() {
		return this.callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCallee(final ComponentType newCallee) {
		final ComponentType oldCallee = this.callee;
		this.callee = newCallee;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.OPERATION_COLLECTION__CALLEE, oldCallee, this.callee));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, OperationType> getOperations() {
		if (this.operations == null) {
			this.operations = new EcoreEMap<>(CollectionPackage.Literals.NAME_TO_OPERATION_MAP, NameToOperationMapImpl.class, this,
					CollectionPackage.OPERATION_COLLECTION__OPERATIONS);
		}
		return this.operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
			return ((InternalEList<?>) this.getOperations()).basicRemove(otherEnd, msgs);
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
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case CollectionPackage.OPERATION_COLLECTION__CALLER:
			if (resolve) {
				return this.getCaller();
			}
			return this.basicGetCaller();
		case CollectionPackage.OPERATION_COLLECTION__CALLEE:
			if (resolve) {
				return this.getCallee();
			}
			return this.basicGetCallee();
		case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
			if (coreType) {
				return this.getOperations();
			} else {
				return this.getOperations().map();
			}
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
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case CollectionPackage.OPERATION_COLLECTION__CALLER:
			this.setCaller((ComponentType) newValue);
			return;
		case CollectionPackage.OPERATION_COLLECTION__CALLEE:
			this.setCallee((ComponentType) newValue);
			return;
		case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
			((EStructuralFeature.Setting) this.getOperations()).set(newValue);
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
	public void eUnset(final int featureID) {
		switch (featureID) {
		case CollectionPackage.OPERATION_COLLECTION__CALLER:
			this.setCaller((ComponentType) null);
			return;
		case CollectionPackage.OPERATION_COLLECTION__CALLEE:
			this.setCallee((ComponentType) null);
			return;
		case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
			this.getOperations().clear();
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
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case CollectionPackage.OPERATION_COLLECTION__CALLER:
			return this.caller != null;
		case CollectionPackage.OPERATION_COLLECTION__CALLEE:
			return this.callee != null;
		case CollectionPackage.OPERATION_COLLECTION__OPERATIONS:
			return (this.operations != null) && !this.operations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // OperationCollectionImpl
