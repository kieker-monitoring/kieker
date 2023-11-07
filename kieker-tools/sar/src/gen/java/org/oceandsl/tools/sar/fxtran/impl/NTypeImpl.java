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
import org.oceandsl.tools.sar.fxtran.NType;
import org.oceandsl.tools.sar.fxtran.OpType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>NType</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NTypeImpl#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NTypeImpl#getN1 <em>N1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.NTypeImpl#getOp <em>Op</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NTypeImpl extends MinimalEObjectImpl.Container implements NType {
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
    protected NTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getNType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.NTYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getNType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NType> getN() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getNType_N());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getN1() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getNType_N1());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OpType> getOp() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getNType_Op());
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
        case FxtranPackage.NTYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.NTYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.NTYPE__N:
            return ((InternalEList<?>) this.getN()).basicRemove(otherEnd, msgs);
        case FxtranPackage.NTYPE__OP:
            return ((InternalEList<?>) this.getOp()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.NTYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.NTYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.NTYPE__N:
            return this.getN();
        case FxtranPackage.NTYPE__N1:
            return this.getN1();
        case FxtranPackage.NTYPE__OP:
            return this.getOp();
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
        case FxtranPackage.NTYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.NTYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.NTYPE__N:
            this.getN().clear();
            this.getN().addAll((Collection<? extends NType>) newValue);
            return;
        case FxtranPackage.NTYPE__N1:
            this.getN1().clear();
            this.getN1().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.NTYPE__OP:
            this.getOp().clear();
            this.getOp().addAll((Collection<? extends OpType>) newValue);
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
        case FxtranPackage.NTYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.NTYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.NTYPE__N:
            this.getN().clear();
            return;
        case FxtranPackage.NTYPE__N1:
            this.getN1().clear();
            return;
        case FxtranPackage.NTYPE__OP:
            this.getOp().clear();
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
        case FxtranPackage.NTYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.NTYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.NTYPE__N:
            return !this.getN().isEmpty();
        case FxtranPackage.NTYPE__N1:
            return !this.getN1().isEmpty();
        case FxtranPackage.NTYPE__OP:
            return !this.getOp().isEmpty();
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

} // NTypeImpl
