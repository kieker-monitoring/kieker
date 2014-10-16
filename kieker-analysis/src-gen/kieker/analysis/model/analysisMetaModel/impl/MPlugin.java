/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDisplay;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIRepositoryConnector;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Plugin</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getOutputPorts <em>Output Ports</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getDisplays <em>Displays</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class MPlugin extends MAnalysisComponent implements MIPlugin {
	/**
	 * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositories()
	 * @generated
	 * @ordered
	 */
	protected EList<MIRepositoryConnector> repositories;

	/**
	 * The cached value of the '{@link #getOutputPorts() <em>Output Ports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOutputPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<MIOutputPort> outputPorts;

	/**
	 * The cached value of the '{@link #getDisplays() <em>Displays</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisplays()
	 * @generated
	 * @ordered
	 */
	protected EList<MIDisplay> displays;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MPlugin() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MIAnalysisMetaModelPackage.Literals.PLUGIN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIRepositoryConnector> getRepositories() {
		if (repositories == null) {
			repositories = new EObjectContainmentEList<MIRepositoryConnector>(MIRepositoryConnector.class, this, MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES);
		}
		return repositories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIOutputPort> getOutputPorts() {
		if (outputPorts == null) {
			outputPorts = new EObjectContainmentWithInverseEList<MIOutputPort>(MIOutputPort.class, this, MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS, MIAnalysisMetaModelPackage.OUTPUT_PORT__PARENT);
		}
		return outputPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIDisplay> getDisplays() {
		if (displays == null) {
			displays = new EObjectContainmentWithInverseEList<MIDisplay>(MIDisplay.class, this, MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS, MIAnalysisMetaModelPackage.DISPLAY__PARENT);
		}
		return displays;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getOutputPorts()).basicAdd(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getDisplays()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				return ((InternalEList<?>)getRepositories()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return ((InternalEList<?>)getOutputPorts()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS:
				return ((InternalEList<?>)getDisplays()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				return getRepositories();
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return getOutputPorts();
			case MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS:
				return getDisplays();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				getRepositories().clear();
				getRepositories().addAll((Collection<? extends MIRepositoryConnector>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				getOutputPorts().clear();
				getOutputPorts().addAll((Collection<? extends MIOutputPort>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS:
				getDisplays().clear();
				getDisplays().addAll((Collection<? extends MIDisplay>)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				getRepositories().clear();
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				getOutputPorts().clear();
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS:
				getDisplays().clear();
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				return repositories != null && !repositories.isEmpty();
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return outputPorts != null && !outputPorts.isEmpty();
			case MIAnalysisMetaModelPackage.PLUGIN__DISPLAYS:
				return displays != null && !displays.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MPlugin
