/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.VNType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>VN Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.VNTypeImpl#getVN <em>VN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.VNTypeImpl#getN <em>N</em>}</li>
 * </ul>
 *
 * @generated
 */
public class VNTypeImpl extends MinimalEObjectImpl.Container implements VNType {
    /**
     * The cached value of the '{@link #getVN() <em>VN</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getVN()
     * @generated
     * @ordered
     */
    protected VNType vN;

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
    protected VNTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getVNType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public VNType getVN() {
        return this.vN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetVN(final VNType newVN, NotificationChain msgs) {
        final VNType oldVN = this.vN;
        this.vN = newVN;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.VN_TYPE__VN, oldVN, newVN);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setVN(final VNType newVN) {
        if (newVN != this.vN) {
            NotificationChain msgs = null;
            if (this.vN != null) {
                msgs = ((InternalEObject) this.vN).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.VN_TYPE__VN, null, msgs);
            }
            if (newVN != null) {
                msgs = ((InternalEObject) newVN).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FxtranPackage.VN_TYPE__VN,
                        null, msgs);
            }
            msgs = this.basicSetVN(newVN, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.VN_TYPE__VN, newVN, newVN));
        }
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
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.VN_TYPE__N, oldN, this.n));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID,
            final NotificationChain msgs) {
        switch (featureID) {
        case FxtranPackage.VN_TYPE__VN:
            return this.basicSetVN(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
        switch (featureID) {
        case FxtranPackage.VN_TYPE__VN:
            return this.getVN();
        case FxtranPackage.VN_TYPE__N:
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
        case FxtranPackage.VN_TYPE__VN:
            this.setVN((VNType) newValue);
            return;
        case FxtranPackage.VN_TYPE__N:
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
        case FxtranPackage.VN_TYPE__VN:
            this.setVN((VNType) null);
            return;
        case FxtranPackage.VN_TYPE__N:
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
        case FxtranPackage.VN_TYPE__VN:
            return this.vN != null;
        case FxtranPackage.VN_TYPE__N:
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

} // VNTypeImpl
