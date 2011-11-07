/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.util;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IAnalysisPlugin;
import kieker.analysis.model.analysisMetaModel.IConfigurable;
import kieker.analysis.model.analysisMetaModel.IConnector;
import kieker.analysis.model.analysisMetaModel.IInputPort;
import kieker.analysis.model.analysisMetaModel.IOutputPort;
import kieker.analysis.model.analysisMetaModel.IPlugin;
import kieker.analysis.model.analysisMetaModel.IPort;
import kieker.analysis.model.analysisMetaModel.IProject;
import kieker.analysis.model.analysisMetaModel.IProperty;
import kieker.analysis.model.analysisMetaModel.IReader;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 * 
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage
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
	protected static IAnalysisMetaModelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AnalysisMetaModelAdapterFactory() {
		if (AnalysisMetaModelAdapterFactory.modelPackage == null) {
			AnalysisMetaModelAdapterFactory.modelPackage = IAnalysisMetaModelPackage.eINSTANCE;
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
		if (object == AnalysisMetaModelAdapterFactory.modelPackage) {
			return true;
		}
		if (object instanceof EObject) {
			return ((EObject) object).eClass().getEPackage() == AnalysisMetaModelAdapterFactory.modelPackage;
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
	protected AnalysisMetaModelSwitch<Adapter> modelSwitch =
			new AnalysisMetaModelSwitch<Adapter>() {
				@Override
				public Adapter caseProject(final IProject object) {
					return AnalysisMetaModelAdapterFactory.this.createProjectAdapter();
				}

				@Override
				public Adapter casePlugin(final IPlugin object) {
					return AnalysisMetaModelAdapterFactory.this.createPluginAdapter();
				}

				@Override
				public Adapter caseConnector(final IConnector object) {
					return AnalysisMetaModelAdapterFactory.this.createConnectorAdapter();
				}

				@Override
				public Adapter caseConfigurable(final IConfigurable object) {
					return AnalysisMetaModelAdapterFactory.this.createConfigurableAdapter();
				}

				@Override
				public Adapter casePort(final IPort object) {
					return AnalysisMetaModelAdapterFactory.this.createPortAdapter();
				}

				@Override
				public Adapter caseInputPort(final IInputPort object) {
					return AnalysisMetaModelAdapterFactory.this.createInputPortAdapter();
				}

				@Override
				public Adapter caseOutputPort(final IOutputPort object) {
					return AnalysisMetaModelAdapterFactory.this.createOutputPortAdapter();
				}

				@Override
				public Adapter caseProperty(final IProperty object) {
					return AnalysisMetaModelAdapterFactory.this.createPropertyAdapter();
				}

				@Override
				public Adapter caseAnalysisPlugin(final IAnalysisPlugin object) {
					return AnalysisMetaModelAdapterFactory.this.createAnalysisPluginAdapter();
				}

				@Override
				public Adapter caseReader(final IReader object) {
					return AnalysisMetaModelAdapterFactory.this.createReaderAdapter();
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
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IProject
	 * @generated
	 */
	public Adapter createProjectAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IPlugin <em>Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IPlugin
	 * @generated
	 */
	public Adapter createPluginAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IConnector <em>Connector</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IConnector
	 * @generated
	 */
	public Adapter createConnectorAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IConfigurable <em>Configurable</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IConfigurable
	 * @generated
	 */
	public Adapter createConfigurableAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IPort
	 * @generated
	 */
	public Adapter createPortAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IInputPort <em>Input Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IInputPort
	 * @generated
	 */
	public Adapter createInputPortAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IOutputPort <em>Output Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IOutputPort
	 * @generated
	 */
	public Adapter createOutputPortAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IProperty <em>Property</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IProperty
	 * @generated
	 */
	public Adapter createPropertyAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IAnalysisPlugin <em>Analysis Plugin</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IAnalysisPlugin
	 * @generated
	 */
	public Adapter createAnalysisPluginAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IReader <em>Reader</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IReader
	 * @generated
	 */
	public Adapter createReaderAdapter() {
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
