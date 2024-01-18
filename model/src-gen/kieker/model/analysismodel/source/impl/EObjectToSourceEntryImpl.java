/**
 */
package kieker.model.analysismodel.source.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import kieker.model.analysismodel.source.SourcePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EObject To Source Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.source.impl.EObjectToSourceEntryImpl#getTypedKey <em>Key</em>}</li>
 * <li>{@link kieker.model.analysismodel.source.impl.EObjectToSourceEntryImpl#getTypedValue <em>Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EObjectToSourceEntryImpl extends MinimalEObjectImpl.Container implements BasicEMap.Entry<EObject, EList<String>> {
	/**
	 * The cached value of the '{@link #getTypedKey() <em>Key</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedKey()
	 * @generated
	 * @ordered
	 */
	protected EObject key;

	/**
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected EList<String> value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected EObjectToSourceEntryImpl() {
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
		return SourcePackage.Literals.EOBJECT_TO_SOURCE_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EObject getTypedKey() {
		if ((this.key != null) && this.key.eIsProxy()) {
			final InternalEObject oldKey = (InternalEObject) this.key;
			this.key = this.eResolveProxy(oldKey);
			if (this.key != oldKey) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, SourcePackage.EOBJECT_TO_SOURCE_ENTRY__KEY, oldKey, this.key));
				}
			}
		}
		return this.key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EObject basicGetTypedKey() {
		return this.key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setTypedKey(final EObject newKey) {
		final EObject oldKey = this.key;
		this.key = newKey;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, SourcePackage.EOBJECT_TO_SOURCE_ENTRY__KEY, oldKey, this.key));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<String> getTypedValue() {
		if (this.value == null) {
			this.value = new EDataTypeUniqueEList<>(String.class, this, SourcePackage.EOBJECT_TO_SOURCE_ENTRY__VALUE);
		}
		return this.value;
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
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__KEY:
			if (resolve) {
				return this.getTypedKey();
			}
			return this.basicGetTypedKey();
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__VALUE:
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
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__KEY:
			this.setTypedKey((EObject) newValue);
			return;
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__VALUE:
			this.getTypedValue().clear();
			this.getTypedValue().addAll((Collection<? extends String>) newValue);
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
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__KEY:
			this.setTypedKey((EObject) null);
			return;
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__VALUE:
			this.getTypedValue().clear();
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
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__KEY:
			return this.key != null;
		case SourcePackage.EOBJECT_TO_SOURCE_ENTRY__VALUE:
			return (this.value != null) && !this.value.isEmpty();
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
		result.append(" (value: ");
		result.append(this.value);
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
	public EObject getKey() {
		return this.getTypedKey();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setKey(final EObject key) {
		this.setTypedKey(key);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<String> getValue() {
		return this.getTypedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<String> setValue(final EList<String> value) {
		final EList<String> oldValue = this.getValue();
		this.getTypedValue().clear();
		this.getTypedValue().addAll(value);
		return oldValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	public EMap<EObject, EList<String>> getEMap() {
		final EObject container = this.eContainer();
		return container == null ? null : (EMap<EObject, EList<String>>) container.eGet(this.eContainmentFeature());
	}

} // EObjectToSourceEntryImpl
