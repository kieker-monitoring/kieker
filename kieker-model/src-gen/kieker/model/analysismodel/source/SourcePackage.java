/**
 */
package kieker.model.analysismodel.source;

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
 * @see kieker.model.analysismodel.source.SourceFactory
 * @model kind="package"
 * @generated
 */
public interface SourcePackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "source";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/source";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "source";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	SourcePackage eINSTANCE = kieker.model.analysismodel.source.impl.SourcePackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.source.impl.SourceModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.source.impl.SourceModelImpl
	 * @see kieker.model.analysismodel.source.impl.SourcePackageImpl#getSourceModel()
	 * @generated
	 */
	int SOURCE_MODEL = 0;

	/**
	 * The feature id for the '<em><b>Sources</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_MODEL__SOURCES = 0;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_MODEL_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.source.impl.EObjectToSourceEntryImpl <em>EObject To Source Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.source.impl.EObjectToSourceEntryImpl
	 * @see kieker.model.analysismodel.source.impl.SourcePackageImpl#getEObjectToSourceEntry()
	 * @generated
	 */
	int EOBJECT_TO_SOURCE_ENTRY = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_SOURCE_ENTRY__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_SOURCE_ENTRY__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EObject To Source Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_SOURCE_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EObject To Source Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_SOURCE_ENTRY_OPERATION_COUNT = 0;


	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.source.SourceModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see kieker.model.analysismodel.source.SourceModel
	 * @generated
	 */
	EClass getSourceModel();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.source.SourceModel#getSources <em>Sources</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Sources</em>'.
	 * @see kieker.model.analysismodel.source.SourceModel#getSources()
	 * @see #getSourceModel()
	 * @generated
	 */
	EReference getSourceModel_Sources();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EObject To Source Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EObject To Source Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model keyType="org.eclipse.emf.ecore.EObject"
	 *        valueDataType="org.eclipse.emf.ecore.EString" valueMany="true"
	 * @generated
	 */
	EClass getEObjectToSourceEntry();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEObjectToSourceEntry()
	 * @generated
	 */
	EReference getEObjectToSourceEntry_Key();

	/**
	 * Returns the meta object for the attribute list '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute list '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEObjectToSourceEntry()
	 * @generated
	 */
	EAttribute getEObjectToSourceEntry_Value();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	SourceFactory getSourceFactory();

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
		 * The meta object literal for the '{@link kieker.model.analysismodel.source.impl.SourceModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.source.impl.SourceModelImpl
		 * @see kieker.model.analysismodel.source.impl.SourcePackageImpl#getSourceModel()
		 * @generated
		 */
		EClass SOURCE_MODEL = eINSTANCE.getSourceModel();

		/**
		 * The meta object literal for the '<em><b>Sources</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SOURCE_MODEL__SOURCES = eINSTANCE.getSourceModel_Sources();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.source.impl.EObjectToSourceEntryImpl <em>EObject To Source Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.source.impl.EObjectToSourceEntryImpl
		 * @see kieker.model.analysismodel.source.impl.SourcePackageImpl#getEObjectToSourceEntry()
		 * @generated
		 */
		EClass EOBJECT_TO_SOURCE_ENTRY = eINSTANCE.getEObjectToSourceEntry();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EOBJECT_TO_SOURCE_ENTRY__KEY = eINSTANCE.getEObjectToSourceEntry_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EOBJECT_TO_SOURCE_ENTRY__VALUE = eINSTANCE.getEObjectToSourceEntry_Value();

	}

} //SourcePackage
