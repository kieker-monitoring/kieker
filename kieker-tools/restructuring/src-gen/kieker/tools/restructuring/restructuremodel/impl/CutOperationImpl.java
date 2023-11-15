/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import kieker.tools.restructuring.restructuremodel.CutOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Cut Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.CutOperationImpl#getComponentName <em>Component Name</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.CutOperationImpl#getOperationName <em>Operation Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CutOperationImpl extends AbstractTransformationStepImpl implements CutOperation {
	/**
	 * The default value of the '{@link #getComponentName() <em>Component Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected static final String COMPONENT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComponentName() <em>Component Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected String componentName = COMPONENT_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationName()
	 * @generated
	 * @ordered
	 */
	protected static final String OPERATION_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationName()
	 * @generated
	 * @ordered
	 */
	protected String operationName = OPERATION_NAME_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected CutOperationImpl() {
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
		return RestructuremodelPackage.Literals.CUT_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getComponentName() {
		return this.componentName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setComponentName(final String newComponentName) {
		final String oldComponentName = this.componentName;
		this.componentName = newComponentName;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.CUT_OPERATION__COMPONENT_NAME, oldComponentName, this.componentName));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getOperationName() {
		return this.operationName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setOperationName(final String newOperationName) {
		final String oldOperationName = this.operationName;
		this.operationName = newOperationName;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.CUT_OPERATION__OPERATION_NAME, oldOperationName, this.operationName));
		}
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
		case RestructuremodelPackage.CUT_OPERATION__COMPONENT_NAME:
			return this.getComponentName();
		case RestructuremodelPackage.CUT_OPERATION__OPERATION_NAME:
			return this.getOperationName();
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
		case RestructuremodelPackage.CUT_OPERATION__COMPONENT_NAME:
			this.setComponentName((String) newValue);
			return;
		case RestructuremodelPackage.CUT_OPERATION__OPERATION_NAME:
			this.setOperationName((String) newValue);
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
		case RestructuremodelPackage.CUT_OPERATION__COMPONENT_NAME:
			this.setComponentName(COMPONENT_NAME_EDEFAULT);
			return;
		case RestructuremodelPackage.CUT_OPERATION__OPERATION_NAME:
			this.setOperationName(OPERATION_NAME_EDEFAULT);
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
		case RestructuremodelPackage.CUT_OPERATION__COMPONENT_NAME:
			return COMPONENT_NAME_EDEFAULT == null ? this.componentName != null : !COMPONENT_NAME_EDEFAULT.equals(this.componentName);
		case RestructuremodelPackage.CUT_OPERATION__OPERATION_NAME:
			return OPERATION_NAME_EDEFAULT == null ? this.operationName != null : !OPERATION_NAME_EDEFAULT.equals(this.operationName);
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

		final StringBuilder result = new StringBuilder(super.toString());
		result.append(" (componentName: ");
		result.append(this.componentName);
		result.append(", operationName: ");
		result.append(this.operationName);
		result.append(')');
		return result.toString();
	}

} // CutOperationImpl
