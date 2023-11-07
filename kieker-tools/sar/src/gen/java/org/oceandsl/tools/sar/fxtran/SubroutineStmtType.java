/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Subroutine Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getDummyArgLT <em>Dummy Arg
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getPrefix <em>Prefix</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getSubroutineN <em>Subroutine
 * N</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSubroutineStmtType()
 * @model extendedMetaData="name='subroutine-stmt_._type' kind='mixed'"
 * @generated
 */
public interface SubroutineStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSubroutineStmtType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSubroutineStmtType_Group()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSubroutineStmtType_Cnt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='cnt'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getCnt();

    /**
     * Returns the value of the '<em><b>Dummy Arg LT</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.DummyArgLTType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Dummy Arg LT</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSubroutineStmtType_DummyArgLT()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dummy-arg-LT' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<DummyArgLTType> getDummyArgLT();

    /**
     * Returns the value of the '<em><b>Prefix</b></em>' attribute list. The list contents are of
     * type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Prefix</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSubroutineStmtType_Prefix()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='prefix'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getPrefix();

    /**
     * Returns the value of the '<em><b>Subroutine N</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.SubroutineNType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Subroutine N</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getSubroutineStmtType_SubroutineN()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='subroutine-N' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<SubroutineNType> getSubroutineN();

} // SubroutineStmtType
