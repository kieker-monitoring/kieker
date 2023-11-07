/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Close Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CloseStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.CloseStmtType#getCloseSpecSpec <em>Close Spec
 * Spec</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCloseStmtType()
 * @model extendedMetaData="name='close-stmt_._type' kind='mixed'"
 * @generated
 */
public interface CloseStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCloseStmtType_Mixed()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='elementWildcard' name=':mixed'"
     * @generated
     */
    FeatureMap getMixed();

    /**
     * Returns the value of the '<em><b>Close Spec Spec</b></em>' containment reference list. The
     * list contents are of type {@link org.oceandsl.tools.sar.fxtran.CloseSpecSpecType}. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Close Spec Spec</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getCloseStmtType_CloseSpecSpec()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='close-spec-spec' namespace='##targetNamespace'"
     * @generated
     */
    EList<CloseSpecSpecType> getCloseSpecSpec();

} // CloseStmtType
