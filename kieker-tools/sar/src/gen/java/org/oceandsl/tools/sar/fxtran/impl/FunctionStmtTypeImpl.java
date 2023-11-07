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

import org.oceandsl.tools.sar.fxtran.DerivedTSpecType;
import org.oceandsl.tools.sar.fxtran.DummyArgLTType;
import org.oceandsl.tools.sar.fxtran.FunctionNType;
import org.oceandsl.tools.sar.fxtran.FunctionStmtType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType;
import org.oceandsl.tools.sar.fxtran.ResultSpecType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Function Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getDerivedTSpec <em>Derived
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getDummyArgLT <em>Dummy Arg
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getFunctionN <em>Function
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getIntrinsicTSpec
 * <em>Intrinsic TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getPrefix
 * <em>Prefix</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl#getResultSpec <em>Result
 * Spec</em>}</li>
 * </ul>
 *
 * @generated
 */
public class FunctionStmtTypeImpl extends MinimalEObjectImpl.Container implements FunctionStmtType {
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
    protected FunctionStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getFunctionStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.FUNCTION_STMT_TYPE__MIXED);
        }
        return this.mixed;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getGroup() {
        return (FeatureMap) this
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getFunctionStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<DerivedTSpecType> getDerivedTSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFunctionStmtType_DerivedTSpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<DummyArgLTType> getDummyArgLT() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFunctionStmtType_DummyArgLT());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<FunctionNType> getFunctionN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFunctionStmtType_FunctionN());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<IntrinsicTSpecType> getIntrinsicTSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFunctionStmtType_IntrinsicTSpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getPrefix() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFunctionStmtType_Prefix());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ResultSpecType> getResultSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getFunctionStmtType_ResultSpec());
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
        case FxtranPackage.FUNCTION_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FUNCTION_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FUNCTION_STMT_TYPE__DERIVED_TSPEC:
            return ((InternalEList<?>) this.getDerivedTSpec()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FUNCTION_STMT_TYPE__DUMMY_ARG_LT:
            return ((InternalEList<?>) this.getDummyArgLT()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FUNCTION_STMT_TYPE__FUNCTION_N:
            return ((InternalEList<?>) this.getFunctionN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FUNCTION_STMT_TYPE__INTRINSIC_TSPEC:
            return ((InternalEList<?>) this.getIntrinsicTSpec()).basicRemove(otherEnd, msgs);
        case FxtranPackage.FUNCTION_STMT_TYPE__RESULT_SPEC:
            return ((InternalEList<?>) this.getResultSpec()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.FUNCTION_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.FUNCTION_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.FUNCTION_STMT_TYPE__DERIVED_TSPEC:
            return this.getDerivedTSpec();
        case FxtranPackage.FUNCTION_STMT_TYPE__DUMMY_ARG_LT:
            return this.getDummyArgLT();
        case FxtranPackage.FUNCTION_STMT_TYPE__FUNCTION_N:
            return this.getFunctionN();
        case FxtranPackage.FUNCTION_STMT_TYPE__INTRINSIC_TSPEC:
            return this.getIntrinsicTSpec();
        case FxtranPackage.FUNCTION_STMT_TYPE__PREFIX:
            return this.getPrefix();
        case FxtranPackage.FUNCTION_STMT_TYPE__RESULT_SPEC:
            return this.getResultSpec();
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
        case FxtranPackage.FUNCTION_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__DERIVED_TSPEC:
            this.getDerivedTSpec().clear();
            this.getDerivedTSpec().addAll((Collection<? extends DerivedTSpecType>) newValue);
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__DUMMY_ARG_LT:
            this.getDummyArgLT().clear();
            this.getDummyArgLT().addAll((Collection<? extends DummyArgLTType>) newValue);
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__FUNCTION_N:
            this.getFunctionN().clear();
            this.getFunctionN().addAll((Collection<? extends FunctionNType>) newValue);
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__INTRINSIC_TSPEC:
            this.getIntrinsicTSpec().clear();
            this.getIntrinsicTSpec().addAll((Collection<? extends IntrinsicTSpecType>) newValue);
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__PREFIX:
            this.getPrefix().clear();
            this.getPrefix().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__RESULT_SPEC:
            this.getResultSpec().clear();
            this.getResultSpec().addAll((Collection<? extends ResultSpecType>) newValue);
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
        case FxtranPackage.FUNCTION_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__DERIVED_TSPEC:
            this.getDerivedTSpec().clear();
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__DUMMY_ARG_LT:
            this.getDummyArgLT().clear();
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__FUNCTION_N:
            this.getFunctionN().clear();
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__INTRINSIC_TSPEC:
            this.getIntrinsicTSpec().clear();
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__PREFIX:
            this.getPrefix().clear();
            return;
        case FxtranPackage.FUNCTION_STMT_TYPE__RESULT_SPEC:
            this.getResultSpec().clear();
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
        case FxtranPackage.FUNCTION_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.FUNCTION_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.FUNCTION_STMT_TYPE__DERIVED_TSPEC:
            return !this.getDerivedTSpec().isEmpty();
        case FxtranPackage.FUNCTION_STMT_TYPE__DUMMY_ARG_LT:
            return !this.getDummyArgLT().isEmpty();
        case FxtranPackage.FUNCTION_STMT_TYPE__FUNCTION_N:
            return !this.getFunctionN().isEmpty();
        case FxtranPackage.FUNCTION_STMT_TYPE__INTRINSIC_TSPEC:
            return !this.getIntrinsicTSpec().isEmpty();
        case FxtranPackage.FUNCTION_STMT_TYPE__PREFIX:
            return !this.getPrefix().isEmpty();
        case FxtranPackage.FUNCTION_STMT_TYPE__RESULT_SPEC:
            return !this.getResultSpec().isEmpty();
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

} // FunctionStmtTypeImpl
