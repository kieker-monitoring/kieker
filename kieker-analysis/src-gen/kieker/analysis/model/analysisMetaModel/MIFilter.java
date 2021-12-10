/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.common.util.EList;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Filter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.MIFilter#getInputPorts <em>Input Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getFilter()
 * @model
 * @generated
 */
public interface MIFilter extends MIPlugin {
	/**
	 * Returns the value of the '<em><b>Input Ports</b></em>' containment reference list.
	 * The list contents are of type {@link kieker.analysis.model.analysisMetaModel.MIInputPort}.
	 * It is bidirectional and its opposite is '{@link kieker.analysis.model.analysisMetaModel.MIInputPort#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <p>
	 * If the meaning of the '<em>Input Ports</em>' containment reference list isn't clear,
	 * there really should be more of a description here...
	 * </p>
	 * <!-- end-user-doc -->
	 * 
	 * @return the value of the '<em>Input Ports</em>' containment reference list.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage#getFilter_InputPorts()
	 * @see kieker.analysis.model.analysisMetaModel.MIInputPort#getParent
	 * @model opposite="parent" containment="true"
	 * @generated
	 */
	EList<MIInputPort> getInputPorts();

} // MIFilter
