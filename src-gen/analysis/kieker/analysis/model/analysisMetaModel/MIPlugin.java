/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Plugin</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getRepositoryPorts <em>Repository Ports</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getOutputPorts <em>Output Ports</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getDisplays <em>Displays</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin()
 * @model abstract="true"
 * @generated
 */
public interface MIPlugin extends MIAnalysisComponent {
	/**
	 * Returns the value of the '<em><b>Repository Ports</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIRepositoryPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repository Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repository Ports</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_RepositoryPorts()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIRepositoryPort> getRepositoryPorts();

	/**
	 * Returns the value of the '<em><b>Output Ports</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIOutputPort}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Ports</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_OutputPorts()
	 * @see kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<MIOutputPort> getOutputPorts();

	/**
	 * Returns the value of the '<em><b>Displays</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIDisplay}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.MIDisplay#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Displays</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Displays</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_Displays()
	 * @see kieker.analysis.model.analysisMetaModel.MIDisplay#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<MIDisplay> getDisplays();

} // MIPlugin
