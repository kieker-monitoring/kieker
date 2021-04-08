/**
 */
package kieker.model.analysismodel.sources;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Source Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.sources.SourceModel#getSources <em>Sources</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.sources.SourcesPackage#getSourceModel()
 * @model
 * @generated
 */
public interface SourceModel extends EObject {
	/**
	 * Returns the value of the '<em><b>Sources</b></em>' map.
	 * The key is of type {@link org.eclipse.emf.ecore.EObject},
	 * and the value is of type list of {@link java.lang.String},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sources</em>' map.
	 * @see kieker.model.analysismodel.sources.SourcesPackage#getSourceModel_Sources()
	 * @model mapType="kieker.model.analysismodel.sources.EObjectToSourcesEntry&lt;org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EString&gt;"
	 * @generated
	 */
	EMap<EObject, EList<String>> getSources();

} // SourceModel
