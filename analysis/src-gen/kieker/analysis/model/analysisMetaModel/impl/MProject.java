/**
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIDependency;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProject;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIRepository;
import kieker.analysis.model.analysisMetaModel.MIView;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getPlugins <em>Plugins</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getName <em>Name</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getRepositories <em>Repositories</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getDependencies <em>Dependencies</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getViews <em>Views</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.MProject#getProperties <em>Properties</em>}</li>
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
	 *
	 * @see #getPlugins()
	 * @generated
	 * @ordered
	 */
	protected EList<MIPlugin> plugins;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getRepositories()
	 * @generated
	 * @ordered
	 */
	protected EList<MIRepository> repositories;

	/**
	 * The cached value of the '{@link #getDependencies() <em>Dependencies</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDependencies()
	 * @generated
	 * @ordered
	 */
	protected EList<MIDependency> dependencies;

	/**
	 * The cached value of the '{@link #getViews() <em>Views</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getViews()
	 * @generated
	 * @ordered
	 */
	protected EList<MIView> views;

	/**
	 * The cached value of the '{@link #getProperties() <em>Properties</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getProperties()
	 * @generated
	 * @ordered
	 */
	protected EList<MIProperty> properties;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MProject() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return MIAnalysisMetaModelPackage.Literals.PROJECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MIPlugin> getPlugins() {
		if (this.plugins == null) {
			this.plugins = new EObjectContainmentEList<>(MIPlugin.class, this, MIAnalysisMetaModelPackage.PROJECT__PLUGINS);
		}
		return this.plugins;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setName(final String newName) {
		final String oldName = this.name;
		this.name = newName;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.PROJECT__NAME, oldName, this.name));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MIRepository> getRepositories() {
		if (this.repositories == null) {
			this.repositories = new EObjectContainmentEList<>(MIRepository.class, this, MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES);
		}
		return this.repositories;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MIDependency> getDependencies() {
		if (this.dependencies == null) {
			this.dependencies = new EObjectContainmentEList<>(MIDependency.class, this, MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES);
		}
		return this.dependencies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MIView> getViews() {
		if (this.views == null) {
			this.views = new EObjectContainmentEList<>(MIView.class, this, MIAnalysisMetaModelPackage.PROJECT__VIEWS);
		}
		return this.views;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MIProperty> getProperties() {
		if (this.properties == null) {
			this.properties = new EObjectContainmentEList<>(MIProperty.class, this, MIAnalysisMetaModelPackage.PROJECT__PROPERTIES);
		}
		return this.properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
			return ((InternalEList<?>) this.getPlugins()).basicRemove(otherEnd, msgs);
		case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
			return ((InternalEList<?>) this.getRepositories()).basicRemove(otherEnd, msgs);
		case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
			return ((InternalEList<?>) this.getDependencies()).basicRemove(otherEnd, msgs);
		case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
			return ((InternalEList<?>) this.getViews()).basicRemove(otherEnd, msgs);
		case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
			return ((InternalEList<?>) this.getProperties()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
			return this.getPlugins();
		case MIAnalysisMetaModelPackage.PROJECT__NAME:
			return this.getName();
		case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
			return this.getRepositories();
		case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
			return this.getDependencies();
		case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
			return this.getViews();
		case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
			return this.getProperties();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
			this.getPlugins().clear();
			this.getPlugins().addAll((Collection<? extends MIPlugin>) newValue);
			return;
		case MIAnalysisMetaModelPackage.PROJECT__NAME:
			this.setName((String) newValue);
			return;
		case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
			this.getRepositories().clear();
			this.getRepositories().addAll((Collection<? extends MIRepository>) newValue);
			return;
		case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
			this.getDependencies().clear();
			this.getDependencies().addAll((Collection<? extends MIDependency>) newValue);
			return;
		case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
			this.getViews().clear();
			this.getViews().addAll((Collection<? extends MIView>) newValue);
			return;
		case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
			this.getProperties().clear();
			this.getProperties().addAll((Collection<? extends MIProperty>) newValue);
			return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void eUnset(final int featureID) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
			this.getPlugins().clear();
			return;
		case MIAnalysisMetaModelPackage.PROJECT__NAME:
			this.setName(NAME_EDEFAULT);
			return;
		case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
			this.getRepositories().clear();
			return;
		case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
			this.getDependencies().clear();
			return;
		case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
			this.getViews().clear();
			return;
		case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
			this.getProperties().clear();
			return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean eIsSet(final int featureID) {
		switch (featureID) {
		case MIAnalysisMetaModelPackage.PROJECT__PLUGINS:
			return (this.plugins != null) && !this.plugins.isEmpty();
		case MIAnalysisMetaModelPackage.PROJECT__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
		case MIAnalysisMetaModelPackage.PROJECT__REPOSITORIES:
			return (this.repositories != null) && !this.repositories.isEmpty();
		case MIAnalysisMetaModelPackage.PROJECT__DEPENDENCIES:
			return (this.dependencies != null) && !this.dependencies.isEmpty();
		case MIAnalysisMetaModelPackage.PROJECT__VIEWS:
			return (this.views != null) && !this.views.isEmpty();
		case MIAnalysisMetaModelPackage.PROJECT__PROPERTIES:
			return (this.properties != null) && !this.properties.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		if (this.eIsProxy()) {
			return super.toString();
		}

		final StringBuffer result = new StringBuffer(super.toString());
		result.append(" (name: ");
		result.append(this.name);
		result.append(')');
		return result.toString();
	}

} // MProject
