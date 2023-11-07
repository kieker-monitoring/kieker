/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Case Value Range LT
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType#getCaseValueRange <em>Case Value
 * Range</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueRangeLTType()
 * @model extendedMetaData="name='case-value-range-LT_._type' kind='mixed'"
 * @generated
 */
public interface CaseValueRangeLTType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueRangeLTType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Case Value Range</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.CaseValueRangeType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Case Value Range</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCaseValueRangeLTType_CaseValueRange()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='case-value-range'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EList<CaseValueRangeType> getCaseValueRange();

} // CaseValueRangeLTType
