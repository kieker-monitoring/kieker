/**
 */
package kieker.model.analysismodel.type.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.type.impl.OperationTypeImpl#getSignature <em>Signature</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.OperationTypeImpl#getName <em>Name</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.OperationTypeImpl#getReturnType <em>Return Type</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.OperationTypeImpl#getModifiers <em>Modifiers</em>}</li>
 * <li>{@link kieker.model.analysismodel.type.impl.OperationTypeImpl#getParameterTypes <em>Parameter Types</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationTypeImpl extends MinimalEObjectImpl.Container implements OperationType {
	/**
	 * The default value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected static final String SIGNATURE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSignature() <em>Signature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getSignature()
	 * @generated
	 * @ordered
	 */
	protected String signature = SIGNATURE_EDEFAULT;

	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getReturnType() <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
	protected static final String RETURN_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getReturnType() <em>Return Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
	protected String returnType = RETURN_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getModifiers() <em>Modifiers</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getModifiers()
	 * @generated
	 * @ordered
	 */
	protected EList<String> modifiers;

	/**
	 * The cached value of the '{@link #getParameterTypes() <em>Parameter Types</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getParameterTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<String> parameterTypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OperationTypeImpl() {
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
		return TypePackage.Literals.OPERATION_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getSignature() {
		return this.signature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setSignature(final String newSignature) {
		final String oldSignature = this.signature;
		this.signature = newSignature;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.OPERATION_TYPE__SIGNATURE, oldSignature, this.signature));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setName(final String newName) {
		final String oldName = this.name;
		this.name = newName;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.OPERATION_TYPE__NAME, oldName, this.name));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getReturnType() {
		return this.returnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setReturnType(final String newReturnType) {
		final String oldReturnType = this.returnType;
		this.returnType = newReturnType;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TypePackage.OPERATION_TYPE__RETURN_TYPE, oldReturnType, this.returnType));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<String> getModifiers() {
		if (this.modifiers == null) {
			this.modifiers = new EDataTypeUniqueEList<>(String.class, this, TypePackage.OPERATION_TYPE__MODIFIERS);
		}
		return this.modifiers;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<String> getParameterTypes() {
		if (this.parameterTypes == null) {
			this.parameterTypes = new EDataTypeUniqueEList<>(String.class, this, TypePackage.OPERATION_TYPE__PARAMETER_TYPES);
		}
		return this.parameterTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComponentType getComponentType() {
		final org.eclipse.emf.ecore.EObject container = this.eContainer();
		if (container != null) {
			final org.eclipse.emf.ecore.EObject containerContainer = container.eContainer();
			if (containerContainer != null) {
				return (ComponentType) containerContainer;
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
		case TypePackage.OPERATION_TYPE__SIGNATURE:
			return this.getSignature();
		case TypePackage.OPERATION_TYPE__NAME:
			return this.getName();
		case TypePackage.OPERATION_TYPE__RETURN_TYPE:
			return this.getReturnType();
		case TypePackage.OPERATION_TYPE__MODIFIERS:
			return this.getModifiers();
		case TypePackage.OPERATION_TYPE__PARAMETER_TYPES:
			return this.getParameterTypes();
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
		case TypePackage.OPERATION_TYPE__SIGNATURE:
			this.setSignature((String) newValue);
			return;
		case TypePackage.OPERATION_TYPE__NAME:
			this.setName((String) newValue);
			return;
		case TypePackage.OPERATION_TYPE__RETURN_TYPE:
			this.setReturnType((String) newValue);
			return;
		case TypePackage.OPERATION_TYPE__MODIFIERS:
			this.getModifiers().clear();
			this.getModifiers().addAll((Collection<? extends String>) newValue);
			return;
		case TypePackage.OPERATION_TYPE__PARAMETER_TYPES:
			this.getParameterTypes().clear();
			this.getParameterTypes().addAll((Collection<? extends String>) newValue);
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
		case TypePackage.OPERATION_TYPE__SIGNATURE:
			this.setSignature(SIGNATURE_EDEFAULT);
			return;
		case TypePackage.OPERATION_TYPE__NAME:
			this.setName(NAME_EDEFAULT);
			return;
		case TypePackage.OPERATION_TYPE__RETURN_TYPE:
			this.setReturnType(RETURN_TYPE_EDEFAULT);
			return;
		case TypePackage.OPERATION_TYPE__MODIFIERS:
			this.getModifiers().clear();
			return;
		case TypePackage.OPERATION_TYPE__PARAMETER_TYPES:
			this.getParameterTypes().clear();
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
		case TypePackage.OPERATION_TYPE__SIGNATURE:
			return SIGNATURE_EDEFAULT == null ? this.signature != null : !SIGNATURE_EDEFAULT.equals(this.signature);
		case TypePackage.OPERATION_TYPE__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
		case TypePackage.OPERATION_TYPE__RETURN_TYPE:
			return RETURN_TYPE_EDEFAULT == null ? this.returnType != null : !RETURN_TYPE_EDEFAULT.equals(this.returnType);
		case TypePackage.OPERATION_TYPE__MODIFIERS:
			return (this.modifiers != null) && !this.modifiers.isEmpty();
		case TypePackage.OPERATION_TYPE__PARAMETER_TYPES:
			return (this.parameterTypes != null) && !this.parameterTypes.isEmpty();
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
		case TypePackage.OPERATION_TYPE___GET_COMPONENT_TYPE:
			return this.getComponentType();
		}
		return super.eInvoke(operationID, arguments);
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
		result.append(" (signature: ");
		result.append(this.signature);
		result.append(", name: ");
		result.append(this.name);
		result.append(", returnType: ");
		result.append(this.returnType);
		result.append(", modifiers: ");
		result.append(this.modifiers);
		result.append(", parameterTypes: ");
		result.append(this.parameterTypes);
		result.append(')');
		return result.toString();
	}

} // OperationTypeImpl
