/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getReturnValueType <em>Return Value Type</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getComponentType <em>Component Type</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getOperationType()
 * @model
 * @generated
 */
public interface OperationType extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getOperationType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Return Value Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Return Value Type</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Value Type</em>' attribute.
	 * @see #setReturnValueType(String)
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getOperationType_ReturnValueType()
	 * @model
	 * @generated
	 */
	String getReturnValueType();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getReturnValueType <em>Return Value Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Value Type</em>' attribute.
	 * @see #getReturnValueType()
	 * @generated
	 */
	void setReturnValueType(String value);

	/**
	 * Returns the value of the '<em><b>Component Type</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getProvidedOperations <em>Provided Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Type</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Type</em>' reference.
	 * @see #setComponentType(ComponentType)
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getOperationType_ComponentType()
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getProvidedOperations
	 * @model opposite="providedOperations"
	 * @generated
	 */
	ComponentType getComponentType();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getComponentType <em>Component Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Component Type</em>' reference.
	 * @see #getComponentType()
	 * @generated
	 */
	void setComponentType(ComponentType value);

} // OperationType
