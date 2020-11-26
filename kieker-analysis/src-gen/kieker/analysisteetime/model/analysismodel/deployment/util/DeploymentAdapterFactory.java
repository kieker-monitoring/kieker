/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.util;

import java.util.Map;
import kieker.analysisteetime.model.analysismodel.deployment.*;

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
 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage
 * @generated
 */
public class DeploymentAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected static DeploymentPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public DeploymentAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = DeploymentPackage.eINSTANCE;
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
	public boolean isFactoryForType(Object object) {
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
	protected DeploymentSwitch<Adapter> modelSwitch = new DeploymentSwitch<Adapter>() {
		@Override
		public Adapter caseDeploymentModel(DeploymentModel object) {
			return createDeploymentModelAdapter();
		}

		@Override
		public Adapter caseEStringToDeploymentContextMapEntry(Map.Entry<String, DeploymentContext> object) {
			return createEStringToDeploymentContextMapEntryAdapter();
		}

		@Override
		public Adapter caseDeploymentContext(DeploymentContext object) {
			return createDeploymentContextAdapter();
		}

		@Override
		public Adapter caseEStringToDeployedComponentMapEntry(Map.Entry<String, DeployedComponent> object) {
			return createEStringToDeployedComponentMapEntryAdapter();
		}

		@Override
		public Adapter caseDeployedComponent(DeployedComponent object) {
			return createDeployedComponentAdapter();
		}

		@Override
		public Adapter caseEStringToDeployedOperationMapEntry(Map.Entry<String, DeployedOperation> object) {
			return createEStringToDeployedOperationMapEntryAdapter();
		}

		@Override
		public Adapter caseDeployedOperation(DeployedOperation object) {
			return createDeployedOperationAdapter();
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
	 * 
	 * @param target
	 *            the object to adapt.
	 * @return the adapter for the <code>target</code>.
	 * @generated
	 */
	@Override
	public Adapter createAdapter(Notifier target) {
		return modelSwitch.doSwitch((EObject) target);
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel
	 * @generated
	 */
	public Adapter createDeploymentModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Deployment Context Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToDeploymentContextMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeploymentContext
	 * @generated
	 */
	public Adapter createDeploymentContextAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Deployed Component Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToDeployedComponentMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent <em>Deployed Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent
	 * @generated
	 */
	public Adapter createDeployedComponentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Deployed Operation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToDeployedOperationMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation <em>Deployed Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 * 
	 * @return the new adapter.
	 * @see kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation
	 * @generated
	 */
	public Adapter createDeployedOperationAdapter() {
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

} // DeploymentAdapterFactory
