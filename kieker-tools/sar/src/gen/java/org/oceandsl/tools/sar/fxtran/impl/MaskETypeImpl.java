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
import org.oceandsl.tools.sar.fxtran.MaskEType;
import org.oceandsl.tools.sar.fxtran.OpEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Mask EType</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.MaskETypeImpl#getOpE <em>Op E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MaskETypeImpl extends MinimalEObjectImpl.Container implements MaskEType {
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
    protected MaskETypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getMaskEType();
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
                    FxtranPackage.MASK_ETYPE__OP_E, oldOpE, newOpE);
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
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.MASK_ETYPE__OP_E, null, msgs);
            }
            if (newOpE != null) {
                msgs = ((InternalEObject) newOpE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.MASK_ETYPE__OP_E, null, msgs);
            }
            msgs = this.basicSetOpE(newOpE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.MASK_ETYPE__OP_E, newOpE, newOpE));
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
        case FxtranPackage.MASK_ETYPE__OP_E:
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
        case FxtranPackage.MASK_ETYPE__OP_E:
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
        case FxtranPackage.MASK_ETYPE__OP_E:
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
        case FxtranPackage.MASK_ETYPE__OP_E:
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
        case FxtranPackage.MASK_ETYPE__OP_E:
            return this.opE != null;
        }
        return super.eIsSet(featureID);
    }

} // MaskETypeImpl
