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

import org.oceandsl.tools.sar.fxtran.ArrayRType;
import org.oceandsl.tools.sar.fxtran.ComponentRType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.ParensRType;
import org.oceandsl.tools.sar.fxtran.RLTType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>RLT Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.RLTTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.RLTTypeImpl#getArrayR <em>Array R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.RLTTypeImpl#getComponentR <em>Component
 * R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.RLTTypeImpl#getParensR <em>Parens R</em>}</li>
 * </ul>
 *
 * @generated
 */
public class RLTTypeImpl extends MinimalEObjectImpl.Container implements RLTType {
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
    protected RLTTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getRLTType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getGroup() {
        if (this.group == null) {
            this.group = new BasicFeatureMap(this, FxtranPackage.RLT_TYPE__GROUP);
        }
        return this.group;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ArrayRType> getArrayR() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getRLTType_ArrayR());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ComponentRType> getComponentR() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getRLTType_ComponentR());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ParensRType> getParensR() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getRLTType_ParensR());
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
        case FxtranPackage.RLT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.RLT_TYPE__ARRAY_R:
            return ((InternalEList<?>) this.getArrayR()).basicRemove(otherEnd, msgs);
        case FxtranPackage.RLT_TYPE__COMPONENT_R:
            return ((InternalEList<?>) this.getComponentR()).basicRemove(otherEnd, msgs);
        case FxtranPackage.RLT_TYPE__PARENS_R:
            return ((InternalEList<?>) this.getParensR()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.RLT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.RLT_TYPE__ARRAY_R:
            return this.getArrayR();
        case FxtranPackage.RLT_TYPE__COMPONENT_R:
            return this.getComponentR();
        case FxtranPackage.RLT_TYPE__PARENS_R:
            return this.getParensR();
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
        case FxtranPackage.RLT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.RLT_TYPE__ARRAY_R:
            this.getArrayR().clear();
            this.getArrayR().addAll((Collection<? extends ArrayRType>) newValue);
            return;
        case FxtranPackage.RLT_TYPE__COMPONENT_R:
            this.getComponentR().clear();
            this.getComponentR().addAll((Collection<? extends ComponentRType>) newValue);
            return;
        case FxtranPackage.RLT_TYPE__PARENS_R:
            this.getParensR().clear();
            this.getParensR().addAll((Collection<? extends ParensRType>) newValue);
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
        case FxtranPackage.RLT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.RLT_TYPE__ARRAY_R:
            this.getArrayR().clear();
            return;
        case FxtranPackage.RLT_TYPE__COMPONENT_R:
            this.getComponentR().clear();
            return;
        case FxtranPackage.RLT_TYPE__PARENS_R:
            this.getParensR().clear();
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
        case FxtranPackage.RLT_TYPE__GROUP:
            return this.group != null && !this.group.isEmpty();
        case FxtranPackage.RLT_TYPE__ARRAY_R:
            return !this.getArrayR().isEmpty();
        case FxtranPackage.RLT_TYPE__COMPONENT_R:
            return !this.getComponentR().isEmpty();
        case FxtranPackage.RLT_TYPE__PARENS_R:
            return !this.getParensR().isEmpty();
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

} // RLTTypeImpl
