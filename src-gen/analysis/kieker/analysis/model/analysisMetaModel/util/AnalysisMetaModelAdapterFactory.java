/**
 */
package kieker.analysis.model.analysisMetaModel.util;

import kieker.analysis.model.analysisMetaModel.*;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;

import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage
 * @generated
 */
public class AnalysisMetaModelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static MIAnalysisMetaModelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisMetaModelAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = MIAnalysisMetaModelPackage.eINSTANCE;
		}
	}

	/**
	 * Returns whether this factory is applicable for the type of the object.
	 * <!-- begin-user-doc -->
	 * This implementation returns <code>true</code> if the object is either the model's package or is an instance object of the model.
	 * <!-- end-user-doc -->
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject)object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AnalysisMetaModelSwitch<Adapter> modelSwitch =
		new AnalysisMetaModelSwitch<Adapter>() {
			@Override
			public Adapter caseProject(MIProject object) {
				return createProjectAdapter();
			}
			@Override
			public Adapter casePlugin(MIPlugin object) {
				return createPluginAdapter();
			}
			@Override
			public Adapter casePort(MIPort object) {
				return createPortAdapter();
			}
			@Override
			public Adapter caseInputPort(MIInputPort object) {
				return createInputPortAdapter();
			}
			@Override
			public Adapter caseOutputPort(MIOutputPort object) {
				return createOutputPortAdapter();
			}
			@Override
			public Adapter caseProperty(MIProperty object) {
				return createPropertyAdapter();
			}
			@Override
			public Adapter caseFilter(MIFilter object) {
				return createFilterAdapter();
			}
			@Override
			public Adapter caseReader(MIReader object) {
				return createReaderAdapter();
			}
			@Override
			public Adapter caseRepository(MIRepository object) {
				return createRepositoryAdapter();
			}
			@Override
			public Adapter caseDependency(MIDependency object) {
				return createDependencyAdapter();
			}
			@Override
			public Adapter caseRepositoryConnector(MIRepositoryConnector object) {
				return createRepositoryConnectorAdapter();
			}
			@Override
			public Adapter caseDisplay(MIDisplay object) {
				return createDisplayAdapter();
			}
			@Override
			public Adapter caseView(MIView object) {
				return createViewAdapter();
			}
			@Override
			public Adapter caseDisplayConnector(MIDisplayConnector object) {
				return createDisplayConnectorAdapter();
			}
			@Override
			public Adapter caseAnalysisComponent(MIAnalysisComponent object) {
				return createAnalysisComponentAdapter();
			}
			@Override
			public Adapter defaultCase(EObject object) {
				return createEObjectAdapter();
			}
		};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param target the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject)target);
	}


	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIProject
	 * @generated
	 */
	public Adapter createProjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIPlugin <em>Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIPlugin
	 * @generated
	 */
	public Adapter createPluginAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIPort
	 * @generated
	 */
	public Adapter createPortAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIInputPort <em>Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIInputPort
	 * @generated
	 */
	public Adapter createInputPortAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIOutputPort <em>Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIOutputPort
	 * @generated
	 */
	public Adapter createOutputPortAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIProperty
	 * @generated
	 */
	public Adapter createPropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIFilter <em>Filter</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIFilter
	 * @generated
	 */
	public Adapter createFilterAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIReader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIReader
	 * @generated
	 */
	public Adapter createReaderAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIRepository <em>Repository</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIRepository
	 * @generated
	 */
	public Adapter createRepositoryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIDependency <em>Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIDependency
	 * @generated
	 */
	public Adapter createDependencyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIRepositoryConnector <em>Repository Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIRepositoryConnector
	 * @generated
	 */
	public Adapter createRepositoryConnectorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIDisplay <em>Display</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIDisplay
	 * @generated
	 */
	public Adapter createDisplayAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIView <em>View</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIView
	 * @generated
	 */
	public Adapter createViewAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIDisplayConnector <em>Display Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIDisplayConnector
	 * @generated
	 */
	public Adapter createDisplayConnectorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIAnalysisComponent <em>Analysis Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisComponent
	 * @generated
	 */
	public Adapter createAnalysisComponentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for the default case.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null.
	 * <!-- end-user-doc -->
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //AnalysisMetaModelAdapterFactory
