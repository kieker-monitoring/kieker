/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.statistics.ComposedUnit;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.Unit;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Composed Unit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.ComposedUnitImpl#getUnits <em>Units</em>}</li>
 * <li>{@link kieker.model.analysismodel.statistics.impl.ComposedUnitImpl#getExponent <em>Exponent</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComposedUnitImpl extends MinimalEObjectImpl.Container implements ComposedUnit {
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
	 * The default value of the '{@link #getExponent() <em>Exponent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getExponent()
	 * @generated
	 * @ordered
	 */
	protected static final long EXPONENT_EDEFAULT = 0L;

	/**
	 * The cached value of the '{@link #getExponent() <em>Exponent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getExponent()
	 * @generated
	 * @ordered
	 */
	protected long exponent = EXPONENT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ComposedUnitImpl() {
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
		return StatisticsPackage.Literals.COMPOSED_UNIT;
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
			this.units = new EObjectContainmentEList<>(Unit.class, this, StatisticsPackage.COMPOSED_UNIT__UNITS);
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
	public long getExponent() {
		return this.exponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setExponent(final long newExponent) {
		final long oldExponent = this.exponent;
		this.exponent = newExponent;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.COMPOSED_UNIT__EXPONENT, oldExponent, this.exponent));
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
		case StatisticsPackage.COMPOSED_UNIT__UNITS:
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
		case StatisticsPackage.COMPOSED_UNIT__UNITS:
			return this.getUnits();
		case StatisticsPackage.COMPOSED_UNIT__EXPONENT:
			return this.getExponent();
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
		case StatisticsPackage.COMPOSED_UNIT__UNITS:
			this.getUnits().clear();
			this.getUnits().addAll((Collection<? extends Unit>) newValue);
			return;
		case StatisticsPackage.COMPOSED_UNIT__EXPONENT:
			this.setExponent((Long) newValue);
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
		case StatisticsPackage.COMPOSED_UNIT__UNITS:
			this.getUnits().clear();
			return;
		case StatisticsPackage.COMPOSED_UNIT__EXPONENT:
			this.setExponent(EXPONENT_EDEFAULT);
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
		case StatisticsPackage.COMPOSED_UNIT__UNITS:
			return (this.units != null) && !this.units.isEmpty();
		case StatisticsPackage.COMPOSED_UNIT__EXPONENT:
			return this.exponent != EXPONENT_EDEFAULT;
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
		result.append(" (exponent: ");
		result.append(this.exponent);
		result.append(')');
		return result.toString();
	}

} // ComposedUnitImpl
