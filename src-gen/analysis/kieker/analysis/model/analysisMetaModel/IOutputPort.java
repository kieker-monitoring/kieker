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
 *   <li>{@link kieker.analysis.model.analysisMetaModel.IOutputPort#getSubscribers <em>Subscribers</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getOutputPort()
 * @model
 * @generated
 */
public interface IOutputPort extends IPort {
	/**
	 * Returns the value of the '<em><b>Subscribers</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.IInputPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subscribers</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subscribers</em>' reference list.
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getOutputPort_Subscribers()
	 * @model
	 * @generated
	 */
	EList<IInputPort> getSubscribers();

} // IOutputPort
