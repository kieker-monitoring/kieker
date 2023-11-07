/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.NamelistGroupNType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Namelist Group
 * NType</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistGroupNTypeImpl#getN <em>N</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamelistGroupNTypeImpl extends MinimalEObjectImpl.Container implements NamelistGroupNType {
    /**
     * The default value of the '{@link #getN() <em>N</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getN()
     * @generated
     * @ordered
     */
    protected static final String N_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getN() <em>N</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getN()
     * @generated
     * @ordered
     */
    protected String n = N_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected NamelistGroupNTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getNamelistGroupNType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getN() {
        return this.n;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setN(final String newN) {
        final String oldN = this.n;
        this.n = newN;
        if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.NAMELIST_GROUP_NTYPE__N, oldN, this.n));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
        switch (featureID) {
        case FxtranPackage.NAMELIST_GROUP_NTYPE__N:
            return this.getN();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(final int featureID, final Object newValue) {
        switch (featureID) {
        case FxtranPackage.NAMELIST_GROUP_NTYPE__N:
            this.setN((String) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(final int featureID) {
        switch (featureID) {
        case FxtranPackage.NAMELIST_GROUP_NTYPE__N:
            this.setN(N_EDEFAULT);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(final int featureID) {
        switch (featureID) {
        case FxtranPackage.NAMELIST_GROUP_NTYPE__N:
            return N_EDEFAULT == null ? this.n != null : !N_EDEFAULT.equals(this.n);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        final StringBuilder result = new StringBuilder(super.toString());
        result.append(" (n: ");
        result.append(this.n);
        result.append(')');
        return result.toString();
    }

} // NamelistGroupNTypeImpl
