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

import org.oceandsl.tools.sar.fxtran.AttributeType;
import org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType;
import org.oceandsl.tools.sar.fxtran.ENDeclLTType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.TSpecType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Component Decl Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ComponentDeclStmtTypeImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ComponentDeclStmtTypeImpl#getGroup
 * <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ComponentDeclStmtTypeImpl#getENDeclLT <em>EN Decl
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ComponentDeclStmtTypeImpl#getTSpec
 * <em>TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ComponentDeclStmtTypeImpl#getAttribute
 * <em>Attribute</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ComponentDeclStmtTypeImpl extends MinimalEObjectImpl.Container implements ComponentDeclStmtType {
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
    protected ComponentDeclStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getComponentDeclStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.COMPONENT_DECL_STMT_TYPE__MIXED);
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
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getComponentDeclStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ENDeclLTType> getENDeclLT() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getComponentDeclStmtType_ENDeclLT());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<TSpecType> getTSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getComponentDeclStmtType_TSpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<AttributeType> getAttribute() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getComponentDeclStmtType_Attribute());
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
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__EN_DECL_LT:
            return ((InternalEList<?>) this.getENDeclLT()).basicRemove(otherEnd, msgs);
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__TSPEC:
            return ((InternalEList<?>) this.getTSpec()).basicRemove(otherEnd, msgs);
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__ATTRIBUTE:
            return ((InternalEList<?>) this.getAttribute()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__EN_DECL_LT:
            return this.getENDeclLT();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__TSPEC:
            return this.getTSpec();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__ATTRIBUTE:
            return this.getAttribute();
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
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__EN_DECL_LT:
            this.getENDeclLT().clear();
            this.getENDeclLT().addAll((Collection<? extends ENDeclLTType>) newValue);
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__TSPEC:
            this.getTSpec().clear();
            this.getTSpec().addAll((Collection<? extends TSpecType>) newValue);
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__ATTRIBUTE:
            this.getAttribute().clear();
            this.getAttribute().addAll((Collection<? extends AttributeType>) newValue);
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
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__EN_DECL_LT:
            this.getENDeclLT().clear();
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__TSPEC:
            this.getTSpec().clear();
            return;
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__ATTRIBUTE:
            this.getAttribute().clear();
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
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__EN_DECL_LT:
            return !this.getENDeclLT().isEmpty();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__TSPEC:
            return !this.getTSpec().isEmpty();
        case FxtranPackage.COMPONENT_DECL_STMT_TYPE__ATTRIBUTE:
            return !this.getAttribute().isEmpty();
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

} // ComponentDeclStmtTypeImpl
