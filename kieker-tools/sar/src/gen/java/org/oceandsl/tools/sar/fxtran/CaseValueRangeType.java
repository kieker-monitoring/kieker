/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Case Value Range
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeType#getCaseValue <em>Case
 * Value</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueRangeType()
 * @model extendedMetaData="name='case-value-range_._type' kind='elementOnly'"
 * @generated
 */
public interface CaseValueRangeType extends EObject {
    /**
     * Returns the value of the '<em><b>Case Value</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Value</em>' containment reference.
     * @see #setCaseValue(CaseValueType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueRangeType_CaseValue()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='case-value'
     *        namespace='##targetNamespace'"
     * @generated
     */
    CaseValueType getCaseValue();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeType#getCaseValue
     * <em>Case Value</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Case Value</em>' containment reference.
     * @see #getCaseValue()
     * @generated
     */
    void setCaseValue(CaseValueType value);

} // CaseValueRangeType
