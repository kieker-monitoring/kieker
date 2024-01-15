/**
 */
package kieker.model.analysismodel.source;

import org.eclipse.emf.common.util.EList;
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
 * <li>{@link kieker.model.analysismodel.source.SourceModel#getSources <em>Sources</em>}</li>
 * </ul>
 *
 * @see kieker.model.analysismodel.source.SourcePackage#getSourceModel()
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
	 *
	 * @return the value of the '<em>Sources</em>' map.
	 * @see kieker.model.analysismodel.source.SourcePackage#getSourceModel_Sources()
	 * @model mapType="kieker.model.analysismodel.source.EObjectToSourceEntry&lt;org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EString&gt;"
	 * @generated
	 */
	EMap<EObject, EList<String>> getSources();

} // SourceModel
