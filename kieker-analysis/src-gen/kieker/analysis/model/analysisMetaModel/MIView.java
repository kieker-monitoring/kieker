/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>View</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIView#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIView#getDescription <em>Description</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIView#getDisplayConnectors <em>Display Connectors</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIView#getId <em>Id</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getView()
 * @model
 * @generated
 */
public interface MIView extends EObject {
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
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getView_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIView#getName <em>Name</em>}' attribute.
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
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Description</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getView_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIView#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Display Connectors</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Display Connectors</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 *
	 * @return the value of the '<em>Display Connectors</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getView_DisplayConnectors()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIDisplayConnector> getDisplayConnectors();

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
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getView_Id()
	 * @model id="true" required="true" ordered="false"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIView#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

} // MIView
