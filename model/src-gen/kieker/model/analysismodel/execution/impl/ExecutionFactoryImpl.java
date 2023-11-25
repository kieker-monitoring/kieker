/**
 */
package kieker.model.analysismodel.execution.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class ExecutionFactoryImpl extends EFactoryImpl implements ExecutionFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static ExecutionFactory init() {
		try {
			final ExecutionFactory theExecutionFactory = (ExecutionFactory) EPackage.Registry.INSTANCE.getEFactory(ExecutionPackage.eNS_URI);
			if (theExecutionFactory != null) {
				return theExecutionFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new ExecutionFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ExecutionFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case ExecutionPackage.EXECUTION_MODEL:
			return this.createExecutionModel();
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY:
			return (EObject) this.createDeployedOperationsPairToInvocationMapEntry();
		case ExecutionPackage.INVOCATION:
			return this.createInvocation();
		case ExecutionPackage.STORAGE_DATAFLOW:
			return this.createStorageDataflow();
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY:
			return (EObject) this.createDeployedOperationsPairToDeployedStorageMapEntry();
		case ExecutionPackage.TUPLE:
			return this.createTuple();
		case ExecutionPackage.OPERATION_DATAFLOW:
			return this.createOperationDataflow();
		case ExecutionPackage.DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY:
			return (EObject) this.createDeployedOperationsPairToDeployedOperationsMapEntry();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object createFromString(final EDataType eDataType, final String initialValue) {
		switch (eDataType.getClassifierID()) {
		case ExecutionPackage.EDIRECTION:
			return this.createEDirectionFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String convertToString(final EDataType eDataType, final Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case ExecutionPackage.EDIRECTION:
			return this.convertEDirectionToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ExecutionModel createExecutionModel() {
		final ExecutionModelImpl executionModel = new ExecutionModelImpl();
		return executionModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> createDeployedOperationsPairToInvocationMapEntry() {
		final DeployedOperationsPairToInvocationMapEntryImpl deployedOperationsPairToInvocationMapEntry = new DeployedOperationsPairToInvocationMapEntryImpl();
		return deployedOperationsPairToInvocationMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Invocation createInvocation() {
		final InvocationImpl invocation = new InvocationImpl();
		return invocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StorageDataflow createStorageDataflow() {
		final StorageDataflowImpl storageDataflow = new StorageDataflowImpl();
		return storageDataflow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> createDeployedOperationsPairToDeployedStorageMapEntry() {
		final DeployedOperationsPairToDeployedStorageMapEntryImpl deployedOperationsPairToDeployedStorageMapEntry = new DeployedOperationsPairToDeployedStorageMapEntryImpl();
		return deployedOperationsPairToDeployedStorageMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public <F, S> Tuple<F, S> createTuple() {
		final TupleImpl<F, S> tuple = new TupleImpl<>();
		return tuple;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationDataflow createOperationDataflow() {
		final OperationDataflowImpl operationDataflow = new OperationDataflowImpl();
		return operationDataflow;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> createDeployedOperationsPairToDeployedOperationsMapEntry() {
		final DeployedOperationsPairToDeployedOperationsMapEntryImpl deployedOperationsPairToDeployedOperationsMapEntry = new DeployedOperationsPairToDeployedOperationsMapEntryImpl();
		return deployedOperationsPairToDeployedOperationsMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EDirection createEDirectionFromString(final EDataType eDataType, final String initialValue) {
		final EDirection result = EDirection.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertEDirectionToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ExecutionPackage getExecutionPackage() {
		return (ExecutionPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static ExecutionPackage getPackage() {
		return ExecutionPackage.eINSTANCE;
	}

} // ExecutionFactoryImpl
