/**
 */
package org.oceandsl.tools.sar.fxtran;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc --> The <b>Package</b> for the model. It contains accessors for the meta
 * objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 *
 * @see org.oceandsl.tools.sar.fxtran.FxtranFactory
 * @model kind="package"
 * @generated
 */
public interface FxtranPackage extends EPackage {
    /**
     * The package name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNAME = "fxtran";

    /**
     * The package namespace URI. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_URI = "http://fxtran.net/#syntax";

    /**
     * The package namespace name. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    String eNS_PREFIX = "fxtran";

    /**
     * The singleton instance of the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    FxtranPackage eINSTANCE = org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl.init();

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl
     * <em>Action Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ActionStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getActionStmtType()
     * @generated
     */
    int ACTION_STMT_TYPE = 0;

    /**
     * The feature id for the '<em><b>Return Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__RETURN_STMT = 0;

    /**
     * The feature id for the '<em><b>Where Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__WHERE_STMT = 1;

    /**
     * The feature id for the '<em><b>AStmt</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__ASTMT = 2;

    /**
     * The feature id for the '<em><b>Allocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__ALLOCATE_STMT = 3;

    /**
     * The feature id for the '<em><b>Call Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__CALL_STMT = 4;

    /**
     * The feature id for the '<em><b>Deallocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__DEALLOCATE_STMT = 5;

    /**
     * The feature id for the '<em><b>Exit Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__EXIT_STMT = 6;

    /**
     * The feature id for the '<em><b>Pointer AStmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__POINTER_ASTMT = 7;

    /**
     * The feature id for the '<em><b>Cycle Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE__CYCLE_STMT = 8;

    /**
     * The number of structural features of the '<em>Action Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE_FEATURE_COUNT = 9;

    /**
     * The number of operations of the '<em>Action Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ACTION_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.AcValueLTTypeImpl
     * <em>Ac Value LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.AcValueLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getAcValueLTType()
     * @generated
     */
    int AC_VALUE_LT_TYPE = 1;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_LT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>C</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_LT_TYPE__C = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_LT_TYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Ac Value</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_LT_TYPE__AC_VALUE = 4;

    /**
     * The number of structural features of the '<em>Ac Value LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_LT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Ac Value LT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.AcValueTypeImpl <em>Ac
     * Value Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.AcValueTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getAcValueType()
     * @generated
     */
    int AC_VALUE_TYPE = 2;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_TYPE__LITERAL_E = 0;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_TYPE__NAMED_E = 1;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_TYPE__OP_E = 2;

    /**
     * The feature id for the '<em><b>Parens E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_TYPE__PARENS_E = 3;

    /**
     * The number of structural features of the '<em>Ac Value Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Ac Value Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int AC_VALUE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.AllocateStmtTypeImpl
     * <em>Allocate Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.AllocateStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getAllocateStmtType()
     * @generated
     */
    int ALLOCATE_STMT_TYPE = 3;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ALLOCATE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Arg Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ALLOCATE_STMT_TYPE__ARG_SPEC = 1;

    /**
     * The number of structural features of the '<em>Allocate Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ALLOCATE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Allocate Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ALLOCATE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ArgNTypeImpl <em>Arg
     * NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ArgNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getArgNType()
     * @generated
     */
    int ARG_NTYPE = 4;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_NTYPE__N = 0;

    /**
     * The feature id for the '<em><b>K</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_NTYPE__K = 1;

    /**
     * The feature id for the '<em><b>N1</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_NTYPE__N1 = 2;

    /**
     * The number of structural features of the '<em>Arg NType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_NTYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Arg NType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ArgSpecTypeImpl <em>Arg
     * Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ArgSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getArgSpecType()
     * @generated
     */
    int ARG_SPEC_TYPE = 5;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_SPEC_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Arg</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_SPEC_TYPE__ARG = 3;

    /**
     * The number of structural features of the '<em>Arg Spec Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_SPEC_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Arg Spec Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl <em>Arg
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ArgTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getArgType()
     * @generated
     */
    int ARG_TYPE = 6;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__ARG_N = 2;

    /**
     * The feature id for the '<em><b>Array Constructor E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__ARRAY_CONSTRUCTOR_E = 3;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__LITERAL_E = 4;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__NAMED_E = 5;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__OP_E = 6;

    /**
     * The feature id for the '<em><b>Parens E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__PARENS_E = 7;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE__STRING_E = 8;

    /**
     * The number of structural features of the '<em>Arg Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE_FEATURE_COUNT = 9;

    /**
     * The number of operations of the '<em>Arg Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARG_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.ArrayConstructorETypeImpl <em>Array Constructor
     * EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ArrayConstructorETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getArrayConstructorEType()
     * @generated
     */
    int ARRAY_CONSTRUCTOR_ETYPE = 7;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_CONSTRUCTOR_ETYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_CONSTRUCTOR_ETYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>C</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_CONSTRUCTOR_ETYPE__C = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_CONSTRUCTOR_ETYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Ac Value LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_CONSTRUCTOR_ETYPE__AC_VALUE_LT = 4;

    /**
     * The number of structural features of the '<em>Array Constructor EType</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_CONSTRUCTOR_ETYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Array Constructor EType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_CONSTRUCTOR_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ArrayRTypeImpl
     * <em>Array RType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ArrayRTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getArrayRType()
     * @generated
     */
    int ARRAY_RTYPE = 8;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_RTYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Section Subscript LT</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_RTYPE__SECTION_SUBSCRIPT_LT = 1;

    /**
     * The number of structural features of the '<em>Array RType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_RTYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Array RType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_RTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ArraySpecTypeImpl
     * <em>Array Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ArraySpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getArraySpecType()
     * @generated
     */
    int ARRAY_SPEC_TYPE = 9;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Shape Spec LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_SPEC_TYPE__SHAPE_SPEC_LT = 1;

    /**
     * The number of structural features of the '<em>Array Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_SPEC_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Array Spec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ARRAY_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.AStmtTypeImpl <em>AStmt
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.AStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getAStmtType()
     * @generated
     */
    int ASTMT_TYPE = 10;

    /**
     * The feature id for the '<em><b>E1</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ASTMT_TYPE__E1 = 0;

    /**
     * The feature id for the '<em><b>A</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ASTMT_TYPE__A = 1;

    /**
     * The feature id for the '<em><b>E2</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ASTMT_TYPE__E2 = 2;

    /**
     * The number of structural features of the '<em>AStmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ASTMT_TYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>AStmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ASTMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.AttributeTypeImpl
     * <em>Attribute Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.AttributeTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getAttributeType()
     * @generated
     */
    int ATTRIBUTE_TYPE = 11;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ATTRIBUTE_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ATTRIBUTE_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Array Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ATTRIBUTE_TYPE__ARRAY_SPEC = 2;

    /**
     * The feature id for the '<em><b>Attribute N</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ATTRIBUTE_TYPE__ATTRIBUTE_N = 3;

    /**
     * The feature id for the '<em><b>Intent Spec</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ATTRIBUTE_TYPE__INTENT_SPEC = 4;

    /**
     * The number of structural features of the '<em>Attribute Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ATTRIBUTE_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Attribute Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ATTRIBUTE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CallStmtTypeImpl
     * <em>Call Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CallStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCallStmtType()
     * @generated
     */
    int CALL_STMT_TYPE = 12;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CALL_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CALL_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CALL_STMT_TYPE__ARG_SPEC = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CALL_STMT_TYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Procedure Designator</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CALL_STMT_TYPE__PROCEDURE_DESIGNATOR = 4;

    /**
     * The number of structural features of the '<em>Call Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CALL_STMT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Call Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CALL_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CaseETypeImpl <em>Case
     * EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CaseETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCaseEType()
     * @generated
     */
    int CASE_ETYPE = 13;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_ETYPE__NAMED_E = 0;

    /**
     * The number of structural features of the '<em>Case EType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_ETYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Case EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CaseSelectorTypeImpl
     * <em>Case Selector Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CaseSelectorTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCaseSelectorType()
     * @generated
     */
    int CASE_SELECTOR_TYPE = 14;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_SELECTOR_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Case Value Range LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_SELECTOR_TYPE__CASE_VALUE_RANGE_LT = 1;

    /**
     * The number of structural features of the '<em>Case Selector Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_SELECTOR_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Case Selector Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_SELECTOR_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CaseStmtTypeImpl
     * <em>Case Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CaseStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCaseStmtType()
     * @generated
     */
    int CASE_STMT_TYPE = 15;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Case Selector</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_STMT_TYPE__CASE_SELECTOR = 1;

    /**
     * The number of structural features of the '<em>Case Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Case Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.CaseValueRangeLTTypeImpl <em>Case Value Range LT
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CaseValueRangeLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCaseValueRangeLTType()
     * @generated
     */
    int CASE_VALUE_RANGE_LT_TYPE = 16;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_RANGE_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Case Value Range</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_RANGE_LT_TYPE__CASE_VALUE_RANGE = 1;

    /**
     * The number of structural features of the '<em>Case Value Range LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_RANGE_LT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Case Value Range LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_RANGE_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CaseValueRangeTypeImpl
     * <em>Case Value Range Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CaseValueRangeTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCaseValueRangeType()
     * @generated
     */
    int CASE_VALUE_RANGE_TYPE = 17;

    /**
     * The feature id for the '<em><b>Case Value</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_RANGE_TYPE__CASE_VALUE = 0;

    /**
     * The number of structural features of the '<em>Case Value Range Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_RANGE_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Case Value Range Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_RANGE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CaseValueTypeImpl
     * <em>Case Value Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CaseValueTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCaseValueType()
     * @generated
     */
    int CASE_VALUE_TYPE = 18;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_TYPE__LITERAL_E = 0;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_TYPE__STRING_E = 1;

    /**
     * The number of structural features of the '<em>Case Value Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Case Value Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CASE_VALUE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CharSelectorTypeImpl
     * <em>Char Selector Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CharSelectorTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCharSelectorType()
     * @generated
     */
    int CHAR_SELECTOR_TYPE = 19;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SELECTOR_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Char Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SELECTOR_TYPE__CHAR_SPEC = 1;

    /**
     * The number of structural features of the '<em>Char Selector Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SELECTOR_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Char Selector Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SELECTOR_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl
     * <em>Char Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CharSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCharSpecType()
     * @generated
     */
    int CHAR_SPEC_TYPE = 20;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__ARG_N = 2;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__LABEL = 3;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__LITERAL_E = 4;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__NAMED_E = 5;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__OP_E = 6;

    /**
     * The feature id for the '<em><b>Star E</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE__STAR_E = 7;

    /**
     * The number of structural features of the '<em>Char Spec Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE_FEATURE_COUNT = 8;

    /**
     * The number of operations of the '<em>Char Spec Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CHAR_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CloseSpecSpecTypeImpl
     * <em>Close Spec Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CloseSpecSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCloseSpecSpecType()
     * @generated
     */
    int CLOSE_SPEC_SPEC_TYPE = 21;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Close Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_SPEC_TYPE__CLOSE_SPEC = 1;

    /**
     * The number of structural features of the '<em>Close Spec Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_SPEC_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Close Spec Spec Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CloseSpecTypeImpl
     * <em>Close Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CloseSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCloseSpecType()
     * @generated
     */
    int CLOSE_SPEC_TYPE = 22;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_TYPE__ARG_N = 2;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_TYPE__LITERAL_E = 3;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_TYPE__NAMED_E = 4;

    /**
     * The number of structural features of the '<em>Close Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Close Spec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CloseStmtTypeImpl
     * <em>Close Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CloseStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCloseStmtType()
     * @generated
     */
    int CLOSE_STMT_TYPE = 23;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Close Spec Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_STMT_TYPE__CLOSE_SPEC_SPEC = 1;

    /**
     * The number of structural features of the '<em>Close Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Close Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CLOSE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.ComponentDeclStmtTypeImpl <em>Component Decl Stmt
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ComponentDeclStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getComponentDeclStmtType()
     * @generated
     */
    int COMPONENT_DECL_STMT_TYPE = 24;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_DECL_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_DECL_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>EN Decl LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_DECL_STMT_TYPE__EN_DECL_LT = 2;

    /**
     * The feature id for the '<em><b>TSpec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_DECL_STMT_TYPE__TSPEC = 3;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_DECL_STMT_TYPE__ATTRIBUTE = 4;

    /**
     * The number of structural features of the '<em>Component Decl Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_DECL_STMT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Component Decl Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_DECL_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ComponentRTypeImpl
     * <em>Component RType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ComponentRTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getComponentRType()
     * @generated
     */
    int COMPONENT_RTYPE = 25;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_RTYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Ct</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_RTYPE__CT = 1;

    /**
     * The number of structural features of the '<em>Component RType</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_RTYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Component RType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int COMPONENT_RTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ConditionETypeImpl
     * <em>Condition EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ConditionETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getConditionEType()
     * @generated
     */
    int CONDITION_ETYPE = 26;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONDITION_ETYPE__NAMED_E = 0;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONDITION_ETYPE__OP_E = 1;

    /**
     * The number of structural features of the '<em>Condition EType</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONDITION_ETYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Condition EType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONDITION_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ConnectSpecSpecTypeImpl
     * <em>Connect Spec Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ConnectSpecSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getConnectSpecSpecType()
     * @generated
     */
    int CONNECT_SPEC_SPEC_TYPE = 27;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_SPEC_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Connect Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_SPEC_TYPE__CONNECT_SPEC = 3;

    /**
     * The number of structural features of the '<em>Connect Spec Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_SPEC_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Connect Spec Spec Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ConnectSpecTypeImpl
     * <em>Connect Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ConnectSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getConnectSpecType()
     * @generated
     */
    int CONNECT_SPEC_TYPE = 28;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE__ARG_N = 2;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE__LITERAL_E = 3;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE__NAMED_E = 4;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE__STRING_E = 5;

    /**
     * The number of structural features of the '<em>Connect Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>Connect Spec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CONNECT_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.CycleStmtTypeImpl
     * <em>Cycle Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.CycleStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getCycleStmtType()
     * @generated
     */
    int CYCLE_STMT_TYPE = 29;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CYCLE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CYCLE_STMT_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>Cycle Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CYCLE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Cycle Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int CYCLE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.DeallocateStmtTypeImpl
     * <em>Deallocate Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.DeallocateStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getDeallocateStmtType()
     * @generated
     */
    int DEALLOCATE_STMT_TYPE = 30;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DEALLOCATE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Arg Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DEALLOCATE_STMT_TYPE__ARG_SPEC = 1;

    /**
     * The number of structural features of the '<em>Deallocate Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DEALLOCATE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Deallocate Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DEALLOCATE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.DerivedTSpecTypeImpl
     * <em>Derived TSpec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.DerivedTSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getDerivedTSpecType()
     * @generated
     */
    int DERIVED_TSPEC_TYPE = 31;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DERIVED_TSPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>TN</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DERIVED_TSPEC_TYPE__TN = 1;

    /**
     * The number of structural features of the '<em>Derived TSpec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DERIVED_TSPEC_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Derived TSpec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DERIVED_TSPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl
     * <em>Document Root</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.DocumentRootImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getDocumentRoot()
     * @generated
     */
    int DOCUMENT_ROOT = 32;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MIXED = 0;

    /**
     * The feature id for the '<em><b>XMLNS Prefix Map</b></em>' map. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XMLNS_PREFIX_MAP = 1;

    /**
     * The feature id for the '<em><b>XSI Schema Location</b></em>' map. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__XSI_SCHEMA_LOCATION = 2;

    /**
     * The feature id for the '<em><b>TSpec</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TSPEC = 3;

    /**
     * The feature id for the '<em><b>A</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__A = 4;

    /**
     * The feature id for the '<em><b>AStmt</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ASTMT = 5;

    /**
     * The feature id for the '<em><b>Ac Value</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AC_VALUE = 6;

    /**
     * The feature id for the '<em><b>Ac Value LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__AC_VALUE_LT = 7;

    /**
     * The feature id for the '<em><b>Action Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ACTION_STMT = 8;

    /**
     * The feature id for the '<em><b>Allocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ALLOCATE_STMT = 9;

    /**
     * The feature id for the '<em><b>Arg</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ARG = 10;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ARG_N = 11;

    /**
     * The feature id for the '<em><b>Arg Spec</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ARG_SPEC = 12;

    /**
     * The feature id for the '<em><b>Array Constructor E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ARRAY_CONSTRUCTOR_E = 13;

    /**
     * The feature id for the '<em><b>Array R</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ARRAY_R = 14;

    /**
     * The feature id for the '<em><b>Array Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ARRAY_SPEC = 15;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ATTRIBUTE = 16;

    /**
     * The feature id for the '<em><b>Attribute N</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ATTRIBUTE_N = 17;

    /**
     * The feature id for the '<em><b>C</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__C = 18;

    /**
     * The feature id for the '<em><b>Call Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CALL_STMT = 19;

    /**
     * The feature id for the '<em><b>Case E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CASE_E = 20;

    /**
     * The feature id for the '<em><b>Case Selector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CASE_SELECTOR = 21;

    /**
     * The feature id for the '<em><b>Case Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CASE_STMT = 22;

    /**
     * The feature id for the '<em><b>Case Value</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CASE_VALUE = 23;

    /**
     * The feature id for the '<em><b>Case Value Range</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CASE_VALUE_RANGE = 24;

    /**
     * The feature id for the '<em><b>Case Value Range LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CASE_VALUE_RANGE_LT = 25;

    /**
     * The feature id for the '<em><b>Char Selector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CHAR_SELECTOR = 26;

    /**
     * The feature id for the '<em><b>Char Spec</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CHAR_SPEC = 27;

    /**
     * The feature id for the '<em><b>Close Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CLOSE_SPEC = 28;

    /**
     * The feature id for the '<em><b>Close Spec Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CLOSE_SPEC_SPEC = 29;

    /**
     * The feature id for the '<em><b>Close Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CLOSE_STMT = 30;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CNT = 31;

    /**
     * The feature id for the '<em><b>Component Decl Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COMPONENT_DECL_STMT = 32;

    /**
     * The feature id for the '<em><b>Component R</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__COMPONENT_R = 33;

    /**
     * The feature id for the '<em><b>Condition E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONDITION_E = 34;

    /**
     * The feature id for the '<em><b>Connect Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONNECT_SPEC = 35;

    /**
     * The feature id for the '<em><b>Connect Spec Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONNECT_SPEC_SPEC = 36;

    /**
     * The feature id for the '<em><b>Contains Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CONTAINS_STMT = 37;

    /**
     * The feature id for the '<em><b>Cpp</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CPP = 38;

    /**
     * The feature id for the '<em><b>Ct</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CT = 39;

    /**
     * The feature id for the '<em><b>Cycle Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__CYCLE_STMT = 40;

    /**
     * The feature id for the '<em><b>Deallocate Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DEALLOCATE_STMT = 41;

    /**
     * The feature id for the '<em><b>Derived TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DERIVED_TSPEC = 42;

    /**
     * The feature id for the '<em><b>Do Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DO_STMT = 43;

    /**
     * The feature id for the '<em><b>Do V</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DO_V = 44;

    /**
     * The feature id for the '<em><b>Dummy Arg LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__DUMMY_ARG_LT = 45;

    /**
     * The feature id for the '<em><b>E1</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__E1 = 46;

    /**
     * The feature id for the '<em><b>E2</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__E2 = 47;

    /**
     * The feature id for the '<em><b>Element</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ELEMENT = 48;

    /**
     * The feature id for the '<em><b>Element LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ELEMENT_LT = 49;

    /**
     * The feature id for the '<em><b>Else If Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ELSE_IF_STMT = 50;

    /**
     * The feature id for the '<em><b>Else Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ELSE_STMT = 51;

    /**
     * The feature id for the '<em><b>Else Where Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ELSE_WHERE_STMT = 52;

    /**
     * The feature id for the '<em><b>EN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EN = 53;

    /**
     * The feature id for the '<em><b>EN Decl</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EN_DECL = 54;

    /**
     * The feature id for the '<em><b>EN Decl LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EN_DECL_LT = 55;

    /**
     * The feature id for the '<em><b>ENLT</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ENLT = 56;

    /**
     * The feature id for the '<em><b>ENN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ENN = 57;

    /**
     * The feature id for the '<em><b>End Do Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_DO_STMT = 58;

    /**
     * The feature id for the '<em><b>End Forall Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_FORALL_STMT = 59;

    /**
     * The feature id for the '<em><b>End Function Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_FUNCTION_STMT = 60;

    /**
     * The feature id for the '<em><b>End If Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_IF_STMT = 61;

    /**
     * The feature id for the '<em><b>End Interface Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_INTERFACE_STMT = 62;

    /**
     * The feature id for the '<em><b>End Module Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_MODULE_STMT = 63;

    /**
     * The feature id for the '<em><b>End Program Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_PROGRAM_STMT = 64;

    /**
     * The feature id for the '<em><b>End Select Case Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_SELECT_CASE_STMT = 65;

    /**
     * The feature id for the '<em><b>End Subroutine Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_SUBROUTINE_STMT = 66;

    /**
     * The feature id for the '<em><b>End TStmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_TSTMT = 67;

    /**
     * The feature id for the '<em><b>End Where Stmt</b></em>' attribute. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__END_WHERE_STMT = 68;

    /**
     * The feature id for the '<em><b>Error</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ERROR = 69;

    /**
     * The feature id for the '<em><b>Exit Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__EXIT_STMT = 70;

    /**
     * The feature id for the '<em><b>File</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FILE = 71;

    /**
     * The feature id for the '<em><b>Forall Construct Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FORALL_CONSTRUCT_STMT = 72;

    /**
     * The feature id for the '<em><b>Forall Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FORALL_STMT = 73;

    /**
     * The feature id for the '<em><b>Forall Triplet Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FORALL_TRIPLET_SPEC = 74;

    /**
     * The feature id for the '<em><b>Forall Triplet Spec LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FORALL_TRIPLET_SPEC_LT = 75;

    /**
     * The feature id for the '<em><b>Function N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FUNCTION_N = 76;

    /**
     * The feature id for the '<em><b>Function Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__FUNCTION_STMT = 77;

    /**
     * The feature id for the '<em><b>If Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IF_STMT = 78;

    /**
     * The feature id for the '<em><b>If Then Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IF_THEN_STMT = 79;

    /**
     * The feature id for the '<em><b>Implicit None Stmt</b></em>' attribute. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IMPLICIT_NONE_STMT = 80;

    /**
     * The feature id for the '<em><b>Init E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INIT_E = 81;

    /**
     * The feature id for the '<em><b>Inquire Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INQUIRE_STMT = 82;

    /**
     * The feature id for the '<em><b>Inquiry Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INQUIRY_SPEC = 83;

    /**
     * The feature id for the '<em><b>Inquiry Spec Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INQUIRY_SPEC_SPEC = 84;

    /**
     * The feature id for the '<em><b>Intent Spec</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INTENT_SPEC = 85;

    /**
     * The feature id for the '<em><b>Interface Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INTERFACE_STMT = 86;

    /**
     * The feature id for the '<em><b>Intrinsic TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__INTRINSIC_TSPEC = 87;

    /**
     * The feature id for the '<em><b>Io Control</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IO_CONTROL = 88;

    /**
     * The feature id for the '<em><b>Io Control Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__IO_CONTROL_SPEC = 89;

    /**
     * The feature id for the '<em><b>Iterator</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ITERATOR = 90;

    /**
     * The feature id for the '<em><b>Iterator Definition LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ITERATOR_DEFINITION_LT = 91;

    /**
     * The feature id for the '<em><b>Iterator Element</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__ITERATOR_ELEMENT = 92;

    /**
     * The feature id for the '<em><b>K</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__K = 93;

    /**
     * The feature id for the '<em><b>KSelector</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__KSELECTOR = 94;

    /**
     * The feature id for the '<em><b>KSpec</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__KSPEC = 95;

    /**
     * The feature id for the '<em><b>L</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__L = 96;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LABEL = 97;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LITERAL_E = 98;

    /**
     * The feature id for the '<em><b>Lower Bound</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__LOWER_BOUND = 99;

    /**
     * The feature id for the '<em><b>Mask E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MASK_E = 100;

    /**
     * The feature id for the '<em><b>Module N</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MODULE_N = 101;

    /**
     * The feature id for the '<em><b>Module Procedure NLT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MODULE_PROCEDURE_NLT = 102;

    /**
     * The feature id for the '<em><b>Module Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__MODULE_STMT = 103;

    /**
     * The feature id for the '<em><b>N</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__N = 104;

    /**
     * The feature id for the '<em><b>N1</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__N1 = 105;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NAMED_E = 106;

    /**
     * The feature id for the '<em><b>Namelist Group N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NAMELIST_GROUP_N = 107;

    /**
     * The feature id for the '<em><b>Namelist Group Obj</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NAMELIST_GROUP_OBJ = 108;

    /**
     * The feature id for the '<em><b>Namelist Group Obj LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_LT = 109;

    /**
     * The feature id for the '<em><b>Namelist Group Obj N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NAMELIST_GROUP_OBJ_N = 110;

    /**
     * The feature id for the '<em><b>Namelist Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NAMELIST_STMT = 111;

    /**
     * The feature id for the '<em><b>Nullify Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__NULLIFY_STMT = 112;

    /**
     * The feature id for the '<em><b>O</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__O = 113;

    /**
     * The feature id for the '<em><b>Object</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OBJECT = 114;

    /**
     * The feature id for the '<em><b>Op</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OP = 115;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OP_E = 116;

    /**
     * The feature id for the '<em><b>Open Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OPEN_STMT = 117;

    /**
     * The feature id for the '<em><b>Output Item</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OUTPUT_ITEM = 118;

    /**
     * The feature id for the '<em><b>Output Item LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__OUTPUT_ITEM_LT = 119;

    /**
     * The feature id for the '<em><b>Parens E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PARENS_E = 120;

    /**
     * The feature id for the '<em><b>Parens R</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PARENS_R = 121;

    /**
     * The feature id for the '<em><b>Pointer AStmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__POINTER_ASTMT = 122;

    /**
     * The feature id for the '<em><b>Pointer Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__POINTER_STMT = 123;

    /**
     * The feature id for the '<em><b>Prefix</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PREFIX = 124;

    /**
     * The feature id for the '<em><b>Private Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PRIVATE_STMT = 125;

    /**
     * The feature id for the '<em><b>Procedure Designator</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROCEDURE_DESIGNATOR = 126;

    /**
     * The feature id for the '<em><b>Procedure Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROCEDURE_STMT = 127;

    /**
     * The feature id for the '<em><b>Program N</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROGRAM_N = 128;

    /**
     * The feature id for the '<em><b>Program Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PROGRAM_STMT = 129;

    /**
     * The feature id for the '<em><b>Public Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__PUBLIC_STMT = 130;

    /**
     * The feature id for the '<em><b>RLT</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RLT = 131;

    /**
     * The feature id for the '<em><b>Read Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__READ_STMT = 132;

    /**
     * The feature id for the '<em><b>Rename</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RENAME = 133;

    /**
     * The feature id for the '<em><b>Rename LT</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RENAME_LT = 134;

    /**
     * The feature id for the '<em><b>Result Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RESULT_SPEC = 135;

    /**
     * The feature id for the '<em><b>Return Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__RETURN_STMT = 136;

    /**
     * The feature id for the '<em><b>S</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__S = 137;

    /**
     * The feature id for the '<em><b>Save Stmt</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SAVE_STMT = 138;

    /**
     * The feature id for the '<em><b>Section Subscript</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SECTION_SUBSCRIPT = 139;

    /**
     * The feature id for the '<em><b>Section Subscript LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SECTION_SUBSCRIPT_LT = 140;

    /**
     * The feature id for the '<em><b>Select Case Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SELECT_CASE_STMT = 141;

    /**
     * The feature id for the '<em><b>Shape Spec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SHAPE_SPEC = 142;

    /**
     * The feature id for the '<em><b>Shape Spec LT</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SHAPE_SPEC_LT = 143;

    /**
     * The feature id for the '<em><b>Star E</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__STAR_E = 144;

    /**
     * The feature id for the '<em><b>Stop Code</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__STOP_CODE = 145;

    /**
     * The feature id for the '<em><b>Stop Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__STOP_STMT = 146;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__STRING_E = 147;

    /**
     * The feature id for the '<em><b>Subroutine N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SUBROUTINE_N = 148;

    /**
     * The feature id for the '<em><b>Subroutine Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__SUBROUTINE_STMT = 149;

    /**
     * The feature id for the '<em><b>TDecl Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TDECL_STMT = 150;

    /**
     * The feature id for the '<em><b>TN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TN = 151;

    /**
     * The feature id for the '<em><b>TStmt</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TSTMT = 152;

    /**
     * The feature id for the '<em><b>Test E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__TEST_E = 153;

    /**
     * The feature id for the '<em><b>Upper Bound</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__UPPER_BOUND = 154;

    /**
     * The feature id for the '<em><b>Use N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__USE_N = 155;

    /**
     * The feature id for the '<em><b>Use Stmt</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__USE_STMT = 156;

    /**
     * The feature id for the '<em><b>V</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__V = 157;

    /**
     * The feature id for the '<em><b>VN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__VN = 158;

    /**
     * The feature id for the '<em><b>Where Construct Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__WHERE_CONSTRUCT_STMT = 159;

    /**
     * The feature id for the '<em><b>Where Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__WHERE_STMT = 160;

    /**
     * The feature id for the '<em><b>Write Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT__WRITE_STMT = 161;

    /**
     * The number of structural features of the '<em>Document Root</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_FEATURE_COUNT = 162;

    /**
     * The number of operations of the '<em>Document Root</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DOCUMENT_ROOT_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.DoStmtTypeImpl <em>Do
     * Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.DoStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getDoStmtType()
     * @generated
     */
    int DO_STMT_TYPE = 33;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE__N = 2;

