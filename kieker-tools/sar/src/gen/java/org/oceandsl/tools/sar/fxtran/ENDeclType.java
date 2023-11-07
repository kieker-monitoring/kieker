/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>EN Decl Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getArraySpec <em>Array Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getENN <em>ENN</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getInitE <em>Init E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclType()
 * @model extendedMetaData="name='EN-decl_._type' kind='mixed'"
 * @generated
 */
public interface ENDeclType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclType_Group()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclType_ArraySpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='array-spec' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ArraySpecType> getArraySpec();

    /**
     * Returns the value of the '<em><b>ENN</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.ENNType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>ENN</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclType_ENN()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN-N' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ENNType> getENN();

    /**
     * Returns the value of the '<em><b>Init E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.InitEType}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Init E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclType_InitE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='init-E' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<InitEType> getInitE();

} // ENDeclType
