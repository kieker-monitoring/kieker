/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Map;

import kieker.model.analysismodel.statistics.*;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class StatisticsFactoryImpl extends EFactoryImpl implements StatisticsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static StatisticsFactory init() {
		try {
			StatisticsFactory theStatisticsFactory = (StatisticsFactory)EPackage.Registry.INSTANCE.getEFactory(StatisticsPackage.eNS_URI);
			if (theStatisticsFactory != null) {
				return theStatisticsFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new StatisticsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatisticsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case StatisticsPackage.STATISTIC_RECORD: return createStatisticRecord();
			case StatisticsPackage.EPROPERTY_TYPE_TO_VALUE: return (EObject)createEPropertyTypeToValue();
			case StatisticsPackage.SCALAR_MEASUREMENT: return createScalarMeasurement();
			case StatisticsPackage.VECTOR_MEASUREMENT: return createVectorMeasurement();
			case StatisticsPackage.INT_MEASUREMENT: return createIntMeasurement();
			case StatisticsPackage.LONG_MEASUREMENT: return createLongMeasurement();
			case StatisticsPackage.FLOAT_MEASUREMENT: return createFloatMeasurement();
			case StatisticsPackage.DOUBLE_MEASUREMENT: return createDoubleMeasurement();
			case StatisticsPackage.STATISTICS_MODEL: return createStatisticsModel();
			case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY: return (EObject)createEObjectToStatisticsMapEntry();
			case StatisticsPackage.UNIT: return createUnit();
			case StatisticsPackage.COMPOSED_UNIT: return createComposedUnit();
			case StatisticsPackage.SIMPLE_UNIT: return createSimpleUnit();
			case StatisticsPackage.SI_UNIT: return createSIUnit();
			case StatisticsPackage.CUSTOM_UNIT: return createCustomUnit();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case StatisticsPackage.ESI_UNIT_TYPE:
				return createESIUnitTypeFromString(eDataType, initialValue);
			case StatisticsPackage.EPREFIX:
				return createEPrefixFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case StatisticsPackage.ESI_UNIT_TYPE:
				return convertESIUnitTypeToString(eDataType, instanceValue);
			case StatisticsPackage.EPREFIX:
				return convertEPrefixToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StatisticRecord createStatisticRecord() {
		StatisticRecordImpl statisticRecord = new StatisticRecordImpl();
		return statisticRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<String, Object> createEPropertyTypeToValue() {
		EPropertyTypeToValueImpl ePropertyTypeToValue = new EPropertyTypeToValueImpl();
		return ePropertyTypeToValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ScalarMeasurement createScalarMeasurement() {
		ScalarMeasurementImpl scalarMeasurement = new ScalarMeasurementImpl();
		return scalarMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VectorMeasurement createVectorMeasurement() {
		VectorMeasurementImpl vectorMeasurement = new VectorMeasurementImpl();
		return vectorMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IntMeasurement createIntMeasurement() {
		IntMeasurementImpl intMeasurement = new IntMeasurementImpl();
		return intMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LongMeasurement createLongMeasurement() {
		LongMeasurementImpl longMeasurement = new LongMeasurementImpl();
		return longMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FloatMeasurement createFloatMeasurement() {
		FloatMeasurementImpl floatMeasurement = new FloatMeasurementImpl();
		return floatMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DoubleMeasurement createDoubleMeasurement() {
		DoubleMeasurementImpl doubleMeasurement = new DoubleMeasurementImpl();
		return doubleMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StatisticsModel createStatisticsModel() {
		StatisticsModelImpl statisticsModel = new StatisticsModelImpl();
		return statisticsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EObject, StatisticRecord> createEObjectToStatisticsMapEntry() {
		EObjectToStatisticsMapEntryImpl eObjectToStatisticsMapEntry = new EObjectToStatisticsMapEntryImpl();
		return eObjectToStatisticsMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Unit createUnit() {
		UnitImpl unit = new UnitImpl();
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ComposedUnit createComposedUnit() {
		ComposedUnitImpl composedUnit = new ComposedUnitImpl();
		return composedUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SimpleUnit createSimpleUnit() {
		SimpleUnitImpl simpleUnit = new SimpleUnitImpl();
		return simpleUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SIUnit createSIUnit() {
		SIUnitImpl siUnit = new SIUnitImpl();
		return siUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CustomUnit createCustomUnit() {
		CustomUnitImpl customUnit = new CustomUnitImpl();
		return customUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public ESIUnitType createESIUnitTypeFromString(EDataType eDataType, String initialValue) {
		ESIUnitType result = ESIUnitType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertESIUnitTypeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPrefix createEPrefixFromString(EDataType eDataType, String initialValue) {
		EPrefix result = EPrefix.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEPrefixToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public StatisticsPackage getStatisticsPackage() {
		return (StatisticsPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static StatisticsPackage getPackage() {
		return StatisticsPackage.eINSTANCE;
	}

} //StatisticsFactoryImpl
