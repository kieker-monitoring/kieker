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
import org.oceandsl.tools.sar.fxtran.ElementLTType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.ParensRType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Parens RType</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ParensRTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ParensRTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ParensRTypeImpl#getArgSpec <em>Arg Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ParensRTypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ParensRTypeImpl#getElementLT <em>Element
 * LT</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ParensRTypeImpl extends MinimalEObjectImpl.Container implements ParensRType {
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
    protected ParensRTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getParensRType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.PARENS_RTYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getParensRType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArgSpecType> getArgSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getParensRType_ArgSpec());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getParensRType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ElementLTType> getElementLT() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getParensRType_ElementLT());
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
        case FxtranPackage.PARENS_RTYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.PARENS_RTYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.PARENS_RTYPE__ARG_SPEC:
            return ((InternalEList<?>) this.getArgSpec()).basicRemove(otherEnd, msgs);
        case FxtranPackage.PARENS_RTYPE__ELEMENT_LT:
            return ((InternalEList<?>) this.getElementLT()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.PARENS_RTYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.PARENS_RTYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.PARENS_RTYPE__ARG_SPEC:
            return this.getArgSpec();
        case FxtranPackage.PARENS_RTYPE__CNT:
            return this.getCnt();
        case FxtranPackage.PARENS_RTYPE__ELEMENT_LT:
            return this.getElementLT();
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
        case FxtranPackage.PARENS_RTYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.PARENS_RTYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.PARENS_RTYPE__ARG_SPEC:
            this.getArgSpec().clear();
            this.getArgSpec().addAll((Collection<? extends ArgSpecType>) newValue);
            return;
        case FxtranPackage.PARENS_RTYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.PARENS_RTYPE__ELEMENT_LT:
            this.getElementLT().clear();
            this.getElementLT().addAll((Collection<? extends ElementLTType>) newValue);
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
        case FxtranPackage.PARENS_RTYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.PARENS_RTYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.PARENS_RTYPE__ARG_SPEC:
            this.getArgSpec().clear();
            return;
        case FxtranPackage.PARENS_RTYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.PARENS_RTYPE__ELEMENT_LT:
            this.getElementLT().clear();
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
        case FxtranPackage.PARENS_RTYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.PARENS_RTYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.PARENS_RTYPE__ARG_SPEC:
            return !this.getArgSpec().isEmpty();
        case FxtranPackage.PARENS_RTYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.PARENS_RTYPE__ELEMENT_LT:
            return !this.getElementLT().isEmpty();
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

} // ParensRTypeImpl
