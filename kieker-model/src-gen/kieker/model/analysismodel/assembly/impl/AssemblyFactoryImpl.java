/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.util.Map;

import kieker.model.analysismodel.assembly.*;

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
public class AssemblyFactoryImpl extends EFactoryImpl implements AssemblyFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static AssemblyFactory init() {
		try {
			AssemblyFactory theAssemblyFactory = (AssemblyFactory)EPackage.Registry.INSTANCE.getEFactory(AssemblyPackage.eNS_URI);
			if (theAssemblyFactory != null) {
				return theAssemblyFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AssemblyFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public AssemblyFactoryImpl() {
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
			case AssemblyPackage.ASSEMBLY_MODEL: return createAssemblyModel();
			case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY: return (EObject)createEStringToAssemblyComponentMapEntry();
			case AssemblyPackage.ASSEMBLY_COMPONENT: return createAssemblyComponent();
			case AssemblyPackage.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY: return (EObject)createEStringToAssemblyOperationMapEntry();
			case AssemblyPackage.ASSEMBLY_OPERATION: return createAssemblyOperation();
			case AssemblyPackage.ASSEMBLY_STORAGE: return createAssemblyStorage();
			case AssemblyPackage.ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY: return (EObject)createEStringToAssemblyStorageMapEntry();
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
	public AssemblyModel createAssemblyModel() {
		AssemblyModelImpl assemblyModel = new AssemblyModelImpl();
		return assemblyModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, AssemblyComponent> createEStringToAssemblyComponentMapEntry() {
		EStringToAssemblyComponentMapEntryImpl eStringToAssemblyComponentMapEntry = new EStringToAssemblyComponentMapEntryImpl();
		return eStringToAssemblyComponentMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyComponent createAssemblyComponent() {
		AssemblyComponentImpl assemblyComponent = new AssemblyComponentImpl();
		return assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, AssemblyOperation> createEStringToAssemblyOperationMapEntry() {
		EStringToAssemblyOperationMapEntryImpl eStringToAssemblyOperationMapEntry = new EStringToAssemblyOperationMapEntryImpl();
		return eStringToAssemblyOperationMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyOperation createAssemblyOperation() {
		AssemblyOperationImpl assemblyOperation = new AssemblyOperationImpl();
		return assemblyOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyStorage createAssemblyStorage() {
		AssemblyStorageImpl assemblyStorage = new AssemblyStorageImpl();
		return assemblyStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, AssemblyStorage> createEStringToAssemblyStorageMapEntry() {
		EStringToAssemblyStorageMapEntryImpl eStringToAssemblyStorageMapEntry = new EStringToAssemblyStorageMapEntryImpl();
		return eStringToAssemblyStorageMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public AssemblyPackage getAssemblyPackage() {
		return (AssemblyPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AssemblyPackage getPackage() {
		return AssemblyPackage.eINSTANCE;
	}

} //AssemblyFactoryImpl
