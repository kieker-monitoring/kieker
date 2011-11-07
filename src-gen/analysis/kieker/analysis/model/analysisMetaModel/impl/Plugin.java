/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IOutputPort;
import kieker.analysis.model.analysisMetaModel.IPlugin;

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
 * An implementation of the model object '<em><b>Plugin</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.Plugin#getOutputPorts <em>Output Ports</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.Plugin#getClassname <em>Classname</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public abstract class Plugin extends Configurable implements IPlugin {
	/**
	 * The cached value of the '{@link #getOutputPorts() <em>Output Ports</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOutputPorts()
	 * @generated
	 * @ordered
	 */
	protected EList<IOutputPort> outputPorts;

	/**
	 * The cached value of the '{@link #getClassname() <em>Classname</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getClassname()
	 * @generated
	 * @ordered
	 */
	protected Class classname;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Plugin() {
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
		return IAnalysisMetaModelPackage.Literals.PLUGIN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<IOutputPort> getOutputPorts() {
		if (this.outputPorts == null) {
			this.outputPorts = new EObjectContainmentEList<IOutputPort>(IOutputPort.class, this, IAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS);
		}
		return this.outputPorts;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public Class getClassname() {
		return this.classname;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setClassname(final Class newClassname) {
		final Class oldClassname = this.classname;
		this.classname = newClassname;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, IAnalysisMetaModelPackage.PLUGIN__CLASSNAME, oldClassname, this.classname));
		}
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
		case IAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
			return ((InternalEList<?>) this.getOutputPorts()).basicRemove(otherEnd, msgs);
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
		case IAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
			return this.getOutputPorts();
		case IAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
			return this.getClassname();
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
		case IAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
			this.getOutputPorts().clear();
			this.getOutputPorts().addAll((Collection<? extends IOutputPort>) newValue);
			return;
		case IAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
			this.setClassname((Class) newValue);
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
		case IAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
			this.getOutputPorts().clear();
			return;
		case IAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
			this.setClassname((Class) null);
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
		case IAnalysisMetaModelPackage.PLUGIN__OUTPUT_PORTS:
			return (this.outputPorts != null) && !this.outputPorts.isEmpty();
		case IAnalysisMetaModelPackage.PLUGIN__CLASSNAME:
			return this.classname != null;
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
		result.append(" (classname: ");
		result.append(this.classname);
		result.append(')');
		return result.toString();
	}

} // Plugin
