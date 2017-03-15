/**
 */
package kieker.analysisteetime.model.analysismodel;

import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EPackage;

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
 * @see kieker.analysisteetime.model.analysismodel.AnalysismodelFactory
 * @model kind="package"
 * @generated
 */
public interface AnalysismodelPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "analysismodel";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "analysismodel";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	AnalysismodelPackage eINSTANCE = kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl.init();

	/**
	 * The meta object id for the '<em>Instant</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.time.Instant
	 * @see kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl#getInstant()
	 * @generated
	 */
	int INSTANT = 0;

	/**
	 * The meta object id for the '<em>Duration</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see java.time.Duration
	 * @see kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl#getDuration()
	 * @generated
	 */
	int DURATION = 1;


	/**
	 * The meta object id for the '<em>Composed Key</em>' data type.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.analysisteetime.util.ComposedKey
	 * @see kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl#getComposedKey()
	 * @generated
	 */
	int COMPOSED_KEY = 2;

	/**
	 * Returns the meta object for data type '{@link java.time.Instant <em>Instant</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Instant</em>'.
	 * @see java.time.Instant
	 * @model instanceClass="java.time.Instant"
	 * @generated
	 */
	EDataType getInstant();

	/**
	 * Returns the meta object for data type '{@link java.time.Duration <em>Duration</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Duration</em>'.
	 * @see java.time.Duration
	 * @model instanceClass="java.time.Duration"
	 * @generated
	 */
	EDataType getDuration();

	/**
	 * Returns the meta object for data type '{@link kieker.analysisteetime.util.ComposedKey <em>Composed Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for data type '<em>Composed Key</em>'.
	 * @see kieker.analysisteetime.util.ComposedKey
	 * @model instanceClass="kieker.analysisteetime.util.ComposedKey" typeParameters="F S"
	 * @generated
	 */
	EDataType getComposedKey();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	AnalysismodelFactory getAnalysismodelFactory();

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
		 * The meta object literal for the '<em>Instant</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.time.Instant
		 * @see kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl#getInstant()
		 * @generated
		 */
		EDataType INSTANT = eINSTANCE.getInstant();

		/**
		 * The meta object literal for the '<em>Duration</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see java.time.Duration
		 * @see kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl#getDuration()
		 * @generated
		 */
		EDataType DURATION = eINSTANCE.getDuration();

		/**
		 * The meta object literal for the '<em>Composed Key</em>' data type.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.analysisteetime.util.ComposedKey
		 * @see kieker.analysisteetime.model.analysismodel.impl.AnalysismodelPackageImpl#getComposedKey()
		 * @generated
		 */
		EDataType COMPOSED_KEY = eINSTANCE.getComposedKey();

	}

} //AnalysismodelPackage
