/**
 */
package kieker.model.collection;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connections</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 * <li>{@link kieker.model.collection.Connections#getConnections <em>Connections</em>}</li>
 * </ul>
 *
 * @see kieker.model.collection.CollectionPackage#getConnections()
 * @model
 * @generated
 */
public interface Connections extends EObject {
	/**
	 * Returns the value of the '<em><b>Connections</b></em>' map.
	 * The key is of type {@link kieker.model.collection.Coupling},
	 * and the value is of type {@link kieker.model.collection.OperationCollection},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Connections</em>' map.
	 * @see kieker.model.collection.CollectionPackage#getConnections_Connections()
	 * @model mapType="kieker.model.collection.CouplingToOperationMap&lt;kieker.model.collection.Coupling, kieker.model.collection.OperationCollection&gt;"
	 * @generated
	 */
	EMap<Coupling, OperationCollection> getConnections();

} // Connections
