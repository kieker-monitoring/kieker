/**
 */
package kieker.model.analysismodel.execution.impl;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Tuple;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Tuple</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.execution.impl.TupleImpl#getFirst <em>First</em>}</li>
 * <li>{@link kieker.model.analysismodel.execution.impl.TupleImpl#getSecond <em>Second</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TupleImpl<F, S> extends MinimalEObjectImpl.Container implements Tuple<F, S> {
	/**
	 * The cached value of the '{@link #getFirst() <em>First</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getFirst()
	 * @generated
	 * @ordered
	 */
	protected F first;

	/**
	 * The cached value of the '{@link #getSecond() <em>Second</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getSecond()
	 * @generated
	 * @ordered
	 */
	protected S second;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TupleImpl() {
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
		return ExecutionPackage.Literals.TUPLE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public F getFirst() {
		if ((this.first != null) && ((EObject) this.first).eIsProxy()) {
			final InternalEObject oldFirst = (InternalEObject) this.first;
			this.first = (F) this.eResolveProxy(oldFirst);
			if (this.first != oldFirst) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.TUPLE__FIRST, oldFirst, this.first));
				}
			}
		}
		return this.first;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public F basicGetFirst() {
		return this.first;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setFirst(final F newFirst) {
		final F oldFirst = this.first;
		this.first = newFirst;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.TUPLE__FIRST, oldFirst, this.first));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public S getSecond() {
		if ((this.second != null) && ((EObject) this.second).eIsProxy()) {
			final InternalEObject oldSecond = (InternalEObject) this.second;
			this.second = (S) this.eResolveProxy(oldSecond);
			if (this.second != oldSecond) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, ExecutionPackage.TUPLE__SECOND, oldSecond, this.second));
				}
			}
		}
		return this.second;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public S basicGetSecond() {
		return this.second;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setSecond(final S newSecond) {
		final S oldSecond = this.second;
		this.second = newSecond;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, ExecutionPackage.TUPLE__SECOND, oldSecond, this.second));
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
			if (value instanceof Tuple) {
				final Tuple<?, ?> key = (Tuple<?, ?>) value;
				if ((this.first == null) && (key.getFirst() == null)) {
					if ((this.second == null) && (key.getSecond() == null)) {
						return true;
					} else if ((this.second != null) && (key.getSecond() != null)) {
						return this.second.equals(key.getSecond());
					}
				} else if ((this.first != null) && (key.getFirst() != null)) {
					if ((this.second == null) && (key.getSecond() == null)) {
						return this.first.equals(key.getFirst());
					} else if ((this.second != null) && (key.getSecond() != null)) {
						return this.first.equals(key.getFirst()) && this.second.equals(key.getSecond());
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
		return (this.first == null ? 0 : this.first.hashCode()) ^ (this.second == null ? 0 : this.second.hashCode());
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
		case ExecutionPackage.TUPLE__FIRST:
			if (resolve) {
				return this.getFirst();
			}
			return this.basicGetFirst();
		case ExecutionPackage.TUPLE__SECOND:
			if (resolve) {
				return this.getSecond();
			}
			return this.basicGetSecond();
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
		case ExecutionPackage.TUPLE__FIRST:
			this.setFirst((F) newValue);
			return;
		case ExecutionPackage.TUPLE__SECOND:
			this.setSecond((S) newValue);
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
		case ExecutionPackage.TUPLE__FIRST:
			this.setFirst((F) null);
			return;
		case ExecutionPackage.TUPLE__SECOND:
			this.setSecond((S) null);
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
		case ExecutionPackage.TUPLE__FIRST:
			return this.first != null;
		case ExecutionPackage.TUPLE__SECOND:
			return this.second != null;
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
		case ExecutionPackage.TUPLE___EQUALS__OBJECT:
			return this.equals(arguments.get(0));
		case ExecutionPackage.TUPLE___HASH_CODE:
			return this.hashCode();
		}
		return super.eInvoke(operationID, arguments);
	}

} // TupleImpl
