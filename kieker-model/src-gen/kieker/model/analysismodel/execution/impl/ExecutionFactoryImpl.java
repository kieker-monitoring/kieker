/**
 */
package kieker.model.analysismodel.execution.impl;

import java.util.Map;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.*;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class ExecutionFactoryImpl extends EFactoryImpl implements ExecutionFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static ExecutionFactory init() {
		try {
			ExecutionFactory theExecutionFactory = (ExecutionFactory)EPackage.Registry.INSTANCE.getEFactory(ExecutionPackage.eNS_URI);
			if (theExecutionFactory != null) {
				return theExecutionFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ExecutionFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ExecutionFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case ExecutionPackage.EXECUTION_MODEL: return createExecutionModel();
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY: return (EObject)createDeployedOperationsPairToAggregatedInvocationMapEntry();
			case ExecutionPackage.AGGREGATED_INVOCATION: return createAggregatedInvocation();
			case ExecutionPackage.AGGREGATED_STORAGE_ACCESS: return createAggregatedStorageAccess();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExecutionModel createExecutionModel() {
		ExecutionModelImpl executionModel = new ExecutionModelImpl();
		return executionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<kieker.model.analysismodel.util.ComposedKey<DeployedOperation, DeployedOperation>, AggregatedInvocation> createDeployedOperationsPairToAggregatedInvocationMapEntry() {
		DeployedOperationsPairToAggregatedInvocationMapEntryImpl deployedOperationsPairToAggregatedInvocationMapEntry = new DeployedOperationsPairToAggregatedInvocationMapEntryImpl();
		return deployedOperationsPairToAggregatedInvocationMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AggregatedInvocation createAggregatedInvocation() {
		AggregatedInvocationImpl aggregatedInvocation = new AggregatedInvocationImpl();
		return aggregatedInvocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AggregatedStorageAccess createAggregatedStorageAccess() {
		AggregatedStorageAccessImpl aggregatedStorageAccess = new AggregatedStorageAccessImpl();
		return aggregatedStorageAccess;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ExecutionPackage getExecutionPackage() {
		return (ExecutionPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ExecutionPackage getPackage() {
		return ExecutionPackage.eINSTANCE;
	}

} // ExecutionFactoryImpl
