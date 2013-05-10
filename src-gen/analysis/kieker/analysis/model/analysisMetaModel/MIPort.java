/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Port</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPort#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPort#getEventTypes <em>Event Types</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPort#isAsynchronous <em>Asynchronous</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPort()
 * @model abstract="true"
 * @generated
 */
public interface MIPort extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPort_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIPort#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Event Types</b></em>' attribute list.
	 * The list contents are of type {@link java.lang.String}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Event Types</em>' attribute list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Event Types</em>' attribute list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPort_EventTypes()
	 * @model required="true"
	 * @generated
	 */
	EList<String> getEventTypes();

	/**
	 * Returns the value of the '<em><b>Asynchronous</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Asynchronous</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Asynchronous</em>' attribute.
	 * @see #setAsynchronous(boolean)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPort_Asynchronous()
	 * @model default="false" required="true"
	 * @generated
	 */
	boolean isAsynchronous();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIPort#isAsynchronous <em>Asynchronous</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Asynchronous</em>' attribute.
	 * @see #isAsynchronous()
	 * @generated
	 */
	void setAsynchronous(boolean value);

} // MIPort
