/**
 */
package org.oceandsl.tools.sar.fxtran;

import java.math.BigInteger;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Stop Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.StopStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.StopStmtType#getStopCode <em>Stop Code</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getStopStmtType()
 * @model extendedMetaData="name='stop-stmt_._type' kind='mixed'"
 * @generated
 */
public interface StopStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getStopStmtType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Stop Code</b></em>' attribute list. The list contents are of
     * type {@link java.math.BigInteger}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Stop Code</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getStopStmtType_StopCode()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.Integer" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='stop-code'
     *        namespace='##targetNamespace'"
     * @generated
     */
    EList<BigInteger> getStopCode();

} // StopStmtType
