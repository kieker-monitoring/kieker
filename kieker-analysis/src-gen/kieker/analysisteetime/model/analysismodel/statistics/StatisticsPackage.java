/**
 */
package kieker.analysisteetime.model.analysismodel.statistics;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 * <li>each class,</li>
 * <li>each feature of each class,</li>
 * <li>each operation of each class,</li>
 * <li>each enum,</li>
 * <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * 
 * @see kieker.analysisteetime.model.analysismodel.statistics.StatisticsFactory
 * @model kind="package"
 * @generated
 */
public interface StatisticsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNAME = "statistics";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/statistics";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	String eNS_PREFIX = "statistics";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	StatisticsPackage eINSTANCE = kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsImpl <em>Statistics</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsImpl
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getStatistics()
	 * @generated
	 */
	int STATISTICS = 0;

	/**
	 * The feature id for the '<em><b>Time Series</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STATISTICS__TIME_SERIES = 0;

	/**
	 * The number of structural features of the '<em>Statistics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STATISTICS_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Statistics</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int STATISTICS_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.TimeSeriesImpl <em>Time Series</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.TimeSeriesImpl
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getTimeSeries()
	 * @generated
	 */
	int TIME_SERIES = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__NAME = 0;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__UNIT = 1;

	/**
	 * The feature id for the '<em><b>Values</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES__VALUES = 2;

	/**
	 * The number of structural features of the '<em>Time Series</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Time Series</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int TIME_SERIES_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.ValueImpl <em>Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.ValueImpl
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getValue()
	 * @generated
	 */
	int VALUE = 2;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VALUE__TIMESTAMP = 0;

	/**
	 * The number of structural features of the '<em>Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VALUE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int VALUE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.IntValueImpl <em>Int Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.IntValueImpl
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getIntValue()
	 * @generated
	 */
	int INT_VALUE = 3;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INT_VALUE__TIMESTAMP = VALUE__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Measurement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INT_VALUE__MEASUREMENT = VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Int Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INT_VALUE_FEATURE_COUNT = VALUE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Int Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int INT_VALUE_OPERATION_COUNT = VALUE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.LongValueImpl <em>Long Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.LongValueImpl
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getLongValue()
	 * @generated
	 */
	int LONG_VALUE = 4;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LONG_VALUE__TIMESTAMP = VALUE__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Measurement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LONG_VALUE__MEASUREMENT = VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Long Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LONG_VALUE_FEATURE_COUNT = VALUE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Long Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int LONG_VALUE_OPERATION_COUNT = VALUE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.FloatValueImpl <em>Float Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.FloatValueImpl
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getFloatValue()
	 * @generated
	 */
	int FLOAT_VALUE = 5;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLOAT_VALUE__TIMESTAMP = VALUE__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Measurement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLOAT_VALUE__MEASUREMENT = VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Float Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLOAT_VALUE_FEATURE_COUNT = VALUE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Float Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int FLOAT_VALUE_OPERATION_COUNT = VALUE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.DoubleValueImpl <em>Double Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.DoubleValueImpl
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getDoubleValue()
	 * @generated
	 */
	int DOUBLE_VALUE = 6;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOUBLE_VALUE__TIMESTAMP = VALUE__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Measurement</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOUBLE_VALUE__MEASUREMENT = VALUE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Double Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOUBLE_VALUE_FEATURE_COUNT = VALUE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Double Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int DOUBLE_VALUE_OPERATION_COUNT = VALUE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.Unit <em>Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.Unit
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getUnit()
	 * @generated
	 */
	int UNIT = 7;

	/**
	 * The number of structural features of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UNIT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 * @ordered
	 */
	int UNIT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.analysisteetime.model.analysismodel.statistics.PredefinedUnits <em>Predefined Units</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @see kieker.analysisteetime.model.analysismodel.statistics.PredefinedUnits
	 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getPredefinedUnits()
	 * @generated
	 */
	int PREDEFINED_UNITS = 8;

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.Statistics <em>Statistics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Statistics</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.Statistics
	 * @generated
	 */
	EClass getStatistics();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysisteetime.model.analysismodel.statistics.Statistics#getTimeSeries <em>Time Series</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Time Series</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.Statistics#getTimeSeries()
	 * @see #getStatistics()
	 * @generated
	 */
	EReference getStatistics_TimeSeries();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries <em>Time Series</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Time Series</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.TimeSeries
	 * @generated
	 */
	EClass getTimeSeries();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getName()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EAttribute getTimeSeries_Name();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Unit</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getUnit()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EAttribute getTimeSeries_Unit();

	/**
	 * Returns the meta object for the reference list '{@link kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getValues <em>Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the reference list '<em>Values</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.TimeSeries#getValues()
	 * @see #getTimeSeries()
	 * @generated
	 */
	EReference getTimeSeries_Values();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.Value <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Value</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.Value
	 * @generated
	 */
	EClass getValue();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.statistics.Value#getTimestamp <em>Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Timestamp</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.Value#getTimestamp()
	 * @see #getValue()
	 * @generated
	 */
	EAttribute getValue_Timestamp();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.IntValue <em>Int Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Int Value</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.IntValue
	 * @generated
	 */
	EClass getIntValue();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.statistics.IntValue#getMeasurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Measurement</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.IntValue#getMeasurement()
	 * @see #getIntValue()
	 * @generated
	 */
	EAttribute getIntValue_Measurement();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.LongValue <em>Long Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Long Value</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.LongValue
	 * @generated
	 */
	EClass getLongValue();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.statistics.LongValue#getMeasurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Measurement</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.LongValue#getMeasurement()
	 * @see #getLongValue()
	 * @generated
	 */
	EAttribute getLongValue_Measurement();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.FloatValue <em>Float Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Float Value</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.FloatValue
	 * @generated
	 */
	EClass getFloatValue();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.statistics.FloatValue#getMeasurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Measurement</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.FloatValue#getMeasurement()
	 * @see #getFloatValue()
	 * @generated
	 */
	EAttribute getFloatValue_Measurement();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.DoubleValue <em>Double Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Double Value</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.DoubleValue
	 * @generated
	 */
	EClass getDoubleValue();

	/**
	 * Returns the meta object for the attribute '{@link kieker.analysisteetime.model.analysismodel.statistics.DoubleValue#getMeasurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for the attribute '<em>Measurement</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.DoubleValue#getMeasurement()
	 * @see #getDoubleValue()
	 * @generated
	 */
	EAttribute getDoubleValue_Measurement();

	/**
	 * Returns the meta object for class '{@link kieker.analysisteetime.model.analysismodel.statistics.Unit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for class '<em>Unit</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.Unit
	 * @generated
	 */
	EClass getUnit();

	/**
	 * Returns the meta object for enum '{@link kieker.analysisteetime.model.analysismodel.statistics.PredefinedUnits <em>Predefined Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the meta object for enum '<em>Predefined Units</em>'.
	 * @see kieker.analysisteetime.model.analysismodel.statistics.PredefinedUnits
	 * @generated
	 */
	EEnum getPredefinedUnits();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * 
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	StatisticsFactory getStatisticsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 * <li>each class,</li>
	 * <li>each feature of each class,</li>
	 * <li>each operation of each class,</li>
	 * <li>each enum,</li>
	 * <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * 
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsImpl <em>Statistics</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsImpl
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getStatistics()
		 * @generated
		 */
		EClass STATISTICS = eINSTANCE.getStatistics();

		/**
		 * The meta object literal for the '<em><b>Time Series</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference STATISTICS__TIME_SERIES = eINSTANCE.getStatistics_TimeSeries();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.TimeSeriesImpl <em>Time Series</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.TimeSeriesImpl
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getTimeSeries()
		 * @generated
		 */
		EClass TIME_SERIES = eINSTANCE.getTimeSeries();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TIME_SERIES__NAME = eINSTANCE.getTimeSeries_Name();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute TIME_SERIES__UNIT = eINSTANCE.getTimeSeries_Unit();

		/**
		 * The meta object literal for the '<em><b>Values</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EReference TIME_SERIES__VALUES = eINSTANCE.getTimeSeries_Values();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.ValueImpl <em>Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.ValueImpl
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getValue()
		 * @generated
		 */
		EClass VALUE = eINSTANCE.getValue();

		/**
		 * The meta object literal for the '<em><b>Timestamp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute VALUE__TIMESTAMP = eINSTANCE.getValue_Timestamp();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.IntValueImpl <em>Int Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.IntValueImpl
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getIntValue()
		 * @generated
		 */
		EClass INT_VALUE = eINSTANCE.getIntValue();

		/**
		 * The meta object literal for the '<em><b>Measurement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute INT_VALUE__MEASUREMENT = eINSTANCE.getIntValue_Measurement();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.LongValueImpl <em>Long Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.LongValueImpl
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getLongValue()
		 * @generated
		 */
		EClass LONG_VALUE = eINSTANCE.getLongValue();

		/**
		 * The meta object literal for the '<em><b>Measurement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute LONG_VALUE__MEASUREMENT = eINSTANCE.getLongValue_Measurement();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.FloatValueImpl <em>Float Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.FloatValueImpl
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getFloatValue()
		 * @generated
		 */
		EClass FLOAT_VALUE = eINSTANCE.getFloatValue();

		/**
		 * The meta object literal for the '<em><b>Measurement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute FLOAT_VALUE__MEASUREMENT = eINSTANCE.getFloatValue_Measurement();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.impl.DoubleValueImpl <em>Double Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.DoubleValueImpl
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getDoubleValue()
		 * @generated
		 */
		EClass DOUBLE_VALUE = eINSTANCE.getDoubleValue();

		/**
		 * The meta object literal for the '<em><b>Measurement</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @generated
		 */
		EAttribute DOUBLE_VALUE__MEASUREMENT = eINSTANCE.getDoubleValue_Measurement();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.Unit <em>Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.Unit
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getUnit()
		 * @generated
		 */
		EClass UNIT = eINSTANCE.getUnit();

		/**
		 * The meta object literal for the '{@link kieker.analysisteetime.model.analysismodel.statistics.PredefinedUnits <em>Predefined Units</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * 
		 * @see kieker.analysisteetime.model.analysismodel.statistics.PredefinedUnits
		 * @see kieker.analysisteetime.model.analysismodel.statistics.impl.StatisticsPackageImpl#getPredefinedUnits()
		 * @generated
		 */
		EEnum PREDEFINED_UNITS = eINSTANCE.getPredefinedUnits();

	}

} // StatisticsPackage
