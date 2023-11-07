/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Read Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ReadStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ReadStmtType#getIoControlSpec <em>Io Control
 * Spec</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getReadStmtType()
 * @model extendedMetaData="name='read-stmt_._type' kind='mixed'"
 * @generated
 */
public interface ReadStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getReadStmtType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Io Control Spec</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.IoControlSpecType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Io Control Spec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getReadStmtType_IoControlSpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='io-control-spec' namespace='##targetNamespace'"
     * @generated
     */
    EList<IoControlSpecType> getIoControlSpec();

} // ReadStmtType
