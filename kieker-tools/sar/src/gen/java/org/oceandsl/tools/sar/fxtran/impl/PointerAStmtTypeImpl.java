/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.E1Type;
import org.oceandsl.tools.sar.fxtran.E2Type;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.PointerAStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Pointer AStmt
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.PointerAStmtTypeImpl#getE1 <em>E1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.PointerAStmtTypeImpl#getA <em>A</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.PointerAStmtTypeImpl#getE2 <em>E2</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PointerAStmtTypeImpl extends MinimalEObjectImpl.Container implements PointerAStmtType {
    /**
     * The cached value of the '{@link #getE1() <em>E1</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getE1()
     * @generated
     * @ordered
     */
    protected E1Type e1;

    /**
     * The default value of the '{@link #getA() <em>A</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getA()
     * @generated
     * @ordered
     */
    protected static final String A_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getA() <em>A</em>}' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @see #getA()
     * @generated
     * @ordered
     */
    protected String a = A_EDEFAULT;

    /**
     * The cached value of the '{@link #getE2() <em>E2</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getE2()
     * @generated
     * @ordered
     */
    protected E2Type e2;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected PointerAStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getPointerAStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public E1Type getE1() {
        return this.e1;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetE1(final E1Type newE1, NotificationChain msgs) {
        final E1Type oldE1 = this.e1;
        this.e1 = newE1;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.POINTER_ASTMT_TYPE__E1, oldE1, newE1);
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
    public void setE1(final E1Type newE1) {
        if (newE1 != this.e1) {
            NotificationChain msgs = null;
            if (this.e1 != null) {
                msgs = ((InternalEObject) this.e1).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.POINTER_ASTMT_TYPE__E1, null, msgs);
            }
            if (newE1 != null) {
                msgs = ((InternalEObject) newE1).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.POINTER_ASTMT_TYPE__E1, null, msgs);
            }
            msgs = this.basicSetE1(newE1, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.POINTER_ASTMT_TYPE__E1, newE1, newE1));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getA() {
        return this.a;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setA(final String newA) {
        final String oldA = this.a;
        this.a = newA;
        if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.POINTER_ASTMT_TYPE__A, oldA, this.a));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public E2Type getE2() {
        return this.e2;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetE2(final E2Type newE2, NotificationChain msgs) {
        final E2Type oldE2 = this.e2;
        this.e2 = newE2;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.POINTER_ASTMT_TYPE__E2, oldE2, newE2);
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
    public void setE2(final E2Type newE2) {
        if (newE2 != this.e2) {
            NotificationChain msgs = null;
            if (this.e2 != null) {
                msgs = ((InternalEObject) this.e2).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.POINTER_ASTMT_TYPE__E2, null, msgs);
            }
            if (newE2 != null) {
                msgs = ((InternalEObject) newE2).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.POINTER_ASTMT_TYPE__E2, null, msgs);
            }
            msgs = this.basicSetE2(newE2, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.POINTER_ASTMT_TYPE__E2, newE2, newE2));
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
        case FxtranPackage.POINTER_ASTMT_TYPE__E1:
            return this.basicSetE1(null, msgs);
        case FxtranPackage.POINTER_ASTMT_TYPE__E2:
            return this.basicSetE2(null, msgs);
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
        case FxtranPackage.POINTER_ASTMT_TYPE__E1:
            return this.getE1();
        case FxtranPackage.POINTER_ASTMT_TYPE__A:
            return this.getA();
        case FxtranPackage.POINTER_ASTMT_TYPE__E2:
            return this.getE2();
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
        case FxtranPackage.POINTER_ASTMT_TYPE__E1:
            this.setE1((E1Type) newValue);
            return;
        case FxtranPackage.POINTER_ASTMT_TYPE__A:
            this.setA((String) newValue);
            return;
        case FxtranPackage.POINTER_ASTMT_TYPE__E2:
            this.setE2((E2Type) newValue);
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
        case FxtranPackage.POINTER_ASTMT_TYPE__E1:
            this.setE1((E1Type) null);
            return;
        case FxtranPackage.POINTER_ASTMT_TYPE__A:
            this.setA(A_EDEFAULT);
            return;
        case FxtranPackage.POINTER_ASTMT_TYPE__E2:
            this.setE2((E2Type) null);
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
        case FxtranPackage.POINTER_ASTMT_TYPE__E1:
            return this.e1 != null;
        case FxtranPackage.POINTER_ASTMT_TYPE__A:
            return A_EDEFAULT == null ? this.a != null : !A_EDEFAULT.equals(this.a);
        case FxtranPackage.POINTER_ASTMT_TYPE__E2:
            return this.e2 != null;
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
        result.append(" (a: ");
        result.append(this.a);
        result.append(')');
        return result.toString();
    }

} // PointerAStmtTypeImpl
