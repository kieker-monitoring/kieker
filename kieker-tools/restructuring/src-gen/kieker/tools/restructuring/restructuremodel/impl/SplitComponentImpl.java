/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import java.util.Collection;

import kieker.tools.restructuring.restructuremodel.CreateComponent;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;
import kieker.tools.restructuring.restructuremodel.SplitComponent;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EDataTypeUniqueEList;
import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Split Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getNewComponent <em>New Component</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getOperationsToMove <em>Operations To Move</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getOldComponent <em>Old Component</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getCreateComponent <em>Create Component</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.SplitComponentImpl#getMoveOperations <em>Move Operations</em>}</li>
 * </ul>
 *
 * @generated
 */
public class SplitComponentImpl extends AbstractTransformationStepImpl implements SplitComponent {
	/**
	 * The default value of the '{@link #getNewComponent() <em>New Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewComponent()
	 * @generated
	 * @ordered
	 */
	protected static final String NEW_COMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNewComponent() <em>New Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNewComponent()
	 * @generated
	 * @ordered
	 */
	protected String newComponent = NEW_COMPONENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOperationsToMove() <em>Operations To Move</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationsToMove()
	 * @generated
	 * @ordered
	 */
	protected EList<String> operationsToMove;

	/**
	 * The default value of the '{@link #getOldComponent() <em>Old Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldComponent()
	 * @generated
	 * @ordered
	 */
	protected static final String OLD_COMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOldComponent() <em>Old Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOldComponent()
	 * @generated
	 * @ordered
	 */
	protected String oldComponent = OLD_COMPONENT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCreateComponent() <em>Create Component</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCreateComponent()
	 * @generated
	 * @ordered
	 */
	protected CreateComponent createComponent;

	/**
	 * The cached value of the '{@link #getMoveOperations() <em>Move Operations</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMoveOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<MoveOperation> moveOperations;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected SplitComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RestructuremodelPackage.Literals.SPLIT_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getNewComponent() {
		return newComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setNewComponent(String newNewComponent) {
		String oldNewComponent = newComponent;
		newComponent = newNewComponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT, oldNewComponent, newComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getOperationsToMove() {
		if (operationsToMove == null) {
			operationsToMove = new EDataTypeUniqueEList<String>(String.class, this, RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE);
		}
		return operationsToMove;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOldComponent() {
		return oldComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOldComponent(String newOldComponent) {
		String oldOldComponent = oldComponent;
		oldComponent = newOldComponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT, oldOldComponent, oldComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CreateComponent getCreateComponent() {
		return createComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCreateComponent(CreateComponent newCreateComponent, NotificationChain msgs) {
		CreateComponent oldCreateComponent = createComponent;
		createComponent = newCreateComponent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT, oldCreateComponent, newCreateComponent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCreateComponent(CreateComponent newCreateComponent) {
		if (newCreateComponent != createComponent) {
			NotificationChain msgs = null;
			if (createComponent != null)
				msgs = ((InternalEObject)createComponent).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT, null, msgs);
			if (newCreateComponent != null)
				msgs = ((InternalEObject)newCreateComponent).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT, null, msgs);
			msgs = basicSetCreateComponent(newCreateComponent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT, newCreateComponent, newCreateComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MoveOperation> getMoveOperations() {
		if (moveOperations == null) {
			moveOperations = new EObjectContainmentEList<MoveOperation>(MoveOperation.class, this, RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS);
		}
		return moveOperations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
				return basicSetCreateComponent(null, msgs);
			case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
				return ((InternalEList<?>)getMoveOperations()).basicRemove(otherEnd, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
				return getNewComponent();
			case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
				return getOperationsToMove();
			case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
				return getOldComponent();
			case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
				return getCreateComponent();
			case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
				return getMoveOperations();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
				setNewComponent((String)newValue);
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
				getOperationsToMove().clear();
				getOperationsToMove().addAll((Collection<? extends String>)newValue);
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
				setOldComponent((String)newValue);
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
				setCreateComponent((CreateComponent)newValue);
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
				getMoveOperations().clear();
				getMoveOperations().addAll((Collection<? extends MoveOperation>)newValue);
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
			case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
				setNewComponent(NEW_COMPONENT_EDEFAULT);
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
				getOperationsToMove().clear();
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
				setOldComponent(OLD_COMPONENT_EDEFAULT);
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
				setCreateComponent((CreateComponent)null);
				return;
			case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
				getMoveOperations().clear();
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
			case RestructuremodelPackage.SPLIT_COMPONENT__NEW_COMPONENT:
				return NEW_COMPONENT_EDEFAULT == null ? newComponent != null : !NEW_COMPONENT_EDEFAULT.equals(newComponent);
			case RestructuremodelPackage.SPLIT_COMPONENT__OPERATIONS_TO_MOVE:
				return operationsToMove != null && !operationsToMove.isEmpty();
			case RestructuremodelPackage.SPLIT_COMPONENT__OLD_COMPONENT:
				return OLD_COMPONENT_EDEFAULT == null ? oldComponent != null : !OLD_COMPONENT_EDEFAULT.equals(oldComponent);
			case RestructuremodelPackage.SPLIT_COMPONENT__CREATE_COMPONENT:
				return createComponent != null;
			case RestructuremodelPackage.SPLIT_COMPONENT__MOVE_OPERATIONS:
				return moveOperations != null && !moveOperations.isEmpty();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (newComponent: ");
		result.append(newComponent);
		result.append(", operationsToMove: ");
		result.append(operationsToMove);
		result.append(", oldComponent: ");
		result.append(oldComponent);
		result.append(')');
		return result.toString();
	}

} //SplitComponentImpl
