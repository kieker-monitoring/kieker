/**
 */
package kieker.tools.restructuring.restructuremodel.impl;

import kieker.tools.restructuring.restructuremodel.CutOperation;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.PasteOperation;
import kieker.tools.restructuring.restructuremodel.RestructuremodelPackage;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Move Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getFrom <em>From</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getTo <em>To</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getOperationName <em>Operation Name</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getCutOperation <em>Cut Operation</em>}</li>
 *   <li>{@link kieker.tools.restructuring.restructuremodel.impl.MoveOperationImpl#getPasteOperation <em>Paste Operation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MoveOperationImpl extends AbstractTransformationStepImpl implements MoveOperation {
	/**
	 * The default value of the '{@link #getFrom() <em>From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected static final String FROM_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFrom() <em>From</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFrom()
	 * @generated
	 * @ordered
	 */
	protected String from = FROM_EDEFAULT;

	/**
	 * The default value of the '{@link #getTo() <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected static final String TO_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getTo() <em>To</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTo()
	 * @generated
	 * @ordered
	 */
	protected String to = TO_EDEFAULT;

	/**
	 * The default value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationName()
	 * @generated
	 * @ordered
	 */
	protected static final String OPERATION_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOperationName() <em>Operation Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOperationName()
	 * @generated
	 * @ordered
	 */
	protected String operationName = OPERATION_NAME_EDEFAULT;

	/**
	 * The cached value of the '{@link #getCutOperation() <em>Cut Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCutOperation()
	 * @generated
	 * @ordered
	 */
	protected CutOperation cutOperation;

	/**
	 * The cached value of the '{@link #getPasteOperation() <em>Paste Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPasteOperation()
	 * @generated
	 * @ordered
	 */
	protected PasteOperation pasteOperation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MoveOperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return RestructuremodelPackage.Literals.MOVE_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getFrom() {
		return from;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setFrom(String newFrom) {
		String oldFrom = from;
		from = newFrom;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__FROM, oldFrom, from));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getTo() {
		return to;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setTo(String newTo) {
		String oldTo = to;
		to = newTo;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__TO, oldTo, to));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String getOperationName() {
		return operationName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setOperationName(String newOperationName) {
		String oldOperationName = operationName;
		operationName = newOperationName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME, oldOperationName, operationName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CutOperation getCutOperation() {
		return cutOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetCutOperation(CutOperation newCutOperation, NotificationChain msgs) {
		CutOperation oldCutOperation = cutOperation;
		cutOperation = newCutOperation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION, oldCutOperation, newCutOperation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setCutOperation(CutOperation newCutOperation) {
		if (newCutOperation != cutOperation) {
			NotificationChain msgs = null;
			if (cutOperation != null)
				msgs = ((InternalEObject)cutOperation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION, null, msgs);
			if (newCutOperation != null)
				msgs = ((InternalEObject)newCutOperation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION, null, msgs);
			msgs = basicSetCutOperation(newCutOperation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION, newCutOperation, newCutOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PasteOperation getPasteOperation() {
		return pasteOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPasteOperation(PasteOperation newPasteOperation, NotificationChain msgs) {
		PasteOperation oldPasteOperation = pasteOperation;
		pasteOperation = newPasteOperation;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION, oldPasteOperation, newPasteOperation);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void setPasteOperation(PasteOperation newPasteOperation) {
		if (newPasteOperation != pasteOperation) {
			NotificationChain msgs = null;
			if (pasteOperation != null)
				msgs = ((InternalEObject)pasteOperation).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION, null, msgs);
			if (newPasteOperation != null)
				msgs = ((InternalEObject)newPasteOperation).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION, null, msgs);
			msgs = basicSetPasteOperation(newPasteOperation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION, newPasteOperation, newPasteOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
				return basicSetCutOperation(null, msgs);
			case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
				return basicSetPasteOperation(null, msgs);
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
			case RestructuremodelPackage.MOVE_OPERATION__FROM:
				return getFrom();
			case RestructuremodelPackage.MOVE_OPERATION__TO:
				return getTo();
			case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
				return getOperationName();
			case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
				return getCutOperation();
			case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
				return getPasteOperation();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case RestructuremodelPackage.MOVE_OPERATION__FROM:
				setFrom((String)newValue);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__TO:
				setTo((String)newValue);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
				setOperationName((String)newValue);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
				setCutOperation((CutOperation)newValue);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
				setPasteOperation((PasteOperation)newValue);
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
			case RestructuremodelPackage.MOVE_OPERATION__FROM:
				setFrom(FROM_EDEFAULT);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__TO:
				setTo(TO_EDEFAULT);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
				setOperationName(OPERATION_NAME_EDEFAULT);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
				setCutOperation((CutOperation)null);
				return;
			case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
				setPasteOperation((PasteOperation)null);
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
			case RestructuremodelPackage.MOVE_OPERATION__FROM:
				return FROM_EDEFAULT == null ? from != null : !FROM_EDEFAULT.equals(from);
			case RestructuremodelPackage.MOVE_OPERATION__TO:
				return TO_EDEFAULT == null ? to != null : !TO_EDEFAULT.equals(to);
			case RestructuremodelPackage.MOVE_OPERATION__OPERATION_NAME:
				return OPERATION_NAME_EDEFAULT == null ? operationName != null : !OPERATION_NAME_EDEFAULT.equals(operationName);
			case RestructuremodelPackage.MOVE_OPERATION__CUT_OPERATION:
				return cutOperation != null;
			case RestructuremodelPackage.MOVE_OPERATION__PASTE_OPERATION:
				return pasteOperation != null;
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
		result.append(" (from: ");
		result.append(from);
		result.append(", to: ");
		result.append(to);
		result.append(", operationName: ");
		result.append(operationName);
		result.append(')');
		return result.toString();
	}

} //MoveOperationImpl
