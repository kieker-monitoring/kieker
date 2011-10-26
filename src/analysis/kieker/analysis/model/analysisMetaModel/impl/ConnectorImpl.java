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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.EObjectImpl;

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
	 * The cached value of the '{@link #getSicOutputPort() <em>Sic Output Port</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSicOutputPort()
	 * @generated
	 * @ordered
	 */
	protected OutputPort sicOutputPort;

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
	public void setDstInputPort(InputPort newDstInputPort) {
		InputPort oldDstInputPort = dstInputPort;
		dstInputPort = newDstInputPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, oldDstInputPort, dstInputPort));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputPort getSicOutputPort() {
		if (sicOutputPort != null && sicOutputPort.eIsProxy()) {
			InternalEObject oldSicOutputPort = (InternalEObject)sicOutputPort;
			sicOutputPort = (OutputPort)eResolveProxy(oldSicOutputPort);
			if (sicOutputPort != oldSicOutputPort) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, oldSicOutputPort, sicOutputPort));
			}
		}
		return sicOutputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OutputPort basicGetSicOutputPort() {
		return sicOutputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setSicOutputPort(OutputPort newSicOutputPort) {
		OutputPort oldSicOutputPort = sicOutputPort;
		sicOutputPort = newSicOutputPort;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, AnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, oldSicOutputPort, sicOutputPort));
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
				if (resolve) return getSicOutputPort();
				return basicGetSicOutputPort();
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
				return sicOutputPort != null;
		}
		return super.eIsSet(featureID);
	}

} //ConnectorImpl
