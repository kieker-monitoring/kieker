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

import org.oceandsl.tools.sar.fxtran.ActionStmtType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.MaskEType;
import org.oceandsl.tools.sar.fxtran.WhereStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Where Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WhereStmtTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WhereStmtTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WhereStmtTypeImpl#getActionStmt <em>Action
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WhereStmtTypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WhereStmtTypeImpl#getMaskE <em>Mask E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WhereStmtTypeImpl extends MinimalEObjectImpl.Container implements WhereStmtType {
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
    protected WhereStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getWhereStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.WHERE_STMT_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getWhereStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ActionStmtType> getActionStmt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getWhereStmtType_ActionStmt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getWhereStmtType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<MaskEType> getMaskE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getWhereStmtType_MaskE());
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
        case FxtranPackage.WHERE_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.WHERE_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.WHERE_STMT_TYPE__ACTION_STMT:
            return ((InternalEList<?>) this.getActionStmt()).basicRemove(otherEnd, msgs);
        case FxtranPackage.WHERE_STMT_TYPE__MASK_E:
            return ((InternalEList<?>) this.getMaskE()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.WHERE_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.WHERE_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.WHERE_STMT_TYPE__ACTION_STMT:
            return this.getActionStmt();
        case FxtranPackage.WHERE_STMT_TYPE__CNT:
            return this.getCnt();
        case FxtranPackage.WHERE_STMT_TYPE__MASK_E:
            return this.getMaskE();
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
        case FxtranPackage.WHERE_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.WHERE_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.WHERE_STMT_TYPE__ACTION_STMT:
            this.getActionStmt().clear();
            this.getActionStmt().addAll((Collection<? extends ActionStmtType>) newValue);
            return;
        case FxtranPackage.WHERE_STMT_TYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.WHERE_STMT_TYPE__MASK_E:
            this.getMaskE().clear();
            this.getMaskE().addAll((Collection<? extends MaskEType>) newValue);
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
        case FxtranPackage.WHERE_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.WHERE_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.WHERE_STMT_TYPE__ACTION_STMT:
            this.getActionStmt().clear();
            return;
        case FxtranPackage.WHERE_STMT_TYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.WHERE_STMT_TYPE__MASK_E:
            this.getMaskE().clear();
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
        case FxtranPackage.WHERE_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.WHERE_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.WHERE_STMT_TYPE__ACTION_STMT:
            return !this.getActionStmt().isEmpty();
        case FxtranPackage.WHERE_STMT_TYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.WHERE_STMT_TYPE__MASK_E:
            return !this.getMaskE().isEmpty();
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

} // WhereStmtTypeImpl
