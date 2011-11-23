/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
 * @generated
 */
public class AnalysisMetaModelFactory extends EFactoryImpl implements IAnalysisMetaModelFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static IAnalysisMetaModelFactory init() {
		try {
			IAnalysisMetaModelFactory theAnalysisMetaModelFactory = (IAnalysisMetaModelFactory)EPackage.Registry.INSTANCE.getEFactory("platform:/resource/Kieker/model/AnalysisMetaModel.ecore"); 
			if (theAnalysisMetaModelFactory != null) {
				return theAnalysisMetaModelFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AnalysisMetaModelFactory();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisMetaModelFactory() {
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
			case IAnalysisMetaModelPackage.PROJECT: return createProject();
			case IAnalysisMetaModelPackage.INPUT_PORT: return createInputPort();
			case IAnalysisMetaModelPackage.OUTPUT_PORT: return createOutputPort();
			case IAnalysisMetaModelPackage.PROPERTY: return createProperty();
			case IAnalysisMetaModelPackage.ANALYSIS_PLUGIN: return createAnalysisPlugin();
			case IAnalysisMetaModelPackage.READER: return createReader();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProject createProject() {
		Project project = new Project();
		return project;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IInputPort createInputPort() {
		InputPort inputPort = new InputPort();
		return inputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOutputPort createOutputPort() {
		OutputPort outputPort = new OutputPort();
		return outputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IProperty createProperty() {
		Property property = new Property();
		return property;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAnalysisPlugin createAnalysisPlugin() {
		AnalysisPlugin analysisPlugin = new AnalysisPlugin();
		return analysisPlugin;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IReader createReader() {
		Reader reader = new Reader();
		return reader;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IAnalysisMetaModelPackage getAnalysisMetaModelPackage() {
		return (IAnalysisMetaModelPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static IAnalysisMetaModelPackage getPackage() {
		return IAnalysisMetaModelPackage.eINSTANCE;
	}

} //AnalysisMetaModelFactory
