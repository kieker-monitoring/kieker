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

import org.oceandsl.tools.sar.fxtran.ArgNType;
import org.oceandsl.tools.sar.fxtran.CharSpecType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.LabelType;
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.NamedEType;
import org.oceandsl.tools.sar.fxtran.OpEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Char Spec Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getArgN <em>Arg N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getLabel <em>Label</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getLiteralE <em>Literal
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl#getStarE <em>Star E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CharSpecTypeImpl extends MinimalEObjectImpl.Container implements CharSpecType {
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
    protected CharSpecTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getCharSpecType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.CHAR_SPEC_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getCharSpecType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArgNType> getArgN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCharSpecType_ArgN());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<LabelType> getLabel() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCharSpecType_Label());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<LiteralEType> getLiteralE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCharSpecType_LiteralE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamedEType> getNamedE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCharSpecType_NamedE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OpEType> getOpE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCharSpecType_OpE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getStarE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getCharSpecType_StarE());
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
        case FxtranPackage.CHAR_SPEC_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CHAR_SPEC_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CHAR_SPEC_TYPE__ARG_N:
            return ((InternalEList<?>) this.getArgN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CHAR_SPEC_TYPE__LABEL:
            return ((InternalEList<?>) this.getLabel()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CHAR_SPEC_TYPE__LITERAL_E:
            return ((InternalEList<?>) this.getLiteralE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CHAR_SPEC_TYPE__NAMED_E:
            return ((InternalEList<?>) this.getNamedE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.CHAR_SPEC_TYPE__OP_E:
            return ((InternalEList<?>) this.getOpE()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.CHAR_SPEC_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.CHAR_SPEC_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.CHAR_SPEC_TYPE__ARG_N:
            return this.getArgN();
        case FxtranPackage.CHAR_SPEC_TYPE__LABEL:
            return this.getLabel();
        case FxtranPackage.CHAR_SPEC_TYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.CHAR_SPEC_TYPE__NAMED_E:
            return this.getNamedE();
        case FxtranPackage.CHAR_SPEC_TYPE__OP_E:
            return this.getOpE();
        case FxtranPackage.CHAR_SPEC_TYPE__STAR_E:
            return this.getStarE();
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
        case FxtranPackage.CHAR_SPEC_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__ARG_N:
            this.getArgN().clear();
            this.getArgN().addAll((Collection<? extends ArgNType>) newValue);
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__LABEL:
            this.getLabel().clear();
            this.getLabel().addAll((Collection<? extends LabelType>) newValue);
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__LITERAL_E:
            this.getLiteralE().clear();
            this.getLiteralE().addAll((Collection<? extends LiteralEType>) newValue);
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__NAMED_E:
            this.getNamedE().clear();
            this.getNamedE().addAll((Collection<? extends NamedEType>) newValue);
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__OP_E:
            this.getOpE().clear();
            this.getOpE().addAll((Collection<? extends OpEType>) newValue);
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__STAR_E:
            this.getStarE().clear();
            this.getStarE().addAll((Collection<? extends String>) newValue);
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
        case FxtranPackage.CHAR_SPEC_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__ARG_N:
            this.getArgN().clear();
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__LABEL:
            this.getLabel().clear();
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__LITERAL_E:
            this.getLiteralE().clear();
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__NAMED_E:
            this.getNamedE().clear();
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__OP_E:
            this.getOpE().clear();
            return;
        case FxtranPackage.CHAR_SPEC_TYPE__STAR_E:
            this.getStarE().clear();
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
        case FxtranPackage.CHAR_SPEC_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.CHAR_SPEC_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.CHAR_SPEC_TYPE__ARG_N:
            return !this.getArgN().isEmpty();
        case FxtranPackage.CHAR_SPEC_TYPE__LABEL:
            return !this.getLabel().isEmpty();
        case FxtranPackage.CHAR_SPEC_TYPE__LITERAL_E:
            return !this.getLiteralE().isEmpty();
        case FxtranPackage.CHAR_SPEC_TYPE__NAMED_E:
            return !this.getNamedE().isEmpty();
        case FxtranPackage.CHAR_SPEC_TYPE__OP_E:
            return !this.getOpE().isEmpty();
        case FxtranPackage.CHAR_SPEC_TYPE__STAR_E:
            return !this.getStarE().isEmpty();
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

} // CharSpecTypeImpl