    /**
     * The feature id for the '<em><b>Lower Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE__LOWER_BOUND = 3;

    /**
     * The feature id for the '<em><b>Upper Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE__UPPER_BOUND = 4;

    /**
     * The feature id for the '<em><b>Do V</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE__DO_V = 5;

    /**
     * The feature id for the '<em><b>Test E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE__TEST_E = 6;

    /**
     * The number of structural features of the '<em>Do Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE_FEATURE_COUNT = 7;

    /**
     * The number of operations of the '<em>Do Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.DoVTypeImpl <em>Do
     * VType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.DoVTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getDoVType()
     * @generated
     */
    int DO_VTYPE = 34;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_VTYPE__NAMED_E = 0;

    /**
     * The number of structural features of the '<em>Do VType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_VTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Do VType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DO_VTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.DummyArgLTTypeImpl
     * <em>Dummy Arg LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.DummyArgLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getDummyArgLTType()
     * @generated
     */
    int DUMMY_ARG_LT_TYPE = 35;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DUMMY_ARG_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DUMMY_ARG_LT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DUMMY_ARG_LT_TYPE__ARG_N = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DUMMY_ARG_LT_TYPE__CNT = 3;

    /**
     * The number of structural features of the '<em>Dummy Arg LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DUMMY_ARG_LT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Dummy Arg LT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int DUMMY_ARG_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.E1TypeImpl <em>E1
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.E1TypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getE1Type()
     * @generated
     */
    int E1_TYPE = 36;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E1_TYPE__NAMED_E = 0;

    /**
     * The number of structural features of the '<em>E1 Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E1_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>E1 Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E1_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.E2TypeImpl <em>E2
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.E2TypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getE2Type()
     * @generated
     */
    int E2_TYPE = 37;

    /**
     * The feature id for the '<em><b>Array Constructor E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE__ARRAY_CONSTRUCTOR_E = 0;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE__LITERAL_E = 1;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE__NAMED_E = 2;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE__OP_E = 3;

    /**
     * The feature id for the '<em><b>Parens E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE__PARENS_E = 4;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE__STRING_E = 5;

    /**
     * The number of structural features of the '<em>E2 Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>E2 Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int E2_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ElementLTTypeImpl
     * <em>Element LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ElementLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getElementLTType()
     * @generated
     */
    int ELEMENT_LT_TYPE = 38;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_LT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_LT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Element</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_LT_TYPE__ELEMENT = 3;

    /**
     * The number of structural features of the '<em>Element LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_LT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Element LT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl
     * <em>Element Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ElementTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getElementType()
     * @generated
     */
    int ELEMENT_TYPE = 39;

    /**
     * The feature id for the '<em><b>Array Constructor E</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE__ARRAY_CONSTRUCTOR_E = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE__NAMED_E = 2;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE__OP_E = 3;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE__LITERAL_E = 4;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE__STRING_E = 5;

    /**
     * The number of structural features of the '<em>Element Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>Element Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELEMENT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ElseIfStmtTypeImpl
     * <em>Else If Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ElseIfStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getElseIfStmtType()
     * @generated
     */
    int ELSE_IF_STMT_TYPE = 40;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELSE_IF_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Condition E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELSE_IF_STMT_TYPE__CONDITION_E = 1;

    /**
     * The number of structural features of the '<em>Else If Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELSE_IF_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Else If Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ELSE_IF_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.EndDoStmtTypeImpl
     * <em>End Do Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndDoStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndDoStmtType()
     * @generated
     */
    int END_DO_STMT_TYPE = 41;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_DO_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_DO_STMT_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>End Do Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_DO_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Do Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_DO_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ENDeclLTTypeImpl <em>EN
     * Decl LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ENDeclLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getENDeclLTType()
     * @generated
     */
    int EN_DECL_LT_TYPE = 42;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_LT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_LT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>EN Decl</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_LT_TYPE__EN_DECL = 3;

    /**
     * The number of structural features of the '<em>EN Decl LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_LT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>EN Decl LT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ENDeclTypeImpl <em>EN
     * Decl Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ENDeclTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getENDeclType()
     * @generated
     */
    int EN_DECL_TYPE = 43;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Array Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_TYPE__ARRAY_SPEC = 2;

    /**
     * The feature id for the '<em><b>ENN</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_TYPE__ENN = 3;

    /**
     * The feature id for the '<em><b>Init E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_TYPE__INIT_E = 4;

    /**
     * The number of structural features of the '<em>EN Decl Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>EN Decl Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_DECL_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.EndForallStmtTypeImpl
     * <em>End Forall Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndForallStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndForallStmtType()
     * @generated
     */
    int END_FORALL_STMT_TYPE = 44;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FORALL_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FORALL_STMT_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>End Forall Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FORALL_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Forall Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FORALL_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.EndFunctionStmtTypeImpl
     * <em>End Function Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndFunctionStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndFunctionStmtType()
     * @generated
     */
    int END_FUNCTION_STMT_TYPE = 45;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FUNCTION_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Function N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FUNCTION_STMT_TYPE__FUNCTION_N = 1;

    /**
     * The number of structural features of the '<em>End Function Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FUNCTION_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Function Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_FUNCTION_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.EndInterfaceStmtTypeImpl <em>End Interface Stmt
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndInterfaceStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndInterfaceStmtType()
     * @generated
     */
    int END_INTERFACE_STMT_TYPE = 46;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_INTERFACE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_INTERFACE_STMT_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>End Interface Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_INTERFACE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Interface Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_INTERFACE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.EndModuleStmtTypeImpl
     * <em>End Module Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndModuleStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndModuleStmtType()
     * @generated
     */
    int END_MODULE_STMT_TYPE = 47;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_MODULE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Module N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_MODULE_STMT_TYPE__MODULE_N = 1;

    /**
     * The number of structural features of the '<em>End Module Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_MODULE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Module Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_MODULE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.EndProgramStmtTypeImpl
     * <em>End Program Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndProgramStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndProgramStmtType()
     * @generated
     */
    int END_PROGRAM_STMT_TYPE = 48;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_PROGRAM_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Program N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_PROGRAM_STMT_TYPE__PROGRAM_N = 1;

    /**
     * The number of structural features of the '<em>End Program Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_PROGRAM_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Program Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_PROGRAM_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.EndSelectCaseStmtTypeImpl <em>End Select Case Stmt
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndSelectCaseStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndSelectCaseStmtType()
     * @generated
     */
    int END_SELECT_CASE_STMT_TYPE = 49;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SELECT_CASE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SELECT_CASE_STMT_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>End Select Case Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SELECT_CASE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Select Case Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SELECT_CASE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.EndSubroutineStmtTypeImpl <em>End Subroutine Stmt
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndSubroutineStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndSubroutineStmtType()
     * @generated
     */
    int END_SUBROUTINE_STMT_TYPE = 50;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SUBROUTINE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Subroutine N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SUBROUTINE_STMT_TYPE__SUBROUTINE_N = 1;

    /**
     * The number of structural features of the '<em>End Subroutine Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SUBROUTINE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End Subroutine Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_SUBROUTINE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.EndTStmtTypeImpl
     * <em>End TStmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.EndTStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getEndTStmtType()
     * @generated
     */
    int END_TSTMT_TYPE = 51;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_TSTMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>TN</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_TSTMT_TYPE__TN = 1;

    /**
     * The number of structural features of the '<em>End TStmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_TSTMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>End TStmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int END_TSTMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ENLTTypeImpl <em>ENLT
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ENLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getENLTType()
     * @generated
     */
    int ENLT_TYPE = 52;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENLT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENLT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENLT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>EN</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENLT_TYPE__EN = 3;

    /**
     * The number of structural features of the '<em>ENLT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENLT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>ENLT Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENLT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ENNTypeImpl <em>ENN
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ENNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getENNType()
     * @generated
     */
    int ENN_TYPE = 53;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENN_TYPE__N = 0;

    /**
     * The number of structural features of the '<em>ENN Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENN_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>ENN Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ENN_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ENTypeImpl <em>EN
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ENTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getENType()
     * @generated
     */
    int EN_TYPE = 54;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_TYPE__N = 0;

    /**
     * The number of structural features of the '<em>EN Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>EN Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int EN_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ErrorTypeImpl <em>Error
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ErrorTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getErrorType()
     * @generated
     */
    int ERROR_TYPE = 55;

    /**
     * The feature id for the '<em><b>Msg</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ERROR_TYPE__MSG = 0;

    /**
     * The number of structural features of the '<em>Error Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ERROR_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Error Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ERROR_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl <em>File
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.FileTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getFileType()
     * @generated
     */
    int FILE_TYPE = 56;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>C</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__C = 1;

    /**
     * The feature id for the '<em><b>AStmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__ASTMT = 2;

    /**
     * The feature id for the '<em><b>Allocate Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__ALLOCATE_STMT = 3;

    /**
     * The feature id for the '<em><b>Call Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__CALL_STMT = 4;

    /**
     * The feature id for the '<em><b>Deallocate Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__DEALLOCATE_STMT = 5;

    /**
     * The feature id for the '<em><b>Exit Stmt</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__EXIT_STMT = 6;

    /**
     * The feature id for the '<em><b>Pointer AStmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__POINTER_ASTMT = 7;

    /**
     * The feature id for the '<em><b>Return Stmt</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__RETURN_STMT = 8;

    /**
     * The feature id for the '<em><b>Where Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__WHERE_STMT = 9;

    /**
     * The feature id for the '<em><b>TDecl Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__TDECL_STMT = 10;

    /**
     * The feature id for the '<em><b>TStmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__TSTMT = 11;

    /**
     * The feature id for the '<em><b>Case Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__CASE_STMT = 12;

    /**
     * The feature id for the '<em><b>Close Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__CLOSE_STMT = 13;

    /**
     * The feature id for the '<em><b>Component Decl Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__COMPONENT_DECL_STMT = 14;

    /**
     * The feature id for the '<em><b>Contains Stmt</b></em>' attribute list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__CONTAINS_STMT = 15;

    /**
     * The feature id for the '<em><b>Cpp</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__CPP = 16;

    /**
     * The feature id for the '<em><b>Do Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__DO_STMT = 17;

    /**
     * The feature id for the '<em><b>Else If Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__ELSE_IF_STMT = 18;

    /**
     * The feature id for the '<em><b>Else Stmt</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__ELSE_STMT = 19;

    /**
     * The feature id for the '<em><b>Else Where Stmt</b></em>' attribute list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__ELSE_WHERE_STMT = 20;

    /**
     * The feature id for the '<em><b>End TStmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_TSTMT = 21;

    /**
     * The feature id for the '<em><b>End Do Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_DO_STMT = 22;

    /**
     * The feature id for the '<em><b>End Forall Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_FORALL_STMT = 23;

    /**
     * The feature id for the '<em><b>End Function Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_FUNCTION_STMT = 24;

    /**
     * The feature id for the '<em><b>End If Stmt</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_IF_STMT = 25;

    /**
     * The feature id for the '<em><b>End Interface Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_INTERFACE_STMT = 26;

    /**
     * The feature id for the '<em><b>End Select Case Stmt</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_SELECT_CASE_STMT = 27;

    /**
     * The feature id for the '<em><b>End Subroutine Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_SUBROUTINE_STMT = 28;

    /**
     * The feature id for the '<em><b>End Where Stmt</b></em>' attribute list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_WHERE_STMT = 29;

    /**
     * The feature id for the '<em><b>Forall Construct Stmt</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__FORALL_CONSTRUCT_STMT = 30;

    /**
     * The feature id for the '<em><b>Forall Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__FORALL_STMT = 31;

    /**
     * The feature id for the '<em><b>Function Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__FUNCTION_STMT = 32;

    /**
     * The feature id for the '<em><b>If Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__IF_STMT = 33;

    /**
     * The feature id for the '<em><b>If Then Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__IF_THEN_STMT = 34;

    /**
     * The feature id for the '<em><b>Implicit None Stmt</b></em>' attribute list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__IMPLICIT_NONE_STMT = 35;

    /**
     * The feature id for the '<em><b>Inquire Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__INQUIRE_STMT = 36;

    /**
     * The feature id for the '<em><b>Interface Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__INTERFACE_STMT = 37;

    /**
     * The feature id for the '<em><b>Module Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__MODULE_STMT = 38;

    /**
     * The feature id for the '<em><b>Namelist Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__NAMELIST_STMT = 39;

    /**
     * The feature id for the '<em><b>Nullify Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__NULLIFY_STMT = 40;

    /**
     * The feature id for the '<em><b>Open Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__OPEN_STMT = 41;

    /**
     * The feature id for the '<em><b>Pointer Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__POINTER_STMT = 42;

    /**
     * The feature id for the '<em><b>Private Stmt</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__PRIVATE_STMT = 43;

    /**
     * The feature id for the '<em><b>Procedure Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__PROCEDURE_STMT = 44;

    /**
     * The feature id for the '<em><b>Program Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__PROGRAM_STMT = 45;

    /**
     * The feature id for the '<em><b>Public Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__PUBLIC_STMT = 46;

    /**
     * The feature id for the '<em><b>Read Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__READ_STMT = 47;

    /**
     * The feature id for the '<em><b>Save Stmt</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__SAVE_STMT = 48;

    /**
     * The feature id for the '<em><b>Select Case Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__SELECT_CASE_STMT = 49;

    /**
     * The feature id for the '<em><b>Stop Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__STOP_STMT = 50;

    /**
     * The feature id for the '<em><b>Subroutine Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__SUBROUTINE_STMT = 51;

    /**
     * The feature id for the '<em><b>Use Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__USE_STMT = 52;

    /**
     * The feature id for the '<em><b>Where Construct Stmt</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__WHERE_CONSTRUCT_STMT = 53;

    /**
     * The feature id for the '<em><b>Write Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__WRITE_STMT = 54;

    /**
     * The feature id for the '<em><b>End Module Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_MODULE_STMT = 55;

    /**
     * The feature id for the '<em><b>End Program Stmt</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__END_PROGRAM_STMT = 56;

    /**
     * The feature id for the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE__NAME = 57;

    /**
     * The number of structural features of the '<em>File Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE_FEATURE_COUNT = 58;

    /**
     * The number of operations of the '<em>File Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FILE_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.ForallConstructStmtTypeImpl <em>Forall Construct
     * Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ForallConstructStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getForallConstructStmtType()
     * @generated
     */
    int FORALL_CONSTRUCT_STMT_TYPE = 57;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_CONSTRUCT_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_CONSTRUCT_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_CONSTRUCT_STMT_TYPE__N = 2;

    /**
     * The feature id for the '<em><b>Forall Triplet Spec LT</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_CONSTRUCT_STMT_TYPE__FORALL_TRIPLET_SPEC_LT = 3;

    /**
     * The number of structural features of the '<em>Forall Construct Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_CONSTRUCT_STMT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Forall Construct Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_CONSTRUCT_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ForallStmtTypeImpl
     * <em>Forall Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ForallStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getForallStmtType()
     * @generated
     */
    int FORALL_STMT_TYPE = 58;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Action Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE__ACTION_STMT = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Forall Triplet Spec LT</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE__FORALL_TRIPLET_SPEC_LT = 4;

