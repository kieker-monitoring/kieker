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
 * <li>{@link kieker.model.collection.impl.CouplingImpl#getCaller <em>Caller</em>}</li>
 * <li>{@link kieker.model.collection.impl.CouplingImpl#getCallee <em>Callee</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CouplingImpl extends MinimalEObjectImpl.Container implements Coupling {
	/**
	 * The cached value of the '{@link #getCaller() <em>Caller</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCaller()
	 * @generated
	 * @ordered
	 */
	protected ComponentType caller;

	/**
	 * The cached value of the '{@link #getCallee() <em>Callee</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCallee()
	 * @generated
	 * @ordered
	 */
	protected ComponentType callee;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected CouplingImpl() {
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
		return CollectionPackage.Literals.COUPLING;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComponentType getCaller() {
		if ((this.caller != null) && this.caller.eIsProxy()) {
			final InternalEObject oldCaller = (InternalEObject) this.caller;
			this.caller = (ComponentType) this.eResolveProxy(oldCaller);
			if (this.caller != oldCaller) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.COUPLING__CALLER, oldCaller, this.caller));
				}
			}
		}
		return this.caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ComponentType basicGetCaller() {
		return this.caller;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCaller(final ComponentType newCaller) {
		final ComponentType oldCaller = this.caller;
		this.caller = newCaller;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.COUPLING__CALLER, oldCaller, this.caller));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComponentType getCallee() {
		if ((this.callee != null) && this.callee.eIsProxy()) {
			final InternalEObject oldCallee = (InternalEObject) this.callee;
			this.callee = (ComponentType) this.eResolveProxy(oldCallee);
			if (this.callee != oldCallee) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, CollectionPackage.COUPLING__CALLEE, oldCallee, this.callee));
				}
			}
		}
		return this.callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ComponentType basicGetCallee() {
		return this.callee;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setCallee(final ComponentType newCallee) {
		final ComponentType oldCallee = this.callee;
		this.callee = newCallee;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, CollectionPackage.COUPLING__CALLEE, oldCallee, this.callee));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean equals(final Object value) {
		if (value != null) {
			if (value instanceof Coupling) {
				final Coupling coupling = (Coupling) value;
				if ((this.caller == null) && (coupling.getCaller() == null)) {
					if ((this.callee == null) && (coupling.getCallee() == null)) {
						return true;
					} else if ((this.callee != null) && (coupling.getCallee() != null)) {
						return this.callee.equals(coupling.getCallee());
					}
				} else if ((this.caller != null) && (coupling.getCaller() != null)) {
					if ((this.callee == null) && (coupling.getCallee() == null)) {
						return this.caller.equals(coupling.getCaller());
					} else if ((this.callee != null) && (coupling.getCallee() != null)) {
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
	 *
	 * @generated
	 */
	@Override
	public int hashCode() {
		return (this.caller == null ? 0 : this.caller.hashCode()) ^ (this.callee == null ? 0 : this.callee.hashCode());
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
		case CollectionPackage.COUPLING__CALLER:
			if (resolve) {
				return this.getCaller();
			}
			return this.basicGetCaller();
		case CollectionPackage.COUPLING__CALLEE:
			if (resolve) {
				return this.getCallee();
			}
			return this.basicGetCallee();
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
		case CollectionPackage.COUPLING__CALLER:
			this.setCaller((ComponentType) newValue);
			return;
		case CollectionPackage.COUPLING__CALLEE:
			this.setCallee((ComponentType) newValue);
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
		case CollectionPackage.COUPLING__CALLER:
			this.setCaller((ComponentType) null);
			return;
		case CollectionPackage.COUPLING__CALLEE:
			this.setCallee((ComponentType) null);
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
		case CollectionPackage.COUPLING__CALLER:
			return this.caller != null;
		case CollectionPackage.COUPLING__CALLEE:
			return this.callee != null;
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
		case CollectionPackage.COUPLING___EQUALS__OBJECT:
			return this.equals(arguments.get(0));
		case CollectionPackage.COUPLING___HASH_CODE:
			return this.hashCode();
		}
		return super.eInvoke(operationID, arguments);
	}

} // CouplingImpl
