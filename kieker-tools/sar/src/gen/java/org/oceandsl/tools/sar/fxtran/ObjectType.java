/**
 */
package org.oceandsl.tools.sar.fxtran;

import java.math.BigInteger;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Object Type</b></em>'. <!--
 * end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ObjectType#getFile <em>File</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ObjectType#getOpenacc <em>Openacc</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ObjectType#getOpenmp <em>Openmp</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ObjectType#getSourceForm <em>Source Form</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.ObjectType#getSourceWidth <em>Source Width</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getObjectType()
 * @model extendedMetaData="name='object_._type' kind='elementOnly'"
 * @generated
 */
public interface ObjectType extends EObject {
    /**
     * Returns the value of the '<em><b>File</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>File</em>' containment reference.
     * @see #setFile(FileType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getObjectType_File()
     * @model containment="true" required="true" extendedMetaData="kind='element' name='file'
     *        namespace='##targetNamespace'"
     * @generated
     */
    FileType getFile();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getFile
     * <em>File</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>File</em>' containment reference.
     * @see #getFile()
     * @generated
     */
    void setFile(FileType value);

    /**
     * Returns the value of the '<em><b>Openacc</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Openacc</em>' attribute.
     * @see #setOpenacc(BigInteger)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getObjectType_Openacc()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer" required="true"
     *        extendedMetaData="kind='attribute' name='openacc'"
     * @generated
     */
    BigInteger getOpenacc();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getOpenacc
     * <em>Openacc</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Openacc</em>' attribute.
     * @see #getOpenacc()
     * @generated
     */
    void setOpenacc(BigInteger value);

    /**
     * Returns the value of the '<em><b>Openmp</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the value of the '<em>Openmp</em>' attribute.
     * @see #setOpenmp(BigInteger)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getObjectType_Openmp()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer" required="true"
     *        extendedMetaData="kind='attribute' name='openmp'"
     * @generated
     */
    BigInteger getOpenmp();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getOpenmp
     * <em>Openmp</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Openmp</em>' attribute.
     * @see #getOpenmp()
     * @generated
     */
    void setOpenmp(BigInteger value);

    /**
     * Returns the value of the '<em><b>Source Form</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Source Form</em>' attribute.
     * @see #setSourceForm(String)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getObjectType_SourceForm()
     * @model dataType="org.eclipse.emf.ecore.xml.type.NCName" required="true"
     *        extendedMetaData="kind='attribute' name='source-form'"
     * @generated
     */
    String getSourceForm();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getSourceForm
     * <em>Source Form</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Source Form</em>' attribute.
     * @see #getSourceForm()
     * @generated
     */
    void setSourceForm(String value);

    /**
     * Returns the value of the '<em><b>Source Width</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the value of the '<em>Source Width</em>' attribute.
     * @see #setSourceWidth(BigInteger)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getObjectType_SourceWidth()
     * @model dataType="org.eclipse.emf.ecore.xml.type.Integer" required="true"
     *        extendedMetaData="kind='attribute' name='source-width'"
     * @generated
     */
    BigInteger getSourceWidth();

    /**
     * Sets the value of the '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getSourceWidth
     * <em>Source Width</em>}' attribute. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Source Width</em>' attribute.
     * @see #getSourceWidth()
     * @generated
     */
    void setSourceWidth(BigInteger value);

} // ObjectType
