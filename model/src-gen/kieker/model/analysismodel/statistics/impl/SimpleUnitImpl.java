/**
 */
package kieker.model.analysismodel.statistics.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import kieker.model.analysismodel.statistics.EPrefix;
import kieker.model.analysismodel.statistics.SimpleUnit;
import kieker.model.analysismodel.statistics.StatisticsPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Simple Unit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.statistics.impl.SimpleUnitImpl#getPrefix <em>Prefix</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SimpleUnitImpl extends UnitImpl implements SimpleUnit {
	/**
	 * The default value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected static final EPrefix PREFIX_EDEFAULT = EPrefix.NO_P;

	/**
	 * The cached value of the '{@link #getPrefix() <em>Prefix</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getPrefix()
	 * @generated
	 * @ordered
	 */
	protected EPrefix prefix = PREFIX_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SimpleUnitImpl() {
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
		return StatisticsPackage.Literals.SIMPLE_UNIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EPrefix getPrefix() {
		return this.prefix;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setPrefix(final EPrefix newPrefix) {
		final EPrefix oldPrefix = this.prefix;
		this.prefix = newPrefix == null ? PREFIX_EDEFAULT : newPrefix;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, StatisticsPackage.SIMPLE_UNIT__PREFIX, oldPrefix, this.prefix));
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
		case StatisticsPackage.SIMPLE_UNIT__PREFIX:
			return this.getPrefix();
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
		case StatisticsPackage.SIMPLE_UNIT__PREFIX:
			this.setPrefix((EPrefix) newValue);
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
		case StatisticsPackage.SIMPLE_UNIT__PREFIX:
			this.setPrefix(PREFIX_EDEFAULT);
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
		case StatisticsPackage.SIMPLE_UNIT__PREFIX:
			return this.prefix != PREFIX_EDEFAULT;
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
		result.append(" (prefix: ");
		result.append(this.prefix);
		result.append(')');
		return result.toString();
	}

} // SimpleUnitImpl
