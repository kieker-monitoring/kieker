/**
 */
package kieker.model.analysismodel.type.util;

import java.util.Map;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.ecore.EObject;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * The <b>Adapter Factory</b> for the model.
 * It provides an adapter <code>createXXX</code> method for each class of the model.
 * <!-- end-user-doc -->
 *
 * @see kieker.model.analysismodel.type.TypePackage
 * @generated
 */
public class TypeAdapterFactory extends AdapterFactoryImpl {
	/**
	 * The cached model package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static TypePackage modelPackage;

	/**
	 * Creates an instance of the adapter factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TypeAdapterFactory() {
		if (modelPackage == null) {
			modelPackage = TypePackage.eINSTANCE;
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
	protected TypeSwitch<Adapter> modelSwitch = new TypeSwitch<Adapter>() {
		@Override
		public Adapter caseTypeModel(final TypeModel object) {
			return TypeAdapterFactory.this.createTypeModelAdapter();
		}

		@Override
		public Adapter caseEStringToComponentTypeMapEntry(final Map.Entry<String, ComponentType> object) {
			return TypeAdapterFactory.this.createEStringToComponentTypeMapEntryAdapter();
		}

		@Override
		public Adapter caseComponentType(final ComponentType object) {
			return TypeAdapterFactory.this.createComponentTypeAdapter();
		}

		@Override
		public Adapter caseEStringToOperationTypeMapEntry(final Map.Entry<String, OperationType> object) {
			return TypeAdapterFactory.this.createEStringToOperationTypeMapEntryAdapter();
		}

		@Override
		public Adapter caseEStringToStorageTypeMapEntry(final Map.Entry<String, StorageType> object) {
			return TypeAdapterFactory.this.createEStringToStorageTypeMapEntryAdapter();
		}

		@Override
		public Adapter caseOperationType(final OperationType object) {
			return TypeAdapterFactory.this.createOperationTypeAdapter();
		}

		@Override
		public Adapter caseStorageType(final StorageType object) {
			return TypeAdapterFactory.this.createStorageTypeAdapter();
		}

		@Override
		public Adapter caseProvidedInterfaceType(final ProvidedInterfaceType object) {
			return TypeAdapterFactory.this.createProvidedInterfaceTypeAdapter();
		}

		@Override
		public Adapter caseEStringToProvidedInterfaceTypeMapEntry(final Map.Entry<String, ProvidedInterfaceType> object) {
			return TypeAdapterFactory.this.createEStringToProvidedInterfaceTypeMapEntryAdapter();
		}

		@Override
		public Adapter caseRequiredInterfaceType(final RequiredInterfaceType object) {
			return TypeAdapterFactory.this.createRequiredInterfaceTypeAdapter();
		}

		@Override
		public Adapter caseInterfaceEStringToOperationTypeMapEntry(final Map.Entry<String, OperationType> object) {
			return TypeAdapterFactory.this.createInterfaceEStringToOperationTypeMapEntryAdapter();
		}

		@Override
		public Adapter defaultCase(final EObject object) {
			return TypeAdapterFactory.this.createEObjectAdapter();
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
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.type.TypeModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.type.TypeModel
	 * @generated
	 */
	public Adapter createTypeModelAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Component Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToComponentTypeMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.type.ComponentType <em>Component Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.type.ComponentType
	 * @generated
	 */
	public Adapter createComponentTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Operation Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToOperationTypeMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Storage Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToStorageTypeMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.type.OperationType <em>Operation Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.type.OperationType
	 * @generated
	 */
	public Adapter createOperationTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.type.StorageType <em>Storage Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.type.StorageType
	 * @generated
	 */
	public Adapter createStorageTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.type.ProvidedInterfaceType <em>Provided Interface Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.type.ProvidedInterfaceType
	 * @generated
	 */
	public Adapter createProvidedInterfaceTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>EString To Provided Interface Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createEStringToProvidedInterfaceTypeMapEntryAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link kieker.model.analysismodel.type.RequiredInterfaceType <em>Required Interface Type</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see kieker.model.analysismodel.type.RequiredInterfaceType
	 * @generated
	 */
	public Adapter createRequiredInterfaceTypeAdapter() {
		return null;
	}

	/**
	 * Creates a new adapter for an object of class '{@link java.util.Map.Entry <em>Interface EString To Operation Type Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * This default implementation returns null so that we can easily ignore cases;
	 * it's useful to ignore a case when inheritance will catch all the cases anyway.
	 * <!-- end-user-doc -->
	 *
	 * @return the new adapter.
	 * @see java.util.Map.Entry
	 * @generated
	 */
	public Adapter createInterfaceEStringToOperationTypeMapEntryAdapter() {
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

} // TypeAdapterFactory