    /**
     * The feature id for the '<em><b>Mask E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE__MASK_E = 5;

    /**
     * The number of structural features of the '<em>Forall Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>Forall Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.ForallTripletSpecLTTypeImpl <em>Forall Triplet
     * Spec LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ForallTripletSpecLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getForallTripletSpecLTType()
     * @generated
     */
    int FORALL_TRIPLET_SPEC_LT_TYPE = 59;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Forall Triplet Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_LT_TYPE__FORALL_TRIPLET_SPEC = 1;

    /**
     * The number of structural features of the '<em>Forall Triplet Spec LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_LT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Forall Triplet Spec LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.ForallTripletSpecTypeImpl <em>Forall Triplet Spec
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ForallTripletSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getForallTripletSpecType()
     * @generated
     */
    int FORALL_TRIPLET_SPEC_TYPE = 60;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Lower Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_TYPE__LOWER_BOUND = 2;

    /**
     * The feature id for the '<em><b>Upper Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_TYPE__UPPER_BOUND = 3;

    /**
     * The feature id for the '<em><b>V</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_TYPE__V = 4;

    /**
     * The number of structural features of the '<em>Forall Triplet Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Forall Triplet Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FORALL_TRIPLET_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.FunctionNTypeImpl
     * <em>Function NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.FunctionNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getFunctionNType()
     * @generated
     */
    int FUNCTION_NTYPE = 61;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_NTYPE__N = 0;

    /**
     * The number of structural features of the '<em>Function NType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_NTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Function NType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl
     * <em>Function Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.FunctionStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getFunctionStmtType()
     * @generated
     */
    int FUNCTION_STMT_TYPE = 62;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Derived TSpec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__DERIVED_TSPEC = 2;

    /**
     * The feature id for the '<em><b>Dummy Arg LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__DUMMY_ARG_LT = 3;

    /**
     * The feature id for the '<em><b>Function N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__FUNCTION_N = 4;

    /**
     * The feature id for the '<em><b>Intrinsic TSpec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__INTRINSIC_TSPEC = 5;

    /**
     * The feature id for the '<em><b>Prefix</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__PREFIX = 6;

    /**
     * The feature id for the '<em><b>Result Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE__RESULT_SPEC = 7;

    /**
     * The number of structural features of the '<em>Function Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE_FEATURE_COUNT = 8;

    /**
     * The number of operations of the '<em>Function Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int FUNCTION_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.IfStmtTypeImpl <em>If
     * Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IfStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIfStmtType()
     * @generated
     */
    int IF_STMT_TYPE = 63;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Action Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_STMT_TYPE__ACTION_STMT = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_STMT_TYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Condition E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_STMT_TYPE__CONDITION_E = 4;

    /**
     * The number of structural features of the '<em>If Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_STMT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>If Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.IfThenStmtTypeImpl
     * <em>If Then Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IfThenStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIfThenStmtType()
     * @generated
     */
    int IF_THEN_STMT_TYPE = 64;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_THEN_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Condition E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_THEN_STMT_TYPE__CONDITION_E = 1;

    /**
     * The number of structural features of the '<em>If Then Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_THEN_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>If Then Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IF_THEN_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.InitETypeImpl <em>Init
     * EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.InitETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getInitEType()
     * @generated
     */
    int INIT_ETYPE = 65;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INIT_ETYPE__LITERAL_E = 0;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INIT_ETYPE__NAMED_E = 1;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INIT_ETYPE__OP_E = 2;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INIT_ETYPE__STRING_E = 3;

    /**
     * The number of structural features of the '<em>Init EType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INIT_ETYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Init EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INIT_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.InquireStmtTypeImpl
     * <em>Inquire Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.InquireStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getInquireStmtType()
     * @generated
     */
    int INQUIRE_STMT_TYPE = 66;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Inquiry Spec Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRE_STMT_TYPE__INQUIRY_SPEC_SPEC = 1;

    /**
     * The number of structural features of the '<em>Inquire Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Inquire Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.InquirySpecSpecTypeImpl
     * <em>Inquiry Spec Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.InquirySpecSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getInquirySpecSpecType()
     * @generated
     */
    int INQUIRY_SPEC_SPEC_TYPE = 67;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Inquiry Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_SPEC_TYPE__INQUIRY_SPEC = 1;

    /**
     * The number of structural features of the '<em>Inquiry Spec Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_SPEC_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Inquiry Spec Spec Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.InquirySpecTypeImpl
     * <em>Inquiry Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.InquirySpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getInquirySpecType()
     * @generated
     */
    int INQUIRY_SPEC_TYPE = 68;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_TYPE__ARG_N = 2;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_TYPE__NAMED_E = 3;

    /**
     * The number of structural features of the '<em>Inquiry Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Inquiry Spec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INQUIRY_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.InterfaceStmtTypeImpl
     * <em>Interface Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.InterfaceStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getInterfaceStmtType()
     * @generated
     */
    int INTERFACE_STMT_TYPE = 69;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTERFACE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTERFACE_STMT_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>Interface Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTERFACE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Interface Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTERFACE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.IntrinsicTSpecTypeImpl
     * <em>Intrinsic TSpec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IntrinsicTSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIntrinsicTSpecType()
     * @generated
     */
    int INTRINSIC_TSPEC_TYPE = 70;

    /**
     * The feature id for the '<em><b>TN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTRINSIC_TSPEC_TYPE__TN = 0;

    /**
     * The feature id for the '<em><b>KSelector</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTRINSIC_TSPEC_TYPE__KSELECTOR = 1;

    /**
     * The feature id for the '<em><b>Char Selector</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTRINSIC_TSPEC_TYPE__CHAR_SELECTOR = 2;

    /**
     * The number of structural features of the '<em>Intrinsic TSpec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTRINSIC_TSPEC_TYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Intrinsic TSpec Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int INTRINSIC_TSPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.IoControlSpecTypeImpl
     * <em>Io Control Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IoControlSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIoControlSpecType()
     * @generated
     */
    int IO_CONTROL_SPEC_TYPE = 71;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Io Control</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_SPEC_TYPE__IO_CONTROL = 1;

    /**
     * The number of structural features of the '<em>Io Control Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_SPEC_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Io Control Spec Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.IoControlTypeImpl
     * <em>Io Control Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IoControlTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIoControlType()
     * @generated
     */
    int IO_CONTROL_TYPE = 72;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE__ARG_N = 2;

    /**
     * The feature id for the '<em><b>Label</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE__LABEL = 3;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE__LITERAL_E = 4;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE__NAMED_E = 5;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE__STRING_E = 6;

    /**
     * The number of structural features of the '<em>Io Control Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE_FEATURE_COUNT = 7;

    /**
     * The number of operations of the '<em>Io Control Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int IO_CONTROL_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.IteratorDefinitionLTTypeImpl <em>Iterator
     * Definition LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IteratorDefinitionLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIteratorDefinitionLTType()
     * @generated
     */
    int ITERATOR_DEFINITION_LT_TYPE = 73;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_DEFINITION_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Iterator Element</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_DEFINITION_LT_TYPE__ITERATOR_ELEMENT = 1;

    /**
     * The number of structural features of the '<em>Iterator Definition LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_DEFINITION_LT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Iterator Definition LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_DEFINITION_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.IteratorElementTypeImpl
     * <em>Iterator Element Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IteratorElementTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIteratorElementType()
     * @generated
     */
    int ITERATOR_ELEMENT_TYPE = 74;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_ELEMENT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_ELEMENT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>VN</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_ELEMENT_TYPE__VN = 2;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_ELEMENT_TYPE__LITERAL_E = 3;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_ELEMENT_TYPE__NAMED_E = 4;

    /**
     * The number of structural features of the '<em>Iterator Element Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_ELEMENT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Iterator Element Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_ELEMENT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.IteratorTypeImpl
     * <em>Iterator Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.IteratorTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getIteratorType()
     * @generated
     */
    int ITERATOR_TYPE = 75;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Iterator Definition LT</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_TYPE__ITERATOR_DEFINITION_LT = 1;

    /**
     * The number of structural features of the '<em>Iterator Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Iterator Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int ITERATOR_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.KSelectorTypeImpl
     * <em>KSelector Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.KSelectorTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getKSelectorType()
     * @generated
     */
    int KSELECTOR_TYPE = 76;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSELECTOR_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>KSpec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSELECTOR_TYPE__KSPEC = 1;

    /**
     * The number of structural features of the '<em>KSelector Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSELECTOR_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>KSelector Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSELECTOR_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.KSpecTypeImpl <em>KSpec
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.KSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getKSpecType()
     * @generated
     */
    int KSPEC_TYPE = 77;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE__N = 2;

    /**
     * The feature id for the '<em><b>L</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE__L = 3;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE__LITERAL_E = 4;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE__NAMED_E = 5;

    /**
     * The number of structural features of the '<em>KSpec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>KSpec Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int KSPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.LabelTypeImpl <em>Label
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.LabelTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getLabelType()
     * @generated
     */
    int LABEL_TYPE = 78;

    /**
     * The feature id for the '<em><b>Error</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LABEL_TYPE__ERROR = 0;

    /**
     * The number of structural features of the '<em>Label Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LABEL_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Label Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LABEL_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.LiteralETypeImpl
     * <em>Literal EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.LiteralETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getLiteralEType()
     * @generated
     */
    int LITERAL_ETYPE = 79;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LITERAL_ETYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LITERAL_ETYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>KSpec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LITERAL_ETYPE__KSPEC = 2;

    /**
     * The feature id for the '<em><b>L</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LITERAL_ETYPE__L = 3;

    /**
     * The number of structural features of the '<em>Literal EType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LITERAL_ETYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Literal EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LITERAL_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.LowerBoundTypeImpl
     * <em>Lower Bound Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.LowerBoundTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getLowerBoundType()
     * @generated
     */
    int LOWER_BOUND_TYPE = 80;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LOWER_BOUND_TYPE__LITERAL_E = 0;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LOWER_BOUND_TYPE__NAMED_E = 1;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LOWER_BOUND_TYPE__OP_E = 2;

    /**
     * The number of structural features of the '<em>Lower Bound Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LOWER_BOUND_TYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Lower Bound Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int LOWER_BOUND_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.MaskETypeImpl <em>Mask
     * EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.MaskETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getMaskEType()
     * @generated
     */
    int MASK_ETYPE = 81;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MASK_ETYPE__OP_E = 0;

    /**
     * The number of structural features of the '<em>Mask EType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MASK_ETYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Mask EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MASK_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ModuleNTypeImpl
     * <em>Module NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ModuleNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getModuleNType()
     * @generated
     */
    int MODULE_NTYPE = 82;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_NTYPE__N = 0;

    /**
     * The number of structural features of the '<em>Module NType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_NTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Module NType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.ModuleProcedureNLTTypeImpl <em>Module Procedure
     * NLT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ModuleProcedureNLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getModuleProcedureNLTType()
     * @generated
     */
    int MODULE_PROCEDURE_NLT_TYPE = 83;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_PROCEDURE_NLT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_PROCEDURE_NLT_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>Module Procedure NLT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_PROCEDURE_NLT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Module Procedure NLT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_PROCEDURE_NLT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ModuleStmtTypeImpl
     * <em>Module Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ModuleStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getModuleStmtType()
     * @generated
     */
    int MODULE_STMT_TYPE = 84;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Module N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_STMT_TYPE__MODULE_N = 1;

    /**
     * The number of structural features of the '<em>Module Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Module Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int MODULE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.NamedETypeImpl
     * <em>Named EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NamedETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNamedEType()
     * @generated
     */
    int NAMED_ETYPE = 85;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMED_ETYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMED_ETYPE__N = 1;

    /**
     * The feature id for the '<em><b>RLT</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMED_ETYPE__RLT = 2;

    /**
     * The number of structural features of the '<em>Named EType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMED_ETYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Named EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMED_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.NamelistGroupNTypeImpl
     * <em>Namelist Group NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NamelistGroupNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNamelistGroupNType()
     * @generated
     */
    int NAMELIST_GROUP_NTYPE = 86;

    /**
     * The feature id for the '<em><b>N</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_NTYPE__N = 0;

    /**
     * The number of structural features of the '<em>Namelist Group NType</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_NTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Namelist Group NType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjLTTypeImpl <em>Namelist Group Obj
     * LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNamelistGroupObjLTType()
     * @generated
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE = 87;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>C</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE__C = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Namelist Group Obj</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE__NAMELIST_GROUP_OBJ = 4;

    /**
     * The number of structural features of the '<em>Namelist Group Obj LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Namelist Group Obj LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjNTypeImpl <em>Namelist Group Obj
     * NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNamelistGroupObjNType()
     * @generated
     */
    int NAMELIST_GROUP_OBJ_NTYPE = 88;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_NTYPE__N = 0;

    /**
     * The number of structural features of the '<em>Namelist Group Obj NType</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_NTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Namelist Group Obj NType</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjTypeImpl <em>Namelist Group Obj
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NamelistGroupObjTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNamelistGroupObjType()
     * @generated
     */
    int NAMELIST_GROUP_OBJ_TYPE = 89;

    /**
     * The feature id for the '<em><b>Namelist Group Obj N</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_TYPE__NAMELIST_GROUP_OBJ_N = 0;

    /**
     * The number of structural features of the '<em>Namelist Group Obj Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Namelist Group Obj Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_GROUP_OBJ_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.NamelistStmtTypeImpl
     * <em>Namelist Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NamelistStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNamelistStmtType()
     * @generated
     */
    int NAMELIST_STMT_TYPE = 90;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_STMT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Namelist Group N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_STMT_TYPE__NAMELIST_GROUP_N = 3;

    /**
     * The feature id for the '<em><b>Namelist Group Obj LT</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_STMT_TYPE__NAMELIST_GROUP_OBJ_LT = 4;

    /**
     * The number of structural features of the '<em>Namelist Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_STMT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Namelist Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NAMELIST_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.NTypeImpl
     * <em>NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNType()
     * @generated
     */
    int NTYPE = 91;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NTYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NTYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NTYPE__N = 2;

    /**
     * The feature id for the '<em><b>N1</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NTYPE__N1 = 3;

    /**
     * The feature id for the '<em><b>Op</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NTYPE__OP = 4;

    /**
     * The number of structural features of the '<em>NType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NTYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>NType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.NullifyStmtTypeImpl
     * <em>Nullify Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.NullifyStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getNullifyStmtType()
     * @generated
     */
    int NULLIFY_STMT_TYPE = 92;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NULLIFY_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Arg Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NULLIFY_STMT_TYPE__ARG_SPEC = 1;

    /**
     * The number of structural features of the '<em>Nullify Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NULLIFY_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Nullify Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int NULLIFY_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ObjectTypeImpl
     * <em>Object Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ObjectTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getObjectType()
     * @generated
     */
    int OBJECT_TYPE = 93;

    /**
     * The feature id for the '<em><b>File</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OBJECT_TYPE__FILE = 0;

    /**
     * The feature id for the '<em><b>Openacc</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OBJECT_TYPE__OPENACC = 1;

    /**
     * The feature id for the '<em><b>Openmp</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OBJECT_TYPE__OPENMP = 2;

    /**
     * The feature id for the '<em><b>Source Form</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OBJECT_TYPE__SOURCE_FORM = 3;

    /**
     * The feature id for the '<em><b>Source Width</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OBJECT_TYPE__SOURCE_WIDTH = 4;

    /**
     * The number of structural features of the '<em>Object Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OBJECT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Object Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OBJECT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.OpenStmtTypeImpl
     * <em>Open Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.OpenStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getOpenStmtType()
     * @generated
     */
    int OPEN_STMT_TYPE = 94;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OPEN_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OPEN_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OPEN_STMT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Connect Spec Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OPEN_STMT_TYPE__CONNECT_SPEC_SPEC = 3;

    /**
     * The number of structural features of the '<em>Open Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OPEN_STMT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Open Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OPEN_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl <em>Op
     * EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.OpETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getOpEType()
     * @generated
     */
    int OP_ETYPE = 95;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__CNT = 1;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__LITERAL_E = 2;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__NAMED_E = 3;

    /**
     * The feature id for the '<em><b>Op</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__OP = 4;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__OP_E = 5;

    /**
     * The feature id for the '<em><b>Parens E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__PARENS_E = 6;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE__STRING_E = 7;

    /**
     * The number of structural features of the '<em>Op EType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE_FEATURE_COUNT = 8;

    /**
     * The number of operations of the '<em>Op EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.OpTypeImpl <em>Op
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.OpTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getOpType()
     * @generated
     */
    int OP_TYPE = 96;

    /**
     * The feature id for the '<em><b>O</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_TYPE__O = 0;

    /**
     * The number of structural features of the '<em>Op Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Op Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OP_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.OutputItemLTTypeImpl
     * <em>Output Item LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.OutputItemLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getOutputItemLTType()
     * @generated
     */
    int OUTPUT_ITEM_LT_TYPE = 97;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Output Item</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_LT_TYPE__OUTPUT_ITEM = 1;

    /**
     * The number of structural features of the '<em>Output Item LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_LT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Output Item LT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.OutputItemTypeImpl
     * <em>Output Item Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.OutputItemTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getOutputItemType()
     * @generated
     */
    int OUTPUT_ITEM_TYPE = 98;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_TYPE__LITERAL_E = 0;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_TYPE__NAMED_E = 1;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_TYPE__OP_E = 2;

    /**
     * The feature id for the '<em><b>String E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_TYPE__STRING_E = 3;

    /**
     * The number of structural features of the '<em>Output Item Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Output Item Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int OUTPUT_ITEM_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ParensETypeImpl
     * <em>Parens EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ParensETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getParensEType()
     * @generated
     */
    int PARENS_ETYPE = 99;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_ETYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_ETYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_ETYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_ETYPE__OP_E = 3;

    /**
     * The feature id for the '<em><b>Iterator</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_ETYPE__ITERATOR = 4;

    /**
     * The number of structural features of the '<em>Parens EType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_ETYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Parens EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ParensRTypeImpl
     * <em>Parens RType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ParensRTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getParensRType()
     * @generated
     */
    int PARENS_RTYPE = 100;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_RTYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_RTYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Arg Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_RTYPE__ARG_SPEC = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_RTYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Element LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_RTYPE__ELEMENT_LT = 4;

    /**
     * The number of structural features of the '<em>Parens RType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_RTYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Parens RType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PARENS_RTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.PointerAStmtTypeImpl
     * <em>Pointer AStmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.PointerAStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getPointerAStmtType()
     * @generated
     */
    int POINTER_ASTMT_TYPE = 101;

    /**
     * The feature id for the '<em><b>E1</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_ASTMT_TYPE__E1 = 0;

    /**
     * The feature id for the '<em><b>A</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_ASTMT_TYPE__A = 1;

    /**
     * The feature id for the '<em><b>E2</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_ASTMT_TYPE__E2 = 2;

    /**
     * The number of structural features of the '<em>Pointer AStmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_ASTMT_TYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Pointer AStmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_ASTMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.PointerStmtTypeImpl
     * <em>Pointer Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.PointerStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getPointerStmtType()
     * @generated
     */
    int POINTER_STMT_TYPE = 102;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>EN Decl LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_STMT_TYPE__EN_DECL_LT = 1;

    /**
     * The number of structural features of the '<em>Pointer Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Pointer Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int POINTER_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.ProcedureDesignatorTypeImpl <em>Procedure
     * Designator Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ProcedureDesignatorTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getProcedureDesignatorType()
     * @generated
     */
    int PROCEDURE_DESIGNATOR_TYPE = 103;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROCEDURE_DESIGNATOR_TYPE__NAMED_E = 0;

    /**
     * The number of structural features of the '<em>Procedure Designator Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROCEDURE_DESIGNATOR_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Procedure Designator Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROCEDURE_DESIGNATOR_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ProcedureStmtTypeImpl
     * <em>Procedure Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ProcedureStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getProcedureStmtType()
     * @generated
     */
    int PROCEDURE_STMT_TYPE = 104;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROCEDURE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Module Procedure NLT</b></em>' containment reference list.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROCEDURE_STMT_TYPE__MODULE_PROCEDURE_NLT = 1;

    /**
     * The number of structural features of the '<em>Procedure Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROCEDURE_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Procedure Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROCEDURE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ProgramNTypeImpl
     * <em>Program NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ProgramNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getProgramNType()
     * @generated
     */
    int PROGRAM_NTYPE = 105;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROGRAM_NTYPE__N = 0;

    /**
     * The number of structural features of the '<em>Program NType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROGRAM_NTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Program NType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROGRAM_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ProgramStmtTypeImpl
     * <em>Program Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ProgramStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getProgramStmtType()
     * @generated
     */
    int PROGRAM_STMT_TYPE = 106;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROGRAM_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Program N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROGRAM_STMT_TYPE__PROGRAM_N = 1;

    /**
     * The number of structural features of the '<em>Program Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROGRAM_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Program Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PROGRAM_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.PublicStmtTypeImpl
     * <em>Public Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.PublicStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getPublicStmtType()
     * @generated
     */
    int PUBLIC_STMT_TYPE = 107;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PUBLIC_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>ENLT</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PUBLIC_STMT_TYPE__ENLT = 1;

    /**
     * The number of structural features of the '<em>Public Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PUBLIC_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Public Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int PUBLIC_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ReadStmtTypeImpl
     * <em>Read Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ReadStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getReadStmtType()
     * @generated
     */
    int READ_STMT_TYPE = 108;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int READ_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Io Control Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int READ_STMT_TYPE__IO_CONTROL_SPEC = 1;

    /**
     * The number of structural features of the '<em>Read Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int READ_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Read Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int READ_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.RenameLTTypeImpl
     * <em>Rename LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.RenameLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getRenameLTType()
     * @generated
     */
    int RENAME_LT_TYPE = 109;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_LT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_LT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Rename</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_LT_TYPE__RENAME = 3;

    /**
     * The number of structural features of the '<em>Rename LT Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_LT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Rename LT Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.RenameTypeImpl
     * <em>Rename Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.RenameTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getRenameType()
     * @generated
     */
    int RENAME_TYPE = 110;

    /**
     * The feature id for the '<em><b>Use N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_TYPE__USE_N = 0;

    /**
     * The number of structural features of the '<em>Rename Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_TYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Rename Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RENAME_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ResultSpecTypeImpl
     * <em>Result Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ResultSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getResultSpecType()
     * @generated
     */
    int RESULT_SPEC_TYPE = 111;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RESULT_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RESULT_SPEC_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>Result Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RESULT_SPEC_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Result Spec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RESULT_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.RLTTypeImpl <em>RLT
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.RLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getRLTType()
     * @generated
     */
    int RLT_TYPE = 112;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RLT_TYPE__GROUP = 0;

    /**
     * The feature id for the '<em><b>Array R</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RLT_TYPE__ARRAY_R = 1;

    /**
     * The feature id for the '<em><b>Component R</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RLT_TYPE__COMPONENT_R = 2;

    /**
     * The feature id for the '<em><b>Parens R</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RLT_TYPE__PARENS_R = 3;

    /**
     * The number of structural features of the '<em>RLT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RLT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>RLT Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int RLT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptLTTypeImpl <em>Section Subscript
     * LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getSectionSubscriptLTType()
     * @generated
     */
    int SECTION_SUBSCRIPT_LT_TYPE = 113;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Section Subscript</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_LT_TYPE__SECTION_SUBSCRIPT = 1;

    /**
     * The number of structural features of the '<em>Section Subscript LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_LT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Section Subscript LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptTypeImpl <em>Section Subscript
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.SectionSubscriptTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getSectionSubscriptType()
     * @generated
     */
    int SECTION_SUBSCRIPT_TYPE = 114;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Lower Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_TYPE__LOWER_BOUND = 2;

    /**
     * The feature id for the '<em><b>Upper Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_TYPE__UPPER_BOUND = 3;

    /**
     * The number of structural features of the '<em>Section Subscript Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Section Subscript Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SECTION_SUBSCRIPT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.SelectCaseStmtTypeImpl
     * <em>Select Case Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.SelectCaseStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getSelectCaseStmtType()
     * @generated
     */
    int SELECT_CASE_STMT_TYPE = 115;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SELECT_CASE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SELECT_CASE_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SELECT_CASE_STMT_TYPE__N = 2;

    /**
     * The feature id for the '<em><b>Case E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SELECT_CASE_STMT_TYPE__CASE_E = 3;

    /**
     * The number of structural features of the '<em>Select Case Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SELECT_CASE_STMT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Select Case Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SELECT_CASE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ShapeSpecLTTypeImpl
     * <em>Shape Spec LT Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ShapeSpecLTTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getShapeSpecLTType()
     * @generated
     */
    int SHAPE_SPEC_LT_TYPE = 116;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_LT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Shape Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_LT_TYPE__SHAPE_SPEC = 1;

    /**
     * The number of structural features of the '<em>Shape Spec LT Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_LT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Shape Spec LT Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_LT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.ShapeSpecTypeImpl
     * <em>Shape Spec Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.ShapeSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getShapeSpecType()
     * @generated
     */
    int SHAPE_SPEC_TYPE = 117;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Lower Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_TYPE__LOWER_BOUND = 2;

    /**
     * The feature id for the '<em><b>Upper Bound</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_TYPE__UPPER_BOUND = 3;

    /**
     * The number of structural features of the '<em>Shape Spec Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Shape Spec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SHAPE_SPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.StopStmtTypeImpl
     * <em>Stop Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.StopStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getStopStmtType()
     * @generated
     */
    int STOP_STMT_TYPE = 118;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STOP_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Stop Code</b></em>' attribute list. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STOP_STMT_TYPE__STOP_CODE = 1;

    /**
     * The number of structural features of the '<em>Stop Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STOP_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Stop Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STOP_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.StringETypeImpl
     * <em>String EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.StringETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getStringEType()
     * @generated
     */
    int STRING_ETYPE = 119;

    /**
     * The feature id for the '<em><b>S</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STRING_ETYPE__S = 0;

    /**
     * The number of structural features of the '<em>String EType</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STRING_ETYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>String EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int STRING_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineNTypeImpl
     * <em>Subroutine NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.SubroutineNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getSubroutineNType()
     * @generated
     */
    int SUBROUTINE_NTYPE = 120;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_NTYPE__N = 0;

    /**
     * The number of structural features of the '<em>Subroutine NType</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_NTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Subroutine NType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl
     * <em>Subroutine Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.SubroutineStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getSubroutineStmtType()
     * @generated
     */
    int SUBROUTINE_STMT_TYPE = 121;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Dummy Arg LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE__DUMMY_ARG_LT = 3;

    /**
     * The feature id for the '<em><b>Prefix</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE__PREFIX = 4;

    /**
     * The feature id for the '<em><b>Subroutine N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE__SUBROUTINE_N = 5;

    /**
     * The number of structural features of the '<em>Subroutine Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE_FEATURE_COUNT = 6;

    /**
     * The number of operations of the '<em>Subroutine Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int SUBROUTINE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.TDeclStmtTypeImpl
     * <em>TDecl Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.TDeclStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getTDeclStmtType()
     * @generated
     */
    int TDECL_STMT_TYPE = 122;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TDECL_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TDECL_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>EN Decl LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TDECL_STMT_TYPE__EN_DECL_LT = 2;

    /**
     * The feature id for the '<em><b>TSpec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TDECL_STMT_TYPE__TSPEC = 3;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TDECL_STMT_TYPE__ATTRIBUTE = 4;

    /**
     * The number of structural features of the '<em>TDecl Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TDECL_STMT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>TDecl Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TDECL_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.TestETypeImpl <em>Test
     * EType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.TestETypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getTestEType()
     * @generated
     */
    int TEST_ETYPE = 123;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TEST_ETYPE__NAMED_E = 0;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TEST_ETYPE__OP_E = 1;

    /**
     * The number of structural features of the '<em>Test EType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TEST_ETYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Test EType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TEST_ETYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.TNTypeImpl <em>TN
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.TNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getTNType()
     * @generated
     */
    int TN_TYPE = 124;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TN_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TN_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>TN Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TN_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>TN Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TN_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.TSpecTypeImpl <em>TSpec
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.TSpecTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getTSpecType()
     * @generated
     */
    int TSPEC_TYPE = 125;

    /**
     * The feature id for the '<em><b>Derived TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSPEC_TYPE__DERIVED_TSPEC = 0;

    /**
     * The feature id for the '<em><b>Intrinsic TSpec</b></em>' containment reference. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSPEC_TYPE__INTRINSIC_TSPEC = 1;

    /**
     * The number of structural features of the '<em>TSpec Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSPEC_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>TSpec Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSPEC_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.TStmtTypeImpl <em>TStmt
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.TStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getTStmtType()
     * @generated
     */
    int TSTMT_TYPE = 126;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSTMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSTMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>TN</b></em>' containment reference list. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSTMT_TYPE__TN = 2;

    /**
     * The feature id for the '<em><b>Attribute</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSTMT_TYPE__ATTRIBUTE = 3;

    /**
     * The number of structural features of the '<em>TStmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSTMT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>TStmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int TSTMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.UpperBoundTypeImpl
     * <em>Upper Bound Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.UpperBoundTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getUpperBoundType()
     * @generated
     */
    int UPPER_BOUND_TYPE = 127;

    /**
     * The feature id for the '<em><b>Literal E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int UPPER_BOUND_TYPE__LITERAL_E = 0;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int UPPER_BOUND_TYPE__NAMED_E = 1;

    /**
     * The feature id for the '<em><b>Op E</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int UPPER_BOUND_TYPE__OP_E = 2;

    /**
     * The number of structural features of the '<em>Upper Bound Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int UPPER_BOUND_TYPE_FEATURE_COUNT = 3;

    /**
     * The number of operations of the '<em>Upper Bound Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int UPPER_BOUND_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.UseNTypeImpl <em>Use
     * NType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.UseNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getUseNType()
     * @generated
     */
    int USE_NTYPE = 128;

    /**
     * The feature id for the '<em><b>N</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_NTYPE__N = 0;

    /**
     * The number of structural features of the '<em>Use NType</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_NTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>Use NType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_NTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.UseStmtTypeImpl <em>Use
     * Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.UseStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getUseStmtType()
     * @generated
     */
    int USE_STMT_TYPE = 129;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Module N</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_STMT_TYPE__MODULE_N = 2;

    /**
     * The feature id for the '<em><b>Rename LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_STMT_TYPE__RENAME_LT = 3;

    /**
     * The number of structural features of the '<em>Use Stmt Type</em>' class. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_STMT_TYPE_FEATURE_COUNT = 4;

    /**
     * The number of operations of the '<em>Use Stmt Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int USE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.VNTypeImpl <em>VN
     * Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.VNTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getVNType()
     * @generated
     */
    int VN_TYPE = 130;

    /**
     * The feature id for the '<em><b>VN</b></em>' containment reference. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int VN_TYPE__VN = 0;

    /**
     * The feature id for the '<em><b>N</b></em>' attribute. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int VN_TYPE__N = 1;

    /**
     * The number of structural features of the '<em>VN Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int VN_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>VN Type</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int VN_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.VTypeImpl
     * <em>VType</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.VTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getVType()
     * @generated
     */
    int VTYPE = 131;

    /**
     * The feature id for the '<em><b>Named E</b></em>' containment reference. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int VTYPE__NAMED_E = 0;

    /**
     * The number of structural features of the '<em>VType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int VTYPE_FEATURE_COUNT = 1;

    /**
     * The number of operations of the '<em>VType</em>' class. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int VTYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the
     * '{@link org.oceandsl.tools.sar.fxtran.impl.WhereConstructStmtTypeImpl <em>Where Construct
     * Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.WhereConstructStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getWhereConstructStmtType()
     * @generated
     */
    int WHERE_CONSTRUCT_STMT_TYPE = 132;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_CONSTRUCT_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Mask E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_CONSTRUCT_STMT_TYPE__MASK_E = 1;

    /**
     * The number of structural features of the '<em>Where Construct Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_CONSTRUCT_STMT_TYPE_FEATURE_COUNT = 2;

    /**
     * The number of operations of the '<em>Where Construct Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_CONSTRUCT_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.WhereStmtTypeImpl
     * <em>Where Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.WhereStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getWhereStmtType()
     * @generated
     */
    int WHERE_STMT_TYPE = 133;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Action Stmt</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_STMT_TYPE__ACTION_STMT = 2;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_STMT_TYPE__CNT = 3;

    /**
     * The feature id for the '<em><b>Mask E</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_STMT_TYPE__MASK_E = 4;

    /**
     * The number of structural features of the '<em>Where Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_STMT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Where Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WHERE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * The meta object id for the '{@link org.oceandsl.tools.sar.fxtran.impl.WriteStmtTypeImpl
     * <em>Write Stmt Type</em>}' class. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.oceandsl.tools.sar.fxtran.impl.WriteStmtTypeImpl
     * @see org.oceandsl.tools.sar.fxtran.impl.FxtranPackageImpl#getWriteStmtType()
     * @generated
     */
    int WRITE_STMT_TYPE = 134;

    /**
     * The feature id for the '<em><b>Mixed</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WRITE_STMT_TYPE__MIXED = 0;

    /**
     * The feature id for the '<em><b>Group</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WRITE_STMT_TYPE__GROUP = 1;

    /**
     * The feature id for the '<em><b>Cnt</b></em>' attribute list. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WRITE_STMT_TYPE__CNT = 2;

    /**
     * The feature id for the '<em><b>Io Control Spec</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WRITE_STMT_TYPE__IO_CONTROL_SPEC = 3;

    /**
     * The feature id for the '<em><b>Output Item LT</b></em>' containment reference list. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WRITE_STMT_TYPE__OUTPUT_ITEM_LT = 4;

    /**
     * The number of structural features of the '<em>Write Stmt Type</em>' class. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WRITE_STMT_TYPE_FEATURE_COUNT = 5;

    /**
     * The number of operations of the '<em>Write Stmt Type</em>' class. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     * @ordered
     */
    int WRITE_STMT_TYPE_OPERATION_COUNT = 0;

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType
     * <em>Action Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Action Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType
     * @generated
     */
    EClass getActionStmtType();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getReturnStmt <em>Return Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Return Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getReturnStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EAttribute getActionStmtType_ReturnStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getWhereStmt <em>Where Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Where Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getWhereStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EReference getActionStmtType_WhereStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getAStmt <em>AStmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>AStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getAStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EReference getActionStmtType_AStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getAllocateStmt <em>Allocate
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Allocate Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getAllocateStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EReference getActionStmtType_AllocateStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getCallStmt <em>Call Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Call Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getCallStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EReference getActionStmtType_CallStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getDeallocateStmt <em>Deallocate
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Deallocate Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getDeallocateStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EReference getActionStmtType_DeallocateStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getExitStmt <em>Exit Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Exit Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getExitStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EAttribute getActionStmtType_ExitStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getPointerAStmt <em>Pointer
     * AStmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Pointer AStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getPointerAStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EReference getActionStmtType_PointerAStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ActionStmtType#getCycleStmt <em>Cycle Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Cycle Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ActionStmtType#getCycleStmt()
     * @see #getActionStmtType()
     * @generated
     */
    EReference getActionStmtType_CycleStmt();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.AcValueLTType <em>Ac
     * Value LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Ac Value LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueLTType
     * @generated
     */
    EClass getAcValueLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueLTType#getMixed()
     * @see #getAcValueLTType()
     * @generated
     */
    EAttribute getAcValueLTType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueLTType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueLTType#getGroup()
     * @see #getAcValueLTType()
     * @generated
     */
    EAttribute getAcValueLTType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueLTType#getC <em>C</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>C</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueLTType#getC()
     * @see #getAcValueLTType()
     * @generated
     */
    EAttribute getAcValueLTType_C();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueLTType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueLTType#getCnt()
     * @see #getAcValueLTType()
     * @generated
     */
    EAttribute getAcValueLTType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueLTType#getAcValue <em>Ac Value</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Ac Value</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueLTType#getAcValue()
     * @see #getAcValueLTType()
     * @generated
     */
    EReference getAcValueLTType_AcValue();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.AcValueType <em>Ac
     * Value Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Ac Value Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueType
     * @generated
     */
    EClass getAcValueType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueType#getLiteralE()
     * @see #getAcValueType()
     * @generated
     */
    EReference getAcValueType_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueType#getNamedE()
     * @see #getAcValueType()
     * @generated
     */
    EReference getAcValueType_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getOpE <em>Op E</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueType#getOpE()
     * @see #getAcValueType()
     * @generated
     */
    EReference getAcValueType_OpE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.AcValueType#getParensE <em>Parens E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Parens E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AcValueType#getParensE()
     * @see #getAcValueType()
     * @generated
     */
    EReference getAcValueType_ParensE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.AllocateStmtType
     * <em>Allocate Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Allocate Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AllocateStmtType
     * @generated
     */
    EClass getAllocateStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AllocateStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AllocateStmtType#getMixed()
     * @see #getAllocateStmtType()
     * @generated
     */
    EAttribute getAllocateStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.AllocateStmtType#getArgSpec <em>Arg Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AllocateStmtType#getArgSpec()
     * @see #getAllocateStmtType()
     * @generated
     */
    EReference getAllocateStmtType_ArgSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ArgNType <em>Arg
     * NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Arg NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgNType
     * @generated
     */
    EClass getArgNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ArgNType#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgNType#getN()
     * @see #getArgNType()
     * @generated
     */
    EReference getArgNType_N();

