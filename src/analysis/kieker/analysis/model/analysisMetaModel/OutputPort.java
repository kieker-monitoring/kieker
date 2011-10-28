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
 * A representation of the model object '<em><b>Output Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.OutputPort#getOutConnector <em>Out Connector</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.AnalysisMetaModelPackage#getOutputPort()
 * @model
 * @generated
 */
public interface OutputPort extends Port {
	/**
	 * Returns the value of the '<em><b>Out Connector</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.Connector}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.Connector#getSicOutputPort <em>Sic Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Out Connector</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Out Connector</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.AnalysisMetaModelPackage#getOutputPort_OutConnector()
	 * @see kieker.analysis.model.analysisMetaModel.Connector#getSicOutputPort
	 * @model opposite="sicOutputPort" containment="true"
	 * @generated
	 */
	EList<Connector> getOutConnector();

} // OutputPort
