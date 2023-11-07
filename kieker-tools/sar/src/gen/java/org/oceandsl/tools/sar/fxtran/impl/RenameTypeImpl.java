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
import org.oceandsl.tools.sar.fxtran.RenameType;
import org.oceandsl.tools.sar.fxtran.UseNType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Rename Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.RenameTypeImpl#getUseN <em>Use N</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RenameTypeImpl extends MinimalEObjectImpl.Container implements RenameType {
    /**
     * The cached value of the '{@link #getUseN() <em>Use N</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getUseN()
     * @generated
     * @ordered
     */
    protected UseNType useN;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected RenameTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getRenameType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public UseNType getUseN() {
        return this.useN;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetUseN(final UseNType newUseN, NotificationChain msgs) {
        final UseNType oldUseN = this.useN;
        this.useN = newUseN;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.RENAME_TYPE__USE_N, oldUseN, newUseN);
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
    public void setUseN(final UseNType newUseN) {
        if (newUseN != this.useN) {
            NotificationChain msgs = null;
            if (this.useN != null) {
                msgs = ((InternalEObject) this.useN).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.RENAME_TYPE__USE_N, null, msgs);
            }
            if (newUseN != null) {
                msgs = ((InternalEObject) newUseN).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.RENAME_TYPE__USE_N, null, msgs);
            }
            msgs = this.basicSetUseN(newUseN, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.RENAME_TYPE__USE_N, newUseN, newUseN));
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
        case FxtranPackage.RENAME_TYPE__USE_N:
            return this.basicSetUseN(null, msgs);
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
        case FxtranPackage.RENAME_TYPE__USE_N:
            return this.getUseN();
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
        case FxtranPackage.RENAME_TYPE__USE_N:
            this.setUseN((UseNType) newValue);
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
        case FxtranPackage.RENAME_TYPE__USE_N:
            this.setUseN((UseNType) null);
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
        case FxtranPackage.RENAME_TYPE__USE_N:
            return this.useN != null;
        }
        return super.eIsSet(featureID);
    }

} // RenameTypeImpl
