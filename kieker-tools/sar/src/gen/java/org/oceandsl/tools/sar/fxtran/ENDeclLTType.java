/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>EN Decl LT Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getENDecl <em>EN Decl</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclLTType()
 * @model extendedMetaData="name='EN-decl-LT_._type' kind='mixed'"
 * @generated
 */
public interface ENDeclLTType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclLTType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclLTType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Cnt</b></em>' attribute list. The list contents are of type
     * {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Cnt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclLTType_Cnt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='cnt'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getCnt();

    /**
     * Returns the value of the '<em><b>EN Decl</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ENDeclType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>EN Decl</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getENDeclLTType_ENDecl()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN-decl' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ENDeclType> getENDecl();

} // ENDeclLTType