    /**
     * Returns the meta object for the attribute '{@link org.oceandsl.tools.sar.fxtran.ArgNType#getK
     * <em>K</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>K</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgNType#getK()
     * @see #getArgNType()
     * @generated
     */
    EAttribute getArgNType_K();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ArgNType#getN1 <em>N1</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>N1</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgNType#getN1()
     * @see #getArgNType()
     * @generated
     */
    EAttribute getArgNType_N1();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ArgSpecType <em>Arg
     * Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Arg Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgSpecType
     * @generated
     */
    EClass getArgSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgSpecType#getMixed()
     * @see #getArgSpecType()
     * @generated
     */
    EAttribute getArgSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgSpecType#getGroup()
     * @see #getArgSpecType()
     * @generated
     */
    EAttribute getArgSpecType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgSpecType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgSpecType#getCnt()
     * @see #getArgSpecType()
     * @generated
     */
    EAttribute getArgSpecType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgSpecType#getArg <em>Arg</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgSpecType#getArg()
     * @see #getArgSpecType()
     * @generated
     */
    EReference getArgSpecType_Arg();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ArgType <em>Arg
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Arg Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType
     * @generated
     */
    EClass getArgType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getMixed <em>Mixed</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getMixed()
     * @see #getArgType()
     * @generated
     */
    EAttribute getArgType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getGroup <em>Group</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getGroup()
     * @see #getArgType()
     * @generated
     */
    EAttribute getArgType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getArgN <em>Arg N</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getArgN()
     * @see #getArgType()
     * @generated
     */
    EReference getArgType_ArgN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getArrayConstructorE <em>Array Constructor
     * E</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Array Constructor E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getArrayConstructorE()
     * @see #getArgType()
     * @generated
     */
    EReference getArgType_ArrayConstructorE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getLiteralE()
     * @see #getArgType()
     * @generated
     */
    EReference getArgType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getNamedE()
     * @see #getArgType()
     * @generated
     */
    EReference getArgType_NamedE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getOpE <em>Op E</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getOpE()
     * @see #getArgType()
     * @generated
     */
    EReference getArgType_OpE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getParensE <em>Parens E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Parens E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getParensE()
     * @see #getArgType()
     * @generated
     */
    EReference getArgType_ParensE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArgType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArgType#getStringE()
     * @see #getArgType()
     * @generated
     */
    EReference getArgType_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ArrayConstructorEType
     * <em>Array Constructor EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Array Constructor EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayConstructorEType
     * @generated
     */
    EClass getArrayConstructorEType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getMixed()
     * @see #getArrayConstructorEType()
     * @generated
     */
    EAttribute getArrayConstructorEType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getGroup()
     * @see #getArrayConstructorEType()
     * @generated
     */
    EAttribute getArrayConstructorEType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getC <em>C</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>C</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getC()
     * @see #getArrayConstructorEType()
     * @generated
     */
    EAttribute getArrayConstructorEType_C();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getCnt()
     * @see #getArrayConstructorEType()
     * @generated
     */
    EAttribute getArrayConstructorEType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getAcValueLT <em>Ac Value
     * LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Ac Value LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayConstructorEType#getAcValueLT()
     * @see #getArrayConstructorEType()
     * @generated
     */
    EReference getArrayConstructorEType_AcValueLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ArrayRType <em>Array
     * RType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Array RType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayRType
     * @generated
     */
    EClass getArrayRType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayRType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayRType#getMixed()
     * @see #getArrayRType()
     * @generated
     */
    EAttribute getArrayRType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArrayRType#getSectionSubscriptLT <em>Section Subscript
     * LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Section Subscript LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArrayRType#getSectionSubscriptLT()
     * @see #getArrayRType()
     * @generated
     */
    EReference getArrayRType_SectionSubscriptLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ArraySpecType
     * <em>Array Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Array Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArraySpecType
     * @generated
     */
    EClass getArraySpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ArraySpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArraySpecType#getMixed()
     * @see #getArraySpecType()
     * @generated
     */
    EAttribute getArraySpecType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ArraySpecType#getShapeSpecLT <em>Shape Spec LT</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Shape Spec LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ArraySpecType#getShapeSpecLT()
     * @see #getArraySpecType()
     * @generated
     */
    EReference getArraySpecType_ShapeSpecLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.AStmtType <em>AStmt
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>AStmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AStmtType
     * @generated
     */
    EClass getAStmtType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.AStmtType#getE1 <em>E1</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>E1</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AStmtType#getE1()
     * @see #getAStmtType()
     * @generated
     */
    EReference getAStmtType_E1();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.AStmtType#getA <em>A</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>A</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AStmtType#getA()
     * @see #getAStmtType()
     * @generated
     */
    EAttribute getAStmtType_A();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.AStmtType#getE2 <em>E2</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>E2</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AStmtType#getE2()
     * @see #getAStmtType()
     * @generated
     */
    EReference getAStmtType_E2();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.AttributeType
     * <em>Attribute Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Attribute Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AttributeType
     * @generated
     */
    EClass getAttributeType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AttributeType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AttributeType#getMixed()
     * @see #getAttributeType()
     * @generated
     */
    EAttribute getAttributeType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AttributeType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AttributeType#getGroup()
     * @see #getAttributeType()
     * @generated
     */
    EAttribute getAttributeType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.AttributeType#getArraySpec <em>Array Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Array Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AttributeType#getArraySpec()
     * @see #getAttributeType()
     * @generated
     */
    EReference getAttributeType_ArraySpec();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AttributeType#getAttributeN <em>Attribute N</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Attribute N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AttributeType#getAttributeN()
     * @see #getAttributeType()
     * @generated
     */
    EAttribute getAttributeType_AttributeN();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.AttributeType#getIntentSpec <em>Intent Spec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Intent Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.AttributeType#getIntentSpec()
     * @see #getAttributeType()
     * @generated
     */
    EAttribute getAttributeType_IntentSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CallStmtType <em>Call
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Call Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CallStmtType
     * @generated
     */
    EClass getCallStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CallStmtType#getMixed()
     * @see #getCallStmtType()
     * @generated
     */
    EAttribute getCallStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CallStmtType#getGroup()
     * @see #getCallStmtType()
     * @generated
     */
    EAttribute getCallStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getArgSpec <em>Arg Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CallStmtType#getArgSpec()
     * @see #getCallStmtType()
     * @generated
     */
    EReference getCallStmtType_ArgSpec();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CallStmtType#getCnt()
     * @see #getCallStmtType()
     * @generated
     */
    EAttribute getCallStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CallStmtType#getProcedureDesignator <em>Procedure
     * Designator</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Procedure Designator</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CallStmtType#getProcedureDesignator()
     * @see #getCallStmtType()
     * @generated
     */
    EReference getCallStmtType_ProcedureDesignator();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CaseEType <em>Case
     * EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Case EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseEType
     * @generated
     */
    EClass getCaseEType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.CaseEType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseEType#getNamedE()
     * @see #getCaseEType()
     * @generated
     */
    EReference getCaseEType_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CaseSelectorType
     * <em>Case Selector Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Case Selector Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseSelectorType
     * @generated
     */
    EClass getCaseSelectorType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CaseSelectorType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseSelectorType#getMixed()
     * @see #getCaseSelectorType()
     * @generated
     */
    EAttribute getCaseSelectorType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CaseSelectorType#getCaseValueRangeLT <em>Case Value
     * Range LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Case Value Range LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseSelectorType#getCaseValueRangeLT()
     * @see #getCaseSelectorType()
     * @generated
     */
    EReference getCaseSelectorType_CaseValueRangeLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CaseStmtType <em>Case
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Case Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseStmtType
     * @generated
     */
    EClass getCaseStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CaseStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseStmtType#getMixed()
     * @see #getCaseStmtType()
     * @generated
     */
    EAttribute getCaseStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CaseStmtType#getCaseSelector <em>Case Selector</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Case Selector</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseStmtType#getCaseSelector()
     * @see #getCaseStmtType()
     * @generated
     */
    EReference getCaseStmtType_CaseSelector();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType
     * <em>Case Value Range LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Case Value Range LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType
     * @generated
     */
    EClass getCaseValueRangeLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType#getMixed()
     * @see #getCaseValueRangeLTType()
     * @generated
     */
    EAttribute getCaseValueRangeLTType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType#getCaseValueRange <em>Case Value
     * Range</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Case Value Range</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueRangeLTType#getCaseValueRange()
     * @see #getCaseValueRangeLTType()
     * @generated
     */
    EReference getCaseValueRangeLTType_CaseValueRange();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeType
     * <em>Case Value Range Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Case Value Range Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueRangeType
     * @generated
     */
    EClass getCaseValueRangeType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueRangeType#getCaseValue <em>Case Value</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Case Value</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueRangeType#getCaseValue()
     * @see #getCaseValueRangeType()
     * @generated
     */
    EReference getCaseValueRangeType_CaseValue();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CaseValueType
     * <em>Case Value Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Case Value Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueType
     * @generated
     */
    EClass getCaseValueType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueType#getLiteralE()
     * @see #getCaseValueType()
     * @generated
     */
    EReference getCaseValueType_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.CaseValueType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CaseValueType#getStringE()
     * @see #getCaseValueType()
     * @generated
     */
    EReference getCaseValueType_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CharSelectorType
     * <em>Char Selector Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Char Selector Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSelectorType
     * @generated
     */
    EClass getCharSelectorType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSelectorType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSelectorType#getMixed()
     * @see #getCharSelectorType()
     * @generated
     */
    EAttribute getCharSelectorType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSelectorType#getCharSpec <em>Char Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Char Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSelectorType#getCharSpec()
     * @see #getCharSelectorType()
     * @generated
     */
    EReference getCharSelectorType_CharSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CharSpecType <em>Char
     * Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Char Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType
     * @generated
     */
    EClass getCharSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getMixed()
     * @see #getCharSpecType()
     * @generated
     */
    EAttribute getCharSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getGroup()
     * @see #getCharSpecType()
     * @generated
     */
    EAttribute getCharSpecType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getArgN <em>Arg N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getArgN()
     * @see #getCharSpecType()
     * @generated
     */
    EReference getCharSpecType_ArgN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getLabel <em>Label</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Label</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getLabel()
     * @see #getCharSpecType()
     * @generated
     */
    EReference getCharSpecType_Label();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getLiteralE()
     * @see #getCharSpecType()
     * @generated
     */
    EReference getCharSpecType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getNamedE()
     * @see #getCharSpecType()
     * @generated
     */
    EReference getCharSpecType_NamedE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getOpE <em>Op E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getOpE()
     * @see #getCharSpecType()
     * @generated
     */
    EReference getCharSpecType_OpE();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CharSpecType#getStarE <em>Star E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Star E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CharSpecType#getStarE()
     * @see #getCharSpecType()
     * @generated
     */
    EAttribute getCharSpecType_StarE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CloseSpecSpecType
     * <em>Close Spec Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Close Spec Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecSpecType
     * @generated
     */
    EClass getCloseSpecSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecSpecType#getMixed()
     * @see #getCloseSpecSpecType()
     * @generated
     */
    EAttribute getCloseSpecSpecType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecSpecType#getCloseSpec <em>Close Spec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Close Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecSpecType#getCloseSpec()
     * @see #getCloseSpecSpecType()
     * @generated
     */
    EReference getCloseSpecSpecType_CloseSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CloseSpecType
     * <em>Close Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Close Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecType
     * @generated
     */
    EClass getCloseSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecType#getMixed()
     * @see #getCloseSpecType()
     * @generated
     */
    EAttribute getCloseSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecType#getGroup()
     * @see #getCloseSpecType()
     * @generated
     */
    EAttribute getCloseSpecType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecType#getArgN <em>Arg N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecType#getArgN()
     * @see #getCloseSpecType()
     * @generated
     */
    EReference getCloseSpecType_ArgN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecType#getLiteralE()
     * @see #getCloseSpecType()
     * @generated
     */
    EReference getCloseSpecType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseSpecType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseSpecType#getNamedE()
     * @see #getCloseSpecType()
     * @generated
     */
    EReference getCloseSpecType_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CloseStmtType
     * <em>Close Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Close Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseStmtType
     * @generated
     */
    EClass getCloseStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseStmtType#getMixed()
     * @see #getCloseStmtType()
     * @generated
     */
    EAttribute getCloseStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CloseStmtType#getCloseSpecSpec <em>Close Spec
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Close Spec Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CloseStmtType#getCloseSpecSpec()
     * @see #getCloseStmtType()
     * @generated
     */
    EReference getCloseStmtType_CloseSpecSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType
     * <em>Component Decl Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Component Decl Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType
     * @generated
     */
    EClass getComponentDeclStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getMixed()
     * @see #getComponentDeclStmtType()
     * @generated
     */
    EAttribute getComponentDeclStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getGroup()
     * @see #getComponentDeclStmtType()
     * @generated
     */
    EAttribute getComponentDeclStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getENDeclLT <em>EN Decl
     * LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>EN Decl LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getENDeclLT()
     * @see #getComponentDeclStmtType()
     * @generated
     */
    EReference getComponentDeclStmtType_ENDeclLT();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getTSpec <em>TSpec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getTSpec()
     * @see #getComponentDeclStmtType()
     * @generated
     */
    EReference getComponentDeclStmtType_TSpec();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getAttribute
     * <em>Attribute</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Attribute</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentDeclStmtType#getAttribute()
     * @see #getComponentDeclStmtType()
     * @generated
     */
    EReference getComponentDeclStmtType_Attribute();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ComponentRType
     * <em>Component RType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Component RType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentRType
     * @generated
     */
    EClass getComponentRType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentRType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentRType#getMixed()
     * @see #getComponentRType()
     * @generated
     */
    EAttribute getComponentRType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ComponentRType#getCt <em>Ct</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Ct</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ComponentRType#getCt()
     * @see #getComponentRType()
     * @generated
     */
    EAttribute getComponentRType_Ct();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ConditionEType
     * <em>Condition EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Condition EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConditionEType
     * @generated
     */
    EClass getConditionEType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ConditionEType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConditionEType#getNamedE()
     * @see #getConditionEType()
     * @generated
     */
    EReference getConditionEType_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ConditionEType#getOpE <em>Op E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConditionEType#getOpE()
     * @see #getConditionEType()
     * @generated
     */
    EReference getConditionEType_OpE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType
     * <em>Connect Spec Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Connect Spec Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType
     * @generated
     */
    EClass getConnectSpecSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getMixed()
     * @see #getConnectSpecSpecType()
     * @generated
     */
    EAttribute getConnectSpecSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getGroup()
     * @see #getConnectSpecSpecType()
     * @generated
     */
    EAttribute getConnectSpecSpecType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getCnt()
     * @see #getConnectSpecSpecType()
     * @generated
     */
    EAttribute getConnectSpecSpecType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getConnectSpec <em>Connect
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Connect Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecSpecType#getConnectSpec()
     * @see #getConnectSpecSpecType()
     * @generated
     */
    EReference getConnectSpecSpecType_ConnectSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType
     * <em>Connect Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Connect Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType
     * @generated
     */
    EClass getConnectSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType#getMixed()
     * @see #getConnectSpecType()
     * @generated
     */
    EAttribute getConnectSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType#getGroup()
     * @see #getConnectSpecType()
     * @generated
     */
    EAttribute getConnectSpecType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType#getArgN <em>Arg N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType#getArgN()
     * @see #getConnectSpecType()
     * @generated
     */
    EReference getConnectSpecType_ArgN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType#getLiteralE()
     * @see #getConnectSpecType()
     * @generated
     */
    EReference getConnectSpecType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType#getNamedE()
     * @see #getConnectSpecType()
     * @generated
     */
    EReference getConnectSpecType_NamedE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ConnectSpecType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ConnectSpecType#getStringE()
     * @see #getConnectSpecType()
     * @generated
     */
    EReference getConnectSpecType_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.CycleStmtType
     * <em>Cycle Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Cycle Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CycleStmtType
     * @generated
     */
    EClass getCycleStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.CycleStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CycleStmtType#getMixed()
     * @see #getCycleStmtType()
     * @generated
     */
    EAttribute getCycleStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.CycleStmtType#getN <em>N</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.CycleStmtType#getN()
     * @see #getCycleStmtType()
     * @generated
     */
    EReference getCycleStmtType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.DeallocateStmtType
     * <em>Deallocate Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Deallocate Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DeallocateStmtType
     * @generated
     */
    EClass getDeallocateStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DeallocateStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DeallocateStmtType#getMixed()
     * @see #getDeallocateStmtType()
     * @generated
     */
    EAttribute getDeallocateStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DeallocateStmtType#getArgSpec <em>Arg Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DeallocateStmtType#getArgSpec()
     * @see #getDeallocateStmtType()
     * @generated
     */
    EReference getDeallocateStmtType_ArgSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.DerivedTSpecType
     * <em>Derived TSpec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Derived TSpec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DerivedTSpecType
     * @generated
     */
    EClass getDerivedTSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DerivedTSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DerivedTSpecType#getMixed()
     * @see #getDerivedTSpecType()
     * @generated
     */
    EAttribute getDerivedTSpecType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DerivedTSpecType#getTN <em>TN</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>TN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DerivedTSpecType#getTN()
     * @see #getDerivedTSpecType()
     * @generated
     */
    EReference getDerivedTSpecType_TN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot
     * <em>Document Root</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Document Root</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot
     * @generated
     */
    EClass getDocumentRoot();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getMixed()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Mixed();

