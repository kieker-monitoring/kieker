/**
 */
package kieker.model.analysismodel.statistics.impl;

import kieker.model.analysismodel.statistics.EPredefinedUnits;
import kieker.model.analysismodel.statistics.Record;
import kieker.model.analysismodel.statistics.Statistics;
import kieker.model.analysismodel.statistics.StatisticsPackage;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Statistics</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.statistics.impl.StatisticsImpl#getStatistics <em>Statistics</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StatisticsImpl extends MinimalEObjectImpl.Container implements Statistics {
	/**
	 * The cached value of the '{@link #getStatistics() <em>Statistics</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatistics()
	 * @generated
	 * @ordered
	 */
	protected EMap<EPredefinedUnits, Record> statistics;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StatisticsImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return StatisticsPackage.Literals.STATISTICS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<EPredefinedUnits, Record> getStatistics() {
		if (statistics == null) {
			statistics = new EcoreEMap<EPredefinedUnits,Record>(StatisticsPackage.Literals.UNITS_TO_STATISTICS_MAP_ENTRY, UnitsToStatisticsMapEntryImpl.class, this, StatisticsPackage.STATISTICS__STATISTICS);
		}
		return statistics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case StatisticsPackage.STATISTICS__STATISTICS:
				return ((InternalEList<?>)getStatistics()).basicRemove(otherEnd, msgs);
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
			case StatisticsPackage.STATISTICS__STATISTICS:
				if (coreType) return getStatistics();
				else return getStatistics().map();
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
			case StatisticsPackage.STATISTICS__STATISTICS:
				((EStructuralFeature.Setting)getStatistics()).set(newValue);
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
			case StatisticsPackage.STATISTICS__STATISTICS:
				getStatistics().clear();
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
			case StatisticsPackage.STATISTICS__STATISTICS:
				return statistics != null && !statistics.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //StatisticsImpl
