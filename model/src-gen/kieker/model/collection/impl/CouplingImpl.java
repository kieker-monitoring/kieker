/**
 */
package kieker.model.collection.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.collection.CollectionPackage;
import kieker.model.collection.Coupling;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Coupling</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.collection.impl.CouplingImpl#getRequired <em>Required</em>}</li>
 *   <li>{@link kieker.model.collection.impl.CouplingImpl#getProvided <em>Provided</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CouplingImpl extends MinimalEObjectImpl.Container implements Coupling {
	/**
	 * The cached value of the '{@link #getRequired() <em>Required</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRequired()
	 * @generated
	 * @ordered
	 */
	protected ComponentType required;

	/**
	 * The cached value of the '{@link #getProvided() <em>Provided</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvided()
	 * @generated
	 * @ordered
	 */
	protected ComponentType provided;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CouplingImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CollectionPackage.Literals.COUPLING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComponentType getRequired() {
		if (required != null && required.eIsProxy()) {
			InternalEObject oldRequired = (InternalEObject)required;
			required = (ComponentType)eResolveProxy(oldRequired);
			if (required != oldRequired) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.COUPLING__REQUIRED, oldRequired, required));
			}
		}
		return required;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetRequired() {
		return required;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRequired(ComponentType newRequired) {
		ComponentType oldRequired = required;
		required = newRequired;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.COUPLING__REQUIRED, oldRequired, required));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComponentType getProvided() {
		if (provided != null && provided.eIsProxy()) {
			InternalEObject oldProvided = (InternalEObject)provided;
			provided = (ComponentType)eResolveProxy(oldProvided);
			if (provided != oldProvided) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.COUPLING__PROVIDED, oldProvided, provided));
			}
		}
		return provided;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ComponentType basicGetProvided() {
		return provided;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProvided(ComponentType newProvided) {
		ComponentType oldProvided = provided;
		provided = newProvided;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.COUPLING__PROVIDED, oldProvided, provided));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean equals(final Object value) {
		if (value != null) {
			if (value instanceof Coupling) {
				final Coupling coupling = (Coupling) value;
		                if (this.caller == null && coupling.getCaller() == null) {
		                	if (this.callee == null && coupling.getCallee() == null) {
						return true;
					} else if (this.callee != null && coupling.getCallee() != null) {
		 				return this.callee.equals(coupling.getCallee());
					}
				} else if (this.caller != null && coupling.getCaller() != null) {
					if (this.callee == null && coupling.getCallee() == null) {
						return this.caller.equals(coupling.getCaller()) ;
					} else if (this.callee != null && coupling.getCallee() != null) {
						return this.caller.equals(coupling.getCaller()) && this.callee.equals(coupling.getCallee());
					}
				}
			}
		}
		return false;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int hashCode() {
		return (this.caller == null ? 0 : this.caller.hashCode()) ^ (this.callee == null ? 0 : this.callee.hashCode());
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CollectionPackage.COUPLING__REQUIRED:
				if (resolve) return getRequired();
				return basicGetRequired();
			case CollectionPackage.COUPLING__PROVIDED:
				if (resolve) return getProvided();
				return basicGetProvided();
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
			case CollectionPackage.COUPLING__REQUIRED:
				setRequired((ComponentType)newValue);
				return;
			case CollectionPackage.COUPLING__PROVIDED:
				setProvided((ComponentType)newValue);
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
			case CollectionPackage.COUPLING__REQUIRED:
				setRequired((ComponentType)null);
				return;
			case CollectionPackage.COUPLING__PROVIDED:
				setProvided((ComponentType)null);
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
			case CollectionPackage.COUPLING__REQUIRED:
				return required != null;
			case CollectionPackage.COUPLING__PROVIDED:
				return provided != null;
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
			case CollectionPackage.COUPLING___EQUALS__OBJECT:
				return equals(arguments.get(0));
			case CollectionPackage.COUPLING___HASH_CODE:
				return hashCode();
		}
		return super.eInvoke(operationID, arguments);
	}

} // CouplingImpl
