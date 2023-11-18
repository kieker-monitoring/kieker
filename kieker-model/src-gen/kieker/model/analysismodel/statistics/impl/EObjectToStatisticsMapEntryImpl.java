/**
 */
package kieker.model.analysismodel.statistics.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.BasicEMap;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EObject To Statistics Map Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.EObjectToStatisticsMapEntryImpl#getTypedValue <em>Value</em>}</li>
 * <li>{@link kieker.model.analysismodel.statistics.impl.EObjectToStatisticsMapEntryImpl#getTypedKey <em>Key</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EObjectToStatisticsMapEntryImpl extends MinimalEObjectImpl.Container implements BasicEMap.Entry<EObject, StatisticRecord> {
	/**
	 * The cached value of the '{@link #getTypedValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTypedValue()
	 * @generated
	 * @ordered
	 */
	protected StatisticRecord value;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected EObjectToStatisticsMapEntryImpl() {
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
		return StatisticsPackage.Literals.EOBJECT_TO_STATISTICS_MAP_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public StatisticRecord getTypedValue() {
		return this.value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetTypedValue(final StatisticRecord newValue, NotificationChain msgs) {
		final StatisticRecord oldValue = this.value;
		this.value = newValue;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE, oldValue,
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
	public void setTypedValue(final StatisticRecord newValue) {
		if (newValue != this.value) {
			NotificationChain msgs = null;
			if (this.value != null) {
				msgs = ((InternalEObject) this.value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE, null,
						msgs);
			}
			if (newValue != null) {
				msgs = ((InternalEObject) newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE, null, msgs);
			}
			msgs = this.basicSetTypedValue(newValue, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE, newValue, newValue));
		}
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
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY, oldKey, this.key));
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
			this.eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY, oldKey, this.key));
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
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE:
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
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE:
			return this.getTypedValue();
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY:
			if (resolve) {
				return this.getTypedKey();
			}
			return this.basicGetTypedKey();
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
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE:
			this.setTypedValue((StatisticRecord) newValue);
			return;
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY:
			this.setTypedKey((EObject) newValue);
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
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE:
			this.setTypedValue((StatisticRecord) null);
			return;
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY:
			this.setTypedKey((EObject) null);
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
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE:
			return this.value != null;
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY:
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
	public StatisticRecord getValue() {
		return this.getTypedValue();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StatisticRecord setValue(final StatisticRecord value) {
		final StatisticRecord oldValue = this.getValue();
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
	public EMap<EObject, StatisticRecord> getEMap() {
		final EObject container = this.eContainer();
		return container == null ? null : (EMap<EObject, StatisticRecord>) container.eGet(this.eContainmentFeature());
	}

} // EObjectToStatisticsMapEntryImpl
