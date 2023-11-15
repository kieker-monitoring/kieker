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
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getOperation <em>Operation</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getParent <em>Parent</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getChildren <em>Children</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getDuration <em>Duration</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getStart <em>Start</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getDurRatioToParent <em>Dur Ratio To Parent</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getDurRatioToRootParent <em>Dur Ratio To Root Parent</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getStackDepth <em>Stack Depth</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getOrderIndex <em>Order Index</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#isFailed <em>Failed</em>}</li>
 * <li>{@link kieker.model.analysismodel.trace.impl.OperationCallImpl#getFailedCause <em>Failed Cause</em>}</li>
 * </ul>
 *
 * @generated
 */
public class OperationCallImpl extends MinimalEObjectImpl.Container implements OperationCall {
	/**
	 * The cached value of the '{@link #getOperation() <em>Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperation()
	 * @generated
	 * @ordered
	 */
	protected DeployedOperation operation;

	/**
	 * The cached value of the '{@link #getParent() <em>Parent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getParent()
	 * @generated
	 * @ordered
	 */
	protected OperationCall parent;

	/**
	 * The cached value of the '{@link #getChildren() <em>Children</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getChildren()
	 * @generated
	 * @ordered
	 */
	protected EList<OperationCall> children;

	/**
	 * The default value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected static final Duration DURATION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDuration() <em>Duration</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDuration()
	 * @generated
	 * @ordered
	 */
	protected Duration duration = DURATION_EDEFAULT;

	/**
	 * The default value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected static final Instant START_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getStart() <em>Start</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStart()
	 * @generated
	 * @ordered
	 */
	protected Instant start = START_EDEFAULT;

	/**
	 * The default value of the '{@link #getDurRatioToParent() <em>Dur Ratio To Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDurRatioToParent()
	 * @generated
	 * @ordered
	 */
	protected static final float DUR_RATIO_TO_PARENT_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getDurRatioToParent() <em>Dur Ratio To Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDurRatioToParent()
	 * @generated
	 * @ordered
	 */
	protected float durRatioToParent = DUR_RATIO_TO_PARENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDurRatioToRootParent() <em>Dur Ratio To Root Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDurRatioToRootParent()
	 * @generated
	 * @ordered
	 */
	protected static final float DUR_RATIO_TO_ROOT_PARENT_EDEFAULT = 0.0F;

	/**
	 * The cached value of the '{@link #getDurRatioToRootParent() <em>Dur Ratio To Root Parent</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getDurRatioToRootParent()
	 * @generated
	 * @ordered
	 */
	protected float durRatioToRootParent = DUR_RATIO_TO_ROOT_PARENT_EDEFAULT;

	/**
	 * The default value of the '{@link #getStackDepth() <em>Stack Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStackDepth()
	 * @generated
	 * @ordered
	 */
	protected static final int STACK_DEPTH_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getStackDepth() <em>Stack Depth</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getStackDepth()
	 * @generated
	 * @ordered
	 */
	protected int stackDepth = STACK_DEPTH_EDEFAULT;

	/**
	 * The default value of the '{@link #getOrderIndex() <em>Order Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOrderIndex()
	 * @generated
	 * @ordered
	 */
	protected static final int ORDER_INDEX_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getOrderIndex() <em>Order Index</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOrderIndex()
	 * @generated
	 * @ordered
	 */
	protected int orderIndex = ORDER_INDEX_EDEFAULT;

	/**
	 * The default value of the '{@link #isFailed() <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isFailed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FAILED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFailed() <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #isFailed()
	 * @generated
	 * @ordered
	 */
	protected boolean failed = FAILED_EDEFAULT;

	/**
	 * The default value of the '{@link #getFailedCause() <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getFailedCause()
	 * @generated
	 * @ordered
	 */
	protected static final String FAILED_CAUSE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFailedCause() <em>Failed Cause</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getFailedCause()
	 * @generated
	 * @ordered
	 */
	protected String failedCause = FAILED_CAUSE_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected OperationCallImpl() {
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
		return TracePackage.Literals.OPERATION_CALL;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DeployedOperation getOperation() {
		if ((this.operation != null) && this.operation.eIsProxy()) {
			final InternalEObject oldOperation = (InternalEObject) this.operation;
			this.operation = (DeployedOperation) this.eResolveProxy(oldOperation);
			if (this.operation != oldOperation) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.OPERATION_CALL__OPERATION, oldOperation, this.operation));
				}
			}
		}
		return this.operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public DeployedOperation basicGetOperation() {
		return this.operation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setOperation(final DeployedOperation newOperation) {
		final DeployedOperation oldOperation = this.operation;
		this.operation = newOperation;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__OPERATION, oldOperation, this.operation));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public OperationCall getParent() {
		if ((this.parent != null) && this.parent.eIsProxy()) {
			final InternalEObject oldParent = (InternalEObject) this.parent;
			this.parent = (OperationCall) this.eResolveProxy(oldParent);
			if (this.parent != oldParent) {
				if (this.eNotificationRequired()) {
					this.eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.OPERATION_CALL__PARENT, oldParent, this.parent));
				}
			}
		}
		return this.parent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public OperationCall basicGetParent() {
		return this.parent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetParent(final OperationCall newParent, NotificationChain msgs) {
		final OperationCall oldParent = this.parent;
		this.parent = newParent;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__PARENT, oldParent, newParent);
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
	@Override
	public void setParent(final OperationCall newParent) {
		if (newParent != this.parent) {
			NotificationChain msgs = null;
			if (this.parent != null) {
				msgs = ((InternalEObject) this.parent).eInverseRemove(this, TracePackage.OPERATION_CALL__CHILDREN, OperationCall.class, msgs);
			}
			if (newParent != null) {
				msgs = ((InternalEObject) newParent).eInverseAdd(this, TracePackage.OPERATION_CALL__CHILDREN, OperationCall.class, msgs);
			}
			msgs = this.basicSetParent(newParent, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__PARENT, newParent, newParent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EList<OperationCall> getChildren() {
		if (this.children == null) {
			this.children = new EObjectWithInverseResolvingEList<>(OperationCall.class, this, TracePackage.OPERATION_CALL__CHILDREN,
					TracePackage.OPERATION_CALL__PARENT);
		}
		return this.children;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Duration getDuration() {
		return this.duration;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDuration(final Duration newDuration) {
		final Duration oldDuration = this.duration;
		this.duration = newDuration;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__DURATION, oldDuration, this.duration));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Instant getStart() {
		return this.start;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStart(final Instant newStart) {
		final Instant oldStart = this.start;
		this.start = newStart;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__START, oldStart, this.start));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public float getDurRatioToParent() {
		return this.durRatioToParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDurRatioToParent(final float newDurRatioToParent) {
		final float oldDurRatioToParent = this.durRatioToParent;
		this.durRatioToParent = newDurRatioToParent;
		if (this.eNotificationRequired()) {
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT, oldDurRatioToParent, this.durRatioToParent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public float getDurRatioToRootParent() {
		return this.durRatioToRootParent;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setDurRatioToRootParent(final float newDurRatioToRootParent) {
		final float oldDurRatioToRootParent = this.durRatioToRootParent;
		this.durRatioToRootParent = newDurRatioToRootParent;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT, oldDurRatioToRootParent,
					this.durRatioToRootParent));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getStackDepth() {
		return this.stackDepth;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setStackDepth(final int newStackDepth) {
		final int oldStackDepth = this.stackDepth;
		this.stackDepth = newStackDepth;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__STACK_DEPTH, oldStackDepth, this.stackDepth));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getOrderIndex() {
		return this.orderIndex;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setOrderIndex(final int newOrderIndex) {
		final int oldOrderIndex = this.orderIndex;
		this.orderIndex = newOrderIndex;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__ORDER_INDEX, oldOrderIndex, this.orderIndex));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public boolean isFailed() {
		return this.failed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setFailed(final boolean newFailed) {
		final boolean oldFailed = this.failed;
		this.failed = newFailed;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__FAILED, oldFailed, this.failed));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getFailedCause() {
		return this.failedCause;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public void setFailedCause(final String newFailedCause) {
		final String oldFailedCause = this.failedCause;
		this.failedCause = newFailedCause;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.OPERATION_CALL__FAILED_CAUSE, oldFailedCause, this.failedCause));
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
	public NotificationChain eInverseAdd(final InternalEObject otherEnd, final int featureID, NotificationChain msgs) {
		switch (featureID) {
		case TracePackage.OPERATION_CALL__PARENT:
			if (this.parent != null) {
				msgs = ((InternalEObject) this.parent).eInverseRemove(this, TracePackage.OPERATION_CALL__CHILDREN, OperationCall.class, msgs);
			}
			return this.basicSetParent((OperationCall) otherEnd, msgs);
		case TracePackage.OPERATION_CALL__CHILDREN:
			return ((InternalEList<InternalEObject>) (InternalEList<?>) this.getChildren()).basicAdd(otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
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
		case TracePackage.OPERATION_CALL__PARENT:
			return this.basicSetParent(null, msgs);
		case TracePackage.OPERATION_CALL__CHILDREN:
			return ((InternalEList<?>) this.getChildren()).basicRemove(otherEnd, msgs);
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
		case TracePackage.OPERATION_CALL__OPERATION:
			if (resolve) {
				return this.getOperation();
			}
			return this.basicGetOperation();
		case TracePackage.OPERATION_CALL__PARENT:
			if (resolve) {
				return this.getParent();
			}
			return this.basicGetParent();
		case TracePackage.OPERATION_CALL__CHILDREN:
			return this.getChildren();
		case TracePackage.OPERATION_CALL__DURATION:
			return this.getDuration();
		case TracePackage.OPERATION_CALL__START:
			return this.getStart();
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
			return this.getDurRatioToParent();
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
			return this.getDurRatioToRootParent();
		case TracePackage.OPERATION_CALL__STACK_DEPTH:
			return this.getStackDepth();
		case TracePackage.OPERATION_CALL__ORDER_INDEX:
			return this.getOrderIndex();
		case TracePackage.OPERATION_CALL__FAILED:
			return this.isFailed();
		case TracePackage.OPERATION_CALL__FAILED_CAUSE:
			return this.getFailedCause();
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
		case TracePackage.OPERATION_CALL__OPERATION:
			this.setOperation((DeployedOperation) newValue);
			return;
		case TracePackage.OPERATION_CALL__PARENT:
			this.setParent((OperationCall) newValue);
			return;
		case TracePackage.OPERATION_CALL__CHILDREN:
			this.getChildren().clear();
			this.getChildren().addAll((Collection<? extends OperationCall>) newValue);
			return;
		case TracePackage.OPERATION_CALL__DURATION:
			this.setDuration((Duration) newValue);
			return;
		case TracePackage.OPERATION_CALL__START:
			this.setStart((Instant) newValue);
			return;
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
			this.setDurRatioToParent((Float) newValue);
			return;
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
			this.setDurRatioToRootParent((Float) newValue);
			return;
		case TracePackage.OPERATION_CALL__STACK_DEPTH:
			this.setStackDepth((Integer) newValue);
			return;
		case TracePackage.OPERATION_CALL__ORDER_INDEX:
			this.setOrderIndex((Integer) newValue);
			return;
		case TracePackage.OPERATION_CALL__FAILED:
			this.setFailed((Boolean) newValue);
			return;
		case TracePackage.OPERATION_CALL__FAILED_CAUSE:
			this.setFailedCause((String) newValue);
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
		case TracePackage.OPERATION_CALL__OPERATION:
			this.setOperation((DeployedOperation) null);
			return;
		case TracePackage.OPERATION_CALL__PARENT:
			this.setParent((OperationCall) null);
			return;
		case TracePackage.OPERATION_CALL__CHILDREN:
			this.getChildren().clear();
			return;
		case TracePackage.OPERATION_CALL__DURATION:
			this.setDuration(DURATION_EDEFAULT);
			return;
		case TracePackage.OPERATION_CALL__START:
			this.setStart(START_EDEFAULT);
			return;
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
			this.setDurRatioToParent(DUR_RATIO_TO_PARENT_EDEFAULT);
			return;
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
			this.setDurRatioToRootParent(DUR_RATIO_TO_ROOT_PARENT_EDEFAULT);
			return;
		case TracePackage.OPERATION_CALL__STACK_DEPTH:
			this.setStackDepth(STACK_DEPTH_EDEFAULT);
			return;
		case TracePackage.OPERATION_CALL__ORDER_INDEX:
			this.setOrderIndex(ORDER_INDEX_EDEFAULT);
			return;
		case TracePackage.OPERATION_CALL__FAILED:
			this.setFailed(FAILED_EDEFAULT);
			return;
		case TracePackage.OPERATION_CALL__FAILED_CAUSE:
			this.setFailedCause(FAILED_CAUSE_EDEFAULT);
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
		case TracePackage.OPERATION_CALL__OPERATION:
			return this.operation != null;
		case TracePackage.OPERATION_CALL__PARENT:
			return this.parent != null;
		case TracePackage.OPERATION_CALL__CHILDREN:
			return (this.children != null) && !this.children.isEmpty();
		case TracePackage.OPERATION_CALL__DURATION:
			return DURATION_EDEFAULT == null ? this.duration != null : !DURATION_EDEFAULT.equals(this.duration);
		case TracePackage.OPERATION_CALL__START:
			return START_EDEFAULT == null ? this.start != null : !START_EDEFAULT.equals(this.start);
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_PARENT:
			return this.durRatioToParent != DUR_RATIO_TO_PARENT_EDEFAULT;
		case TracePackage.OPERATION_CALL__DUR_RATIO_TO_ROOT_PARENT:
			return this.durRatioToRootParent != DUR_RATIO_TO_ROOT_PARENT_EDEFAULT;
		case TracePackage.OPERATION_CALL__STACK_DEPTH:
			return this.stackDepth != STACK_DEPTH_EDEFAULT;
		case TracePackage.OPERATION_CALL__ORDER_INDEX:
			return this.orderIndex != ORDER_INDEX_EDEFAULT;
		case TracePackage.OPERATION_CALL__FAILED:
			return this.failed != FAILED_EDEFAULT;
		case TracePackage.OPERATION_CALL__FAILED_CAUSE:
			return FAILED_CAUSE_EDEFAULT == null ? this.failedCause != null : !FAILED_CAUSE_EDEFAULT.equals(this.failedCause);
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
		result.append(" (duration: ");
		result.append(this.duration);
		result.append(", start: ");
		result.append(this.start);
		result.append(", durRatioToParent: ");
		result.append(this.durRatioToParent);
		result.append(", durRatioToRootParent: ");
		result.append(this.durRatioToRootParent);
		result.append(", stackDepth: ");
		result.append(this.stackDepth);
		result.append(", orderIndex: ");
		result.append(this.orderIndex);
		result.append(", failed: ");
		result.append(this.failed);
		result.append(", failedCause: ");
		result.append(this.failedCause);
		result.append(')');
		return result.toString();
	}

} // OperationCallImpl
