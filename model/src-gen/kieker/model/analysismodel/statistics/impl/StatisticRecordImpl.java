/**
 */
package kieker.model.analysismodel.statistics.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Statistic Record</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.StatisticRecordImpl#getProperties <em>Properties</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StatisticRecordImpl extends MinimalEObjectImpl.Container implements StatisticRecord {
	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, Object> properties;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected StatisticRecordImpl() {
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
		return StatisticsPackage.Literals.STATISTIC_RECORD;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, Object> getProperties() {
		if (this.properties == null) {
			this.properties = new EcoreEMap<>(StatisticsPackage.Literals.EPROPERTY_TYPE_TO_VALUE, EPropertyTypeToValueImpl.class, this,
					StatisticsPackage.STATISTIC_RECORD__PROPERTIES);
		}
		return this.properties;
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
		case StatisticsPackage.STATISTIC_RECORD__PROPERTIES:
			return ((InternalEList<?>) this.getProperties()).basicRemove(otherEnd, msgs);
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
		case StatisticsPackage.STATISTIC_RECORD__PROPERTIES:
			if (coreType) {
				return this.getProperties();
			} else {
				return this.getProperties().map();
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
		case StatisticsPackage.STATISTIC_RECORD__PROPERTIES:
			((EStructuralFeature.Setting) this.getProperties()).set(newValue);
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
		case StatisticsPackage.STATISTIC_RECORD__PROPERTIES:
			this.getProperties().clear();
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
		case StatisticsPackage.STATISTIC_RECORD__PROPERTIES:
			return (this.properties != null) && !this.properties.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // StatisticRecordImpl
