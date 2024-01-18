/**
 */
package kieker.model.analysismodel.deployment.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Deployed Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.deployment.impl.DeployedOperationImpl#getAssemblyOperation <em>Assembly Operation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DeployedOperationImpl extends MinimalEObjectImpl.Container implements DeployedOperation {
	/**
	 * The cached value of the '{@link #getAssemblyOperation() <em>Assembly Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getAssemblyOperation()
	 * @generated
	 * @ordered
	 */
	protected AssemblyOperation assemblyOperation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected DeployedOperationImpl() {
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
		return DeploymentPackage.Literals.DEPLOYED_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyOperation getAssemblyOperation() {
		if ((this.assemblyOperation != null) && this.assemblyOperation.eIsProxy()) {
			final InternalEObject oldAssemblyOperation = (InternalEObject) this.assemblyOperation;
			this.assemblyOperation = (AssemblyOperation) this.eResolveProxy(oldAssemblyOperation);
			if (this.assemblyOperation != oldAssemblyOperation) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, DeploymentPackage.DEPLOYED_OPERATION__ASSEMBLY_OPERATION, oldAssemblyOperation,
							this.assemblyOperation));
				}
			}
		}
		return this.assemblyOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public AssemblyOperation basicGetAssemblyOperation() {
		return this.assemblyOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setAssemblyOperation(final AssemblyOperation newAssemblyOperation) {
		final AssemblyOperation oldAssemblyOperation = this.assemblyOperation;
		this.assemblyOperation = newAssemblyOperation;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, DeploymentPackage.DEPLOYED_OPERATION__ASSEMBLY_OPERATION, oldAssemblyOperation,
					this.assemblyOperation));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedComponent getComponent() {
		final org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			final org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (DeployedComponent) containerContainer;
			}
		}
		return null;

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
		case DeploymentPackage.DEPLOYED_OPERATION__ASSEMBLY_OPERATION:
			if (resolve) {
				return this.getAssemblyOperation();
			}
			return this.basicGetAssemblyOperation();
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
		case DeploymentPackage.DEPLOYED_OPERATION__ASSEMBLY_OPERATION:
			this.setAssemblyOperation((AssemblyOperation) newValue);
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
		case DeploymentPackage.DEPLOYED_OPERATION__ASSEMBLY_OPERATION:
			this.setAssemblyOperation((AssemblyOperation) null);
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
		case DeploymentPackage.DEPLOYED_OPERATION__ASSEMBLY_OPERATION:
			return this.assemblyOperation != null;
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
	public Object eInvoke(final int operationID, final EList<?> arguments) throws InvocationTargetException {
		switch (operationID) {
		case DeploymentPackage.DEPLOYED_OPERATION___GET_COMPONENT:
			return this.getComponent();
		}
		return super.eInvoke(operationID, arguments);
	}

} // DeployedOperationImpl