    /**
     * Returns the meta object for the map
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getXMLNSPrefixMap <em>XMLNS Prefix
     * Map</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the map '<em>XMLNS Prefix Map</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getXMLNSPrefixMap()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XMLNSPrefixMap();

    /**
     * Returns the meta object for the map
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getXSISchemaLocation <em>XSI Schema
     * Location</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the map '<em>XSI Schema Location</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getXSISchemaLocation()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_XSISchemaLocation();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTSpec <em>TSpec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getTSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TSpec();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getA <em>A</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>A</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getA()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_A();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAStmt <em>AStmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>AStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getAStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValue <em>Ac Value</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Ac Value</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AcValue();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValueLT <em>Ac Value LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Ac Value LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getAcValueLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AcValueLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getActionStmt <em>Action Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Action Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getActionStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ActionStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAllocateStmt <em>Allocate Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Allocate Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getAllocateStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_AllocateStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArg <em>Arg</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Arg</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getArg()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Arg();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgN <em>Arg N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ArgN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgSpec <em>Arg Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Arg Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getArgSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ArgSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayConstructorE <em>Array Constructor
     * E</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Array Constructor E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayConstructorE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ArrayConstructorE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayR <em>Array R</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Array R</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getArrayR()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ArrayR();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getArraySpec <em>Array Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Array Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getArraySpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ArraySpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttribute <em>Attribute</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Attribute</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttribute()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Attribute();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttributeN <em>Attribute N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Attribute N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getAttributeN()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_AttributeN();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getC <em>C</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>C</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getC()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_C();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCallStmt <em>Call Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Call Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCallStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CallStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseE <em>Case E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Case E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CaseE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseSelector <em>Case Selector</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Case Selector</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseSelector()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CaseSelector();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseStmt <em>Case Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Case Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CaseStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValue <em>Case Value</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Case Value</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValue()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CaseValue();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRange <em>Case Value
     * Range</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Case Value Range</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRange()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CaseValueRange();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRangeLT <em>Case Value Range
     * LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Case Value Range LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCaseValueRangeLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CaseValueRangeLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSelector <em>Char Selector</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Char Selector</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSelector()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CharSelector();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSpec <em>Char Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Char Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCharSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CharSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpec <em>Close Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Close Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CloseSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpecSpec <em>Close Spec
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Close Spec Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseSpecSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CloseSpecSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseStmt <em>Close Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Close Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCloseStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CloseStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCnt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Cnt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentDeclStmt <em>Component Decl
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Component Decl Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentDeclStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ComponentDeclStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentR <em>Component R</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Component R</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getComponentR()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ComponentR();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConditionE <em>Condition E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Condition E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getConditionE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ConditionE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpec <em>Connect Spec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Connect Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ConnectSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpecSpec <em>Connect Spec
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Connect Spec Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getConnectSpecSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ConnectSpecSpec();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getContainsStmt <em>Contains Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Contains Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getContainsStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ContainsStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCpp <em>Cpp</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Cpp</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCpp()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Cpp();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCt <em>Ct</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Ct</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Ct();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getCycleStmt <em>Cycle Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Cycle Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getCycleStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_CycleStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDeallocateStmt <em>Deallocate
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Deallocate Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getDeallocateStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DeallocateStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDerivedTSpec <em>Derived TSpec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Derived TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getDerivedTSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DerivedTSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoStmt <em>Do Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Do Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DoStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoV <em>Do V</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Do V</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getDoV()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DoV();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getDummyArgLT <em>Dummy Arg LT</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Dummy Arg LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getDummyArgLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_DummyArgLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getE1 <em>E1</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>E1</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getE1()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_E1();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getE2 <em>E2</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>E2</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getE2()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_E2();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElement <em>Element</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Element</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getElement()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Element();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElementLT <em>Element LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Element LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getElementLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ElementLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseIfStmt <em>Else If Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Else If Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseIfStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ElseIfStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseStmt <em>Else Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Else Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ElseStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseWhereStmt <em>Else Where
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Else Where Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getElseWhereStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ElseWhereStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEN <em>EN</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>EN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDecl <em>EN Decl</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>EN Decl</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDecl()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ENDecl();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDeclLT <em>EN Decl LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>EN Decl LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getENDeclLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ENDeclLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENLT <em>ENLT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>ENLT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getENLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ENLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getENN <em>ENN</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>ENN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getENN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ENN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndDoStmt <em>End Do Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Do Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndDoStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndDoStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndForallStmt <em>End Forall
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Forall Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndForallStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndForallStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndFunctionStmt <em>End Function
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Function Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndFunctionStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndFunctionStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndIfStmt <em>End If Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>End If Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndIfStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_EndIfStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndInterfaceStmt <em>End Interface
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Interface Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndInterfaceStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndInterfaceStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndModuleStmt <em>End Module
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Module Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndModuleStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndModuleStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndProgramStmt <em>End Program
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Program Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndProgramStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndProgramStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSelectCaseStmt <em>End Select Case
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Select Case Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSelectCaseStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndSelectCaseStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSubroutineStmt <em>End Subroutine
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Subroutine Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndSubroutineStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndSubroutineStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndTStmt <em>End TStmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End TStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndTStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_EndTStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndWhereStmt <em>End Where Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>End Where Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getEndWhereStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_EndWhereStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getError <em>Error</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Error</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getError()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Error();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getExitStmt <em>Exit Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Exit Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getExitStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ExitStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFile <em>File</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>File</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getFile()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_File();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallConstructStmt <em>Forall
     * Construct Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Forall Construct Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallConstructStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ForallConstructStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallStmt <em>Forall Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Forall Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ForallStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpec <em>Forall Triplet
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Forall Triplet Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ForallTripletSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpecLT <em>Forall Triplet
     * Spec LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Forall Triplet Spec LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getForallTripletSpecLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ForallTripletSpecLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionN <em>Function N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Function N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_FunctionN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionStmt <em>Function Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Function Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getFunctionStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_FunctionStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfStmt <em>If Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>If Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_IfStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfThenStmt <em>If Then Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>If Then Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIfThenStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_IfThenStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getImplicitNoneStmt <em>Implicit None
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Implicit None Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getImplicitNoneStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ImplicitNoneStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInitE <em>Init E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Init E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getInitE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_InitE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquireStmt <em>Inquire Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Inquire Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquireStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_InquireStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpec <em>Inquiry Spec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Inquiry Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_InquirySpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpecSpec <em>Inquiry Spec
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Inquiry Spec Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getInquirySpecSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_InquirySpecSpec();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntentSpec <em>Intent Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Intent Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntentSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_IntentSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getInterfaceStmt <em>Interface
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Interface Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getInterfaceStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_InterfaceStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntrinsicTSpec <em>Intrinsic
     * TSpec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Intrinsic TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIntrinsicTSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_IntrinsicTSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControl <em>Io Control</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Io Control</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControl()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_IoControl();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControlSpec <em>Io Control
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Io Control Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIoControlSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_IoControlSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIterator <em>Iterator</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Iterator</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIterator()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Iterator();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorDefinitionLT <em>Iterator
     * Definition LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Iterator Definition LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorDefinitionLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_IteratorDefinitionLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorElement <em>Iterator
     * Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Iterator Element</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getIteratorElement()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_IteratorElement();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getK <em>K</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>K</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getK()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_K();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSelector <em>KSelector</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>KSelector</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSelector()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_KSelector();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSpec <em>KSpec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>KSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getKSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_KSpec();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getL <em>L</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>L</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getL()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_L();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLabel <em>Label</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Label</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getLabel()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Label();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getLiteralE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getLowerBound <em>Lower Bound</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Lower Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getLowerBound()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_LowerBound();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getMaskE <em>Mask E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Mask E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getMaskE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_MaskE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleN <em>Module N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Module N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ModuleN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleProcedureNLT <em>Module Procedure
     * NLT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Module Procedure NLT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleProcedureNLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ModuleProcedureNLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleStmt <em>Module Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Module Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getModuleStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ModuleStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getN()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_N();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getN1 <em>N1</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N1</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getN1()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_N1();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamedE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupN <em>Namelist Group
     * N</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Namelist Group N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NamelistGroupN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObj <em>Namelist Group
     * Obj</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Namelist Group Obj</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObj()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NamelistGroupObj();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjLT <em>Namelist Group
     * Obj LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Namelist Group Obj LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NamelistGroupObjLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjN <em>Namelist Group
     * Obj N</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Namelist Group Obj N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistGroupObjN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NamelistGroupObjN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistStmt <em>Namelist Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Namelist Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getNamelistStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NamelistStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getNullifyStmt <em>Nullify Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Nullify Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getNullifyStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_NullifyStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getO <em>O</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>O</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getO()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_O();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getObject <em>Object</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Object</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getObject()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Object();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOp <em>Op</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getOp()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Op();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpE <em>Op E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OpE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpenStmt <em>Open Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Open Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getOpenStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OpenStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItem <em>Output Item</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Output Item</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItem()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OutputItem();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItemLT <em>Output Item LT</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Output Item LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getOutputItemLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_OutputItemLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensE <em>Parens E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Parens E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ParensE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensR <em>Parens R</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Parens R</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getParensR()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ParensR();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerAStmt <em>Pointer AStmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Pointer AStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerAStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PointerAStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerStmt <em>Pointer Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Pointer Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getPointerStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PointerStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrefix <em>Prefix</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Prefix</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrefix()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_Prefix();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrivateStmt <em>Private Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Private Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getPrivateStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_PrivateStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureDesignator <em>Procedure
     * Designator</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Procedure Designator</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureDesignator()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ProcedureDesignator();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureStmt <em>Procedure
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Procedure Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getProcedureStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ProcedureStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramN <em>Program N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Program N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ProgramN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramStmt <em>Program Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Program Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getProgramStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ProgramStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getPublicStmt <em>Public Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Public Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getPublicStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_PublicStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRLT <em>RLT</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>RLT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getRLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_RLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getReadStmt <em>Read Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Read Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getReadStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ReadStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRename <em>Rename</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Rename</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getRename()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_Rename();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getRenameLT <em>Rename LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Rename LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getRenameLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_RenameLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getResultSpec <em>Result Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Result Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getResultSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ResultSpec();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getReturnStmt <em>Return Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Return Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getReturnStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_ReturnStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getS <em>S</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>S</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getS()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_S();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSaveStmt <em>Save Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Save Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getSaveStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_SaveStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscript <em>Section
     * Subscript</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Section Subscript</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscript()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_SectionSubscript();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscriptLT <em>Section
     * Subscript LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Section Subscript LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getSectionSubscriptLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_SectionSubscriptLT();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSelectCaseStmt <em>Select Case
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Select Case Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getSelectCaseStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_SelectCaseStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpec <em>Shape Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Shape Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpec()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ShapeSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpecLT <em>Shape Spec LT</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Shape Spec LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getShapeSpecLT()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_ShapeSpecLT();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStarE <em>Star E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Star E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getStarE()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_StarE();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopCode <em>Stop Code</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Stop Code</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopCode()
     * @see #getDocumentRoot()
     * @generated
     */
    EAttribute getDocumentRoot_StopCode();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopStmt <em>Stop Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Stop Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getStopStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_StopStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getStringE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_StringE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineN <em>Subroutine N</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Subroutine N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_SubroutineN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineStmt <em>Subroutine
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Subroutine Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getSubroutineStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_SubroutineStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTDeclStmt <em>TDecl Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>TDecl Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getTDeclStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TDeclStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTN <em>TN</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>TN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getTN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTStmt <em>TStmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>TStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getTStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getTestE <em>Test E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Test E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getTestE()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_TestE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUpperBound <em>Upper Bound</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Upper Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getUpperBound()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_UpperBound();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseN <em>Use N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Use N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_UseN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseStmt <em>Use Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Use Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getUseStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_UseStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getV <em>V</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>V</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getV()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_V();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getVN <em>VN</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>VN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getVN()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_VN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereConstructStmt <em>Where Construct
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Where Construct Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereConstructStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_WhereConstructStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereStmt <em>Where Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Where Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getWhereStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_WhereStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DocumentRoot#getWriteStmt <em>Write Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Write Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DocumentRoot#getWriteStmt()
     * @see #getDocumentRoot()
     * @generated
     */
    EReference getDocumentRoot_WriteStmt();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.DoStmtType <em>Do
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Do Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType
     * @generated
     */
    EClass getDoStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DoStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType#getMixed()
     * @see #getDoStmtType()
     * @generated
     */
    EAttribute getDoStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DoStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType#getGroup()
     * @see #getDoStmtType()
     * @generated
     */
    EAttribute getDoStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DoStmtType#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType#getN()
     * @see #getDoStmtType()
     * @generated
     */
    EReference getDoStmtType_N();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DoStmtType#getLowerBound <em>Lower Bound</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Lower Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType#getLowerBound()
     * @see #getDoStmtType()
     * @generated
     */
    EReference getDoStmtType_LowerBound();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DoStmtType#getUpperBound <em>Upper Bound</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Upper Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType#getUpperBound()
     * @see #getDoStmtType()
     * @generated
     */
    EReference getDoStmtType_UpperBound();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DoStmtType#getDoV <em>Do V</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Do V</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType#getDoV()
     * @see #getDoStmtType()
     * @generated
     */
    EReference getDoStmtType_DoV();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DoStmtType#getTestE <em>Test E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Test E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoStmtType#getTestE()
     * @see #getDoStmtType()
     * @generated
     */
    EReference getDoStmtType_TestE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.DoVType <em>Do
     * VType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Do VType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoVType
     * @generated
     */
    EClass getDoVType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.DoVType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DoVType#getNamedE()
     * @see #getDoVType()
     * @generated
     */
    EReference getDoVType_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.DummyArgLTType
     * <em>Dummy Arg LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Dummy Arg LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DummyArgLTType
     * @generated
     */
    EClass getDummyArgLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DummyArgLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DummyArgLTType#getMixed()
     * @see #getDummyArgLTType()
     * @generated
     */
    EAttribute getDummyArgLTType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DummyArgLTType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DummyArgLTType#getGroup()
     * @see #getDummyArgLTType()
     * @generated
     */
    EAttribute getDummyArgLTType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.DummyArgLTType#getArgN <em>Arg N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DummyArgLTType#getArgN()
     * @see #getDummyArgLTType()
     * @generated
     */
    EReference getDummyArgLTType_ArgN();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.DummyArgLTType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.DummyArgLTType#getCnt()
     * @see #getDummyArgLTType()
     * @generated
     */
    EAttribute getDummyArgLTType_Cnt();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.E1Type <em>E1
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>E1 Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E1Type
     * @generated
     */
    EClass getE1Type();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.E1Type#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E1Type#getNamedE()
     * @see #getE1Type()
     * @generated
     */
    EReference getE1Type_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.E2Type <em>E2
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>E2 Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E2Type
     * @generated
     */
    EClass getE2Type();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.E2Type#getArrayConstructorE <em>Array Constructor
     * E</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Array Constructor E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E2Type#getArrayConstructorE()
     * @see #getE2Type()
     * @generated
     */
    EReference getE2Type_ArrayConstructorE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.E2Type#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E2Type#getLiteralE()
     * @see #getE2Type()
     * @generated
     */
    EReference getE2Type_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.E2Type#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E2Type#getNamedE()
     * @see #getE2Type()
     * @generated
     */
    EReference getE2Type_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.E2Type#getOpE <em>Op E</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E2Type#getOpE()
     * @see #getE2Type()
     * @generated
     */
    EReference getE2Type_OpE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.E2Type#getParensE <em>Parens E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Parens E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E2Type#getParensE()
     * @see #getE2Type()
     * @generated
     */
    EReference getE2Type_ParensE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.E2Type#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.E2Type#getStringE()
     * @see #getE2Type()
     * @generated
     */
    EReference getE2Type_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ElementLTType
     * <em>Element LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Element LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementLTType
     * @generated
     */
    EClass getElementLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ElementLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementLTType#getMixed()
     * @see #getElementLTType()
     * @generated
     */
    EAttribute getElementLTType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ElementLTType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementLTType#getGroup()
     * @see #getElementLTType()
     * @generated
     */
    EAttribute getElementLTType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ElementLTType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementLTType#getCnt()
     * @see #getElementLTType()
     * @generated
     */
    EAttribute getElementLTType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ElementLTType#getElement <em>Element</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Element</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementLTType#getElement()
     * @see #getElementLTType()
     * @generated
     */
    EReference getElementLTType_Element();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ElementType
     * <em>Element Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Element Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementType
     * @generated
     */
    EClass getElementType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ElementType#getArrayConstructorE <em>Array Constructor
     * E</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Array Constructor E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementType#getArrayConstructorE()
     * @see #getElementType()
     * @generated
     */
    EReference getElementType_ArrayConstructorE();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ElementType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementType#getGroup()
     * @see #getElementType()
     * @generated
     */
    EAttribute getElementType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ElementType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementType#getNamedE()
     * @see #getElementType()
     * @generated
     */
    EReference getElementType_NamedE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ElementType#getOpE <em>Op E</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementType#getOpE()
     * @see #getElementType()
     * @generated
     */
    EReference getElementType_OpE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ElementType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementType#getLiteralE()
     * @see #getElementType()
     * @generated
     */
    EReference getElementType_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ElementType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElementType#getStringE()
     * @see #getElementType()
     * @generated
     */
    EReference getElementType_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ElseIfStmtType
     * <em>Else If Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Else If Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElseIfStmtType
     * @generated
     */
    EClass getElseIfStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ElseIfStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElseIfStmtType#getMixed()
     * @see #getElseIfStmtType()
     * @generated
     */
    EAttribute getElseIfStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ElseIfStmtType#getConditionE <em>Condition E</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Condition E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ElseIfStmtType#getConditionE()
     * @see #getElseIfStmtType()
     * @generated
     */
    EReference getElseIfStmtType_ConditionE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndDoStmtType <em>End
     * Do Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Do Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndDoStmtType
     * @generated
     */
    EClass getEndDoStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndDoStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndDoStmtType#getMixed()
     * @see #getEndDoStmtType()
     * @generated
     */
    EAttribute getEndDoStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndDoStmtType#getN <em>N</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndDoStmtType#getN()
     * @see #getEndDoStmtType()
     * @generated
     */
    EReference getEndDoStmtType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType <em>EN
     * Decl LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>EN Decl LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclLTType
     * @generated
     */
    EClass getENDeclLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclLTType#getMixed()
     * @see #getENDeclLTType()
     * @generated
     */
    EAttribute getENDeclLTType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclLTType#getGroup()
     * @see #getENDeclLTType()
     * @generated
     */
    EAttribute getENDeclLTType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclLTType#getCnt()
     * @see #getENDeclLTType()
     * @generated
     */
    EAttribute getENDeclLTType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclLTType#getENDecl <em>EN Decl</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>EN Decl</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclLTType#getENDecl()
     * @see #getENDeclLTType()
     * @generated
     */
    EReference getENDeclLTType_ENDecl();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ENDeclType <em>EN
     * Decl Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>EN Decl Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclType
     * @generated
     */
    EClass getENDeclType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclType#getMixed()
     * @see #getENDeclType()
     * @generated
     */
    EAttribute getENDeclType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclType#getGroup()
     * @see #getENDeclType()
     * @generated
     */
    EAttribute getENDeclType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getArraySpec <em>Array Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Array Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclType#getArraySpec()
     * @see #getENDeclType()
     * @generated
     */
    EReference getENDeclType_ArraySpec();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getENN <em>ENN</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>ENN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclType#getENN()
     * @see #getENDeclType()
     * @generated
     */
    EReference getENDeclType_ENN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ENDeclType#getInitE <em>Init E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Init E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENDeclType#getInitE()
     * @see #getENDeclType()
     * @generated
     */
    EReference getENDeclType_InitE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndForallStmtType
     * <em>End Forall Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Forall Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndForallStmtType
     * @generated
     */
    EClass getEndForallStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndForallStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndForallStmtType#getMixed()
     * @see #getEndForallStmtType()
     * @generated
     */
    EAttribute getEndForallStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndForallStmtType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndForallStmtType#getN()
     * @see #getEndForallStmtType()
     * @generated
     */
    EReference getEndForallStmtType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndFunctionStmtType
     * <em>End Function Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Function Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndFunctionStmtType
     * @generated
     */
    EClass getEndFunctionStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndFunctionStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndFunctionStmtType#getMixed()
     * @see #getEndFunctionStmtType()
     * @generated
     */
    EAttribute getEndFunctionStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndFunctionStmtType#getFunctionN <em>Function N</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Function N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndFunctionStmtType#getFunctionN()
     * @see #getEndFunctionStmtType()
     * @generated
     */
    EReference getEndFunctionStmtType_FunctionN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType
     * <em>End Interface Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Interface Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType
     * @generated
     */
    EClass getEndInterfaceStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType#getMixed()
     * @see #getEndInterfaceStmtType()
     * @generated
     */
    EAttribute getEndInterfaceStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndInterfaceStmtType#getN()
     * @see #getEndInterfaceStmtType()
     * @generated
     */
    EReference getEndInterfaceStmtType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndModuleStmtType
     * <em>End Module Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Module Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndModuleStmtType
     * @generated
     */
    EClass getEndModuleStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndModuleStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndModuleStmtType#getMixed()
     * @see #getEndModuleStmtType()
     * @generated
     */
    EAttribute getEndModuleStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndModuleStmtType#getModuleN <em>Module N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Module N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndModuleStmtType#getModuleN()
     * @see #getEndModuleStmtType()
     * @generated
     */
    EReference getEndModuleStmtType_ModuleN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndProgramStmtType
     * <em>End Program Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Program Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndProgramStmtType
     * @generated
     */
    EClass getEndProgramStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndProgramStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndProgramStmtType#getMixed()
     * @see #getEndProgramStmtType()
     * @generated
     */
    EAttribute getEndProgramStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndProgramStmtType#getProgramN <em>Program N</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Program N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndProgramStmtType#getProgramN()
     * @see #getEndProgramStmtType()
     * @generated
     */
    EReference getEndProgramStmtType_ProgramN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType
     * <em>End Select Case Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Select Case Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType
     * @generated
     */
    EClass getEndSelectCaseStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType#getMixed()
     * @see #getEndSelectCaseStmtType()
     * @generated
     */
    EAttribute getEndSelectCaseStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndSelectCaseStmtType#getN()
     * @see #getEndSelectCaseStmtType()
     * @generated
     */
    EReference getEndSelectCaseStmtType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType
     * <em>End Subroutine Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End Subroutine Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType
     * @generated
     */
    EClass getEndSubroutineStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType#getMixed()
     * @see #getEndSubroutineStmtType()
     * @generated
     */
    EAttribute getEndSubroutineStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType#getSubroutineN <em>Subroutine
     * N</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Subroutine N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndSubroutineStmtType#getSubroutineN()
     * @see #getEndSubroutineStmtType()
     * @generated
     */
    EReference getEndSubroutineStmtType_SubroutineN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.EndTStmtType <em>End
     * TStmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>End TStmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndTStmtType
     * @generated
     */
    EClass getEndTStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.EndTStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndTStmtType#getMixed()
     * @see #getEndTStmtType()
     * @generated
     */
    EAttribute getEndTStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.EndTStmtType#getTN <em>TN</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>TN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.EndTStmtType#getTN()
     * @see #getEndTStmtType()
     * @generated
     */
    EReference getEndTStmtType_TN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ENLTType <em>ENLT
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>ENLT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENLTType
     * @generated
     */
    EClass getENLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENLTType#getMixed <em>Mixed</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENLTType#getMixed()
     * @see #getENLTType()
     * @generated
     */
    EAttribute getENLTType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENLTType#getGroup <em>Group</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENLTType#getGroup()
     * @see #getENLTType()
     * @generated
     */
    EAttribute getENLTType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ENLTType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENLTType#getCnt()
     * @see #getENLTType()
     * @generated
     */
    EAttribute getENLTType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ENLTType#getEN <em>EN</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>EN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENLTType#getEN()
     * @see #getENLTType()
     * @generated
     */
    EReference getENLTType_EN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ENNType <em>ENN
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>ENN Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENNType
     * @generated
     */
    EClass getENNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ENNType#getN <em>N</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENNType#getN()
     * @see #getENNType()
     * @generated
     */
    EReference getENNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ENType <em>EN
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>EN Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENType
     * @generated
     */
    EClass getENType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ENType#getN <em>N</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ENType#getN()
     * @see #getENType()
     * @generated
     */
    EReference getENType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ErrorType <em>Error
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Error Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ErrorType
     * @generated
     */
    EClass getErrorType();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ErrorType#getMsg <em>Msg</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Msg</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ErrorType#getMsg()
     * @see #getErrorType()
     * @generated
     */
    EAttribute getErrorType_Msg();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.FileType <em>File
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>File Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType
     * @generated
     */
    EClass getFileType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getGroup <em>Group</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getGroup()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getC <em>C</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>C</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getC()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_C();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getAStmt <em>AStmt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>AStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getAStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_AStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getAllocateStmt <em>Allocate Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Allocate Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getAllocateStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_AllocateStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getCallStmt <em>Call Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Call Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getCallStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_CallStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getDeallocateStmt <em>Deallocate Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Deallocate Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getDeallocateStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_DeallocateStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getExitStmt <em>Exit Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Exit Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getExitStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_ExitStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getPointerAStmt <em>Pointer AStmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Pointer AStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getPointerAStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_PointerAStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getReturnStmt <em>Return Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Return Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getReturnStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_ReturnStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getWhereStmt <em>Where Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Where Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getWhereStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_WhereStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getTDeclStmt <em>TDecl Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>TDecl Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getTDeclStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_TDeclStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getTStmt <em>TStmt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>TStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getTStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_TStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getCaseStmt <em>Case Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Case Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getCaseStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_CaseStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getCloseStmt <em>Close Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Close Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getCloseStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_CloseStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getComponentDeclStmt <em>Component Decl
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Component Decl Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getComponentDeclStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ComponentDeclStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getContainsStmt <em>Contains Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Contains Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getContainsStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_ContainsStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getCpp <em>Cpp</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cpp</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getCpp()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_Cpp();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getDoStmt <em>Do Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Do Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getDoStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_DoStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getElseIfStmt <em>Else If Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Else If Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getElseIfStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ElseIfStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getElseStmt <em>Else Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Else Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getElseStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_ElseStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getElseWhereStmt <em>Else Where Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Else Where Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getElseWhereStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_ElseWhereStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndTStmt <em>End TStmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>End TStmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndTStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndTStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndDoStmt <em>End Do Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>End Do Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndDoStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndDoStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndForallStmt <em>End Forall Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>End Forall Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndForallStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndForallStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndFunctionStmt <em>End Function
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>End Function Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndFunctionStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndFunctionStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndIfStmt <em>End If Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>End If Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndIfStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_EndIfStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndInterfaceStmt <em>End Interface
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>End Interface Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndInterfaceStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndInterfaceStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndSelectCaseStmt <em>End Select Case
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>End Select Case Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndSelectCaseStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndSelectCaseStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndSubroutineStmt <em>End Subroutine
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>End Subroutine Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndSubroutineStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndSubroutineStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndWhereStmt <em>End Where Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>End Where Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndWhereStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_EndWhereStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getForallConstructStmt <em>Forall Construct
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Forall Construct Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getForallConstructStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ForallConstructStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getForallStmt <em>Forall Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Forall Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getForallStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ForallStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getFunctionStmt <em>Function Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Function Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getFunctionStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_FunctionStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getIfStmt <em>If Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>If Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getIfStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_IfStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getIfThenStmt <em>If Then Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>If Then Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getIfThenStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_IfThenStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getImplicitNoneStmt <em>Implicit None
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Implicit None Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getImplicitNoneStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_ImplicitNoneStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getInquireStmt <em>Inquire Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Inquire Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getInquireStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_InquireStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getInterfaceStmt <em>Interface Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Interface Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getInterfaceStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_InterfaceStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getModuleStmt <em>Module Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Module Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getModuleStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ModuleStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getNamelistStmt <em>Namelist Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Namelist Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getNamelistStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_NamelistStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getNullifyStmt <em>Nullify Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Nullify Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getNullifyStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_NullifyStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getOpenStmt <em>Open Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Open Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getOpenStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_OpenStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getPointerStmt <em>Pointer Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Pointer Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getPointerStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_PointerStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getPrivateStmt <em>Private Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Private Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getPrivateStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_PrivateStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getProcedureStmt <em>Procedure Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Procedure Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getProcedureStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ProcedureStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getProgramStmt <em>Program Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Program Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getProgramStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ProgramStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getPublicStmt <em>Public Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Public Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getPublicStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_PublicStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getReadStmt <em>Read Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Read Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getReadStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_ReadStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getSaveStmt <em>Save Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Save Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getSaveStmt()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_SaveStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getSelectCaseStmt <em>Select Case Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Select Case Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getSelectCaseStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_SelectCaseStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getStopStmt <em>Stop Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Stop Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getStopStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_StopStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getSubroutineStmt <em>Subroutine Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Subroutine Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getSubroutineStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_SubroutineStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getUseStmt <em>Use Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Use Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getUseStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_UseStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getWhereConstructStmt <em>Where Construct
     * Stmt</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Where Construct Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getWhereConstructStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_WhereConstructStmt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getWriteStmt <em>Write Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Write Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getWriteStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_WriteStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndModuleStmt <em>End Module Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Module Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndModuleStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndModuleStmt();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getEndProgramStmt <em>End Program Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>End Program Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getEndProgramStmt()
     * @see #getFileType()
     * @generated
     */
    EReference getFileType_EndProgramStmt();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.FileType#getName <em>Name</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Name</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FileType#getName()
     * @see #getFileType()
     * @generated
     */
    EAttribute getFileType_Name();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType <em>Forall Construct Stmt
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Forall Construct Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallConstructStmtType
     * @generated
     */
    EClass getForallConstructStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getMixed()
     * @see #getForallConstructStmtType()
     * @generated
     */
    EAttribute getForallConstructStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getGroup()
     * @see #getForallConstructStmtType()
     * @generated
     */
    EAttribute getForallConstructStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getN()
     * @see #getForallConstructStmtType()
     * @generated
     */
    EReference getForallConstructStmtType_N();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getForallTripletSpecLT
     * <em>Forall Triplet Spec LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Forall Triplet Spec LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallConstructStmtType#getForallTripletSpecLT()
     * @see #getForallConstructStmtType()
     * @generated
     */
    EReference getForallConstructStmtType_ForallTripletSpecLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType
     * <em>Forall Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Forall Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType
     * @generated
     */
    EClass getForallStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType#getMixed()
     * @see #getForallStmtType()
     * @generated
     */
    EAttribute getForallStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType#getGroup()
     * @see #getForallStmtType()
     * @generated
     */
    EAttribute getForallStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType#getActionStmt <em>Action Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Action Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType#getActionStmt()
     * @see #getForallStmtType()
     * @generated
     */
    EReference getForallStmtType_ActionStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType#getCnt()
     * @see #getForallStmtType()
     * @generated
     */
    EAttribute getForallStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType#getForallTripletSpecLT <em>Forall
     * Triplet Spec LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Forall Triplet Spec LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType#getForallTripletSpecLT()
     * @see #getForallStmtType()
     * @generated
     */
    EReference getForallStmtType_ForallTripletSpecLT();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallStmtType#getMaskE <em>Mask E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Mask E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallStmtType#getMaskE()
     * @see #getForallStmtType()
     * @generated
     */
    EReference getForallStmtType_MaskE();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType <em>Forall Triplet Spec LT
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Forall Triplet Spec LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType
     * @generated
     */
    EClass getForallTripletSpecLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType#getMixed()
     * @see #getForallTripletSpecLTType()
     * @generated
     */
    EAttribute getForallTripletSpecLTType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType#getForallTripletSpec <em>Forall
     * Triplet Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Forall Triplet Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecLTType#getForallTripletSpec()
     * @see #getForallTripletSpecLTType()
     * @generated
     */
    EReference getForallTripletSpecLTType_ForallTripletSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecType
     * <em>Forall Triplet Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Forall Triplet Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecType
     * @generated
     */
    EClass getForallTripletSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getMixed()
     * @see #getForallTripletSpecType()
     * @generated
     */
    EAttribute getForallTripletSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getGroup()
     * @see #getForallTripletSpecType()
     * @generated
     */
    EAttribute getForallTripletSpecType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getLowerBound <em>Lower
     * Bound</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Lower Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getLowerBound()
     * @see #getForallTripletSpecType()
     * @generated
     */
    EReference getForallTripletSpecType_LowerBound();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getUpperBound <em>Upper
     * Bound</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Upper Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getUpperBound()
     * @see #getForallTripletSpecType()
     * @generated
     */
    EReference getForallTripletSpecType_UpperBound();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getV <em>V</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>V</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ForallTripletSpecType#getV()
     * @see #getForallTripletSpecType()
     * @generated
     */
    EReference getForallTripletSpecType_V();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.FunctionNType
     * <em>Function NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Function NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionNType
     * @generated
     */
    EClass getFunctionNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionNType#getN <em>N</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionNType#getN()
     * @see #getFunctionNType()
     * @generated
     */
    EReference getFunctionNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType
     * <em>Function Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Function Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType
     * @generated
     */
    EClass getFunctionStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getMixed()
     * @see #getFunctionStmtType()
     * @generated
     */
    EAttribute getFunctionStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getGroup()
     * @see #getFunctionStmtType()
     * @generated
     */
    EAttribute getFunctionStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getDerivedTSpec <em>Derived
     * TSpec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Derived TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getDerivedTSpec()
     * @see #getFunctionStmtType()
     * @generated
     */
    EReference getFunctionStmtType_DerivedTSpec();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getDummyArgLT <em>Dummy Arg LT</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Dummy Arg LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getDummyArgLT()
     * @see #getFunctionStmtType()
     * @generated
     */
    EReference getFunctionStmtType_DummyArgLT();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getFunctionN <em>Function N</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Function N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getFunctionN()
     * @see #getFunctionStmtType()
     * @generated
     */
    EReference getFunctionStmtType_FunctionN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getIntrinsicTSpec <em>Intrinsic
     * TSpec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Intrinsic TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getIntrinsicTSpec()
     * @see #getFunctionStmtType()
     * @generated
     */
    EReference getFunctionStmtType_IntrinsicTSpec();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getPrefix <em>Prefix</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Prefix</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getPrefix()
     * @see #getFunctionStmtType()
     * @generated
     */
    EAttribute getFunctionStmtType_Prefix();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.FunctionStmtType#getResultSpec <em>Result Spec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Result Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.FunctionStmtType#getResultSpec()
     * @see #getFunctionStmtType()
     * @generated
     */
    EReference getFunctionStmtType_ResultSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.IfStmtType <em>If
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>If Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfStmtType
     * @generated
     */
    EClass getIfStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IfStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfStmtType#getMixed()
     * @see #getIfStmtType()
     * @generated
     */
    EAttribute getIfStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IfStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfStmtType#getGroup()
     * @see #getIfStmtType()
     * @generated
     */
    EAttribute getIfStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IfStmtType#getActionStmt <em>Action Stmt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Action Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfStmtType#getActionStmt()
     * @see #getIfStmtType()
     * @generated
     */
    EReference getIfStmtType_ActionStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IfStmtType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfStmtType#getCnt()
     * @see #getIfStmtType()
     * @generated
     */
    EAttribute getIfStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IfStmtType#getConditionE <em>Condition E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Condition E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfStmtType#getConditionE()
     * @see #getIfStmtType()
     * @generated
     */
    EReference getIfStmtType_ConditionE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.IfThenStmtType <em>If
     * Then Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>If Then Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfThenStmtType
     * @generated
     */
    EClass getIfThenStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IfThenStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfThenStmtType#getMixed()
     * @see #getIfThenStmtType()
     * @generated
     */
    EAttribute getIfThenStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IfThenStmtType#getConditionE <em>Condition E</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Condition E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IfThenStmtType#getConditionE()
     * @see #getIfThenStmtType()
     * @generated
     */
    EReference getIfThenStmtType_ConditionE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.InitEType <em>Init
     * EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Init EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InitEType
     * @generated
     */
    EClass getInitEType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.InitEType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InitEType#getLiteralE()
     * @see #getInitEType()
     * @generated
     */
    EReference getInitEType_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.InitEType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InitEType#getNamedE()
     * @see #getInitEType()
     * @generated
     */
    EReference getInitEType_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.InitEType#getOpE <em>Op E</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InitEType#getOpE()
     * @see #getInitEType()
     * @generated
     */
    EReference getInitEType_OpE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.InitEType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InitEType#getStringE()
     * @see #getInitEType()
     * @generated
     */
    EReference getInitEType_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.InquireStmtType
     * <em>Inquire Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Inquire Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquireStmtType
     * @generated
     */
    EClass getInquireStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.InquireStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquireStmtType#getMixed()
     * @see #getInquireStmtType()
     * @generated
     */
    EAttribute getInquireStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.InquireStmtType#getInquirySpecSpec <em>Inquiry Spec
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Inquiry Spec Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquireStmtType#getInquirySpecSpec()
     * @see #getInquireStmtType()
     * @generated
     */
    EReference getInquireStmtType_InquirySpecSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.InquirySpecSpecType
     * <em>Inquiry Spec Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Inquiry Spec Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecSpecType
     * @generated
     */
    EClass getInquirySpecSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecSpecType#getMixed()
     * @see #getInquirySpecSpecType()
     * @generated
     */
    EAttribute getInquirySpecSpecType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecSpecType#getInquirySpec <em>Inquiry
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Inquiry Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecSpecType#getInquirySpec()
     * @see #getInquirySpecSpecType()
     * @generated
     */
    EReference getInquirySpecSpecType_InquirySpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.InquirySpecType
     * <em>Inquiry Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Inquiry Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecType
     * @generated
     */
    EClass getInquirySpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecType#getMixed()
     * @see #getInquirySpecType()
     * @generated
     */
    EAttribute getInquirySpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecType#getGroup()
     * @see #getInquirySpecType()
     * @generated
     */
    EAttribute getInquirySpecType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecType#getArgN <em>Arg N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecType#getArgN()
     * @see #getInquirySpecType()
     * @generated
     */
    EReference getInquirySpecType_ArgN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.InquirySpecType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InquirySpecType#getNamedE()
     * @see #getInquirySpecType()
     * @generated
     */
    EReference getInquirySpecType_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.InterfaceStmtType
     * <em>Interface Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Interface Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InterfaceStmtType
     * @generated
     */
    EClass getInterfaceStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.InterfaceStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InterfaceStmtType#getMixed()
     * @see #getInterfaceStmtType()
     * @generated
     */
    EAttribute getInterfaceStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.InterfaceStmtType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.InterfaceStmtType#getN()
     * @see #getInterfaceStmtType()
     * @generated
     */
    EReference getInterfaceStmtType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType
     * <em>Intrinsic TSpec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Intrinsic TSpec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType
     * @generated
     */
    EClass getIntrinsicTSpecType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getTN <em>TN</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>TN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getTN()
     * @see #getIntrinsicTSpecType()
     * @generated
     */
    EReference getIntrinsicTSpecType_TN();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getKSelector <em>KSelector</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>KSelector</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getKSelector()
     * @see #getIntrinsicTSpecType()
     * @generated
     */
    EReference getIntrinsicTSpecType_KSelector();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getCharSelector <em>Char
     * Selector</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Char Selector</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IntrinsicTSpecType#getCharSelector()
     * @see #getIntrinsicTSpecType()
     * @generated
     */
    EReference getIntrinsicTSpecType_CharSelector();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.IoControlSpecType
     * <em>Io Control Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Io Control Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlSpecType
     * @generated
     */
    EClass getIoControlSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlSpecType#getMixed()
     * @see #getIoControlSpecType()
     * @generated
     */
    EAttribute getIoControlSpecType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlSpecType#getIoControl <em>Io Control</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Io Control</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlSpecType#getIoControl()
     * @see #getIoControlSpecType()
     * @generated
     */
    EReference getIoControlSpecType_IoControl();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.IoControlType <em>Io
     * Control Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Io Control Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType
     * @generated
     */
    EClass getIoControlType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType#getMixed()
     * @see #getIoControlType()
     * @generated
     */
    EAttribute getIoControlType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType#getGroup()
     * @see #getIoControlType()
     * @generated
     */
    EAttribute getIoControlType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType#getArgN <em>Arg N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType#getArgN()
     * @see #getIoControlType()
     * @generated
     */
    EReference getIoControlType_ArgN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType#getLabel <em>Label</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Label</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType#getLabel()
     * @see #getIoControlType()
     * @generated
     */
    EReference getIoControlType_Label();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType#getLiteralE()
     * @see #getIoControlType()
     * @generated
     */
    EReference getIoControlType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType#getNamedE()
     * @see #getIoControlType()
     * @generated
     */
    EReference getIoControlType_NamedE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IoControlType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IoControlType#getStringE()
     * @see #getIoControlType()
     * @generated
     */
    EReference getIoControlType_StringE();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType <em>Iterator Definition LT
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Iterator Definition LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType
     * @generated
     */
    EClass getIteratorDefinitionLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType#getMixed <em>Mixed</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType#getMixed()
     * @see #getIteratorDefinitionLTType()
     * @generated
     */
    EAttribute getIteratorDefinitionLTType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType#getIteratorElement
     * <em>Iterator Element</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Iterator Element</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorDefinitionLTType#getIteratorElement()
     * @see #getIteratorDefinitionLTType()
     * @generated
     */
    EReference getIteratorDefinitionLTType_IteratorElement();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.IteratorElementType
     * <em>Iterator Element Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Iterator Element Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorElementType
     * @generated
     */
    EClass getIteratorElementType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorElementType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorElementType#getMixed()
     * @see #getIteratorElementType()
     * @generated
     */
    EAttribute getIteratorElementType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorElementType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorElementType#getGroup()
     * @see #getIteratorElementType()
     * @generated
     */
    EAttribute getIteratorElementType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorElementType#getVN <em>VN</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>VN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorElementType#getVN()
     * @see #getIteratorElementType()
     * @generated
     */
    EReference getIteratorElementType_VN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorElementType#getLiteralE <em>Literal E</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorElementType#getLiteralE()
     * @see #getIteratorElementType()
     * @generated
     */
    EReference getIteratorElementType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorElementType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorElementType#getNamedE()
     * @see #getIteratorElementType()
     * @generated
     */
    EReference getIteratorElementType_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.IteratorType
     * <em>Iterator Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Iterator Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorType
     * @generated
     */
    EClass getIteratorType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorType#getMixed()
     * @see #getIteratorType()
     * @generated
     */
    EAttribute getIteratorType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.IteratorType#getIteratorDefinitionLT <em>Iterator
     * Definition LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Iterator Definition LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.IteratorType#getIteratorDefinitionLT()
     * @see #getIteratorType()
     * @generated
     */
    EReference getIteratorType_IteratorDefinitionLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.KSelectorType
     * <em>KSelector Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>KSelector Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSelectorType
     * @generated
     */
    EClass getKSelectorType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.KSelectorType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSelectorType#getMixed()
     * @see #getKSelectorType()
     * @generated
     */
    EAttribute getKSelectorType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.KSelectorType#getKSpec <em>KSpec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>KSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSelectorType#getKSpec()
     * @see #getKSelectorType()
     * @generated
     */
    EReference getKSelectorType_KSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.KSpecType <em>KSpec
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>KSpec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType
     * @generated
     */
    EClass getKSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.KSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType#getMixed()
     * @see #getKSpecType()
     * @generated
     */
    EAttribute getKSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.KSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType#getGroup()
     * @see #getKSpecType()
     * @generated
     */
    EAttribute getKSpecType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.KSpecType#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType#getN()
     * @see #getKSpecType()
     * @generated
     */
    EReference getKSpecType_N();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.KSpecType#getL <em>L</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>L</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType#getL()
     * @see #getKSpecType()
     * @generated
     */
    EAttribute getKSpecType_L();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.KSpecType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType#getLiteralE()
     * @see #getKSpecType()
     * @generated
     */
    EReference getKSpecType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.KSpecType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.KSpecType#getNamedE()
     * @see #getKSpecType()
     * @generated
     */
    EReference getKSpecType_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.LabelType <em>Label
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Label Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LabelType
     * @generated
     */
    EClass getLabelType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.LabelType#getError <em>Error</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Error</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LabelType#getError()
     * @see #getLabelType()
     * @generated
     */
    EReference getLabelType_Error();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.LiteralEType
     * <em>Literal EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Literal EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LiteralEType
     * @generated
     */
    EClass getLiteralEType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.LiteralEType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LiteralEType#getMixed()
     * @see #getLiteralEType()
     * @generated
     */
    EAttribute getLiteralEType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.LiteralEType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LiteralEType#getGroup()
     * @see #getLiteralEType()
     * @generated
     */
    EAttribute getLiteralEType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.LiteralEType#getKSpec <em>KSpec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>KSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LiteralEType#getKSpec()
     * @see #getLiteralEType()
     * @generated
     */
    EReference getLiteralEType_KSpec();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.LiteralEType#getL <em>L</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>L</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LiteralEType#getL()
     * @see #getLiteralEType()
     * @generated
     */
    EAttribute getLiteralEType_L();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.LowerBoundType
     * <em>Lower Bound Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Lower Bound Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LowerBoundType
     * @generated
     */
    EClass getLowerBoundType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.LowerBoundType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LowerBoundType#getLiteralE()
     * @see #getLowerBoundType()
     * @generated
     */
    EReference getLowerBoundType_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.LowerBoundType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LowerBoundType#getNamedE()
     * @see #getLowerBoundType()
     * @generated
     */
    EReference getLowerBoundType_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.LowerBoundType#getOpE <em>Op E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.LowerBoundType#getOpE()
     * @see #getLowerBoundType()
     * @generated
     */
    EReference getLowerBoundType_OpE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.MaskEType <em>Mask
     * EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Mask EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.MaskEType
     * @generated
     */
    EClass getMaskEType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.MaskEType#getOpE <em>Op E</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.MaskEType#getOpE()
     * @see #getMaskEType()
     * @generated
     */
    EReference getMaskEType_OpE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ModuleNType
     * <em>Module NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Module NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleNType
     * @generated
     */
    EClass getModuleNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleNType#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleNType#getN()
     * @see #getModuleNType()
     * @generated
     */
    EReference getModuleNType_N();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType <em>Module Procedure NLT
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Module Procedure NLT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType
     * @generated
     */
    EClass getModuleProcedureNLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType#getMixed()
     * @see #getModuleProcedureNLTType()
     * @generated
     */
    EAttribute getModuleProcedureNLTType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleProcedureNLTType#getN()
     * @see #getModuleProcedureNLTType()
     * @generated
     */
    EReference getModuleProcedureNLTType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ModuleStmtType
     * <em>Module Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Module Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleStmtType
     * @generated
     */
    EClass getModuleStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleStmtType#getMixed()
     * @see #getModuleStmtType()
     * @generated
     */
    EAttribute getModuleStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ModuleStmtType#getModuleN <em>Module N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Module N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ModuleStmtType#getModuleN()
     * @see #getModuleStmtType()
     * @generated
     */
    EReference getModuleStmtType_ModuleN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.NamedEType <em>Named
     * EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Named EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamedEType
     * @generated
     */
    EClass getNamedEType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamedEType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamedEType#getGroup()
     * @see #getNamedEType()
     * @generated
     */
    EAttribute getNamedEType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NamedEType#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamedEType#getN()
     * @see #getNamedEType()
     * @generated
     */
    EReference getNamedEType_N();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NamedEType#getRLT <em>RLT</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>RLT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamedEType#getRLT()
     * @see #getNamedEType()
     * @generated
     */
    EReference getNamedEType_RLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupNType
     * <em>Namelist Group NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Namelist Group NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupNType
     * @generated
     */
    EClass getNamelistGroupNType();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupNType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupNType#getN()
     * @see #getNamelistGroupNType()
     * @generated
     */
    EAttribute getNamelistGroupNType_N();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType <em>Namelist Group Obj LT
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Namelist Group Obj LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType
     * @generated
     */
    EClass getNamelistGroupObjLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getMixed()
     * @see #getNamelistGroupObjLTType()
     * @generated
     */
    EAttribute getNamelistGroupObjLTType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getGroup()
     * @see #getNamelistGroupObjLTType()
     * @generated
     */
    EAttribute getNamelistGroupObjLTType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getC <em>C</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>C</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getC()
     * @see #getNamelistGroupObjLTType()
     * @generated
     */
    EAttribute getNamelistGroupObjLTType_C();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getCnt()
     * @see #getNamelistGroupObjLTType()
     * @generated
     */
    EAttribute getNamelistGroupObjLTType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getNamelistGroupObj <em>Namelist
     * Group Obj</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Namelist Group Obj</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjLTType#getNamelistGroupObj()
     * @see #getNamelistGroupObjLTType()
     * @generated
     */
    EReference getNamelistGroupObjLTType_NamelistGroupObj();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType
     * <em>Namelist Group Obj NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Namelist Group Obj NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType
     * @generated
     */
    EClass getNamelistGroupObjNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjNType#getN()
     * @see #getNamelistGroupObjNType()
     * @generated
     */
    EReference getNamelistGroupObjNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjType
     * <em>Namelist Group Obj Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Namelist Group Obj Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjType
     * @generated
     */
    EClass getNamelistGroupObjType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistGroupObjType#getNamelistGroupObjN <em>Namelist
     * Group Obj N</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Namelist Group Obj N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistGroupObjType#getNamelistGroupObjN()
     * @see #getNamelistGroupObjType()
     * @generated
     */
    EReference getNamelistGroupObjType_NamelistGroupObjN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.NamelistStmtType
     * <em>Namelist Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Namelist Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistStmtType
     * @generated
     */
    EClass getNamelistStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistStmtType#getMixed()
     * @see #getNamelistStmtType()
     * @generated
     */
    EAttribute getNamelistStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistStmtType#getGroup()
     * @see #getNamelistStmtType()
     * @generated
     */
    EAttribute getNamelistStmtType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistStmtType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistStmtType#getCnt()
     * @see #getNamelistStmtType()
     * @generated
     */
    EAttribute getNamelistStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistStmtType#getNamelistGroupN <em>Namelist Group
     * N</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Namelist Group N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistStmtType#getNamelistGroupN()
     * @see #getNamelistStmtType()
     * @generated
     */
    EReference getNamelistStmtType_NamelistGroupN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NamelistStmtType#getNamelistGroupObjLT <em>Namelist
     * Group Obj LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Namelist Group Obj LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NamelistStmtType#getNamelistGroupObjLT()
     * @see #getNamelistStmtType()
     * @generated
     */
    EReference getNamelistStmtType_NamelistGroupObjLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.NType
     * <em>NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NType
     * @generated
     */
    EClass getNType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NType#getMixed <em>Mixed</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NType#getMixed()
     * @see #getNType()
     * @generated
     */
    EAttribute getNType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NType#getGroup <em>Group</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NType#getGroup()
     * @see #getNType()
     * @generated
     */
    EAttribute getNType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NType#getN <em>N</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NType#getN()
     * @see #getNType()
     * @generated
     */
    EReference getNType_N();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NType#getN1 <em>N1</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>N1</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NType#getN1()
     * @see #getNType()
     * @generated
     */
    EAttribute getNType_N1();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NType#getOp <em>Op</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Op</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NType#getOp()
     * @see #getNType()
     * @generated
     */
    EReference getNType_Op();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.NullifyStmtType
     * <em>Nullify Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Nullify Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NullifyStmtType
     * @generated
     */
    EClass getNullifyStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.NullifyStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NullifyStmtType#getMixed()
     * @see #getNullifyStmtType()
     * @generated
     */
    EAttribute getNullifyStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.NullifyStmtType#getArgSpec <em>Arg Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.NullifyStmtType#getArgSpec()
     * @see #getNullifyStmtType()
     * @generated
     */
    EReference getNullifyStmtType_ArgSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ObjectType <em>Object
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Object Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ObjectType
     * @generated
     */
    EClass getObjectType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getFile <em>File</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>File</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ObjectType#getFile()
     * @see #getObjectType()
     * @generated
     */
    EReference getObjectType_File();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getOpenacc <em>Openacc</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Openacc</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ObjectType#getOpenacc()
     * @see #getObjectType()
     * @generated
     */
    EAttribute getObjectType_Openacc();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getOpenmp <em>Openmp</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Openmp</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ObjectType#getOpenmp()
     * @see #getObjectType()
     * @generated
     */
    EAttribute getObjectType_Openmp();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getSourceForm <em>Source Form</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Source Form</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ObjectType#getSourceForm()
     * @see #getObjectType()
     * @generated
     */
    EAttribute getObjectType_SourceForm();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.ObjectType#getSourceWidth <em>Source Width</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>Source Width</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ObjectType#getSourceWidth()
     * @see #getObjectType()
     * @generated
     */
    EAttribute getObjectType_SourceWidth();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.OpenStmtType <em>Open
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Open Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpenStmtType
     * @generated
     */
    EClass getOpenStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.OpenStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpenStmtType#getMixed()
     * @see #getOpenStmtType()
     * @generated
     */
    EAttribute getOpenStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.OpenStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpenStmtType#getGroup()
     * @see #getOpenStmtType()
     * @generated
     */
    EAttribute getOpenStmtType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.OpenStmtType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpenStmtType#getCnt()
     * @see #getOpenStmtType()
     * @generated
     */
    EAttribute getOpenStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OpenStmtType#getConnectSpecSpec <em>Connect Spec
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Connect Spec Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpenStmtType#getConnectSpecSpec()
     * @see #getOpenStmtType()
     * @generated
     */
    EReference getOpenStmtType_ConnectSpecSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.OpEType <em>Op
     * EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Op EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType
     * @generated
     */
    EClass getOpEType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getGroup <em>Group</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getGroup()
     * @see #getOpEType()
     * @generated
     */
    EAttribute getOpEType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getCnt()
     * @see #getOpEType()
     * @generated
     */
    EAttribute getOpEType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getLiteralE()
     * @see #getOpEType()
     * @generated
     */
    EReference getOpEType_LiteralE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getNamedE()
     * @see #getOpEType()
     * @generated
     */
    EReference getOpEType_NamedE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getOp <em>Op</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Op</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getOp()
     * @see #getOpEType()
     * @generated
     */
    EReference getOpEType_Op();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getOpE <em>Op E</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getOpE()
     * @see #getOpEType()
     * @generated
     */
    EReference getOpEType_OpE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getParensE <em>Parens E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Parens E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getParensE()
     * @see #getOpEType()
     * @generated
     */
    EReference getOpEType_ParensE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OpEType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpEType#getStringE()
     * @see #getOpEType()
     * @generated
     */
    EReference getOpEType_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.OpType <em>Op
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Op Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpType
     * @generated
     */
    EClass getOpType();

