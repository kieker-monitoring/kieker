/**
 */
package kieker.model.analysismodel.execution.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.Tuple;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Operations Pair To Invocation Map Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToInvocationMapEntryImpl#getTypedValue <em>Value</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToInvocationMapEntryImpl#getTypedKey <em>Key</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedOperationsPairToInvocationMapEntryImpl extends MinimalEObjectImpl.Container
		implements BasicEMap.Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> {
	/**
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected Invocation value;

	/**
	 * The cached value of the '{@link #getTypedKey() <em>Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected Tuple<DeployedOperation, DeployedOperation> key;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeployedOperationsPairToInvocationMapEntryImpl() {
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
		return ExecutionPackage.Literals.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Invocation getTypedValue() {
		return this.value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetTypedValue(final Invocation newValue, NotificationChain msgs) {
		final Invocation oldValue = this.value;
		this.value = newValue;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE,
					oldValue, newValue);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setTypedValue(final Invocation newValue) {
		if (newValue != this.value) {
			NotificationChain msgs = null;
			if (this.value != null) {
				msgs = ((InternalEObject) this.value).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE, null, msgs);
			}
			if (newValue != null) {
				msgs = ((InternalEObject) newValue).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE, null, msgs);
			}
			msgs = this.basicSetTypedValue(newValue, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE, newValue, newValue));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Tuple<DeployedOperation, DeployedOperation> getTypedKey() {
		return this.key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetTypedKey(final Tuple<DeployedOperation, DeployedOperation> newKey, NotificationChain msgs) {
		final Tuple<DeployedOperation, DeployedOperation> oldKey = this.key;
		this.key = newKey;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
					ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY,
					oldKey, newKey);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setTypedKey(final Tuple<DeployedOperation, DeployedOperation> newKey) {
		if (newKey != this.key) {
			NotificationChain msgs = null;
			if (this.key != null) {
				msgs = ((InternalEObject) this.key).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY,
						null, msgs);
			}
			if (newKey != null) {
				msgs = ((InternalEObject) newKey).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY,
						null, msgs);
			}
			msgs = this.basicSetTypedKey(newKey, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY, newKey, newKey));
		}
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
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE:
			return this.basicSetTypedValue(null, msgs);
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY:
			return this.basicSetTypedKey(null, msgs);
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
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE:
			return this.getTypedValue();
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY:
			return this.getTypedKey();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE:
			this.setTypedValue((Invocation) newValue);
			return;
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY:
			this.setTypedKey((Tuple<DeployedOperation, DeployedOperation>) newValue);
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
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE:
			this.setTypedValue((Invocation) null);
			return;
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY:
			this.setTypedKey((Tuple<DeployedOperation, DeployedOperation>) null);
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
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE:
			return this.value != null;
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY:
			return this.key != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected int hash = -1;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getHash() {
		if (this.hash == -1) {
			final Object theKey = this.getKey();
			this.hash = (theKey == null ? 0 : theKey.hashCode());
		}
		return this.hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setHash(final int hash) {
		this.hash = hash;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Tuple<DeployedOperation, DeployedOperation> getKey() {
		return this.getTypedKey();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setKey(final Tuple<DeployedOperation, DeployedOperation> key) {
		this.setTypedKey(key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Invocation getValue() {
		return this.getTypedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Invocation setValue(final Invocation value) {
		final Invocation oldValue = this.getValue();
		this.setTypedValue(value);
		return oldValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation> getEMap() {
		final EObject container = this.eContainer();
		return container == null ? null : (EMap<Tuple<DeployedOperation, DeployedOperation>, Invocation>) container.eGet(this.eContainmentFeature());
	}

} // DeployedOperationsPairToInvocationMapEntryImpl
