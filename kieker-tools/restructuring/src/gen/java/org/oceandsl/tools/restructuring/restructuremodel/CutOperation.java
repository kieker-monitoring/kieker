/**
 */
package org.oceandsl.tools.restructuring.restructuremodel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Cut Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.oceandsl.tools.restructuring.restructuremodel.CutOperation#getComponentName <em>Component Name</em>}</li>
 *   <li>{@link org.oceandsl.tools.restructuring.restructuremodel.CutOperation#getOperationName <em>Operation Name</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.restructuring.restructuremodel.RestructuremodelPackage#getCutOperation()
 * @model
 * @generated
 */
public interface CutOperation extends AbstractTransformationStep {
    /**
     * Returns the value of the '<em><b>Component Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Component Name</em>' attribute.
     * @see #setComponentName(String)
     * @see org.oceandsl.tools.restructuring.restructuremodel.RestructuremodelPackage#getCutOperation_ComponentName()
     * @model
     * @generated
     */
    String getComponentName();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.restructuring.restructuremodel.CutOperation#getComponentName <em>Component Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Component Name</em>' attribute.
     * @see #getComponentName()
     * @generated
     */
    void setComponentName(String value);

    /**
     * Returns the value of the '<em><b>Operation Name</b></em>' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @return the value of the '<em>Operation Name</em>' attribute.
     * @see #setOperationName(String)
     * @see org.oceandsl.tools.restructuring.restructuremodel.RestructuremodelPackage#getCutOperation_OperationName()
     * @model
     * @generated
     */
    String getOperationName();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.restructuring.restructuremodel.CutOperation#getOperationName <em>Operation Name</em>}' attribute.
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     * @param value the new value of the '<em>Operation Name</em>' attribute.
     * @see #getOperationName()
     * @generated
     */
    void setOperationName(String value);

} // CutOperation
