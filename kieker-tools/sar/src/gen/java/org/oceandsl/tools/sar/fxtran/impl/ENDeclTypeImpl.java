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

import org.oceandsl.tools.sar.fxtran.ArraySpecType;
import org.oceandsl.tools.sar.fxtran.ENDeclType;
import org.oceandsl.tools.sar.fxtran.ENNType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.InitEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>EN Decl Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ENDeclTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ENDeclTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ENDeclTypeImpl#getArraySpec <em>Array
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ENDeclTypeImpl#getENN <em>ENN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ENDeclTypeImpl#getInitE <em>Init E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ENDeclTypeImpl extends MinimalEObjectImpl.Container implements ENDeclType {
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
    protected ENDeclTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getENDeclType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.EN_DECL_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getENDeclType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArraySpecType> getArraySpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getENDeclType_ArraySpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ENNType> getENN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getENDeclType_ENN());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<InitEType> getInitE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getENDeclType_InitE());
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
        case FxtranPackage.EN_DECL_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.EN_DECL_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.EN_DECL_TYPE__ARRAY_SPEC:
            return ((InternalEList<?>) this.getArraySpec()).basicRemove(otherEnd, msgs);
        case FxtranPackage.EN_DECL_TYPE__ENN:
            return ((InternalEList<?>) this.getENN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.EN_DECL_TYPE__INIT_E:
            return ((InternalEList<?>) this.getInitE()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.EN_DECL_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.EN_DECL_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.EN_DECL_TYPE__ARRAY_SPEC:
            return this.getArraySpec();
        case FxtranPackage.EN_DECL_TYPE__ENN:
            return this.getENN();
        case FxtranPackage.EN_DECL_TYPE__INIT_E:
            return this.getInitE();
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
        case FxtranPackage.EN_DECL_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.EN_DECL_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.EN_DECL_TYPE__ARRAY_SPEC:
            this.getArraySpec().clear();
            this.getArraySpec().addAll((Collection<? extends ArraySpecType>) newValue);
            return;
        case FxtranPackage.EN_DECL_TYPE__ENN:
            this.getENN().clear();
            this.getENN().addAll((Collection<? extends ENNType>) newValue);
            return;
        case FxtranPackage.EN_DECL_TYPE__INIT_E:
            this.getInitE().clear();
            this.getInitE().addAll((Collection<? extends InitEType>) newValue);
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
        case FxtranPackage.EN_DECL_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.EN_DECL_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.EN_DECL_TYPE__ARRAY_SPEC:
            this.getArraySpec().clear();
            return;
        case FxtranPackage.EN_DECL_TYPE__ENN:
            this.getENN().clear();
            return;
        case FxtranPackage.EN_DECL_TYPE__INIT_E:
            this.getInitE().clear();
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
        case FxtranPackage.EN_DECL_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.EN_DECL_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.EN_DECL_TYPE__ARRAY_SPEC:
            return !this.getArraySpec().isEmpty();
        case FxtranPackage.EN_DECL_TYPE__ENN:
            return !this.getENN().isEmpty();
        case FxtranPackage.EN_DECL_TYPE__INIT_E:
            return !this.getInitE().isEmpty();
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

} // ENDeclTypeImpl
