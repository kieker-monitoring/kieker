/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.InquireStmtType;
import org.oceandsl.tools.sar.fxtran.InquirySpecSpecType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Inquire Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.InquireStmtTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.InquireStmtTypeImpl#getInquirySpecSpec <em>Inquiry
 * Spec Spec</em>}</li>
 * </ul>
 *
 * @generated
 */
public class InquireStmtTypeImpl extends MinimalEObjectImpl.Container implements InquireStmtType {
    /**
     * The cached value of the '{@link #getMixed() <em>Mixed</em>}' attribute list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getMixed()
     * @generated
     * @ordered
     */
    protected FeatureMap mixed;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected InquireStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getInquireStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.INQUIRE_STMT_TYPE__MIXED);
        }
        return this.mixed;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<InquirySpecSpecType> getInquirySpecSpec() {
        return this.getMixed().list(FxtranPackage.eINSTANCE.getInquireStmtType_InquirySpecSpec());
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
        case FxtranPackage.INQUIRE_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.INQUIRE_STMT_TYPE__INQUIRY_SPEC_SPEC:
            return ((InternalEList<?>) this.getInquirySpecSpec()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.INQUIRE_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.INQUIRE_STMT_TYPE__INQUIRY_SPEC_SPEC:
            return this.getInquirySpecSpec();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(final int featureID, final Object newValue) {
        switch (featureID) {
        case FxtranPackage.INQUIRE_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.INQUIRE_STMT_TYPE__INQUIRY_SPEC_SPEC:
            this.getInquirySpecSpec().clear();
            this.getInquirySpecSpec().addAll((Collection<? extends InquirySpecSpecType>) newValue);
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
        case FxtranPackage.INQUIRE_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.INQUIRE_STMT_TYPE__INQUIRY_SPEC_SPEC:
            this.getInquirySpecSpec().clear();
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
        case FxtranPackage.INQUIRE_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.INQUIRE_STMT_TYPE__INQUIRY_SPEC_SPEC:
            return !this.getInquirySpecSpec().isEmpty();
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        final StringBuilder result = new StringBuilder(super.toString());
        result.append(" (mixed: ");
        result.append(this.mixed);
        result.append(')');
        return result.toString();
    }

} // InquireStmtTypeImpl
