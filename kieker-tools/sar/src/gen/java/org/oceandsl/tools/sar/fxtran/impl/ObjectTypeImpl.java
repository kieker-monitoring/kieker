/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import java.math.BigInteger;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.oceandsl.tools.sar.fxtran.FileType;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;
import org.oceandsl.tools.sar.fxtran.ObjectType;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>Object Type</b></em>'. <!--
 * end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ObjectTypeImpl#getFile <em>File</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ObjectTypeImpl#getOpenacc <em>Openacc</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ObjectTypeImpl#getOpenmp <em>Openmp</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ObjectTypeImpl#getSourceForm <em>Source
 * Form</em>}</li>
 * <li>{@link org.oceandsl.tools.sar.fxtran.impl.ObjectTypeImpl#getSourceWidth <em>Source
 * Width</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ObjectTypeImpl extends MinimalEObjectImpl.Container implements ObjectType {
    /**
     * The cached value of the '{@link #getFile() <em>File</em>}' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getFile()
     * @generated
     * @ordered
     */
    protected FileType file;

    /**
     * The default value of the '{@link #getOpenacc() <em>Openacc</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOpenacc()
     * @generated
     * @ordered
     */
    protected static final BigInteger OPENACC_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOpenacc() <em>Openacc</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOpenacc()
     * @generated
     * @ordered
     */
    protected BigInteger openacc = OPENACC_EDEFAULT;

    /**
     * The default value of the '{@link #getOpenmp() <em>Openmp</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getOpenmp()
     * @generated
     * @ordered
     */
    protected static final BigInteger OPENMP_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getOpenmp() <em>Openmp</em>}' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @see #getOpenmp()
     * @generated
     * @ordered
     */
    protected BigInteger openmp = OPENMP_EDEFAULT;

    /**
     * The default value of the '{@link #getSourceForm() <em>Source Form</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSourceForm()
     * @generated
     * @ordered
     */
    protected static final String SOURCE_FORM_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSourceForm() <em>Source Form</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSourceForm()
     * @generated
     * @ordered
     */
    protected String sourceForm = SOURCE_FORM_EDEFAULT;

    /**
     * The default value of the '{@link #getSourceWidth() <em>Source Width</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSourceWidth()
     * @generated
     * @ordered
     */
    protected static final BigInteger SOURCE_WIDTH_EDEFAULT = null;

    /**
     * The cached value of the '{@link #getSourceWidth() <em>Source Width</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #getSourceWidth()
     * @generated
     * @ordered
     */
    protected BigInteger sourceWidth = SOURCE_WIDTH_EDEFAULT;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected ObjectTypeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return FxtranPackage.eINSTANCE.getObjectType();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FileType getFile() {
        return this.file;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public NotificationChain basicSetFile(final FileType newFile, NotificationChain msgs) {
        final FileType oldFile = this.file;
        this.file = newFile;
        if (this.eNotificationRequired()) {
            final ENotificationImpl notification = new ENotificationImpl(this, Notification.SET,
                    FxtranPackage.OBJECT_TYPE__FILE, oldFile, newFile);
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
    public void setFile(final FileType newFile) {
        if (newFile != this.file) {
            NotificationChain msgs = null;
            if (this.file != null) {
                msgs = ((InternalEObject) this.file).eInverseRemove(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.OBJECT_TYPE__FILE, null, msgs);
            }
            if (newFile != null) {
                msgs = ((InternalEObject) newFile).eInverseAdd(this,
                        EOPPOSITE_FEATURE_BASE - FxtranPackage.OBJECT_TYPE__FILE, null, msgs);
            }
            msgs = this.basicSetFile(newFile, msgs);
            if (msgs != null) {
                msgs.dispatch();
            }
        } else if (this.eNotificationRequired()) {
            this.eNotify(
                    new ENotificationImpl(this, Notification.SET, FxtranPackage.OBJECT_TYPE__FILE, newFile, newFile));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public BigInteger getOpenacc() {
        return this.openacc;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOpenacc(final BigInteger newOpenacc) {
        final BigInteger oldOpenacc = this.openacc;
        this.openacc = newOpenacc;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.OBJECT_TYPE__OPENACC, oldOpenacc,
                    this.openacc));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public BigInteger getOpenmp() {
        return this.openmp;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setOpenmp(final BigInteger newOpenmp) {
        final BigInteger oldOpenmp = this.openmp;
        this.openmp = newOpenmp;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.OBJECT_TYPE__OPENMP, oldOpenmp,
                    this.openmp));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public String getSourceForm() {
        return this.sourceForm;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSourceForm(final String newSourceForm) {
        final String oldSourceForm = this.sourceForm;
        this.sourceForm = newSourceForm;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.OBJECT_TYPE__SOURCE_FORM,
                    oldSourceForm, this.sourceForm));
        }
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public BigInteger getSourceWidth() {
        return this.sourceWidth;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public void setSourceWidth(final BigInteger newSourceWidth) {
        final BigInteger oldSourceWidth = this.sourceWidth;
        this.sourceWidth = newSourceWidth;
        if (this.eNotificationRequired()) {
            this.eNotify(new ENotificationImpl(this, Notification.SET, FxtranPackage.OBJECT_TYPE__SOURCE_WIDTH,
                    oldSourceWidth, this.sourceWidth));
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
        case FxtranPackage.OBJECT_TYPE__FILE:
            return this.basicSetFile(null, msgs);
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
        case FxtranPackage.OBJECT_TYPE__FILE:
            return this.getFile();
        case FxtranPackage.OBJECT_TYPE__OPENACC:
            return this.getOpenacc();
        case FxtranPackage.OBJECT_TYPE__OPENMP:
            return this.getOpenmp();
        case FxtranPackage.OBJECT_TYPE__SOURCE_FORM:
            return this.getSourceForm();
        case FxtranPackage.OBJECT_TYPE__SOURCE_WIDTH:
            return this.getSourceWidth();
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
        case FxtranPackage.OBJECT_TYPE__FILE:
            this.setFile((FileType) newValue);
            return;
        case FxtranPackage.OBJECT_TYPE__OPENACC:
            this.setOpenacc((BigInteger) newValue);
            return;
        case FxtranPackage.OBJECT_TYPE__OPENMP:
            this.setOpenmp((BigInteger) newValue);
            return;
        case FxtranPackage.OBJECT_TYPE__SOURCE_FORM:
            this.setSourceForm((String) newValue);
            return;
        case FxtranPackage.OBJECT_TYPE__SOURCE_WIDTH:
            this.setSourceWidth((BigInteger) newValue);
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
        case FxtranPackage.OBJECT_TYPE__FILE:
            this.setFile((FileType) null);
            return;
        case FxtranPackage.OBJECT_TYPE__OPENACC:
            this.setOpenacc(OPENACC_EDEFAULT);
            return;
        case FxtranPackage.OBJECT_TYPE__OPENMP:
            this.setOpenmp(OPENMP_EDEFAULT);
            return;
        case FxtranPackage.OBJECT_TYPE__SOURCE_FORM:
            this.setSourceForm(SOURCE_FORM_EDEFAULT);
            return;
        case FxtranPackage.OBJECT_TYPE__SOURCE_WIDTH:
            this.setSourceWidth(SOURCE_WIDTH_EDEFAULT);
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
        case FxtranPackage.OBJECT_TYPE__FILE:
            return this.file != null;
        case FxtranPackage.OBJECT_TYPE__OPENACC:
            return OPENACC_EDEFAULT == null ? this.openacc != null : !OPENACC_EDEFAULT.equals(this.openacc);
        case FxtranPackage.OBJECT_TYPE__OPENMP:
            return OPENMP_EDEFAULT == null ? this.openmp != null : !OPENMP_EDEFAULT.equals(this.openmp);
        case FxtranPackage.OBJECT_TYPE__SOURCE_FORM:
            return SOURCE_FORM_EDEFAULT == null ? this.sourceForm != null
                    : !SOURCE_FORM_EDEFAULT.equals(this.sourceForm);
        case FxtranPackage.OBJECT_TYPE__SOURCE_WIDTH:
            return SOURCE_WIDTH_EDEFAULT == null ? this.sourceWidth != null
                    : !SOURCE_WIDTH_EDEFAULT.equals(this.sourceWidth);
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
        result.append(" (openacc: ");
        result.append(this.openacc);
        result.append(", openmp: ");
        result.append(this.openmp);
        result.append(", sourceForm: ");
        result.append(this.sourceForm);
        result.append(", sourceWidth: ");
        result.append(this.sourceWidth);
        result.append(')');
        return result.toString();
    }

} // ObjectTypeImpl
