/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.FeatureMap;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Element Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ElementType#getArrayConstructorE <em>Array Constructor
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ElementType#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ElementType#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ElementType#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ElementType#getLiteralE <em>Literal E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ElementType#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getElementType()
 * @model extendedMetaData="name='element_._type' kind='elementOnly'"
 * @generated
 */
public interface ElementType extends EObject {
    /**
     * Returns the value of the '<em><b>Array Constructor E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Array Constructor E</em>' containment reference.
     * @see #setArrayConstructorE(ArrayConstructorEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getElementType_ArrayConstructorE()
     * @model containment="true" extendedMetaData="kind='element' name='array-constructor-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    ArrayConstructorEType getArrayConstructorE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ElementType#getArrayConstructorE
     * <em>Array Constructor E</em>}' containment reference. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Array Constructor E</em>' containment reference.
     * @see #getArrayConstructorE()
     * @generated
     */
    void setArrayConstructorE(ArrayConstructorEType value);

    /**
     * Returns the value of the '<em><b>Group</b></em>' attribute list. The list contents are of
     * type {@link org.eclipse.emf.ecore.util.FeatureMap.Entry}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Group</em>' attribute list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getElementType_Group()
     * @model unique="false" dataType="org.eclipse.emf.ecore.EFeatureMapEntry" many="true"
     *        extendedMetaData="kind='group' name='group:1'"
     * @generated
     */
    FeatureMap getGroup();

    /**
     * Returns the value of the '<em><b>Named E</b></em>' containment reference list. The list
     * contents are of type {@link org.oceandsl.tools.sar.fxtran.NamedEType}. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Named E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getElementType_NamedE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='named-E' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<NamedEType> getNamedE();

    /**
     * Returns the value of the '<em><b>Op E</b></em>' containment reference list. The list contents
     * are of type {@link org.oceandsl.tools.sar.fxtran.OpEType}. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Op E</em>' containment reference list.
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getElementType_OpE()
     * @model containment="true" transient="true" volatile="true" derived="true"
     *        extendedMetaData="kind='element' name='op-E' namespace='##targetNamespace'
     *        group='#group:1'"
     * @generated
     */
    EList<OpEType> getOpE();

    /**
     * Returns the value of the '<em><b>Literal E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Literal E</em>' containment reference.
     * @see #setLiteralE(LiteralEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getElementType_LiteralE()
     * @model containment="true" extendedMetaData="kind='element' name='literal-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    LiteralEType getLiteralE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ElementType#getLiteralE
     * <em>Literal E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Literal E</em>' containment reference.
     * @see #getLiteralE()
     * @generated
     */
    void setLiteralE(LiteralEType value);

    /**
     * Returns the value of the '<em><b>String E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>String E</em>' containment reference.
     * @see #setStringE(StringEType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getElementType_StringE()
     * @model containment="true" extendedMetaData="kind='element' name='string-E'
     *        namespace='##targetNamespace'"
     * @generated
     */
    StringEType getStringE();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ElementType#getStringE <em>String
     * E</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>String E</em>' containment reference.
     * @see #getStringE()
     * @generated
     */
    void setStringE(StringEType value);

} // ElementType
