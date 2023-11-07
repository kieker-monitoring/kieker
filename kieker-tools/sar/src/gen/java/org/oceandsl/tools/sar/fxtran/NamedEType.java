/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Named EType</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NamedEType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NamedEType#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NamedEType#getRLT <em>RLT</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamedEType()
 * @model extendedMetaData="name='named-E_._type' kind='elementOnly'"
 * @generated
 */
public interface NamedEType extends EObject {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Group</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamedEType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>N</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.NType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>N</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamedEType_N()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='N' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<NType> getN();

    /**
     * Returns the value of the '<em><b>RLT</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.RLTType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>RLT</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamedEType_RLT()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='R-LT' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<RLTType> getRLT();

} // NamedEType
