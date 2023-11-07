/**
 */
package org.oceandsl.tools.restructuring.restructuremodel.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.oceandsl.tools.restructuring.restructuremodel.PasteOperation;
import org.oceandsl.tools.restructuring.restructuremodel.RestructuremodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Paste Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.oceandsl.tools.restructuring.restructuremodel.impl.PasteOperationImpl#getComponentName <em>Component Name</em>}</li>
 *   <li>{@link org.oceandsl.tools.restructuring.restructuremodel.impl.PasteOperationImpl#getOperationName <em>Operation Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PasteOperationImpl extends AbstractTransformationStepImpl implements PasteOperation {
    /**
     * The default value of the '{@link #getComponentName() <em>Component Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponentName()
     * @generated
     * @ordered
     */
    protected static final String COMPONENT_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getComponentName() <em>Component Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getComponentName()
     * @generated
     * @ordered
     */
    protected String componentName = COMPONENT_NAME_EDEFAULT;

    /**
     * The default value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperationName()
     * @generated
     * @ordered
     */
    protected static final String OPERATION_NAME_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @see #getOperationName()
     * @generated
     * @ordered
     */
    protected String operationName = OPERATION_NAME_EDEFAULT;

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    protected PasteOperationImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return RestructuremodelPackage.Literals.PASTE_OPERATION;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getComponentName() {
        return componentName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setComponentName(String newComponentName) {
        String oldComponentName = componentName;
        componentName = newComponentName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.PASTE_OPERATION__COMPONENT_NAME, oldComponentName, componentName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public String getOperationName() {
        return operationName;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    public void setOperationName(String newOperationName) {
        String oldOperationName = operationName;
        operationName = newOperationName;
        if (eNotificationRequired())
            eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.PASTE_OPERATION__OPERATION_NAME, oldOperationName, operationName));
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public Object eGet(int featureID, boolean resolve, boolean coreType) {
        switch (featureID) {
            case RestructuremodelPackage.PASTE_OPERATION__COMPONENT_NAME:
                return getComponentName();
            case RestructuremodelPackage.PASTE_OPERATION__OPERATION_NAME:
                return getOperationName();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @generated
     */
    @Override
    public void eSet(int featureID, Object newValue) {
        switch (featureID) {
            case RestructuremodelPackage.PASTE_OPERATION__COMPONENT_NAME:
                setComponentName((String)newValue);
                return;
            case RestructuremodelPackage.PASTE_OPERATION__OPERATION_NAME:
                setOperationName((String)newValue);
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
            case RestructuremodelPackage.PASTE_OPERATION__COMPONENT_NAME:
                setComponentName(COMPONENT_NAME_EDEFAULT);
                return;
            case RestructuremodelPackage.PASTE_OPERATION__OPERATION_NAME:
                setOperationName(OPERATION_NAME_EDEFAULT);
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
            case RestructuremodelPackage.PASTE_OPERATION__COMPONENT_NAME:
                return COMPONENT_NAME_EDEFAULT == null ? componentName != null : !COMPONENT_NAME_EDEFAULT.equals(componentName);
            case RestructuremodelPackage.PASTE_OPERATION__OPERATION_NAME:
                return OPERATION_NAME_EDEFAULT == null ? operationName != null : !OPERATION_NAME_EDEFAULT.equals(operationName);
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
        result.append(" (componentName: ");
        result.append(componentName);
        result.append(", operationName: ");
        result.append(operationName);
        result.append(')');
        return result.toString();
    }

} //PasteOperationImpl
