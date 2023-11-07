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
import org.oceandsl.tools.sar.fxtran.ShapeSpecType;
import org.oceandsl.tools.sar.fxtran.UpperBoundType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Shape Spec Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ShapeSpecTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ShapeSpecTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ShapeSpecTypeImpl#getLowerBound <em>Lower
 * Bound</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ShapeSpecTypeImpl#getUpperBound <em>Upper
 * Bound</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ShapeSpecTypeImpl extends MinimalEObjectImpl.Container implements ShapeSpecType {
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
    protected ShapeSpecTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getShapeSpecType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.SHAPE_SPEC_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getShapeSpecType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<LowerBoundType> getLowerBound() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getShapeSpecType_LowerBound());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<UpperBoundType> getUpperBound() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getShapeSpecType_UpperBound());
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
        case FxtranPackage.SHAPE_SPEC_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SHAPE_SPEC_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SHAPE_SPEC_TYPE__LOWER_BOUND:
            return ((InternalEList<?>) this.getLowerBound()).basicRemove(otherEnd, msgs);
        case FxtranPackage.SHAPE_SPEC_TYPE__UPPER_BOUND:
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
        case FxtranPackage.SHAPE_SPEC_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.SHAPE_SPEC_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.SHAPE_SPEC_TYPE__LOWER_BOUND:
            return this.getLowerBound();
        case FxtranPackage.SHAPE_SPEC_TYPE__UPPER_BOUND:
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
        case FxtranPackage.SHAPE_SPEC_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.SHAPE_SPEC_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.SHAPE_SPEC_TYPE__LOWER_BOUND:
            this.getLowerBound().clear();
            this.getLowerBound().addAll((Collection<? extends LowerBoundType>) newValue);
            return;
        case FxtranPackage.SHAPE_SPEC_TYPE__UPPER_BOUND:
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
        case FxtranPackage.SHAPE_SPEC_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.SHAPE_SPEC_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.SHAPE_SPEC_TYPE__LOWER_BOUND:
            this.getLowerBound().clear();
            return;
        case FxtranPackage.SHAPE_SPEC_TYPE__UPPER_BOUND:
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
        case FxtranPackage.SHAPE_SPEC_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.SHAPE_SPEC_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.SHAPE_SPEC_TYPE__LOWER_BOUND:
            return !this.getLowerBound().isEmpty();
        case FxtranPackage.SHAPE_SPEC_TYPE__UPPER_BOUND:
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

} // ShapeSpecTypeImpl
