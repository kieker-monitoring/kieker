/**
 */
package kieker.analysisteetime.model.analysismodel.type;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Component Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getSignature <em>Signature</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getProvidedOperations <em>Provided Operations</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getPackage <em>Package</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getComponentType()
 * @model
 * @generated
 */
public interface ComponentType extends EObject {
	/**
	 * Returns the value of the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Signature</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Signature</em>' attribute.
	 * @see #setSignature(String)
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getComponentType_Signature()
	 * @model
	 * @generated
	 */
	String getSignature();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getSignature <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Signature</em>' attribute.
	 * @see #getSignature()
	 * @generated
	 */
	void setSignature(String value);

	/**
	 * Returns the value of the '<em><b>Provided Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.type.OperationType},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provided Operations</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Provided Operations</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getComponentType_ProvidedOperations()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.type.EStringToOperationTypeMapEntry<org.eclipse.emf.ecore.EString,
	 *        kieker.analysisteetime.model.analysismodel.type.OperationType>" ordered="false"
	 * @generated
	 */
	EMap<String, OperationType> getProvidedOperations();

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getComponentType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Package</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Package</em>' attribute.
	 * @see #setPackage(String)
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getComponentType_Package()
	 * @model
	 * @generated
	 */
	String getPackage();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.type.ComponentType#getPackage <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Package</em>' attribute.
	 * @see #getPackage()
	 * @generated
	 */
	void setPackage(String value);

} // ComponentType
