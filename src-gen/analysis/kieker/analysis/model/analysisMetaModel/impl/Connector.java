/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import kieker.analysis.model.analysisMetaModel.IAnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.IConnector;
import kieker.analysis.model.analysisMetaModel.IInputPort;
import kieker.analysis.model.analysisMetaModel.IOutputPort;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connector</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * <ul>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.Connector#getDstInputPort <em>Dst Input Port</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.Connector#getSicOutputPort <em>Sic Output Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class Connector extends EObjectImpl implements IConnector {
	/**
	 * The cached value of the '{@link #getDstInputPort() <em>Dst Input Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDstInputPort()
	 * @generated
	 * @ordered
	 */
	protected IInputPort dstInputPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected Connector() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return IAnalysisMetaModelPackage.Literals.CONNECTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IInputPort getDstInputPort() {
		if (dstInputPort != null && dstInputPort.eIsProxy()) {
			InternalEObject oldDstInputPort = (InternalEObject)dstInputPort;
			dstInputPort = (IInputPort)eResolveProxy(oldDstInputPort);
			if (dstInputPort != oldDstInputPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, oldDstInputPort, dstInputPort));
			}
		}
		return dstInputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IInputPort basicGetDstInputPort() {
		return dstInputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDstInputPort(IInputPort newDstInputPort, NotificationChain msgs) {
		IInputPort oldDstInputPort = dstInputPort;
		dstInputPort = newDstInputPort;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, oldDstInputPort, newDstInputPort);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDstInputPort(IInputPort newDstInputPort) {
		if (newDstInputPort != dstInputPort) {
			NotificationChain msgs = null;
			if (dstInputPort != null)
				msgs = ((InternalEObject)dstInputPort).eInverseRemove(this, IAnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, IInputPort.class, msgs);
			if (newDstInputPort != null)
				msgs = ((InternalEObject)newDstInputPort).eInverseAdd(this, IAnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, IInputPort.class, msgs);
			msgs = basicSetDstInputPort(newDstInputPort, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, newDstInputPort, newDstInputPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public IOutputPort getSicOutputPort() {
		if (eContainerFeatureID() != IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT) return null;
		return (IOutputPort)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSicOutputPort(IOutputPort newSicOutputPort, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSicOutputPort, IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSicOutputPort(IOutputPort newSicOutputPort) {
		if (newSicOutputPort != eInternalContainer() || (eContainerFeatureID() != IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT && newSicOutputPort != null)) {
			if (EcoreUtil.isAncestor(this, newSicOutputPort))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSicOutputPort != null)
				msgs = ((InternalEObject)newSicOutputPort).eInverseAdd(this, IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR, IOutputPort.class, msgs);
			msgs = basicSetSicOutputPort(newSicOutputPort, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, newSicOutputPort, newSicOutputPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				if (dstInputPort != null)
					msgs = ((InternalEObject)dstInputPort).eInverseRemove(this, IAnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, IInputPort.class, msgs);
				return basicSetDstInputPort((IInputPort)otherEnd, msgs);
			case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSicOutputPort((IOutputPort)otherEnd, msgs);
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
			case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				return basicSetDstInputPort(null, msgs);
			case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				return basicSetSicOutputPort(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				return eInternalContainer().eInverseRemove(this, IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR, IOutputPort.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				if (resolve) return getDstInputPort();
				return basicGetDstInputPort();
			case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				return getSicOutputPort();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				setDstInputPort((IInputPort)newValue);
				return;
			case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				setSicOutputPort((IOutputPort)newValue);
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
			case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				setDstInputPort((IInputPort)null);
				return;
			case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				setSicOutputPort((IOutputPort)null);
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
			case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				return dstInputPort != null;
			case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				return getSicOutputPort() != null;
		}
		return super.eIsSet(featureID);
	}

} //Connector
