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

import org.oceandsl.tools.sar.fxtran.DummyArgLTType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.SubroutineNType;
import org.oceandsl.tools.sar.fxtran.SubroutineStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Subroutine Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl#getGroup
 * <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl#getDummyArgLT <em>Dummy Arg
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl#getPrefix
 * <em>Prefix</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl#getSubroutineN
 * <em>Subroutine N</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SubroutineStmtTypeImpl extends MinimalEObjectImpl.Container implements SubroutineStmtType {
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
    protected SubroutineStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getSubroutineStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.SUBROUTINE_STMT_TYPE__MIXED);
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
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getSubroutineStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSubroutineStmtType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<DummyArgLTType> getDummyArgLT() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSubroutineStmtType_DummyArgLT());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getPrefix() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSubroutineStmtType_Prefix());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<SubroutineNType> getSubroutineN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSubroutineStmtType_SubroutineN());
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
        case FxtranPackage.SUBROUTINE_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SUBROUTINE_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SUBROUTINE_STMT_TYPE__DUMMY_ARG_LT:
            return ((InternalEList<?>) this.getDummyArgLT()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SUBROUTINE_STMT_TYPE__SUBROUTINE_N:
            return ((InternalEList<?>) this.getSubroutineN()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.SUBROUTINE_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__CNT:
            return this.getCnt();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__DUMMY_ARG_LT:
            return this.getDummyArgLT();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__PREFIX:
            return this.getPrefix();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__SUBROUTINE_N:
            return this.getSubroutineN();
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
        case FxtranPackage.SUBROUTINE_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__DUMMY_ARG_LT:
            this.getDummyArgLT().clear();
            this.getDummyArgLT().addAll((Collection<? extends DummyArgLTType>) newValue);
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__PREFIX:
            this.getPrefix().clear();
            this.getPrefix().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__SUBROUTINE_N:
            this.getSubroutineN().clear();
            this.getSubroutineN().addAll((Collection<? extends SubroutineNType>) newValue);
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
        case FxtranPackage.SUBROUTINE_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__DUMMY_ARG_LT:
            this.getDummyArgLT().clear();
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__PREFIX:
            this.getPrefix().clear();
            return;
        case FxtranPackage.SUBROUTINE_STMT_TYPE__SUBROUTINE_N:
            this.getSubroutineN().clear();
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
        case FxtranPackage.SUBROUTINE_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__DUMMY_ARG_LT:
            return !this.getDummyArgLT().isEmpty();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__PREFIX:
            return !this.getPrefix().isEmpty();
        case FxtranPackage.SUBROUTINE_STMT_TYPE__SUBROUTINE_N:
            return !this.getSubroutineN().isEmpty();
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

} // SubroutineStmtTypeImpl
