/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>NType</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NType#getN <em>N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NType#getN1 <em>N1</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NType#getOp <em>Op</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNType()
 * @model extendedMetaData="name='N_._type' kind='mixed'"
 * @generated
 */
public interface NType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNType_Group()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNType_N()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='N' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<NType> getN();

    /**
     * Returns the value of the '<em><b>N1</b></em>' attribute list. The list contents are of type
     * {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>N1</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNType_N1()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.NCName" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='n'
     *        namespace='##targetNamespace' group='#group:1'"
     * @generated
     */
    EList<String> getN1();

    /**
     * Returns the value of the '<em><b>Op</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.OpType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Op</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNType_Op()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='op' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<OpType> getOp();

} // NType
