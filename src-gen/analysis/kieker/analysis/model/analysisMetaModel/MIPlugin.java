/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Plugin</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getClassname <em>Classname</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getProperties <em>Properties</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getOutputPorts <em>Output Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin()
 * @model abstract="true"
 * @generated
 */
public interface MIPlugin extends EObject {
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
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Classname</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Classname</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Classname</em>' attribute.
	 * @see #setClassname(String)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_Classname()
	 * @model required="true"
	 * @generated
	 */
	String getClassname();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIPlugin#getClassname <em>Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Classname</em>' attribute.
	 * @see #getClassname()
	 * @generated
	 */
	void setClassname(String value);

	/**
	 * Returns the value of the '<em><b>Properties</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIProperty}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Properties</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Properties</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_Properties()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIProperty> getProperties();

	/**
	 * Returns the value of the '<em><b>Repositories</b></em>' reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIRepository}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositories</em>' reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositories</em>' reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_Repositories()
	 * @model
	 * @generated
	 */
	EList<MIRepository> getRepositories();

	/**
	 * Returns the value of the '<em><b>Output Ports</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIOutputPort}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Output Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Output Ports</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getPlugin_OutputPorts()
	 * @see kieker.analysis.model.analysisMetaModel.MIOutputPort#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<MIOutputPort> getOutputPorts();

} // MIPlugin
