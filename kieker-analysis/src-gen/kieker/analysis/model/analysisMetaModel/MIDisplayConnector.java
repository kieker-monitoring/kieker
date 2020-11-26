/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Display Connector</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector#getDisplay <em>Display</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getDisplayConnector()
 * @model
 * @generated
 */
public interface MIDisplayConnector extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getDisplayConnector_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Display</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Display</em>' reference isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Display</em>' reference.
	 * @see #setDisplay(MIDisplay)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getDisplayConnector_Display()
	 * @model required="true"
	 * @generated
	 */
	MIDisplay getDisplay();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector#getDisplay <em>Display</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Display</em>' reference.
	 * @see #getDisplay()
	 * @generated
	 */
	void setDisplay(MIDisplay value);

	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Id</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getDisplayConnector_Id()
	 * @model id="true" required="true" ordered="false"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // MIDisplayConnector