    /**
     * Returns the meta object for the attribute '{@link org.oceandsl.tools.sar.fxtran.OpType#getO
     * <em>O</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>O</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OpType#getO()
     * @see #getOpType()
     * @generated
     */
    EAttribute getOpType_O();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.OutputItemLTType
     * <em>Output Item LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Output Item LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemLTType
     * @generated
     */
    EClass getOutputItemLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemLTType#getMixed()
     * @see #getOutputItemLTType()
     * @generated
     */
    EAttribute getOutputItemLTType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemLTType#getOutputItem <em>Output Item</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Output Item</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemLTType#getOutputItem()
     * @see #getOutputItemLTType()
     * @generated
     */
    EReference getOutputItemLTType_OutputItem();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.OutputItemType
     * <em>Output Item Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Output Item Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemType
     * @generated
     */
    EClass getOutputItemType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemType#getLiteralE()
     * @see #getOutputItemType()
     * @generated
     */
    EReference getOutputItemType_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemType#getNamedE()
     * @see #getOutputItemType()
     * @generated
     */
    EReference getOutputItemType_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemType#getOpE <em>Op E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemType#getOpE()
     * @see #getOutputItemType()
     * @generated
     */
    EReference getOutputItemType_OpE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.OutputItemType#getStringE <em>String E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>String E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.OutputItemType#getStringE()
     * @see #getOutputItemType()
     * @generated
     */
    EReference getOutputItemType_StringE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ParensEType
     * <em>Parens EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Parens EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensEType
     * @generated
     */
    EClass getParensEType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensEType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensEType#getMixed()
     * @see #getParensEType()
     * @generated
     */
    EAttribute getParensEType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensEType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensEType#getGroup()
     * @see #getParensEType()
     * @generated
     */
    EAttribute getParensEType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensEType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensEType#getCnt()
     * @see #getParensEType()
     * @generated
     */
    EAttribute getParensEType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensEType#getOpE <em>Op E</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensEType#getOpE()
     * @see #getParensEType()
     * @generated
     */
    EReference getParensEType_OpE();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensEType#getIterator <em>Iterator</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Iterator</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensEType#getIterator()
     * @see #getParensEType()
     * @generated
     */
    EReference getParensEType_Iterator();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ParensRType
     * <em>Parens RType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Parens RType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensRType
     * @generated
     */
    EClass getParensRType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensRType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensRType#getMixed()
     * @see #getParensRType()
     * @generated
     */
    EAttribute getParensRType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensRType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensRType#getGroup()
     * @see #getParensRType()
     * @generated
     */
    EAttribute getParensRType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensRType#getArgSpec <em>Arg Spec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Arg Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensRType#getArgSpec()
     * @see #getParensRType()
     * @generated
     */
    EReference getParensRType_ArgSpec();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensRType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensRType#getCnt()
     * @see #getParensRType()
     * @generated
     */
    EAttribute getParensRType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ParensRType#getElementLT <em>Element LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Element LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ParensRType#getElementLT()
     * @see #getParensRType()
     * @generated
     */
    EReference getParensRType_ElementLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType
     * <em>Pointer AStmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Pointer AStmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PointerAStmtType
     * @generated
     */
    EClass getPointerAStmtType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE1 <em>E1</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>E1</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE1()
     * @see #getPointerAStmtType()
     * @generated
     */
    EReference getPointerAStmtType_E1();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getA <em>A</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>A</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PointerAStmtType#getA()
     * @see #getPointerAStmtType()
     * @generated
     */
    EAttribute getPointerAStmtType_A();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE2 <em>E2</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>E2</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PointerAStmtType#getE2()
     * @see #getPointerAStmtType()
     * @generated
     */
    EReference getPointerAStmtType_E2();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.PointerStmtType
     * <em>Pointer Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Pointer Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PointerStmtType
     * @generated
     */
    EClass getPointerStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.PointerStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PointerStmtType#getMixed()
     * @see #getPointerStmtType()
     * @generated
     */
    EAttribute getPointerStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.PointerStmtType#getENDeclLT <em>EN Decl LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>EN Decl LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PointerStmtType#getENDeclLT()
     * @see #getPointerStmtType()
     * @generated
     */
    EReference getPointerStmtType_ENDeclLT();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType <em>Procedure Designator
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Procedure Designator Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType
     * @generated
     */
    EClass getProcedureDesignatorType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType#getNamedE <em>Named E</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProcedureDesignatorType#getNamedE()
     * @see #getProcedureDesignatorType()
     * @generated
     */
    EReference getProcedureDesignatorType_NamedE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ProcedureStmtType
     * <em>Procedure Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Procedure Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProcedureStmtType
     * @generated
     */
    EClass getProcedureStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ProcedureStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProcedureStmtType#getMixed()
     * @see #getProcedureStmtType()
     * @generated
     */
    EAttribute getProcedureStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ProcedureStmtType#getModuleProcedureNLT <em>Module
     * Procedure NLT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Module Procedure NLT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProcedureStmtType#getModuleProcedureNLT()
     * @see #getProcedureStmtType()
     * @generated
     */
    EReference getProcedureStmtType_ModuleProcedureNLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ProgramNType
     * <em>Program NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Program NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProgramNType
     * @generated
     */
    EClass getProgramNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.ProgramNType#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProgramNType#getN()
     * @see #getProgramNType()
     * @generated
     */
    EReference getProgramNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ProgramStmtType
     * <em>Program Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Program Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProgramStmtType
     * @generated
     */
    EClass getProgramStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ProgramStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProgramStmtType#getMixed()
     * @see #getProgramStmtType()
     * @generated
     */
    EAttribute getProgramStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ProgramStmtType#getProgramN <em>Program N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Program N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ProgramStmtType#getProgramN()
     * @see #getProgramStmtType()
     * @generated
     */
    EReference getProgramStmtType_ProgramN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.PublicStmtType
     * <em>Public Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Public Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PublicStmtType
     * @generated
     */
    EClass getPublicStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.PublicStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PublicStmtType#getMixed()
     * @see #getPublicStmtType()
     * @generated
     */
    EAttribute getPublicStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.PublicStmtType#getENLT <em>ENLT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>ENLT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.PublicStmtType#getENLT()
     * @see #getPublicStmtType()
     * @generated
     */
    EReference getPublicStmtType_ENLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ReadStmtType <em>Read
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Read Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ReadStmtType
     * @generated
     */
    EClass getReadStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ReadStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ReadStmtType#getMixed()
     * @see #getReadStmtType()
     * @generated
     */
    EAttribute getReadStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ReadStmtType#getIoControlSpec <em>Io Control
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Io Control Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ReadStmtType#getIoControlSpec()
     * @see #getReadStmtType()
     * @generated
     */
    EReference getReadStmtType_IoControlSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.RenameLTType
     * <em>Rename LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Rename LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RenameLTType
     * @generated
     */
    EClass getRenameLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.RenameLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RenameLTType#getMixed()
     * @see #getRenameLTType()
     * @generated
     */
    EAttribute getRenameLTType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.RenameLTType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RenameLTType#getGroup()
     * @see #getRenameLTType()
     * @generated
     */
    EAttribute getRenameLTType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.RenameLTType#getCnt <em>Cnt</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RenameLTType#getCnt()
     * @see #getRenameLTType()
     * @generated
     */
    EAttribute getRenameLTType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.RenameLTType#getRename <em>Rename</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Rename</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RenameLTType#getRename()
     * @see #getRenameLTType()
     * @generated
     */
    EReference getRenameLTType_Rename();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.RenameType <em>Rename
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Rename Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RenameType
     * @generated
     */
    EClass getRenameType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.RenameType#getUseN <em>Use N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Use N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RenameType#getUseN()
     * @see #getRenameType()
     * @generated
     */
    EReference getRenameType_UseN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ResultSpecType
     * <em>Result Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Result Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ResultSpecType
     * @generated
     */
    EClass getResultSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ResultSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ResultSpecType#getMixed()
     * @see #getResultSpecType()
     * @generated
     */
    EAttribute getResultSpecType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ResultSpecType#getN <em>N</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ResultSpecType#getN()
     * @see #getResultSpecType()
     * @generated
     */
    EReference getResultSpecType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.RLTType <em>RLT
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>RLT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RLTType
     * @generated
     */
    EClass getRLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.RLTType#getGroup <em>Group</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RLTType#getGroup()
     * @see #getRLTType()
     * @generated
     */
    EAttribute getRLTType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.RLTType#getArrayR <em>Array R</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Array R</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RLTType#getArrayR()
     * @see #getRLTType()
     * @generated
     */
    EReference getRLTType_ArrayR();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.RLTType#getComponentR <em>Component R</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Component R</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RLTType#getComponentR()
     * @see #getRLTType()
     * @generated
     */
    EReference getRLTType_ComponentR();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.RLTType#getParensR <em>Parens R</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Parens R</em>'.
     * @see org.oceandsl.tools.sar.fxtran.RLTType#getParensR()
     * @see #getRLTType()
     * @generated
     */
    EReference getRLTType_ParensR();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType <em>Section Subscript LT
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Section Subscript LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType
     * @generated
     */
    EClass getSectionSubscriptLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType#getMixed()
     * @see #getSectionSubscriptLTType()
     * @generated
     */
    EAttribute getSectionSubscriptLTType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType#getSectionSubscript <em>Section
     * Subscript</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Section Subscript</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptLTType#getSectionSubscript()
     * @see #getSectionSubscriptLTType()
     * @generated
     */
    EReference getSectionSubscriptLTType_SectionSubscript();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType
     * <em>Section Subscript Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Section Subscript Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptType
     * @generated
     */
    EClass getSectionSubscriptType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getMixed()
     * @see #getSectionSubscriptType()
     * @generated
     */
    EAttribute getSectionSubscriptType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getGroup()
     * @see #getSectionSubscriptType()
     * @generated
     */
    EAttribute getSectionSubscriptType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getLowerBound <em>Lower
     * Bound</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Lower Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getLowerBound()
     * @see #getSectionSubscriptType()
     * @generated
     */
    EReference getSectionSubscriptType_LowerBound();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getUpperBound <em>Upper
     * Bound</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Upper Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SectionSubscriptType#getUpperBound()
     * @see #getSectionSubscriptType()
     * @generated
     */
    EReference getSectionSubscriptType_UpperBound();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.SelectCaseStmtType
     * <em>Select Case Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Select Case Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SelectCaseStmtType
     * @generated
     */
    EClass getSelectCaseStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getMixed()
     * @see #getSelectCaseStmtType()
     * @generated
     */
    EAttribute getSelectCaseStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getGroup()
     * @see #getSelectCaseStmtType()
     * @generated
     */
    EAttribute getSelectCaseStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getN <em>N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getN()
     * @see #getSelectCaseStmtType()
     * @generated
     */
    EReference getSelectCaseStmtType_N();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getCaseE <em>Case E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Case E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SelectCaseStmtType#getCaseE()
     * @see #getSelectCaseStmtType()
     * @generated
     */
    EReference getSelectCaseStmtType_CaseE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecLTType
     * <em>Shape Spec LT Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Shape Spec LT Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecLTType
     * @generated
     */
    EClass getShapeSpecLTType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecLTType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecLTType#getMixed()
     * @see #getShapeSpecLTType()
     * @generated
     */
    EAttribute getShapeSpecLTType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecLTType#getShapeSpec <em>Shape Spec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Shape Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecLTType#getShapeSpec()
     * @see #getShapeSpecLTType()
     * @generated
     */
    EReference getShapeSpecLTType_ShapeSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecType
     * <em>Shape Spec Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Shape Spec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecType
     * @generated
     */
    EClass getShapeSpecType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecType#getMixed()
     * @see #getShapeSpecType()
     * @generated
     */
    EAttribute getShapeSpecType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecType#getGroup()
     * @see #getShapeSpecType()
     * @generated
     */
    EAttribute getShapeSpecType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecType#getLowerBound <em>Lower Bound</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Lower Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecType#getLowerBound()
     * @see #getShapeSpecType()
     * @generated
     */
    EReference getShapeSpecType_LowerBound();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.ShapeSpecType#getUpperBound <em>Upper Bound</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Upper Bound</em>'.
     * @see org.oceandsl.tools.sar.fxtran.ShapeSpecType#getUpperBound()
     * @see #getShapeSpecType()
     * @generated
     */
    EReference getShapeSpecType_UpperBound();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.StopStmtType <em>Stop
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Stop Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.StopStmtType
     * @generated
     */
    EClass getStopStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.StopStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.StopStmtType#getMixed()
     * @see #getStopStmtType()
     * @generated
     */
    EAttribute getStopStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.StopStmtType#getStopCode <em>Stop Code</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Stop Code</em>'.
     * @see org.oceandsl.tools.sar.fxtran.StopStmtType#getStopCode()
     * @see #getStopStmtType()
     * @generated
     */
    EAttribute getStopStmtType_StopCode();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.StringEType
     * <em>String EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>String EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.StringEType
     * @generated
     */
    EClass getStringEType();

