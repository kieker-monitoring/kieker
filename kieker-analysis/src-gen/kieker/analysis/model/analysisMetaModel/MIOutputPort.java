/**
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
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIOutputPort#getSubscribers <em>Subscribers</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getOutputPort()
 * @model
 * @generated
 */
public interface MIOutputPort extends MIPort {
	/**
	 * Returns the value of the '<em><b>Subscribers</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIInputPort}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Subscribers</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Subscribers</em>' reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getOutputPort_Subscribers()
	 * @model
	 * @generated
	 */
	EList<MIInputPort> getSubscribers();

	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getOutputPorts <em>Output Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(MIPlugin)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getOutputPort_Parent()
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin#getOutputPorts
	 * @model opposite="outputPorts" required="true" transient="false"
	 * @generated
	 */
	MIPlugin getParent();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(MIPlugin value);

} // MIOutputPort
