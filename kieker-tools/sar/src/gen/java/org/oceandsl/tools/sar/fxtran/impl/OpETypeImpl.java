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
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.NamedEType;
import org.oceandsl.tools.sar.fxtran.OpEType;
import org.oceandsl.tools.sar.fxtran.OpType;
import org.oceandsl.tools.sar.fxtran.ParensEType;
import org.oceandsl.tools.sar.fxtran.StringEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Op EType</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getOp <em>Op</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getParensE <em>Parens E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OpETypeImpl extends MinimalEObjectImpl.Container implements OpEType {
    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap group;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected OpETypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getOpEType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getGroup() {
        if (this.group == null) {
            this.group = new BasicFeatureMap(this, FxtranPackage.OP_ETYPE__GROUP);
        }
        return this.group;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpEType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<LiteralEType> getLiteralE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpEType_LiteralE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamedEType> getNamedE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpEType_NamedE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OpType> getOp() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpEType_Op());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OpEType> getOpE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpEType_OpE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ParensEType> getParensE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpEType_ParensE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<StringEType> getStringE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpEType_StringE());
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
        case FxtranPackage.OP_ETYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OP_ETYPE__LITERAL_E:
            return ((InternalEList<?>) this.getLiteralE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OP_ETYPE__NAMED_E:
            return ((InternalEList<?>) this.getNamedE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OP_ETYPE__OP:
            return ((InternalEList<?>) this.getOp()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OP_ETYPE__OP_E:
            return ((InternalEList<?>) this.getOpE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OP_ETYPE__PARENS_E:
            return ((InternalEList<?>) this.getParensE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OP_ETYPE__STRING_E:
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
        case FxtranPackage.OP_ETYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.OP_ETYPE__CNT:
            return this.getCnt();
        case FxtranPackage.OP_ETYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.OP_ETYPE__NAMED_E:
            return this.getNamedE();
        case FxtranPackage.OP_ETYPE__OP:
            return this.getOp();
        case FxtranPackage.OP_ETYPE__OP_E:
            return this.getOpE();
        case FxtranPackage.OP_ETYPE__PARENS_E:
            return this.getParensE();
        case FxtranPackage.OP_ETYPE__STRING_E:
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
        case FxtranPackage.OP_ETYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.OP_ETYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.OP_ETYPE__LITERAL_E:
            this.getLiteralE().clear();
            this.getLiteralE().addAll((Collection<? extends LiteralEType>) newValue);
            return;
        case FxtranPackage.OP_ETYPE__NAMED_E:
            this.getNamedE().clear();
            this.getNamedE().addAll((Collection<? extends NamedEType>) newValue);
            return;
        case FxtranPackage.OP_ETYPE__OP:
            this.getOp().clear();
            this.getOp().addAll((Collection<? extends OpType>) newValue);
            return;
        case FxtranPackage.OP_ETYPE__OP_E:
            this.getOpE().clear();
            this.getOpE().addAll((Collection<? extends OpEType>) newValue);
            return;
        case FxtranPackage.OP_ETYPE__PARENS_E:
            this.getParensE().clear();
            this.getParensE().addAll((Collection<? extends ParensEType>) newValue);
            return;
        case FxtranPackage.OP_ETYPE__STRING_E:
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
        case FxtranPackage.OP_ETYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.OP_ETYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.OP_ETYPE__LITERAL_E:
            this.getLiteralE().clear();
            return;
        case FxtranPackage.OP_ETYPE__NAMED_E:
            this.getNamedE().clear();
            return;
        case FxtranPackage.OP_ETYPE__OP:
            this.getOp().clear();
            return;
        case FxtranPackage.OP_ETYPE__OP_E:
            this.getOpE().clear();
            return;
        case FxtranPackage.OP_ETYPE__PARENS_E:
            this.getParensE().clear();
            return;
        case FxtranPackage.OP_ETYPE__STRING_E:
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
        case FxtranPackage.OP_ETYPE__GROUP:
            return this.group != null && !this.group.isEmpty();
        case FxtranPackage.OP_ETYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.OP_ETYPE__LITERAL_E:
            return !this.getLiteralE().isEmpty();
        case FxtranPackage.OP_ETYPE__NAMED_E:
            return !this.getNamedE().isEmpty();
        case FxtranPackage.OP_ETYPE__OP:
            return !this.getOp().isEmpty();
        case FxtranPackage.OP_ETYPE__OP_E:
            return !this.getOpE().isEmpty();
        case FxtranPackage.OP_ETYPE__PARENS_E:
            return !this.getParensE().isEmpty();
        case FxtranPackage.OP_ETYPE__STRING_E:
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
        result.append(" (group: ");
        result.append(this.group);
        result.append(')');
        return result.toString();
    }

} // OpETypeImpl
