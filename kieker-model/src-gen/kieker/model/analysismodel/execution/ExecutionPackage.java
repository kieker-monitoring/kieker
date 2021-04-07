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
	 * The feature id for the '<em><b>Aggregated Invocations</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL__AGGREGATED_INVOCATIONS = 0;

	/**
	 * The feature id for the '<em><b>Aggregated Storage Accesses</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES = 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedInvocationMapEntryImpl <em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedInvocationMapEntryImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToAggregatedInvocationMapEntry()
	 * @generated
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__KEY = 1;

	/**
	 * The number of structural features of the '<em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.AggregatedInvocationImpl <em>Aggregated Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.AggregatedInvocationImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getAggregatedInvocation()
	 * @generated
	 */
	int AGGREGATED_INVOCATION = 2;

	/**
	 * The feature id for the '<em><b>Source</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_INVOCATION__SOURCE = 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_INVOCATION__TARGET = 1;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_INVOCATION__SOURCES = 2;

	/**
	 * The number of structural features of the '<em>Aggregated Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_INVOCATION_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Aggregated Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_INVOCATION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl <em>Aggregated Storage Access</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getAggregatedStorageAccess()
	 * @generated
	 */
	int AGGREGATED_STORAGE_ACCESS = 3;

	/**
	 * The feature id for the '<em><b>Storage</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS__STORAGE = 0;

	/**
	 * The feature id for the '<em><b>Code</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS__CODE = 1;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS__SOURCES = 2;

	/**
	 * The feature id for the '<em><b>Direction</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS__DIRECTION = 3;

	/**
	 * The number of structural features of the '<em>Aggregated Storage Access</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Aggregated Storage Access</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl <em>Deployed Operations Pair To Aggregated Storage Access Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToAggregatedStorageAccessMapEntry()
	 * @generated
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY = 4;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY = 1;

	/**
	 * The number of structural features of the '<em>Deployed Operations Pair To Aggregated Storage Access Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Deployed Operations Pair To Aggregated Storage Access Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY_OPERATION_COUNT = 0;

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
	 * The meta object id for the '{@link kieker.model.analysismodel.execution.EDirection <em>EDirection</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.execution.EDirection
	 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getEDirection()
	 * @generated
	 */
	int EDIRECTION = 6;


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
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.execution.ExecutionModel#getAggregatedInvocations <em>Aggregated Invocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Aggregated Invocations</em>'.
	 * @see kieker.model.analysismodel.execution.ExecutionModel#getAggregatedInvocations()
	 * @see #getExecutionModel()
	 * @generated
	 */
	EReference getExecutionModel_AggregatedInvocations();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.execution.ExecutionModel#getAggregatedStorageAccesses <em>Aggregated Storage Accesses</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Aggregated Storage Accesses</em>'.
	 * @see kieker.model.analysismodel.execution.ExecutionModel#getAggregatedStorageAccesses()
	 * @see #getExecutionModel()
	 * @generated
	 */
	EReference getExecutionModel_AggregatedStorageAccesses();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="kieker.model.analysismodel.execution.AggregatedInvocation" valueContainment="true"
	 *        keyType="kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;" keyContainment="true"
	 * @generated
	 */
	EClass getDeployedOperationsPairToAggregatedInvocationMapEntry();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToAggregatedInvocationMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToAggregatedInvocationMapEntry_Value();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToAggregatedInvocationMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToAggregatedInvocationMapEntry_Key();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.execution.AggregatedInvocation <em>Aggregated Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Aggregated Invocation</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedInvocation
	 * @generated
	 */
	EClass getAggregatedInvocation();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.AggregatedInvocation#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedInvocation#getSource()
	 * @see #getAggregatedInvocation()
	 * @generated
	 */
	EReference getAggregatedInvocation_Source();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.AggregatedInvocation#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedInvocation#getTarget()
	 * @see #getAggregatedInvocation()
	 * @generated
	 */
	EReference getAggregatedInvocation_Target();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.model.analysismodel.execution.AggregatedInvocation#getSources <em>Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Sources</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedInvocation#getSources()
	 * @see #getAggregatedInvocation()
	 * @generated
	 */
	EAttribute getAggregatedInvocation_Sources();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.execution.AggregatedStorageAccess <em>Aggregated Storage Access</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Aggregated Storage Access</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedStorageAccess
	 * @generated
	 */
	EClass getAggregatedStorageAccess();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getStorage <em>Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Storage</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedStorageAccess#getStorage()
	 * @see #getAggregatedStorageAccess()
	 * @generated
	 */
	EReference getAggregatedStorageAccess_Storage();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getCode <em>Code</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Code</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedStorageAccess#getCode()
	 * @see #getAggregatedStorageAccess()
	 * @generated
	 */
	EReference getAggregatedStorageAccess_Code();

	/**
	 * Returns the meta object for the attribute list '{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getSources <em>Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Sources</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedStorageAccess#getSources()
	 * @see #getAggregatedStorageAccess()
	 * @generated
	 */
	EAttribute getAggregatedStorageAccess_Sources();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.execution.AggregatedStorageAccess#getDirection <em>Direction</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Direction</em>'.
	 * @see kieker.model.analysismodel.execution.AggregatedStorageAccess#getDirection()
	 * @see #getAggregatedStorageAccess()
	 * @generated
	 */
	EAttribute getAggregatedStorageAccess_Direction();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Aggregated Storage Access Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Operations Pair To Aggregated Storage Access Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="kieker.model.analysismodel.execution.AggregatedStorageAccess" valueContainment="true"
	 *        keyType="kieker.model.analysismodel.execution.Tuple&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;" keyContainment="true"
	 * @generated
	 */
	EClass getDeployedOperationsPairToAggregatedStorageAccessMapEntry();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToAggregatedStorageAccessMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Value();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToAggregatedStorageAccessMapEntry()
	 * @generated
	 */
	EReference getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Key();

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
		 * The meta object literal for the '<em><b>Aggregated Invocations</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION_MODEL__AGGREGATED_INVOCATIONS = eINSTANCE.getExecutionModel_AggregatedInvocations();

		/**
		 * The meta object literal for the '<em><b>Aggregated Storage Accesses</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION_MODEL__AGGREGATED_STORAGE_ACCESSES = eINSTANCE.getExecutionModel_AggregatedStorageAccesses();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedInvocationMapEntryImpl <em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedInvocationMapEntryImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToAggregatedInvocationMapEntry()
		 * @generated
		 */
		EClass DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY = eINSTANCE.getDeployedOperationsPairToAggregatedInvocationMapEntry();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__VALUE = eINSTANCE.getDeployedOperationsPairToAggregatedInvocationMapEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__KEY = eINSTANCE.getDeployedOperationsPairToAggregatedInvocationMapEntry_Key();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.AggregatedInvocationImpl <em>Aggregated Invocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.AggregatedInvocationImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getAggregatedInvocation()
		 * @generated
		 */
		EClass AGGREGATED_INVOCATION = eINSTANCE.getAggregatedInvocation();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AGGREGATED_INVOCATION__SOURCE = eINSTANCE.getAggregatedInvocation_Source();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AGGREGATED_INVOCATION__TARGET = eINSTANCE.getAggregatedInvocation_Target();

		/**
		 * The meta object literal for the '<em><b>Sources</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AGGREGATED_INVOCATION__SOURCES = eINSTANCE.getAggregatedInvocation_Sources();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl <em>Aggregated Storage Access</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.AggregatedStorageAccessImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getAggregatedStorageAccess()
		 * @generated
		 */
		EClass AGGREGATED_STORAGE_ACCESS = eINSTANCE.getAggregatedStorageAccess();

		/**
		 * The meta object literal for the '<em><b>Storage</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AGGREGATED_STORAGE_ACCESS__STORAGE = eINSTANCE.getAggregatedStorageAccess_Storage();

		/**
		 * The meta object literal for the '<em><b>Code</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AGGREGATED_STORAGE_ACCESS__CODE = eINSTANCE.getAggregatedStorageAccess_Code();

		/**
		 * The meta object literal for the '<em><b>Sources</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AGGREGATED_STORAGE_ACCESS__SOURCES = eINSTANCE.getAggregatedStorageAccess_Sources();

		/**
		 * The meta object literal for the '<em><b>Direction</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute AGGREGATED_STORAGE_ACCESS__DIRECTION = eINSTANCE.getAggregatedStorageAccess_Direction();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl <em>Deployed Operations Pair To Aggregated Storage Access Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedStorageAccessMapEntryImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToAggregatedStorageAccessMapEntry()
		 * @generated
		 */
		EClass DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY = eINSTANCE.getDeployedOperationsPairToAggregatedStorageAccessMapEntry();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__VALUE = eINSTANCE.getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_STORAGE_ACCESS_MAP_ENTRY__KEY = eINSTANCE.getDeployedOperationsPairToAggregatedStorageAccessMapEntry_Key();

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
