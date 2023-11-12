/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import java.util.Collection;

import kieker.tools.restructuring.restructuremodel.DeleteComponent;
import kieker.tools.restructuring.restructuremodel.MergeComponent;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;

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
 * An implementation of the model object '<em><b>Merge Component</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getMergeGoalComponent <em>Merge Goal Component</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getComponentName <em>Component Name</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getOperations <em>Operations</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getDeleteTransformation <em>Delete Transformation</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MergeComponentImpl#getOperationToMove <em>Operation To Move</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MergeComponentImpl extends AbstractTransformationStepImpl implements MergeComponent {
	/**
	 * The default value of the '{@link #getMergeGoalComponent() <em>Merge Goal Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMergeGoalComponent()
	 * @generated
	 * @ordered
	 */
	protected static final String MERGE_GOAL_COMPONENT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getMergeGoalComponent() <em>Merge Goal Component</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMergeGoalComponent()
	 * @generated
	 * @ordered
	 */
	protected String mergeGoalComponent = MERGE_GOAL_COMPONENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getComponentName() <em>Component Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected static final String COMPONENT_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getComponentName() <em>Component Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getComponentName()
	 * @generated
	 * @ordered
	 */
	protected String componentName = COMPONENT_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getOperations() <em>Operations</em>}' attribute list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperations()
	 * @generated
	 * @ordered
	 */
	protected EList<String> operations;

	/**
	 * The cached value of the '{@link #getDeleteTransformation() <em>Delete Transformation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDeleteTransformation()
	 * @generated
	 * @ordered
	 */
	protected DeleteComponent deleteTransformation;

	/**
	 * The cached value of the '{@link #getOperationToMove() <em>Operation To Move</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationToMove()
	 * @generated
	 * @ordered
	 */
	protected EList<MoveOperation> operationToMove;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MergeComponentImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RestructuremodelPackage.Literals.MERGE_COMPONENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getMergeGoalComponent() {
		return mergeGoalComponent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setMergeGoalComponent(String newMergeGoalComponent) {
		String oldMergeGoalComponent = mergeGoalComponent;
		mergeGoalComponent = newMergeGoalComponent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT, oldMergeGoalComponent, mergeGoalComponent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getComponentName() {
		return componentName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setComponentName(String newComponentName) {
		String oldComponentName = componentName;
		componentName = newComponentName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME, oldComponentName, componentName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<String> getOperations() {
		if (operations == null) {
			operations = new EDataTypeUniqueEList<String>(String.class, this, RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS);
		}
		return operations;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeleteComponent getDeleteTransformation() {
		return deleteTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDeleteTransformation(DeleteComponent newDeleteTransformation, NotificationChain msgs) {
		DeleteComponent oldDeleteTransformation = deleteTransformation;
		deleteTransformation = newDeleteTransformation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION, oldDeleteTransformation, newDeleteTransformation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setDeleteTransformation(DeleteComponent newDeleteTransformation) {
		if (newDeleteTransformation != deleteTransformation) {
			NotificationChain msgs = null;
			if (deleteTransformation != null)
				msgs = ((InternalEObject)deleteTransformation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION, null, msgs);
			if (newDeleteTransformation != null)
				msgs = ((InternalEObject)newDeleteTransformation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION, null, msgs);
			msgs = basicSetDeleteTransformation(newDeleteTransformation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION, newDeleteTransformation, newDeleteTransformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EList<MoveOperation> getOperationToMove() {
		if (operationToMove == null) {
			operationToMove = new EObjectContainmentEList<MoveOperation>(MoveOperation.class, this, RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE);
		}
		return operationToMove;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
				return basicSetDeleteTransformation(null, msgs);
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
				return ((InternalEList<?>)getOperationToMove()).basicRemove(otherEnd, msgs);
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
			case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
				return getMergeGoalComponent();
			case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
				return getComponentName();
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
				return getOperations();
			case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
				return getDeleteTransformation();
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
				return getOperationToMove();
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
			case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
				setMergeGoalComponent((String)newValue);
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
				setComponentName((String)newValue);
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
				getOperations().clear();
				getOperations().addAll((Collection<? extends String>)newValue);
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
				setDeleteTransformation((DeleteComponent)newValue);
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
				getOperationToMove().clear();
				getOperationToMove().addAll((Collection<? extends MoveOperation>)newValue);
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
			case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
				setMergeGoalComponent(MERGE_GOAL_COMPONENT_EDEFAULT);
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
				setComponentName(COMPONENT_NAME_EDEFAULT);
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
				getOperations().clear();
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
				setDeleteTransformation((DeleteComponent)null);
				return;
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
				getOperationToMove().clear();
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
			case RestructuremodelPackage.MERGE_COMPONENT__MERGE_GOAL_COMPONENT:
				return MERGE_GOAL_COMPONENT_EDEFAULT == null ? mergeGoalComponent != null : !MERGE_GOAL_COMPONENT_EDEFAULT.equals(mergeGoalComponent);
			case RestructuremodelPackage.MERGE_COMPONENT__COMPONENT_NAME:
				return COMPONENT_NAME_EDEFAULT == null ? componentName != null : !COMPONENT_NAME_EDEFAULT.equals(componentName);
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATIONS:
				return operations != null && !operations.isEmpty();
			case RestructuremodelPackage.MERGE_COMPONENT__DELETE_TRANSFORMATION:
				return deleteTransformation != null;
			case RestructuremodelPackage.MERGE_COMPONENT__OPERATION_TO_MOVE:
				return operationToMove != null && !operationToMove.isEmpty();
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
		result.append(" (mergeGoalComponent: ");
		result.append(mergeGoalComponent);
		result.append(", componentName: ");
		result.append(componentName);
		result.append(", operations: ");
		result.append(operations);
		result.append(')');
		return result.toString();
	}

} //MergeComponentImpl
