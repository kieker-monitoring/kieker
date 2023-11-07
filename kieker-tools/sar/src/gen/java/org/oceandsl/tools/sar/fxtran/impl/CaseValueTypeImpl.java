/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.CaseValueType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.StringEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Case Value Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CaseValueTypeImpl#getLiteralE <em>Literal
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CaseValueTypeImpl#getStringE <em>String
 * E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CaseValueTypeImpl extends MinimalEObjectImpl.Container implements CaseValueType {
    /**
     * The cached value of the '{@link #getLiteralE() <em>Literal E</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getLiteralE()
     * @generated
     * @ordered
     */
    protected LiteralEType literalE;

    /**
     * The cached value of the '{@link #getStringE() <em>String E</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getStringE()
     * @generated
     * @ordered
     */
    protected StringEType stringE;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected CaseValueTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getCaseValueType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LiteralEType getLiteralE() {
        return this.literalE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetLiteralE(final LiteralEType newLiteralE, NotificationChain msgs) {
        final LiteralEType oldLiteralE = this.literalE;
        this.literalE = newLiteralE;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.CASE_VALUE_TYPE__LITERAL_E, oldLiteralE, newLiteralE);
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
    public void setLiteralE(final LiteralEType newLiteralE) {
        if (newLiteralE != this.literalE) {
            NotificationChain msgs = null;
            if (this.literalE != null) {
                msgs = ((InternalEObject) this.literalE).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.CASE_VALUE_TYPE__LITERAL_E, null, msgs);
            }
            if (newLiteralE != null) {
                msgs = ((InternalEObject) newLiteralE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.CASE_VALUE_TYPE__LITERAL_E, null, msgs);
            }
            msgs = this.basicSetLiteralE(newLiteralE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.CASE_VALUE_TYPE__LITERAL_E,
                    newLiteralE, newLiteralE));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public StringEType getStringE() {
        return this.stringE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetStringE(final StringEType newStringE, NotificationChain msgs) {
        final StringEType oldStringE = this.stringE;
        this.stringE = newStringE;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.CASE_VALUE_TYPE__STRING_E, oldStringE, newStringE);
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
    public void setStringE(final StringEType newStringE) {
        if (newStringE != this.stringE) {
            NotificationChain msgs = null;
            if (this.stringE != null) {
                msgs = ((InternalEObject) this.stringE).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.CASE_VALUE_TYPE__STRING_E, null, msgs);
            }
            if (newStringE != null) {
                msgs = ((InternalEObject) newStringE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.CASE_VALUE_TYPE__STRING_E, null, msgs);
            }
            msgs = this.basicSetStringE(newStringE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.CASE_VALUE_TYPE__STRING_E,
                    newStringE, newStringE));
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
        case FxtranPackage.CASE_VALUE_TYPE__LITERAL_E:
            return this.basicSetLiteralE(null, msgs);
        case FxtranPackage.CASE_VALUE_TYPE__STRING_E:
            return this.basicSetStringE(null, msgs);
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
        case FxtranPackage.CASE_VALUE_TYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.CASE_VALUE_TYPE__STRING_E:
            return this.getStringE();
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
        case FxtranPackage.CASE_VALUE_TYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) newValue);
            return;
        case FxtranPackage.CASE_VALUE_TYPE__STRING_E:
            this.setStringE((StringEType) newValue);
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
        case FxtranPackage.CASE_VALUE_TYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) null);
            return;
        case FxtranPackage.CASE_VALUE_TYPE__STRING_E:
            this.setStringE((StringEType) null);
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
        case FxtranPackage.CASE_VALUE_TYPE__LITERAL_E:
            return this.literalE != null;
        case FxtranPackage.CASE_VALUE_TYPE__STRING_E:
            return this.stringE != null;
        }
        return super.eIsSet(featureID);
    }

} // CaseValueTypeImpl
