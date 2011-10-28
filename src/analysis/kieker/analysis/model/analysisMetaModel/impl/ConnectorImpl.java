/**
 * <copyright>
 * </copyright>
 *
 * $Id$
 */
package kieker.analysis.model.analysisMetaModel.impl;

import kieker.analysis.model.analysisMetaModel.AnalysisMetaModelPackage;
import kieker.analysis.model.analysisMetaModel.Connector;
import kieker.analysis.model.analysisMetaModel.InputPort;
import kieker.analysis.model.analysisMetaModel.OutputPort;

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
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.ConnectorImpl#getDstInputPort <em>Dst Input Port</em>}</li>
 *   <li>{@link kieker.analysis.model.analysisMetaModel.impl.ConnectorImpl#getSicOutputPort <em>Sic Output Port</em>}</li>
 * </ul>
 * </p>
 *
 * @generated
 */
public class ConnectorImpl extends EObjectImpl implements Connector {
	/**
	 * The cached value of the '{@link #getDstInputPort() <em>Dst Input Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDstInputPort()
	 * @generated
	 * @ordered
	 */
	protected InputPort dstInputPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConnectorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return AnalysisMetaModelPackage.Literals.CONNECTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputPort getDstInputPort() {
		if (dstInputPort != null && dstInputPort.eIsProxy()) {
			InternalEObject oldDstInputPort = (InternalEObject)dstInputPort;
			dstInputPort = (InputPort)eResolveProxy(oldDstInputPort);
			if (dstInputPort != oldDstInputPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, oldDstInputPort, dstInputPort));
			}
		}
		return dstInputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public InputPort basicGetDstInputPort() {
		return dstInputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDstInputPort(InputPort newDstInputPort, NotificationChain msgs) {
		InputPort oldDstInputPort = dstInputPort;
		dstInputPort = newDstInputPort;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, oldDstInputPort, newDstInputPort);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDstInputPort(InputPort newDstInputPort) {
		if (newDstInputPort != dstInputPort) {
			NotificationChain msgs = null;
			if (dstInputPort != null)
				msgs = ((InternalEObject)dstInputPort).eInverseRemove(this, AnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, InputPort.class, msgs);
			if (newDstInputPort != null)
				msgs = ((InternalEObject)newDstInputPort).eInverseAdd(this, AnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, InputPort.class, msgs);
			msgs = basicSetDstInputPort(newDstInputPort, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, newDstInputPort, newDstInputPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputPort getSicOutputPort() {
		if (eContainerFeatureID() != AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT) return null;
		return (OutputPort)eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSicOutputPort(OutputPort newSicOutputPort, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newSicOutputPort, AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSicOutputPort(OutputPort newSicOutputPort) {
		if (newSicOutputPort != eInternalContainer() || (eContainerFeatureID() != AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT && newSicOutputPort != null)) {
			if (EcoreUtil.isAncestor(this, newSicOutputPort))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newSicOutputPort != null)
				msgs = ((InternalEObject)newSicOutputPort).eInverseAdd(this, AnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR, OutputPort.class, msgs);
			msgs = basicSetSicOutputPort(newSicOutputPort, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, newSicOutputPort, newSicOutputPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				if (dstInputPort != null)
					msgs = ((InternalEObject)dstInputPort).eInverseRemove(this, AnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, InputPort.class, msgs);
				return basicSetDstInputPort((InputPort)otherEnd, msgs);
			case AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetSicOutputPort((OutputPort)otherEnd, msgs);
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
			case AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				return basicSetDstInputPort(null, msgs);
			case AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
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
			case AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				return eInternalContainer().eInverseRemove(this, AnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR, OutputPort.class, msgs);
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
			case AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				if (resolve) return getDstInputPort();
				return basicGetDstInputPort();
			case AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
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
			case AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				setDstInputPort((InputPort)newValue);
				return;
			case AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				setSicOutputPort((OutputPort)newValue);
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
			case AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				setDstInputPort((InputPort)null);
				return;
			case AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				setSicOutputPort((OutputPort)null);
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
			case AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
				return dstInputPort != null;
			case AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
				return getSicOutputPort() != null;
		}
		return super.eIsSet(featureID);
	}

} //ConnectorImpl
