/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.ErrorType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.LabelType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Label Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.LabelTypeImpl#getError <em>Error</em>}</li>
 * </ul>
 *
 * @generated
 */
public class LabelTypeImpl extends MinimalEObjectImpl.Container implements LabelType {
    /**
     * The cached value of the '{@link #getError() <em>Error</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getError()
     * @generated
     * @ordered
     */
    protected ErrorType error;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected LabelTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getLabelType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ErrorType getError() {
        return this.error;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetError(final ErrorType newError, NotificationChain msgs) {
        final ErrorType oldError = this.error;
        this.error = newError;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.LABEL_TYPE__ERROR, oldError, newError);
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
    public void setError(final ErrorType newError) {
        if (newError != this.error) {
            NotificationChain msgs = null;
            if (this.error != null) {
                msgs = ((InternalEObject) this.error).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.LABEL_TYPE__ERROR, null, msgs);
            }
            if (newError != null) {
                msgs = ((InternalEObject) newError).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.LABEL_TYPE__ERROR, null, msgs);
            }
            msgs = this.basicSetError(newError, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.LABEL_TYPE__ERROR, newError, newError));
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
        case FxtranPackage.LABEL_TYPE__ERROR:
            return this.basicSetError(null, msgs);
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
        case FxtranPackage.LABEL_TYPE__ERROR:
            return this.getError();
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
        case FxtranPackage.LABEL_TYPE__ERROR:
            this.setError((ErrorType) newValue);
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
        case FxtranPackage.LABEL_TYPE__ERROR:
            this.setError((ErrorType) null);
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
        case FxtranPackage.LABEL_TYPE__ERROR:
            return this.error != null;
        }
        return super.eIsSet(featureID);
    }

} // LabelTypeImpl
