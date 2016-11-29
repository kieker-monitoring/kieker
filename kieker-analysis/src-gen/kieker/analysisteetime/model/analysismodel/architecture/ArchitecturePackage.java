/**
 */
package kieker.analysisteetime.model.analysismodel.architecture;

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
 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitectureFactory
 * @model kind="package"
 * @generated
 */
public interface ArchitecturePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "architecture";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/architecture";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "architecture";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ArchitecturePackage eINSTANCE = kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitectureRootImpl <em>Root</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitectureRootImpl
	 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl#getArchitectureRoot()
	 * @generated
	 */
	int ARCHITECTURE_ROOT = 0;

	/**
	 * The feature id for the '<em><b>Component Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURE_ROOT__COMPONENT_TYPES = 0;

	/**
	 * The number of structural features of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURE_ROOT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Root</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ARCHITECTURE_ROOT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl <em>Component Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl
	 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl#getComponentType()
	 * @generated
	 */
	int COMPONENT_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__SIGNATURE = 0;

	/**
	 * The feature id for the '<em><b>Architecture Root</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__ARCHITECTURE_ROOT = 1;

	/**
	 * The feature id for the '<em><b>Provided Operations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE__PROVIDED_OPERATIONS = 2;

	/**
	 * The number of structural features of the '<em>Component Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Component Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPONENT_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.architecture.impl.OperationTypeImpl <em>Operation Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.OperationTypeImpl
	 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl#getOperationType()
	 * @generated
	 */
	int OPERATION_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Signature</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE__SIGNATURE = 0;

	/**
	 * The feature id for the '<em><b>Component Type</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE__COMPONENT_TYPE = 1;

	/**
	 * The number of structural features of the '<em>Operation Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Operation Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_TYPE_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot <em>Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Root</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot
	 * @generated
	 */
	EClass getArchitectureRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot#getComponentTypes <em>Component Types</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Component Types</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ArchitectureRoot#getComponentTypes()
	 * @see #getArchitectureRoot()
	 * @generated
	 */
	EReference getArchitectureRoot_ComponentTypes();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Component Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ComponentType
	 * @generated
	 */
	EClass getComponentType();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getSignature()
	 * @see #getComponentType()
	 * @generated
	 */
	EAttribute getComponentType_Signature();

	/**
	 * Returns the meta object for the container reference '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getArchitectureRoot <em>Architecture Root</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Architecture Root</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getArchitectureRoot()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_ArchitectureRoot();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getProvidedOperations <em>Provided Operations</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Provided Operations</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.ComponentType#getProvidedOperations()
	 * @see #getComponentType()
	 * @generated
	 */
	EReference getComponentType_ProvidedOperations();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.OperationType
	 * @generated
	 */
	EClass getOperationType();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getSignature <em>Signature</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.OperationType#getSignature()
	 * @see #getOperationType()
	 * @generated
	 */
	EAttribute getOperationType_Signature();

	/**
	 * Returns the meta object for the container reference '{@link kieker.analysisteetime.model.analysismodel.architecture.OperationType#getComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Component Type</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.architecture.OperationType#getComponentType()
	 * @see #getOperationType()
	 * @generated
	 */
	EReference getOperationType_ComponentType();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ArchitectureFactory getArchitectureFactory();

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
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitectureRootImpl <em>Root</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitectureRootImpl
		 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl#getArchitectureRoot()
		 * @generated
		 */
		EClass ARCHITECTURE_ROOT = eINSTANCE.getArchitectureRoot();

		/**
		 * The meta object literal for the '<em><b>Component Types</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ARCHITECTURE_ROOT__COMPONENT_TYPES = eINSTANCE.getArchitectureRoot_ComponentTypes();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl <em>Component Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ComponentTypeImpl
		 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl#getComponentType()
		 * @generated
		 */
		EClass COMPONENT_TYPE = eINSTANCE.getComponentType();

		/**
		 * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPONENT_TYPE__SIGNATURE = eINSTANCE.getComponentType_Signature();

		/**
		 * The meta object literal for the '<em><b>Architecture Root</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_TYPE__ARCHITECTURE_ROOT = eINSTANCE.getComponentType_ArchitectureRoot();

		/**
		 * The meta object literal for the '<em><b>Provided Operations</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPONENT_TYPE__PROVIDED_OPERATIONS = eINSTANCE.getComponentType_ProvidedOperations();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.architecture.impl.OperationTypeImpl <em>Operation Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.OperationTypeImpl
		 * @see kieker.analysisteetime.model.analysismodel.architecture.impl.ArchitecturePackageImpl#getOperationType()
		 * @generated
		 */
		EClass OPERATION_TYPE = eINSTANCE.getOperationType();

		/**
		 * The meta object literal for the '<em><b>Signature</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_TYPE__SIGNATURE = eINSTANCE.getOperationType_Signature();

		/**
		 * The meta object literal for the '<em><b>Component Type</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_TYPE__COMPONENT_TYPE = eINSTANCE.getOperationType_ComponentType();

	}

} //ArchitecturePackage
