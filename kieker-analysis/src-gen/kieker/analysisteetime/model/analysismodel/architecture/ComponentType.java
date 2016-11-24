/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

import org.eclipse.emf.common.util.EList;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getPackageName <em>Package Name</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getArchitectureRoot <em>Architecture Root</em>}</li>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getProvidedOperations <em>Provided Operations</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType()
 * @model
 * @generated
 */
public interface ComponentType extends EObject {
	/**
	 * Returns the value of the '<em><b>Package Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Package Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Package Name</em>' attribute.
	 * @see #setPackageName(String)
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType_PackageName()
	 * @model
	 * @generated
	 */
	String getPackageName();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getPackageName <em>Package Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Package Name</em>' attribute.
	 * @see #getPackageName()
	 * @generated
	 */
	void setPackageName(String value);

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
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Architecture Root</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot#getComponentTypes <em>Component Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Architecture Root</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Architecture Root</em>' reference.
	 * @see #setArchitectureRoot(ArchitectureRoot)
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType_ArchitectureRoot()
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot#getComponentTypes
	 * @model opposite="componentTypes"
	 * @generated
	 */
	ArchitectureRoot getArchitectureRoot();

	/**
	 * Sets the value of the '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getArchitectureRoot <em>Architecture Root</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Architecture Root</em>' reference.
	 * @see #getArchitectureRoot()
	 * @generated
	 */
	void setArchitectureRoot(ArchitectureRoot value);

	/**
	 * Returns the value of the '<em><b>Provided Operations</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.architecture.OperationType}.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Provided Operations</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Provided Operations</em>' reference list.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getComponentType_ProvidedOperations()
	 * @see kieker.analysisteetime.model.analysismodel.architecture.OperationType#getComponentType
	 * @model opposite="componentType"
	 * @generated
	 */
	EList<OperationType> getProvidedOperations();

} // ComponentType
