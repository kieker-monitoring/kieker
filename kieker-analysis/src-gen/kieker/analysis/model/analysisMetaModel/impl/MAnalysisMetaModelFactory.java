/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import kieker.analysis.model.analysisMetaModel.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class MAnalysisMetaModelFactory extends EFactoryImpl implements MIAnalysisMetaModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static MIAnalysisMetaModelFactory init() {
		try {
			MIAnalysisMetaModelFactory theAnalysisMetaModelFactory = (MIAnalysisMetaModelFactory) EPackage.Registry.INSTANCE
					.getEFactory(MIAnalysisMetaModelPackage.eNS_URI);
			if (theAnalysisMetaModelFactory != null) {
				return theAnalysisMetaModelFactory;
			}
		} catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new MAnalysisMetaModelFactory();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MAnalysisMetaModelFactory() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
		case MIAnalysisMetaModelPackage.PROJECT:
			return createProject();
		case MIAnalysisMetaModelPackage.INPUT_PORT:
			return createInputPort();
		case MIAnalysisMetaModelPackage.OUTPUT_PORT:
			return createOutputPort();
		case MIAnalysisMetaModelPackage.PROPERTY:
			return createProperty();
		case MIAnalysisMetaModelPackage.FILTER:
			return createFilter();
		case MIAnalysisMetaModelPackage.READER:
			return createReader();
		case MIAnalysisMetaModelPackage.REPOSITORY:
			return createRepository();
		case MIAnalysisMetaModelPackage.DEPENDENCY:
			return createDependency();
		case MIAnalysisMetaModelPackage.REPOSITORY_CONNECTOR:
			return createRepositoryConnector();
		case MIAnalysisMetaModelPackage.DISPLAY:
			return createDisplay();
		case MIAnalysisMetaModelPackage.VIEW:
			return createView();
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR:
			return createDisplayConnector();
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
	public MIProject createProject() {
		MProject project = new MProject();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIInputPort createInputPort() {
		MInputPort inputPort = new MInputPort();
		return inputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIOutputPort createOutputPort() {
		MOutputPort outputPort = new MOutputPort();
		return outputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIProperty createProperty() {
		MProperty property = new MProperty();
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIFilter createFilter() {
		MFilter filter = new MFilter();
		return filter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIReader createReader() {
		MReader reader = new MReader();
		return reader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIRepository createRepository() {
		MRepository repository = new MRepository();
		return repository;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIDependency createDependency() {
		MDependency dependency = new MDependency();
		return dependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIRepositoryConnector createRepositoryConnector() {
		MRepositoryConnector repositoryConnector = new MRepositoryConnector();
		return repositoryConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIDisplay createDisplay() {
		MDisplay display = new MDisplay();
		return display;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIView createView() {
		MView view = new MView();
		return view;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIDisplayConnector createDisplayConnector() {
		MDisplayConnector displayConnector = new MDisplayConnector();
		return displayConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public MIAnalysisMetaModelPackage getAnalysisMetaModelPackage() {
		return (MIAnalysisMetaModelPackage) getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static MIAnalysisMetaModelPackage getPackage() {
		return MIAnalysisMetaModelPackage.eINSTANCE;
	}

} // MAnalysisMetaModelFactory
