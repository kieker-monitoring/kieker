/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import kieker.tools.restructuring.restructuremodel.CutOperation;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.PasteOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Move Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getFrom <em>From</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getTo <em>To</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getOperationName <em>Operation Name</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getCutOperation <em>Cut Operation</em>}</li>
 * <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getPasteOperation <em>Paste Operation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MoveOperationImpl extends AbstractTransformationStepImpl implements MoveOperation {
	/**
	 * The default value of the '{@link #getFrom() <em>From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected static final String FROM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected String from = FROM_EDEFAULT;

	/**
	 * The default value of the '{@link #getTo() <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected static final String TO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected String to = TO_EDEFAULT;

	/**
	 * The default value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationName()
	 * @generated
	 * @ordered
	 */
	protected static final String OPERATION_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getOperationName()
	 * @generated
	 * @ordered
	 */
	protected String operationName = OPERATION_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCutOperation() <em>Cut Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getCutOperation()
	 * @generated
	 * @ordered
	 */
	protected CutOperation cutOperation;

	/**
	 * The cached value of the '{@link #getPasteOperation() <em>Paste Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #getPasteOperation()
	 * @generated
	 * @ordered
	 */
	protected PasteOperation pasteOperation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected MoveOperationImpl() {
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
		return RestructuremodelPackage.Literals.MOVE_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getFrom() {
		return this.from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setFrom(final String newFrom) {
		final String oldFrom = this.from;
		this.from = newFrom;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__FROM, oldFrom, this.from));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getTo() {
		return this.to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setTo(final String newTo) {
		final String oldTo = this.to;
		this.to = newTo;
		if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__TO, oldTo, this.to));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String getOperationName() {
		return this.operationName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void setOperationName(final String newOperationName) {
		final String oldOperationName = this.operationName;
		this.operationName = newOperationName;
		if (this.eNotificationRequired()) {
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME, oldOperationName, this.operationName));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public CutOperation getCutOperation() {
		return this.cutOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetCutOperation(final CutOperation newCutOperation, NotificationChain msgs) {
		final CutOperation oldCutOperation = this.cutOperation;
		this.cutOperation = newCutOperation;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION,
					oldCutOperation,
					newCutOperation);
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
	public void setCutOperation(final CutOperation newCutOperation) {
		if (newCutOperation != this.cutOperation) {
			NotificationChain msgs = null;
			if (this.cutOperation != null) {
				msgs = ((InternalEObject) this.cutOperation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION,
						null,
						msgs);
			}
			if (newCutOperation != null) {
				msgs = ((InternalEObject) newCutOperation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION, null,
						msgs);
			}
			msgs = this.basicSetCutOperation(newCutOperation, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION, newCutOperation, newCutOperation));
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public PasteOperation getPasteOperation() {
		return this.pasteOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public NotificationChain basicSetPasteOperation(final PasteOperation newPasteOperation, NotificationChain msgs) {
		final PasteOperation oldPasteOperation = this.pasteOperation;
		this.pasteOperation = newPasteOperation;
		if (this.eNotificationRequired()) {
			final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION,
					oldPasteOperation, newPasteOperation);
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
	public void setPasteOperation(final PasteOperation newPasteOperation) {
		if (newPasteOperation != this.pasteOperation) {
			NotificationChain msgs = null;
			if (this.pasteOperation != null) {
				msgs = ((InternalEObject) this.pasteOperation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION,
						null, msgs);
			}
			if (newPasteOperation != null) {
				msgs = ((InternalEObject) newPasteOperation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION,
						null, msgs);
			}
			msgs = this.basicSetPasteOperation(newPasteOperation, msgs);
			if (msgs != null) {
				msgs.dispatch();
			}
		} else if (this.eNotificationRequired()) {
			this.eNotify(
					new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION, newPasteOperation, newPasteOperation));
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
		case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
			return this.basicSetCutOperation(null, msgs);
		case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
			return this.basicSetPasteOperation(null, msgs);
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
		case RestructuremodelPackage.MOVE_OPERATION__FROM:
			return this.getFrom();
		case RestructuremodelPackage.MOVE_OPERATION__TO:
			return this.getTo();
		case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
			return this.getOperationName();
		case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
			return this.getCutOperation();
		case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
			return this.getPasteOperation();
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
		case RestructuremodelPackage.MOVE_OPERATION__FROM:
			this.setFrom((String) newValue);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__TO:
			this.setTo((String) newValue);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
			this.setOperationName((String) newValue);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
			this.setCutOperation((CutOperation) newValue);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
			this.setPasteOperation((PasteOperation) newValue);
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
		case RestructuremodelPackage.MOVE_OPERATION__FROM:
			this.setFrom(FROM_EDEFAULT);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__TO:
			this.setTo(TO_EDEFAULT);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
			this.setOperationName(OPERATION_NAME_EDEFAULT);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
			this.setCutOperation((CutOperation) null);
			return;
		case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
			this.setPasteOperation((PasteOperation) null);
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
		case RestructuremodelPackage.MOVE_OPERATION__FROM:
			return FROM_EDEFAULT == null ? this.from != null : !FROM_EDEFAULT.equals(this.from);
		case RestructuremodelPackage.MOVE_OPERATION__TO:
			return TO_EDEFAULT == null ? this.to != null : !TO_EDEFAULT.equals(this.to);
		case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
			return OPERATION_NAME_EDEFAULT == null ? this.operationName != null : !OPERATION_NAME_EDEFAULT.equals(this.operationName);
		case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
			return this.cutOperation != null;
		case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
			return this.pasteOperation != null;
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
		result.append(" (from: ");
		result.append(this.from);
		result.append(", to: ");
		result.append(this.to);
		result.append(", operationName: ");
		result.append(this.operationName);
		result.append(')');
		return result.toString();
	}

} // MoveOperationImpl
