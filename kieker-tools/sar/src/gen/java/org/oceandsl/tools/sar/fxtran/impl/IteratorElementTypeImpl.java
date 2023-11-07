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
import org.oceandsl.tools.sar.fxtran.IteratorElementType;
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.NamedEType;
import org.oceandsl.tools.sar.fxtran.VNType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Iterator Element
 * Type</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IteratorElementTypeImpl#getMixed
 * <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IteratorElementTypeImpl#getGroup
 * <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IteratorElementTypeImpl#getVN <em>VN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IteratorElementTypeImpl#getLiteralE <em>Literal
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.IteratorElementTypeImpl#getNamedE <em>Named
 * E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IteratorElementTypeImpl extends MinimalEObjectImpl.Container implements IteratorElementType {
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
    protected IteratorElementTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getIteratorElementType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.ITERATOR_ELEMENT_TYPE__MIXED);
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
                .getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getIteratorElementType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<VNType> getVN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getIteratorElementType_VN());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<LiteralEType> getLiteralE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getIteratorElementType_LiteralE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamedEType> getNamedE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getIteratorElementType_NamedE());
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
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__VN:
            return ((InternalEList<?>) this.getVN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__LITERAL_E:
            return ((InternalEList<?>) this.getLiteralE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__NAMED_E:
            return ((InternalEList<?>) this.getNamedE()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__VN:
            return this.getVN();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__NAMED_E:
            return this.getNamedE();
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
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__VN:
            this.getVN().clear();
            this.getVN().addAll((Collection<? extends VNType>) newValue);
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__LITERAL_E:
            this.getLiteralE().clear();
            this.getLiteralE().addAll((Collection<? extends LiteralEType>) newValue);
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__NAMED_E:
            this.getNamedE().clear();
            this.getNamedE().addAll((Collection<? extends NamedEType>) newValue);
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
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__VN:
            this.getVN().clear();
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__LITERAL_E:
            this.getLiteralE().clear();
            return;
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__NAMED_E:
            this.getNamedE().clear();
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
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__VN:
            return !this.getVN().isEmpty();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__LITERAL_E:
            return !this.getLiteralE().isEmpty();
        case FxtranPackage.ITERATOR_ELEMENT_TYPE__NAMED_E:
            return !this.getNamedE().isEmpty();
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

} // IteratorElementTypeImpl
