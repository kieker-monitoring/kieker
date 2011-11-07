/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import java.util.Collection;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IConnector;
import kieker.analysis.model.analysisMetaModel.IOutputPort;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Output Port</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.OutputPort#getOutConnector <em>Out Connector</em>}</li>
 * </ul>
 * </p>
 * 
 * @generated
 */
public class OutputPort extends Port implements IOutputPort {
	/**
	 * The cached value of the '{@link #getOutConnector() <em>Out Connector</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see #getOutConnector()
	 * @generated
	 * @ordered
	 */
	protected EList<IConnector> outConnector;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected OutputPort() {
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
		return IAnalysisMetaModelPackage.Literals.OUTPUT_PORT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public EList<IConnector> getOutConnector() {
		if (this.outConnector == null) {
			this.outConnector = new EObjectContainmentWithInverseEList<IConnector>(IConnector.class, this, IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR,
					IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT);
		}
		return this.outConnector;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(final InternalEObject otherEnd, final int featureID, final NotificationChain msgs) {
		switch (featureID) {
		case IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getOutConnector()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
		case IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR:
			return ((InternalEList<?>) this.getOutConnector()).basicRemove(otherEnd, msgs);
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
		case IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR:
			return this.getOutConnector();
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
		case IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR:
			this.getOutConnector().clear();
			this.getOutConnector().addAll((Collection<? extends IConnector>) newValue);
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
		case IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR:
			this.getOutConnector().clear();
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
		case IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR:
			return (this.outConnector != null) && !this.outConnector.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // OutputPort
