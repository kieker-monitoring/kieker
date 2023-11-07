/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>RLT Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.RLTType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.RLTType#getArrayR <em>Array R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.RLTType#getComponentR <em>Component R</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.RLTType#getParensR <em>Parens R</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getRLTType()
 * @model extendedMetaData="name='R-LT_._type' kind='elementOnly'"
 * @generated
 */
public interface RLTType extends EObject {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Group</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getRLTType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Array R</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ArrayRType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Array R</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getRLTType_ArrayR()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='array-R' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ArrayRType> getArrayR();

    /**
     * Returns the value of the '<em><b>Component R</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ComponentRType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Component R</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getRLTType_ComponentR()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='component-R' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ComponentRType> getComponentR();

    /**
     * Returns the value of the '<em><b>Parens R</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ParensRType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Parens R</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getRLTType_ParensR()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parens-R' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ParensRType> getParensR();

} // RLTType
