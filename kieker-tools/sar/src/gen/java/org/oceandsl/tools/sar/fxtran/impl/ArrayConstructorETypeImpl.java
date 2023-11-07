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

import org.oceandsl.tools.sar.fxtran.AcValueLTType;
import org.oceandsl.tools.sar.fxtran.ArrayConstructorEType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Array Constructor
 * EType</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArrayConstructorETypeImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArrayConstructorETypeImpl#getGroup
 * <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArrayConstructorETypeImpl#getC <em>C</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArrayConstructorETypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArrayConstructorETypeImpl#getAcValueLT <em>Ac Value
 * LT</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArrayConstructorETypeImpl extends MinimalEObjectImpl.Container implements ArrayConstructorEType {
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
    protected ArrayConstructorETypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getArrayConstructorEType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__MIXED);
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
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getArrayConstructorEType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getC() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArrayConstructorEType_C());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArrayConstructorEType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<AcValueLTType> getAcValueLT() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArrayConstructorEType_AcValueLT());
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
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__AC_VALUE_LT:
            return ((InternalEList<?>) this.getAcValueLT()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__C:
            return this.getC();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__CNT:
            return this.getCnt();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__AC_VALUE_LT:
            return this.getAcValueLT();
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
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__C:
            this.getC().clear();
            this.getC().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__AC_VALUE_LT:
            this.getAcValueLT().clear();
            this.getAcValueLT().addAll((Collection<? extends AcValueLTType>) newValue);
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
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__C:
            this.getC().clear();
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__AC_VALUE_LT:
            this.getAcValueLT().clear();
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
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__C:
            return !this.getC().isEmpty();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.ARRAY_CONSTRUCTOR_ETYPE__AC_VALUE_LT:
            return !this.getAcValueLT().isEmpty();
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

} // ArrayConstructorETypeImpl
