/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IConfigurable;
import kieker.analysis.model.analysisMetaModel.IProject;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Project</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.Project#getConfigurables <em>Configurables</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class Project extends Configurable implements IProject {
	/**
	 * The cached value of the '{@link #getConfigurables() <em>Configurables</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getConfigurables()
	 * @generated
	 * @ordered
	 */
	protected EList<IConfigurable> configurables;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Project() {
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
		return IAnalysisMetaModelPackage.Literals.PROJECT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<IConfigurable> getConfigurables() {
		if (this.configurables == null) {
			this.configurables = new EObjectContainmentEList<IConfigurable>(IConfigurable.class, this, IAnalysisMetaModelPackage.PROJECT__CONFIGURABLES);
		}
		return this.configurables;
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
		case IAnalysisMetaModelPackage.PROJECT__CONFIGURABLES:
			return ((InternalEList<?>) this.getConfigurables()).basicRemove(otherEnd, msgs);
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
		case IAnalysisMetaModelPackage.PROJECT__CONFIGURABLES:
			return this.getConfigurables();
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
		case IAnalysisMetaModelPackage.PROJECT__CONFIGURABLES:
			this.getConfigurables().clear();
			this.getConfigurables().addAll((Collection<? extends IConfigurable>) newValue);
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
		case IAnalysisMetaModelPackage.PROJECT__CONFIGURABLES:
			this.getConfigurables().clear();
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
		case IAnalysisMetaModelPackage.PROJECT__CONFIGURABLES:
			return (this.configurables != null) && !this.configurables.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // Project
