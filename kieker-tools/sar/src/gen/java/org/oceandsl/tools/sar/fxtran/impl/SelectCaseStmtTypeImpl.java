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

import org.oceandsl.tools.sar.fxtran.CaseEType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.NType;
import org.oceandsl.tools.sar.fxtran.SelectCaseStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Select Case Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SelectCaseStmtTypeImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SelectCaseStmtTypeImpl#getGroup
 * <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SelectCaseStmtTypeImpl#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SelectCaseStmtTypeImpl#getCaseE <em>Case
 * E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SelectCaseStmtTypeImpl extends MinimalEObjectImpl.Container implements SelectCaseStmtType {
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
    protected SelectCaseStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getSelectCaseStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.SELECT_CASE_STMT_TYPE__MIXED);
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
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getSelectCaseStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NType> getN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSelectCaseStmtType_N());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<CaseEType> getCaseE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSelectCaseStmtType_CaseE());
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
        case FxtranPackage.SELECT_CASE_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SELECT_CASE_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SELECT_CASE_STMT_TYPE__N:
            return ((InternalEList<?>) this.getN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SELECT_CASE_STMT_TYPE__CASE_E:
            return ((InternalEList<?>) this.getCaseE()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.SELECT_CASE_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.SELECT_CASE_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.SELECT_CASE_STMT_TYPE__N:
            return this.getN();
        case FxtranPackage.SELECT_CASE_STMT_TYPE__CASE_E:
            return this.getCaseE();
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
        case FxtranPackage.SELECT_CASE_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.SELECT_CASE_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.SELECT_CASE_STMT_TYPE__N:
            this.getN().clear();
            this.getN().addAll((Collection<? extends NType>) newValue);
            return;
        case FxtranPackage.SELECT_CASE_STMT_TYPE__CASE_E:
            this.getCaseE().clear();
            this.getCaseE().addAll((Collection<? extends CaseEType>) newValue);
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
        case FxtranPackage.SELECT_CASE_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.SELECT_CASE_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.SELECT_CASE_STMT_TYPE__N:
            this.getN().clear();
            return;
        case FxtranPackage.SELECT_CASE_STMT_TYPE__CASE_E:
            this.getCaseE().clear();
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
        case FxtranPackage.SELECT_CASE_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.SELECT_CASE_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.SELECT_CASE_STMT_TYPE__N:
            return !this.getN().isEmpty();
        case FxtranPackage.SELECT_CASE_STMT_TYPE__CASE_E:
            return !this.getCaseE().isEmpty();
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

} // SelectCaseStmtTypeImpl
