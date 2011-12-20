/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Analysis Plugin</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIAnalysisPlugin#getInputPorts <em>Input Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getAnalysisPlugin()
 * @model
 * @generated
 */
public interface MIAnalysisPlugin extends MIPlugin {
	/**
	 * Returns the value of the '<em><b>Input Ports</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIInputPort}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.MIInputPort#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Input Ports</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getAnalysisPlugin_InputPorts()
	 * @see kieker.analysis.model.analysisMetaModel.MIInputPort#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<MIInputPort> getInputPorts();

} // MIAnalysisPlugin
