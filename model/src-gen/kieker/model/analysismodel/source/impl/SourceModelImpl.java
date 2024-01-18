/**
 */
package kieker.model.analysismodel.source.impl;

import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.source.SourcePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.model.analysismodel.source.impl.SourceModelImpl#getSources <em>Sources</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SourceModelImpl extends MinimalEObjectImpl.Container implements SourceModel {
	/**
	 * The cached value of the '{@link #getSources() <em>Sources</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getSources()
	 * @generated
	 * @ordered
	 */
	protected EMap<EObject, EList<String>> sources;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SourceModelImpl() {
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
		return SourcePackage.Literals.SOURCE_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EMap<EObject, EList<String>> getSources() {
		if (this.sources == null) {
			this.sources = new EcoreEMap<>(SourcePackage.Literals.EOBJECT_TO_SOURCE_ENTRY, EObjectToSourceEntryImpl.class, this,
					SourcePackage.SOURCE_MODEL__SOURCES);
		}
		return this.sources;
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
		case SourcePackage.SOURCE_MODEL__SOURCES:
			return ((InternalEList<?>) this.getSources()).basicRemove(otherEnd, msgs);
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
		case SourcePackage.SOURCE_MODEL__SOURCES:
			if (coreType) {
				return this.getSources();
			} else {
				return this.getSources().map();
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
		case SourcePackage.SOURCE_MODEL__SOURCES:
			((EStructuralFeature.Setting) this.getSources()).set(newValue);
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
		case SourcePackage.SOURCE_MODEL__SOURCES:
			this.getSources().clear();
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
		case SourcePackage.SOURCE_MODEL__SOURCES:
			return (this.sources != null) && !this.sources.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} // SourceModelImpl
