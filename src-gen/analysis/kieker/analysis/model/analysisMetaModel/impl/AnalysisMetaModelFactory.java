/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IAnalysisPlugin;
import kieker.analysis.model.analysisMetaModel.IConnector;
import kieker.analysis.model.analysisMetaModel.IInputPort;
import kieker.analysis.model.analysisMetaModel.IOutputPort;
import kieker.analysis.model.analysisMetaModel.IProject;
import kieker.analysis.model.analysisMetaModel.IProperty;
import kieker.analysis.model.analysisMetaModel.IReader;

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
public class AnalysisMetaModelFactory extends EFactoryImpl implements IAnalysisMetaModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public static IAnalysisMetaModelFactory init() {
		try {
			final IAnalysisMetaModelFactory theAnalysisMetaModelFactory = (IAnalysisMetaModelFactory) EPackage.Registry.INSTANCE
					.getEFactory("platform:/resource/Kieker/model/AnalysisMetaModel.ecore");
			if (theAnalysisMetaModelFactory != null) {
				return theAnalysisMetaModelFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AnalysisMetaModelFactory();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AnalysisMetaModelFactory() {
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
		case IAnalysisMetaModelPackage.PROJECT:
			return this.createProject();
		case IAnalysisMetaModelPackage.CONNECTOR:
			return this.createConnector();
		case IAnalysisMetaModelPackage.INPUT_PORT:
			return this.createInputPort();
		case IAnalysisMetaModelPackage.OUTPUT_PORT:
			return this.createOutputPort();
		case IAnalysisMetaModelPackage.PROPERTY:
			return this.createProperty();
		case IAnalysisMetaModelPackage.ANALYSIS_PLUGIN:
			return this.createAnalysisPlugin();
		case IAnalysisMetaModelPackage.READER:
			return this.createReader();
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
	@Override
	public IProject createProject() {
		final Project project = new Project();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IConnector createConnector() {
		final Connector connector = new Connector();
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IInputPort createInputPort() {
		final InputPort inputPort = new InputPort();
		return inputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IOutputPort createOutputPort() {
		final OutputPort outputPort = new OutputPort();
		return outputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IProperty createProperty() {
		final Property property = new Property();
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IAnalysisPlugin createAnalysisPlugin() {
		final AnalysisPlugin analysisPlugin = new AnalysisPlugin();
		return analysisPlugin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IReader createReader() {
		final Reader reader = new Reader();
		return reader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IAnalysisMetaModelPackage getAnalysisMetaModelPackage() {
		return (IAnalysisMetaModelPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static IAnalysisMetaModelPackage getPackage() {
		return IAnalysisMetaModelPackage.eINSTANCE;
	}

} // AnalysisMetaModelFactory
