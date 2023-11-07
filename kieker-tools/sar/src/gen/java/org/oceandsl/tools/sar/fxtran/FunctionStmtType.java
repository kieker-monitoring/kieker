/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Function Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getDerivedTSpec <em>Derived
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getDummyArgLT <em>Dummy Arg
 * LT</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getFunctionN <em>Function N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getIntrinsicTSpec <em>Intrinsic
 * TSpec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getPrefix <em>Prefix</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getResultSpec <em>Result
 * Spec</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType()
 * @model extendedMetaData="name='function-stmt_._type' kind='mixed'"
 * @generated
 */
public interface FunctionStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Derived TSpec</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.DerivedTSpecType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Derived TSpec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_DerivedTSpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='derived-T-spec' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<DerivedTSpecType> getDerivedTSpec();

    /**
     * Returns the value of the '<em><b>Dummy Arg LT</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.DummyArgLTType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Dummy Arg LT</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_DummyArgLT()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='dummy-arg-LT' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<DummyArgLTType> getDummyArgLT();

    /**
     * Returns the value of the '<em><b>Function N</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.FunctionNType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Function N</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_FunctionN()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='function-N' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<FunctionNType> getFunctionN();

    /**
     * Returns the value of the '<em><b>Intrinsic TSpec</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Intrinsic TSpec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_IntrinsicTSpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='intrinsic-T-spec' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<IntrinsicTSpecType> getIntrinsicTSpec();

    /**
     * Returns the value of the '<em><b>Prefix</b></em>' attribute list. The list contents are of
     * type {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Prefix</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_Prefix()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='prefix'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getPrefix();

    /**
     * Returns the value of the '<em><b>Result Spec</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ResultSpecType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Result Spec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getFunctionStmtType_ResultSpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='result-spec' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ResultSpecType> getResultSpec();

} // FunctionStmtType
