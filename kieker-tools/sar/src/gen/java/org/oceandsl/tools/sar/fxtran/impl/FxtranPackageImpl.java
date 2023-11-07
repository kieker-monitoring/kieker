/**
 */
package org.oceandsl.tools.sar.fxtran.impl;

import java.io.IOException;
import java.net.URL;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.emf.ecore.xml.type.XMLTypePackage;

import org.oceandsl.tools.sar.fxtran.FxtranFactory;
import org.oceandsl.tools.sar.fxtran.FxtranPackage;

/**
 * <!-- begin-user-doc --> An implementation of the model <b>Package</b>. <!-- end-user-doc -->
 *
 * @generated
 */
public class FxtranPackageImpl extends EPackageImpl implements FxtranPackage {
    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    protected String packageFilename = "fxtran.ecore";

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass actionStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass acValueLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass acValueTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass allocateStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass argNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass argSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass argTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass arrayConstructorETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass arrayRTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass arraySpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass aStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass attributeTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass callStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseSelectorTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseValueRangeLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseValueRangeTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass caseValueTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass charSelectorTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass charSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass closeSpecSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass closeSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass closeStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass componentDeclStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass componentRTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass conditionETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectSpecSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass connectSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass cycleStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass deallocateStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass derivedTSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass documentRootEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass doStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass doVTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass dummyArgLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass e1TypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass e2TypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass elementLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass elementTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass elseIfStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endDoStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass enDeclLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass enDeclTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endForallStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endFunctionStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endInterfaceStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endModuleStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endProgramStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endSelectCaseStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endSubroutineStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass endTStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass enltTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass ennTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass enTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass errorTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass fileTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass forallConstructStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass forallStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass forallTripletSpecLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass forallTripletSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass functionNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass functionStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass ifStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass ifThenStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass initETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass inquireStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass inquirySpecSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass inquirySpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass interfaceStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass intrinsicTSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass ioControlSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass ioControlTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass iteratorDefinitionLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass iteratorElementTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass iteratorTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass kSelectorTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass kSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass labelTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass literalETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass lowerBoundTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass maskETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass moduleNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass moduleProcedureNLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass moduleStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namedETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namelistGroupNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namelistGroupObjLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namelistGroupObjNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namelistGroupObjTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass namelistStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass nTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass nullifyStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass objectTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass openStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass opETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass opTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass outputItemLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass outputItemTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass parensETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass parensRTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass pointerAStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass pointerStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass procedureDesignatorTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass procedureStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass programNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass programStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass publicStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass readStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass renameLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass renameTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass resultSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass rltTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass sectionSubscriptLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass sectionSubscriptTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass selectCaseStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass shapeSpecLTTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass shapeSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stopStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass stringETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass subroutineNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass subroutineStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass tDeclStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass testETypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass tnTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass tSpecTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass tStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass upperBoundTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass useNTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass useStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass vnTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass vTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass whereConstructStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass whereStmtTypeEClass = null;

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private EClass writeStmtTypeEClass = null;

