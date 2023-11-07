/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Component Decl Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getENDeclLT <em>EN Decl
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getTSpec <em>TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getAttribute
 * <em>Attribute</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getComponentDeclStmtType()
 * @model extendedMetaData="name='component-decl-stmt_._type' kind='mixed'"
 * @generated
 */
public interface ComponentDeclStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getComponentDeclStmtType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getComponentDeclStmtType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>EN Decl LT</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ENDeclLTType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>EN Decl LT</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getComponentDeclStmtType_ENDeclLT()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='EN-decl-LT' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ENDeclLTType> getENDeclLT();

    /**
     * Returns the value of the '<em><b>TSpec</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.TSpecType}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>TSpec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getComponentDeclStmtType_TSpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='_T-spec_' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<TSpecType> getTSpec();

    /**
     * Returns the value of the '<em><b>Attribute</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.AttributeType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Attribute</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getComponentDeclStmtType_Attribute()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='attribute' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<AttributeType> getAttribute();

} // ComponentDeclStmtType
