/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Condition EType</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ConditionEType#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ConditionEType#getOpE <em>Op E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getConditionEType()
 * @model extendedMetaData="name='condition-E_._type' kind='elementOnly'"
 * @generated
 */
public interface ConditionEType extends EObject {
    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference.
     * @see #setNamedE(NamedEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getConditionEType_NamedE()
     * @model containment="true" extendedMetaData="kind='element' name='named-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    NamedEType getNamedE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ConditionEType#getNamedE
     * <em>Named E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Named E</em>' containment reference.
     * @see #getNamedE()
     * @generated
     */
    void setNamedE(NamedEType value);

    /**
     * Returns the value of the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Op E</em>' containment reference.
     * @see #setOpE(OpEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getConditionEType_OpE()
     * @model containment="true" extendedMetaData="kind='element' name='op-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    OpEType getOpE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ConditionEType#getOpE <em>Op
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Op E</em>' containment reference.
     * @see #getOpE()
     * @generated
     */
    void setOpE(OpEType value);

} // ConditionEType
