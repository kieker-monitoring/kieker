/**
 * <copyright>
 * </copyright>
 *
 * $Id$
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
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage
 * @generated
 */
public class AnalysisMetaModelAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static IAnalysisMetaModelPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AnalysisMetaModelAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = IAnalysisMetaModelPackage.eINSTANCE;
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
			public Adapter caseProject(IProject object) {
				return createProjectAdapter();
			}
			@Override
			public Adapter casePlugin(IPlugin object) {
				return createPluginAdapter();
			}
			@Override
			public Adapter casePort(IPort object) {
				return createPortAdapter();
			}
			@Override
			public Adapter caseInputPort(IInputPort object) {
				return createInputPortAdapter();
			}
			@Override
			public Adapter caseOutputPort(IOutputPort object) {
				return createOutputPortAdapter();
			}
			@Override
			public Adapter caseProperty(IProperty object) {
				return createPropertyAdapter();
			}
			@Override
			public Adapter caseAnalysisPlugin(IAnalysisPlugin object) {
				return createAnalysisPluginAdapter();
			}
			@Override
			public Adapter caseReader(IReader object) {
				return createReaderAdapter();
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
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IProject <em>Project</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
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
	 * @return the new adapter.
	 * @see kieker.analysis.model.analysisMetaModel.IPlugin
	 * @generated
	 */
	public Adapter createPluginAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysis.model.analysisMetaModel.IPort <em>Port</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
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
	 * @return the new adapter.
	 * @generated
	 */
	public Adapter createEObjectAdapter() {
		return null;
	}

} //AnalysisMetaModelAdapterFactory
