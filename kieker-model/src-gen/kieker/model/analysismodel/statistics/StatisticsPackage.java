/**
 */
package kieker.model.analysismodel.statistics;

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
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.statistics.StatisticsFactory
 * @model kind="package"
 * @generated
 */
public interface StatisticsPackage extends EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "statistics";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "platform:/resource/Kieker/model/analysismodel.ecore/statistics";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "statistics";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	StatisticsPackage eINSTANCE = kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl.init();

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.StatisticRecordImpl <em>Statistic Record</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.StatisticRecordImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getStatisticRecord()
	 * @generated
	 */
	int STATISTIC_RECORD = 0;

	/**
	 * The feature id for the '<em><b>Properties</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIC_RECORD__PROPERTIES = 0;

	/**
	 * The number of structural features of the '<em>Statistic Record</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIC_RECORD_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Statistic Record</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTIC_RECORD_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.EPropertyTypeToValueImpl <em>EProperty Type To Value</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.EPropertyTypeToValueImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getEPropertyTypeToValue()
	 * @generated
	 */
	int EPROPERTY_TYPE_TO_VALUE = 1;

	/**
	 * The feature id for the '<em><b>Key</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPROPERTY_TYPE_TO_VALUE__KEY = 0;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPROPERTY_TYPE_TO_VALUE__VALUE = 1;

	/**
	 * The number of structural features of the '<em>EProperty Type To Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPROPERTY_TYPE_TO_VALUE_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EProperty Type To Value</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EPROPERTY_TYPE_TO_VALUE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.MeasurementImpl <em>Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.MeasurementImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getMeasurement()
	 * @generated
	 */
	int MEASUREMENT = 2;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT__TIMESTAMP = 0;

	/**
	 * The number of structural features of the '<em>Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MEASUREMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.ScalarMeasurementImpl <em>Scalar Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.ScalarMeasurementImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getScalarMeasurement()
	 * @generated
	 */
	int SCALAR_MEASUREMENT = 3;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCALAR_MEASUREMENT__TIMESTAMP = MEASUREMENT__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCALAR_MEASUREMENT__UNIT = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Scalar Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCALAR_MEASUREMENT_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Scalar Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SCALAR_MEASUREMENT_OPERATION_COUNT = MEASUREMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.VectorMeasurementImpl <em>Vector Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.VectorMeasurementImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getVectorMeasurement()
	 * @generated
	 */
	int VECTOR_MEASUREMENT = 4;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VECTOR_MEASUREMENT__TIMESTAMP = MEASUREMENT__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Values</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VECTOR_MEASUREMENT__VALUES = MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Vector Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VECTOR_MEASUREMENT_FEATURE_COUNT = MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Vector Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VECTOR_MEASUREMENT_OPERATION_COUNT = MEASUREMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.IntMeasurementImpl <em>Int Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.IntMeasurementImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getIntMeasurement()
	 * @generated
	 */
	int INT_MEASUREMENT = 5;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INT_MEASUREMENT__TIMESTAMP = SCALAR_MEASUREMENT__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INT_MEASUREMENT__UNIT = SCALAR_MEASUREMENT__UNIT;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INT_MEASUREMENT__VALUE = SCALAR_MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Int Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INT_MEASUREMENT_FEATURE_COUNT = SCALAR_MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Int Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INT_MEASUREMENT_OPERATION_COUNT = SCALAR_MEASUREMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.LongMeasurementImpl <em>Long Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.LongMeasurementImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getLongMeasurement()
	 * @generated
	 */
	int LONG_MEASUREMENT = 6;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LONG_MEASUREMENT__TIMESTAMP = SCALAR_MEASUREMENT__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LONG_MEASUREMENT__UNIT = SCALAR_MEASUREMENT__UNIT;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LONG_MEASUREMENT__VALUE = SCALAR_MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Long Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LONG_MEASUREMENT_FEATURE_COUNT = SCALAR_MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Long Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LONG_MEASUREMENT_OPERATION_COUNT = SCALAR_MEASUREMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.FloatMeasurementImpl <em>Float Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.FloatMeasurementImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getFloatMeasurement()
	 * @generated
	 */
	int FLOAT_MEASUREMENT = 7;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOAT_MEASUREMENT__TIMESTAMP = SCALAR_MEASUREMENT__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOAT_MEASUREMENT__UNIT = SCALAR_MEASUREMENT__UNIT;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOAT_MEASUREMENT__VALUE = SCALAR_MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Float Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOAT_MEASUREMENT_FEATURE_COUNT = SCALAR_MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Float Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FLOAT_MEASUREMENT_OPERATION_COUNT = SCALAR_MEASUREMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.DoubleMeasurementImpl <em>Double Measurement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.DoubleMeasurementImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getDoubleMeasurement()
	 * @generated
	 */
	int DOUBLE_MEASUREMENT = 8;

	/**
	 * The feature id for the '<em><b>Timestamp</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_MEASUREMENT__TIMESTAMP = SCALAR_MEASUREMENT__TIMESTAMP;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_MEASUREMENT__UNIT = SCALAR_MEASUREMENT__UNIT;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_MEASUREMENT__VALUE = SCALAR_MEASUREMENT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Double Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_MEASUREMENT_FEATURE_COUNT = SCALAR_MEASUREMENT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Double Measurement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DOUBLE_MEASUREMENT_OPERATION_COUNT = SCALAR_MEASUREMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.UnitImpl <em>Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.UnitImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getUnit()
	 * @generated
	 */
	int UNIT = 11;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.ComposedUnitImpl <em>Composed Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.ComposedUnitImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getComposedUnit()
	 * @generated
	 */
	int COMPOSED_UNIT = 12;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.SimpleUnitImpl <em>Simple Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.SimpleUnitImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getSimpleUnit()
	 * @generated
	 */
	int SIMPLE_UNIT = 13;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.SIUnitImpl <em>SI Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.SIUnitImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getSIUnit()
	 * @generated
	 */
	int SI_UNIT = 14;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.CustomUnitImpl <em>Custom Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.CustomUnitImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getCustomUnit()
	 * @generated
	 */
	int CUSTOM_UNIT = 15;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.ESIUnitType <em>ESI Unit Type</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.ESIUnitType
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getESIUnitType()
	 * @generated
	 */
	int ESI_UNIT_TYPE = 16;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.EPrefix <em>EPrefix</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.EPrefix
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getEPrefix()
	 * @generated
	 */
	int EPREFIX = 17;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.StatisticsModelImpl <em>Model</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsModelImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getStatisticsModel()
	 * @generated
	 */
	int STATISTICS_MODEL = 9;

	/**
	 * The feature id for the '<em><b>Statistics</b></em>' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTICS_MODEL__STATISTICS = 0;

	/**
	 * The feature id for the '<em><b>Units</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTICS_MODEL__UNITS = 1;

	/**
	 * The number of structural features of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTICS_MODEL_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Model</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STATISTICS_MODEL_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link kieker.model.analysismodel.statistics.impl.EObjectToStatisticsMapEntryImpl <em>EObject To Statistics Map Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see kieker.model.analysismodel.statistics.impl.EObjectToStatisticsMapEntryImpl
	 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getEObjectToStatisticsMapEntry()
	 * @generated
	 */
	int EOBJECT_TO_STATISTICS_MAP_ENTRY = 10;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE = 0;

	/**
	 * The feature id for the '<em><b>Key</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY = 1;

	/**
	 * The number of structural features of the '<em>EObject To Statistics Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_STATISTICS_MAP_ENTRY_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>EObject To Statistics Map Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int EOBJECT_TO_STATISTICS_MAP_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The number of structural features of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNIT_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Units</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_UNIT__UNITS = 0;

	/**
	 * The feature id for the '<em><b>Exponent</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_UNIT__EXPONENT = 1;

	/**
	 * The number of structural features of the '<em>Composed Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_UNIT_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Composed Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPOSED_UNIT_OPERATION_COUNT = 0;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_UNIT__PREFIX = UNIT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Simple Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_UNIT_FEATURE_COUNT = UNIT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Simple Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SIMPLE_UNIT_OPERATION_COUNT = UNIT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SI_UNIT__PREFIX = SIMPLE_UNIT__PREFIX;

	/**
	 * The feature id for the '<em><b>Unit Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SI_UNIT__UNIT_TYPE = SIMPLE_UNIT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>SI Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SI_UNIT_FEATURE_COUNT = SIMPLE_UNIT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>SI Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SI_UNIT_OPERATION_COUNT = SIMPLE_UNIT_OPERATION_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_UNIT__PREFIX = SIMPLE_UNIT__PREFIX;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_UNIT__NAME = SIMPLE_UNIT_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Custom Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_UNIT_FEATURE_COUNT = SIMPLE_UNIT_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Custom Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CUSTOM_UNIT_OPERATION_COUNT = SIMPLE_UNIT_OPERATION_COUNT + 0;

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.StatisticRecord <em>Statistic Record</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Statistic Record</em>'.
	 * @see kieker.model.analysismodel.statistics.StatisticRecord
	 * @generated
	 */
	EClass getStatisticRecord();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.statistics.StatisticRecord#getProperties <em>Properties</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Properties</em>'.
	 * @see kieker.model.analysismodel.statistics.StatisticRecord#getProperties()
	 * @see #getStatisticRecord()
	 * @generated
	 */
	EReference getStatisticRecord_Properties();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EProperty Type To Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EProperty Type To Value</em>'.
	 * @see java.util.Map.Entry
	 * @model keyDataType="org.eclipse.emf.ecore.EString"
	 *        valueDataType="org.eclipse.emf.ecore.EJavaObject"
	 * @generated
	 */
	EClass getEPropertyTypeToValue();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEPropertyTypeToValue()
	 * @generated
	 */
	EAttribute getEPropertyTypeToValue_Key();

	/**
	 * Returns the meta object for the attribute '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEPropertyTypeToValue()
	 * @generated
	 */
	EAttribute getEPropertyTypeToValue_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.Measurement <em>Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Measurement</em>'.
	 * @see kieker.model.analysismodel.statistics.Measurement
	 * @generated
	 */
	EClass getMeasurement();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.Measurement#getTimestamp <em>Timestamp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Timestamp</em>'.
	 * @see kieker.model.analysismodel.statistics.Measurement#getTimestamp()
	 * @see #getMeasurement()
	 * @generated
	 */
	EAttribute getMeasurement_Timestamp();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.ScalarMeasurement <em>Scalar Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Scalar Measurement</em>'.
	 * @see kieker.model.analysismodel.statistics.ScalarMeasurement
	 * @generated
	 */
	EClass getScalarMeasurement();

	/**
	 * Returns the meta object for the reference '{@link kieker.model.analysismodel.statistics.ScalarMeasurement#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Unit</em>'.
	 * @see kieker.model.analysismodel.statistics.ScalarMeasurement#getUnit()
	 * @see #getScalarMeasurement()
	 * @generated
	 */
	EReference getScalarMeasurement_Unit();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.VectorMeasurement <em>Vector Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Vector Measurement</em>'.
	 * @see kieker.model.analysismodel.statistics.VectorMeasurement
	 * @generated
	 */
	EClass getVectorMeasurement();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.model.analysismodel.statistics.VectorMeasurement#getValues <em>Values</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Values</em>'.
	 * @see kieker.model.analysismodel.statistics.VectorMeasurement#getValues()
	 * @see #getVectorMeasurement()
	 * @generated
	 */
	EReference getVectorMeasurement_Values();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.IntMeasurement <em>Int Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Int Measurement</em>'.
	 * @see kieker.model.analysismodel.statistics.IntMeasurement
	 * @generated
	 */
	EClass getIntMeasurement();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.IntMeasurement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see kieker.model.analysismodel.statistics.IntMeasurement#getValue()
	 * @see #getIntMeasurement()
	 * @generated
	 */
	EAttribute getIntMeasurement_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.LongMeasurement <em>Long Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Long Measurement</em>'.
	 * @see kieker.model.analysismodel.statistics.LongMeasurement
	 * @generated
	 */
	EClass getLongMeasurement();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.LongMeasurement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see kieker.model.analysismodel.statistics.LongMeasurement#getValue()
	 * @see #getLongMeasurement()
	 * @generated
	 */
	EAttribute getLongMeasurement_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.FloatMeasurement <em>Float Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Float Measurement</em>'.
	 * @see kieker.model.analysismodel.statistics.FloatMeasurement
	 * @generated
	 */
	EClass getFloatMeasurement();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.FloatMeasurement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see kieker.model.analysismodel.statistics.FloatMeasurement#getValue()
	 * @see #getFloatMeasurement()
	 * @generated
	 */
	EAttribute getFloatMeasurement_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.DoubleMeasurement <em>Double Measurement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Double Measurement</em>'.
	 * @see kieker.model.analysismodel.statistics.DoubleMeasurement
	 * @generated
	 */
	EClass getDoubleMeasurement();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.DoubleMeasurement#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see kieker.model.analysismodel.statistics.DoubleMeasurement#getValue()
	 * @see #getDoubleMeasurement()
	 * @generated
	 */
	EAttribute getDoubleMeasurement_Value();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.Unit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unit</em>'.
	 * @see kieker.model.analysismodel.statistics.Unit
	 * @generated
	 */
	EClass getUnit();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.ComposedUnit <em>Composed Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Composed Unit</em>'.
	 * @see kieker.model.analysismodel.statistics.ComposedUnit
	 * @generated
	 */
	EClass getComposedUnit();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.model.analysismodel.statistics.ComposedUnit#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Units</em>'.
	 * @see kieker.model.analysismodel.statistics.ComposedUnit#getUnits()
	 * @see #getComposedUnit()
	 * @generated
	 */
	EReference getComposedUnit_Units();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.ComposedUnit#getExponent <em>Exponent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Exponent</em>'.
	 * @see kieker.model.analysismodel.statistics.ComposedUnit#getExponent()
	 * @see #getComposedUnit()
	 * @generated
	 */
	EAttribute getComposedUnit_Exponent();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.SimpleUnit <em>Simple Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Simple Unit</em>'.
	 * @see kieker.model.analysismodel.statistics.SimpleUnit
	 * @generated
	 */
	EClass getSimpleUnit();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.SimpleUnit#getPrefix <em>Prefix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Prefix</em>'.
	 * @see kieker.model.analysismodel.statistics.SimpleUnit#getPrefix()
	 * @see #getSimpleUnit()
	 * @generated
	 */
	EAttribute getSimpleUnit_Prefix();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.SIUnit <em>SI Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>SI Unit</em>'.
	 * @see kieker.model.analysismodel.statistics.SIUnit
	 * @generated
	 */
	EClass getSIUnit();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.SIUnit#getUnitType <em>Unit Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit Type</em>'.
	 * @see kieker.model.analysismodel.statistics.SIUnit#getUnitType()
	 * @see #getSIUnit()
	 * @generated
	 */
	EAttribute getSIUnit_UnitType();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.CustomUnit <em>Custom Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Custom Unit</em>'.
	 * @see kieker.model.analysismodel.statistics.CustomUnit
	 * @generated
	 */
	EClass getCustomUnit();

	/**
	 * Returns the meta object for the attribute '{@link kieker.model.analysismodel.statistics.CustomUnit#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see kieker.model.analysismodel.statistics.CustomUnit#getName()
	 * @see #getCustomUnit()
	 * @generated
	 */
	EAttribute getCustomUnit_Name();

	/**
	 * Returns the meta object for enum '{@link kieker.model.analysismodel.statistics.ESIUnitType <em>ESI Unit Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>ESI Unit Type</em>'.
	 * @see kieker.model.analysismodel.statistics.ESIUnitType
	 * @generated
	 */
	EEnum getESIUnitType();

	/**
	 * Returns the meta object for enum '{@link kieker.model.analysismodel.statistics.EPrefix <em>EPrefix</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>EPrefix</em>'.
	 * @see kieker.model.analysismodel.statistics.EPrefix
	 * @generated
	 */
	EEnum getEPrefix();

	/**
	 * Returns the meta object for class '{@link kieker.model.analysismodel.statistics.StatisticsModel <em>Model</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Model</em>'.
	 * @see kieker.model.analysismodel.statistics.StatisticsModel
	 * @generated
	 */
	EClass getStatisticsModel();

	/**
	 * Returns the meta object for the map '{@link kieker.model.analysismodel.statistics.StatisticsModel#getStatistics <em>Statistics</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the map '<em>Statistics</em>'.
	 * @see kieker.model.analysismodel.statistics.StatisticsModel#getStatistics()
	 * @see #getStatisticsModel()
	 * @generated
	 */
	EReference getStatisticsModel_Statistics();

	/**
	 * Returns the meta object for the containment reference list '{@link kieker.model.analysismodel.statistics.StatisticsModel#getUnits <em>Units</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Units</em>'.
	 * @see kieker.model.analysismodel.statistics.StatisticsModel#getUnits()
	 * @see #getStatisticsModel()
	 * @generated
	 */
	EReference getStatisticsModel_Units();

	/**
	 * Returns the meta object for class '{@link java.util.Map.Entry <em>EObject To Statistics Map Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>EObject To Statistics Map Entry</em>'.
	 * @see java.util.Map.Entry
	 * @model features="value key" 
	 *        valueType="kieker.model.analysismodel.statistics.StatisticRecord" valueContainment="true"
	 *        keyType="org.eclipse.emf.ecore.EObject"
	 * @generated
	 */
	EClass getEObjectToStatisticsMapEntry();

	/**
	 * Returns the meta object for the containment reference '{@link java.util.Map.Entry <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEObjectToStatisticsMapEntry()
	 * @generated
	 */
	EReference getEObjectToStatisticsMapEntry_Value();

	/**
	 * Returns the meta object for the reference '{@link java.util.Map.Entry <em>Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key</em>'.
	 * @see java.util.Map.Entry
	 * @see #getEObjectToStatisticsMapEntry()
	 * @generated
	 */
	EReference getEObjectToStatisticsMapEntry_Key();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	StatisticsFactory getStatisticsFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.StatisticRecordImpl <em>Statistic Record</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.StatisticRecordImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getStatisticRecord()
		 * @generated
		 */
		EClass STATISTIC_RECORD = eINSTANCE.getStatisticRecord();

		/**
		 * The meta object literal for the '<em><b>Properties</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATISTIC_RECORD__PROPERTIES = eINSTANCE.getStatisticRecord_Properties();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.EPropertyTypeToValueImpl <em>EProperty Type To Value</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.EPropertyTypeToValueImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getEPropertyTypeToValue()
		 * @generated
		 */
		EClass EPROPERTY_TYPE_TO_VALUE = eINSTANCE.getEPropertyTypeToValue();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPROPERTY_TYPE_TO_VALUE__KEY = eINSTANCE.getEPropertyTypeToValue_Key();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute EPROPERTY_TYPE_TO_VALUE__VALUE = eINSTANCE.getEPropertyTypeToValue_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.MeasurementImpl <em>Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.MeasurementImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getMeasurement()
		 * @generated
		 */
		EClass MEASUREMENT = eINSTANCE.getMeasurement();

		/**
		 * The meta object literal for the '<em><b>Timestamp</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MEASUREMENT__TIMESTAMP = eINSTANCE.getMeasurement_Timestamp();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.ScalarMeasurementImpl <em>Scalar Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.ScalarMeasurementImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getScalarMeasurement()
		 * @generated
		 */
		EClass SCALAR_MEASUREMENT = eINSTANCE.getScalarMeasurement();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SCALAR_MEASUREMENT__UNIT = eINSTANCE.getScalarMeasurement_Unit();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.VectorMeasurementImpl <em>Vector Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.VectorMeasurementImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getVectorMeasurement()
		 * @generated
		 */
		EClass VECTOR_MEASUREMENT = eINSTANCE.getVectorMeasurement();

		/**
		 * The meta object literal for the '<em><b>Values</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VECTOR_MEASUREMENT__VALUES = eINSTANCE.getVectorMeasurement_Values();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.IntMeasurementImpl <em>Int Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.IntMeasurementImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getIntMeasurement()
		 * @generated
		 */
		EClass INT_MEASUREMENT = eINSTANCE.getIntMeasurement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INT_MEASUREMENT__VALUE = eINSTANCE.getIntMeasurement_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.LongMeasurementImpl <em>Long Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.LongMeasurementImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getLongMeasurement()
		 * @generated
		 */
		EClass LONG_MEASUREMENT = eINSTANCE.getLongMeasurement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute LONG_MEASUREMENT__VALUE = eINSTANCE.getLongMeasurement_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.FloatMeasurementImpl <em>Float Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.FloatMeasurementImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getFloatMeasurement()
		 * @generated
		 */
		EClass FLOAT_MEASUREMENT = eINSTANCE.getFloatMeasurement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FLOAT_MEASUREMENT__VALUE = eINSTANCE.getFloatMeasurement_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.DoubleMeasurementImpl <em>Double Measurement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.DoubleMeasurementImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getDoubleMeasurement()
		 * @generated
		 */
		EClass DOUBLE_MEASUREMENT = eINSTANCE.getDoubleMeasurement();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DOUBLE_MEASUREMENT__VALUE = eINSTANCE.getDoubleMeasurement_Value();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.UnitImpl <em>Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.UnitImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getUnit()
		 * @generated
		 */
		EClass UNIT = eINSTANCE.getUnit();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.ComposedUnitImpl <em>Composed Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.ComposedUnitImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getComposedUnit()
		 * @generated
		 */
		EClass COMPOSED_UNIT = eINSTANCE.getComposedUnit();

		/**
		 * The meta object literal for the '<em><b>Units</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPOSED_UNIT__UNITS = eINSTANCE.getComposedUnit_Units();

		/**
		 * The meta object literal for the '<em><b>Exponent</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPOSED_UNIT__EXPONENT = eINSTANCE.getComposedUnit_Exponent();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.SimpleUnitImpl <em>Simple Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.SimpleUnitImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getSimpleUnit()
		 * @generated
		 */
		EClass SIMPLE_UNIT = eINSTANCE.getSimpleUnit();

		/**
		 * The meta object literal for the '<em><b>Prefix</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SIMPLE_UNIT__PREFIX = eINSTANCE.getSimpleUnit_Prefix();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.SIUnitImpl <em>SI Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.SIUnitImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getSIUnit()
		 * @generated
		 */
		EClass SI_UNIT = eINSTANCE.getSIUnit();

		/**
		 * The meta object literal for the '<em><b>Unit Type</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SI_UNIT__UNIT_TYPE = eINSTANCE.getSIUnit_UnitType();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.CustomUnitImpl <em>Custom Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.CustomUnitImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getCustomUnit()
		 * @generated
		 */
		EClass CUSTOM_UNIT = eINSTANCE.getCustomUnit();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CUSTOM_UNIT__NAME = eINSTANCE.getCustomUnit_Name();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.ESIUnitType <em>ESI Unit Type</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.ESIUnitType
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getESIUnitType()
		 * @generated
		 */
		EEnum ESI_UNIT_TYPE = eINSTANCE.getESIUnitType();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.EPrefix <em>EPrefix</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.EPrefix
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getEPrefix()
		 * @generated
		 */
		EEnum EPREFIX = eINSTANCE.getEPrefix();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.StatisticsModelImpl <em>Model</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsModelImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getStatisticsModel()
		 * @generated
		 */
		EClass STATISTICS_MODEL = eINSTANCE.getStatisticsModel();

		/**
		 * The meta object literal for the '<em><b>Statistics</b></em>' map feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATISTICS_MODEL__STATISTICS = eINSTANCE.getStatisticsModel_Statistics();

		/**
		 * The meta object literal for the '<em><b>Units</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference STATISTICS_MODEL__UNITS = eINSTANCE.getStatisticsModel_Units();

		/**
		 * The meta object literal for the '{@link kieker.model.analysismodel.statistics.impl.EObjectToStatisticsMapEntryImpl <em>EObject To Statistics Map Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see kieker.model.analysismodel.statistics.impl.EObjectToStatisticsMapEntryImpl
		 * @see kieker.model.analysismodel.statistics.impl.StatisticsPackageImpl#getEObjectToStatisticsMapEntry()
		 * @generated
		 */
		EClass EOBJECT_TO_STATISTICS_MAP_ENTRY = eINSTANCE.getEObjectToStatisticsMapEntry();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EOBJECT_TO_STATISTICS_MAP_ENTRY__VALUE = eINSTANCE.getEObjectToStatisticsMapEntry_Value();

		/**
		 * The meta object literal for the '<em><b>Key</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference EOBJECT_TO_STATISTICS_MAP_ENTRY__KEY = eINSTANCE.getEObjectToStatisticsMapEntry_Key();

	}

} //StatisticsPackage
