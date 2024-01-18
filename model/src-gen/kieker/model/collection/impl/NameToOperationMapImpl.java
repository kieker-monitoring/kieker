/**
 */
package kieker.model.collection.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.type.OperationType;
import kieker.model.collection.CollectionPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Name To Operation Map</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.collection.impl.NameToOperationMapImpl#getTypedKey <em>Key</em>}</li>
 * <li>{@link kieker.model.collection.impl.NameToOperationMapImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NameToOperationMapImpl extends MinimalEObjectImpl.Container implements BasicEMap.Entry<String, OperationType> {
	/**
	 * The default value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected static final String KEY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTypedKey() <em>Key</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected String key = KEY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected OperationType value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected NameToOperationMapImpl() {
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
		return CollectionPackage.Literals.NAME_TO_OPERATION_MAP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getTypedKey() {
		return this.key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setTypedKey(final String newKey) {
		final String oldKey = this.key;
		this.key = newKey;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.NAME_TO_OPERATION_MAP__KEY, oldKey, this.key));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public OperationType getTypedValue() {
		if ((this.value != null) && this.value.eIsProxy()) {
			final InternalEObject oldValue = (InternalEObject) this.value;
			this.value = (OperationType) this.eResolveProxy(oldValue);
			if (this.value != oldValue) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.NAME_TO_OPERATION_MAP__VALUE, oldValue, this.value));
				}
			}
		}
		return this.value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public OperationType basicGetTypedValue() {
		return this.value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setTypedValue(final OperationType newValue) {
		final OperationType oldValue = this.value;
		this.value = newValue;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.NAME_TO_OPERATION_MAP__VALUE, oldValue, this.value));
		}
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
		case CollectionPackage.NAME_TO_OPERATION_MAP__KEY:
			return this.getTypedKey();
		case CollectionPackage.NAME_TO_OPERATION_MAP__VALUE:
			if (resolve) {
				return this.getTypedValue();
			}
			return this.basicGetTypedValue();
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
		case CollectionPackage.NAME_TO_OPERATION_MAP__KEY:
			this.setTypedKey((String) newValue);
			return;
		case CollectionPackage.NAME_TO_OPERATION_MAP__VALUE:
			this.setTypedValue((OperationType) newValue);
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
		case CollectionPackage.NAME_TO_OPERATION_MAP__KEY:
			this.setTypedKey(KEY_EDEFAULT);
			return;
		case CollectionPackage.NAME_TO_OPERATION_MAP__VALUE:
			this.setTypedValue((OperationType) null);
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
		case CollectionPackage.NAME_TO_OPERATION_MAP__KEY:
			return KEY_EDEFAULT == null ? this.key != null : !KEY_EDEFAULT.equals(this.key);
		case CollectionPackage.NAME_TO_OPERATION_MAP__VALUE:
			return this.value != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (this.eIsProxy()) {
			return super.toString();
		}

		final StringBuilder result = new StringBuilder(super.toString());
		result.append(" (key: ");
		result.append(this.key);
		result.append(')');
		return result.toString();
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
	public String getKey() {
		return this.getTypedKey();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setKey(final String key) {
		this.setTypedKey(key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationType getValue() {
		return this.getTypedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationType setValue(final OperationType value) {
		final OperationType oldValue = this.getValue();
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
	public EMap<String, OperationType> getEMap() {
		final EObject container = this.eContainer();
		return container == null ? null : (EMap<String, OperationType>) container.eGet(this.eContainmentFeature());
	}

} // NameToOperationMapImpl
