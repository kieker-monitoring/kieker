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
import org.oceandsl.tools.sar.fxtran.IoControlSpecType;
import org.oceandsl.tools.sar.fxtran.OutputItemLTType;
import org.oceandsl.tools.sar.fxtran.WriteStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Write Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WriteStmtTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WriteStmtTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WriteStmtTypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WriteStmtTypeImpl#getIoControlSpec <em>Io Control
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.WriteStmtTypeImpl#getOutputItemLT <em>Output Item
 * LT</em>}</li>
 * </ul>
 *
 * @generated
 */
public class WriteStmtTypeImpl extends MinimalEObjectImpl.Container implements WriteStmtType {
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
    protected WriteStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getWriteStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.WRITE_STMT_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getWriteStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getWriteStmtType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<IoControlSpecType> getIoControlSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getWriteStmtType_IoControlSpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OutputItemLTType> getOutputItemLT() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getWriteStmtType_OutputItemLT());
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
        case FxtranPackage.WRITE_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.WRITE_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.WRITE_STMT_TYPE__IO_CONTROL_SPEC:
            return ((InternalEList<?>) this.getIoControlSpec()).basicRemove(otherEnd, msgs);
        case FxtranPackage.WRITE_STMT_TYPE__OUTPUT_ITEM_LT:
            return ((InternalEList<?>) this.getOutputItemLT()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.WRITE_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.WRITE_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.WRITE_STMT_TYPE__CNT:
            return this.getCnt();
        case FxtranPackage.WRITE_STMT_TYPE__IO_CONTROL_SPEC:
            return this.getIoControlSpec();
        case FxtranPackage.WRITE_STMT_TYPE__OUTPUT_ITEM_LT:
            return this.getOutputItemLT();
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
        case FxtranPackage.WRITE_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.WRITE_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.WRITE_STMT_TYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.WRITE_STMT_TYPE__IO_CONTROL_SPEC:
            this.getIoControlSpec().clear();
            this.getIoControlSpec().addAll((Collection<? extends IoControlSpecType>) newValue);
            return;
        case FxtranPackage.WRITE_STMT_TYPE__OUTPUT_ITEM_LT:
            this.getOutputItemLT().clear();
            this.getOutputItemLT().addAll((Collection<? extends OutputItemLTType>) newValue);
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
        case FxtranPackage.WRITE_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.WRITE_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.WRITE_STMT_TYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.WRITE_STMT_TYPE__IO_CONTROL_SPEC:
            this.getIoControlSpec().clear();
            return;
        case FxtranPackage.WRITE_STMT_TYPE__OUTPUT_ITEM_LT:
            this.getOutputItemLT().clear();
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
        case FxtranPackage.WRITE_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.WRITE_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.WRITE_STMT_TYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.WRITE_STMT_TYPE__IO_CONTROL_SPEC:
            return !this.getIoControlSpec().isEmpty();
        case FxtranPackage.WRITE_STMT_TYPE__OUTPUT_ITEM_LT:
            return !this.getOutputItemLT().isEmpty();
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

} // WriteStmtTypeImpl
