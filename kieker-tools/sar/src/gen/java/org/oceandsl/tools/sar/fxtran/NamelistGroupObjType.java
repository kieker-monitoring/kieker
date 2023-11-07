/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Namelist Group Obj
 * Type</b></em>'. <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjType#getNamelistGroupObjN <em>Namelist
 * Group Obj N</em>}</li>
 * </ul>
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamelistGroupObjType()
 * @model extendedMetaData="name='namelist-group-obj_._type' kind='elementOnly'"
 * @generated
 */
public interface NamelistGroupObjType extends EObject {
    /**
     * Returns the value of the '<em><b>Namelist Group Obj N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the value of the '<em>Namelist Group Obj N</em>' containment reference.
     * @see #setNamelistGroupObjN(NamelistGroupObjNType)
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#getNamelistGroupObjType_NamelistGroupObjN()
     * @model containment="true" required="true" extendedMetaData="kind='element'
     *        name='namelist-group-obj-N' namespace='##targetNamespace'"
     * @generated
     */
    NamelistGroupObjNType getNamelistGroupObjN();

    /**
     * Sets the value of the
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjType#getNamelistGroupObjN <em>Namelist
     * Group Obj N</em>}' containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @param value
     *            the new value of the '<em>Namelist Group Obj N</em>' containment reference.
     * @see #getNamelistGroupObjN()
     * @generated
     */
    void setNamelistGroupObjN(NamelistGroupObjNType value);

} // NamelistGroupObjType
