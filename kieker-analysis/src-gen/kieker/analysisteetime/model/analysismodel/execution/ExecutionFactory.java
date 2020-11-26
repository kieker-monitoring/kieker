/**
 */
package kieker.analysisteetime.model.analysismodel.execution;

import org.eclipse.emf.ecore.EFactory;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * 
 * @see kieker.analysisteetime.model.analysismodel.execution.ExecutionPackage
 * @generated
 */
public interface ExecutionFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	ExecutionFactory eINSTANCE = kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Model</em>'.
	 * @generated
	 */
	ExecutionModel createExecutionModel();

	/**
	 * Returns a new object of class '<em>Aggregated Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return a new object of class '<em>Aggregated Invocation</em>'.
	 * @generated
	 */
	AggregatedInvocation createAggregatedInvocation();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the package supported by this factory.
	 * @generated
	 */
	ExecutionPackage getExecutionPackage();

} // ExecutionFactory
