/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Section Subscript
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getLowerBound <em>Lower
 * Bound</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getUpperBound <em>Upper
 * Bound</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSectionSubscriptType()
 * @model extendedMetaData="name='section-subscript_._type' kind='mixed'"
 * @generated
 */
public interface SectionSubscriptType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSectionSubscriptType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Group</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSectionSubscriptType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Lower Bound</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.LowerBoundType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Lower Bound</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSectionSubscriptType_LowerBound()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='lower-bound' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<LowerBoundType> getLowerBound();

    /**
     * Returns the value of the '<em><b>Upper Bound</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.UpperBoundType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Upper Bound</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSectionSubscriptType_UpperBound()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='upper-bound' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<UpperBoundType> getUpperBound();

} // SectionSubscriptType
