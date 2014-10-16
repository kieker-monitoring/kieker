/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIProject#getPlugins <em>Plugins</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIProject#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIProject#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIProject#getDependencies <em>Dependencies</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIProject#getViews <em>Views</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIProject#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getProject()
 * @model
 * @generated
 */
public interface MIProject extends EObject {
	/**
	 * Returns the value of the '<em><b>Plugins</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIPlugin}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Plugins</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Plugins</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getProject_Plugins()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIPlugin> getPlugins();

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
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getProject_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIProject#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Repositories</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIRepository}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Repositories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Repositories</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getProject_Repositories()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIRepository> getRepositories();

	/**
	 * Returns the value of the '<em><b>Dependencies</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIDependency}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Dependencies</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Dependencies</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getProject_Dependencies()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIDependency> getDependencies();

	/**
	 * Returns the value of the '<em><b>Views</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIView}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Views</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Views</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getProject_Views()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIView> getViews();

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
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getProject_Properties()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIProperty> getProperties();

} // MIProject
