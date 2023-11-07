/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.AStmtType;
import org.oceandsl.tools.sar.fxtran.ActionStmtType;
import org.oceandsl.tools.sar.fxtran.AllocateStmtType;
import org.oceandsl.tools.sar.fxtran.CallStmtType;
import org.oceandsl.tools.sar.fxtran.CycleStmtType;
import org.oceandsl.tools.sar.fxtran.DeallocateStmtType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.PointerAStmtType;
import org.oceandsl.tools.sar.fxtran.WhereStmtType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Action Stmt Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getReturnStmt <em>Return
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getWhereStmt <em>Where
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getAStmt <em>AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getAllocateStmt <em>Allocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getCallStmt <em>Call
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getDeallocateStmt <em>Deallocate
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getExitStmt <em>Exit
 * Stmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getPointerAStmt <em>Pointer
 * AStmt</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl#getCycleStmt <em>Cycle
 * Stmt</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ActionStmtTypeImpl extends MinimalEObjectImpl.Container implements ActionStmtType {
    /**
     * The default value of the '{@link #getReturnStmt() <em>Return Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getReturnStmt()
     * @generated
     * @ordered
     */
    protected static final String RETURN_STMT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getReturnStmt() <em>Return Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getReturnStmt()
     * @generated
     * @ordered
     */
    protected String returnStmt = RETURN_STMT_EDEFAULT;

    /**
     * The cached value of the '{@link #getWhereStmt() <em>Where Stmt</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getWhereStmt()
     * @generated
     * @ordered
     */
    protected WhereStmtType whereStmt;

    /**
     * The cached value of the '{@link #getAStmt() <em>AStmt</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getAStmt()
     * @generated
     * @ordered
     */
    protected AStmtType aStmt;

    /**
     * The cached value of the '{@link #getAllocateStmt() <em>Allocate Stmt</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getAllocateStmt()
     * @generated
     * @ordered
     */
    protected AllocateStmtType allocateStmt;

    /**
     * The cached value of the '{@link #getCallStmt() <em>Call Stmt</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getCallStmt()
     * @generated
     * @ordered
     */
    protected CallStmtType callStmt;

    /**
     * The cached value of the '{@link #getDeallocateStmt() <em>Deallocate Stmt</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getDeallocateStmt()
     * @generated
     * @ordered
     */
    protected DeallocateStmtType deallocateStmt;

    /**
     * The default value of the '{@link #getExitStmt() <em>Exit Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getExitStmt()
     * @generated
     * @ordered
     */
    protected static final String EXIT_STMT_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getExitStmt() <em>Exit Stmt</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getExitStmt()
     * @generated
     * @ordered
     */
    protected String exitStmt = EXIT_STMT_EDEFAULT;

    /**
     * The cached value of the '{@link #getPointerAStmt() <em>Pointer AStmt</em>}' containment
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getPointerAStmt()
     * @generated
     * @ordered
     */
    protected PointerAStmtType pointerAStmt;

    /**
     * The cached value of the '{@link #getCycleStmt() <em>Cycle Stmt</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getCycleStmt()
     * @generated
     * @ordered
     */
    protected CycleStmtType cycleStmt;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ActionStmtTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getActionStmtType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getReturnStmt() {
        return this.returnStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setReturnStmt(final String newReturnStmt) {
        final String oldReturnStmt = this.returnStmt;
        this.returnStmt = newReturnStmt;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__RETURN_STMT,
                    oldReturnStmt, this.returnStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public WhereStmtType getWhereStmt() {
        return this.whereStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetWhereStmt(final WhereStmtType newWhereStmt, NotificationChain msgs) {
        final WhereStmtType oldWhereStmt = this.whereStmt;
        this.whereStmt = newWhereStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT, oldWhereStmt, newWhereStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setWhereStmt(final WhereStmtType newWhereStmt) {
        if (newWhereStmt != this.whereStmt) {
            NotificationChain msgs = null;
            if (this.whereStmt != null) {
                msgs = ((InternalEObject) this.whereStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT, null, msgs);
            }
            if (newWhereStmt != null) {
                msgs = ((InternalEObject) newWhereStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT, null, msgs);
            }
            msgs = this.basicSetWhereStmt(newWhereStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT,
                    newWhereStmt, newWhereStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AStmtType getAStmt() {
        return this.aStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAStmt(final AStmtType newAStmt, NotificationChain msgs) {
        final AStmtType oldAStmt = this.aStmt;
        this.aStmt = newAStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ACTION_STMT_TYPE__ASTMT, oldAStmt, newAStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAStmt(final AStmtType newAStmt) {
        if (newAStmt != this.aStmt) {
            NotificationChain msgs = null;
            if (this.aStmt != null) {
                msgs = ((InternalEObject) this.aStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__ASTMT, null, msgs);
            }
            if (newAStmt != null) {
                msgs = ((InternalEObject) newAStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__ASTMT, null, msgs);
            }
            msgs = this.basicSetAStmt(newAStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__ASTMT, newAStmt,
                    newAStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public AllocateStmtType getAllocateStmt() {
        return this.allocateStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetAllocateStmt(final AllocateStmtType newAllocateStmt, NotificationChain msgs) {
        final AllocateStmtType oldAllocateStmt = this.allocateStmt;
        this.allocateStmt = newAllocateStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT, oldAllocateStmt, newAllocateStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setAllocateStmt(final AllocateStmtType newAllocateStmt) {
        if (newAllocateStmt != this.allocateStmt) {
            NotificationChain msgs = null;
            if (this.allocateStmt != null) {
                msgs = ((InternalEObject) this.allocateStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT, null, msgs);
            }
            if (newAllocateStmt != null) {
                msgs = ((InternalEObject) newAllocateStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT, null, msgs);
            }
            msgs = this.basicSetAllocateStmt(newAllocateStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT,
                    newAllocateStmt, newAllocateStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CallStmtType getCallStmt() {
        return this.callStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCallStmt(final CallStmtType newCallStmt, NotificationChain msgs) {
        final CallStmtType oldCallStmt = this.callStmt;
        this.callStmt = newCallStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ACTION_STMT_TYPE__CALL_STMT, oldCallStmt, newCallStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCallStmt(final CallStmtType newCallStmt) {
        if (newCallStmt != this.callStmt) {
            NotificationChain msgs = null;
            if (this.callStmt != null) {
                msgs = ((InternalEObject) this.callStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__CALL_STMT, null, msgs);
            }
            if (newCallStmt != null) {
                msgs = ((InternalEObject) newCallStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__CALL_STMT, null, msgs);
            }
            msgs = this.basicSetCallStmt(newCallStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__CALL_STMT,
                    newCallStmt, newCallStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public DeallocateStmtType getDeallocateStmt() {
        return this.deallocateStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetDeallocateStmt(final DeallocateStmtType newDeallocateStmt,
            NotificationChain msgs) {
        final DeallocateStmtType oldDeallocateStmt = this.deallocateStmt;
        this.deallocateStmt = newDeallocateStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT, oldDeallocateStmt, newDeallocateStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setDeallocateStmt(final DeallocateStmtType newDeallocateStmt) {
        if (newDeallocateStmt != this.deallocateStmt) {
            NotificationChain msgs = null;
            if (this.deallocateStmt != null) {
                msgs = ((InternalEObject) this.deallocateStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT, null, msgs);
            }
            if (newDeallocateStmt != null) {
                msgs = ((InternalEObject) newDeallocateStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT, null, msgs);
            }
            msgs = this.basicSetDeallocateStmt(newDeallocateStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT,
                    newDeallocateStmt, newDeallocateStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getExitStmt() {
        return this.exitStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setExitStmt(final String newExitStmt) {
        final String oldExitStmt = this.exitStmt;
        this.exitStmt = newExitStmt;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__EXIT_STMT,
                    oldExitStmt, this.exitStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public PointerAStmtType getPointerAStmt() {
        return this.pointerAStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetPointerAStmt(final PointerAStmtType newPointerAStmt, NotificationChain msgs) {
        final PointerAStmtType oldPointerAStmt = this.pointerAStmt;
        this.pointerAStmt = newPointerAStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT, oldPointerAStmt, newPointerAStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setPointerAStmt(final PointerAStmtType newPointerAStmt) {
        if (newPointerAStmt != this.pointerAStmt) {
            NotificationChain msgs = null;
            if (this.pointerAStmt != null) {
                msgs = ((InternalEObject) this.pointerAStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT, null, msgs);
            }
            if (newPointerAStmt != null) {
                msgs = ((InternalEObject) newPointerAStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT, null, msgs);
            }
            msgs = this.basicSetPointerAStmt(newPointerAStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT,
                    newPointerAStmt, newPointerAStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public CycleStmtType getCycleStmt() {
        return this.cycleStmt;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetCycleStmt(final CycleStmtType newCycleStmt, NotificationChain msgs) {
        final CycleStmtType oldCycleStmt = this.cycleStmt;
        this.cycleStmt = newCycleStmt;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT, oldCycleStmt, newCycleStmt);
            if (msgs == null) {
                msgs = notification;
            } else {
                msgs.add(notification);
            }
        }
        return msgs;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setCycleStmt(final CycleStmtType newCycleStmt) {
        if (newCycleStmt != this.cycleStmt) {
            NotificationChain msgs = null;
            if (this.cycleStmt != null) {
                msgs = ((InternalEObject) this.cycleStmt).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT, null, msgs);
            }
            if (newCycleStmt != null) {
                msgs = ((InternalEObject) newCycleStmt).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT, null, msgs);
            }
            msgs = this.basicSetCycleStmt(newCycleStmt, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT,
                    newCycleStmt, newCycleStmt));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public NotificationChain eInverseRemove(final InternalEObject otherEnd, final int featureID,
            final NotificationChain msgs) {
        switch (featureID) {
        case FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT:
            return this.basicSetWhereStmt(null, msgs);
        case FxtranPackage.ACTION_STMT_TYPE__ASTMT:
            return this.basicSetAStmt(null, msgs);
        case FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT:
            return this.basicSetAllocateStmt(null, msgs);
        case FxtranPackage.ACTION_STMT_TYPE__CALL_STMT:
            return this.basicSetCallStmt(null, msgs);
        case FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT:
            return this.basicSetDeallocateStmt(null, msgs);
        case FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT:
            return this.basicSetPointerAStmt(null, msgs);
        case FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT:
            return this.basicSetCycleStmt(null, msgs);
        }
        return super.eInverseRemove(otherEnd, featureID, msgs);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public Object eGet(final int featureID, final boolean resolve, final boolean coreType) {
        switch (featureID) {
        case FxtranPackage.ACTION_STMT_TYPE__RETURN_STMT:
            return this.getReturnStmt();
        case FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT:
            return this.getWhereStmt();
        case FxtranPackage.ACTION_STMT_TYPE__ASTMT:
            return this.getAStmt();
        case FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT:
            return this.getAllocateStmt();
        case FxtranPackage.ACTION_STMT_TYPE__CALL_STMT:
            return this.getCallStmt();
        case FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT:
            return this.getDeallocateStmt();
        case FxtranPackage.ACTION_STMT_TYPE__EXIT_STMT:
            return this.getExitStmt();
        case FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT:
            return this.getPointerAStmt();
        case FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT:
            return this.getCycleStmt();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eSet(final int featureID, final Object newValue) {
        switch (featureID) {
        case FxtranPackage.ACTION_STMT_TYPE__RETURN_STMT:
            this.setReturnStmt((String) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT:
            this.setWhereStmt((WhereStmtType) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__ASTMT:
            this.setAStmt((AStmtType) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT:
            this.setAllocateStmt((AllocateStmtType) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__CALL_STMT:
            this.setCallStmt((CallStmtType) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT:
            this.setDeallocateStmt((DeallocateStmtType) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__EXIT_STMT:
            this.setExitStmt((String) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT:
            this.setPointerAStmt((PointerAStmtType) newValue);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT:
            this.setCycleStmt((CycleStmtType) newValue);
            return;
        }
        super.eSet(featureID, newValue);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void eUnset(final int featureID) {
        switch (featureID) {
        case FxtranPackage.ACTION_STMT_TYPE__RETURN_STMT:
            this.setReturnStmt(RETURN_STMT_EDEFAULT);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT:
            this.setWhereStmt((WhereStmtType) null);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__ASTMT:
            this.setAStmt((AStmtType) null);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT:
            this.setAllocateStmt((AllocateStmtType) null);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__CALL_STMT:
            this.setCallStmt((CallStmtType) null);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT:
            this.setDeallocateStmt((DeallocateStmtType) null);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__EXIT_STMT:
            this.setExitStmt(EXIT_STMT_EDEFAULT);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT:
            this.setPointerAStmt((PointerAStmtType) null);
            return;
        case FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT:
            this.setCycleStmt((CycleStmtType) null);
            return;
        }
        super.eUnset(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public boolean eIsSet(final int featureID) {
        switch (featureID) {
        case FxtranPackage.ACTION_STMT_TYPE__RETURN_STMT:
            return RETURN_STMT_EDEFAULT == null ? this.returnStmt != null
                    : !RETURN_STMT_EDEFAULT.equals(this.returnStmt);
        case FxtranPackage.ACTION_STMT_TYPE__WHERE_STMT:
            return this.whereStmt != null;
        case FxtranPackage.ACTION_STMT_TYPE__ASTMT:
            return this.aStmt != null;
        case FxtranPackage.ACTION_STMT_TYPE__ALLOCATE_STMT:
            return this.allocateStmt != null;
        case FxtranPackage.ACTION_STMT_TYPE__CALL_STMT:
            return this.callStmt != null;
        case FxtranPackage.ACTION_STMT_TYPE__DEALLOCATE_STMT:
            return this.deallocateStmt != null;
        case FxtranPackage.ACTION_STMT_TYPE__EXIT_STMT:
            return EXIT_STMT_EDEFAULT == null ? this.exitStmt != null : !EXIT_STMT_EDEFAULT.equals(this.exitStmt);
        case FxtranPackage.ACTION_STMT_TYPE__POINTER_ASTMT:
            return this.pointerAStmt != null;
        case FxtranPackage.ACTION_STMT_TYPE__CYCLE_STMT:
            return this.cycleStmt != null;
        }
        return super.eIsSet(featureID);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String toString() {
        if (this.eIsProxy()) {
            return super.toString();
        }

        final StringBuilder result = new StringBuilder(super.toString());
        result.append(" (returnStmt: ");
        result.append(this.returnStmt);
        result.append(", exitStmt: ");
        result.append(this.exitStmt);
        result.append(')');
        return result.toString();
    }

} // ActionStmtTypeImpl
