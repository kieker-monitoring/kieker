/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Analysis Node</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIAnalysisNode#getContainedPlugins <em>Contained Plugins</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIAnalysisNode#getContainedRepositories <em>Contained Repositories</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.MIAnalysisNode#getNodeName <em>Node Name</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getAnalysisNode()
 * @model
 * @generated
 */
public interface MIAnalysisNode extends MIFilter {
	/**
	 * Returns the value of the '<em><b>Contained Plugins</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIPlugin}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Plugins</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Plugins</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getAnalysisNode_ContainedPlugins()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIPlugin> getContainedPlugins();

	/**
	 * Returns the value of the '<em><b>Contained Repositories</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIRepository}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Contained Repositories</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Contained Repositories</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getAnalysisNode_ContainedRepositories()
	 * @model containment="true"
	 * @generated
	 */
	EList<MIRepository> getContainedRepositories();

	/**
	 * Returns the value of the '<em><b>Node Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Node Name</em>' attribute isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Node Name</em>' attribute.
	 * @see #setNodeName(String)
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getAnalysisNode_NodeName()
	 * @model required="true"
	 * @generated
	 */
	String getNodeName();

	/**
	 * Sets the value of the '{@link kieker.analysis.model.analysisMetaModel.MIAnalysisNode#getNodeName <em>Node Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Node Name</em>' attribute.
	 * @see #getNodeName()
	 * @generated
	 */
	void setNodeName(String value);

} // MIAnalysisNode
