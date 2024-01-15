/**
 */
package kieker.model.collection.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.collection.CollectionPackage;
import kieker.model.collection.Connections;
import kieker.model.collection.Coupling;
import kieker.model.collection.OperationCollection;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Connections</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.collection.impl.ConnectionsImpl#getConnections <em>Connections</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConnectionsImpl extends MinimalEObjectImpl.Container implements Connections {
	/**
	 * The cached value of the '{@link #getConnections() <em>Connections</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getConnections()
	 * @generated
	 * @ordered
	 */
	protected EMap<Coupling, OperationCollection> connections;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected ConnectionsImpl() {
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
		return CollectionPackage.Literals.CONNECTIONS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<Coupling, OperationCollection> getConnections() {
		if (this.connections == null) {
			this.connections = new EcoreEMap<>(CollectionPackage.Literals.COUPLING_TO_OPERATION_MAP, CouplingToOperationMapImpl.class,
					this,
					CollectionPackage.CONNECTIONS__CONNECTIONS);
		}
		return this.connections;
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
		case CollectionPackage.CONNECTIONS__CONNECTIONS:
			return ((InternalEList<?>) this.getConnections()).basicRemove(otherEnd, msgs);
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
	public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
		switch (featureID) {
		case CollectionPackage.CONNECTIONS__CONNECTIONS:
			if (coreType) {
				return this.getConnections();
			} else {
				return this.getConnections().map();
			}
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
		case CollectionPackage.CONNECTIONS__CONNECTIONS:
			((EStructuralFeature.Setting) this.getConnections()).set(newValue);
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
		case CollectionPackage.CONNECTIONS__CONNECTIONS:
			this.getConnections().clear();
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
		case CollectionPackage.CONNECTIONS__CONNECTIONS:
			return (this.connections != null) && !this.connections.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // ConnectionsImpl
