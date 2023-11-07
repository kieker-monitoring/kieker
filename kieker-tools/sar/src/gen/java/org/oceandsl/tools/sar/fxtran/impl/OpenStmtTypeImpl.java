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

import org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.OpenStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Open Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpenStmtTypeImpl#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpenStmtTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpenStmtTypeImpl#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.OpenStmtTypeImpl#getConnectSpecSpec <em>Connect
 * Spec Spec</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OpenStmtTypeImpl extends MinimalEObjectImpl.Container implements OpenStmtType {
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
    protected OpenStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getOpenStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getMixed() {
        if (this.mixed == null) {
            this.mixed = new BasicFeatureMap(this, FxtranPackage.OPEN_STMT_TYPE__MIXED);
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
        return (FeatureMap) this.getMixed().<FeatureMap.Entry> list(FxtranPackage.eINSTANCE.getOpenStmtType_Group());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<String> getCnt() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpenStmtType_Cnt());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<ConnectSpecSpecType> getConnectSpecSpec() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getOpenStmtType_ConnectSpecSpec());
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
        case FxtranPackage.OPEN_STMT_TYPE__MIXED:
            return ((InternalEList<?>) this.getMixed()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OPEN_STMT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.OPEN_STMT_TYPE__CONNECT_SPEC_SPEC:
            return ((InternalEList<?>) this.getConnectSpecSpec()).basicRemove(otherEnd, msgs);
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
        case FxtranPackage.OPEN_STMT_TYPE__MIXED:
            if (coreType) {
                return this.getMixed();
            }
            return ((FeatureMap.Internal) this.getMixed()).getWrapper();
        case FxtranPackage.OPEN_STMT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.OPEN_STMT_TYPE__CNT:
            return this.getCnt();
        case FxtranPackage.OPEN_STMT_TYPE__CONNECT_SPEC_SPEC:
            return this.getConnectSpecSpec();
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
        case FxtranPackage.OPEN_STMT_TYPE__MIXED:
            ((FeatureMap.Internal) this.getMixed()).set(newValue);
            return;
        case FxtranPackage.OPEN_STMT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.OPEN_STMT_TYPE__CNT:
            this.getCnt().clear();
            this.getCnt().addAll((Collection<? extends String>) newValue);
            return;
        case FxtranPackage.OPEN_STMT_TYPE__CONNECT_SPEC_SPEC:
            this.getConnectSpecSpec().clear();
            this.getConnectSpecSpec().addAll((Collection<? extends ConnectSpecSpecType>) newValue);
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
        case FxtranPackage.OPEN_STMT_TYPE__MIXED:
            this.getMixed().clear();
            return;
        case FxtranPackage.OPEN_STMT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.OPEN_STMT_TYPE__CNT:
            this.getCnt().clear();
            return;
        case FxtranPackage.OPEN_STMT_TYPE__CONNECT_SPEC_SPEC:
            this.getConnectSpecSpec().clear();
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
        case FxtranPackage.OPEN_STMT_TYPE__MIXED:
            return this.mixed != null && !this.mixed.isEmpty();
        case FxtranPackage.OPEN_STMT_TYPE__GROUP:
            return !this.getGroup().isEmpty();
        case FxtranPackage.OPEN_STMT_TYPE__CNT:
            return !this.getCnt().isEmpty();
        case FxtranPackage.OPEN_STMT_TYPE__CONNECT_SPEC_SPEC:
            return !this.getConnectSpecSpec().isEmpty();
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

} // OpenStmtTypeImpl
