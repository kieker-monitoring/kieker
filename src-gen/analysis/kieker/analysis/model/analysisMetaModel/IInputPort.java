/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Input Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.IInputPort#getParent <em>Parent</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getInputPort()
 * @model
 * @generated
 */
public interface IInputPort extends IPort {
	/**
	 * Returns the value of the '<em><b>Parent</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.IAnalysisPlugin#getInputPorts <em>Input Ports</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Parent</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parent</em>' container reference.
	 * @see #setParent(IAnalysisPlugin)
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getInputPort_Parent()
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisPlugin#getInputPorts
	 * @model opposite="inputPorts" required="true" transient="false"
	 * @generated
	 */
	IAnalysisPlugin getParent();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.IInputPort#getParent <em>Parent</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parent</em>' container reference.
	 * @see #getParent()
	 * @generated
	 */
	void setParent(IAnalysisPlugin value);

} // IInputPort
