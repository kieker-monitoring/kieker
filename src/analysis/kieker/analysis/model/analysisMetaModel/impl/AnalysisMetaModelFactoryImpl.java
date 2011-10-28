/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import kieker.analysis.model.analysisMetaModel.AnalysisMetaModelFactory;
import kieker.analysis.model.analysisMetaModel.AnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.AnalysisPlugin;
import kieker.analysis.model.analysisMetaModel.Configurable;
import kieker.analysis.model.analysisMetaModel.Connector;
import kieker.analysis.model.analysisMetaModel.InputPort;
import kieker.analysis.model.analysisMetaModel.OutputPort;
import kieker.analysis.model.analysisMetaModel.Plugin;
import kieker.analysis.model.analysisMetaModel.Port;
import kieker.analysis.model.analysisMetaModel.Project;
import kieker.analysis.model.analysisMetaModel.Property;

import kieker.analysis.model.analysisMetaModel.Reader;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class AnalysisMetaModelFactoryImpl extends EFactoryImpl implements AnalysisMetaModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AnalysisMetaModelFactory init() {
		try {
			AnalysisMetaModelFactory theAnalysisMetaModelFactory = (AnalysisMetaModelFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/Kieker/model/AnalysisMetaModel.ecore"); 
			if (theAnalysisMetaModelFactory != null) {
				return theAnalysisMetaModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AnalysisMetaModelFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisMetaModelFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case AnalysisMetaModelPackage.PROJECT: return createProject();
			case AnalysisMetaModelPackage.CONNECTOR: return createConnector();
			case AnalysisMetaModelPackage.INPUT_PORT: return createInputPort();
			case AnalysisMetaModelPackage.OUTPUT_PORT: return createOutputPort();
			case AnalysisMetaModelPackage.CLASS: return createClass();
			case AnalysisMetaModelPackage.PROPERTY: return createProperty();
			case AnalysisMetaModelPackage.ANALYSIS_PLUGIN: return createAnalysisPlugin();
			case AnalysisMetaModelPackage.READER: return createReader();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Project createProject() {
		ProjectImpl project = new ProjectImpl();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Connector createConnector() {
		ConnectorImpl connector = new ConnectorImpl();
		return connector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputPort createInputPort() {
		InputPortImpl inputPort = new InputPortImpl();
		return inputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputPort createOutputPort() {
		OutputPortImpl outputPort = new OutputPortImpl();
		return outputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public kieker.analysis.model.analysisMetaModel.Class createClass() {
		ClassImpl class_ = new ClassImpl();
		return class_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Property createProperty() {
		PropertyImpl property = new PropertyImpl();
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisPlugin createAnalysisPlugin() {
		AnalysisPluginImpl analysisPlugin = new AnalysisPluginImpl();
		return analysisPlugin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Reader createReader() {
		ReaderImpl reader = new ReaderImpl();
		return reader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisMetaModelPackage getAnalysisMetaModelPackage() {
		return (AnalysisMetaModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AnalysisMetaModelPackage getPackage() {
		return AnalysisMetaModelPackage.eINSTANCE;
	}

} //AnalysisMetaModelFactoryImpl
