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
import org.oceandsl.tools.sar.fxtran.ArgType;
import org.oceandsl.tools.sar.fxtran.ArrayConstructorEType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.NamedEType;
import org.oceandsl.tools.sar.fxtran.OpEType;
import org.oceandsl.tools.sar.fxtran.ParensEType;
import org.oceandsl.tools.sar.fxtran.StringEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Arg Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getArgN <em>Arg N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getArrayConstructorE <em>Array
 * Constructor E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getParensE <em>Parens E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ArgTypeImpl extends MinimalEObjectImpl.Container implements ArgType {
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
    protected ArgTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getArgType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.ARG_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getArgType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArgNType> getArgN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArgType_ArgN());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArrayConstructorEType> getArrayConstructorE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArgType_ArrayConstructorE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<LiteralEType> getLiteralE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArgType_LiteralE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamedEType> getNamedE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArgType_NamedE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OpEType> getOpE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArgType_OpE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ParensEType> getParensE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArgType_ParensE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<StringEType> getStringE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getArgType_StringE());
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
        case FxtranPackage.ARG_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__ARG_N:
            return ((InternalEList<?>) this.getArgN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__ARRAY_CONSTRUCTOR_E:
            return ((InternalEList<?>) this.getArrayConstructorE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__LITERAL_E:
            return ((InternalEList<?>) this.getLiteralE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__NAMED_E:
            return ((InternalEList<?>) this.getNamedE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__OP_E:
            return ((InternalEList<?>) this.getOpE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__PARENS_E:
            return ((InternalEList<?>) this.getParensE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ARG_TYPE__STRING_E:
            return ((InternalEList<?>) this.getStringE()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.ARG_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.ARG_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.ARG_TYPE__ARG_N:
            return this.getArgN();
        case FxtranPackage.ARG_TYPE__ARRAY_CONSTRUCTOR_E:
            return this.getArrayConstructorE();
        case FxtranPackage.ARG_TYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.ARG_TYPE__NAMED_E:
            return this.getNamedE();
        case FxtranPackage.ARG_TYPE__OP_E:
            return this.getOpE();
        case FxtranPackage.ARG_TYPE__PARENS_E:
            return this.getParensE();
        case FxtranPackage.ARG_TYPE__STRING_E:
            return this.getStringE();
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
        case FxtranPackage.ARG_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.ARG_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.ARG_TYPE__ARG_N:
            this.getArgN().clear();
            this.getArgN().addAll((Collection<? extends ArgNType>) newValue);
            return;
        case FxtranPackage.ARG_TYPE__ARRAY_CONSTRUCTOR_E:
            this.getArrayConstructorE().clear();
            this.getArrayConstructorE().addAll((Collection<? extends ArrayConstructorEType>) newValue);
            return;
        case FxtranPackage.ARG_TYPE__LITERAL_E:
            this.getLiteralE().clear();
            this.getLiteralE().addAll((Collection<? extends LiteralEType>) newValue);
            return;
        case FxtranPackage.ARG_TYPE__NAMED_E:
            this.getNamedE().clear();
            this.getNamedE().addAll((Collection<? extends NamedEType>) newValue);
            return;
        case FxtranPackage.ARG_TYPE__OP_E:
            this.getOpE().clear();
            this.getOpE().addAll((Collection<? extends OpEType>) newValue);
            return;
        case FxtranPackage.ARG_TYPE__PARENS_E:
            this.getParensE().clear();
            this.getParensE().addAll((Collection<? extends ParensEType>) newValue);
            return;
        case FxtranPackage.ARG_TYPE__STRING_E:
            this.getStringE().clear();
            this.getStringE().addAll((Collection<? extends StringEType>) newValue);
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
        case FxtranPackage.ARG_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.ARG_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.ARG_TYPE__ARG_N:
            this.getArgN().clear();
            return;
        case FxtranPackage.ARG_TYPE__ARRAY_CONSTRUCTOR_E:
            this.getArrayConstructorE().clear();
            return;
        case FxtranPackage.ARG_TYPE__LITERAL_E:
            this.getLiteralE().clear();
            return;
        case FxtranPackage.ARG_TYPE__NAMED_E:
            this.getNamedE().clear();
            return;
        case FxtranPackage.ARG_TYPE__OP_E:
            this.getOpE().clear();
            return;
        case FxtranPackage.ARG_TYPE__PARENS_E:
            this.getParensE().clear();
            return;
        case FxtranPackage.ARG_TYPE__STRING_E:
            this.getStringE().clear();
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
        case FxtranPackage.ARG_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.ARG_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.ARG_TYPE__ARG_N:
            return !this.getArgN().isEmpty();
        case FxtranPackage.ARG_TYPE__ARRAY_CONSTRUCTOR_E:
            return !this.getArrayConstructorE().isEmpty();
        case FxtranPackage.ARG_TYPE__LITERAL_E:
            return !this.getLiteralE().isEmpty();
        case FxtranPackage.ARG_TYPE__NAMED_E:
            return !this.getNamedE().isEmpty();
        case FxtranPackage.ARG_TYPE__OP_E:
            return !this.getOpE().isEmpty();
        case FxtranPackage.ARG_TYPE__PARENS_E:
            return !this.getParensE().isEmpty();
        case FxtranPackage.ARG_TYPE__STRING_E:
            return !this.getStringE().isEmpty();
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

} // ArgTypeImpl
