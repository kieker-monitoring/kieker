/**
 */
package kieker.model.collection.impl;

import java.util.Map;

import kieker.model.analysismodel.type.OperationType;

import kieker.model.collection.*;

import org.eclipse.emf.common.util.EMap;

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
public class CollectionFactoryImpl extends EFactoryImpl implements CollectionFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CollectionFactory init() {
		try {
			CollectionFactory theCollectionFactory = (CollectionFactory)EPackage.Registry.INSTANCE.getEFactory(CollectionPackage.eNS_URI);
			if (theCollectionFactory != null) {
				return theCollectionFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CollectionFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CollectionFactoryImpl() {
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
			case CollectionPackage.CONNECTIONS: return createConnections();
			case CollectionPackage.OPERATION_COLLECTION: return createOperationCollection();
			case CollectionPackage.COUPLING_TO_OPERATION_MAP: return (EObject)createCouplingToOperationMap();
			case CollectionPackage.NAME_TO_OPERATION_MAP: return (EObject)createNameToOperationMap();
			case CollectionPackage.COUPLING: return createCoupling();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Connections createConnections() {
		ConnectionsImpl connections = new ConnectionsImpl();
		return connections;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OperationCollection createOperationCollection() {
		OperationCollectionImpl operationCollection = new OperationCollectionImpl();
		return operationCollection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<Coupling, EMap<String, OperationType>> createCouplingToOperationMap() {
		CouplingToOperationMapImpl couplingToOperationMap = new CouplingToOperationMapImpl();
		return couplingToOperationMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, OperationType> createNameToOperationMap() {
		NameToOperationMapImpl nameToOperationMap = new NameToOperationMapImpl();
		return nameToOperationMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Coupling createCoupling() {
		CouplingImpl coupling = new CouplingImpl();
		return coupling;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CollectionPackage getCollectionPackage() {
		return (CollectionPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CollectionPackage getPackage() {
		return CollectionPackage.eINSTANCE;
	}

} //CollectionFactoryImpl
