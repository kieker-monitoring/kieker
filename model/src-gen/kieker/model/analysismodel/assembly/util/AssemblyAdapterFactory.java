/**
 */
package kieker.model.analysismodel.assembly.util;

import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.assembly.AssemblyStorage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 *
 * @see kieker.model.analysismodel.assembly.AssemblyPackage
 * @generated
 */
public class AssemblyAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static AssemblyPackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = AssemblyPackage.eINSTANCE;
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
	protected AssemblySwitch<Adapter> modelSwitch = new AssemblySwitch<Adapter>() {
		@Override
		public Adapter caseAssemblyModel(final AssemblyModel object) {
			return AssemblyAdapterFactory.this.createAssemblyModelAdapter();
		}

		@Override
		public Adapter caseEStringToAssemblyComponentMapEntry(final Map.Entry<String, AssemblyComponent> object) {
			return AssemblyAdapterFactory.this.createEStringToAssemblyComponentMapEntryAdapter();
		}

		@Override
		public Adapter caseAssemblyComponent(final AssemblyComponent object) {
			return AssemblyAdapterFactory.this.createAssemblyComponentAdapter();
		}

		@Override
		public Adapter caseEStringToAssemblyOperationMapEntry(final Map.Entry<String, AssemblyOperation> object) {
			return AssemblyAdapterFactory.this.createEStringToAssemblyOperationMapEntryAdapter();
		}

		@Override
		public Adapter caseAssemblyOperation(final AssemblyOperation object) {
			return AssemblyAdapterFactory.this.createAssemblyOperationAdapter();
		}

		@Override
		public Adapter caseAssemblyStorage(final AssemblyStorage object) {
			return AssemblyAdapterFactory.this.createAssemblyStorageAdapter();
		}

		@Override
		public Adapter caseEStringToAssemblyStorageMapEntry(final Map.Entry<String, AssemblyStorage> object) {
			return AssemblyAdapterFactory.this.createEStringToAssemblyStorageMapEntryAdapter();
		}

		@Override
		public Adapter caseAssemblyProvidedInterface(final AssemblyProvidedInterface object) {
			return AssemblyAdapterFactory.this.createAssemblyProvidedInterfaceAdapter();
		}

		@Override
		public Adapter caseEStringToAssemblyProvidedInterfaceMapEntry(final Map.Entry<String, AssemblyProvidedInterface> object) {
			return AssemblyAdapterFactory.this.createEStringToAssemblyProvidedInterfaceMapEntryAdapter();
		}

		@Override
		public Adapter caseAssemblyRequiredInterface(final AssemblyRequiredInterface object) {
			return AssemblyAdapterFactory.this.createAssemblyRequiredInterfaceAdapter();
		}

		@Override
		public Adapter defaultCase(final EObject object) {
			return AssemblyAdapterFactory.this.createEObjectAdapter();
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
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.assembly.AssemblyModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.assembly.AssemblyModel
	 * @generated
	 */
	public Adapter createAssemblyModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Assembly Component Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToAssemblyComponentMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.assembly.AssemblyComponent <em>Component</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.assembly.AssemblyComponent
	 * @generated
	 */
	public Adapter createAssemblyComponentAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Assembly Operation Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToAssemblyOperationMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.assembly.AssemblyOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.assembly.AssemblyOperation
	 * @generated
	 */
	public Adapter createAssemblyOperationAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.assembly.AssemblyStorage <em>Storage</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.assembly.AssemblyStorage
	 * @generated
	 */
	public Adapter createAssemblyStorageAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Assembly Storage Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToAssemblyStorageMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.assembly.AssemblyProvidedInterface <em>Provided Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.assembly.AssemblyProvidedInterface
	 * @generated
	 */
	public Adapter createAssemblyProvidedInterfaceAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Assembly Provided Interface Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToAssemblyProvidedInterfaceMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.assembly.AssemblyRequiredInterface <em>Required Interface</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.assembly.AssemblyRequiredInterface
	 * @generated
	 */
	public Adapter createAssemblyRequiredInterfaceAdapter() {
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

} // AssemblyAdapterFactory
