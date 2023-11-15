/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.tools.restructuring.restructuremodel.AbstractTransformationStep;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;
import kieker.tools.restructuring.restructuremodel.TransformationModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transformation Model</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.TransformationModelImpl#getTransformations <em>Transformations</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.TransformationModelImpl#getName <em>Name</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransformationModelImpl extends MinimalEObjectImpl.Container implements TransformationModel {
	/**
	 * The cached value of the '{@link #getTransformations() <em>Transformations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTransformations()
	 * @generated
	 * @ordered
	 */
	protected EList<AbstractTransformationStep> transformations;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected TransformationModelImpl() {
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
		return RestructuremodelPackage.Literals.TRANSFORMATION_MODEL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<AbstractTransformationStep> getTransformations() {
		if (this.transformations == null) {
			this.transformations = new EObjectContainmentEList<>(AbstractTransformationStep.class, this,
					RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS);
		}
		return this.transformations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setName(final String newName) {
		final String oldName = this.name;
		this.name = newName;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.TRANSFORMATION_MODEL__NAME, oldName, this.name));
		}
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
		case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
			return ((InternalEList<?>) this.getTransformations()).basicRemove(otherEnd, msgs);
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
		case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
			return this.getTransformations();
		case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
			return this.getName();
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
		case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
			this.getTransformations().clear();
			this.getTransformations().addAll((Collection<? extends AbstractTransformationStep>) newValue);
			return;
		case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
			this.setName((String) newValue);
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
		case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
			this.getTransformations().clear();
			return;
		case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
			this.setName(NAME_EDEFAULT);
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
		case RestructuremodelPackage.TRANSFORMATION_MODEL__TRANSFORMATIONS:
			return (this.transformations != null) && !this.transformations.isEmpty();
		case RestructuremodelPackage.TRANSFORMATION_MODEL__NAME:
			return NAME_EDEFAULT == null ? this.name != null : !NAME_EDEFAULT.equals(this.name);
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
		result.append(" (name: ");
		result.append(this.name);
		result.append(')');
		return result.toString();
	}

} // TransformationModelImpl
