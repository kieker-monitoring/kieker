/**
 */
package kieker.analysisteetime.model.analysismodel.type;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getSignature <em>Signature</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getReturnType <em>Return Type</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getModifiers <em>Modifiers</em>}</li>
 * <li>{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getParameterTypes <em>Parameter Types</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getOperationType()
 * @model
 * @generated
 */
public interface OperationType extends EObject {
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
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getOperationType_Signature()
	 * @model
	 * @generated
	 */
	String getSignature();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getSignature <em>Signature</em>}' attribute.
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
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getOperationType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getName <em>Name</em>}' attribute.
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
	 * Returns the value of the '<em><b>Return Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Return Type</em>' attribute.
	 * @see #setReturnType(String)
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getOperationType_ReturnType()
	 * @model
	 * @generated
	 */
	String getReturnType();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.type.OperationType#getReturnType <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Return Type</em>' attribute.
	 * @see #getReturnType()
	 * @generated
	 */
	void setReturnType(String value);

	/**
	 * Returns the value of the '<em><b>Modifiers</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Modifiers</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Modifiers</em>' attribute list.
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getOperationType_Modifiers()
	 * @model
	 * @generated
	 */
	EList<String> getModifiers();

	/**
	 * Returns the value of the '<em><b>Parameter Types</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parameter Types</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Parameter Types</em>' attribute list.
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getOperationType_ParameterTypes()
	 * @model
	 * @generated
	 */
	EList<String> getParameterTypes();

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.eclipse.emf.ecore.EObject container = this.eContainer();\r\nif (container != null)
	 *        {\r\n\torg.eclipse.emf.ecore.EObject containerContainer = container.eContainer();\r\n\tif (containerContainer != null) {\r\n\t\treturn (ComponentType)
	 *        containerContainer ;\r\n\t}\r\n}\r\nreturn null;\r\n'"
	 * @generated
	 */
	ComponentType getComponentType();

} // OperationType
