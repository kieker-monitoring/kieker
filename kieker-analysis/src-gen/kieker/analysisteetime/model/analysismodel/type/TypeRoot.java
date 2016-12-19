/**
 */
package kieker.analysisteetime.model.analysismodel.type;

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
 *   <li>{@link kieker.analysisteetime.model.analysismodel.type.TypeRoot#getComponentTypes <em>Component Types</em>}</li>
 * </ul>
 *
 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getTypeRoot()
 * @model
 * @generated
 */
public interface TypeRoot extends EObject {
	/**
	 * Returns the value of the '<em><b>Component Types</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.analysisteetime.model.analysismodel.type.ComponentType},
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Component Types</em>' map isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Component Types</em>' map.
	 * @see kieker.analysisteetime.model.analysismodel.type.TypePackage#getTypeRoot_ComponentTypes()
	 * @model mapType="kieker.analysisteetime.model.analysismodel.type.EStringToComponentTypeMapEntry<org.eclipse.emf.ecore.EString, kieker.analysisteetime.model.analysismodel.type.ComponentType>" ordered="false"
	 * @generated
	 */
	EMap<String, ComponentType> getComponentTypes();

} // TypeRoot
