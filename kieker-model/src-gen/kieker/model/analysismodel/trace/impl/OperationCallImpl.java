/**
 */
package kieker.model.analysismodel.trace.impl;

import java.time.Duration;
import java.time.Instant;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectWithInverseResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.trace.OperationCall;
import kieker.model.analysismodel.trace.TracePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Operation Call</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getOperation <em>Operation</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getParent <em>Parent</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getChildren <em>Children</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getDuration <em>Duration</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getStart <em>Start</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getDurRatioToParent <em>Dur Ratio To Parent</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getDurRatioToRootParent <em>Dur Ratio To Root Parent</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getStackDepth <em>Stack Depth</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getOrderIndex <em>Order Index</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#isFailed <em>Failed</em>}</li>
 *   <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getFailedCause <em>Failed Cause</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationCallImpl extends MinimalEObjectImpl.Container implements OperationCall {
	/**
	 * The cached value of the '{@link #getOperation() <em>Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperation()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation operation;

	/**
	 * The cached value of the '{@link #getParent() <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParent()
	 * @generated
	 * @ordered
	 */
	protected OperationCall parent;

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<OperationCall> children;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final Duration DURATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected Duration duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected static final Instant START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected Instant start = START_EDEFAULT;

	/**
	 * The default value of the '{@link #getDurRatioToParent() <em>Dur Ratio To Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurRatioToParent()
	 * @generated
	 * @ordered
	 */
	protected static final float DUR_RATIO_TO_PARENT_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getDurRatioToParent() <em>Dur Ratio To Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurRatioToParent()
	 * @generated
	 * @ordered
	 */
	protected float durRatioToParent = DUR_RATIO_TO_PARENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDurRatioToRootParent() <em>Dur Ratio To Root Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurRatioToRootParent()
	 * @generated
	 * @ordered
	 */
	protected static final float DUR_RATIO_TO_ROOT_PARENT_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getDurRatioToRootParent() <em>Dur Ratio To Root Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDurRatioToRootParent()
	 * @generated
	 * @ordered
	 */
	protected float durRatioToRootParent = DUR_RATIO_TO_ROOT_PARENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getStackDepth() <em>Stack Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStackDepth()
	 * @generated
	 * @ordered
	 */
	protected static final int STACK_DEPTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStackDepth() <em>Stack Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStackDepth()
	 * @generated
	 * @ordered
	 */
	protected int stackDepth = STACK_DEPTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getOrderIndex() <em>Order Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrderIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int ORDER_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOrderIndex() <em>Order Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOrderIndex()
	 * @generated
	 * @ordered
	 */
	protected int orderIndex = ORDER_INDEX_EDEFAULT;

	/**
	 * The default value of the '{@link #isFailed() <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFailed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FAILED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFailed() <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFailed()
	 * @generated
	 * @ordered
	 */
	protected boolean failed = FAILED_EDEFAULT;

	/**
	 * The default value of the '{@link #getFailedCause() <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedCause()
	 * @generated
	 * @ordered
	 */
	protected static final String FAILED_CAUSE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFailedCause() <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFailedCause()
	 * @generated
	 * @ordered
	 */
	protected String failedCause = FAILED_CAUSE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected OperationCallImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.OPERATION_CALL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DeployedOperation getOperation() {
		if (operation != null && operation.eIsProxy()) {
			InternalEObject oldOperation = (InternalEObject)operation;
			operation = (DeployedOperation)eResolveProxy(oldOperation);
			if (operation != oldOperation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.OPERATION_CALL__OPERATION, oldOperation, operation));
			}
		}
		return operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DeployedOperation basicGetOperation() {
		return operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOperation(DeployedOperation newOperation) {
		DeployedOperation oldOperation = operation;
		operation = newOperation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__OPERATION, oldOperation, operation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OperationCall getParent() {
		if (parent != null && parent.eIsProxy()) {
			InternalEObject oldParent = (InternalEObject)parent;
			parent = (OperationCall)eResolveProxy(oldParent);
			if (parent != oldParent) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.OPERATION_CALL__PARENT, oldParent, parent));
			}
		}
		return parent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationCall basicGetParent() {
		return parent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetParent(OperationCall newParent, NotificationChain msgs) {
		OperationCall oldParent = parent;
		parent = newParent;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__PARENT, oldParent, newParent);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setParent(OperationCall newParent) {
		if (newParent != parent) {
			NotificationChain msgs = null;
			if (parent != null)
				msgs = ((InternalEObject)parent).eInverseRemove(this, TracePackage.OPERATION_CALL__CHILDREN, OperationCall.class, msgs);
			if (newParent != null)
				msgs = ((InternalEObject)newParent).eInverseAdd(this, TracePackage.OPERATION_CALL__CHILDREN, OperationCall.class, msgs);
			msgs = basicSetParent(newParent, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__PARENT, newParent, newParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OperationCall> getChildren() {
		if (children == null) {
			children = new EObjectWithInverseResolvingEList<OperationCall>(OperationCall.class, this, TracePackage.OPERATION_CALL__CHILDREN, TracePackage.OPERATION_CALL__PARENT);
		}
		return children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Duration getDuration() {
		return duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDuration(Duration newDuration) {
		Duration oldDuration = duration;
		duration = newDuration;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__DURATION, oldDuration, duration));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Instant getStart() {
		return start;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStart(Instant newStart) {
		Instant oldStart = start;
		start = newStart;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__START, oldStart, start));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getDurRatioToParent() {
		return durRatioToParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDurRatioToParent(float newDurRatioToParent) {
		float oldDurRatioToParent = durRatioToParent;
		durRatioToParent = newDurRatioToParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT, oldDurRatioToParent, durRatioToParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public float getDurRatioToRootParent() {
		return durRatioToRootParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDurRatioToRootParent(float newDurRatioToRootParent) {
		float oldDurRatioToRootParent = durRatioToRootParent;
		durRatioToRootParent = newDurRatioToRootParent;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT, oldDurRatioToRootParent, durRatioToRootParent));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getStackDepth() {
		return stackDepth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStackDepth(int newStackDepth) {
		int oldStackDepth = stackDepth;
		stackDepth = newStackDepth;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__STACK_DEPTH, oldStackDepth, stackDepth));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getOrderIndex() {
		return orderIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOrderIndex(int newOrderIndex) {
		int oldOrderIndex = orderIndex;
		orderIndex = newOrderIndex;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__ORDER_INDEX, oldOrderIndex, orderIndex));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFailed() {
		return failed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFailed(boolean newFailed) {
		boolean oldFailed = failed;
		failed = newFailed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__FAILED, oldFailed, failed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getFailedCause() {
		return failedCause;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFailedCause(String newFailedCause) {
		String oldFailedCause = failedCause;
		failedCause = newFailedCause;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__FAILED_CAUSE, oldFailedCause, failedCause));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TracePackage.OPERATION_CALL__PARENT:
				if (parent != null)
					msgs = ((InternalEObject)parent).eInverseRemove(this, TracePackage.OPERATION_CALL__CHILDREN, OperationCall.class, msgs);
				return basicSetParent((OperationCall)otherEnd, msgs);
			case TracePackage.OPERATION_CALL__CHILDREN:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getChildren()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TracePackage.OPERATION_CALL__PARENT:
				return basicSetParent(null, msgs);
			case TracePackage.OPERATION_CALL__CHILDREN:
				return ((InternalEList<?>)getChildren()).basicRemove(otherEnd, msgs);
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
			case TracePackage.OPERATION_CALL__OPERATION:
				if (resolve) return getOperation();
				return basicGetOperation();
			case TracePackage.OPERATION_CALL__PARENT:
				if (resolve) return getParent();
				return basicGetParent();
			case TracePackage.OPERATION_CALL__CHILDREN:
				return getChildren();
			case TracePackage.OPERATION_CALL__DURATION:
				return getDuration();
			case TracePackage.OPERATION_CALL__START:
				return getStart();
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
				return getDurRatioToParent();
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
				return getDurRatioToRootParent();
			case TracePackage.OPERATION_CALL__STACK_DEPTH:
				return getStackDepth();
			case TracePackage.OPERATION_CALL__ORDER_INDEX:
				return getOrderIndex();
			case TracePackage.OPERATION_CALL__FAILED:
				return isFailed();
			case TracePackage.OPERATION_CALL__FAILED_CAUSE:
				return getFailedCause();
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
			case TracePackage.OPERATION_CALL__OPERATION:
				setOperation((DeployedOperation)newValue);
				return;
			case TracePackage.OPERATION_CALL__PARENT:
				setParent((OperationCall)newValue);
				return;
			case TracePackage.OPERATION_CALL__CHILDREN:
				getChildren().clear();
				getChildren().addAll((Collection<? extends OperationCall>)newValue);
				return;
			case TracePackage.OPERATION_CALL__DURATION:
				setDuration((Duration)newValue);
				return;
			case TracePackage.OPERATION_CALL__START:
				setStart((Instant)newValue);
				return;
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
				setDurRatioToParent((Float)newValue);
				return;
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
				setDurRatioToRootParent((Float)newValue);
				return;
			case TracePackage.OPERATION_CALL__STACK_DEPTH:
				setStackDepth((Integer)newValue);
				return;
			case TracePackage.OPERATION_CALL__ORDER_INDEX:
				setOrderIndex((Integer)newValue);
				return;
			case TracePackage.OPERATION_CALL__FAILED:
				setFailed((Boolean)newValue);
				return;
			case TracePackage.OPERATION_CALL__FAILED_CAUSE:
				setFailedCause((String)newValue);
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
			case TracePackage.OPERATION_CALL__OPERATION:
				setOperation((DeployedOperation)null);
				return;
			case TracePackage.OPERATION_CALL__PARENT:
				setParent((OperationCall)null);
				return;
			case TracePackage.OPERATION_CALL__CHILDREN:
				getChildren().clear();
				return;
			case TracePackage.OPERATION_CALL__DURATION:
				setDuration(DURATION_EDEFAULT);
				return;
			case TracePackage.OPERATION_CALL__START:
				setStart(START_EDEFAULT);
				return;
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
				setDurRatioToParent(DUR_RATIO_TO_PARENT_EDEFAULT);
				return;
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
				setDurRatioToRootParent(DUR_RATIO_TO_ROOT_PARENT_EDEFAULT);
				return;
			case TracePackage.OPERATION_CALL__STACK_DEPTH:
				setStackDepth(STACK_DEPTH_EDEFAULT);
				return;
			case TracePackage.OPERATION_CALL__ORDER_INDEX:
				setOrderIndex(ORDER_INDEX_EDEFAULT);
				return;
			case TracePackage.OPERATION_CALL__FAILED:
				setFailed(FAILED_EDEFAULT);
				return;
			case TracePackage.OPERATION_CALL__FAILED_CAUSE:
				setFailedCause(FAILED_CAUSE_EDEFAULT);
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
			case TracePackage.OPERATION_CALL__OPERATION:
				return operation != null;
			case TracePackage.OPERATION_CALL__PARENT:
				return parent != null;
			case TracePackage.OPERATION_CALL__CHILDREN:
				return children != null && !children.isEmpty();
			case TracePackage.OPERATION_CALL__DURATION:
				return DURATION_EDEFAULT == null ? duration != null : !DURATION_EDEFAULT.equals(duration);
			case TracePackage.OPERATION_CALL__START:
				return START_EDEFAULT == null ? start != null : !START_EDEFAULT.equals(start);
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
				return durRatioToParent != DUR_RATIO_TO_PARENT_EDEFAULT;
			case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
				return durRatioToRootParent != DUR_RATIO_TO_ROOT_PARENT_EDEFAULT;
			case TracePackage.OPERATION_CALL__STACK_DEPTH:
				return stackDepth != STACK_DEPTH_EDEFAULT;
			case TracePackage.OPERATION_CALL__ORDER_INDEX:
				return orderIndex != ORDER_INDEX_EDEFAULT;
			case TracePackage.OPERATION_CALL__FAILED:
				return failed != FAILED_EDEFAULT;
			case TracePackage.OPERATION_CALL__FAILED_CAUSE:
				return FAILED_CAUSE_EDEFAULT == null ? failedCause != null : !FAILED_CAUSE_EDEFAULT.equals(failedCause);
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
		result.append(" (duration: ");
		result.append(duration);
		result.append(", start: ");
		result.append(start);
		result.append(", durRatioToParent: ");
		result.append(durRatioToParent);
		result.append(", durRatioToRootParent: ");
		result.append(durRatioToRootParent);
		result.append(", stackDepth: ");
		result.append(stackDepth);
		result.append(", orderIndex: ");
		result.append(orderIndex);
		result.append(", failed: ");
		result.append(failed);
		result.append(", failedCause: ");
		result.append(failedCause);
		result.append(')');
		return result.toString();
	}

} // OperationCallImpl
