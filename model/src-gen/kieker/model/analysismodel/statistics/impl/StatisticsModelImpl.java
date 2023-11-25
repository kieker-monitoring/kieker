/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.Unit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.StatisticsModelImpl#getStatistics <em>Statistics</em>}</li>
 * <li>{@link kieker.model.analysismodel.statistics.impl.StatisticsModelImpl#getUnits <em>Units</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StatisticsModelImpl extends MinimalEObjectImpl.Container implements StatisticsModel {
	/**
	 * The cached value of the '{@link #getStatistics() <em>Statistics</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStatistics()
	 * @generated
	 * @ordered
	 */
	protected EMap<EObject, StatisticRecord> statistics;

	/**
	 * The cached value of the '{@link #getUnits() <em>Units</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getUnits()
	 * @generated
	 * @ordered
	 */
	protected EList<Unit> units;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected StatisticsModelImpl() {
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
		return StatisticsPackage.Literals.STATISTICS_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<EObject, StatisticRecord> getStatistics() {
		if (this.statistics == null) {
			this.statistics = new EcoreEMap<>(StatisticsPackage.Literals.EOBJECT_TO_STATISTICS_MAP_ENTRY,
					EObjectToStatisticsMapEntryImpl.class,
					this, StatisticsPackage.STATISTICS_MODEL__STATISTICS);
		}
		return this.statistics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<Unit> getUnits() {
		if (this.units == null) {
			this.units = new EObjectContainmentEList<>(Unit.class, this, StatisticsPackage.STATISTICS_MODEL__UNITS);
		}
		return this.units;
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
		case StatisticsPackage.STATISTICS_MODEL__STATISTICS:
			return ((InternalEList<?>) this.getStatistics()).basicRemove(otherEnd, msgs);
		case StatisticsPackage.STATISTICS_MODEL__UNITS:
			return ((InternalEList<?>) this.getUnits()).basicRemove(otherEnd, msgs);
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
		case StatisticsPackage.STATISTICS_MODEL__STATISTICS:
			if (coreType) {
				return this.getStatistics();
			} else {
				return this.getStatistics().map();
			}
		case StatisticsPackage.STATISTICS_MODEL__UNITS:
			return this.getUnits();
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
		case StatisticsPackage.STATISTICS_MODEL__STATISTICS:
			((EStructuralFeature.Setting) this.getStatistics()).set(newValue);
			return;
		case StatisticsPackage.STATISTICS_MODEL__UNITS:
			this.getUnits().clear();
			this.getUnits().addAll((Collection<? extends Unit>) newValue);
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
		case StatisticsPackage.STATISTICS_MODEL__STATISTICS:
			this.getStatistics().clear();
			return;
		case StatisticsPackage.STATISTICS_MODEL__UNITS:
			this.getUnits().clear();
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
		case StatisticsPackage.STATISTICS_MODEL__STATISTICS:
			return (this.statistics != null) && !this.statistics.isEmpty();
		case StatisticsPackage.STATISTICS_MODEL__UNITS:
			return (this.units != null) && !this.units.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // StatisticsModelImpl
