/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.util;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IAnalysisPlugin;
import kieker.analysis.model.analysisMetaModel.IConfigurable;
import kieker.analysis.model.analysisMetaModel.IConnector;
import kieker.analysis.model.analysisMetaModel.IInputPort;
import kieker.analysis.model.analysisMetaModel.IOutputPort;
import kieker.analysis.model.analysisMetaModel.IPlugin;
import kieker.analysis.model.analysisMetaModel.IPort;
import kieker.analysis.model.analysisMetaModel.IProject;
import kieker.analysis.model.analysisMetaModel.IProperty;
import kieker.analysis.model.analysisMetaModel.IReader;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)} to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * 
 * @see kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage
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
	protected static IAnalysisMetaModelPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public AnalysisMetaModelSwitch() {
		if (AnalysisMetaModelSwitch.modelPackage == null) {
			AnalysisMetaModelSwitch.modelPackage = IAnalysisMetaModelPackage.eINSTANCE;
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
		return ePackage == AnalysisMetaModelSwitch.modelPackage;
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
		case IAnalysisMetaModelPackage.PROJECT: {
			final IProject project = (IProject) theEObject;
			T result = this.caseProject(project);
			if (result == null) {
				result = this.caseConfigurable(project);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.PLUGIN: {
			final IPlugin plugin = (IPlugin) theEObject;
			T result = this.casePlugin(plugin);
			if (result == null) {
				result = this.caseConfigurable(plugin);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.CONNECTOR: {
			final IConnector connector = (IConnector) theEObject;
			T result = this.caseConnector(connector);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.CONFIGURABLE: {
			final IConfigurable configurable = (IConfigurable) theEObject;
			T result = this.caseConfigurable(configurable);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.PORT: {
			final IPort port = (IPort) theEObject;
			T result = this.casePort(port);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.INPUT_PORT: {
			final IInputPort inputPort = (IInputPort) theEObject;
			T result = this.caseInputPort(inputPort);
			if (result == null) {
				result = this.casePort(inputPort);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.OUTPUT_PORT: {
			final IOutputPort outputPort = (IOutputPort) theEObject;
			T result = this.caseOutputPort(outputPort);
			if (result == null) {
				result = this.casePort(outputPort);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.PROPERTY: {
			final IProperty property = (IProperty) theEObject;
			T result = this.caseProperty(property);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.ANALYSIS_PLUGIN: {
			final IAnalysisPlugin analysisPlugin = (IAnalysisPlugin) theEObject;
			T result = this.caseAnalysisPlugin(analysisPlugin);
			if (result == null) {
				result = this.casePlugin(analysisPlugin);
			}
			if (result == null) {
				result = this.caseConfigurable(analysisPlugin);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case IAnalysisMetaModelPackage.READER: {
			final IReader reader = (IReader) theEObject;
			T result = this.caseReader(reader);
			if (result == null) {
				result = this.casePlugin(reader);
			}
			if (result == null) {
				result = this.caseConfigurable(reader);
			}
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
	public T caseProject(final IProject object) {
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
	public T casePlugin(final IPlugin object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Connector</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Connector</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConnector(final IConnector object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Configurable</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Configurable</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConfigurable(final IConfigurable object) {
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
	public T casePort(final IPort object) {
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
	public T caseInputPort(final IInputPort object) {
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
	public T caseOutputPort(final IOutputPort object) {
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
	public T caseProperty(final IProperty object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Analysis Plugin</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * 
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Analysis Plugin</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseAnalysisPlugin(final IAnalysisPlugin object) {
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
	public T caseReader(final IReader object) {
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
