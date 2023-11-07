/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.DerivedTSpecType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType;
import org.oceandsl.tools.sar.fxtran.TSpecType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>TSpec Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.TSpecTypeImpl#getDerivedTSpec <em>Derived
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.TSpecTypeImpl#getIntrinsicTSpec <em>Intrinsic
 * TSpec</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TSpecTypeImpl extends MinimalEObjectImpl.Container implements TSpecType {
    /**
     * The cached value of the '{@link #getDerivedTSpec() <em>Derived TSpec</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDerivedTSpec()
     * @generated
     * @ordered
     */
    protected DerivedTSpecType derivedTSpec;

    /**
     * The cached value of the '{@link #getIntrinsicTSpec() <em>Intrinsic TSpec</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getIntrinsicTSpec()
     * @generated
     * @ordered
     */
    protected IntrinsicTSpecType intrinsicTSpec;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected TSpecTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getTSpecType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DerivedTSpecType getDerivedTSpec() {
        return this.derivedTSpec;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetDerivedTSpec(final DerivedTSpecType newDerivedTSpec, NotificationChain msgs) {
        final DerivedTSpecType oldDerivedTSpec = this.derivedTSpec;
        this.derivedTSpec = newDerivedTSpec;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC, oldDerivedTSpec, newDerivedTSpec);
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
    public void setDerivedTSpec(final DerivedTSpecType newDerivedTSpec) {
        if (newDerivedTSpec != this.derivedTSpec) {
            NotificationChain msgs = null;
            if (this.derivedTSpec != null) {
                msgs = ((InternalEObject) this.derivedTSpec).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC, null, msgs);
            }
            if (newDerivedTSpec != null) {
                msgs = ((InternalEObject) newDerivedTSpec).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC, null, msgs);
            }
            msgs = this.basicSetDerivedTSpec(newDerivedTSpec, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC,
                    newDerivedTSpec, newDerivedTSpec));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public IntrinsicTSpecType getIntrinsicTSpec() {
        return this.intrinsicTSpec;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetIntrinsicTSpec(final IntrinsicTSpecType newIntrinsicTSpec,
            NotificationChain msgs) {
        final IntrinsicTSpecType oldIntrinsicTSpec = this.intrinsicTSpec;
        this.intrinsicTSpec = newIntrinsicTSpec;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC, oldIntrinsicTSpec, newIntrinsicTSpec);
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
    public void setIntrinsicTSpec(final IntrinsicTSpecType newIntrinsicTSpec) {
        if (newIntrinsicTSpec != this.intrinsicTSpec) {
            NotificationChain msgs = null;
            if (this.intrinsicTSpec != null) {
                msgs = ((InternalEObject) this.intrinsicTSpec).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC, null, msgs);
            }
            if (newIntrinsicTSpec != null) {
                msgs = ((InternalEObject) newIntrinsicTSpec).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC, null, msgs);
            }
            msgs = this.basicSetIntrinsicTSpec(newIntrinsicTSpec, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC,
                    newIntrinsicTSpec, newIntrinsicTSpec));
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
        case FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC:
            return this.basicSetDerivedTSpec(null, msgs);
        case FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC:
            return this.basicSetIntrinsicTSpec(null, msgs);
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
        case FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC:
            return this.getDerivedTSpec();
        case FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC:
            return this.getIntrinsicTSpec();
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
        case FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC:
            this.setDerivedTSpec((DerivedTSpecType) newValue);
            return;
        case FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC:
            this.setIntrinsicTSpec((IntrinsicTSpecType) newValue);
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
        case FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC:
            this.setDerivedTSpec((DerivedTSpecType) null);
            return;
        case FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC:
            this.setIntrinsicTSpec((IntrinsicTSpecType) null);
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
        case FxtranPackage.TSPEC_TYPE__DERIVED_TSPEC:
            return this.derivedTSpec != null;
        case FxtranPackage.TSPEC_TYPE__INTRINSIC_TSPEC:
            return this.intrinsicTSpec != null;
        }
        return super.eIsSet(featureID);
    }

} // TSpecTypeImpl
