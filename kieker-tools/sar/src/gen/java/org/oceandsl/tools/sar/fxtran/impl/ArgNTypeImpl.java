/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.ArgNType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.NType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Arg NType</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgNTypeImpl#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgNTypeImpl#getK <em>K</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgNTypeImpl#getN1 <em>N1</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArgNTypeImpl extends MinimalEObjectImpl.Container implements ArgNType {
    /**
     * The cached value of the '{@link #getN() <em>N</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getN()
     * @generated
     * @ordered
     */
    protected NType n;

    /**
     * The default value of the '{@link #getK() <em>K</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getK()
     * @generated
     * @ordered
     */
    protected static final String K_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getK() <em>K</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getK()
     * @generated
     * @ordered
     */
    protected String k = K_EDEFAULT;

    /**
     * The default value of the '{@link #getN1() <em>N1</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getN1()
     * @generated
     * @ordered
     */
    protected static final String N1_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getN1() <em>N1</em>}' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @see #getN1()
     * @generated
     * @ordered
     */
    protected String n1 = N1_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ArgNTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getArgNType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NType getN() {
        return this.n;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetN(final NType newN, NotificationChain msgs) {
        final NType oldN = this.n;
        this.n = newN;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ARG_NTYPE__N, oldN, newN);
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
    public void setN(final NType newN) {
        if (newN != this.n) {
            NotificationChain msgs = null;
            if (this.n != null) {
                msgs = ((InternalEObject) this.n).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ARG_NTYPE__N, null, msgs);
            }
            if (newN != null) {
                msgs = ((InternalEObject) newN).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - FxtranPackage.ARG_NTYPE__N,
                        null, msgs);
            }
            msgs = this.basicSetN(newN, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ARG_NTYPE__N, newN, newN));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getK() {
        return this.k;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setK(final String newK) {
        final String oldK = this.k;
        this.k = newK;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ARG_NTYPE__K, oldK, this.k));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getN1() {
        return this.n1;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setN1(final String newN1) {
        final String oldN1 = this.n1;
        this.n1 = newN1;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ARG_NTYPE__N1, oldN1, this.n1));
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
        case FxtranPackage.ARG_NTYPE__N:
            return this.basicSetN(null, msgs);
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
        case FxtranPackage.ARG_NTYPE__N:
            return this.getN();
        case FxtranPackage.ARG_NTYPE__K:
            return this.getK();
        case FxtranPackage.ARG_NTYPE__N1:
            return this.getN1();
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
        case FxtranPackage.ARG_NTYPE__N:
            this.setN((NType) newValue);
            return;
        case FxtranPackage.ARG_NTYPE__K:
            this.setK((String) newValue);
            return;
        case FxtranPackage.ARG_NTYPE__N1:
            this.setN1((String) newValue);
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
        case FxtranPackage.ARG_NTYPE__N:
            this.setN((NType) null);
            return;
        case FxtranPackage.ARG_NTYPE__K:
            this.setK(K_EDEFAULT);
            return;
        case FxtranPackage.ARG_NTYPE__N1:
            this.setN1(N1_EDEFAULT);
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
        case FxtranPackage.ARG_NTYPE__N:
            return this.n != null;
        case FxtranPackage.ARG_NTYPE__K:
            return K_EDEFAULT == null ? this.k != null : !K_EDEFAULT.equals(this.k);
        case FxtranPackage.ARG_NTYPE__N1:
            return N1_EDEFAULT == null ? this.n1 != null : !N1_EDEFAULT.equals(this.n1);
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
        result.append(" (k: ");
        result.append(this.k);
        result.append(", n1: ");
        result.append(this.n1);
        result.append(')');
        return result.toString();
    }

} // ArgNTypeImpl