    /**
     * Creates an instance of the model <b>Package</b>, registered with
     * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package package URI
     * value.
     * <p>
     * Note: the correct way to create the package is via the static factory method {@link #init
     * init()}, which also performs initialization of the package, or returns the registered
     * package, if one already exists. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see org.eclipse.emf.ecore.EPackage.Registry
     * @see org.oceandsl.tools.sar.fxtran.FxtranPackage#eNS_URI
     * @see #init()
     * @generated
     */
    private FxtranPackageImpl() {
        super(eNS_URI, FxtranFactory.eINSTANCE);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private static boolean isInited = false;

    /**
     * Creates, registers, and initializes the <b>Package</b> for this model, and for any others
     * upon which it depends.
     *
     * <p>
     * This method is used to initialize {@link FxtranPackage#eINSTANCE} when that field is
     * accessed. Clients should not invoke it directly. Instead, they should simply access that
     * field to obtain the package. <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @see #eNS_URI
     * @generated
     */
    public static FxtranPackage init() {
        if (isInited) {
            return (FxtranPackage) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI);
        }

        // Obtain or create and register package
        final Object registeredFxtranPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
        final FxtranPackageImpl theFxtranPackage = registeredFxtranPackage instanceof FxtranPackageImpl
                ? (FxtranPackageImpl) registeredFxtranPackage
                : new FxtranPackageImpl();

        isInited = true;

        // Initialize simple dependencies
        XMLTypePackage.eINSTANCE.eClass();

        // Load packages
        theFxtranPackage.loadPackage();

        // Fix loaded packages
        theFxtranPackage.fixPackageContents();

        // Mark meta-data to indicate it can't be changed
        theFxtranPackage.freeze();

        // Update the registry and return the package
        EPackage.Registry.INSTANCE.put(FxtranPackage.eNS_URI, theFxtranPackage);
        return theFxtranPackage;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getActionStmtType() {
        if (this.actionStmtTypeEClass == null) {
            this.actionStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(0);
        }
        return this.actionStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getActionStmtType_ReturnStmt() {
        return (EAttribute) this.getActionStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionStmtType_WhereStmt() {
        return (EReference) this.getActionStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionStmtType_AStmt() {
        return (EReference) this.getActionStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionStmtType_AllocateStmt() {
        return (EReference) this.getActionStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionStmtType_CallStmt() {
        return (EReference) this.getActionStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionStmtType_DeallocateStmt() {
        return (EReference) this.getActionStmtType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getActionStmtType_ExitStmt() {
        return (EAttribute) this.getActionStmtType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionStmtType_PointerAStmt() {
        return (EReference) this.getActionStmtType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getActionStmtType_CycleStmt() {
        return (EReference) this.getActionStmtType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAcValueLTType() {
        if (this.acValueLTTypeEClass == null) {
            this.acValueLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(1);
        }
        return this.acValueLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAcValueLTType_Mixed() {
        return (EAttribute) this.getAcValueLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAcValueLTType_Group() {
        return (EAttribute) this.getAcValueLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAcValueLTType_C() {
        return (EAttribute) this.getAcValueLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAcValueLTType_Cnt() {
        return (EAttribute) this.getAcValueLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcValueLTType_AcValue() {
        return (EReference) this.getAcValueLTType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAcValueType() {
        if (this.acValueTypeEClass == null) {
            this.acValueTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(2);
        }
        return this.acValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcValueType_LiteralE() {
        return (EReference) this.getAcValueType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcValueType_NamedE() {
        return (EReference) this.getAcValueType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcValueType_OpE() {
        return (EReference) this.getAcValueType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAcValueType_ParensE() {
        return (EReference) this.getAcValueType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAllocateStmtType() {
        if (this.allocateStmtTypeEClass == null) {
            this.allocateStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(3);
        }
        return this.allocateStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAllocateStmtType_Mixed() {
        return (EAttribute) this.getAllocateStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAllocateStmtType_ArgSpec() {
        return (EReference) this.getAllocateStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getArgNType() {
        if (this.argNTypeEClass == null) {
            this.argNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(4);
        }
        return this.argNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgNType_N() {
        return (EReference) this.getArgNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArgNType_K() {
        return (EAttribute) this.getArgNType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArgNType_N1() {
        return (EAttribute) this.getArgNType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getArgSpecType() {
        if (this.argSpecTypeEClass == null) {
            this.argSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(5);
        }
        return this.argSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArgSpecType_Mixed() {
        return (EAttribute) this.getArgSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArgSpecType_Group() {
        return (EAttribute) this.getArgSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArgSpecType_Cnt() {
        return (EAttribute) this.getArgSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgSpecType_Arg() {
        return (EReference) this.getArgSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getArgType() {
        if (this.argTypeEClass == null) {
            this.argTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(6);
        }
        return this.argTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArgType_Mixed() {
        return (EAttribute) this.getArgType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArgType_Group() {
        return (EAttribute) this.getArgType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgType_ArgN() {
        return (EReference) this.getArgType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgType_ArrayConstructorE() {
        return (EReference) this.getArgType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgType_LiteralE() {
        return (EReference) this.getArgType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgType_NamedE() {
        return (EReference) this.getArgType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgType_OpE() {
        return (EReference) this.getArgType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgType_ParensE() {
        return (EReference) this.getArgType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArgType_StringE() {
        return (EReference) this.getArgType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getArrayConstructorEType() {
        if (this.arrayConstructorETypeEClass == null) {
            this.arrayConstructorETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(7);
        }
        return this.arrayConstructorETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArrayConstructorEType_Mixed() {
        return (EAttribute) this.getArrayConstructorEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArrayConstructorEType_Group() {
        return (EAttribute) this.getArrayConstructorEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArrayConstructorEType_C() {
        return (EAttribute) this.getArrayConstructorEType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArrayConstructorEType_Cnt() {
        return (EAttribute) this.getArrayConstructorEType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArrayConstructorEType_AcValueLT() {
        return (EReference) this.getArrayConstructorEType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getArrayRType() {
        if (this.arrayRTypeEClass == null) {
            this.arrayRTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(8);
        }
        return this.arrayRTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArrayRType_Mixed() {
        return (EAttribute) this.getArrayRType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArrayRType_SectionSubscriptLT() {
        return (EReference) this.getArrayRType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getArraySpecType() {
        if (this.arraySpecTypeEClass == null) {
            this.arraySpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(9);
        }
        return this.arraySpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getArraySpecType_Mixed() {
        return (EAttribute) this.getArraySpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getArraySpecType_ShapeSpecLT() {
        return (EReference) this.getArraySpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAStmtType() {
        if (this.aStmtTypeEClass == null) {
            this.aStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(10);
        }
        return this.aStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAStmtType_E1() {
        return (EReference) this.getAStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAStmtType_A() {
        return (EAttribute) this.getAStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAStmtType_E2() {
        return (EReference) this.getAStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getAttributeType() {
        if (this.attributeTypeEClass == null) {
            this.attributeTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(11);
        }
        return this.attributeTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAttributeType_Mixed() {
        return (EAttribute) this.getAttributeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAttributeType_Group() {
        return (EAttribute) this.getAttributeType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getAttributeType_ArraySpec() {
        return (EReference) this.getAttributeType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAttributeType_AttributeN() {
        return (EAttribute) this.getAttributeType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getAttributeType_IntentSpec() {
        return (EAttribute) this.getAttributeType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCallStmtType() {
        if (this.callStmtTypeEClass == null) {
            this.callStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(12);
        }
        return this.callStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCallStmtType_Mixed() {
        return (EAttribute) this.getCallStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCallStmtType_Group() {
        return (EAttribute) this.getCallStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCallStmtType_ArgSpec() {
        return (EReference) this.getCallStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCallStmtType_Cnt() {
        return (EAttribute) this.getCallStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCallStmtType_ProcedureDesignator() {
        return (EReference) this.getCallStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseEType() {
        if (this.caseETypeEClass == null) {
            this.caseETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(13);
        }
        return this.caseETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseEType_NamedE() {
        return (EReference) this.getCaseEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseSelectorType() {
        if (this.caseSelectorTypeEClass == null) {
            this.caseSelectorTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(14);
        }
        return this.caseSelectorTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCaseSelectorType_Mixed() {
        return (EAttribute) this.getCaseSelectorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseSelectorType_CaseValueRangeLT() {
        return (EReference) this.getCaseSelectorType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseStmtType() {
        if (this.caseStmtTypeEClass == null) {
            this.caseStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(15);
        }
        return this.caseStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCaseStmtType_Mixed() {
        return (EAttribute) this.getCaseStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseStmtType_CaseSelector() {
        return (EReference) this.getCaseStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseValueRangeLTType() {
        if (this.caseValueRangeLTTypeEClass == null) {
            this.caseValueRangeLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(16);
        }
        return this.caseValueRangeLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCaseValueRangeLTType_Mixed() {
        return (EAttribute) this.getCaseValueRangeLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseValueRangeLTType_CaseValueRange() {
        return (EReference) this.getCaseValueRangeLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseValueRangeType() {
        if (this.caseValueRangeTypeEClass == null) {
            this.caseValueRangeTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(17);
        }
        return this.caseValueRangeTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseValueRangeType_CaseValue() {
        return (EReference) this.getCaseValueRangeType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCaseValueType() {
        if (this.caseValueTypeEClass == null) {
            this.caseValueTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(18);
        }
        return this.caseValueTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseValueType_LiteralE() {
        return (EReference) this.getCaseValueType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCaseValueType_StringE() {
        return (EReference) this.getCaseValueType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCharSelectorType() {
        if (this.charSelectorTypeEClass == null) {
            this.charSelectorTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(19);
        }
        return this.charSelectorTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCharSelectorType_Mixed() {
        return (EAttribute) this.getCharSelectorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCharSelectorType_CharSpec() {
        return (EReference) this.getCharSelectorType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCharSpecType() {
        if (this.charSpecTypeEClass == null) {
            this.charSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(20);
        }
        return this.charSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCharSpecType_Mixed() {
        return (EAttribute) this.getCharSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCharSpecType_Group() {
        return (EAttribute) this.getCharSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCharSpecType_ArgN() {
        return (EReference) this.getCharSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCharSpecType_Label() {
        return (EReference) this.getCharSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCharSpecType_LiteralE() {
        return (EReference) this.getCharSpecType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCharSpecType_NamedE() {
        return (EReference) this.getCharSpecType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCharSpecType_OpE() {
        return (EReference) this.getCharSpecType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCharSpecType_StarE() {
        return (EAttribute) this.getCharSpecType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCloseSpecSpecType() {
        if (this.closeSpecSpecTypeEClass == null) {
            this.closeSpecSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(21);
        }
        return this.closeSpecSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCloseSpecSpecType_Mixed() {
        return (EAttribute) this.getCloseSpecSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCloseSpecSpecType_CloseSpec() {
        return (EReference) this.getCloseSpecSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCloseSpecType() {
        if (this.closeSpecTypeEClass == null) {
            this.closeSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(22);
        }
        return this.closeSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCloseSpecType_Mixed() {
        return (EAttribute) this.getCloseSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCloseSpecType_Group() {
        return (EAttribute) this.getCloseSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCloseSpecType_ArgN() {
        return (EReference) this.getCloseSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCloseSpecType_LiteralE() {
        return (EReference) this.getCloseSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCloseSpecType_NamedE() {
        return (EReference) this.getCloseSpecType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCloseStmtType() {
        if (this.closeStmtTypeEClass == null) {
            this.closeStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(23);
        }
        return this.closeStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCloseStmtType_Mixed() {
        return (EAttribute) this.getCloseStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCloseStmtType_CloseSpecSpec() {
        return (EReference) this.getCloseStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getComponentDeclStmtType() {
        if (this.componentDeclStmtTypeEClass == null) {
            this.componentDeclStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(24);
        }
        return this.componentDeclStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getComponentDeclStmtType_Mixed() {
        return (EAttribute) this.getComponentDeclStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getComponentDeclStmtType_Group() {
        return (EAttribute) this.getComponentDeclStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getComponentDeclStmtType_ENDeclLT() {
        return (EReference) this.getComponentDeclStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getComponentDeclStmtType_TSpec() {
        return (EReference) this.getComponentDeclStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getComponentDeclStmtType_Attribute() {
        return (EReference) this.getComponentDeclStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getComponentRType() {
        if (this.componentRTypeEClass == null) {
            this.componentRTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(25);
        }
        return this.componentRTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getComponentRType_Mixed() {
        return (EAttribute) this.getComponentRType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getComponentRType_Ct() {
        return (EAttribute) this.getComponentRType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConditionEType() {
        if (this.conditionETypeEClass == null) {
            this.conditionETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(26);
        }
        return this.conditionETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConditionEType_NamedE() {
        return (EReference) this.getConditionEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConditionEType_OpE() {
        return (EReference) this.getConditionEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnectSpecSpecType() {
        if (this.connectSpecSpecTypeEClass == null) {
            this.connectSpecSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(27);
        }
        return this.connectSpecSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getConnectSpecSpecType_Mixed() {
        return (EAttribute) this.getConnectSpecSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getConnectSpecSpecType_Group() {
        return (EAttribute) this.getConnectSpecSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getConnectSpecSpecType_Cnt() {
        return (EAttribute) this.getConnectSpecSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnectSpecSpecType_ConnectSpec() {
        return (EReference) this.getConnectSpecSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getConnectSpecType() {
        if (this.connectSpecTypeEClass == null) {
            this.connectSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(28);
        }
        return this.connectSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getConnectSpecType_Mixed() {
        return (EAttribute) this.getConnectSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getConnectSpecType_Group() {
        return (EAttribute) this.getConnectSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnectSpecType_ArgN() {
        return (EReference) this.getConnectSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnectSpecType_LiteralE() {
        return (EReference) this.getConnectSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnectSpecType_NamedE() {
        return (EReference) this.getConnectSpecType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getConnectSpecType_StringE() {
        return (EReference) this.getConnectSpecType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getCycleStmtType() {
        if (this.cycleStmtTypeEClass == null) {
            this.cycleStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(29);
        }
        return this.cycleStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getCycleStmtType_Mixed() {
        return (EAttribute) this.getCycleStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getCycleStmtType_N() {
        return (EReference) this.getCycleStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDeallocateStmtType() {
        if (this.deallocateStmtTypeEClass == null) {
            this.deallocateStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(30);
        }
        return this.deallocateStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDeallocateStmtType_Mixed() {
        return (EAttribute) this.getDeallocateStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDeallocateStmtType_ArgSpec() {
        return (EReference) this.getDeallocateStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDerivedTSpecType() {
        if (this.derivedTSpecTypeEClass == null) {
            this.derivedTSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(31);
        }
        return this.derivedTSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDerivedTSpecType_Mixed() {
        return (EAttribute) this.getDerivedTSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDerivedTSpecType_TN() {
        return (EReference) this.getDerivedTSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDocumentRoot() {
        if (this.documentRootEClass == null) {
            this.documentRootEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(32);
        }
        return this.documentRootEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_Mixed() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_XMLNSPrefixMap() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_XSISchemaLocation() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_TSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_A() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_AStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_AcValue() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_AcValueLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ActionStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_AllocateStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Arg() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ArgN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ArgSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ArrayConstructorE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ArrayR() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ArraySpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Attribute() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_AttributeN() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_C() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CallStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CaseE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CaseSelector() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CaseStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CaseValue() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CaseValueRange() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CaseValueRangeLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CharSelector() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CharSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CloseSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CloseSpecSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CloseStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_Cnt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ComponentDeclStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ComponentR() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ConditionE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ConnectSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ConnectSpecSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(36);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_ContainsStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(37);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_Cpp() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(38);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_Ct() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(39);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_CycleStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(40);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_DeallocateStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(41);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_DerivedTSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(42);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_DoStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(43);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_DoV() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(44);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_DummyArgLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(45);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_E1() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(46);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_E2() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(47);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Element() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(48);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ElementLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(49);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ElseIfStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(50);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_ElseStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(51);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_ElseWhereStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(52);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(53);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ENDecl() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(54);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ENDeclLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(55);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ENLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(56);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ENN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(57);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndDoStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(58);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndForallStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(59);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndFunctionStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(60);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_EndIfStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(61);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndInterfaceStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(62);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndModuleStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(63);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndProgramStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(64);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndSelectCaseStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(65);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndSubroutineStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(66);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_EndTStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(67);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_EndWhereStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(68);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Error() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(69);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_ExitStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(70);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_File() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(71);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ForallConstructStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(72);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ForallStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(73);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ForallTripletSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(74);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ForallTripletSpecLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(75);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_FunctionN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(76);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_FunctionStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(77);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_IfStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(78);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_IfThenStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(79);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_ImplicitNoneStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(80);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_InitE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(81);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_InquireStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(82);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_InquirySpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(83);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_InquirySpecSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(84);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_IntentSpec() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(85);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_InterfaceStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(86);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_IntrinsicTSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(87);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_IoControl() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(88);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_IoControlSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(89);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Iterator() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(90);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_IteratorDefinitionLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(91);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_IteratorElement() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(92);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_K() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(93);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_KSelector() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(94);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_KSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(95);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_L() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(96);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Label() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(97);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_LiteralE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(98);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_LowerBound() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(99);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_MaskE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(100);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ModuleN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(101);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ModuleProcedureNLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(102);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ModuleStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(103);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_N() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(104);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_N1() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(105);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_NamedE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(106);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_NamelistGroupN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(107);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_NamelistGroupObj() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(108);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_NamelistGroupObjLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(109);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_NamelistGroupObjN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(110);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_NamelistStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(111);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_NullifyStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(112);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_O() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(113);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Object() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(114);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Op() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(115);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_OpE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(116);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_OpenStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(117);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_OutputItem() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(118);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_OutputItemLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(119);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ParensE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(120);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ParensR() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(121);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_PointerAStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(122);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_PointerStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(123);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_Prefix() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(124);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_PrivateStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(125);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ProcedureDesignator() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(126);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ProcedureStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(127);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ProgramN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(128);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ProgramStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(129);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_PublicStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(130);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_RLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(131);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ReadStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(132);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_Rename() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(133);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_RenameLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(134);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ResultSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(135);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_ReturnStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(136);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_S() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(137);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_SaveStmt() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(138);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_SectionSubscript() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(139);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_SectionSubscriptLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(140);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_SelectCaseStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(141);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ShapeSpec() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(142);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_ShapeSpecLT() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(143);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_StarE() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(144);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDocumentRoot_StopCode() {
        return (EAttribute) this.getDocumentRoot().getEStructuralFeatures().get(145);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_StopStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(146);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_StringE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(147);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_SubroutineN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(148);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_SubroutineStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(149);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_TDeclStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(150);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_TN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(151);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_TStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(152);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_TestE() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(153);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_UpperBound() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(154);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_UseN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(155);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_UseStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(156);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_V() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(157);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_VN() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(158);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_WhereConstructStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(159);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_WhereStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(160);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDocumentRoot_WriteStmt() {
        return (EReference) this.getDocumentRoot().getEStructuralFeatures().get(161);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDoStmtType() {
        if (this.doStmtTypeEClass == null) {
            this.doStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(33);
        }
        return this.doStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDoStmtType_Mixed() {
        return (EAttribute) this.getDoStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDoStmtType_Group() {
        return (EAttribute) this.getDoStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDoStmtType_N() {
        return (EReference) this.getDoStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDoStmtType_LowerBound() {
        return (EReference) this.getDoStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDoStmtType_UpperBound() {
        return (EReference) this.getDoStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDoStmtType_DoV() {
        return (EReference) this.getDoStmtType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDoStmtType_TestE() {
        return (EReference) this.getDoStmtType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDoVType() {
        if (this.doVTypeEClass == null) {
            this.doVTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(34);
        }
        return this.doVTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDoVType_NamedE() {
        return (EReference) this.getDoVType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getDummyArgLTType() {
        if (this.dummyArgLTTypeEClass == null) {
            this.dummyArgLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(35);
        }
        return this.dummyArgLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDummyArgLTType_Mixed() {
        return (EAttribute) this.getDummyArgLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDummyArgLTType_Group() {
        return (EAttribute) this.getDummyArgLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getDummyArgLTType_ArgN() {
        return (EReference) this.getDummyArgLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getDummyArgLTType_Cnt() {
        return (EAttribute) this.getDummyArgLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getE1Type() {
        if (this.e1TypeEClass == null) {
            this.e1TypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(36);
        }
        return this.e1TypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getE1Type_NamedE() {
        return (EReference) this.getE1Type().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getE2Type() {
        if (this.e2TypeEClass == null) {
            this.e2TypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(37);
        }
        return this.e2TypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getE2Type_ArrayConstructorE() {
        return (EReference) this.getE2Type().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getE2Type_LiteralE() {
        return (EReference) this.getE2Type().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getE2Type_NamedE() {
        return (EReference) this.getE2Type().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getE2Type_OpE() {
        return (EReference) this.getE2Type().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getE2Type_ParensE() {
        return (EReference) this.getE2Type().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getE2Type_StringE() {
        return (EReference) this.getE2Type().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getElementLTType() {
        if (this.elementLTTypeEClass == null) {
            this.elementLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(38);
        }
        return this.elementLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElementLTType_Mixed() {
        return (EAttribute) this.getElementLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElementLTType_Group() {
        return (EAttribute) this.getElementLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElementLTType_Cnt() {
        return (EAttribute) this.getElementLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElementLTType_Element() {
        return (EReference) this.getElementLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getElementType() {
        if (this.elementTypeEClass == null) {
            this.elementTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(39);
        }
        return this.elementTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElementType_ArrayConstructorE() {
        return (EReference) this.getElementType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElementType_Group() {
        return (EAttribute) this.getElementType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElementType_NamedE() {
        return (EReference) this.getElementType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElementType_OpE() {
        return (EReference) this.getElementType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElementType_LiteralE() {
        return (EReference) this.getElementType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElementType_StringE() {
        return (EReference) this.getElementType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getElseIfStmtType() {
        if (this.elseIfStmtTypeEClass == null) {
            this.elseIfStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(40);
        }
        return this.elseIfStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getElseIfStmtType_Mixed() {
        return (EAttribute) this.getElseIfStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getElseIfStmtType_ConditionE() {
        return (EReference) this.getElseIfStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndDoStmtType() {
        if (this.endDoStmtTypeEClass == null) {
            this.endDoStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(41);
        }
        return this.endDoStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndDoStmtType_Mixed() {
        return (EAttribute) this.getEndDoStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndDoStmtType_N() {
        return (EReference) this.getEndDoStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getENDeclLTType() {
        if (this.enDeclLTTypeEClass == null) {
            this.enDeclLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(42);
        }
        return this.enDeclLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENDeclLTType_Mixed() {
        return (EAttribute) this.getENDeclLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENDeclLTType_Group() {
        return (EAttribute) this.getENDeclLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENDeclLTType_Cnt() {
        return (EAttribute) this.getENDeclLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getENDeclLTType_ENDecl() {
        return (EReference) this.getENDeclLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getENDeclType() {
        if (this.enDeclTypeEClass == null) {
            this.enDeclTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(43);
        }
        return this.enDeclTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENDeclType_Mixed() {
        return (EAttribute) this.getENDeclType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENDeclType_Group() {
        return (EAttribute) this.getENDeclType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getENDeclType_ArraySpec() {
        return (EReference) this.getENDeclType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getENDeclType_ENN() {
        return (EReference) this.getENDeclType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getENDeclType_InitE() {
        return (EReference) this.getENDeclType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndForallStmtType() {
        if (this.endForallStmtTypeEClass == null) {
            this.endForallStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(44);
        }
        return this.endForallStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndForallStmtType_Mixed() {
        return (EAttribute) this.getEndForallStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndForallStmtType_N() {
        return (EReference) this.getEndForallStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndFunctionStmtType() {
        if (this.endFunctionStmtTypeEClass == null) {
            this.endFunctionStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(45);
        }
        return this.endFunctionStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndFunctionStmtType_Mixed() {
        return (EAttribute) this.getEndFunctionStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndFunctionStmtType_FunctionN() {
        return (EReference) this.getEndFunctionStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndInterfaceStmtType() {
        if (this.endInterfaceStmtTypeEClass == null) {
            this.endInterfaceStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(46);
        }
        return this.endInterfaceStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndInterfaceStmtType_Mixed() {
        return (EAttribute) this.getEndInterfaceStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndInterfaceStmtType_N() {
        return (EReference) this.getEndInterfaceStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndModuleStmtType() {
        if (this.endModuleStmtTypeEClass == null) {
            this.endModuleStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(47);
        }
        return this.endModuleStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndModuleStmtType_Mixed() {
        return (EAttribute) this.getEndModuleStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndModuleStmtType_ModuleN() {
        return (EReference) this.getEndModuleStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndProgramStmtType() {
        if (this.endProgramStmtTypeEClass == null) {
            this.endProgramStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(48);
        }
        return this.endProgramStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndProgramStmtType_Mixed() {
        return (EAttribute) this.getEndProgramStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndProgramStmtType_ProgramN() {
        return (EReference) this.getEndProgramStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndSelectCaseStmtType() {
        if (this.endSelectCaseStmtTypeEClass == null) {
            this.endSelectCaseStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(49);
        }
        return this.endSelectCaseStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndSelectCaseStmtType_Mixed() {
        return (EAttribute) this.getEndSelectCaseStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndSelectCaseStmtType_N() {
        return (EReference) this.getEndSelectCaseStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndSubroutineStmtType() {
        if (this.endSubroutineStmtTypeEClass == null) {
            this.endSubroutineStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(50);
        }
        return this.endSubroutineStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndSubroutineStmtType_Mixed() {
        return (EAttribute) this.getEndSubroutineStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndSubroutineStmtType_SubroutineN() {
        return (EReference) this.getEndSubroutineStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getEndTStmtType() {
        if (this.endTStmtTypeEClass == null) {
            this.endTStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(51);
        }
        return this.endTStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getEndTStmtType_Mixed() {
        return (EAttribute) this.getEndTStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getEndTStmtType_TN() {
        return (EReference) this.getEndTStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getENLTType() {
        if (this.enltTypeEClass == null) {
            this.enltTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(52);
        }
        return this.enltTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENLTType_Mixed() {
        return (EAttribute) this.getENLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENLTType_Group() {
        return (EAttribute) this.getENLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getENLTType_Cnt() {
        return (EAttribute) this.getENLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getENLTType_EN() {
        return (EReference) this.getENLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getENNType() {
        if (this.ennTypeEClass == null) {
            this.ennTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(53);
        }
        return this.ennTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getENNType_N() {
        return (EReference) this.getENNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getENType() {
        if (this.enTypeEClass == null) {
            this.enTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(54);
        }
        return this.enTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getENType_N() {
        return (EReference) this.getENType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getErrorType() {
        if (this.errorTypeEClass == null) {
            this.errorTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(55);
        }
        return this.errorTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getErrorType_Msg() {
        return (EAttribute) this.getErrorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFileType() {
        if (this.fileTypeEClass == null) {
            this.fileTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(56);
        }
        return this.fileTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_Group() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_C() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_AStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_AllocateStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_CallStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_DeallocateStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_ExitStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_PointerAStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_ReturnStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(8);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_WhereStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(9);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_TDeclStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(10);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_TStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(11);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_CaseStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(12);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_CloseStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(13);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ComponentDeclStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(14);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_ContainsStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(15);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_Cpp() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(16);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_DoStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(17);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ElseIfStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(18);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_ElseStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(19);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_ElseWhereStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(20);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndTStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(21);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndDoStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(22);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndForallStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(23);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndFunctionStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(24);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_EndIfStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(25);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndInterfaceStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(26);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndSelectCaseStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(27);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndSubroutineStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(28);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_EndWhereStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(29);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ForallConstructStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(30);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ForallStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(31);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_FunctionStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(32);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_IfStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(33);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_IfThenStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(34);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_ImplicitNoneStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(35);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_InquireStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(36);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_InterfaceStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(37);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ModuleStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(38);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_NamelistStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(39);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_NullifyStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(40);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_OpenStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(41);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_PointerStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(42);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_PrivateStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(43);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ProcedureStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(44);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ProgramStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(45);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_PublicStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(46);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_ReadStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(47);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_SaveStmt() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(48);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_SelectCaseStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(49);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_StopStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(50);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_SubroutineStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(51);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_UseStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(52);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_WhereConstructStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(53);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_WriteStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(54);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndModuleStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(55);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFileType_EndProgramStmt() {
        return (EReference) this.getFileType().getEStructuralFeatures().get(56);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFileType_Name() {
        return (EAttribute) this.getFileType().getEStructuralFeatures().get(57);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getForallConstructStmtType() {
        if (this.forallConstructStmtTypeEClass == null) {
            this.forallConstructStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(57);
        }
        return this.forallConstructStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallConstructStmtType_Mixed() {
        return (EAttribute) this.getForallConstructStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallConstructStmtType_Group() {
        return (EAttribute) this.getForallConstructStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallConstructStmtType_N() {
        return (EReference) this.getForallConstructStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallConstructStmtType_ForallTripletSpecLT() {
        return (EReference) this.getForallConstructStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getForallStmtType() {
        if (this.forallStmtTypeEClass == null) {
            this.forallStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(58);
        }
        return this.forallStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallStmtType_Mixed() {
        return (EAttribute) this.getForallStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallStmtType_Group() {
        return (EAttribute) this.getForallStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallStmtType_ActionStmt() {
        return (EReference) this.getForallStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallStmtType_Cnt() {
        return (EAttribute) this.getForallStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallStmtType_ForallTripletSpecLT() {
        return (EReference) this.getForallStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallStmtType_MaskE() {
        return (EReference) this.getForallStmtType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getForallTripletSpecLTType() {
        if (this.forallTripletSpecLTTypeEClass == null) {
            this.forallTripletSpecLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(59);
        }
        return this.forallTripletSpecLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallTripletSpecLTType_Mixed() {
        return (EAttribute) this.getForallTripletSpecLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallTripletSpecLTType_ForallTripletSpec() {
        return (EReference) this.getForallTripletSpecLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getForallTripletSpecType() {
        if (this.forallTripletSpecTypeEClass == null) {
            this.forallTripletSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(60);
        }
        return this.forallTripletSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallTripletSpecType_Mixed() {
        return (EAttribute) this.getForallTripletSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getForallTripletSpecType_Group() {
        return (EAttribute) this.getForallTripletSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallTripletSpecType_LowerBound() {
        return (EReference) this.getForallTripletSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallTripletSpecType_UpperBound() {
        return (EReference) this.getForallTripletSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getForallTripletSpecType_V() {
        return (EReference) this.getForallTripletSpecType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFunctionNType() {
        if (this.functionNTypeEClass == null) {
            this.functionNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(61);
        }
        return this.functionNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunctionNType_N() {
        return (EReference) this.getFunctionNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getFunctionStmtType() {
        if (this.functionStmtTypeEClass == null) {
            this.functionStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(62);
        }
        return this.functionStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFunctionStmtType_Mixed() {
        return (EAttribute) this.getFunctionStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFunctionStmtType_Group() {
        return (EAttribute) this.getFunctionStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunctionStmtType_DerivedTSpec() {
        return (EReference) this.getFunctionStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunctionStmtType_DummyArgLT() {
        return (EReference) this.getFunctionStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunctionStmtType_FunctionN() {
        return (EReference) this.getFunctionStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunctionStmtType_IntrinsicTSpec() {
        return (EReference) this.getFunctionStmtType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getFunctionStmtType_Prefix() {
        return (EAttribute) this.getFunctionStmtType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getFunctionStmtType_ResultSpec() {
        return (EReference) this.getFunctionStmtType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIfStmtType() {
        if (this.ifStmtTypeEClass == null) {
            this.ifStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(63);
        }
        return this.ifStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIfStmtType_Mixed() {
        return (EAttribute) this.getIfStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIfStmtType_Group() {
        return (EAttribute) this.getIfStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIfStmtType_ActionStmt() {
        return (EReference) this.getIfStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIfStmtType_Cnt() {
        return (EAttribute) this.getIfStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIfStmtType_ConditionE() {
        return (EReference) this.getIfStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIfThenStmtType() {
        if (this.ifThenStmtTypeEClass == null) {
            this.ifThenStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(64);
        }
        return this.ifThenStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIfThenStmtType_Mixed() {
        return (EAttribute) this.getIfThenStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIfThenStmtType_ConditionE() {
        return (EReference) this.getIfThenStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInitEType() {
        if (this.initETypeEClass == null) {
            this.initETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(65);
        }
        return this.initETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInitEType_LiteralE() {
        return (EReference) this.getInitEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInitEType_NamedE() {
        return (EReference) this.getInitEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInitEType_OpE() {
        return (EReference) this.getInitEType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInitEType_StringE() {
        return (EReference) this.getInitEType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInquireStmtType() {
        if (this.inquireStmtTypeEClass == null) {
            this.inquireStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(66);
        }
        return this.inquireStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getInquireStmtType_Mixed() {
        return (EAttribute) this.getInquireStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInquireStmtType_InquirySpecSpec() {
        return (EReference) this.getInquireStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInquirySpecSpecType() {
        if (this.inquirySpecSpecTypeEClass == null) {
            this.inquirySpecSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(67);
        }
        return this.inquirySpecSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getInquirySpecSpecType_Mixed() {
        return (EAttribute) this.getInquirySpecSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInquirySpecSpecType_InquirySpec() {
        return (EReference) this.getInquirySpecSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInquirySpecType() {
        if (this.inquirySpecTypeEClass == null) {
            this.inquirySpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(68);
        }
        return this.inquirySpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getInquirySpecType_Mixed() {
        return (EAttribute) this.getInquirySpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getInquirySpecType_Group() {
        return (EAttribute) this.getInquirySpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInquirySpecType_ArgN() {
        return (EReference) this.getInquirySpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInquirySpecType_NamedE() {
        return (EReference) this.getInquirySpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getInterfaceStmtType() {
        if (this.interfaceStmtTypeEClass == null) {
            this.interfaceStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(69);
        }
        return this.interfaceStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getInterfaceStmtType_Mixed() {
        return (EAttribute) this.getInterfaceStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getInterfaceStmtType_N() {
        return (EReference) this.getInterfaceStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIntrinsicTSpecType() {
        if (this.intrinsicTSpecTypeEClass == null) {
            this.intrinsicTSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(70);
        }
        return this.intrinsicTSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIntrinsicTSpecType_TN() {
        return (EReference) this.getIntrinsicTSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIntrinsicTSpecType_KSelector() {
        return (EReference) this.getIntrinsicTSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIntrinsicTSpecType_CharSelector() {
        return (EReference) this.getIntrinsicTSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIoControlSpecType() {
        if (this.ioControlSpecTypeEClass == null) {
            this.ioControlSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(71);
        }
        return this.ioControlSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIoControlSpecType_Mixed() {
        return (EAttribute) this.getIoControlSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIoControlSpecType_IoControl() {
        return (EReference) this.getIoControlSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIoControlType() {
        if (this.ioControlTypeEClass == null) {
            this.ioControlTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(72);
        }
        return this.ioControlTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIoControlType_Mixed() {
        return (EAttribute) this.getIoControlType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIoControlType_Group() {
        return (EAttribute) this.getIoControlType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIoControlType_ArgN() {
        return (EReference) this.getIoControlType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIoControlType_Label() {
        return (EReference) this.getIoControlType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIoControlType_LiteralE() {
        return (EReference) this.getIoControlType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIoControlType_NamedE() {
        return (EReference) this.getIoControlType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIoControlType_StringE() {
        return (EReference) this.getIoControlType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIteratorDefinitionLTType() {
        if (this.iteratorDefinitionLTTypeEClass == null) {
            this.iteratorDefinitionLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(73);
        }
        return this.iteratorDefinitionLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIteratorDefinitionLTType_Mixed() {
        return (EAttribute) this.getIteratorDefinitionLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIteratorDefinitionLTType_IteratorElement() {
        return (EReference) this.getIteratorDefinitionLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIteratorElementType() {
        if (this.iteratorElementTypeEClass == null) {
            this.iteratorElementTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(74);
        }
        return this.iteratorElementTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIteratorElementType_Mixed() {
        return (EAttribute) this.getIteratorElementType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIteratorElementType_Group() {
        return (EAttribute) this.getIteratorElementType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIteratorElementType_VN() {
        return (EReference) this.getIteratorElementType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIteratorElementType_LiteralE() {
        return (EReference) this.getIteratorElementType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIteratorElementType_NamedE() {
        return (EReference) this.getIteratorElementType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getIteratorType() {
        if (this.iteratorTypeEClass == null) {
            this.iteratorTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(75);
        }
        return this.iteratorTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getIteratorType_Mixed() {
        return (EAttribute) this.getIteratorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getIteratorType_IteratorDefinitionLT() {
        return (EReference) this.getIteratorType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getKSelectorType() {
        if (this.kSelectorTypeEClass == null) {
            this.kSelectorTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(76);
        }
        return this.kSelectorTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getKSelectorType_Mixed() {
        return (EAttribute) this.getKSelectorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getKSelectorType_KSpec() {
        return (EReference) this.getKSelectorType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getKSpecType() {
        if (this.kSpecTypeEClass == null) {
            this.kSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(77);
        }
        return this.kSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getKSpecType_Mixed() {
        return (EAttribute) this.getKSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getKSpecType_Group() {
        return (EAttribute) this.getKSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getKSpecType_N() {
        return (EReference) this.getKSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getKSpecType_L() {
        return (EAttribute) this.getKSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getKSpecType_LiteralE() {
        return (EReference) this.getKSpecType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getKSpecType_NamedE() {
        return (EReference) this.getKSpecType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLabelType() {
        if (this.labelTypeEClass == null) {
            this.labelTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(78);
        }
        return this.labelTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getLabelType_Error() {
        return (EReference) this.getLabelType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLiteralEType() {
        if (this.literalETypeEClass == null) {
            this.literalETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(79);
        }
        return this.literalETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLiteralEType_Mixed() {
        return (EAttribute) this.getLiteralEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLiteralEType_Group() {
        return (EAttribute) this.getLiteralEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getLiteralEType_KSpec() {
        return (EReference) this.getLiteralEType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getLiteralEType_L() {
        return (EAttribute) this.getLiteralEType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getLowerBoundType() {
        if (this.lowerBoundTypeEClass == null) {
            this.lowerBoundTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(80);
        }
        return this.lowerBoundTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getLowerBoundType_LiteralE() {
        return (EReference) this.getLowerBoundType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getLowerBoundType_NamedE() {
        return (EReference) this.getLowerBoundType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getLowerBoundType_OpE() {
        return (EReference) this.getLowerBoundType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getMaskEType() {
        if (this.maskETypeEClass == null) {
            this.maskETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(81);
        }
        return this.maskETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getMaskEType_OpE() {
        return (EReference) this.getMaskEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getModuleNType() {
        if (this.moduleNTypeEClass == null) {
            this.moduleNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(82);
        }
        return this.moduleNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getModuleNType_N() {
        return (EReference) this.getModuleNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getModuleProcedureNLTType() {
        if (this.moduleProcedureNLTTypeEClass == null) {
            this.moduleProcedureNLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(83);
        }
        return this.moduleProcedureNLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getModuleProcedureNLTType_Mixed() {
        return (EAttribute) this.getModuleProcedureNLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getModuleProcedureNLTType_N() {
        return (EReference) this.getModuleProcedureNLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getModuleStmtType() {
        if (this.moduleStmtTypeEClass == null) {
            this.moduleStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(84);
        }
        return this.moduleStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getModuleStmtType_Mixed() {
        return (EAttribute) this.getModuleStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getModuleStmtType_ModuleN() {
        return (EReference) this.getModuleStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamedEType() {
        if (this.namedETypeEClass == null) {
            this.namedETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(85);
        }
        return this.namedETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamedEType_Group() {
        return (EAttribute) this.getNamedEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamedEType_N() {
        return (EReference) this.getNamedEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamedEType_RLT() {
        return (EReference) this.getNamedEType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamelistGroupNType() {
        if (this.namelistGroupNTypeEClass == null) {
            this.namelistGroupNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(86);
        }
        return this.namelistGroupNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistGroupNType_N() {
        return (EAttribute) this.getNamelistGroupNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamelistGroupObjLTType() {
        if (this.namelistGroupObjLTTypeEClass == null) {
            this.namelistGroupObjLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(87);
        }
        return this.namelistGroupObjLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistGroupObjLTType_Mixed() {
        return (EAttribute) this.getNamelistGroupObjLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistGroupObjLTType_Group() {
        return (EAttribute) this.getNamelistGroupObjLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistGroupObjLTType_C() {
        return (EAttribute) this.getNamelistGroupObjLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistGroupObjLTType_Cnt() {
        return (EAttribute) this.getNamelistGroupObjLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamelistGroupObjLTType_NamelistGroupObj() {
        return (EReference) this.getNamelistGroupObjLTType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamelistGroupObjNType() {
        if (this.namelistGroupObjNTypeEClass == null) {
            this.namelistGroupObjNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(88);
        }
        return this.namelistGroupObjNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamelistGroupObjNType_N() {
        return (EReference) this.getNamelistGroupObjNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamelistGroupObjType() {
        if (this.namelistGroupObjTypeEClass == null) {
            this.namelistGroupObjTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(89);
        }
        return this.namelistGroupObjTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamelistGroupObjType_NamelistGroupObjN() {
        return (EReference) this.getNamelistGroupObjType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNamelistStmtType() {
        if (this.namelistStmtTypeEClass == null) {
            this.namelistStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(90);
        }
        return this.namelistStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistStmtType_Mixed() {
        return (EAttribute) this.getNamelistStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistStmtType_Group() {
        return (EAttribute) this.getNamelistStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNamelistStmtType_Cnt() {
        return (EAttribute) this.getNamelistStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamelistStmtType_NamelistGroupN() {
        return (EReference) this.getNamelistStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNamelistStmtType_NamelistGroupObjLT() {
        return (EReference) this.getNamelistStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNType() {
        if (this.nTypeEClass == null) {
            this.nTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(91);
        }
        return this.nTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNType_Mixed() {
        return (EAttribute) this.getNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNType_Group() {
        return (EAttribute) this.getNType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNType_N() {
        return (EReference) this.getNType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNType_N1() {
        return (EAttribute) this.getNType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNType_Op() {
        return (EReference) this.getNType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getNullifyStmtType() {
        if (this.nullifyStmtTypeEClass == null) {
            this.nullifyStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(92);
        }
        return this.nullifyStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getNullifyStmtType_Mixed() {
        return (EAttribute) this.getNullifyStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getNullifyStmtType_ArgSpec() {
        return (EReference) this.getNullifyStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getObjectType() {
        if (this.objectTypeEClass == null) {
            this.objectTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(93);
        }
        return this.objectTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getObjectType_File() {
        return (EReference) this.getObjectType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getObjectType_Openacc() {
        return (EAttribute) this.getObjectType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getObjectType_Openmp() {
        return (EAttribute) this.getObjectType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getObjectType_SourceForm() {
        return (EAttribute) this.getObjectType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getObjectType_SourceWidth() {
        return (EAttribute) this.getObjectType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOpenStmtType() {
        if (this.openStmtTypeEClass == null) {
            this.openStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(94);
        }
        return this.openStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOpenStmtType_Mixed() {
        return (EAttribute) this.getOpenStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOpenStmtType_Group() {
        return (EAttribute) this.getOpenStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOpenStmtType_Cnt() {
        return (EAttribute) this.getOpenStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOpenStmtType_ConnectSpecSpec() {
        return (EReference) this.getOpenStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOpEType() {
        if (this.opETypeEClass == null) {
            this.opETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(95);
        }
        return this.opETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOpEType_Group() {
        return (EAttribute) this.getOpEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOpEType_Cnt() {
        return (EAttribute) this.getOpEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOpEType_LiteralE() {
        return (EReference) this.getOpEType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOpEType_NamedE() {
        return (EReference) this.getOpEType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOpEType_Op() {
        return (EReference) this.getOpEType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOpEType_OpE() {
        return (EReference) this.getOpEType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOpEType_ParensE() {
        return (EReference) this.getOpEType().getEStructuralFeatures().get(6);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOpEType_StringE() {
        return (EReference) this.getOpEType().getEStructuralFeatures().get(7);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOpType() {
        if (this.opTypeEClass == null) {
            this.opTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(96);
        }
        return this.opTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOpType_O() {
        return (EAttribute) this.getOpType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOutputItemLTType() {
        if (this.outputItemLTTypeEClass == null) {
            this.outputItemLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(97);
        }
        return this.outputItemLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getOutputItemLTType_Mixed() {
        return (EAttribute) this.getOutputItemLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOutputItemLTType_OutputItem() {
        return (EReference) this.getOutputItemLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getOutputItemType() {
        if (this.outputItemTypeEClass == null) {
            this.outputItemTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(98);
        }
        return this.outputItemTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOutputItemType_LiteralE() {
        return (EReference) this.getOutputItemType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOutputItemType_NamedE() {
        return (EReference) this.getOutputItemType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOutputItemType_OpE() {
        return (EReference) this.getOutputItemType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getOutputItemType_StringE() {
        return (EReference) this.getOutputItemType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getParensEType() {
        if (this.parensETypeEClass == null) {
            this.parensETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(99);
        }
        return this.parensETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getParensEType_Mixed() {
        return (EAttribute) this.getParensEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getParensEType_Group() {
        return (EAttribute) this.getParensEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getParensEType_Cnt() {
        return (EAttribute) this.getParensEType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getParensEType_OpE() {
        return (EReference) this.getParensEType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getParensEType_Iterator() {
        return (EReference) this.getParensEType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getParensRType() {
        if (this.parensRTypeEClass == null) {
            this.parensRTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(100);
        }
        return this.parensRTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getParensRType_Mixed() {
        return (EAttribute) this.getParensRType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getParensRType_Group() {
        return (EAttribute) this.getParensRType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getParensRType_ArgSpec() {
        return (EReference) this.getParensRType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getParensRType_Cnt() {
        return (EAttribute) this.getParensRType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getParensRType_ElementLT() {
        return (EReference) this.getParensRType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPointerAStmtType() {
        if (this.pointerAStmtTypeEClass == null) {
            this.pointerAStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(101);
        }
        return this.pointerAStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPointerAStmtType_E1() {
        return (EReference) this.getPointerAStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getPointerAStmtType_A() {
        return (EAttribute) this.getPointerAStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPointerAStmtType_E2() {
        return (EReference) this.getPointerAStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPointerStmtType() {
        if (this.pointerStmtTypeEClass == null) {
            this.pointerStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(102);
        }
        return this.pointerStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getPointerStmtType_Mixed() {
        return (EAttribute) this.getPointerStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPointerStmtType_ENDeclLT() {
        return (EReference) this.getPointerStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getProcedureDesignatorType() {
        if (this.procedureDesignatorTypeEClass == null) {
            this.procedureDesignatorTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(103);
        }
        return this.procedureDesignatorTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getProcedureDesignatorType_NamedE() {
        return (EReference) this.getProcedureDesignatorType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getProcedureStmtType() {
        if (this.procedureStmtTypeEClass == null) {
            this.procedureStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(104);
        }
        return this.procedureStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getProcedureStmtType_Mixed() {
        return (EAttribute) this.getProcedureStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getProcedureStmtType_ModuleProcedureNLT() {
        return (EReference) this.getProcedureStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getProgramNType() {
        if (this.programNTypeEClass == null) {
            this.programNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(105);
        }
        return this.programNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getProgramNType_N() {
        return (EReference) this.getProgramNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getProgramStmtType() {
        if (this.programStmtTypeEClass == null) {
            this.programStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(106);
        }
        return this.programStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getProgramStmtType_Mixed() {
        return (EAttribute) this.getProgramStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getProgramStmtType_ProgramN() {
        return (EReference) this.getProgramStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getPublicStmtType() {
        if (this.publicStmtTypeEClass == null) {
            this.publicStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(107);
        }
        return this.publicStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getPublicStmtType_Mixed() {
        return (EAttribute) this.getPublicStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getPublicStmtType_ENLT() {
        return (EReference) this.getPublicStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getReadStmtType() {
        if (this.readStmtTypeEClass == null) {
            this.readStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(108);
        }
        return this.readStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getReadStmtType_Mixed() {
        return (EAttribute) this.getReadStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getReadStmtType_IoControlSpec() {
        return (EReference) this.getReadStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRenameLTType() {
        if (this.renameLTTypeEClass == null) {
            this.renameLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(109);
        }
        return this.renameLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRenameLTType_Mixed() {
        return (EAttribute) this.getRenameLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRenameLTType_Group() {
        return (EAttribute) this.getRenameLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRenameLTType_Cnt() {
        return (EAttribute) this.getRenameLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRenameLTType_Rename() {
        return (EReference) this.getRenameLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRenameType() {
        if (this.renameTypeEClass == null) {
            this.renameTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(110);
        }
        return this.renameTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRenameType_UseN() {
        return (EReference) this.getRenameType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getResultSpecType() {
        if (this.resultSpecTypeEClass == null) {
            this.resultSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(111);
        }
        return this.resultSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getResultSpecType_Mixed() {
        return (EAttribute) this.getResultSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getResultSpecType_N() {
        return (EReference) this.getResultSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getRLTType() {
        if (this.rltTypeEClass == null) {
            this.rltTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(112);
        }
        return this.rltTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getRLTType_Group() {
        return (EAttribute) this.getRLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRLTType_ArrayR() {
        return (EReference) this.getRLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRLTType_ComponentR() {
        return (EReference) this.getRLTType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getRLTType_ParensR() {
        return (EReference) this.getRLTType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSectionSubscriptLTType() {
        if (this.sectionSubscriptLTTypeEClass == null) {
            this.sectionSubscriptLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(113);
        }
        return this.sectionSubscriptLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSectionSubscriptLTType_Mixed() {
        return (EAttribute) this.getSectionSubscriptLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSectionSubscriptLTType_SectionSubscript() {
        return (EReference) this.getSectionSubscriptLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSectionSubscriptType() {
        if (this.sectionSubscriptTypeEClass == null) {
            this.sectionSubscriptTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(114);
        }
        return this.sectionSubscriptTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSectionSubscriptType_Mixed() {
        return (EAttribute) this.getSectionSubscriptType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSectionSubscriptType_Group() {
        return (EAttribute) this.getSectionSubscriptType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSectionSubscriptType_LowerBound() {
        return (EReference) this.getSectionSubscriptType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSectionSubscriptType_UpperBound() {
        return (EReference) this.getSectionSubscriptType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSelectCaseStmtType() {
        if (this.selectCaseStmtTypeEClass == null) {
            this.selectCaseStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(115);
        }
        return this.selectCaseStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSelectCaseStmtType_Mixed() {
        return (EAttribute) this.getSelectCaseStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSelectCaseStmtType_Group() {
        return (EAttribute) this.getSelectCaseStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSelectCaseStmtType_N() {
        return (EReference) this.getSelectCaseStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSelectCaseStmtType_CaseE() {
        return (EReference) this.getSelectCaseStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getShapeSpecLTType() {
        if (this.shapeSpecLTTypeEClass == null) {
            this.shapeSpecLTTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(116);
        }
        return this.shapeSpecLTTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getShapeSpecLTType_Mixed() {
        return (EAttribute) this.getShapeSpecLTType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getShapeSpecLTType_ShapeSpec() {
        return (EReference) this.getShapeSpecLTType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getShapeSpecType() {
        if (this.shapeSpecTypeEClass == null) {
            this.shapeSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(117);
        }
        return this.shapeSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getShapeSpecType_Mixed() {
        return (EAttribute) this.getShapeSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getShapeSpecType_Group() {
        return (EAttribute) this.getShapeSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getShapeSpecType_LowerBound() {
        return (EReference) this.getShapeSpecType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getShapeSpecType_UpperBound() {
        return (EReference) this.getShapeSpecType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStopStmtType() {
        if (this.stopStmtTypeEClass == null) {
            this.stopStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(118);
        }
        return this.stopStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getStopStmtType_Mixed() {
        return (EAttribute) this.getStopStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getStopStmtType_StopCode() {
        return (EAttribute) this.getStopStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getStringEType() {
        if (this.stringETypeEClass == null) {
            this.stringETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(119);
        }
        return this.stringETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getStringEType_S() {
        return (EAttribute) this.getStringEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSubroutineNType() {
        if (this.subroutineNTypeEClass == null) {
            this.subroutineNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(120);
        }
        return this.subroutineNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubroutineNType_N() {
        return (EReference) this.getSubroutineNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getSubroutineStmtType() {
        if (this.subroutineStmtTypeEClass == null) {
            this.subroutineStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(121);
        }
        return this.subroutineStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSubroutineStmtType_Mixed() {
        return (EAttribute) this.getSubroutineStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSubroutineStmtType_Group() {
        return (EAttribute) this.getSubroutineStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSubroutineStmtType_Cnt() {
        return (EAttribute) this.getSubroutineStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubroutineStmtType_DummyArgLT() {
        return (EReference) this.getSubroutineStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getSubroutineStmtType_Prefix() {
        return (EAttribute) this.getSubroutineStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getSubroutineStmtType_SubroutineN() {
        return (EReference) this.getSubroutineStmtType().getEStructuralFeatures().get(5);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTDeclStmtType() {
        if (this.tDeclStmtTypeEClass == null) {
            this.tDeclStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(122);
        }
        return this.tDeclStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTDeclStmtType_Mixed() {
        return (EAttribute) this.getTDeclStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTDeclStmtType_Group() {
        return (EAttribute) this.getTDeclStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTDeclStmtType_ENDeclLT() {
        return (EReference) this.getTDeclStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTDeclStmtType_TSpec() {
        return (EReference) this.getTDeclStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTDeclStmtType_Attribute() {
        return (EReference) this.getTDeclStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTestEType() {
        if (this.testETypeEClass == null) {
            this.testETypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(123);
        }
        return this.testETypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTestEType_NamedE() {
        return (EReference) this.getTestEType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTestEType_OpE() {
        return (EReference) this.getTestEType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTNType() {
        if (this.tnTypeEClass == null) {
            this.tnTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(124);
        }
        return this.tnTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTNType_Mixed() {
        return (EAttribute) this.getTNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTNType_N() {
        return (EReference) this.getTNType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTSpecType() {
        if (this.tSpecTypeEClass == null) {
            this.tSpecTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(125);
        }
        return this.tSpecTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTSpecType_DerivedTSpec() {
        return (EReference) this.getTSpecType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTSpecType_IntrinsicTSpec() {
        return (EReference) this.getTSpecType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getTStmtType() {
        if (this.tStmtTypeEClass == null) {
            this.tStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(126);
        }
        return this.tStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTStmtType_Mixed() {
        return (EAttribute) this.getTStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getTStmtType_Group() {
        return (EAttribute) this.getTStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTStmtType_TN() {
        return (EReference) this.getTStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getTStmtType_Attribute() {
        return (EReference) this.getTStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUpperBoundType() {
        if (this.upperBoundTypeEClass == null) {
            this.upperBoundTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(127);
        }
        return this.upperBoundTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUpperBoundType_LiteralE() {
        return (EReference) this.getUpperBoundType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUpperBoundType_NamedE() {
        return (EReference) this.getUpperBoundType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUpperBoundType_OpE() {
        return (EReference) this.getUpperBoundType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUseNType() {
        if (this.useNTypeEClass == null) {
            this.useNTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(128);
        }
        return this.useNTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUseNType_N() {
        return (EReference) this.getUseNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getUseStmtType() {
        if (this.useStmtTypeEClass == null) {
            this.useStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(129);
        }
        return this.useStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getUseStmtType_Mixed() {
        return (EAttribute) this.getUseStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getUseStmtType_Group() {
        return (EAttribute) this.getUseStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUseStmtType_ModuleN() {
        return (EReference) this.getUseStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getUseStmtType_RenameLT() {
        return (EReference) this.getUseStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getVNType() {
        if (this.vnTypeEClass == null) {
            this.vnTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(130);
        }
        return this.vnTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getVNType_VN() {
        return (EReference) this.getVNType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getVNType_N() {
        return (EAttribute) this.getVNType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getVType() {
        if (this.vTypeEClass == null) {
            this.vTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI).getEClassifiers()
                    .get(131);
        }
        return this.vTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getVType_NamedE() {
        return (EReference) this.getVType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getWhereConstructStmtType() {
        if (this.whereConstructStmtTypeEClass == null) {
            this.whereConstructStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(132);
        }
        return this.whereConstructStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getWhereConstructStmtType_Mixed() {
        return (EAttribute) this.getWhereConstructStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getWhereConstructStmtType_MaskE() {
        return (EReference) this.getWhereConstructStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getWhereStmtType() {
        if (this.whereStmtTypeEClass == null) {
            this.whereStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(133);
        }
        return this.whereStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getWhereStmtType_Mixed() {
        return (EAttribute) this.getWhereStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getWhereStmtType_Group() {
        return (EAttribute) this.getWhereStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getWhereStmtType_ActionStmt() {
        return (EReference) this.getWhereStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getWhereStmtType_Cnt() {
        return (EAttribute) this.getWhereStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getWhereStmtType_MaskE() {
        return (EReference) this.getWhereStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EClass getWriteStmtType() {
        if (this.writeStmtTypeEClass == null) {
            this.writeStmtTypeEClass = (EClass) EPackage.Registry.INSTANCE.getEPackage(FxtranPackage.eNS_URI)
                    .getEClassifiers().get(134);
        }
        return this.writeStmtTypeEClass;
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getWriteStmtType_Mixed() {
        return (EAttribute) this.getWriteStmtType().getEStructuralFeatures().get(0);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getWriteStmtType_Group() {
        return (EAttribute) this.getWriteStmtType().getEStructuralFeatures().get(1);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EAttribute getWriteStmtType_Cnt() {
        return (EAttribute) this.getWriteStmtType().getEStructuralFeatures().get(2);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getWriteStmtType_IoControlSpec() {
        return (EReference) this.getWriteStmtType().getEStructuralFeatures().get(3);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public EReference getWriteStmtType_OutputItemLT() {
        return (EReference) this.getWriteStmtType().getEStructuralFeatures().get(4);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    @Override
    public FxtranFactory getFxtranFactory() {
        return (FxtranFactory) this.getEFactoryInstance();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isLoaded = false;

    /**
     * Laods the package and any sub-packages from their serialized form. <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated
     */
    public void loadPackage() {
        if (this.isLoaded) {
            return;
        }
        this.isLoaded = true;

        final URL url = this.getClass().getResource(this.packageFilename);
        if (url == null) {
            throw new RuntimeException("Missing serialized package: " + this.packageFilename);
        }
        final URI uri = URI.createURI(url.toString());
        final Resource resource = new EcoreResourceFactoryImpl().createResource(uri);
        try {
            resource.load(null);
        } catch (final IOException exception) {
            throw new WrappedException(exception);
        }
        this.initializeFromLoadedEPackage(this, (EPackage) resource.getContents().get(0));
        this.createResource(eNS_URI);
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    private boolean isFixed = false;

    /**
     * Fixes up the loaded package, to make it appear as if it had been programmatically built. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     *
     * @generated
     */
    public void fixPackageContents() {
        if (this.isFixed) {
            return;
        }
        this.isFixed = true;
        this.fixEClassifiers();
    }

    /**
     * Sets the instance class on the given classifier. <!-- begin-user-doc --> <!-- end-user-doc
     * -->
     *
     * @generated
     */
    @Override
    protected void fixInstanceClass(final EClassifier eClassifier) {
        if (eClassifier.getInstanceClassName() == null) {
            eClassifier.setInstanceClassName("org.oceandsl.tools.sar.fxtran." + eClassifier.getName());
            this.setGeneratedClassName(eClassifier);
        }
    }

} // FxtranPackageImpl
