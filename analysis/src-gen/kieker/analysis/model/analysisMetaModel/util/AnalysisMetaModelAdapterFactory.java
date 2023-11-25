/**
 */
package kieker.analysis.model.analysisMetaModel.util;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.model.analysisMetaModel.MIAnalysisComponent;
import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIDisplay;
import kieker.analysis.model.analysisMetaModel.MIDisplayConnector;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIPort;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIReader;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;
import kieker.analysis.model.analysisMetaModel.MIView;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage
 * @generated
 */
public class AnalysisMetaModelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static MIAnalysisMetaModelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
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
	 *
	 * @return whether this factory is applicable for the type of the object.
	 * @generated
	 */
	@Override
	public boolean isFactoryForType(final Object object) {
		if (object == modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == modelPackage;
		}
		return false;
	}

	/**
	 * The switch that delegates to the <code>createXXX</code> methods.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AnalysisMetaModelSwitch<Adapter> modelSwitch = new AnalysisMetaModelSwitch<>() {
		@Override
		public Adapter caseProject(final MIProject object) {
			return AnalysisMetaModelAdapterFactory.this.createProjectAdapter();
		}

		@Override
		public Adapter casePlugin(final MIPlugin object) {
			return AnalysisMetaModelAdapterFactory.this.createPluginAdapter();
		}

		@Override
		public Adapter casePort(final MIPort object) {
			return AnalysisMetaModelAdapterFactory.this.createPortAdapter();
		}

		@Override
		public Adapter caseInputPort(final MIInputPort object) {
			return AnalysisMetaModelAdapterFactory.this.createInputPortAdapter();
		}

		@Override
		public Adapter caseOutputPort(final MIOutputPort object) {
			return AnalysisMetaModelAdapterFactory.this.createOutputPortAdapter();
		}

		@Override
		public Adapter caseProperty(final MIProperty object) {
			return AnalysisMetaModelAdapterFactory.this.createPropertyAdapter();
		}

		@Override
		public Adapter caseFilter(final MIFilter object) {
			return AnalysisMetaModelAdapterFactory.this.createFilterAdapter();
		}

		@Override
		public Adapter caseReader(final MIReader object) {
			return AnalysisMetaModelAdapterFactory.this.createReaderAdapter();
		}

		@Override
		public Adapter caseRepository(final MIRepository object) {
			return AnalysisMetaModelAdapterFactory.this.createRepositoryAdapter();
		}

		@Override
		public Adapter caseDependency(final MIDependency object) {
			return AnalysisMetaModelAdapterFactory.this.createDependencyAdapter();
		}

		@Override
		public Adapter caseRepositoryConnector(final MIRepositoryConnector object) {
			return AnalysisMetaModelAdapterFactory.this.createRepositoryConnectorAdapter();
		}

		@Override
		public Adapter caseDisplay(final MIDisplay object) {
			return AnalysisMetaModelAdapterFactory.this.createDisplayAdapter();
		}

		@Override
		public Adapter caseView(final MIView object) {
			return AnalysisMetaModelAdapterFactory.this.createViewAdapter();
		}

		@Override
		public Adapter caseDisplayConnector(final MIDisplayConnector object) {
			return AnalysisMetaModelAdapterFactory.this.createDisplayConnectorAdapter();
		}

		@Override
		public Adapter caseAnalysisComponent(final MIAnalysisComponent object) {
			return AnalysisMetaModelAdapterFactory.this.createAnalysisComponentAdapter();
		}

		@Override
		public Adapter defaultCase(final EObject object) {
			return AnalysisMetaModelAdapterFactory.this.createEObjectAdapter();
		}
	};

	/**
	 * Creates an adapter for the <code>target</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(final Notifier target) {
		return this.modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.MIProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
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
	 *
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} // AnalysisMetaModelAdapterFactory
