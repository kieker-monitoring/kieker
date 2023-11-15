/**
 */
package kieker.model.collection.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.model.analysismodel.type.OperationType;
import kieker.model.collection.CollectionFactory;
import kieker.model.collection.CollectionPackage;
import kieker.model.collection.Connections;
import kieker.model.collection.Coupling;
import kieker.model.collection.OperationCollection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class CollectionFactoryImpl extends EFactoryImpl implements CollectionFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static CollectionFactory init() {
		try {
			final CollectionFactory theCollectionFactory = (CollectionFactory) EPackage.Registry.INSTANCE.getEFactory(CollectionPackage.eNS_URI);
			if (theCollectionFactory != null) {
				return theCollectionFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CollectionFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public CollectionFactoryImpl() {
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
		case CollectionPackage.CONNECTIONS:
			return this.createConnections();
		case CollectionPackage.OPERATION_COLLECTION:
			return this.createOperationCollection();
		case CollectionPackage.COUPLING_TO_OPERATION_MAP:
			return (EObject) this.createCouplingToOperationMap();
		case CollectionPackage.NAME_TO_OPERATION_MAP:
			return (EObject) this.createNameToOperationMap();
		case CollectionPackage.COUPLING:
			return this.createCoupling();
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
	public Connections createConnections() {
		final ConnectionsImpl connections = new ConnectionsImpl();
		return connections;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationCollection createOperationCollection() {
		final OperationCollectionImpl operationCollection = new OperationCollectionImpl();
		return operationCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<Coupling, OperationCollection> createCouplingToOperationMap() {
		final CouplingToOperationMapImpl couplingToOperationMap = new CouplingToOperationMapImpl();
		return couplingToOperationMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, OperationType> createNameToOperationMap() {
		final NameToOperationMapImpl nameToOperationMap = new NameToOperationMapImpl();
		return nameToOperationMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Coupling createCoupling() {
		final CouplingImpl coupling = new CouplingImpl();
		return coupling;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public CollectionPackage getCollectionPackage() {
		return (CollectionPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CollectionPackage getPackage() {
		return CollectionPackage.eINSTANCE;
	}

} // CollectionFactoryImpl
