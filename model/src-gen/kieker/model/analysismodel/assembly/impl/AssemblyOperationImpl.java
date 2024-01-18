/**
 */
package kieker.model.analysismodel.assembly.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.type.OperationType;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.assembly.impl.AssemblyOperationImpl#getOperationType <em>Operation Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssemblyOperationImpl extends MinimalEObjectImpl.Container implements AssemblyOperation {
	/**
	 * The cached value of the '{@link #getOperationType() <em>Operation Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationType()
	 * @generated
	 * @ordered
	 */
	protected OperationType operationType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected AssemblyOperationImpl() {
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
		return AssemblyPackage.Literals.ASSEMBLY_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationType getOperationType() {
		if ((this.operationType != null) && this.operationType.eIsProxy()) {
			final InternalEObject oldOperationType = (InternalEObject) this.operationType;
			this.operationType = (OperationType) this.eResolveProxy(oldOperationType);
			if (this.operationType != oldOperationType) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, AssemblyPackage.ASSEMBLY_OPERATION__OPERATION_TYPE, oldOperationType,
							this.operationType));
				}
			}
		}
		return this.operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public OperationType basicGetOperationType() {
		return this.operationType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setOperationType(final OperationType newOperationType) {
		final OperationType oldOperationType = this.operationType;
		this.operationType = newOperationType;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, AssemblyPackage.ASSEMBLY_OPERATION__OPERATION_TYPE, oldOperationType, this.operationType));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public AssemblyComponent getComponent() {
		final org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			final org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (AssemblyComponent) containerContainer;
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
		case AssemblyPackage.ASSEMBLY_OPERATION__OPERATION_TYPE:
			if (resolve) {
				return this.getOperationType();
			}
			return this.basicGetOperationType();
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
		case AssemblyPackage.ASSEMBLY_OPERATION__OPERATION_TYPE:
			this.setOperationType((OperationType) newValue);
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
		case AssemblyPackage.ASSEMBLY_OPERATION__OPERATION_TYPE:
			this.setOperationType((OperationType) null);
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
		case AssemblyPackage.ASSEMBLY_OPERATION__OPERATION_TYPE:
			return this.operationType != null;
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
		case AssemblyPackage.ASSEMBLY_OPERATION___GET_COMPONENT:
			return this.getComponent();
		}
		return super.eInvoke(operationID, arguments);
	}

} // AssemblyOperationImpl
