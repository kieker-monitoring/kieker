/**
 */
package kieker.model.analysismodel.type.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class TypeFactoryImpl extends EFactoryImpl implements TypeFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static TypeFactory init() {
		try {
			final TypeFactory theTypeFactory = (TypeFactory) EPackage.Registry.INSTANCE.getEFactory(TypePackage.eNS_URI);
			if (theTypeFactory != null) {
				return theTypeFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TypeFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public TypeFactoryImpl() {
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
		case TypePackage.TYPE_MODEL:
			return this.createTypeModel();
		case TypePackage.ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY:
			return (EObject) this.createEStringToComponentTypeMapEntry();
		case TypePackage.COMPONENT_TYPE:
			return this.createComponentType();
		case TypePackage.ESTRING_TO_OPERATION_TYPE_MAP_ENTRY:
			return (EObject) this.createEStringToOperationTypeMapEntry();
		case TypePackage.ESTRING_TO_STORAGE_TYPE_MAP_ENTRY:
			return (EObject) this.createEStringToStorageTypeMapEntry();
		case TypePackage.OPERATION_TYPE:
			return this.createOperationType();
		case TypePackage.STORAGE_TYPE:
			return this.createStorageType();
		case TypePackage.PROVIDED_INTERFACE_TYPE:
			return this.createProvidedInterfaceType();
		case TypePackage.ESTRING_TO_PROVIDED_INTERFACE_TYPE_MAP_ENTRY:
			return (EObject) this.createEStringToProvidedInterfaceTypeMapEntry();
		case TypePackage.REQUIRED_INTERFACE_TYPE:
			return this.createRequiredInterfaceType();
		case TypePackage.INTERFACE_ESTRING_TO_OPERATION_TYPE_MAP_ENTRY:
			return (EObject) this.createInterfaceEStringToOperationTypeMapEntry();
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
	public TypeModel createTypeModel() {
		final TypeModelImpl typeModel = new TypeModelImpl();
		return typeModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, ComponentType> createEStringToComponentTypeMapEntry() {
		final EStringToComponentTypeMapEntryImpl eStringToComponentTypeMapEntry = new EStringToComponentTypeMapEntryImpl();
		return eStringToComponentTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComponentType createComponentType() {
		final ComponentTypeImpl componentType = new ComponentTypeImpl();
		return componentType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, OperationType> createEStringToOperationTypeMapEntry() {
		final EStringToOperationTypeMapEntryImpl eStringToOperationTypeMapEntry = new EStringToOperationTypeMapEntryImpl();
		return eStringToOperationTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, StorageType> createEStringToStorageTypeMapEntry() {
		final EStringToStorageTypeMapEntryImpl eStringToStorageTypeMapEntry = new EStringToStorageTypeMapEntryImpl();
		return eStringToStorageTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationType createOperationType() {
		final OperationTypeImpl operationType = new OperationTypeImpl();
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StorageType createStorageType() {
		final StorageTypeImpl storageType = new StorageTypeImpl();
		return storageType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ProvidedInterfaceType createProvidedInterfaceType() {
		final ProvidedInterfaceTypeImpl providedInterfaceType = new ProvidedInterfaceTypeImpl();
		return providedInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, ProvidedInterfaceType> createEStringToProvidedInterfaceTypeMapEntry() {
		final EStringToProvidedInterfaceTypeMapEntryImpl eStringToProvidedInterfaceTypeMapEntry = new EStringToProvidedInterfaceTypeMapEntryImpl();
		return eStringToProvidedInterfaceTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public RequiredInterfaceType createRequiredInterfaceType() {
		final RequiredInterfaceTypeImpl requiredInterfaceType = new RequiredInterfaceTypeImpl();
		return requiredInterfaceType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, OperationType> createInterfaceEStringToOperationTypeMapEntry() {
		final InterfaceEStringToOperationTypeMapEntryImpl interfaceEStringToOperationTypeMapEntry = new InterfaceEStringToOperationTypeMapEntryImpl();
		return interfaceEStringToOperationTypeMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public TypePackage getTypePackage() {
		return (TypePackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TypePackage getPackage() {
		return TypePackage.eINSTANCE;
	}

} // TypeFactoryImpl
