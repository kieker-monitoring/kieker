/**
 */
package org.oceandsl.tools.restructuring.restructuremodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.oceandsl.tools.restructuring.restructuremodel.AbstractTransformationStep;
import org.oceandsl.tools.restructuring.restructuremodel.RestructuremodelPackage;
import org.oceandsl.tools.restructuring.restructuremodel.TransformationModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transformation Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.oceandsl.tools.restructuring.restructuremodel.impl.TransformationModelImpl#getTransformations <em>Transformations</em>}</li>
 *   <li>{@link org.oceandsl.tools.restructuring.restructuremodel.impl.TransformationModelImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransformationModelImpl extends MinimalEObjectImpl.Container implements TransformationModel {
    /**
     * The cached value of the '{@link #getTransformations() <em>Transformations</em>}' containment reference list.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getTransformations()
     * @generated
     * @ordered
     */
    protected EList<AbstractTransformationStep> transformations;

    /**
     * The default value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected static final String NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getName()
     * @generated
     * @ordered
     */
    protected String name = NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected TransformationModelImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RestructuremodelPackage.Literals.TRANSFORMATION_MODEL;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public EList<AbstractTransformationStep> getTransformations() {
        if (transformations == null) {
            transformations = new EObjectContainmentEList<AbstractTransformationStep>(AbstractTransformationStep.class, this, RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS);
        }
        return transformations;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getName() {
        return name;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setName(String newName) {
        String oldName = name;
        name = newName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.TRANSFORMATION_MODEL__NAME, oldName, name));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
        switch (featureID) {
            case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
                return ((InternalEList<?>)getTransformations()).basicRemove(otherEnd, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
                return getTransformations();
            case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
                return getName();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
                getTransformations().clear();
                getTransformations().addAll((Collection<? extends AbstractTransformationStep>)newValue);
                return;
            case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
                setName((String)newValue);
                return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eUnset(int featureID) {
        switch (featureID) {
            case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
                getTransformations().clear();
                return;
            case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
                setName(NAME_EDEFAULT);
                return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public boolean eIsSet(int featureID) {
        switch (featureID) {
            case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
                return transformations != null && !transformations.isEmpty();
            case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
                return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public String toString() {
        if (eIsProxy()) return super.toString();

        StringBuilder result = new StringBuilder(super.toString());
        result.append(" (name: ");
        result.append(name);
        result.append(')');
        return result.toString();
    }

} //TransformationModelImpl
