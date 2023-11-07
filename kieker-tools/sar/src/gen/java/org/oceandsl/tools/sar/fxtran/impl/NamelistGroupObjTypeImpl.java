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
import org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType;
import org.oceandsl.tools.sar.fxtran.NamelistGroupObjType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Namelist Group Obj
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjTypeImpl#getNamelistGroupObjN
 * <em>Namelist Group Obj N</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamelistGroupObjTypeImpl extends MinimalEObjectImpl.Container implements NamelistGroupObjType {
    /**
     * The cached value of the '{@link #getNamelistGroupObjN() <em>Namelist Group Obj N</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getNamelistGroupObjN()
     * @generated
     * @ordered
     */
    protected NamelistGroupObjNType namelistGroupObjN;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected NamelistGroupObjTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getNamelistGroupObjType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NamelistGroupObjNType getNamelistGroupObjN() {
        return this.namelistGroupObjN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetNamelistGroupObjN(final NamelistGroupObjNType newNamelistGroupObjN,
            NotificationChain msgs) {
        final NamelistGroupObjNType oldNamelistGroupObjN = this.namelistGroupObjN;
        this.namelistGroupObjN = newNamelistGroupObjN;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N, oldNamelistGroupObjN,
                    newNamelistGroupObjN);
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
    public void setNamelistGroupObjN(final NamelistGroupObjNType newNamelistGroupObjN) {
        if (newNamelistGroupObjN != this.namelistGroupObjN) {
            NotificationChain msgs = null;
            if (this.namelistGroupObjN != null) {
                msgs = ((InternalEObject) this.namelistGroupObjN).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N, null,
                        msgs);
            }
            if (newNamelistGroupObjN != null) {
                msgs = ((InternalEObject) newNamelistGroupObjN).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N, null,
                        msgs);
            }
            msgs = this.basicSetNamelistGroupObjN(newNamelistGroupObjN, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N, newNamelistGroupObjN,
                    newNamelistGroupObjN));
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N:
            return this.basicSetNamelistGroupObjN(null, msgs);
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N:
            return this.getNamelistGroupObjN();
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N:
            this.setNamelistGroupObjN((NamelistGroupObjNType) newValue);
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N:
            this.setNamelistGroupObjN((NamelistGroupObjNType) null);
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
        case FxtranPackage.NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N:
            return this.namelistGroupObjN != null;
        }
        return super.eIsSet(featureID);
    }

} // NamelistGroupObjTypeImpl
