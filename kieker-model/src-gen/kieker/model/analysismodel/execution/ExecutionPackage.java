/**
 */
package kieker.model.analysismodel.execution;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.execution.ExecutionFactory
 * @model kind="package"
 * @generated
 */
public interface ExecutionPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "execution";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/execution";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "execution";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ExecutionPackage eINSTANCE = kieker.model.analysismodel.execution.impl.ExecutionPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.ExecutionModelImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getExecutionModel()
	 * @generated
	 */
	int EXECUTION_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Invocations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL__INVOCATIONS = 0;

	/**
	 * The feature id for the '<em><b>Storage Dataflow</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL__STORAGE_DATAFLOW = 1;

	/**
	 * The feature id for the '<em><b>Operation Dataflow</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL__OPERATION_DATAFLOW = 2;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToInvocationMapEntryImpl <em>Deployed Operations Pair To Invocation Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToInvocationMapEntryImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToInvocationMapEntry()
	 * @generated
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY = 1;

	/**
	 * The number of structural features of the '<em>Deployed Operations Pair To Invocation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Deployed Operations Pair To Invocation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.InvocationImpl <em>Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.InvocationImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getInvocation()
	 * @generated
	 */
	int INVOCATION = 2;

	/**
	 * The feature id for the '<em><b>Caller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCATION__CALLER = 0;

	/**
	 * The feature id for the '<em><b>Callee</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCATION__CALLEE = 1;

	/**
	 * The number of structural features of the '<em>Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCATION_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVOCATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.StorageDataflowImpl <em>Storage Dataflow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.StorageDataflowImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getStorageDataflow()
	 * @generated
	 */
	int STORAGE_DATAFLOW = 3;

	/**
	 * The feature id for the '<em><b>Storage</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_DATAFLOW__STORAGE = 0;

	/**
	 * The feature id for the '<em><b>Code</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_DATAFLOW__CODE = 1;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_DATAFLOW__DIRECTION = 2;

	/**
	 * The number of structural features of the '<em>Storage Dataflow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_DATAFLOW_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Storage Dataflow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STORAGE_DATAFLOW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedStorageMapEntryImpl <em>Deployed Operations Pair To Deployed Storage Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedStorageMapEntryImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToDeployedStorageMapEntry()
	 * @generated
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY = 4;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY__KEY = 1;

	/**
	 * The number of structural features of the '<em>Deployed Operations Pair To Deployed Storage Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Deployed Operations Pair To Deployed Storage Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.TupleImpl <em>Tuple</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.TupleImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getTuple()
	 * @generated
	 */
	int TUPLE = 5;

	/**
	 * The feature id for the '<em><b>First</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE__FIRST = 0;

	/**
	 * The feature id for the '<em><b>Second</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE__SECOND = 1;

	/**
	 * The number of structural features of the '<em>Tuple</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_FEATURE_COUNT = 2;

	/**
	 * The operation id for the '<em>Equals</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE___EQUALS__OBJECT = 0;

	/**
	 * The operation id for the '<em>Hash Code</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE___HASH_CODE = 1;

	/**
	 * The number of operations of the '<em>Tuple</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_OPERATION_COUNT = 2;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.OperationDataflowImpl <em>Operation Dataflow</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.OperationDataflowImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getOperationDataflow()
	 * @generated
	 */
	int OPERATION_DATAFLOW = 6;

	/**
	 * The feature id for the '<em><b>Caller</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_DATAFLOW__CALLER = 0;

	/**
	 * The feature id for the '<em><b>Callee</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_DATAFLOW__CALLEE = 1;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_DATAFLOW__DIRECTION = 2;

	/**
	 * The number of structural features of the '<em>Operation Dataflow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_DATAFLOW_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Operation Dataflow</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_DATAFLOW_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedOperationsMapEntryImpl <em>Deployed Operations Pair To Deployed Operations Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedOperationsMapEntryImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToDeployedOperationsMapEntry()
	 * @generated
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY = 7;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY__KEY = 1;

	/**
	 * The number of structural features of the '<em>Deployed Operations Pair To Deployed Operations Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Deployed Operations Pair To Deployed Operations Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.EDirection <em>EDirection</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.EDirection
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getEDirection()
	 * @generated
	 */
	int EDIRECTION = 8;


	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.execution.ExecutionModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see kieker.model.analysismodel.execution.ExecutionModel
	 * @generated
	 */
	EClass getExecutionModel();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.execution.ExecutionModel#getInvocations <em>Invocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Invocations</em>'.
	 * @see kieker.model.analysismodel.execution.ExecutionModel#getInvocations()
	 * @see #getExecutionModel()
	 * @generated
	 */
	EReference getExecutionModel_Invocations();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.execution.ExecutionModel#getStorageDataflow <em>Storage Dataflow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Storage Dataflow</em>'.
	 * @see kieker.model.analysismodel.execution.ExecutionModel#getStorageDataflow()
	 * @see #getExecutionModel()
	 * @generated
	 */
	EReference getExecutionModel_StorageDataflow();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.execution.ExecutionModel#getOperationDataflow <em>Operation Dataflow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Operation Dataflow</em>'.
	 * @see kieker.model.analysismodel.execution.ExecutionModel#getOperationDataflow()
	 * @see #getExecutionModel()
	 * @generated
	 */
	EReference getExecutionModel_OperationDataflow();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Invocation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Operations Pair To Invocation Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="kieker.model.analysismodel.execution.Invocation" valueContainment="true"
	 *        keyType="kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;" keyContainment="true"
	 * @generated
	 */
	EClass getDeployedOperationsPairToInvocationMapEntry();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToInvocationMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToInvocationMapEntry_Value();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToInvocationMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToInvocationMapEntry_Key();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.execution.Invocation <em>Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Invocation</em>'.
	 * @see kieker.model.analysismodel.execution.Invocation
	 * @generated
	 */
	EClass getInvocation();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.Invocation#getCaller <em>Caller</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Caller</em>'.
	 * @see kieker.model.analysismodel.execution.Invocation#getCaller()
	 * @see #getInvocation()
	 * @generated
	 */
	EReference getInvocation_Caller();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.Invocation#getCallee <em>Callee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Callee</em>'.
	 * @see kieker.model.analysismodel.execution.Invocation#getCallee()
	 * @see #getInvocation()
	 * @generated
	 */
	EReference getInvocation_Callee();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.execution.StorageDataflow <em>Storage Dataflow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Storage Dataflow</em>'.
	 * @see kieker.model.analysismodel.execution.StorageDataflow
	 * @generated
	 */
	EClass getStorageDataflow();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.StorageDataflow#getStorage <em>Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Storage</em>'.
	 * @see kieker.model.analysismodel.execution.StorageDataflow#getStorage()
	 * @see #getStorageDataflow()
	 * @generated
	 */
	EReference getStorageDataflow_Storage();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.StorageDataflow#getCode <em>Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Code</em>'.
	 * @see kieker.model.analysismodel.execution.StorageDataflow#getCode()
	 * @see #getStorageDataflow()
	 * @generated
	 */
	EReference getStorageDataflow_Code();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.execution.StorageDataflow#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see kieker.model.analysismodel.execution.StorageDataflow#getDirection()
	 * @see #getStorageDataflow()
	 * @generated
	 */
	EAttribute getStorageDataflow_Direction();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Deployed Storage Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Operations Pair To Deployed Storage Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="kieker.model.analysismodel.execution.StorageDataflow" valueContainment="true"
	 *        keyType="kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedStorage&gt;" keyContainment="true"
	 * @generated
	 */
	EClass getDeployedOperationsPairToDeployedStorageMapEntry();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToDeployedStorageMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToDeployedStorageMapEntry_Value();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToDeployedStorageMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToDeployedStorageMapEntry_Key();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.execution.Tuple <em>Tuple</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tuple</em>'.
	 * @see kieker.model.analysismodel.execution.Tuple
	 * @generated
	 */
	EClass getTuple();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.Tuple#getFirst <em>First</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>First</em>'.
	 * @see kieker.model.analysismodel.execution.Tuple#getFirst()
	 * @see #getTuple()
	 * @generated
	 */
	EReference getTuple_First();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.Tuple#getSecond <em>Second</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Second</em>'.
	 * @see kieker.model.analysismodel.execution.Tuple#getSecond()
	 * @see #getTuple()
	 * @generated
	 */
	EReference getTuple_Second();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.execution.Tuple#equals(java.lang.Object) <em>Equals</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Equals</em>' operation.
	 * @see kieker.model.analysismodel.execution.Tuple#equals(java.lang.Object)
	 * @generated
	 */
	EOperation getTuple__Equals__Object();

	/**
	 * Returns the meta object for the '{@link kieker.model.analysismodel.execution.Tuple#hashCode() <em>Hash Code</em>}' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the '<em>Hash Code</em>' operation.
	 * @see kieker.model.analysismodel.execution.Tuple#hashCode()
	 * @generated
	 */
	EOperation getTuple__HashCode();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.execution.OperationDataflow <em>Operation Dataflow</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Dataflow</em>'.
	 * @see kieker.model.analysismodel.execution.OperationDataflow
	 * @generated
	 */
	EClass getOperationDataflow();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.OperationDataflow#getCaller <em>Caller</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Caller</em>'.
	 * @see kieker.model.analysismodel.execution.OperationDataflow#getCaller()
	 * @see #getOperationDataflow()
	 * @generated
	 */
	EReference getOperationDataflow_Caller();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.OperationDataflow#getCallee <em>Callee</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Callee</em>'.
	 * @see kieker.model.analysismodel.execution.OperationDataflow#getCallee()
	 * @see #getOperationDataflow()
	 * @generated
	 */
	EReference getOperationDataflow_Callee();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.execution.OperationDataflow#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see kieker.model.analysismodel.execution.OperationDataflow#getDirection()
	 * @see #getOperationDataflow()
	 * @generated
	 */
	EAttribute getOperationDataflow_Direction();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Deployed Operations Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Operations Pair To Deployed Operations Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="kieker.model.analysismodel.execution.OperationDataflow" valueContainment="true"
	 *        keyType="kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;" keyContainment="true"
	 * @generated
	 */
	EClass getDeployedOperationsPairToDeployedOperationsMapEntry();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToDeployedOperationsMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToDeployedOperationsMapEntry_Value();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToDeployedOperationsMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToDeployedOperationsMapEntry_Key();

	/**
	 * Returns the meta object for enum '{@link kieker.model.analysismodel.execution.EDirection <em>EDirection</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>EDirection</em>'.
	 * @see kieker.model.analysismodel.execution.EDirection
	 * @generated
	 */
	EEnum getEDirection();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ExecutionFactory getExecutionFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.ExecutionModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.ExecutionModelImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getExecutionModel()
		 * @generated
		 */
		EClass EXECUTION_MODEL = eINSTANCE.getExecutionModel();

		/**
		 * The meta object literal for the '<em><b>Invocations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION_MODEL__INVOCATIONS = eINSTANCE.getExecutionModel_Invocations();

		/**
		 * The meta object literal for the '<em><b>Storage Dataflow</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION_MODEL__STORAGE_DATAFLOW = eINSTANCE.getExecutionModel_StorageDataflow();

		/**
		 * The meta object literal for the '<em><b>Operation Dataflow</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION_MODEL__OPERATION_DATAFLOW = eINSTANCE.getExecutionModel_OperationDataflow();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToInvocationMapEntryImpl <em>Deployed Operations Pair To Invocation Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToInvocationMapEntryImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToInvocationMapEntry()
		 * @generated
		 */
		EClass DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY = eINSTANCE.getDeployedOperationsPairToInvocationMapEntry();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__VALUE = eINSTANCE.getDeployedOperationsPairToInvocationMapEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_INVOCATION_MAP_ENTRY__KEY = eINSTANCE.getDeployedOperationsPairToInvocationMapEntry_Key();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.InvocationImpl <em>Invocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.InvocationImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getInvocation()
		 * @generated
		 */
		EClass INVOCATION = eINSTANCE.getInvocation();

		/**
		 * The meta object literal for the '<em><b>Caller</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVOCATION__CALLER = eINSTANCE.getInvocation_Caller();

		/**
		 * The meta object literal for the '<em><b>Callee</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INVOCATION__CALLEE = eINSTANCE.getInvocation_Callee();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.StorageDataflowImpl <em>Storage Dataflow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.StorageDataflowImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getStorageDataflow()
		 * @generated
		 */
		EClass STORAGE_DATAFLOW = eINSTANCE.getStorageDataflow();

		/**
		 * The meta object literal for the '<em><b>Storage</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STORAGE_DATAFLOW__STORAGE = eINSTANCE.getStorageDataflow_Storage();

		/**
		 * The meta object literal for the '<em><b>Code</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STORAGE_DATAFLOW__CODE = eINSTANCE.getStorageDataflow_Code();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STORAGE_DATAFLOW__DIRECTION = eINSTANCE.getStorageDataflow_Direction();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedStorageMapEntryImpl <em>Deployed Operations Pair To Deployed Storage Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedStorageMapEntryImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToDeployedStorageMapEntry()
		 * @generated
		 */
		EClass DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY = eINSTANCE.getDeployedOperationsPairToDeployedStorageMapEntry();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY__VALUE = eINSTANCE.getDeployedOperationsPairToDeployedStorageMapEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_STORAGE_MAP_ENTRY__KEY = eINSTANCE.getDeployedOperationsPairToDeployedStorageMapEntry_Key();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.TupleImpl <em>Tuple</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.TupleImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getTuple()
		 * @generated
		 */
		EClass TUPLE = eINSTANCE.getTuple();

		/**
		 * The meta object literal for the '<em><b>First</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE__FIRST = eINSTANCE.getTuple_First();

		/**
		 * The meta object literal for the '<em><b>Second</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE__SECOND = eINSTANCE.getTuple_Second();

		/**
		 * The meta object literal for the '<em><b>Equals</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TUPLE___EQUALS__OBJECT = eINSTANCE.getTuple__Equals__Object();

		/**
		 * The meta object literal for the '<em><b>Hash Code</b></em>' operation.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EOperation TUPLE___HASH_CODE = eINSTANCE.getTuple__HashCode();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.OperationDataflowImpl <em>Operation Dataflow</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.OperationDataflowImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getOperationDataflow()
		 * @generated
		 */
		EClass OPERATION_DATAFLOW = eINSTANCE.getOperationDataflow();

		/**
		 * The meta object literal for the '<em><b>Caller</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_DATAFLOW__CALLER = eINSTANCE.getOperationDataflow_Caller();

		/**
		 * The meta object literal for the '<em><b>Callee</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_DATAFLOW__CALLEE = eINSTANCE.getOperationDataflow_Callee();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_DATAFLOW__DIRECTION = eINSTANCE.getOperationDataflow_Direction();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedOperationsMapEntryImpl <em>Deployed Operations Pair To Deployed Operations Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToDeployedOperationsMapEntryImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToDeployedOperationsMapEntry()
		 * @generated
		 */
		EClass DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY = eINSTANCE.getDeployedOperationsPairToDeployedOperationsMapEntry();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY__VALUE = eINSTANCE.getDeployedOperationsPairToDeployedOperationsMapEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_DEPLOYED_OPERATIONS_MAP_ENTRY__KEY = eINSTANCE.getDeployedOperationsPairToDeployedOperationsMapEntry_Key();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.EDirection <em>EDirection</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.EDirection
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getEDirection()
		 * @generated
		 */
		EEnum EDIRECTION = eINSTANCE.getEDirection();

	}

} //ExecutionPackage
