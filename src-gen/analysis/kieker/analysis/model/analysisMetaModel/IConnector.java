/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Connector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.IConnector#getDstInputPort <em>Dst Input Port</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.IConnector#getSicOutputPort <em>Sic Output Port</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getConnector()
 * @model
 * @generated
 */
public interface IConnector extends EObject {
	/**
	 * Returns the value of the '<em><b>Dst Input Port</b></em>' reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.IInputPort#getInConnector <em>In Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dst Input Port</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dst Input Port</em>' reference.
	 * @see #setDstInputPort(IInputPort)
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getConnector_DstInputPort()
	 * @see kieker.analysis.model.analysisMetaModel.IInputPort#getInConnector
	 * @model opposite="inConnector" required="true"
	 * @generated
	 */
	IInputPort getDstInputPort();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.IConnector#getDstInputPort <em>Dst Input Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dst Input Port</em>' reference.
	 * @see #getDstInputPort()
	 * @generated
	 */
	void setDstInputPort(IInputPort value);

	/**
	 * Returns the value of the '<em><b>Sic Output Port</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.IOutputPort#getOutConnector <em>Out Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Sic Output Port</em>' container reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Sic Output Port</em>' container reference.
	 * @see #setSicOutputPort(IOutputPort)
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getConnector_SicOutputPort()
	 * @see kieker.analysis.model.analysisMetaModel.IOutputPort#getOutConnector
	 * @model opposite="outConnector" required="true" transient="false"
	 * @generated
	 */
	IOutputPort getSicOutputPort();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.IConnector#getSicOutputPort <em>Sic Output Port</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Sic Output Port</em>' container reference.
	 * @see #getSicOutputPort()
	 * @generated
	 */
	void setSicOutputPort(IOutputPort value);

} // IConnector
