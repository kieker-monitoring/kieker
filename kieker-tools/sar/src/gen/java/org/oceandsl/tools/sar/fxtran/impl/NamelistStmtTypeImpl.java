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
import org.oceandsl.tools.sar.fxtran.NamelistGroupNType;
import org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType;
import org.oceandsl.tools.sar.fxtran.NamelistStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Namelist Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistStmtTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistStmtTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistStmtTypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistStmtTypeImpl#getNamelistGroupN <em>Namelist
 * Group N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NamelistStmtTypeImpl#getNamelistGroupObjLT
 * <em>Namelist Group Obj LT</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NamelistStmtTypeImpl extends MinimalEObjectImpl.Container implements NamelistStmtType {
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
    protected NamelistStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getNamelistStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.NAMELIST_STMT_TYPE__MIXED);
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
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getNamelistStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getNamelistStmtType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamelistGroupNType> getNamelistGroupN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getNamelistStmtType_NamelistGroupN());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamelistGroupObjLTType> getNamelistGroupObjLT() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getNamelistStmtType_NamelistGroupObjLT());
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
        case FxtranPackage.NAMELIST_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.NAMELIST_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_N:
            return ((InternalEList<?>) this.getNamelistGroupN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_OBJ_LT:
            return ((InternalEList<?>) this.getNamelistGroupObjLT()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.NAMELIST_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.NAMELIST_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.NAMELIST_STMT_TYPE__CNT:
            return this.getCnt();
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_N:
            return this.getNamelistGroupN();
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_OBJ_LT:
            return this.getNamelistGroupObjLT();
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
        case FxtranPackage.NAMELIST_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_N:
            this.getNamelistGroupN().clear();
            this.getNamelistGroupN().addAll((Collection<? extends NamelistGroupNType>) newValue);
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_OBJ_LT:
            this.getNamelistGroupObjLT().clear();
            this.getNamelistGroupObjLT().addAll((Collection<? extends NamelistGroupObjLTType>) newValue);
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
        case FxtranPackage.NAMELIST_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_N:
            this.getNamelistGroupN().clear();
            return;
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_OBJ_LT:
            this.getNamelistGroupObjLT().clear();
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
        case FxtranPackage.NAMELIST_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.NAMELIST_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.NAMELIST_STMT_TYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_N:
            return !this.getNamelistGroupN().isEmpty();
        case FxtranPackage.NAMELIST_STMT_TYPE__NAMELIST_GROUP_OBJ_LT:
            return !this.getNamelistGroupObjLT().isEmpty();
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

} // NamelistStmtTypeImpl
