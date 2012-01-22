/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import kieker.analysis.model.analysisMetaModel.MIAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.MIOutputPort;
import kieker.analysis.model.analysisMetaModel.MIPlugin;
import kieker.analysis.model.analysisMetaModel.MIProperty;
import kieker.analysis.model.analysisMetaModel.MIRepository;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Plugin</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getName <em>Name</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getClassname <em>Classname</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getProperties <em>Properties</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getRepositories <em>Repositories</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.MPlugin#getOutputPorts <em>Output Ports</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public abstract class MPlugin extends EObjectImpl implements MIPlugin {
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
	 * The default value of the '{@link #getClassname() <em>Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClassname()
	 * @generated
	 * @ordered
	 */
	protected static final String CLASSNAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getClassname() <em>Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getClassname()
	 * @generated
	 * @ordered
	 */
	protected String classname = CLASSNAME_EDEFAULT;

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
	 * The cached value of the '{@link #getRepositories() <em>Repositories</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRepositories()
	 * @generated
	 * @ordered
	 */
	protected EList<MIRepository> repositories;

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
			eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.PLUGIN__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getClassname() {
		return classname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setClassname(String newClassname) {
		String oldClassname = classname;
		classname = newClassname;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, MIAnalysisMetaModelPackage.PLUGIN__CLASSNAME, oldClassname, classname));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIProperty> getProperties() {
		if (properties == null) {
			properties = new EObjectContainmentEList<MIProperty>(MIProperty.class, this, MIAnalysisMetaModelPackage.PLUGIN__PROPERTIES);
		}
		return properties;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MIRepository> getRepositories() {
		if (repositories == null) {
			repositories = new EObjectResolvingEList<MIRepository>(MIRepository.class, this, MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES);
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
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getOutputPorts()).basicAdd(otherEnd, msgs);
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
			case MIAnalysisMetaModelPackage.PLUGIN__PROPERTIES:
				return ((InternalEList<?>)getProperties()).basicRemove(otherEnd, msgs);
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return ((InternalEList<?>)getOutputPorts()).basicRemove(otherEnd, msgs);
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
			case MIAnalysisMetaModelPackage.PLUGIN__NAME:
				return getName();
			case MIAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
				return getClassname();
			case MIAnalysisMetaModelPackage.PLUGIN__PROPERTIES:
				return getProperties();
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				return getRepositories();
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return getOutputPorts();
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
			case MIAnalysisMetaModelPackage.PLUGIN__NAME:
				setName((String)newValue);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
				setClassname((String)newValue);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__PROPERTIES:
				getProperties().clear();
				getProperties().addAll((Collection<? extends MIProperty>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				getRepositories().clear();
				getRepositories().addAll((Collection<? extends MIRepository>)newValue);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				getOutputPorts().clear();
				getOutputPorts().addAll((Collection<? extends MIOutputPort>)newValue);
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
			case MIAnalysisMetaModelPackage.PLUGIN__NAME:
				setName(NAME_EDEFAULT);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
				setClassname(CLASSNAME_EDEFAULT);
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__PROPERTIES:
				getProperties().clear();
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				getRepositories().clear();
				return;
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				getOutputPorts().clear();
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
			case MIAnalysisMetaModelPackage.PLUGIN__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case MIAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
				return CLASSNAME_EDEFAULT == null ? classname != null : !CLASSNAME_EDEFAULT.equals(classname);
			case MIAnalysisMetaModelPackage.PLUGIN__PROPERTIES:
				return properties != null && !properties.isEmpty();
			case MIAnalysisMetaModelPackage.PLUGIN__REPOSITORIES:
				return repositories != null && !repositories.isEmpty();
			case MIAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
				return outputPorts != null && !outputPorts.isEmpty();
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
		result.append(", classname: ");
		result.append(classname);
		result.append(')');
		return result.toString();
	}

} //MPlugin
