/**
 */
package kieker.model.analysismodel.type;

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
 * <li>{@link kieker.model.analysismodel.type.TypeModel#getComponentTypes <em>Component Types</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.type.TypePackage#getTypeModel()
 * @model
 * @generated
 */
public interface TypeModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Component Types</b></em>' map.
	 * The key is of type {@link java.lang.String},
	 * and the value is of type {@link kieker.model.analysismodel.type.ComponentType},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Component Types</em>' map.
	 * @see kieker.model.analysismodel.type.TypePackage#getTypeModel_ComponentTypes()
	 * @model mapType="kieker.model.analysismodel.type.EStringToComponentTypeMapEntry&lt;org.eclipse.emf.ecore.EString,
	 *        kieker.model.analysismodel.type.ComponentType&gt;" ordered="false"
	 * @generated
	 */
	EMap<String, ComponentType> getComponentTypes();

} // TypeModel
