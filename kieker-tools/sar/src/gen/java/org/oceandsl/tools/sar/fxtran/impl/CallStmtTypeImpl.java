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

import org.oceandsl.tools.sar.fxtran.ArgSpecType;
import org.oceandsl.tools.sar.fxtran.CallStmtType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Call Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CallStmtTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CallStmtTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CallStmtTypeImpl#getArgSpec <em>Arg Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CallStmtTypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CallStmtTypeImpl#getProcedureDesignator
 * <em>Procedure Designator</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CallStmtTypeImpl extends MinimalEObjectImpl.Container implements CallStmtType {
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
    protected CallStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getCallStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.CALL_STMT_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getCallStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArgSpecType> getArgSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCallStmtType_ArgSpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCallStmtType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ProcedureDesignatorType> getProcedureDesignator() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCallStmtType_ProcedureDesignator());
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
        case FxtranPackage.CALL_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CALL_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CALL_STMT_TYPE__ARG_SPEC:
            return ((InternalEList<?>) this.getArgSpec()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CALL_STMT_TYPE__PROCEDURE_DESIGNATOR:
            return ((InternalEList<?>) this.getProcedureDesignator()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.CALL_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.CALL_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.CALL_STMT_TYPE__ARG_SPEC:
            return this.getArgSpec();
        case FxtranPackage.CALL_STMT_TYPE__CNT:
            return this.getCnt();
        case FxtranPackage.CALL_STMT_TYPE__PROCEDURE_DESIGNATOR:
            return this.getProcedureDesignator();
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
        case FxtranPackage.CALL_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.CALL_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.CALL_STMT_TYPE__ARG_SPEC:
            this.getArgSpec().clear();
            this.getArgSpec().addAll((Collection<? extends ArgSpecType>) newValue);
            return;
        case FxtranPackage.CALL_STMT_TYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.CALL_STMT_TYPE__PROCEDURE_DESIGNATOR:
            this.getProcedureDesignator().clear();
            this.getProcedureDesignator().addAll((Collection<? extends ProcedureDesignatorType>) newValue);
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
        case FxtranPackage.CALL_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.CALL_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.CALL_STMT_TYPE__ARG_SPEC:
            this.getArgSpec().clear();
            return;
        case FxtranPackage.CALL_STMT_TYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.CALL_STMT_TYPE__PROCEDURE_DESIGNATOR:
            this.getProcedureDesignator().clear();
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
        case FxtranPackage.CALL_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.CALL_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.CALL_STMT_TYPE__ARG_SPEC:
            return !this.getArgSpec().isEmpty();
        case FxtranPackage.CALL_STMT_TYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.CALL_STMT_TYPE__PROCEDURE_DESIGNATOR:
            return !this.getProcedureDesignator().isEmpty();
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

} // CallStmtTypeImpl
