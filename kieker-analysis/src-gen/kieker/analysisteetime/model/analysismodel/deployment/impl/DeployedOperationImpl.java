/**
 */
package kieker.analysisteetime.model.analysismodel.deployment.impl;

import java.lang.reflect.InvocationTargetException;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedComponent;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentPackage;

import kieker.analysisteetime.model.analysismodel.type.OperationType;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.analysisteetime.model.analysismodel.deployment.impl.DeployedOperationImpl#getOperationType <em>Operation Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedOperationImpl extends MinimalEObjectImpl.Container implements DeployedOperation {
	/**
	 * The cached value of the '{@link #getOperationType() <em>Operation Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationType()
	 * @generated
	 * @ordered
	 */
	protected OperationType operationType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DeployedOperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return DeploymentPackage.Literals.DEPLOYED_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationType getOperationType() {
		if (operationType != null && operationType.eIsProxy()) {
			InternalEObject oldOperationType = (InternalEObject)operationType;
			operationType = (OperationType)eResolveProxy(oldOperationType);
			if (operationType != oldOperationType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE, oldOperationType, operationType));
			}
		}
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationType basicGetOperationType() {
		return operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperationType(OperationType newOperationType) {
		OperationType oldOperationType = operationType;
		operationType = newOperationType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE, oldOperationType, operationType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedComponent getComponent() {
		org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (DeployedComponent) containerContainer ;
			}
		}
		return null;
		
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				if (resolve) return getOperationType();
				return basicGetOperationType();
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
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				setOperationType((OperationType)newValue);
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
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				setOperationType((OperationType)null);
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
			case DeploymentPackage.DEPLOYED_OPERATION__OPERATION_TYPE:
				return operationType != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eInvoke(int operationID, EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
			case DeploymentPackage.DEPLOYED_OPERATION___GET_COMPONENT:
				return getComponent();
		}
		return super.eInvoke(operationID, arguments);
	}

} //DeployedOperationImpl
