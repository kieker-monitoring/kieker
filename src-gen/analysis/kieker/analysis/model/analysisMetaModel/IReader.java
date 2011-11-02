/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel;


/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Reader</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.IReader#getInitString <em>Init String</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getReader()
 * @model
 * @generated
 */
public interface IReader extends IPlugin {
	/**
	 * Returns the value of the '<em><b>Init String</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Init String</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Init String</em>' attribute.
	 * @see #setInitString(String)
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getReader_InitString()
	 * @model
	 * @generated
	 */
	String getInitString();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.IReader#getInitString <em>Init String</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init String</em>' attribute.
	 * @see #getInitString()
	 * @generated
	 */
	void setInitString(String value);

} // IReader
