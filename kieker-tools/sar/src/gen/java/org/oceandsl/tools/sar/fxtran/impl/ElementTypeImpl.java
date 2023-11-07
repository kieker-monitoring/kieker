/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.BasicFeatureMap;
import org.eclipse.emf.ecore.util.FeatureMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.oceandsl.tools.sar.fxtran.ArrayConstructorEType;
import org.oceandsl.tools.sar.fxtran.ElementType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.LiteralEType;
import org.oceandsl.tools.sar.fxtran.NamedEType;
import org.oceandsl.tools.sar.fxtran.OpEType;
import org.oceandsl.tools.sar.fxtran.StringEType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Element Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl#getArrayConstructorE <em>Array
 * Constructor E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl#getGroup <em>Group</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl#getNamedE <em>Named E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl#getOpE <em>Op E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl#getLiteralE <em>Literal
 * E</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl#getStringE <em>String E</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ElementTypeImpl extends MinimalEObjectImpl.Container implements ElementType {
    /**
     * The cached value of the '{@link #getArrayConstructorE() <em>Array Constructor E</em>}'
     * containment reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getArrayConstructorE()
     * @generated
     * @ordered
     */
    protected ArrayConstructorEType arrayConstructorE;

    /**
     * The cached value of the '{@link #getGroup() <em>Group</em>}' attribute list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getGroup()
     * @generated
     * @ordered
     */
    protected FeatureMap group;

    /**
     * The cached value of the '{@link #getLiteralE() <em>Literal E</em>}' containment reference.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getLiteralE()
     * @generated
     * @ordered
     */
    protected LiteralEType literalE;

    /**
     * The cached value of the '{@link #getStringE() <em>String E</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getStringE()
     * @generated
     * @ordered
     */
    protected StringEType stringE;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ElementTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getElementType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public ArrayConstructorEType getArrayConstructorE() {
        return this.arrayConstructorE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetArrayConstructorE(final ArrayConstructorEType newArrayConstructorE,
            NotificationChain msgs) {
        final ArrayConstructorEType oldArrayConstructorE = this.arrayConstructorE;
        this.arrayConstructorE = newArrayConstructorE;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E, oldArrayConstructorE, newArrayConstructorE);
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
    public void setArrayConstructorE(final ArrayConstructorEType newArrayConstructorE) {
        if (newArrayConstructorE != this.arrayConstructorE) {
            NotificationChain msgs = null;
            if (this.arrayConstructorE != null) {
                msgs = ((InternalEObject) this.arrayConstructorE).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E, null, msgs);
            }
            if (newArrayConstructorE != null) {
                msgs = ((InternalEObject) newArrayConstructorE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E, null, msgs);
            }
            msgs = this.basicSetArrayConstructorE(newArrayConstructorE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E,
                    newArrayConstructorE, newArrayConstructorE));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FeatureMap getGroup() {
        if (this.group == null) {
            this.group = new BasicFeatureMap(this, FxtranPackage.ELEMENT_TYPE__GROUP);
        }
        return this.group;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<NamedEType> getNamedE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getElementType_NamedE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EList<OpEType> getOpE() {
        return this.getGroup().list(FxtranPackage.eINSTANCE.getElementType_OpE());
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public LiteralEType getLiteralE() {
        return this.literalE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetLiteralE(final LiteralEType newLiteralE, NotificationChain msgs) {
        final LiteralEType oldLiteralE = this.literalE;
        this.literalE = newLiteralE;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ELEMENT_TYPE__LITERAL_E, oldLiteralE, newLiteralE);
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
    public void setLiteralE(final LiteralEType newLiteralE) {
        if (newLiteralE != this.literalE) {
            NotificationChain msgs = null;
            if (this.literalE != null) {
                msgs = ((InternalEObject) this.literalE).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ELEMENT_TYPE__LITERAL_E, null, msgs);
            }
            if (newLiteralE != null) {
                msgs = ((InternalEObject) newLiteralE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ELEMENT_TYPE__LITERAL_E, null, msgs);
            }
            msgs = this.basicSetLiteralE(newLiteralE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ELEMENT_TYPE__LITERAL_E,
                    newLiteralE, newLiteralE));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public StringEType getStringE() {
        return this.stringE;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetStringE(final StringEType newStringE, NotificationChain msgs) {
        final StringEType oldStringE = this.stringE;
        this.stringE = newStringE;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.ELEMENT_TYPE__STRING_E, oldStringE, newStringE);
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
    public void setStringE(final StringEType newStringE) {
        if (newStringE != this.stringE) {
            NotificationChain msgs = null;
            if (this.stringE != null) {
                msgs = ((InternalEObject) this.stringE).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ELEMENT_TYPE__STRING_E, null, msgs);
            }
            if (newStringE != null) {
                msgs = ((InternalEObject) newStringE).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.ELEMENT_TYPE__STRING_E, null, msgs);
            }
            msgs = this.basicSetStringE(newStringE, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.ELEMENT_TYPE__STRING_E, newStringE,
                    newStringE));
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
        case FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E:
            return this.basicSetArrayConstructorE(null, msgs);
        case FxtranPackage.ELEMENT_TYPE__GROUP:
            return ((InternalEList<?>) this.getGroup()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ELEMENT_TYPE__NAMED_E:
            return ((InternalEList<?>) this.getNamedE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ELEMENT_TYPE__OP_E:
            return ((InternalEList<?>) this.getOpE()).basicRemove(otherEnd, msgs);
        case FxtranPackage.ELEMENT_TYPE__LITERAL_E:
            return this.basicSetLiteralE(null, msgs);
        case FxtranPackage.ELEMENT_TYPE__STRING_E:
            return this.basicSetStringE(null, msgs);
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
        case FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E:
            return this.getArrayConstructorE();
        case FxtranPackage.ELEMENT_TYPE__GROUP:
            if (coreType) {
                return this.getGroup();
            }
            return ((FeatureMap.Internal) this.getGroup()).getWrapper();
        case FxtranPackage.ELEMENT_TYPE__NAMED_E:
            return this.getNamedE();
        case FxtranPackage.ELEMENT_TYPE__OP_E:
            return this.getOpE();
        case FxtranPackage.ELEMENT_TYPE__LITERAL_E:
            return this.getLiteralE();
        case FxtranPackage.ELEMENT_TYPE__STRING_E:
            return this.getStringE();
        }
        return super.eGet(featureID, resolve, coreType);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @SuppressWarnings("unchecked")
    @Override
    public void eSet(final int featureID, final Object newValue) {
        switch (featureID) {
        case FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E:
            this.setArrayConstructorE((ArrayConstructorEType) newValue);
            return;
        case FxtranPackage.ELEMENT_TYPE__GROUP:
            ((FeatureMap.Internal) this.getGroup()).set(newValue);
            return;
        case FxtranPackage.ELEMENT_TYPE__NAMED_E:
            this.getNamedE().clear();
            this.getNamedE().addAll((Collection<? extends NamedEType>) newValue);
            return;
        case FxtranPackage.ELEMENT_TYPE__OP_E:
            this.getOpE().clear();
            this.getOpE().addAll((Collection<? extends OpEType>) newValue);
            return;
        case FxtranPackage.ELEMENT_TYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) newValue);
            return;
        case FxtranPackage.ELEMENT_TYPE__STRING_E:
            this.setStringE((StringEType) newValue);
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
        case FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E:
            this.setArrayConstructorE((ArrayConstructorEType) null);
            return;
        case FxtranPackage.ELEMENT_TYPE__GROUP:
            this.getGroup().clear();
            return;
        case FxtranPackage.ELEMENT_TYPE__NAMED_E:
            this.getNamedE().clear();
            return;
        case FxtranPackage.ELEMENT_TYPE__OP_E:
            this.getOpE().clear();
            return;
        case FxtranPackage.ELEMENT_TYPE__LITERAL_E:
            this.setLiteralE((LiteralEType) null);
            return;
        case FxtranPackage.ELEMENT_TYPE__STRING_E:
            this.setStringE((StringEType) null);
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
        case FxtranPackage.ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E:
            return this.arrayConstructorE != null;
        case FxtranPackage.ELEMENT_TYPE__GROUP:
            return this.group != null && !this.group.isEmpty();
        case FxtranPackage.ELEMENT_TYPE__NAMED_E:
            return !this.getNamedE().isEmpty();
        case FxtranPackage.ELEMENT_TYPE__OP_E:
            return !this.getOpE().isEmpty();
        case FxtranPackage.ELEMENT_TYPE__LITERAL_E:
            return this.literalE != null;
        case FxtranPackage.ELEMENT_TYPE__STRING_E:
            return this.stringE != null;
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
        result.append(" (group: ");
        result.append(this.group);
        result.append(')');
        return result.toString();
    }

} // ElementTypeImpl
