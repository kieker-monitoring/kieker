/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * 
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.IProject#getConfigurables <em>Configurables</em>}</li>
 * </ul>
 * </p>
 * 
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getProject()
 * @model
 * @generated
 */
public interface IProject extends IConfigurable {
	/**
	 * Returns the value of the '<em><b>Configurables</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.IConfigurable}.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Configurables</em>' containment reference list isn't clear, there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Configurables</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage#getProject_Configurables()
	 * @model containment="true"
	 * @generated
	 */
	EList<IConfigurable> getConfigurables();

} // IProject
