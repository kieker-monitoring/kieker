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
import org.oceandsl.tools.sar.fxtran.AttributeType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Attribute Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.AttributeTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.AttributeTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.AttributeTypeImpl#getArraySpec <em>Array
 * Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.AttributeTypeImpl#getAttributeN <em>Attribute
 * N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.AttributeTypeImpl#getIntentSpec <em>Intent
 * Spec</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AttributeTypeImpl extends MinimalEObjectImpl.Container implements AttributeType {
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
    protected AttributeTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getAttributeType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.ATTRIBUTE_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getAttributeType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArraySpecType> getArraySpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getAttributeType_ArraySpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getAttributeN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getAttributeType_AttributeN());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getIntentSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getAttributeType_IntentSpec());
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
        case FxtranPackage.ATTRIBUTE_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ATTRIBUTE_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ATTRIBUTE_TYPE__ARRAY_SPEC:
            return ((InternalEList<?>) this.getArraySpec()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.ATTRIBUTE_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.ATTRIBUTE_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.ATTRIBUTE_TYPE__ARRAY_SPEC:
            return this.getArraySpec();
        case FxtranPackage.ATTRIBUTE_TYPE__ATTRIBUTE_N:
            return this.getAttributeN();
        case FxtranPackage.ATTRIBUTE_TYPE__INTENT_SPEC:
            return this.getIntentSpec();
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
        case FxtranPackage.ATTRIBUTE_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__ARRAY_SPEC:
            this.getArraySpec().clear();
            this.getArraySpec().addAll((Collection<? extends ArraySpecType>) newValue);
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__ATTRIBUTE_N:
            this.getAttributeN().clear();
            this.getAttributeN().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__INTENT_SPEC:
            this.getIntentSpec().clear();
            this.getIntentSpec().addAll((Collection<? extends String>) newValue);
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
        case FxtranPackage.ATTRIBUTE_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__ARRAY_SPEC:
            this.getArraySpec().clear();
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__ATTRIBUTE_N:
            this.getAttributeN().clear();
            return;
        case FxtranPackage.ATTRIBUTE_TYPE__INTENT_SPEC:
            this.getIntentSpec().clear();
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
        case FxtranPackage.ATTRIBUTE_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.ATTRIBUTE_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.ATTRIBUTE_TYPE__ARRAY_SPEC:
            return !this.getArraySpec().isEmpty();
        case FxtranPackage.ATTRIBUTE_TYPE__ATTRIBUTE_N:
            return !this.getAttributeN().isEmpty();
        case FxtranPackage.ATTRIBUTE_TYPE__INTENT_SPEC:
            return !this.getIntentSpec().isEmpty();
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

} // AttributeTypeImpl
