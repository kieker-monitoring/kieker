/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Attribute Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AttributeType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AttributeType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AttributeType#getArraySpec <em>Array Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AttributeType#getAttributeN <em>Attribute N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.AttributeType#getIntentSpec <em>Intent Spec</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAttributeType()
 * @model extendedMetaData="name='attribute_._type' kind='mixed'"
 * @generated
 */
public interface AttributeType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAttributeType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAttributeType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Array Spec</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ArraySpecType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Array Spec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAttributeType_ArraySpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='array-spec' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ArraySpecType> getArraySpec();

    /**
     * Returns the value of the '<em><b>Attribute N</b></em>' attribute list. The list contents are
     * of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Attribute N</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAttributeType_AttributeN()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='attribute-N'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getAttributeN();

    /**
     * Returns the value of the '<em><b>Intent Spec</b></em>' attribute list. The list contents are
     * of type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Intent Spec</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getAttributeType_IntentSpec()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='intent-spec'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getIntentSpec();

} // AttributeType
