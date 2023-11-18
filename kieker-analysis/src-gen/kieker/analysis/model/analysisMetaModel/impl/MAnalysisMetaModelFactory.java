/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIDisplay;
import kieker.analysis.model.analysisMetaModel.MIDisplayConnector;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIReader;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;
import kieker.analysis.model.analysisMetaModel.MIView;

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
			final MIAnalysisMetaModelFactory theAnalysisMetaModelFactory = (MIAnalysisMetaModelFactory) EPackage.Registry.INSTANCE
					.getEFactory(MIAnalysisMetaModelPackage.eNS_URI);
			if (theAnalysisMetaModelFactory != null) {
				return theAnalysisMetaModelFactory;
			}
		} catch (final Exception exception) {
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
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case MIAnalysisMetaModelPackage.PROJECT:
			return this.createProject();
		case MIAnalysisMetaModelPackage.INPUT_PORT:
			return this.createInputPort();
		case MIAnalysisMetaModelPackage.OUTPUT_PORT:
			return this.createOutputPort();
		case MIAnalysisMetaModelPackage.PROPERTY:
			return this.createProperty();
		case MIAnalysisMetaModelPackage.FILTER:
			return this.createFilter();
		case MIAnalysisMetaModelPackage.READER:
			return this.createReader();
		case MIAnalysisMetaModelPackage.REPOSITORY:
			return this.createRepository();
		case MIAnalysisMetaModelPackage.DEPENDENCY:
			return this.createDependency();
		case MIAnalysisMetaModelPackage.REPOSITORY_CONNECTOR:
			return this.createRepositoryConnector();
		case MIAnalysisMetaModelPackage.DISPLAY:
			return this.createDisplay();
		case MIAnalysisMetaModelPackage.VIEW:
			return this.createView();
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR:
			return this.createDisplayConnector();
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
		final MProject project = new MProject();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIInputPort createInputPort() {
		final MInputPort inputPort = new MInputPort();
		return inputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIOutputPort createOutputPort() {
		final MOutputPort outputPort = new MOutputPort();
		return outputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIProperty createProperty() {
		final MProperty property = new MProperty();
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIFilter createFilter() {
		final MFilter filter = new MFilter();
		return filter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIReader createReader() {
		final MReader reader = new MReader();
		return reader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIRepository createRepository() {
		final MRepository repository = new MRepository();
		return repository;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIDependency createDependency() {
		final MDependency dependency = new MDependency();
		return dependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIRepositoryConnector createRepositoryConnector() {
		final MRepositoryConnector repositoryConnector = new MRepositoryConnector();
		return repositoryConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIDisplay createDisplay() {
		final MDisplay display = new MDisplay();
		return display;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIView createView() {
		final MView view = new MView();
		return view;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIDisplayConnector createDisplayConnector() {
		final MDisplayConnector displayConnector = new MDisplayConnector();
		return displayConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public MIAnalysisMetaModelPackage getAnalysisMetaModelPackage() {
		return (MIAnalysisMetaModelPackage) this.getEPackage();
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
