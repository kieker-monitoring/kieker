/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIView;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getPlugins <em>Plugins</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getDependencies <em>Dependencies</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getViews <em>Views</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getProperties <em>Properties</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class MProject extends EObjectImpl implements MIProject {
	/**
	 * The cached value of the '{@link #getPlugins() <em>Plugins</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPlugins()
	 * @generated
	 * @ordered
	 */
	protected EList<MIPlugin> plugins;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositories()
	 * @generated
	 * @ordered
	 */
	protected EList<MIRepository> repositories;

	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<MIDependency> dependencies;

	/**
	 * The cached value of the '{@link #getViews() <em>Views</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getViews()
	 * @generated
	 * @ordered
	 */
	protected EList<MIView> views;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<MIProperty> properties;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MProject() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MIAnalysisMetaModelPackage.Literals.PROJECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIPlugin> getPlugins() {
		if (plugins == null) {
			plugins = new EObjectContainmentEList<MIPlugin>(MIPlugin.class, this, MIAnalysisMetaModelPackage.PROJECT__PLUGINS);
		}
		return plugins;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.PROJECT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIRepository> getRepositories() {
		if (repositories == null) {
			repositories = new EObjectContainmentEList<MIRepository>(MIRepository.class, this, MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES);
		}
		return repositories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIDependency> getDependencies() {
		if (dependencies == null) {
			dependencies = new EObjectContainmentEList<MIDependency>(MIDependency.class, this, MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES);
		}
		return dependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIView> getViews() {
		if (views == null) {
			views = new EObjectContainmentEList<MIView>(MIView.class, this, MIAnalysisMetaModelPackage.PROJECT__VIEWS);
		}
		return views;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIProperty> getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList<MIProperty>(MIProperty.class, this, MIAnalysisMetaModelPackage.PROJECT__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
				return ((InternalEList<?>)getPlugins()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
				return ((InternalEList<?>)getRepositories()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
				return ((InternalEList<?>)getDependencies()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
				return ((InternalEList<?>)getViews()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
				return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
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
			case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
				return getPlugins();
			case MIAnalysisMetaModelPackage.PROJECT__NAME:
				return getName();
			case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
				return getRepositories();
			case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
				return getDependencies();
			case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
				return getViews();
			case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
				return getProperties();
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
			case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
				getPlugins().clear();
				getPlugins().addAll((Collection<? extends MIPlugin>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PROJECT__NAME:
				setName((String)newValue);
				return;
			case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
				getRepositories().clear();
				getRepositories().addAll((Collection<? extends MIRepository>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
				getDependencies().clear();
				getDependencies().addAll((Collection<? extends MIDependency>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
				getViews().clear();
				getViews().addAll((Collection<? extends MIView>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends MIProperty>)newValue);
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
			case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
				getPlugins().clear();
				return;
			case MIAnalysisMetaModelPackage.PROJECT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
				getRepositories().clear();
				return;
			case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
				getDependencies().clear();
				return;
			case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
				getViews().clear();
				return;
			case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
				getProperties().clear();
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
			case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
				return plugins != null && !plugins.isEmpty();
			case MIAnalysisMetaModelPackage.PROJECT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
				return repositories != null && !repositories.isEmpty();
			case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
				return dependencies != null && !dependencies.isEmpty();
			case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
				return views != null && !views.isEmpty();
			case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
				return properties != null && !properties.isEmpty();
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
		result.append(" (name: ");
		result.append(name);
		result.append(')');
		return result.toString();
	}

} //MProject
