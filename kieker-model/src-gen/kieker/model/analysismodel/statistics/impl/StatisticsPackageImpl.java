/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Map;

import kieker.model.analysismodel.AnalysismodelPackage;

import kieker.model.analysismodel.assembly.AssemblyPackage;

import kieker.model.analysismodel.assembly.impl.AssemblyPackageImpl;

import kieker.model.analysismodel.deployment.DeploymentPackage;

import kieker.model.analysismodel.deployment.impl.DeploymentPackageImpl;

import kieker.model.analysismodel.execution.ExecutionPackage;

import kieker.model.analysismodel.execution.impl.ExecutionPackageImpl;

import kieker.model.analysismodel.impl.AnalysismodelPackageImpl;

import kieker.model.analysismodel.sources.SourcesPackage;

import kieker.model.analysismodel.sources.impl.SourcesPackageImpl;

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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.impl.EPackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StatisticsPackageImpl extends EPackageImpl implements StatisticsPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statisticRecordEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass ePropertyTypeToValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass measurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass scalarMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass vectorMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass intMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass longMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass floatMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass doubleMeasurementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass unitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass composedUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass simpleUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass siUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass customUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum esiUnitTypeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum ePrefixEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass statisticsModelEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eObjectToStatisticsMapEntryEClass = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
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
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link StatisticsPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static StatisticsPackage init() {
		if (isInited) return (StatisticsPackage)EPackage.Registry.INSTANCE.getEPackage(StatisticsPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredStatisticsPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		StatisticsPackageImpl theStatisticsPackage = registeredStatisticsPackage instanceof StatisticsPackageImpl ? (StatisticsPackageImpl)registeredStatisticsPackage : new StatisticsPackageImpl();

		isInited = true;

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);
		AnalysismodelPackageImpl theAnalysismodelPackage = (AnalysismodelPackageImpl)(registeredPackage instanceof AnalysismodelPackageImpl ? registeredPackage : AnalysismodelPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TypePackage.eNS_URI);
		TypePackageImpl theTypePackage = (TypePackageImpl)(registeredPackage instanceof TypePackageImpl ? registeredPackage : TypePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(AssemblyPackage.eNS_URI);
		AssemblyPackageImpl theAssemblyPackage = (AssemblyPackageImpl)(registeredPackage instanceof AssemblyPackageImpl ? registeredPackage : AssemblyPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(DeploymentPackage.eNS_URI);
		DeploymentPackageImpl theDeploymentPackage = (DeploymentPackageImpl)(registeredPackage instanceof DeploymentPackageImpl ? registeredPackage : DeploymentPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ExecutionPackage.eNS_URI);
		ExecutionPackageImpl theExecutionPackage = (ExecutionPackageImpl)(registeredPackage instanceof ExecutionPackageImpl ? registeredPackage : ExecutionPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		TracePackageImpl theTracePackage = (TracePackageImpl)(registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(SourcesPackage.eNS_URI);
		SourcesPackageImpl theSourcesPackage = (SourcesPackageImpl)(registeredPackage instanceof SourcesPackageImpl ? registeredPackage : SourcesPackage.eINSTANCE);

		// Create package meta-data objects
		theStatisticsPackage.createPackageContents();
		theAnalysismodelPackage.createPackageContents();
		theTypePackage.createPackageContents();
		theAssemblyPackage.createPackageContents();
		theDeploymentPackage.createPackageContents();
		theExecutionPackage.createPackageContents();
		theTracePackage.createPackageContents();
		theSourcesPackage.createPackageContents();

		// Initialize created meta-data
		theStatisticsPackage.initializePackageContents();
		theAnalysismodelPackage.initializePackageContents();
		theTypePackage.initializePackageContents();
		theAssemblyPackage.initializePackageContents();
		theDeploymentPackage.initializePackageContents();
		theExecutionPackage.initializePackageContents();
		theTracePackage.initializePackageContents();
		theSourcesPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theStatisticsPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(StatisticsPackage.eNS_URI, theStatisticsPackage);
		return theStatisticsPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStatisticRecord() {
		return statisticRecordEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getStatisticRecord_Properties() {
		return (EReference)statisticRecordEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEPropertyTypeToValue() {
		return ePropertyTypeToValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEPropertyTypeToValue_Key() {
		return (EAttribute)ePropertyTypeToValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEPropertyTypeToValue_Value() {
		return (EAttribute)ePropertyTypeToValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMeasurement() {
		return measurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMeasurement_Timestamp() {
		return (EAttribute)measurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getScalarMeasurement() {
		return scalarMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getScalarMeasurement_Unit() {
		return (EReference)scalarMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVectorMeasurement() {
		return vectorMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVectorMeasurement_Values() {
		return (EReference)vectorMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIntMeasurement() {
		return intMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getIntMeasurement_Value() {
		return (EAttribute)intMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLongMeasurement() {
		return longMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getLongMeasurement_Value() {
		return (EAttribute)longMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getFloatMeasurement() {
		return floatMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getFloatMeasurement_Value() {
		return (EAttribute)floatMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDoubleMeasurement() {
		return doubleMeasurementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDoubleMeasurement_Value() {
		return (EAttribute)doubleMeasurementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getUnit() {
		return unitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getComposedUnit() {
		return composedUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getComposedUnit_Units() {
		return (EReference)composedUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getComposedUnit_Exponent() {
		return (EAttribute)composedUnitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSimpleUnit() {
		return simpleUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSimpleUnit_Prefix() {
		return (EAttribute)simpleUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSIUnit() {
		return siUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSIUnit_UnitType() {
		return (EAttribute)siUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCustomUnit() {
		return customUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCustomUnit_Name() {
		return (EAttribute)customUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getESIUnitType() {
		return esiUnitTypeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getEPrefix() {
		return ePrefixEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getStatisticsModel() {
		return statisticsModelEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getStatisticsModel_Statistics() {
		return (EReference)statisticsModelEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getStatisticsModel_Units() {
		return (EReference)statisticsModelEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEObjectToStatisticsMapEntry() {
		return eObjectToStatisticsMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEObjectToStatisticsMapEntry_Value() {
		return (EReference)eObjectToStatisticsMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEObjectToStatisticsMapEntry_Key() {
		return (EReference)eObjectToStatisticsMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StatisticsFactory getStatisticsFactory() {
		return (StatisticsFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		statisticRecordEClass = createEClass(STATISTIC_RECORD);
		createEReference(statisticRecordEClass, STATISTIC_RECORD__PROPERTIES);

		ePropertyTypeToValueEClass = createEClass(EPROPERTY_TYPE_TO_VALUE);
		createEAttribute(ePropertyTypeToValueEClass, EPROPERTY_TYPE_TO_VALUE__KEY);
		createEAttribute(ePropertyTypeToValueEClass, EPROPERTY_TYPE_TO_VALUE__VALUE);

		measurementEClass = createEClass(MEASUREMENT);
		createEAttribute(measurementEClass, MEASUREMENT__TIMESTAMP);

		scalarMeasurementEClass = createEClass(SCALAR_MEASUREMENT);
		createEReference(scalarMeasurementEClass, SCALAR_MEASUREMENT__UNIT);

		vectorMeasurementEClass = createEClass(VECTOR_MEASUREMENT);
		createEReference(vectorMeasurementEClass, VECTOR_MEASUREMENT__VALUES);

		intMeasurementEClass = createEClass(INT_MEASUREMENT);
		createEAttribute(intMeasurementEClass, INT_MEASUREMENT__VALUE);

		longMeasurementEClass = createEClass(LONG_MEASUREMENT);
		createEAttribute(longMeasurementEClass, LONG_MEASUREMENT__VALUE);

		floatMeasurementEClass = createEClass(FLOAT_MEASUREMENT);
		createEAttribute(floatMeasurementEClass, FLOAT_MEASUREMENT__VALUE);

		doubleMeasurementEClass = createEClass(DOUBLE_MEASUREMENT);
		createEAttribute(doubleMeasurementEClass, DOUBLE_MEASUREMENT__VALUE);

		statisticsModelEClass = createEClass(STATISTICS_MODEL);
		createEReference(statisticsModelEClass, STATISTICS_MODEL__STATISTICS);
		createEReference(statisticsModelEClass, STATISTICS_MODEL__UNITS);

		eObjectToStatisticsMapEntryEClass = createEClass(EOBJECT_TO_STATISTICS_MAP_ENTRY);
		createEReference(eObjectToStatisticsMapEntryEClass, EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE);
		createEReference(eObjectToStatisticsMapEntryEClass, EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY);

		unitEClass = createEClass(UNIT);

		composedUnitEClass = createEClass(COMPOSED_UNIT);
		createEReference(composedUnitEClass, COMPOSED_UNIT__UNITS);
		createEAttribute(composedUnitEClass, COMPOSED_UNIT__EXPONENT);

		simpleUnitEClass = createEClass(SIMPLE_UNIT);
		createEAttribute(simpleUnitEClass, SIMPLE_UNIT__PREFIX);

		siUnitEClass = createEClass(SI_UNIT);
		createEAttribute(siUnitEClass, SI_UNIT__UNIT_TYPE);

		customUnitEClass = createEClass(CUSTOM_UNIT);
		createEAttribute(customUnitEClass, CUSTOM_UNIT__NAME);

		// Create enums
		esiUnitTypeEEnum = createEEnum(ESI_UNIT_TYPE);
		ePrefixEEnum = createEEnum(EPREFIX);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		AnalysismodelPackage theAnalysismodelPackage = (AnalysismodelPackage)EPackage.Registry.INSTANCE.getEPackage(AnalysismodelPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		scalarMeasurementEClass.getESuperTypes().add(this.getMeasurement());
		vectorMeasurementEClass.getESuperTypes().add(this.getMeasurement());
		intMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		longMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		floatMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		doubleMeasurementEClass.getESuperTypes().add(this.getScalarMeasurement());
		simpleUnitEClass.getESuperTypes().add(this.getUnit());
		siUnitEClass.getESuperTypes().add(this.getSimpleUnit());
		customUnitEClass.getESuperTypes().add(this.getSimpleUnit());

		// Initialize classes, features, and operations; add parameters
		initEClass(statisticRecordEClass, StatisticRecord.class, "StatisticRecord", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStatisticRecord_Properties(), this.getEPropertyTypeToValue(), null, "properties", null, 0, -1, StatisticRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(ePropertyTypeToValueEClass, Map.Entry.class, "EPropertyTypeToValue", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEPropertyTypeToValue_Key(), ecorePackage.getEString(), "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEPropertyTypeToValue_Value(), ecorePackage.getEJavaObject(), "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(measurementEClass, Measurement.class, "Measurement", IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMeasurement_Timestamp(), theAnalysismodelPackage.getInstant(), "timestamp", null, 0, 1, Measurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(scalarMeasurementEClass, ScalarMeasurement.class, "ScalarMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getScalarMeasurement_Unit(), this.getUnit(), null, "unit", null, 0, 1, ScalarMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(vectorMeasurementEClass, VectorMeasurement.class, "VectorMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getVectorMeasurement_Values(), this.getScalarMeasurement(), null, "values", null, 0, -1, VectorMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(intMeasurementEClass, IntMeasurement.class, "IntMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getIntMeasurement_Value(), ecorePackage.getEInt(), "value", null, 0, 1, IntMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(longMeasurementEClass, LongMeasurement.class, "LongMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getLongMeasurement_Value(), ecorePackage.getELong(), "value", null, 0, 1, LongMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(floatMeasurementEClass, FloatMeasurement.class, "FloatMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getFloatMeasurement_Value(), ecorePackage.getEFloat(), "value", null, 0, 1, FloatMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(doubleMeasurementEClass, DoubleMeasurement.class, "DoubleMeasurement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDoubleMeasurement_Value(), ecorePackage.getEDouble(), "value", null, 0, 1, DoubleMeasurement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(statisticsModelEClass, StatisticsModel.class, "StatisticsModel", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getStatisticsModel_Statistics(), this.getEObjectToStatisticsMapEntry(), null, "statistics", null, 0, -1, StatisticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getStatisticsModel_Units(), this.getUnit(), null, "units", null, 0, -1, StatisticsModel.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eObjectToStatisticsMapEntryEClass, Map.Entry.class, "EObjectToStatisticsMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEObjectToStatisticsMapEntry_Value(), this.getStatisticRecord(), null, "value", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEObjectToStatisticsMapEntry_Key(), ecorePackage.getEObject(), null, "key", null, 0, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(unitEClass, Unit.class, "Unit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(composedUnitEClass, ComposedUnit.class, "ComposedUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getComposedUnit_Units(), this.getUnit(), null, "units", null, 0, -1, ComposedUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getComposedUnit_Exponent(), ecorePackage.getELong(), "exponent", null, 1, 1, ComposedUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(simpleUnitEClass, SimpleUnit.class, "SimpleUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSimpleUnit_Prefix(), this.getEPrefix(), "prefix", null, 0, 1, SimpleUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(siUnitEClass, SIUnit.class, "SIUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSIUnit_UnitType(), this.getESIUnitType(), "unitType", null, 1, 1, SIUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(customUnitEClass, CustomUnit.class, "CustomUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCustomUnit_Name(), ecorePackage.getEString(), "name", null, 1, 1, CustomUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(esiUnitTypeEEnum, ESIUnitType.class, "ESIUnitType");
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.METER);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.GRAM);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.TON);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.SECOND);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.AMPERE);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.KELVIN);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.MOLE);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.CANDELA);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.PASCAL);
		addEEnumLiteral(esiUnitTypeEEnum, ESIUnitType.JOUL);

		initEEnum(ePrefixEEnum, EPrefix.class, "EPrefix");
		addEEnumLiteral(ePrefixEEnum, EPrefix.NO_P);
		addEEnumLiteral(ePrefixEEnum, EPrefix.YOTTA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.ZETTA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.EXA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.PETA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.TERA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.GIGA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.MEGA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.KILO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.HECTO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.DECA);
		addEEnumLiteral(ePrefixEEnum, EPrefix.DECI);
		addEEnumLiteral(ePrefixEEnum, EPrefix.CENTI);
		addEEnumLiteral(ePrefixEEnum, EPrefix.MILI);
		addEEnumLiteral(ePrefixEEnum, EPrefix.MICRO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.NANO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.PICO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.FEMTO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.ATTO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.ZEPTO);
		addEEnumLiteral(ePrefixEEnum, EPrefix.YOCTO);
	}

} //StatisticsPackageImpl
