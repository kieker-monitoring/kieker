/**
 */
package kieker.analysis.model.analysisMetaModel.util;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import kieker.analysis.model.analysisMetaModel.MIAnalysisComponent;
import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIDisplay;
import kieker.analysis.model.analysisMetaModel.MIDisplayConnector;
import kieker.analysis.model.analysisMetaModel.MIFilter;
import kieker.analysis.model.analysisMetaModel.MIInputPort;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIPort;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIReader;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;
import kieker.analysis.model.analysisMetaModel.MIView;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 *
 * @see kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage
 * @generated
 */
public class AnalysisMetaModelSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static MIAnalysisMetaModelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AnalysisMetaModelSwitch() {
		if (modelPackage == null) {
			modelPackage = MIAnalysisMetaModelPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @parameter ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case MIAnalysisMetaModelPackage.PROJECT: {
			final MIProject project = (MIProject) theEObject;
			T result = this.caseProject(project);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.PLUGIN: {
			final MIPlugin plugin = (MIPlugin) theEObject;
			T result = this.casePlugin(plugin);
			if (result == null) {
				result = this.caseAnalysisComponent(plugin);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.PORT: {
			final MIPort port = (MIPort) theEObject;
			T result = this.casePort(port);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.INPUT_PORT: {
			final MIInputPort inputPort = (MIInputPort) theEObject;
			T result = this.caseInputPort(inputPort);
			if (result == null) {
				result = this.casePort(inputPort);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.OUTPUT_PORT: {
			final MIOutputPort outputPort = (MIOutputPort) theEObject;
			T result = this.caseOutputPort(outputPort);
			if (result == null) {
				result = this.casePort(outputPort);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.PROPERTY: {
			final MIProperty property = (MIProperty) theEObject;
			T result = this.caseProperty(property);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.FILTER: {
			final MIFilter filter = (MIFilter) theEObject;
			T result = this.caseFilter(filter);
			if (result == null) {
				result = this.casePlugin(filter);
			}
			if (result == null) {
				result = this.caseAnalysisComponent(filter);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.READER: {
			final MIReader reader = (MIReader) theEObject;
			T result = this.caseReader(reader);
			if (result == null) {
				result = this.casePlugin(reader);
			}
			if (result == null) {
				result = this.caseAnalysisComponent(reader);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.REPOSITORY: {
			final MIRepository repository = (MIRepository) theEObject;
			T result = this.caseRepository(repository);
			if (result == null) {
				result = this.caseAnalysisComponent(repository);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.DEPENDENCY: {
			final MIDependency dependency = (MIDependency) theEObject;
			T result = this.caseDependency(dependency);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.REPOSITORY_CONNECTOR: {
			final MIRepositoryConnector repositoryConnector = (MIRepositoryConnector) theEObject;
			T result = this.caseRepositoryConnector(repositoryConnector);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.DISPLAY: {
			final MIDisplay display = (MIDisplay) theEObject;
			T result = this.caseDisplay(display);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.VIEW: {
			final MIView view = (MIView) theEObject;
			T result = this.caseView(view);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.DISPLAY_CONNECTOR: {
			final MIDisplayConnector displayConnector = (MIDisplayConnector) theEObject;
			T result = this.caseDisplayConnector(displayConnector);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case MIAnalysisMetaModelPackage.ANALYSIS_COMPONENT: {
			final MIAnalysisComponent analysisComponent = (MIAnalysisComponent) theEObject;
			T result = this.caseAnalysisComponent(analysisComponent);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		default:
			return this.defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Project</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Project</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProject(final MIProject object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Plugin</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Plugin</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePlugin(final MIPlugin object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Port</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Port</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T casePort(final MIPort object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Input Port</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Input Port</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInputPort(final MIInputPort object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Output Port</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Output Port</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOutputPort(final MIOutputPort object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Property</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Property</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProperty(final MIProperty object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Filter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Filter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFilter(final MIFilter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Reader</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Reader</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseReader(final MIReader object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Repository</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Repository</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRepository(final MIRepository object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Dependency</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDependency(final MIDependency object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Repository Connector</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Repository Connector</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseRepositoryConnector(final MIRepositoryConnector object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Display</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Display</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDisplay(final MIDisplay object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>View</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>View</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseView(final MIView object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Display Connector</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Display Connector</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDisplayConnector(final MIDisplayConnector object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Analysis Component</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Analysis Component</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnalysisComponent(final MIAnalysisComponent object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // AnalysisMetaModelSwitch
