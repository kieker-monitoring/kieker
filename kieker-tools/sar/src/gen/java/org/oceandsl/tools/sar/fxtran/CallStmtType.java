/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Call Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getArgSpec <em>Arg Spec</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getProcedureDesignator <em>Procedure
 * Designator</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCallStmtType()
 * @model extendedMetaData="name='call-stmt_._type' kind='mixed'"
 * @generated
 */
public interface CallStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCallStmtType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCallStmtType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Arg Spec</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ArgSpecType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Arg Spec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCallStmtType_ArgSpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='arg-spec' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ArgSpecType> getArgSpec();

    /**
     * Returns the value of the '<em><b>Cnt</b></em>' attribute list. The list contents are of type
     * {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Cnt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCallStmtType_Cnt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='cnt'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getCnt();

    /**
     * Returns the value of the '<em><b>Procedure Designator</b></em>' containment reference list.
     * The list contents are of type {@link org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Procedure Designator</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCallStmtType_ProcedureDesignator()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='procedure-designator'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<ProcedureDesignatorType> getProcedureDesignator();

} // CallStmtType
