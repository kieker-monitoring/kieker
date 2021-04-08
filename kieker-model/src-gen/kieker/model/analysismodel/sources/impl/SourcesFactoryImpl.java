/**
 */
package kieker.model.analysismodel.sources.impl;

import java.util.Map;

import kieker.model.analysismodel.sources.*;

import org.eclipse.emf.common.util.EList;

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
public class SourcesFactoryImpl extends EFactoryImpl implements SourcesFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static SourcesFactory init() {
		try {
			SourcesFactory theSourcesFactory = (SourcesFactory)EPackage.Registry.INSTANCE.getEFactory(SourcesPackage.eNS_URI);
			if (theSourcesFactory != null) {
				return theSourcesFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new SourcesFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public SourcesFactoryImpl() {
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
			case SourcesPackage.SOURCE_MODEL: return createSourceModel();
			case SourcesPackage.EOBJECT_TO_SOURCES_ENTRY: return (EObject)createEObjectToSourcesEntry();
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
	public SourceModel createSourceModel() {
		SourceModelImpl sourceModel = new SourceModelImpl();
		return sourceModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EObject, EList<String>> createEObjectToSourcesEntry() {
		EObjectToSourcesEntryImpl eObjectToSourcesEntry = new EObjectToSourcesEntryImpl();
		return eObjectToSourcesEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SourcesPackage getSourcesPackage() {
		return (SourcesPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static SourcesPackage getPackage() {
		return SourcesPackage.eINSTANCE;
	}

} //SourcesFactoryImpl
