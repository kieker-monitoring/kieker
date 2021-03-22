/**
 */
package kieker.model.analysismodel.execution.impl;

import kieker.model.analysismodel.deployment.DeployedOperation;

import kieker.model.analysismodel.execution.AggregatedStorageAccess;
import kieker.model.analysismodel.execution.ExecutionPackage;

import kieker.model.analysismodel.util.ComposedKey;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Operations Pair To Aggregated Storage Access Map Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl#getTypedKey <em>Key</em>}</li>
 *   <li>{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl extends MinimalEObjectImpl.Container implements BasicEMap.Entry<ComposedKey<DeployedOperation, DeployedOperation>,AggregatedStorageAccess> {
	/**
	 * The cached value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected ComposedKey<DeployedOperation, DeployedOperation> key;

	/**
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected AggregatedStorageAccess value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComposedKey<DeployedOperation, DeployedOperation> getTypedKey() {
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypedKey(ComposedKey<DeployedOperation, DeployedOperation> newKey) {
		ComposedKey<DeployedOperation, DeployedOperation> oldKey = key;
		key = newKey;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY, oldKey, key));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AggregatedStorageAccess getTypedValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTypedValue(AggregatedStorageAccess newValue, NotificationChain msgs) {
		AggregatedStorageAccess oldValue = value;
		value = newValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE, oldValue, newValue);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTypedValue(AggregatedStorageAccess newValue) {
		if (newValue != value) {
			NotificationChain msgs = null;
			if (value != null)
				msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE, null, msgs);
			if (newValue != null)
				msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE, null, msgs);
			msgs = basicSetTypedValue(newValue, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE, newValue, newValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE:
				return basicSetTypedValue(null, msgs);
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
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY:
				return getTypedKey();
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE:
				return getTypedValue();
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
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY:
				setTypedKey((ComposedKey<DeployedOperation, DeployedOperation>)newValue);
				return;
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE:
				setTypedValue((AggregatedStorageAccess)newValue);
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
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY:
				setTypedKey((ComposedKey<DeployedOperation, DeployedOperation>)null);
				return;
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE:
				setTypedValue((AggregatedStorageAccess)null);
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
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY:
				return key != null;
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE:
				return value != null;
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
		result.append(" (key: ");
		result.append(key);
		result.append(')');
		return result.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected int hash = -1;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getHash() {
		if (hash == -1) {
			Object theKey = getKey();
			hash = (theKey == null ? 0 : theKey.hashCode());
		}
		return hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setHash(int hash) {
		this.hash = hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComposedKey<DeployedOperation, DeployedOperation> getKey() {
		return getTypedKey();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKey(ComposedKey<DeployedOperation, DeployedOperation> key) {
		setTypedKey(key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AggregatedStorageAccess getValue() {
		return getTypedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AggregatedStorageAccess setValue(AggregatedStorageAccess value) {
		AggregatedStorageAccess oldValue = getValue();
		setTypedValue(value);
		return oldValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EMap<ComposedKey<DeployedOperation, DeployedOperation>, AggregatedStorageAccess> getEMap() {
		EObject container = eContainer();
		return container == null ? null : (EMap<ComposedKey<DeployedOperation, DeployedOperation>, AggregatedStorageAccess>)container.eGet(eContainmentFeature());
	}

} //DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl
