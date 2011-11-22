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
 * A representation of the model object '<em><b>Input Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.IInputPort#getInConnector <em>In Connector</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getInputPort()
 * @model
 * @generated
 */
public interface IInputPort extends IPort {
	/**
	 * Returns the value of the '<em><b>In Connector</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.IConnector}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.IConnector#getDstInputPort <em>Dst Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>In Connector</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Connector</em>' reference list.
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getInputPort_InConnector()
	 * @see kieker.analysis.model.analysisMetaModel.IConnector#getDstInputPort
	 * @model opposite="dstInputPort"
	 * @generated
	 */
	EList<IConnector> getInConnector();

} // IInputPort
