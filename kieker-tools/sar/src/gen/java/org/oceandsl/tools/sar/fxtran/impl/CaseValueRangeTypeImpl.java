/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.CaseValueRangeType;
import org.oceandsl.tools.sar.fxtran.CaseValueType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Case Value Range
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CaseValueRangeTypeImpl#getCaseValue <em>Case
 * Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CaseValueRangeTypeImpl extends MinimalEObjectImpl.Container implements CaseValueRangeType {
    /**
     * The cached value of the '{@link #getCaseValue() <em>Case Value</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getCaseValue()
     * @generated
     * @ordered
     */
    protected CaseValueType caseValue;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected CaseValueRangeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getCaseValueRangeType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CaseValueType getCaseValue() {
        return this.caseValue;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCaseValue(final CaseValueType newCaseValue, NotificationChain msgs) {
        final CaseValueType oldCaseValue = this.caseValue;
        this.caseValue = newCaseValue;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE, oldCaseValue, newCaseValue);
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
    public void setCaseValue(final CaseValueType newCaseValue) {
        if (newCaseValue != this.caseValue) {
            NotificationChain msgs = null;
            if (this.caseValue != null) {
                msgs = ((InternalEObject) this.caseValue).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE, null, msgs);
            }
            if (newCaseValue != null) {
                msgs = ((InternalEObject) newCaseValue).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE, null, msgs);
            }
            msgs = this.basicSetCaseValue(newCaseValue, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE,
                    newCaseValue, newCaseValue));
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
        case FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE:
            return this.basicSetCaseValue(null, msgs);
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
        case FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE:
            return this.getCaseValue();
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
        case FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE:
            this.setCaseValue((CaseValueType) newValue);
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
        case FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE:
            this.setCaseValue((CaseValueType) null);
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
        case FxtranPackage.CASE_VALUE_RANGE_TYPE__CASE_VALUE:
            return this.caseValue != null;
        }
        return super.eIsSet(featureID);
    }

} // CaseValueRangeTypeImpl
