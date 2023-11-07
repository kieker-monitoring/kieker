/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.CharSelectorType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType;
import org.oceandsl.tools.sar.fxtran.KSelectorType;
import org.oceandsl.tools.sar.fxtran.TNType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Intrinsic TSpec
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IntrinsicTSpecTypeImpl#getTN <em>TN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IntrinsicTSpecTypeImpl#getKSelector
 * <em>KSelector</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IntrinsicTSpecTypeImpl#getCharSelector <em>Char
 * Selector</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IntrinsicTSpecTypeImpl extends MinimalEObjectImpl.Container implements IntrinsicTSpecType {
    /**
     * The cached value of the '{@link #getTN() <em>TN</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getTN()
     * @generated
     * @ordered
     */
    protected TNType tN;

    /**
     * The cached value of the '{@link #getKSelector() <em>KSelector</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getKSelector()
     * @generated
     * @ordered
     */
    protected KSelectorType kSelector;

    /**
     * The cached value of the '{@link #getCharSelector() <em>Char Selector</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getCharSelector()
     * @generated
     * @ordered
     */
    protected CharSelectorType charSelector;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected IntrinsicTSpecTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getIntrinsicTSpecType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public TNType getTN() {
        return this.tN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetTN(final TNType newTN, NotificationChain msgs) {
        final TNType oldTN = this.tN;
        this.tN = newTN;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.INTRINSIC_TSPEC_TYPE__TN, oldTN, newTN);
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
    public void setTN(final TNType newTN) {
        if (newTN != this.tN) {
            NotificationChain msgs = null;
            if (this.tN != null) {
                msgs = ((InternalEObject) this.tN).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INTRINSIC_TSPEC_TYPE__TN, null, msgs);
            }
            if (newTN != null) {
                msgs = ((InternalEObject) newTN).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INTRINSIC_TSPEC_TYPE__TN, null, msgs);
            }
            msgs = this.basicSetTN(newTN, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.INTRINSIC_TSPEC_TYPE__TN, newTN,
                    newTN));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public KSelectorType getKSelector() {
        return this.kSelector;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetKSelector(final KSelectorType newKSelector, NotificationChain msgs) {
        final KSelectorType oldKSelector = this.kSelector;
        this.kSelector = newKSelector;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR, oldKSelector, newKSelector);
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
    public void setKSelector(final KSelectorType newKSelector) {
        if (newKSelector != this.kSelector) {
            NotificationChain msgs = null;
            if (this.kSelector != null) {
                msgs = ((InternalEObject) this.kSelector).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR, null, msgs);
            }
            if (newKSelector != null) {
                msgs = ((InternalEObject) newKSelector).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR, null, msgs);
            }
            msgs = this.basicSetKSelector(newKSelector, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR,
                    newKSelector, newKSelector));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CharSelectorType getCharSelector() {
        return this.charSelector;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCharSelector(final CharSelectorType newCharSelector, NotificationChain msgs) {
        final CharSelectorType oldCharSelector = this.charSelector;
        this.charSelector = newCharSelector;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR, oldCharSelector, newCharSelector);
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
    public void setCharSelector(final CharSelectorType newCharSelector) {
        if (newCharSelector != this.charSelector) {
            NotificationChain msgs = null;
            if (this.charSelector != null) {
                msgs = ((InternalEObject) this.charSelector).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR, null, msgs);
            }
            if (newCharSelector != null) {
                msgs = ((InternalEObject) newCharSelector).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR, null, msgs);
            }
            msgs = this.basicSetCharSelector(newCharSelector, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR, newCharSelector, newCharSelector));
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
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__TN:
            return this.basicSetTN(null, msgs);
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR:
            return this.basicSetKSelector(null, msgs);
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR:
            return this.basicSetCharSelector(null, msgs);
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
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__TN:
            return this.getTN();
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR:
            return this.getKSelector();
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR:
            return this.getCharSelector();
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
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__TN:
            this.setTN((TNType) newValue);
            return;
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR:
            this.setKSelector((KSelectorType) newValue);
            return;
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR:
            this.setCharSelector((CharSelectorType) newValue);
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
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__TN:
            this.setTN((TNType) null);
            return;
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR:
            this.setKSelector((KSelectorType) null);
            return;
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR:
            this.setCharSelector((CharSelectorType) null);
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
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__TN:
            return this.tN != null;
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__KSELECTOR:
            return this.kSelector != null;
        case FxtranPackage.INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR:
            return this.charSelector != null;
        }
        return super.eIsSet(featureID);
    }

} // IntrinsicTSpecTypeImpl
