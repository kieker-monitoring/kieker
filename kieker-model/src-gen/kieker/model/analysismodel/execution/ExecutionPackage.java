/**
 */
package kieker.model.analysismodel.execution;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
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
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_MODEL_FEATURE_COUNT = 1;

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
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__VALUE = 1;

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
	 * The number of structural features of the '<em>Aggregated Storage Access</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Aggregated Storage Access</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_STORAGE_ACCESS_OPERATION_COUNT = 0;

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
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="kieker.model.analysismodel.ComposedKey&lt;kieker.model.analysismodel.deployment.DeployedOperation, kieker.model.analysismodel.deployment.DeployedOperation&gt;"
	 *        valueType="kieker.model.analysismodel.execution.AggregatedInvocation" valueContainment="true"
	 * @generated
	 */
	EClass getDeployedOperationsPairToAggregatedInvocationMapEntry();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getDeployedOperationsPairToAggregatedInvocationMapEntry()
	 * @generated
	 */
	EAttribute getDeployedOperationsPairToAggregatedInvocationMapEntry_Key();

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
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
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
		 * The meta object literal for the '{@link kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedInvocationMapEntryImpl <em>Deployed Operations Pair To Aggregated Invocation Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.execution.impl.DeployedOperationsPairToAggregatedInvocationMapEntryImpl
		 * @see kieker.model.analysismodel.execution.impl.ExecutionPackageImpl#getDeployedOperationsPairToAggregatedInvocationMapEntry()
		 * @generated
		 */
		EClass DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY = eINSTANCE.getDeployedOperationsPairToAggregatedInvocationMapEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__KEY = eINSTANCE.getDeployedOperationsPairToAggregatedInvocationMapEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference DEPLOYED_OPERATIONS_PAIR_TO_AGGREGATED_INVOCATION_MAP_ENTRY__VALUE = eINSTANCE.getDeployedOperationsPairToAggregatedInvocationMapEntry_Value();

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

	}

} // ExecutionPackage
