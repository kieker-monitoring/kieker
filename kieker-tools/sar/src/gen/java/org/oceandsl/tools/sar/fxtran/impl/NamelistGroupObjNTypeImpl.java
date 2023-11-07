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
import org.oceandsl.tools.sar.fxtran.NType;
import org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Namelist Group Obj
 * NType</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjNTypeImpl#getN <em>N</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamelistGroupObjNTypeImpl extends MinimalEObjectImpl.Container implements NamelistGroupObjNType {
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
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected NamelistGroupObjNTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getNamelistGroupObjNType();
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
                    FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N, oldN, newN);
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
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N, null, msgs);
            }
            if (newN != null) {
                msgs = ((InternalEObject) newN).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N, null, msgs);
            }
            msgs = this.basicSetN(newN, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N, newN,
                    newN));
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N:
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N:
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N:
            this.setN((NType) newValue);
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N:
            this.setN((NType) null);
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_NTYPE__N:
            return this.n != null;
        }
        return super.eIsSet(featureID);
    }

} // NamelistGroupObjNTypeImpl
