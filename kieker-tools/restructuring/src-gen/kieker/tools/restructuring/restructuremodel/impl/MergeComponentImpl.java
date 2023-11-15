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
import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.tools.restructuring.restructuremodel.DeleteComponent;
import kieker.tools.restructuring.restructuremodel.MergeComponent;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Merge Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getMergeGoalComponent <em>Merge Goal Component</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getComponentName <em>Component Name</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getOperations <em>Operations</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getDeleteTransformation <em>Delete Transformation</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getOperationToMove <em>Operation To Move</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MergeComponentImpl extends AbstractTransformationStepImpl implements MergeComponent {
	/**
	 * The default value of the '{@link #getMergeGoalComponent() <em>Merge Goal Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getMergeGoalComponent()
	 * @generated
	 * @ordered
	 */
	protected static final String MERGE_GOAL_COMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMergeGoalComponent() <em>Merge Goal Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getMergeGoalComponent()
	 * @generated
	 * @ordered
	 */
	protected String mergeGoalComponent = MERGE_GOAL_COMPONENT_EDEFAULT;

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
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<String> operations;

	/**
	 * The cached value of the '{@link #getDeleteTransformation() <em>Delete Transformation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDeleteTransformation()
	 * @generated
	 * @ordered
	 */
	protected DeleteComponent deleteTransformation;

	/**
	 * The cached value of the '{@link #getOperationToMove() <em>Operation To Move</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationToMove()
	 * @generated
	 * @ordered
	 */
	protected EList<MoveOperation> operationToMove;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MergeComponentImpl() {
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
		return RestructuremodelPackage.Literals.MERGE_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getMergeGoalComponent() {
		return this.mergeGoalComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setMergeGoalComponent(final String newMergeGoalComponent) {
		final String oldMergeGoalComponent = this.mergeGoalComponent;
		this.mergeGoalComponent = newMergeGoalComponent;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT, oldMergeGoalComponent,
					this.mergeGoalComponent));
		}
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
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME, oldComponentName, this.componentName));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<String> getOperations() {
		if (this.operations == null) {
			this.operations = new EDataTypeUniqueEList<>(String.class, this, RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS);
		}
		return this.operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeleteComponent getDeleteTransformation() {
		return this.deleteTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetDeleteTransformation(final DeleteComponent newDeleteTransformation, NotificationChain msgs) {
		final DeleteComponent oldDeleteTransformation = this.deleteTransformation;
		this.deleteTransformation = newDeleteTransformation;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION,
					oldDeleteTransformation, newDeleteTransformation);
			if (msgs == null) {
				msgs = notification;
			} else {
				msgs.add(notification);
			}
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setDeleteTransformation(final DeleteComponent newDeleteTransformation) {
		if (newDeleteTransformation != this.deleteTransformation) {
			NotificationChain msgs = null;
			if (this.deleteTransformation != null) {
				msgs = ((InternalEObject) this.deleteTransformation).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION, null, msgs);
			}
			if (newDeleteTransformation != null) {
				msgs = ((InternalEObject) newDeleteTransformation).eInverseAdd(this,
						EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION, null, msgs);
			}
			msgs = this.basicSetDeleteTransformation(newDeleteTransformation, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION, newDeleteTransformation,
					newDeleteTransformation));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MoveOperation> getOperationToMove() {
		if (this.operationToMove == null) {
			this.operationToMove = new EObjectContainmentEList<>(MoveOperation.class, this, RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE);
		}
		return this.operationToMove;
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
		case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
			return this.basicSetDeleteTransformation(null, msgs);
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
			return ((InternalEList<?>) this.getOperationToMove()).basicRemove(otherEnd, msgs);
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
		case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
			return this.getMergeGoalComponent();
		case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
			return this.getComponentName();
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
			return this.getOperations();
		case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
			return this.getDeleteTransformation();
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
			return this.getOperationToMove();
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
		case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
			this.setMergeGoalComponent((String) newValue);
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
			this.setComponentName((String) newValue);
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
			this.getOperations().clear();
			this.getOperations().addAll((Collection<? extends String>) newValue);
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
			this.setDeleteTransformation((DeleteComponent) newValue);
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
			this.getOperationToMove().clear();
			this.getOperationToMove().addAll((Collection<? extends MoveOperation>) newValue);
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
		case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
			this.setMergeGoalComponent(MERGE_GOAL_COMPONENT_EDEFAULT);
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
			this.setComponentName(COMPONENT_NAME_EDEFAULT);
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
			this.getOperations().clear();
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
			this.setDeleteTransformation((DeleteComponent) null);
			return;
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
			this.getOperationToMove().clear();
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
		case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
			return MERGE_GOAL_COMPONENT_EDEFAULT == null ? this.mergeGoalComponent != null : !MERGE_GOAL_COMPONENT_EDEFAULT.equals(this.mergeGoalComponent);
		case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
			return COMPONENT_NAME_EDEFAULT == null ? this.componentName != null : !COMPONENT_NAME_EDEFAULT.equals(this.componentName);
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
			return (this.operations != null) && !this.operations.isEmpty();
		case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
			return this.deleteTransformation != null;
		case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
			return (this.operationToMove != null) && !this.operationToMove.isEmpty();
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
		result.append(" (mergeGoalComponent: ");
		result.append(this.mergeGoalComponent);
		result.append(", componentName: ");
		result.append(this.componentName);
		result.append(", operations: ");
		result.append(this.operations);
		result.append(')');
		return result.toString();
	}

} // MergeComponentImpl
