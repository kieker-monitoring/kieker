/**
 */
package kieker.analysisteetime.model.analysismodel.execution;

import org.eclipse.emf.ecore.EClass;
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
 * @see kieker.analysisteetime.model.analysismodel.execution.ExecutionFactory
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
	ExecutionPackage eINSTANCE = kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionRootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionRootImpl
	 * @see kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionPackageImpl#getExecutionRoot()
	 * @generated
	 */
	int EXECUTION_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Aggregated Invocations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_ROOT__AGGREGATED_INVOCATIONS = 0;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EXECUTION_ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.execution.impl.AggregatedInvocationImpl <em>Aggregated Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.execution.impl.AggregatedInvocationImpl
	 * @see kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionPackageImpl#getAggregatedInvocation()
	 * @generated
	 */
	int AGGREGATED_INVOCATION = 1;

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
	 * The feature id for the '<em><b>Execution Root</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int AGGREGATED_INVOCATION__EXECUTION_ROOT = 2;

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
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot
	 * @generated
	 */
	EClass getExecutionRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot#getAggregatedInvocations <em>Aggregated Invocations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Aggregated Invocations</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.execution.ExecutionRoot#getAggregatedInvocations()
	 * @see #getExecutionRoot()
	 * @generated
	 */
	EReference getExecutionRoot_AggregatedInvocations();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation <em>Aggregated Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Aggregated Invocation</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation
	 * @generated
	 */
	EClass getAggregatedInvocation();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Source</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getSource()
	 * @see #getAggregatedInvocation()
	 * @generated
	 */
	EReference getAggregatedInvocation_Source();

	/**
	 * Returns the meta object for the reference '{@link kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Target</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getTarget()
	 * @see #getAggregatedInvocation()
	 * @generated
	 */
	EReference getAggregatedInvocation_Target();

	/**
	 * Returns the meta object for the container reference '{@link kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getExecutionRoot <em>Execution Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Execution Root</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation#getExecutionRoot()
	 * @see #getAggregatedInvocation()
	 * @generated
	 */
	EReference getAggregatedInvocation_ExecutionRoot();

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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionRootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionRootImpl
		 * @see kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionPackageImpl#getExecutionRoot()
		 * @generated
		 */
		EClass EXECUTION_ROOT = eINSTANCE.getExecutionRoot();

		/**
		 * The meta object literal for the '<em><b>Aggregated Invocations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EXECUTION_ROOT__AGGREGATED_INVOCATIONS = eINSTANCE.getExecutionRoot_AggregatedInvocations();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.execution.impl.AggregatedInvocationImpl <em>Aggregated Invocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.execution.impl.AggregatedInvocationImpl
		 * @see kieker.analysisteetime.model.analysismodel.execution.impl.ExecutionPackageImpl#getAggregatedInvocation()
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
		 * The meta object literal for the '<em><b>Execution Root</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference AGGREGATED_INVOCATION__EXECUTION_ROOT = eINSTANCE.getAggregatedInvocation_ExecutionRoot();

	}

} //ExecutionPackage
