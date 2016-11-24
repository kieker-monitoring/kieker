/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Root</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot#getComponentTypes <em>Component Types</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getArchitectureRoot()
 * @model
 * @generated
 */
public interface ArchitectureRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Component Types</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType}.
	 * It is bidirectional and its opposite is '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getArchitectureRoot <em>Architecture Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Types</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Types</em>' reference list.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitecturePackage#getArchitectureRoot_ComponentTypes()
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getArchitectureRoot
	 * @model opposite="architectureRoot"
	 * @generated
	 */
	EList<ComponentType> getComponentTypes();

} // ArchitectureRoot
