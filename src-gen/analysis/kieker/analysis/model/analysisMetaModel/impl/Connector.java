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
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.Connector#getDstInputPort <em>Dst Input Port</em>}</li>
 * <li>{@link kieker.analysis.model.analysisMetaModel.impl.Connector#getSicOutputPort <em>Sic Output Port</em>}</li>
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
	 * 
	 * @see #getDstInputPort()
	 * @generated
	 * @ordered
	 */
	protected IInputPort dstInputPort;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	protected Connector() {
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
		return IAnalysisMetaModelPackage.Literals.CONNECTOR;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IInputPort getDstInputPort() {
		if ((this.dstInputPort != null) && this.dstInputPort.eIsProxy()) {
			final InternalEObject oldDstInputPort = (InternalEObject) this.dstInputPort;
			this.dstInputPort = (IInputPort) this.eResolveProxy(oldDstInputPort);
			if (this.dstInputPort != oldDstInputPort) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, oldDstInputPort,
							this.dstInputPort));
				}
			}
		}
		return this.dstInputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public IInputPort basicGetDstInputPort() {
		return this.dstInputPort;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetDstInputPort(final IInputPort newDstInputPort, NotificationChain msgs) {
		final IInputPort oldDstInputPort = this.dstInputPort;
		this.dstInputPort = newDstInputPort;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT,
					oldDstInputPort, newDstInputPort);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setDstInputPort(final IInputPort newDstInputPort) {
		if (newDstInputPort != this.dstInputPort) {
			NotificationChain msgs = null;
			if (this.dstInputPort != null) {
				msgs = ((InternalEObject) this.dstInputPort).eInverseRemove(this, IAnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, IInputPort.class, msgs);
			}
			if (newDstInputPort != null) {
				msgs = ((InternalEObject) newDstInputPort).eInverseAdd(this, IAnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, IInputPort.class, msgs);
			}
			msgs = this.basicSetDstInputPort(newDstInputPort, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		}
		else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT, newDstInputPort, newDstInputPort));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public IOutputPort getSicOutputPort() {
		if (this.eContainerFeatureID() != IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT) {
			return null;
		}
		return (IOutputPort) this.eContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	public NotificationChain basicSetSicOutputPort(final IOutputPort newSicOutputPort, NotificationChain msgs) {
		msgs = this.eBasicSetContainer((InternalEObject) newSicOutputPort, IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void setSicOutputPort(final IOutputPort newSicOutputPort) {
		if ((newSicOutputPort != this.eInternalContainer())
				|| ((this.eContainerFeatureID() != IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT) && (newSicOutputPort != null))) {
			if (EcoreUtil.isAncestor(this, newSicOutputPort)) {
				throw new IllegalArgumentException("Recursive containment not allowed for " + this.toString());
			}
			NotificationChain msgs = null;
			if (this.eInternalContainer() != null) {
				msgs = this.eBasicRemoveFromContainer(msgs);
			}
			if (newSicOutputPort != null) {
				msgs = ((InternalEObject) newSicOutputPort).eInverseAdd(this, IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR, IOutputPort.class, msgs);
			}
			msgs = this.basicSetSicOutputPort(newSicOutputPort, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		}
		else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT, newSicOutputPort, newSicOutputPort));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(final InternalEObject otherEnd, final int featureID, NotificationChain msgs) {
		switch (featureID) {
		case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
			if (this.dstInputPort != null) {
				msgs = ((InternalEObject) this.dstInputPort).eInverseRemove(this, IAnalysisMetaModelPackage.INPUT_PORT__IN_CONNECTOR, IInputPort.class, msgs);
			}
			return this.basicSetDstInputPort((IInputPort) otherEnd, msgs);
		case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
			if (this.eInternalContainer() != null) {
				msgs = this.eBasicRemoveFromContainer(msgs);
			}
			return this.basicSetSicOutputPort((IOutputPort) otherEnd, msgs);
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
		case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
			return this.basicSetDstInputPort(null, msgs);
		case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
			return this.basicSetSicOutputPort(null, msgs);
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
	public NotificationChain eBasicRemoveFromContainerFeature(final NotificationChain msgs) {
		switch (this.eContainerFeatureID()) {
		case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
			return this.eInternalContainer().eInverseRemove(this, IAnalysisMetaModelPackage.OUTPUT_PORT__OUT_CONNECTOR, IOutputPort.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
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
		case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
			if (resolve) {
				return this.getDstInputPort();
			}
			return this.basicGetDstInputPort();
		case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
			return this.getSicOutputPort();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	@Override
	public void eSet(final int featureID, final Object newValue) {
		switch (featureID) {
		case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
			this.setDstInputPort((IInputPort) newValue);
			return;
		case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
			this.setSicOutputPort((IOutputPort) newValue);
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
		case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
			this.setDstInputPort((IInputPort) null);
			return;
		case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
			this.setSicOutputPort((IOutputPort) null);
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
		case IAnalysisMetaModelPackage.CONNECTOR__DST_INPUT_PORT:
			return this.dstInputPort != null;
		case IAnalysisMetaModelPackage.CONNECTOR__SIC_OUTPUT_PORT:
			return this.getSicOutputPort() != null;
		}
		return super.eIsSet(featureID);
	}

} // Connector
