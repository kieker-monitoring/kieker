/**
 */
package kieker.model.analysismodel.type.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.type.impl.TypeModelImpl#getComponentTypes <em>Component Types</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TypeModelImpl extends MinimalEObjectImpl.Container implements TypeModel {
	/**
	 * The cached value of the '{@link #getComponentTypes() <em>Component Types</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getComponentTypes()
	 * @generated
	 * @ordered
	 */
	protected EMap<String, ComponentType> componentTypes;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TypeModelImpl() {
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
		return TypePackage.Literals.TYPE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<String, ComponentType> getComponentTypes() {
		if (this.componentTypes == null) {
			this.componentTypes = new EcoreEMap<>(TypePackage.Literals.ESTRING_TO_COMPONENT_TYPE_MAP_ENTRY,
					EStringToComponentTypeMapEntryImpl.class,
					this, TypePackage.TYPE_MODEL__COMPONENT_TYPES);
		}
		return this.componentTypes;
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
		case TypePackage.TYPE_MODEL__COMPONENT_TYPES:
			return ((InternalEList<?>) this.getComponentTypes()).basicRemove(otherEnd, msgs);
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
		case TypePackage.TYPE_MODEL__COMPONENT_TYPES:
			if (coreType) {
				return this.getComponentTypes();
			} else {
				return this.getComponentTypes().map();
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
		case TypePackage.TYPE_MODEL__COMPONENT_TYPES:
			((EStructuralFeature.Setting) this.getComponentTypes()).set(newValue);
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
		case TypePackage.TYPE_MODEL__COMPONENT_TYPES:
			this.getComponentTypes().clear();
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
		case TypePackage.TYPE_MODEL__COMPONENT_TYPES:
			return (this.componentTypes != null) && !this.componentTypes.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // TypeModelImpl
