/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

import kieker.model.analysismodel.AnalysismodelPackage;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.impl.ExecutionPackageImpl;
import kieker.model.analysismodel.impl.AnalysismodelPackageImpl;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.source.impl.SourcePackageImpl;
import kieker.model.analysismodel.statistics.ComposedUnit;
import kieker.model.analysismodel.statistics.CustomUnit;
import kieker.model.analysismodel.statistics.DoubleMeasurement;
import kieker.model.analysismodel.statistics.EPrefix;
import kieker.model.analysismodel.statistics.ESIUnitType;
import kieker.model.analysismodel.statistics.FloatMeasurement;
import kieker.model.analysismodel.statistics.IntMeasurement;
import kieker.model.analysismodel.statistics.LongMeasurement;
import kieker.model.analysismodel.statistics.Measurement;
import kieker.model.analysismodel.statistics.SIUnit;
import kieker.model.analysismodel.statistics.ScalarMeasurement;
import kieker.model.analysismodel.statistics.SimpleUnit;
import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.Unit;
import kieker.model.analysismodel.statistics.VectorMeasurement;
import kieker.model.analysismodel.trace.TracePackage;
import kieker.model.analysismodel.trace.impl.TracePackageImpl;
import kieker.model.analysismodel.type.TypePackage;
import kieker.model.analysismodel.type.impl.TypePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class StatisticsPackageImpl extends EPackageImpl implements StatisticsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass statisticRecordEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass ePropertyTypeToValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass measurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass scalarMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass vectorMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass intMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass longMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass floatMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass doubleMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass unitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass composedUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass simpleUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass siUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass customUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EEnum esiUnitTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EEnum ePrefixEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass statisticsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EClass eObjectToStatisticsMapEntryEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>
	 * Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see kieker.model.analysismodel.statistics.StatisticsPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private StatisticsPackageImpl() {
		super(eNS_URI, StatisticsFactory.eINSTANCE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>
	 * This method is used to initialize {@link StatisticsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static StatisticsPackage init() {
		if (isInited) {
			return (StatisticsPackage) EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);
		}

		// Obtain or create and register package
		final Object registeredStatisticsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		final StatisticsPackageImpl theStatisticsPackage = registeredStatisticsPackage instanceof StatisticsPackageImpl
				? (StatisticsPackageImpl) registeredStatisticsPackage
				: new StatisticsPackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);
		final AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl) (registeredPackage instanceof AnalysismodelPackageImpl
				? registeredPackage
				: AnalysismodelPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		final TypePackageImpl theTypePackage = (TypePackageImpl) (registeredPackage instanceof TypePackageImpl ? registeredPackage : TypePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		final AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl) (registeredPackage instanceof AssemblyPackageImpl ? registeredPackage
				: AssemblyPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		final DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl) (registeredPackage instanceof DeploymentPackageImpl ? registeredPackage
				: DeploymentPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		final ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl) (registeredPackage instanceof ExecutionPackageImpl ? registeredPackage
				: ExecutionPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		final TracePackageImpl theTracePackage = (TracePackageImpl) (registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcePackage.eNS_URI);
		final SourcePackageImpl theSourcePackage = (SourcePackageImpl) (registeredPackage instanceof SourcePackageImpl ? registeredPackage
				: SourcePackage.eINSTANCE);

		// Create package meta-data objects
		theStatisticsPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcePackage.createPackageContents();

		// Initialize created meta-data
		theStatisticsPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theStatisticsPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(StatisticsPackage.eNS_URI, theStatisticsPackage);
		return theStatisticsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getStatisticRecord() {
		return this.statisticRecordEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getStatisticRecord_Properties() {
		return (EReference) this.statisticRecordEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEPropertyTypeToValue() {
		return this.ePropertyTypeToValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEPropertyTypeToValue_Key() {
		return (EAttribute) this.ePropertyTypeToValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getEPropertyTypeToValue_Value() {
		return (EAttribute) this.ePropertyTypeToValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getMeasurement() {
		return this.measurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getMeasurement_Timestamp() {
		return (EAttribute) this.measurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getScalarMeasurement() {
		return this.scalarMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getScalarMeasurement_Unit() {
		return (EReference) this.scalarMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getVectorMeasurement() {
		return this.vectorMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getVectorMeasurement_Values() {
		return (EReference) this.vectorMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getIntMeasurement() {
		return this.intMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getIntMeasurement_Value() {
		return (EAttribute) this.intMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getLongMeasurement() {
		return this.longMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getLongMeasurement_Value() {
		return (EAttribute) this.longMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getFloatMeasurement() {
		return this.floatMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getFloatMeasurement_Value() {
		return (EAttribute) this.floatMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getDoubleMeasurement() {
		return this.doubleMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getDoubleMeasurement_Value() {
		return (EAttribute) this.doubleMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getUnit() {
		return this.unitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getComposedUnit() {
		return this.composedUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getComposedUnit_Units() {
		return (EReference) this.composedUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getComposedUnit_Exponent() {
		return (EAttribute) this.composedUnitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getSimpleUnit() {
		return this.simpleUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getSimpleUnit_Prefix() {
		return (EAttribute) this.simpleUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getSIUnit() {
		return this.siUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getSIUnit_UnitType() {
		return (EAttribute) this.siUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getCustomUnit() {
		return this.customUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EAttribute getCustomUnit_Name() {
		return (EAttribute) this.customUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EEnum getESIUnitType() {
		return this.esiUnitTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EEnum getEPrefix() {
		return this.ePrefixEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getStatisticsModel() {
		return this.statisticsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getStatisticsModel_Statistics() {
		return (EReference) this.statisticsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getStatisticsModel_Units() {
		return (EReference) this.statisticsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EClass getEObjectToStatisticsMapEntry() {
		return this.eObjectToStatisticsMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEObjectToStatisticsMapEntry_Value() {
		return (EReference) this.eObjectToStatisticsMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EReference getEObjectToStatisticsMapEntry_Key() {
		return (EReference) this.eObjectToStatisticsMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StatisticsFactory getStatisticsFactory() {
		return (StatisticsFactory) this.getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package. This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void createPackageContents() {
		if (this.isCreated) {
			return;
		}
		this.isCreated = true;

		// Create classes and their features
		this.statisticRecordEClass = this.createEClass(STATISTIC_RECORD);
		this.createEReference(this.statisticRecordEClass, STATISTIC_RECORD__PROPERTIES);

		this.ePropertyTypeToValueEClass = this.createEClass(EPROPERTY_TYPE_TO_VALUE);
		this.createEAttribute(this.ePropertyTypeToValueEClass, EPROPERTY_TYPE_TO_VALUE__KEY);
		this.createEAttribute(this.ePropertyTypeToValueEClass, EPROPERTY_TYPE_TO_VALUE__VALUE);

		this.measurementEClass = this.createEClass(MEASUREMENT);
		this.createEAttribute(this.measurementEClass, MEASUREMENT__TIMESTAMP);

		this.scalarMeasurementEClass = this.createEClass(SCALAR_MEASUREMENT);
		this.createEReference(this.scalarMeasurementEClass, SCALAR_MEASUREMENT__UNIT);

		this.vectorMeasurementEClass = this.createEClass(VECTOR_MEASUREMENT);
		this.createEReference(this.vectorMeasurementEClass, VECTOR_MEASUREMENT__VALUES);

		this.intMeasurementEClass = this.createEClass(INT_MEASUREMENT);
		this.createEAttribute(this.intMeasurementEClass, INT_MEASUREMENT__VALUE);

		this.longMeasurementEClass = this.createEClass(LONG_MEASUREMENT);
		this.createEAttribute(this.longMeasurementEClass, LONG_MEASUREMENT__VALUE);

		this.floatMeasurementEClass = this.createEClass(FLOAT_MEASUREMENT);
		this.createEAttribute(this.floatMeasurementEClass, FLOAT_MEASUREMENT__VALUE);

		this.doubleMeasurementEClass = this.createEClass(DOUBLE_MEASUREMENT);
		this.createEAttribute(this.doubleMeasurementEClass, DOUBLE_MEASUREMENT__VALUE);

		this.statisticsModelEClass = this.createEClass(STATISTICS_MODEL);
		this.createEReference(this.statisticsModelEClass, STATISTICS_MODEL__STATISTICS);
		this.createEReference(this.statisticsModelEClass, STATISTICS_MODEL__UNITS);

		this.eObjectToStatisticsMapEntryEClass = this.createEClass(EOBJECT_TO_STATISTICS_MAP_ENTRY);
		this.createEReference(this.eObjectToStatisticsMapEntryEClass, EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE);
		this.createEReference(this.eObjectToStatisticsMapEntryEClass, EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY);

		this.unitEClass = this.createEClass(UNIT);

		this.composedUnitEClass = this.createEClass(COMPOSED_UNIT);
		this.createEReference(this.composedUnitEClass, COMPOSED_UNIT__UNITS);
		this.createEAttribute(this.composedUnitEClass, COMPOSED_UNIT__EXPONENT);

		this.simpleUnitEClass = this.createEClass(SIMPLE_UNIT);
		this.createEAttribute(this.simpleUnitEClass, SIMPLE_UNIT__PREFIX);

		this.siUnitEClass = this.createEClass(SI_UNIT);
		this.createEAttribute(this.siUnitEClass, SI_UNIT__UNIT_TYPE);

		this.customUnitEClass = this.createEClass(CUSTOM_UNIT);
		this.createEAttribute(this.customUnitEClass, CUSTOM_UNIT__NAME);

		// Create enums
		this.esiUnitTypeEEnum = this.createEEnum(ESI_UNIT_TYPE);
		this.ePrefixEEnum = this.createEEnum(EPREFIX);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model. This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public void initializePackageContents() {
		if (this.isInitialized) {
			return;
		}
		this.isInitialized = true;

		// Initialize package
		this.setName(eNAME);
		this.setNsPrefix(eNS_PREFIX);
		this.setNsURI(eNS_URI);

		// Obtain other dependent packages
		final AnalysismodelPackage theAnalysismodelPackage = (AnalysismodelPackage) EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		this.scalarMeasurementEClass.getESuperTypes().add(this.getMeasurement());
		this.vectorMeasurementEClass.getESuperTypes().add(this.getMeasurement());
		this.intMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		this.longMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		this.floatMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		this.doubleMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		this.simpleUnitEClass.getESuperTypes().add(this.getUnit());
		this.siUnitEClass.getESuperTypes().add(this.getSimpleUnit());
		this.customUnitEClass.getESuperTypes().add(this.getSimpleUnit());

		// Initialize classes, features, and operations; add parameters
		this.initEClass(this.statisticRecordEClass, StatisticRecord.class, "StatisticRecord", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getStatisticRecord_Properties(), this.getEPropertyTypeToValue(), null, "properties", null, 0, -1, StatisticRecord.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.ePropertyTypeToValueEClass, Map.Entry.class, "EPropertyTypeToValue", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getEPropertyTypeToValue_Key(), this.ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getEPropertyTypeToValue_Value(), this.ecorePackage.getEJavaObject(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.measurementEClass, Measurement.class, "Measurement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getMeasurement_Timestamp(), theAnalysismodelPackage.getInstant(), "timestamp", null, 0, 1, Measurement.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.scalarMeasurementEClass, ScalarMeasurement.class, "ScalarMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getScalarMeasurement_Unit(), this.getUnit(), null, "unit", null, 0, 1, ScalarMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.vectorMeasurementEClass, VectorMeasurement.class, "VectorMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getVectorMeasurement_Values(), this.getScalarMeasurement(), null, "values", null, 0, -1, VectorMeasurement.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.intMeasurementEClass, IntMeasurement.class, "IntMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getIntMeasurement_Value(), this.ecorePackage.getEInt(), "value", null, 0, 1, IntMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.longMeasurementEClass, LongMeasurement.class, "LongMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getLongMeasurement_Value(), this.ecorePackage.getELong(), "value", null, 0, 1, LongMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.floatMeasurementEClass, FloatMeasurement.class, "FloatMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getFloatMeasurement_Value(), this.ecorePackage.getEFloat(), "value", null, 0, 1, FloatMeasurement.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.doubleMeasurementEClass, DoubleMeasurement.class, "DoubleMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getDoubleMeasurement_Value(), this.ecorePackage.getEDouble(), "value", null, 0, 1, DoubleMeasurement.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.statisticsModelEClass, StatisticsModel.class, "StatisticsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getStatisticsModel_Statistics(), this.getEObjectToStatisticsMapEntry(), null, "statistics", null, 0, -1, StatisticsModel.class,
				!IS_TRANSIENT,
				!IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getStatisticsModel_Units(), this.getUnit(), null, "units", null, 0, -1, StatisticsModel.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.eObjectToStatisticsMapEntryEClass, Map.Entry.class, "EObjectToStatisticsMapEntry", !IS_ABSTRACT, !IS_INTERFACE,
				!IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getEObjectToStatisticsMapEntry_Value(), this.getStatisticRecord(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEReference(this.getEObjectToStatisticsMapEntry_Key(), this.ecorePackage.getEObject(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT,
				!IS_VOLATILE,
				IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.unitEClass, Unit.class, "Unit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		this.initEClass(this.composedUnitEClass, ComposedUnit.class, "ComposedUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEReference(this.getComposedUnit_Units(), this.getUnit(), null, "units", null, 0, -1, ComposedUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		this.initEAttribute(this.getComposedUnit_Exponent(), this.ecorePackage.getELong(), "exponent", null, 1, 1, ComposedUnit.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.simpleUnitEClass, SimpleUnit.class, "SimpleUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getSimpleUnit_Prefix(), this.getEPrefix(), "prefix", null, 0, 1, SimpleUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.siUnitEClass, SIUnit.class, "SIUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getSIUnit_UnitType(), this.getESIUnitType(), "unitType", null, 1, 1, SIUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE,
				!IS_UNSETTABLE,
				!IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		this.initEClass(this.customUnitEClass, CustomUnit.class, "CustomUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		this.initEAttribute(this.getCustomUnit_Name(), this.ecorePackage.getEString(), "name", null, 1, 1, CustomUnit.class, !IS_TRANSIENT, !IS_VOLATILE,
				IS_CHANGEABLE,
				!IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		this.initEEnum(this.esiUnitTypeEEnum, ESIUnitType.class, "ESIUnitType");
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.METER);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.GRAM);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.TON);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.SECOND);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.AMPERE);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.KELVIN);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.MOLE);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.CANDELA);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.PASCAL);
		this.addEEnumLiteral(this.esiUnitTypeEEnum, ESIUnitType.JOUL);

		this.initEEnum(this.ePrefixEEnum, EPrefix.class, "EPrefix");
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.NO_P);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.YOTTA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.ZETTA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.EXA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.PETA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.TERA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.GIGA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.MEGA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.KILO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.HECTO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.DECA);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.DECI);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.CENTI);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.MILI);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.MICRO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.NANO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.PICO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.FEMTO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.ATTO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.ZEPTO);
		this.addEEnumLiteral(this.ePrefixEEnum, EPrefix.YOCTO);
	}

} // StatisticsPackageImpl
