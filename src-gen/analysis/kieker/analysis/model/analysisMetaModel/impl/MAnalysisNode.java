/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIAnalysisNode;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIRepository;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Analysis Node</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MAnalysisNode#getContainedPlugins <em>Contained Plugins</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MAnalysisNode#getContainedRepositories <em>Contained Repositories</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MAnalysisNode#getNodeName <em>Node Name</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MAnalysisNode extends MFilter implements MIAnalysisNode {
	/**
	 * The cached value of the '{@link #getContainedPlugins() <em>Contained Plugins</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedPlugins()
	 * @generated
	 * @ordered
	 */
	protected EList<MIPlugin> containedPlugins;

	/**
	 * The cached value of the '{@link #getContainedRepositories() <em>Contained Repositories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContainedRepositories()
	 * @generated
	 * @ordered
	 */
	protected EList<MIRepository> containedRepositories;

	/**
	 * The default value of the '{@link #getNodeName() <em>Node Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNodeName()
	 * @generated
	 * @ordered
	 */
	protected static final String NODE_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNodeName() <em>Node Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNodeName()
	 * @generated
	 * @ordered
	 */
	protected String nodeName = NODE_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MAnalysisNode() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MIAnalysisMetaModelPackage.Literals.ANALYSIS_NODE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIPlugin> getContainedPlugins() {
		if (containedPlugins == null) {
			containedPlugins = new EObjectContainmentEList<MIPlugin>(MIPlugin.class, this, MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_PLUGINS);
		}
		return containedPlugins;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIRepository> getContainedRepositories() {
		if (containedRepositories == null) {
			containedRepositories = new EObjectContainmentEList<MIRepository>(MIRepository.class, this, MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_REPOSITORIES);
		}
		return containedRepositories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNodeName() {
		return nodeName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNodeName(String newNodeName) {
		String oldNodeName = nodeName;
		nodeName = newNodeName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.ANALYSIS_NODE__NODE_NAME, oldNodeName, nodeName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_PLUGINS:
				return ((InternalEList<?>)getContainedPlugins()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_REPOSITORIES:
				return ((InternalEList<?>)getContainedRepositories()).basicRemove(otherEnd, msgs);
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
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_PLUGINS:
				return getContainedPlugins();
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_REPOSITORIES:
				return getContainedRepositories();
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__NODE_NAME:
				return getNodeName();
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
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_PLUGINS:
				getContainedPlugins().clear();
				getContainedPlugins().addAll((Collection<? extends MIPlugin>)newValue);
				return;
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_REPOSITORIES:
				getContainedRepositories().clear();
				getContainedRepositories().addAll((Collection<? extends MIRepository>)newValue);
				return;
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__NODE_NAME:
				setNodeName((String)newValue);
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
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_PLUGINS:
				getContainedPlugins().clear();
				return;
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_REPOSITORIES:
				getContainedRepositories().clear();
				return;
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__NODE_NAME:
				setNodeName(NODE_NAME_EDEFAULT);
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
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_PLUGINS:
				return containedPlugins != null && !containedPlugins.isEmpty();
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__CONTAINED_REPOSITORIES:
				return containedRepositories != null && !containedRepositories.isEmpty();
			case MIAnalysisMetaModelPackage.ANALYSIS_NODE__NODE_NAME:
				return NODE_NAME_EDEFAULT == null ? nodeName != null : !NODE_NAME_EDEFAULT.equals(nodeName);
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuffer result = new StringBuffer(super.toString());
		result.append(" (nodeName: ");
		result.append(nodeName);
		result.append(')');
		return result.toString();
	}

} //MAnalysisNode
