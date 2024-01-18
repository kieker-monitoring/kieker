/**
 */
package kieker.analysis.model.analysisMetaModel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage
 * @generated
 */
public interface MIAnalysisMetaModelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	MIAnalysisMetaModelFactory eINSTANCE = kieker.analysis.model.analysisMetaModel.impl.MAnalysisMetaModelFactory.init();

	/**
	 * Returns a new object of class '<em>Project</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Project</em>'.
	 * @generated
	 */
	MIProject createProject();

	/**
	 * Returns a new object of class '<em>Input Port</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Input Port</em>'.
	 * @generated
	 */
	MIInputPort createInputPort();

	/**
	 * Returns a new object of class '<em>Output Port</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Output Port</em>'.
	 * @generated
	 */
	MIOutputPort createOutputPort();

	/**
	 * Returns a new object of class '<em>Property</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Property</em>'.
	 * @generated
	 */
	MIProperty createProperty();

	/**
	 * Returns a new object of class '<em>Filter</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Filter</em>'.
	 * @generated
	 */
	MIFilter createFilter();

	/**
	 * Returns a new object of class '<em>Reader</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Reader</em>'.
	 * @generated
	 */
	MIReader createReader();

	/**
	 * Returns a new object of class '<em>Repository</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Repository</em>'.
	 * @generated
	 */
	MIRepository createRepository();

	/**
	 * Returns a new object of class '<em>Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Dependency</em>'.
	 * @generated
	 */
	MIDependency createDependency();

	/**
	 * Returns a new object of class '<em>Repository Connector</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Repository Connector</em>'.
	 * @generated
	 */
	MIRepositoryConnector createRepositoryConnector();

	/**
	 * Returns a new object of class '<em>Display</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Display</em>'.
	 * @generated
	 */
	MIDisplay createDisplay();

	/**
	 * Returns a new object of class '<em>View</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>View</em>'.
	 * @generated
	 */
	MIView createView();

	/**
	 * Returns a new object of class '<em>Display Connector</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return a new object of class '<em>Display Connector</em>'.
	 * @generated
	 */
	MIDisplayConnector createDisplayConnector();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the package supported by this factory.
	 * @generated
	 */
	MIAnalysisMetaModelPackage getAnalysisMetaModelPackage();

} // MIAnalysisMetaModelFactory
