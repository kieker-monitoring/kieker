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

import kieker.tools.restructuring.restructuremodel.CreateComponent;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;
import kieker.tools.restructuring.restructuremodel.SplitComponent;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Split Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getNewComponent <em>New Component</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getOperationsToMove <em>Operations To Move</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getOldComponent <em>Old Component</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getCreateComponent <em>Create Component</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getMoveOperations <em>Move Operations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SplitComponentImpl extends AbstractTransformationStepImpl implements SplitComponent {
	/**
	 * The default value of the '{@link #getNewComponent() <em>New Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getNewComponent()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_COMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewComponent() <em>New Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getNewComponent()
	 * @generated
	 * @ordered
	 */
	protected String newComponent = NEW_COMPONENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOperationsToMove() <em>Operations To Move</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationsToMove()
	 * @generated
	 * @ordered
	 */
	protected EList<String> operationsToMove;

	/**
	 * The default value of the '{@link #getOldComponent() <em>Old Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOldComponent()
	 * @generated
	 * @ordered
	 */
	protected static final String OLD_COMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOldComponent() <em>Old Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOldComponent()
	 * @generated
	 * @ordered
	 */
	protected String oldComponent = OLD_COMPONENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCreateComponent() <em>Create Component</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCreateComponent()
	 * @generated
	 * @ordered
	 */
	protected CreateComponent createComponent;

	/**
	 * The cached value of the '{@link #getMoveOperations() <em>Move Operations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getMoveOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<MoveOperation> moveOperations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected SplitComponentImpl() {
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
		return RestructuremodelPackage.Literals.SPLIT_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getNewComponent() {
		return this.newComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setNewComponent(final String newNewComponent) {
		final String oldNewComponent = this.newComponent;
		this.newComponent = newNewComponent;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT, oldNewComponent, this.newComponent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<String> getOperationsToMove() {
		if (this.operationsToMove == null) {
			this.operationsToMove = new EDataTypeUniqueEList<>(String.class, this, RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE);
		}
		return this.operationsToMove;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getOldComponent() {
		return this.oldComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setOldComponent(final String newOldComponent) {
		final String oldOldComponent = this.oldComponent;
		this.oldComponent = newOldComponent;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT, oldOldComponent, this.oldComponent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public CreateComponent getCreateComponent() {
		return this.createComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetCreateComponent(final CreateComponent newCreateComponent, NotificationChain msgs) {
		final CreateComponent oldCreateComponent = this.createComponent;
		this.createComponent = newCreateComponent;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT,
					oldCreateComponent, newCreateComponent);
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
	public void setCreateComponent(final CreateComponent newCreateComponent) {
		if (newCreateComponent != this.createComponent) {
			NotificationChain msgs = null;
			if (this.createComponent != null) {
				msgs = ((InternalEObject) this.createComponent).eInverseRemove(this,
						EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT,
						null, msgs);
			}
			if (newCreateComponent != null) {
				msgs = ((InternalEObject) newCreateComponent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT,
						null, msgs);
			}
			msgs = this.basicSetCreateComponent(newCreateComponent, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT, newCreateComponent,
					newCreateComponent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EList<MoveOperation> getMoveOperations() {
		if (this.moveOperations == null) {
			this.moveOperations = new EObjectContainmentEList<>(MoveOperation.class, this, RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS);
		}
		return this.moveOperations;
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
		case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
			return this.basicSetCreateComponent(null, msgs);
		case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
			return ((InternalEList<?>) this.getMoveOperations()).basicRemove(otherEnd, msgs);
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
		case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
			return this.getNewComponent();
		case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
			return this.getOperationsToMove();
		case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
			return this.getOldComponent();
		case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
			return this.getCreateComponent();
		case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
			return this.getMoveOperations();
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
		case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
			this.setNewComponent((String) newValue);
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
			this.getOperationsToMove().clear();
			this.getOperationsToMove().addAll((Collection<? extends String>) newValue);
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
			this.setOldComponent((String) newValue);
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
			this.setCreateComponent((CreateComponent) newValue);
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
			this.getMoveOperations().clear();
			this.getMoveOperations().addAll((Collection<? extends MoveOperation>) newValue);
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
		case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
			this.setNewComponent(NEW_COMPONENT_EDEFAULT);
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
			this.getOperationsToMove().clear();
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
			this.setOldComponent(OLD_COMPONENT_EDEFAULT);
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
			this.setCreateComponent((CreateComponent) null);
			return;
		case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
			this.getMoveOperations().clear();
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
		case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
			return NEW_COMPONENT_EDEFAULT == null ? this.newComponent != null : !NEW_COMPONENT_EDEFAULT.equals(this.newComponent);
		case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
			return (this.operationsToMove != null) && !this.operationsToMove.isEmpty();
		case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
			return OLD_COMPONENT_EDEFAULT == null ? this.oldComponent != null : !OLD_COMPONENT_EDEFAULT.equals(this.oldComponent);
		case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
			return this.createComponent != null;
		case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
			return (this.moveOperations != null) && !this.moveOperations.isEmpty();
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
		result.append(" (newComponent: ");
		result.append(this.newComponent);
		result.append(", operationsToMove: ");
		result.append(this.operationsToMove);
		result.append(", oldComponent: ");
		result.append(this.oldComponent);
		result.append(')');
		return result.toString();
	}

} // SplitComponentImpl
