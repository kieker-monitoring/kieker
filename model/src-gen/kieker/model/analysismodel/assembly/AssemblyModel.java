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
 * <li>{@link kieker.model.analysismodel.assembly.AssemblyModel#getComponents <em>Components</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyModel()
 * @model
 * @generated
 */
public interface AssemblyModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Components</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.assembly.AssemblyComponent},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Components</em>' map.
	 * @see kieker.model.analysismodel.assembly.AssemblyPackage#getAssemblyModel_Components()
	 * @model mapType="kieker.model.analysismodel.assembly.EStringToAssemblyComponentMapEntry&lt;org.eclipse.emf.ecore.EString,
	 *        kieker.model.analysismodel.assembly.AssemblyComponent&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, AssemblyComponent> getComponents();

} // AssemblyModel
