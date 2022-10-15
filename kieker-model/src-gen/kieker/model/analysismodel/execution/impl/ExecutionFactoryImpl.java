/**
 */
package kieker.model.analysismodel.execution.impl;

import java.util.Map;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;

import kieker.model.analysismodel.execution.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY: return (EObject)createDeployedOperationsPairToInvocationMapEntry();
			case ExecutionPackage.INVOCATION: return createInvocation();
			case ExecutionPackage.STORAGE_DATAFLOW: return createStorageDataflow();
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY: return (EObject)createDeployedOperationsPairToDeployedStorageMapEntry();
			case ExecutionPackage.TUPLE: return createTuple();
			case ExecutionPackage.OPERATION_DATAFLOW: return createOperationDataflow();
			case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY: return (EObject)createDeployedOperationsPairToDeployedOperationsMapEntry();
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
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case ExecutionPackage.EDIRECTION:
				return createEDirectionFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case ExecutionPackage.EDIRECTION:
				return convertEDirectionToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
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
	public Map.Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> createDeployedOperationsPairToInvocationMapEntry() {
		DeployedOperationsPairToInvocationMapEntryImpl deployedOperationsPairToInvocationMapEntry = new DeployedOperationsPairToInvocationMapEntryImpl();
		return deployedOperationsPairToInvocationMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Invocation createInvocation() {
		InvocationImpl invocation = new InvocationImpl();
		return invocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StorageDataflow createStorageDataflow() {
		StorageDataflowImpl storageDataflow = new StorageDataflowImpl();
		return storageDataflow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> createDeployedOperationsPairToDeployedStorageMapEntry() {
		DeployedOperationsPairToDeployedStorageMapEntryImpl deployedOperationsPairToDeployedStorageMapEntry = new DeployedOperationsPairToDeployedStorageMapEntryImpl();
		return deployedOperationsPairToDeployedStorageMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <F, S> Tuple<F, S> createTuple() {
		TupleImpl<F, S> tuple = new TupleImpl<F, S>();
		return tuple;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OperationDataflow createOperationDataflow() {
		OperationDataflowImpl operationDataflow = new OperationDataflowImpl();
		return operationDataflow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> createDeployedOperationsPairToDeployedOperationsMapEntry() {
		DeployedOperationsPairToDeployedOperationsMapEntryImpl deployedOperationsPairToDeployedOperationsMapEntry = new DeployedOperationsPairToDeployedOperationsMapEntryImpl();
		return deployedOperationsPairToDeployedOperationsMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDirection createEDirectionFromString(EDataType eDataType, String initialValue) {
		EDirection result = EDirection.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEDirectionToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
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

} //ExecutionFactoryImpl