    /**
     * Returns the meta object for the attribute
     * '{@link org.oceandsl.tools.sar.fxtran.StringEType#getS <em>S</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>S</em>'.
     * @see org.oceandsl.tools.sar.fxtran.StringEType#getS()
     * @see #getStringEType()
     * @generated
     */
    EAttribute getStringEType_S();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.SubroutineNType
     * <em>Subroutine NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Subroutine NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineNType
     * @generated
     */
    EClass getSubroutineNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineNType#getN <em>N</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineNType#getN()
     * @see #getSubroutineNType()
     * @generated
     */
    EReference getSubroutineNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType
     * <em>Subroutine Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Subroutine Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType
     * @generated
     */
    EClass getSubroutineStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getMixed()
     * @see #getSubroutineStmtType()
     * @generated
     */
    EAttribute getSubroutineStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getGroup()
     * @see #getSubroutineStmtType()
     * @generated
     */
    EAttribute getSubroutineStmtType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getCnt()
     * @see #getSubroutineStmtType()
     * @generated
     */
    EAttribute getSubroutineStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getDummyArgLT <em>Dummy Arg
     * LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Dummy Arg LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getDummyArgLT()
     * @see #getSubroutineStmtType()
     * @generated
     */
    EReference getSubroutineStmtType_DummyArgLT();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getPrefix <em>Prefix</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Prefix</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getPrefix()
     * @see #getSubroutineStmtType()
     * @generated
     */
    EAttribute getSubroutineStmtType_Prefix();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getSubroutineN <em>Subroutine
     * N</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Subroutine N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.SubroutineStmtType#getSubroutineN()
     * @see #getSubroutineStmtType()
     * @generated
     */
    EReference getSubroutineStmtType_SubroutineN();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.TDeclStmtType
     * <em>TDecl Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>TDecl Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TDeclStmtType
     * @generated
     */
    EClass getTDeclStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.TDeclStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TDeclStmtType#getMixed()
     * @see #getTDeclStmtType()
     * @generated
     */
    EAttribute getTDeclStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.TDeclStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TDeclStmtType#getGroup()
     * @see #getTDeclStmtType()
     * @generated
     */
    EAttribute getTDeclStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.TDeclStmtType#getENDeclLT <em>EN Decl LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>EN Decl LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TDeclStmtType#getENDeclLT()
     * @see #getTDeclStmtType()
     * @generated
     */
    EReference getTDeclStmtType_ENDeclLT();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.TDeclStmtType#getTSpec <em>TSpec</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TDeclStmtType#getTSpec()
     * @see #getTDeclStmtType()
     * @generated
     */
    EReference getTDeclStmtType_TSpec();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.TDeclStmtType#getAttribute <em>Attribute</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Attribute</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TDeclStmtType#getAttribute()
     * @see #getTDeclStmtType()
     * @generated
     */
    EReference getTDeclStmtType_Attribute();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.TestEType <em>Test
     * EType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Test EType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TestEType
     * @generated
     */
    EClass getTestEType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.TestEType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TestEType#getNamedE()
     * @see #getTestEType()
     * @generated
     */
    EReference getTestEType_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.TestEType#getOpE <em>Op E</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TestEType#getOpE()
     * @see #getTestEType()
     * @generated
     */
    EReference getTestEType_OpE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.TNType <em>TN
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>TN Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TNType
     * @generated
     */
    EClass getTNType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.TNType#getMixed <em>Mixed</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TNType#getMixed()
     * @see #getTNType()
     * @generated
     */
    EAttribute getTNType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.TNType#getN <em>N</em>}'. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TNType#getN()
     * @see #getTNType()
     * @generated
     */
    EReference getTNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.TSpecType <em>TSpec
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>TSpec Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TSpecType
     * @generated
     */
    EClass getTSpecType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.TSpecType#getDerivedTSpec <em>Derived TSpec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Derived TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TSpecType#getDerivedTSpec()
     * @see #getTSpecType()
     * @generated
     */
    EReference getTSpecType_DerivedTSpec();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.TSpecType#getIntrinsicTSpec <em>Intrinsic TSpec</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Intrinsic TSpec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TSpecType#getIntrinsicTSpec()
     * @see #getTSpecType()
     * @generated
     */
    EReference getTSpecType_IntrinsicTSpec();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.TStmtType <em>TStmt
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>TStmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TStmtType
     * @generated
     */
    EClass getTStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.TStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TStmtType#getMixed()
     * @see #getTStmtType()
     * @generated
     */
    EAttribute getTStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.TStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TStmtType#getGroup()
     * @see #getTStmtType()
     * @generated
     */
    EAttribute getTStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.TStmtType#getTN <em>TN</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>TN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TStmtType#getTN()
     * @see #getTStmtType()
     * @generated
     */
    EReference getTStmtType_TN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.TStmtType#getAttribute <em>Attribute</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Attribute</em>'.
     * @see org.oceandsl.tools.sar.fxtran.TStmtType#getAttribute()
     * @see #getTStmtType()
     * @generated
     */
    EReference getTStmtType_Attribute();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.UpperBoundType
     * <em>Upper Bound Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Upper Bound Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UpperBoundType
     * @generated
     */
    EClass getUpperBoundType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.UpperBoundType#getLiteralE <em>Literal E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Literal E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UpperBoundType#getLiteralE()
     * @see #getUpperBoundType()
     * @generated
     */
    EReference getUpperBoundType_LiteralE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.UpperBoundType#getNamedE <em>Named E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UpperBoundType#getNamedE()
     * @see #getUpperBoundType()
     * @generated
     */
    EReference getUpperBoundType_NamedE();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.UpperBoundType#getOpE <em>Op E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Op E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UpperBoundType#getOpE()
     * @see #getUpperBoundType()
     * @generated
     */
    EReference getUpperBoundType_OpE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.UseNType <em>Use
     * NType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Use NType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UseNType
     * @generated
     */
    EClass getUseNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.UseNType#getN <em>N</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UseNType#getN()
     * @see #getUseNType()
     * @generated
     */
    EReference getUseNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.UseStmtType <em>Use
     * Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Use Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UseStmtType
     * @generated
     */
    EClass getUseStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.UseStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UseStmtType#getMixed()
     * @see #getUseStmtType()
     * @generated
     */
    EAttribute getUseStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.UseStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UseStmtType#getGroup()
     * @see #getUseStmtType()
     * @generated
     */
    EAttribute getUseStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.UseStmtType#getModuleN <em>Module N</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Module N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UseStmtType#getModuleN()
     * @see #getUseStmtType()
     * @generated
     */
    EReference getUseStmtType_ModuleN();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.UseStmtType#getRenameLT <em>Rename LT</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Rename LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.UseStmtType#getRenameLT()
     * @see #getUseStmtType()
     * @generated
     */
    EReference getUseStmtType_RenameLT();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.VNType <em>VN
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>VN Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.VNType
     * @generated
     */
    EClass getVNType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.VNType#getVN <em>VN</em>}'. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>VN</em>'.
     * @see org.oceandsl.tools.sar.fxtran.VNType#getVN()
     * @see #getVNType()
     * @generated
     */
    EReference getVNType_VN();

    /**
     * Returns the meta object for the attribute '{@link org.oceandsl.tools.sar.fxtran.VNType#getN
     * <em>N</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute '<em>N</em>'.
     * @see org.oceandsl.tools.sar.fxtran.VNType#getN()
     * @see #getVNType()
     * @generated
     */
    EAttribute getVNType_N();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.VType
     * <em>VType</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>VType</em>'.
     * @see org.oceandsl.tools.sar.fxtran.VType
     * @generated
     */
    EClass getVType();

    /**
     * Returns the meta object for the containment reference
     * '{@link org.oceandsl.tools.sar.fxtran.VType#getNamedE <em>Named E</em>}'. <!-- begin-user-doc
     * --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference '<em>Named E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.VType#getNamedE()
     * @see #getVType()
     * @generated
     */
    EReference getVType_NamedE();

    /**
     * Returns the meta object for class
     * '{@link org.oceandsl.tools.sar.fxtran.WhereConstructStmtType <em>Where Construct Stmt
     * Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Where Construct Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereConstructStmtType
     * @generated
     */
    EClass getWhereConstructStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.WhereConstructStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereConstructStmtType#getMixed()
     * @see #getWhereConstructStmtType()
     * @generated
     */
    EAttribute getWhereConstructStmtType_Mixed();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.WhereConstructStmtType#getMaskE <em>Mask E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Mask E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereConstructStmtType#getMaskE()
     * @see #getWhereConstructStmtType()
     * @generated
     */
    EReference getWhereConstructStmtType_MaskE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.WhereStmtType
     * <em>Where Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Where Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereStmtType
     * @generated
     */
    EClass getWhereStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.WhereStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereStmtType#getMixed()
     * @see #getWhereStmtType()
     * @generated
     */
    EAttribute getWhereStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.WhereStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereStmtType#getGroup()
     * @see #getWhereStmtType()
     * @generated
     */
    EAttribute getWhereStmtType_Group();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.WhereStmtType#getActionStmt <em>Action Stmt</em>}'.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Action Stmt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereStmtType#getActionStmt()
     * @see #getWhereStmtType()
     * @generated
     */
    EReference getWhereStmtType_ActionStmt();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.WhereStmtType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereStmtType#getCnt()
     * @see #getWhereStmtType()
     * @generated
     */
    EAttribute getWhereStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.WhereStmtType#getMaskE <em>Mask E</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Mask E</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WhereStmtType#getMaskE()
     * @see #getWhereStmtType()
     * @generated
     */
    EReference getWhereStmtType_MaskE();

    /**
     * Returns the meta object for class '{@link org.oceandsl.tools.sar.fxtran.WriteStmtType
     * <em>Write Stmt Type</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for class '<em>Write Stmt Type</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WriteStmtType
     * @generated
     */
    EClass getWriteStmtType();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.WriteStmtType#getMixed <em>Mixed</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Mixed</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WriteStmtType#getMixed()
     * @see #getWriteStmtType()
     * @generated
     */
    EAttribute getWriteStmtType_Mixed();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.WriteStmtType#getGroup <em>Group</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Group</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WriteStmtType#getGroup()
     * @see #getWriteStmtType()
     * @generated
     */
    EAttribute getWriteStmtType_Group();

    /**
     * Returns the meta object for the attribute list
     * '{@link org.oceandsl.tools.sar.fxtran.WriteStmtType#getCnt <em>Cnt</em>}'. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the attribute list '<em>Cnt</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WriteStmtType#getCnt()
     * @see #getWriteStmtType()
     * @generated
     */
    EAttribute getWriteStmtType_Cnt();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.WriteStmtType#getIoControlSpec <em>Io Control
     * Spec</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Io Control Spec</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WriteStmtType#getIoControlSpec()
     * @see #getWriteStmtType()
     * @generated
     */
    EReference getWriteStmtType_IoControlSpec();

    /**
     * Returns the meta object for the containment reference list
     * '{@link org.oceandsl.tools.sar.fxtran.WriteStmtType#getOutputItemLT <em>Output Item
     * LT</em>}'. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @return the meta object for the containment reference list '<em>Output Item LT</em>'.
     * @see org.oceandsl.tools.sar.fxtran.WriteStmtType#getOutputItemLT()
     * @see #getWriteStmtType()
     * @generated
     */
    EReference getWriteStmtType_OutputItemLT();

    /**
     * Returns the factory that creates the instances of the model. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     *
     * @return the factory that creates the instances of the model.
     * @generated
     */
    FxtranFactory getFxtranFactory();

} // FxtranPackage
