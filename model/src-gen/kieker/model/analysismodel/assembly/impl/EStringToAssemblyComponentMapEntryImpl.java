/**
 */
package kieker.model.analysismodel.assembly.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EString To Assembly Component Map Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl#getTypedKey <em>Key</em>}</li>
 * <li>{@link kieker.model.analysismodel.assembly.impl.EStringToAssemblyComponentMapEntryImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EStringToAssemblyComponentMapEntryImpl extends MinimalEObjectImpl.Container implements BasicEMap.Entry<String, AssemblyComponent> {
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
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected AssemblyComponent value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected EStringToAssemblyComponentMapEntryImpl() {
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
		return AssemblyPackage.Literals.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY;
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY, oldKey, this.key));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyComponent getTypedValue() {
		return this.value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetTypedValue(final AssemblyComponent newValue, NotificationChain msgs) {
		final AssemblyComponent oldValue = this.value;
		this.value = newValue;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE,
					oldValue,
					newValue);
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
	public void setTypedValue(final AssemblyComponent newValue) {
		if (newValue != this.value) {
			NotificationChain msgs = null;
			if (this.value != null) {
				msgs = ((InternalEObject) this.value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE,
						null,
						msgs);
			}
			if (newValue != null) {
				msgs = ((InternalEObject) newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE, null,
						msgs);
			}
			msgs = this.basicSetTypedValue(newValue, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE, newValue, newValue));
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
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE:
			return this.basicSetTypedValue(null, msgs);
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
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY:
			return this.getTypedKey();
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE:
			return this.getTypedValue();
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
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY:
			this.setTypedKey((String) newValue);
			return;
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE:
			this.setTypedValue((AssemblyComponent) newValue);
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
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY:
			this.setTypedKey(KEY_EDEFAULT);
			return;
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE:
			this.setTypedValue((AssemblyComponent) null);
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
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__KEY:
			return KEY_EDEFAULT == null ? this.key != null : !KEY_EDEFAULT.equals(this.key);
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY__VALUE:
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
	public AssemblyComponent getValue() {
		return this.getTypedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyComponent setValue(final AssemblyComponent value) {
		final AssemblyComponent oldValue = this.getValue();
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
	public EMap<String, AssemblyComponent> getEMap() {
		final EObject container = this.eContainer();
		return container == null ? null : (EMap<String, AssemblyComponent>) container.eGet(this.eContainmentFeature());
	}

} // EStringToAssemblyComponentMapEntryImpl
