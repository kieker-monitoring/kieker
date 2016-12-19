/**
 */
package kieker.analysisteetime.model.analysismodel.assembly;

import org.eclipse.emf.common.util.EMap;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyRoot#getAssemblyComponents <em>Assembly Components</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage#getAssemblyRoot()
 * @model
 * @generated
 */
public interface AssemblyRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Components</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assembly Components</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assembly Components</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.assembly.AssemblyPackage#getAssemblyRoot_AssemblyComponents()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.assembly.EStringToAssemblyComponentMapEntry<org.eclipse.emf.ecore.EString, kieker.analysisteetime.model.analysismodel.assembly.AssemblyComponent>" ordered="false"
	 * @generated
	 */
	EMap<String, AssemblyComponent> getAssemblyComponents();

} // AssemblyRoot
