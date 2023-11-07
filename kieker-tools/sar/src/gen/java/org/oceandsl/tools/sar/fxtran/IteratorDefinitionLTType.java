/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Iterator Definition LT
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType#getIteratorElement <em>Iterator
 * Element</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIteratorDefinitionLTType()
 * @model extendedMetaData="name='iterator-definition-LT_._type' kind='mixed'"
 * @generated
 */
public interface IteratorDefinitionLTType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIteratorDefinitionLTType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Iterator Element</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.IteratorElementType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Iterator Element</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIteratorDefinitionLTType_IteratorElement()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='iterator-element'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EList<IteratorElementType> getIteratorElement();

} // IteratorDefinitionLTType
