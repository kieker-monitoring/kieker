/**
 */
package kieker.model.analysismodel.execution.util;

import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.execution.Tuple;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 *
 * @see kieker.model.analysismodel.execution.ExecutionPackage
 * @generated
 */
public class ExecutionAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static ExecutionPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ExecutionAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = ExecutionPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 *
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(final Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ExecutionSwitch<Adapter> modelSwitch = new ExecutionSwitch<Adapter>() {
		@Override
		public Adapter caseExecutionModel(final ExecutionModel object) {
			return ExecutionAdapterFactory.this.createExecutionModelAdapter();
		}

		@Override
		public Adapter caseDeployedOperationsPairToInvocationMapEntry(final Map.Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> object) {
			return ExecutionAdapterFactory.this.createDeployedOperationsPairToInvocationMapEntryAdapter();
		}

		@Override
		public Adapter caseInvocation(final Invocation object) {
			return ExecutionAdapterFactory.this.createInvocationAdapter();
		}

		@Override
		public Adapter caseStorageDataflow(final StorageDataflow object) {
			return ExecutionAdapterFactory.this.createStorageDataflowAdapter();
		}

		@Override
		public Adapter caseDeployedOperationsPairToDeployedStorageMapEntry(final Map.Entry<Tuple<DeployedOperation, DeployedStorage>, StorageDataflow> object) {
			return ExecutionAdapterFactory.this.createDeployedOperationsPairToDeployedStorageMapEntryAdapter();
		}

		@Override
		public <F, S> Adapter caseTuple(final Tuple<F, S> object) {
			return ExecutionAdapterFactory.this.createTupleAdapter();
		}

		@Override
		public Adapter caseOperationDataflow(final OperationDataflow object) {
			return ExecutionAdapterFactory.this.createOperationDataflowAdapter();
		}

		@Override
		public Adapter caseDeployedOperationsPairToDeployedOperationsMapEntry(
				final Map.Entry<Tuple<DeployedOperation, DeployedOperation>, OperationDataflow> object) {
			return ExecutionAdapterFactory.this.createDeployedOperationsPairToDeployedOperationsMapEntryAdapter();
		}

		@Override
		public Adapter defaultCase(final EObject object) {
			return ExecutionAdapterFactory.this.createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(final Notifier target) {
		return this.modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.execution.ExecutionModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.execution.ExecutionModel
	 * @generated
	 */
	public Adapter createExecutionModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Invocation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createDeployedOperationsPairToInvocationMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.execution.Invocation <em>Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.execution.Invocation
	 * @generated
	 */
	public Adapter createInvocationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.execution.StorageDataflow <em>Storage Dataflow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.execution.StorageDataflow
	 * @generated
	 */
	public Adapter createStorageDataflowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Deployed Storage Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createDeployedOperationsPairToDeployedStorageMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.execution.Tuple <em>Tuple</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.execution.Tuple
	 * @generated
	 */
	public Adapter createTupleAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.execution.OperationDataflow <em>Operation Dataflow</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.execution.OperationDataflow
	 * @generated
	 */
	public Adapter createOperationDataflowAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Deployed Operations Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createDeployedOperationsPairToDeployedOperationsMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // ExecutionAdapterFactory
