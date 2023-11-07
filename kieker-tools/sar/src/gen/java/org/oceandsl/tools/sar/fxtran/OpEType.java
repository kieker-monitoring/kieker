/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Op EType</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getCnt <em>Cnt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getOp <em>Op</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getParensE <em>Parens E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.OpEType#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType()
 * @model extendedMetaData="name='op-E_._type' kind='elementOnly'"
 * @generated
 */
public interface OpEType extends EObject {
    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Group</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:0'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Cnt</b></em>' attribute list. The list contents are of type
     * {@link java.lang.String}. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Cnt</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_Cnt()
     * @model unique="false" dataType="org.eclipse.emf.ecore.xml.type.String" transient="true"
     *        volatile="true" derived="true" extendedMetaData="kind='element' name='cnt'
     *        namespace='##targetNamespace' group='#group:0'"
     * @generated
     */
    EList<String> getCnt();

    /**
     * Returns the value of the '<em><b>Literal E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.LiteralEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Literal E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_LiteralE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='literal-E' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<LiteralEType> getLiteralE();

    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.NamedEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_NamedE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='named-E' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<NamedEType> getNamedE();

    /**
     * Returns the value of the '<em><b>Op</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.OpType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Op</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_Op()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='op' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<OpType> getOp();

    /**
     * Returns the value of the '<em><b>Op E</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.OpEType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Op E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_OpE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='op-E' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<OpEType> getOpE();

    /**
     * Returns the value of the '<em><b>Parens E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ParensEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Parens E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_ParensE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='parens-E' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<ParensEType> getParensE();

    /**
     * Returns the value of the '<em><b>String E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.StringEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>String E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getOpEType_StringE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='string-E' namespace='##targetNamespace'
     *        group='#group:0'"
     * @generated
     */
    EList<StringEType> getStringE();

} // OpEType
