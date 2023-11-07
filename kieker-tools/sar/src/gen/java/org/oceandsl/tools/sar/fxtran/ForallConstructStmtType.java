/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Forall Construct Stmt
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getForallTripletSpecLT
 * <em>Forall Triplet Spec LT</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getForallConstructStmtType()
 * @model extendedMetaData="name='forall-construct-stmt_._type' kind='mixed'"
 * @generated
 */
public interface ForallConstructStmtType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getForallConstructStmtType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getForallConstructStmtType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>N</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.NType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>N</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getForallConstructStmtType_N()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='N' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<NType> getN();

    /**
     * Returns the value of the '<em><b>Forall Triplet Spec LT</b></em>' containment reference list.
     * The list contents are of type {@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType}.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Forall Triplet Spec LT</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getForallConstructStmtType_ForallTripletSpecLT()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='forall-triplet-spec-LT'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<ForallTripletSpecLTType> getForallTripletSpecLT();

} // ForallConstructStmtType
