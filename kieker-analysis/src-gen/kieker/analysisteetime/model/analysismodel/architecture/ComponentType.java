/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getSignature <em>Signature</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getProvidedOperations <em>Provided Operations</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType()
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
	 * @return the value of the '<em>Signature</em>' attribute.
	 * @see #setSignature(String)
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType_Signature()
	 * @model
	 * @generated
	 */
	String getSignature();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getSignature <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Signature</em>' attribute.
	 * @see #getSignature()
	 * @generated
	 */
	void setSignature(String value);

	/**
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Architecture Root</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @model kind="operation"
	 *        annotation="http://www.eclipse.org/emf/2002/GenModel body='org.eclipse.emf.ecore.EObject container = this.eContainer();\r\nif (container != null) {\r\n\torg.eclipse.emf.ecore.EObject containerContainer = container.eContainer();\r\n\tif (containerContainer != null) {\r\n\t\treturn (ArchitectureRoot) containerContainer ;\r\n\t}\r\n}\r\nreturn null;\r\n'"
	 * @generated
	 */
	ArchitectureRoot getArchitectureRoot();

	/**
	 * Returns the value of the '<em><b>Provided Operations</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.architecture.OperationType},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provided Operations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Operations</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType_ProvidedOperations()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.architecture.EStringToOperationTypeMapEntry<org.eclipse.emf.ecore.EString, kieker.analysisteetime.model.analysismodel.architecture.OperationType>" ordered="false"
	 * @generated
	 */
	EMap<String, OperationType> getProvidedOperations();

} // ComponentType
