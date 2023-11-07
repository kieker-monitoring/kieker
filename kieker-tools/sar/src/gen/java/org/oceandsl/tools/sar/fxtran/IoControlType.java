/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Io Control Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IoControlType#getMixed <em>Mixed</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IoControlType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IoControlType#getArgN <em>Arg N</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IoControlType#getLabel <em>Label</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IoControlType#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IoControlType#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.IoControlType#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType()
 * @model extendedMetaData="name='io-control_._type' kind='mixed'"
 * @generated
 */
public interface IoControlType extends EObject {
    /**
     * Returns the value of the '<em><b>Mixed</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Mixed</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType_Mixed()
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
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        transient="true" volatile="true" derived="true" extendedMetaData="kind='group'
     *        name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Arg N</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.ArgNType}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Arg N</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType_ArgN()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='arg-N' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<ArgNType> getArgN();

    /**
     * Returns the value of the '<em><b>Label</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.LabelType}. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Label</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType_Label()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='label' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<LabelType> getLabel();

    /**
     * Returns the value of the '<em><b>Literal E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.LiteralEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Literal E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType_LiteralE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='literal-E' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<LiteralEType> getLiteralE();

    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.NamedEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType_NamedE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='named-E' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<NamedEType> getNamedE();

    /**
     * Returns the value of the '<em><b>String E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.StringEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>String E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getIoControlType_StringE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='string-E' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<StringEType> getStringE();

} // IoControlType
