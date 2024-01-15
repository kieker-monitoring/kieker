/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.AssemblyProvidedInterface;
import kieker.model.analysismodel.assembly.AssemblyRequiredInterface;
import kieker.model.analysismodel.assembly.AssemblyStorage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class AssemblyFactoryImpl extends EFactoryImpl implements AssemblyFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static AssemblyFactory init() {
		try {
			final AssemblyFactory theAssemblyFactory = (AssemblyFactory) EPackage.Registry.INSTANCE.getEFactory(AssemblyPackage.eNS_URI);
			if (theAssemblyFactory != null) {
				return theAssemblyFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new AssemblyFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyFactoryImpl() {
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
		case AssemblyPackage.ASSEMBLY_MODEL:
			return this.createAssemblyModel();
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_COMPONENT_MAP_ENTRY:
			return (EObject) this.createEStringToAssemblyComponentMapEntry();
		case AssemblyPackage.ASSEMBLY_COMPONENT:
			return this.createAssemblyComponent();
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_OPERATION_MAP_ENTRY:
			return (EObject) this.createEStringToAssemblyOperationMapEntry();
		case AssemblyPackage.ASSEMBLY_OPERATION:
			return this.createAssemblyOperation();
		case AssemblyPackage.ASSEMBLY_STORAGE:
			return this.createAssemblyStorage();
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_STORAGE_MAP_ENTRY:
			return (EObject) this.createEStringToAssemblyStorageMapEntry();
		case AssemblyPackage.ASSEMBLY_PROVIDED_INTERFACE:
			return this.createAssemblyProvidedInterface();
		case AssemblyPackage.ESTRING_TO_ASSEMBLY_PROVIDED_INTERFACE_MAP_ENTRY:
			return (EObject) this.createEStringToAssemblyProvidedInterfaceMapEntry();
		case AssemblyPackage.ASSEMBLY_REQUIRED_INTERFACE:
			return this.createAssemblyRequiredInterface();
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
	public AssemblyModel createAssemblyModel() {
		final AssemblyModelImpl assemblyModel = new AssemblyModelImpl();
		return assemblyModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, AssemblyComponent> createEStringToAssemblyComponentMapEntry() {
		final EStringToAssemblyComponentMapEntryImpl eStringToAssemblyComponentMapEntry = new EStringToAssemblyComponentMapEntryImpl();
		return eStringToAssemblyComponentMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyComponent createAssemblyComponent() {
		final AssemblyComponentImpl assemblyComponent = new AssemblyComponentImpl();
		return assemblyComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, AssemblyOperation> createEStringToAssemblyOperationMapEntry() {
		final EStringToAssemblyOperationMapEntryImpl eStringToAssemblyOperationMapEntry = new EStringToAssemblyOperationMapEntryImpl();
		return eStringToAssemblyOperationMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyOperation createAssemblyOperation() {
		final AssemblyOperationImpl assemblyOperation = new AssemblyOperationImpl();
		return assemblyOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyStorage createAssemblyStorage() {
		final AssemblyStorageImpl assemblyStorage = new AssemblyStorageImpl();
		return assemblyStorage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, AssemblyStorage> createEStringToAssemblyStorageMapEntry() {
		final EStringToAssemblyStorageMapEntryImpl eStringToAssemblyStorageMapEntry = new EStringToAssemblyStorageMapEntryImpl();
		return eStringToAssemblyStorageMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyProvidedInterface createAssemblyProvidedInterface() {
		final AssemblyProvidedInterfaceImpl assemblyProvidedInterface = new AssemblyProvidedInterfaceImpl();
		return assemblyProvidedInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, AssemblyProvidedInterface> createEStringToAssemblyProvidedInterfaceMapEntry() {
		final EStringToAssemblyProvidedInterfaceMapEntryImpl eStringToAssemblyProvidedInterfaceMapEntry = new EStringToAssemblyProvidedInterfaceMapEntryImpl();
		return eStringToAssemblyProvidedInterfaceMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyRequiredInterface createAssemblyRequiredInterface() {
		final AssemblyRequiredInterfaceImpl assemblyRequiredInterface = new AssemblyRequiredInterfaceImpl();
		return assemblyRequiredInterface;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyPackage getAssemblyPackage() {
		return (AssemblyPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static AssemblyPackage getPackage() {
		return AssemblyPackage.eINSTANCE;
	}

} // AssemblyFactoryImpl
