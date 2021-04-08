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
			case StatisticsPackage.STATISTICS: return createStatistics();
			case StatisticsPackage.UNITS_TO_STATISTICS_MAP_ENTRY: return (EObject)createUnitsToStatisticsMapEntry();
			case StatisticsPackage.STATISTIC_RECORD: return createStatisticRecord();
			case StatisticsPackage.EPROPERTY_TYPE_TO_VALUE: return (EObject)createEPropertyTypeToValue();
			case StatisticsPackage.TIME_SERIES: return createTimeSeries();
			case StatisticsPackage.INT_VALUE: return createIntValue();
			case StatisticsPackage.LONG_VALUE: return createLongValue();
			case StatisticsPackage.FLOAT_VALUE: return createFloatValue();
			case StatisticsPackage.DOUBLE_VALUE: return createDoubleValue();
			case StatisticsPackage.STATISTICS_MODEL: return createStatisticsModel();
			case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY: return (EObject)createEObjectToStatisticsMapEntry();
			case StatisticsPackage.TIME_SERIES_STATISTICS: return createTimeSeriesStatistics();
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
			case StatisticsPackage.EPREDEFINED_UNITS:
				return createEPredefinedUnitsFromString(eDataType, initialValue);
			case StatisticsPackage.EPROPERTY_TYPE:
				return createEPropertyTypeFromString(eDataType, initialValue);
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
			case StatisticsPackage.EPREDEFINED_UNITS:
				return convertEPredefinedUnitsToString(eDataType, instanceValue);
			case StatisticsPackage.EPROPERTY_TYPE:
				return convertEPropertyTypeToString(eDataType, instanceValue);
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
	public Statistics createStatistics() {
		StatisticsImpl statistics = new StatisticsImpl();
		return statistics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<EPredefinedUnits, StatisticRecord> createUnitsToStatisticsMapEntry() {
		UnitsToStatisticsMapEntryImpl unitsToStatisticsMapEntry = new UnitsToStatisticsMapEntryImpl();
		return unitsToStatisticsMapEntry;
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
	public Map.Entry<EPropertyType, Object> createEPropertyTypeToValue() {
		EPropertyTypeToValueImpl ePropertyTypeToValue = new EPropertyTypeToValueImpl();
		return ePropertyTypeToValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public <V extends Value, U extends Unit<V>> TimeSeries<V, U> createTimeSeries() {
		TimeSeriesImpl<V, U> timeSeries = new TimeSeriesImpl<V, U>();
		return timeSeries;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IntValue createIntValue() {
		IntValueImpl intValue = new IntValueImpl();
		return intValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LongValue createLongValue() {
		LongValueImpl longValue = new LongValueImpl();
		return longValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FloatValue createFloatValue() {
		FloatValueImpl floatValue = new FloatValueImpl();
		return floatValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DoubleValue createDoubleValue() {
		DoubleValueImpl doubleValue = new DoubleValueImpl();
		return doubleValue;
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
	public Map.Entry<Object, Statistics> createEObjectToStatisticsMapEntry() {
		EObjectToStatisticsMapEntryImpl eObjectToStatisticsMapEntry = new EObjectToStatisticsMapEntryImpl();
		return eObjectToStatisticsMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TimeSeriesStatistics createTimeSeriesStatistics() {
		TimeSeriesStatisticsImpl timeSeriesStatistics = new TimeSeriesStatisticsImpl();
		return timeSeriesStatistics;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPredefinedUnits createEPredefinedUnitsFromString(EDataType eDataType, String initialValue) {
		EPredefinedUnits result = EPredefinedUnits.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEPredefinedUnitsToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EPropertyType createEPropertyTypeFromString(EDataType eDataType, String initialValue) {
		EPropertyType result = EPropertyType.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEPropertyTypeToString(EDataType eDataType, Object instanceValue) {
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
