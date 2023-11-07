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
import org.oceandsl.tools.sar.fxtran.LowerBoundType;
import org.oceandsl.tools.sar.fxtran.SectionSubscriptType;
import org.oceandsl.tools.sar.fxtran.UpperBoundType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Section Subscript
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptTypeImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptTypeImpl#getGroup
 * <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptTypeImpl#getLowerBound <em>Lower
 * Bound</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptTypeImpl#getUpperBound <em>Upper
 * Bound</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SectionSubscriptTypeImpl extends MinimalEObjectImpl.Container implements SectionSubscriptType {
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
    protected SectionSubscriptTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getSectionSubscriptType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.SECTION_SUBSCRIPT_TYPE__MIXED);
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
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getSectionSubscriptType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<LowerBoundType> getLowerBound() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSectionSubscriptType_LowerBound());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<UpperBoundType> getUpperBound() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getSectionSubscriptType_UpperBound());
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
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__LOWER_BOUND:
            return ((InternalEList<?>) this.getLowerBound()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__UPPER_BOUND:
            return ((InternalEList<?>) this.getUpperBound()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__LOWER_BOUND:
            return this.getLowerBound();
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__UPPER_BOUND:
            return this.getUpperBound();
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
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__LOWER_BOUND:
            this.getLowerBound().clear();
            this.getLowerBound().addAll((Collection<? extends LowerBoundType>) newValue);
            return;
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__UPPER_BOUND:
            this.getUpperBound().clear();
            this.getUpperBound().addAll((Collection<? extends UpperBoundType>) newValue);
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
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__LOWER_BOUND:
            this.getLowerBound().clear();
            return;
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__UPPER_BOUND:
            this.getUpperBound().clear();
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
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__LOWER_BOUND:
            return !this.getLowerBound().isEmpty();
        case FxtranPackage.SECTION_SUBSCRIPT_TYPE__UPPER_BOUND:
            return !this.getUpperBound().isEmpty();
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

} // SectionSubscriptTypeImpl
