/**
 */
package kieker.model.analysismodel.type.impl;

import java.util.Map;

import kieker.model.analysismodel.type.*;

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
public class TypeFactoryImpl extends EFactoryImpl implements TypeFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TypeFactory init() {
		try {
			TypeFactory theTypeFactory = (TypeFactory)EPackage.Registry.INSTANCE.getEFactory(TypePackage.eNS_URI);
			if (theTypeFactory != null) {
				return theTypeFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TypeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypeFactoryImpl() {
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
			case TypePackage.TYPE_MODEL: return createTypeModel();
			case TypePackage.ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY: return (EObject)createEStringToComponentTypeMapEntry();
			case TypePackage.COMPONENT_TYPE: return createComponentType();
			case TypePackage.ESTRING_TO_OPERATION_TYPE_MAP_ENTRY: return (EObject)createEStringToOperationTypeMapEntry();
			case TypePackage.ESTRING_TO_STORAGE_TYPE_MAP_ENTRY: return (EObject)createEStringToStorageTypeMapEntry();
			case TypePackage.OPERATION_TYPE: return createOperationType();
			case TypePackage.STORAGE_TYPE: return createStorageType();
			case TypePackage.PROVIDED_INTERFACE_TYPE: return createProvidedInterfaceType();
			case TypePackage.ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY: return (EObject)createEStringToProvidedInterfaceTypeMapEntry();
			case TypePackage.REQUIRED_INTERFACE_TYPE: return createRequiredInterfaceType();
			case TypePackage.INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY: return (EObject)createInterfaceEStringToOperationTypeMapEntry();
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
	public TypeModel createTypeModel() {
		TypeModelImpl typeModel = new TypeModelImpl();
		return typeModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, ComponentType> createEStringToComponentTypeMapEntry() {
		EStringToComponentTypeMapEntryImpl eStringToComponentTypeMapEntry = new EStringToComponentTypeMapEntryImpl();
		return eStringToComponentTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComponentType createComponentType() {
		ComponentTypeImpl componentType = new ComponentTypeImpl();
		return componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, OperationType> createEStringToOperationTypeMapEntry() {
		EStringToOperationTypeMapEntryImpl eStringToOperationTypeMapEntry = new EStringToOperationTypeMapEntryImpl();
		return eStringToOperationTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, StorageType> createEStringToStorageTypeMapEntry() {
		EStringToStorageTypeMapEntryImpl eStringToStorageTypeMapEntry = new EStringToStorageTypeMapEntryImpl();
		return eStringToStorageTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OperationType createOperationType() {
		OperationTypeImpl operationType = new OperationTypeImpl();
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StorageType createStorageType() {
		StorageTypeImpl storageType = new StorageTypeImpl();
		return storageType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType createProvidedInterfaceType() {
		ProvidedInterfaceTypeImpl providedInterfaceType = new ProvidedInterfaceTypeImpl();
		return providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, ProvidedInterfaceType> createEStringToProvidedInterfaceTypeMapEntry() {
		EStringToProvidedInterfaceTypeMapEntryImpl eStringToProvidedInterfaceTypeMapEntry = new EStringToProvidedInterfaceTypeMapEntryImpl();
		return eStringToProvidedInterfaceTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RequiredInterfaceType createRequiredInterfaceType() {
		RequiredInterfaceTypeImpl requiredInterfaceType = new RequiredInterfaceTypeImpl();
		return requiredInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, OperationType> createInterfaceEStringToOperationTypeMapEntry() {
		InterfaceEStringToOperationTypeMapEntryImpl interfaceEStringToOperationTypeMapEntry = new InterfaceEStringToOperationTypeMapEntryImpl();
		return interfaceEStringToOperationTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TypePackage getTypePackage() {
		return (TypePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TypePackage getPackage() {
		return TypePackage.eINSTANCE;
	}

} //TypeFactoryImpl
