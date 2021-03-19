/**
 */
package kieker.model.analysismodel.assembly;

import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.AssemblyModel#getAssemblyComponents <em>Assembly Components</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyModel()
 * @model
 * @generated
 */
public interface AssemblyModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Assembly Components</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.assembly.AssemblyComponent},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Assembly Components</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Assembly Components</em>' map.
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyModel_AssemblyComponents()
	 * @model mapType="kieker.model.analysismodel.assembly.EStringToAssemblyComponentMapEntry<org.eclipse.emf.ecore.EString,
	 *        kieker.model.analysismodel.assembly.AssemblyComponent>" ordered="false"
	 * @generated
	 */
	EMap<String, AssemblyComponent> getAssemblyComponents();

} // AssemblyModel
