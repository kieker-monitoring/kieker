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
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.NamedEType;
import org.oceandsl.tools.sar.fxtran.OpEType;
import org.oceandsl.tools.sar.fxtran.UpperBoundType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Upper Bound Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.UpperBoundTypeImpl#getLiteralE <em>Literal
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.UpperBoundTypeImpl#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.UpperBoundTypeImpl#getOpE <em>Op E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class UpperBoundTypeImpl extends MinimalEObjectImpl.Container implements UpperBoundType {
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
     * The cached value of the '{@link #getNamedE() <em>Named E</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getNamedE()
     * @generated
     * @ordered
     */
    protected NamedEType namedE;

    /**
     * The cached value of the '{@link #getOpE() <em>Op E</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOpE()
     * @generated
     * @ordered
     */
    protected OpEType opE;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected UpperBoundTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getUpperBoundType();
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
                    FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E, oldLiteralE, newLiteralE);
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
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E, null, msgs);
            }
            if (newLiteralE != null) {
                msgs = ((InternalEObject) newLiteralE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E, null, msgs);
            }
            msgs = this.basicSetLiteralE(newLiteralE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E,
                    newLiteralE, newLiteralE));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamedEType getNamedE() {
        return this.namedE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamedE(final NamedEType newNamedE, NotificationChain msgs) {
        final NamedEType oldNamedE = this.namedE;
        this.namedE = newNamedE;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.UPPER_BOUND_TYPE__NAMED_E, oldNamedE, newNamedE);
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
    public void setNamedE(final NamedEType newNamedE) {
        if (newNamedE != this.namedE) {
            NotificationChain msgs = null;
            if (this.namedE != null) {
                msgs = ((InternalEObject) this.namedE).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.UPPER_BOUND_TYPE__NAMED_E, null, msgs);
            }
            if (newNamedE != null) {
                msgs = ((InternalEObject) newNamedE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.UPPER_BOUND_TYPE__NAMED_E, null, msgs);
            }
            msgs = this.basicSetNamedE(newNamedE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.UPPER_BOUND_TYPE__NAMED_E,
                    newNamedE, newNamedE));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public OpEType getOpE() {
        return this.opE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetOpE(final OpEType newOpE, NotificationChain msgs) {
        final OpEType oldOpE = this.opE;
        this.opE = newOpE;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.UPPER_BOUND_TYPE__OP_E, oldOpE, newOpE);
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
    public void setOpE(final OpEType newOpE) {
        if (newOpE != this.opE) {
            NotificationChain msgs = null;
            if (this.opE != null) {
                msgs = ((InternalEObject) this.opE).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.UPPER_BOUND_TYPE__OP_E, null, msgs);
            }
            if (newOpE != null) {
                msgs = ((InternalEObject) newOpE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.UPPER_BOUND_TYPE__OP_E, null, msgs);
            }
            msgs = this.basicSetOpE(newOpE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.UPPER_BOUND_TYPE__OP_E, newOpE,
                    newOpE));
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
        case FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E:
            return this.basicSetLiteralE(null, msgs);
        case FxtranPackage.UPPER_BOUND_TYPE__NAMED_E:
            return this.basicSetNamedE(null, msgs);
        case FxtranPackage.UPPER_BOUND_TYPE__OP_E:
            return this.basicSetOpE(null, msgs);
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
        case FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.UPPER_BOUND_TYPE__NAMED_E:
            return this.getNamedE();
        case FxtranPackage.UPPER_BOUND_TYPE__OP_E:
            return this.getOpE();
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
        case FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) newValue);
            return;
        case FxtranPackage.UPPER_BOUND_TYPE__NAMED_E:
            this.setNamedE((NamedEType) newValue);
            return;
        case FxtranPackage.UPPER_BOUND_TYPE__OP_E:
            this.setOpE((OpEType) newValue);
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
        case FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) null);
            return;
        case FxtranPackage.UPPER_BOUND_TYPE__NAMED_E:
            this.setNamedE((NamedEType) null);
            return;
        case FxtranPackage.UPPER_BOUND_TYPE__OP_E:
            this.setOpE((OpEType) null);
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
        case FxtranPackage.UPPER_BOUND_TYPE__LITERAL_E:
            return this.literalE != null;
        case FxtranPackage.UPPER_BOUND_TYPE__NAMED_E:
            return this.namedE != null;
        case FxtranPackage.UPPER_BOUND_TYPE__OP_E:
            return this.opE != null;
        }
        return super.eIsSet(featureID);
    }

} // UpperBoundTypeImpl
