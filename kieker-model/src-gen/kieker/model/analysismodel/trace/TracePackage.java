/**
 */
package kieker.model.analysismodel.trace;

import org.eclipse.emf.ecore.EAttribute;
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
 * @see kieker.model.analysismodel.trace.TraceFactory
 * @model kind="package"
 * @generated
 */
public interface TracePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "trace";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/trace";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "trace";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	TracePackage eINSTANCE = kieker.model.analysismodel.trace.impl.TracePackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.trace.impl.TraceImpl <em>Trace</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.trace.impl.TraceImpl
	 * @see kieker.model.analysismodel.trace.impl.TracePackageImpl#getTrace()
	 * @generated
	 */
	int TRACE = 0;

	/**
	 * The feature id for the '<em><b>Trace ID</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__TRACE_ID = 0;

	/**
	 * The feature id for the '<em><b>Root Operation Call</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE__ROOT_OPERATION_CALL = 1;

	/**
	 * The number of structural features of the '<em>Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Trace</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.trace.impl.OperationCallImpl <em>Operation Call</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.trace.impl.OperationCallImpl
	 * @see kieker.model.analysismodel.trace.impl.TracePackageImpl#getOperationCall()
	 * @generated
	 */
	int OPERATION_CALL = 1;

	/**
	 * The feature id for the '<em><b>Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__OPERATION = 0;

	/**
	 * The feature id for the '<em><b>Parent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__PARENT = 1;

	/**
	 * The feature id for the '<em><b>Children</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__CHILDREN = 2;

	/**
	 * The feature id for the '<em><b>Duration</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__DURATION = 3;

	/**
	 * The feature id for the '<em><b>Start</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__START = 4;

	/**
	 * The feature id for the '<em><b>Dur Ratio To Parent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__DUR_RATIO_TO_PARENT = 5;

	/**
	 * The feature id for the '<em><b>Dur Ratio To Root Parent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT = 6;

	/**
	 * The feature id for the '<em><b>Stack Depth</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__STACK_DEPTH = 7;

	/**
	 * The feature id for the '<em><b>Order Index</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__ORDER_INDEX = 8;

	/**
	 * The feature id for the '<em><b>Failed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__FAILED = 9;

	/**
	 * The feature id for the '<em><b>Failed Cause</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL__FAILED_CAUSE = 10;

	/**
	 * The number of structural features of the '<em>Operation Call</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_FEATURE_COUNT = 11;

	/**
	 * The number of operations of the '<em>Operation Call</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.trace.Trace <em>Trace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace</em>'.
	 * @see kieker.model.analysismodel.trace.Trace
	 * @generated
	 */
	EClass getTrace();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.Trace#getTraceID <em>Trace ID</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Trace ID</em>'.
	 * @see kieker.model.analysismodel.trace.Trace#getTraceID()
	 * @see #getTrace()
	 * @generated
	 */
	EAttribute getTrace_TraceID();

	/**
	 * Returns the meta object for the containment reference '{@link kieker.model.analysismodel.trace.Trace#getRootOperationCall <em>Root Operation Call</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Root Operation Call</em>'.
	 * @see kieker.model.analysismodel.trace.Trace#getRootOperationCall()
	 * @see #getTrace()
	 * @generated
	 */
	EReference getTrace_RootOperationCall();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.trace.OperationCall <em>Operation Call</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Call</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall
	 * @generated
	 */
	EClass getOperationCall();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.trace.OperationCall#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Operation</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getOperation()
	 * @see #getOperationCall()
	 * @generated
	 */
	EReference getOperationCall_Operation();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.trace.OperationCall#getParent <em>Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Parent</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getParent()
	 * @see #getOperationCall()
	 * @generated
	 */
	EReference getOperationCall_Parent();

	/**
	 * Returns the meta object for the reference list '{@link kieker.model.analysismodel.trace.OperationCall#getChildren <em>Children</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Children</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getChildren()
	 * @see #getOperationCall()
	 * @generated
	 */
	EReference getOperationCall_Children();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#getDuration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Duration</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getDuration()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_Duration();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#getStart <em>Start</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Start</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getStart()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_Start();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#getDurRatioToParent <em>Dur Ratio To Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dur Ratio To Parent</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getDurRatioToParent()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_DurRatioToParent();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#getDurRatioToRootParent <em>Dur Ratio To Root Parent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dur Ratio To Root Parent</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getDurRatioToRootParent()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_DurRatioToRootParent();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#getStackDepth <em>Stack Depth</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Stack Depth</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getStackDepth()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_StackDepth();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#getOrderIndex <em>Order Index</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Order Index</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getOrderIndex()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_OrderIndex();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#isFailed <em>Failed</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Failed</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#isFailed()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_Failed();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.trace.OperationCall#getFailedCause <em>Failed Cause</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Failed Cause</em>'.
	 * @see kieker.model.analysismodel.trace.OperationCall#getFailedCause()
	 * @see #getOperationCall()
	 * @generated
	 */
	EAttribute getOperationCall_FailedCause();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	TraceFactory getTraceFactory();

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
		 * The meta object literal for the '{@link kieker.model.analysismodel.trace.impl.TraceImpl <em>Trace</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.trace.impl.TraceImpl
		 * @see kieker.model.analysismodel.trace.impl.TracePackageImpl#getTrace()
		 * @generated
		 */
		EClass TRACE = eINSTANCE.getTrace();

		/**
		 * The meta object literal for the '<em><b>Trace ID</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TRACE__TRACE_ID = eINSTANCE.getTrace_TraceID();

		/**
		 * The meta object literal for the '<em><b>Root Operation Call</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE__ROOT_OPERATION_CALL = eINSTANCE.getTrace_RootOperationCall();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.trace.impl.OperationCallImpl <em>Operation Call</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.trace.impl.OperationCallImpl
		 * @see kieker.model.analysismodel.trace.impl.TracePackageImpl#getOperationCall()
		 * @generated
		 */
		EClass OPERATION_CALL = eINSTANCE.getOperationCall();

		/**
		 * The meta object literal for the '<em><b>Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_CALL__OPERATION = eINSTANCE.getOperationCall_Operation();

		/**
		 * The meta object literal for the '<em><b>Parent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_CALL__PARENT = eINSTANCE.getOperationCall_Parent();

		/**
		 * The meta object literal for the '<em><b>Children</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_CALL__CHILDREN = eINSTANCE.getOperationCall_Children();

		/**
		 * The meta object literal for the '<em><b>Duration</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__DURATION = eINSTANCE.getOperationCall_Duration();

		/**
		 * The meta object literal for the '<em><b>Start</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__START = eINSTANCE.getOperationCall_Start();

		/**
		 * The meta object literal for the '<em><b>Dur Ratio To Parent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__DUR_RATIO_TO_PARENT = eINSTANCE.getOperationCall_DurRatioToParent();

		/**
		 * The meta object literal for the '<em><b>Dur Ratio To Root Parent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT = eINSTANCE.getOperationCall_DurRatioToRootParent();

		/**
		 * The meta object literal for the '<em><b>Stack Depth</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__STACK_DEPTH = eINSTANCE.getOperationCall_StackDepth();

		/**
		 * The meta object literal for the '<em><b>Order Index</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__ORDER_INDEX = eINSTANCE.getOperationCall_OrderIndex();

		/**
		 * The meta object literal for the '<em><b>Failed</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__FAILED = eINSTANCE.getOperationCall_Failed();

		/**
		 * The meta object literal for the '<em><b>Failed Cause</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL__FAILED_CAUSE = eINSTANCE.getOperationCall_FailedCause();

	}

} //TracePackage
