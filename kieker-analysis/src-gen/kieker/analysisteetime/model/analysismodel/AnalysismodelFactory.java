/**
 */
package kieker.analysisteetime.model.analysismodel;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see kieker.analysisteetime.model.analysismodel.AnalysismodelPackage
 * @generated
 */
public interface AnalysismodelFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnalysismodelFactory eINSTANCE = kieker.analysisteetime.model.analysismodel.impl.AnalysismodelFactoryImpl.init();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	AnalysismodelPackage getAnalysismodelPackage();

} //AnalysismodelFactory
