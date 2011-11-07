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
 * <li>{@link kieker.analysis.model.analysisMetaModel.IOutputPort#getOutConnector <em>Out Connector</em>}</li>
 * </ul>
 * </p>
 * 
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getOutputPort()
 * @model
 * @generated
 */
public interface IOutputPort extends IPort {
	/**
	 * Returns the value of the '<em><b>Out Connector</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.IConnector}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.IConnector#getSicOutputPort <em>Sic Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Out Connector</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Out Connector</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getOutputPort_OutConnector()
	 * @see kieker.analysis.model.analysisMetaModel.IConnector#getSicOutputPort
	 * @model opposite="sicOutputPort" containment="true"
	 * @generated
	 */
	EList<IConnector> getOutConnector();

} // IOutputPort
