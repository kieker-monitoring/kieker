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
import org.oceandsl.tools.sar.fxtran.InitEType;
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.NamedEType;
import org.oceandsl.tools.sar.fxtran.OpEType;
import org.oceandsl.tools.sar.fxtran.StringEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Init EType</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.InitETypeImpl#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.InitETypeImpl#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.InitETypeImpl#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.InitETypeImpl#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InitETypeImpl extends MinimalEObjectImpl.Container implements InitEType {
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
    protected InitETypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getInitEType();
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
                    FxtranPackage.INIT_ETYPE__LITERAL_E, oldLiteralE, newLiteralE);
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
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__LITERAL_E, null, msgs);
            }
            if (newLiteralE != null) {
                msgs = ((InternalEObject) newLiteralE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__LITERAL_E, null, msgs);
            }
            msgs = this.basicSetLiteralE(newLiteralE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.INIT_ETYPE__LITERAL_E, newLiteralE,
                    newLiteralE));
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
                    FxtranPackage.INIT_ETYPE__NAMED_E, oldNamedE, newNamedE);
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
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__NAMED_E, null, msgs);
            }
            if (newNamedE != null) {
                msgs = ((InternalEObject) newNamedE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__NAMED_E, null, msgs);
            }
            msgs = this.basicSetNamedE(newNamedE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.INIT_ETYPE__NAMED_E, newNamedE,
                    newNamedE));
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
                    FxtranPackage.INIT_ETYPE__OP_E, oldOpE, newOpE);
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
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__OP_E, null, msgs);
            }
            if (newOpE != null) {
                msgs = ((InternalEObject) newOpE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__OP_E, null, msgs);
            }
            msgs = this.basicSetOpE(newOpE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.INIT_ETYPE__OP_E, newOpE, newOpE));
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
                    FxtranPackage.INIT_ETYPE__STRING_E, oldStringE, newStringE);
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
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__STRING_E, null, msgs);
            }
            if (newStringE != null) {
                msgs = ((InternalEObject) newStringE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.INIT_ETYPE__STRING_E, null, msgs);
            }
            msgs = this.basicSetStringE(newStringE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.INIT_ETYPE__STRING_E, newStringE,
                    newStringE));
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
        case FxtranPackage.INIT_ETYPE__LITERAL_E:
            return this.basicSetLiteralE(null, msgs);
        case FxtranPackage.INIT_ETYPE__NAMED_E:
            return this.basicSetNamedE(null, msgs);
        case FxtranPackage.INIT_ETYPE__OP_E:
            return this.basicSetOpE(null, msgs);
        case FxtranPackage.INIT_ETYPE__STRING_E:
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
        case FxtranPackage.INIT_ETYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.INIT_ETYPE__NAMED_E:
            return this.getNamedE();
        case FxtranPackage.INIT_ETYPE__OP_E:
            return this.getOpE();
        case FxtranPackage.INIT_ETYPE__STRING_E:
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
        case FxtranPackage.INIT_ETYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) newValue);
            return;
        case FxtranPackage.INIT_ETYPE__NAMED_E:
            this.setNamedE((NamedEType) newValue);
            return;
        case FxtranPackage.INIT_ETYPE__OP_E:
            this.setOpE((OpEType) newValue);
            return;
        case FxtranPackage.INIT_ETYPE__STRING_E:
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
        case FxtranPackage.INIT_ETYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) null);
            return;
        case FxtranPackage.INIT_ETYPE__NAMED_E:
            this.setNamedE((NamedEType) null);
            return;
        case FxtranPackage.INIT_ETYPE__OP_E:
            this.setOpE((OpEType) null);
            return;
        case FxtranPackage.INIT_ETYPE__STRING_E:
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
        case FxtranPackage.INIT_ETYPE__LITERAL_E:
            return this.literalE != null;
        case FxtranPackage.INIT_ETYPE__NAMED_E:
            return this.namedE != null;
        case FxtranPackage.INIT_ETYPE__OP_E:
            return this.opE != null;
        case FxtranPackage.INIT_ETYPE__STRING_E:
            return this.stringE != null;
        }
        return super.eIsSet(featureID);
    }

} // InitETypeImpl
