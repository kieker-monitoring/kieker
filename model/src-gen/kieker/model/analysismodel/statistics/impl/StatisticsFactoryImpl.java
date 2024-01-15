/**
 */
package kieker.model.analysismodel.statistics.impl;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EFactoryImpl;
import org.eclipse.emf.ecore.plugin.EcorePlugin;

import kieker.model.analysismodel.statistics.ComposedUnit;
import kieker.model.analysismodel.statistics.CustomUnit;
import kieker.model.analysismodel.statistics.DoubleMeasurement;
import kieker.model.analysismodel.statistics.EPrefix;
import kieker.model.analysismodel.statistics.ESIUnitType;
import kieker.model.analysismodel.statistics.FloatMeasurement;
import kieker.model.analysismodel.statistics.IntMeasurement;
import kieker.model.analysismodel.statistics.LongMeasurement;
import kieker.model.analysismodel.statistics.SIUnit;
import kieker.model.analysismodel.statistics.ScalarMeasurement;
import kieker.model.analysismodel.statistics.SimpleUnit;
import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.Unit;
import kieker.model.analysismodel.statistics.VectorMeasurement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class StatisticsFactoryImpl extends EFactoryImpl implements StatisticsFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static StatisticsFactory init() {
		try {
			final StatisticsFactory theStatisticsFactory = (StatisticsFactory) EPackage.Registry.INSTANCE.getEFactory(StatisticsPackage.eNS_URI);
			if (theStatisticsFactory != null) {
				return theStatisticsFactory;
			}
		} catch (final Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new StatisticsFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public StatisticsFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public EObject create(final EClass eClass) {
		switch (eClass.getClassifierID()) {
		case StatisticsPackage.STATISTIC_RECORD:
			return this.createStatisticRecord();
		case StatisticsPackage.EPROPERTY_TYPE_TO_VALUE:
			return (EObject) this.createEPropertyTypeToValue();
		case StatisticsPackage.SCALAR_MEASUREMENT:
			return this.createScalarMeasurement();
		case StatisticsPackage.VECTOR_MEASUREMENT:
			return this.createVectorMeasurement();
		case StatisticsPackage.INT_MEASUREMENT:
			return this.createIntMeasurement();
		case StatisticsPackage.LONG_MEASUREMENT:
			return this.createLongMeasurement();
		case StatisticsPackage.FLOAT_MEASUREMENT:
			return this.createFloatMeasurement();
		case StatisticsPackage.DOUBLE_MEASUREMENT:
			return this.createDoubleMeasurement();
		case StatisticsPackage.STATISTICS_MODEL:
			return this.createStatisticsModel();
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY:
			return (EObject) this.createEObjectToStatisticsMapEntry();
		case StatisticsPackage.UNIT:
			return this.createUnit();
		case StatisticsPackage.COMPOSED_UNIT:
			return this.createComposedUnit();
		case StatisticsPackage.SIMPLE_UNIT:
			return this.createSimpleUnit();
		case StatisticsPackage.SI_UNIT:
			return this.createSIUnit();
		case StatisticsPackage.CUSTOM_UNIT:
			return this.createCustomUnit();
		default:
			throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Object createFromString(final EDataType eDataType, final String initialValue) {
		switch (eDataType.getClassifierID()) {
		case StatisticsPackage.ESI_UNIT_TYPE:
			return this.createESIUnitTypeFromString(eDataType, initialValue);
		case StatisticsPackage.EPREFIX:
			return this.createEPrefixFromString(eDataType, initialValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String convertToString(final EDataType eDataType, final Object instanceValue) {
		switch (eDataType.getClassifierID()) {
		case StatisticsPackage.ESI_UNIT_TYPE:
			return this.convertESIUnitTypeToString(eDataType, instanceValue);
		case StatisticsPackage.EPREFIX:
			return this.convertEPrefixToString(eDataType, instanceValue);
		default:
			throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StatisticRecord createStatisticRecord() {
		final StatisticRecordImpl statisticRecord = new StatisticRecordImpl();
		return statisticRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<String, Object> createEPropertyTypeToValue() {
		final EPropertyTypeToValueImpl ePropertyTypeToValue = new EPropertyTypeToValueImpl();
		return ePropertyTypeToValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ScalarMeasurement createScalarMeasurement() {
		final ScalarMeasurementImpl scalarMeasurement = new ScalarMeasurementImpl();
		return scalarMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public VectorMeasurement createVectorMeasurement() {
		final VectorMeasurementImpl vectorMeasurement = new VectorMeasurementImpl();
		return vectorMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public IntMeasurement createIntMeasurement() {
		final IntMeasurementImpl intMeasurement = new IntMeasurementImpl();
		return intMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public LongMeasurement createLongMeasurement() {
		final LongMeasurementImpl longMeasurement = new LongMeasurementImpl();
		return longMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public FloatMeasurement createFloatMeasurement() {
		final FloatMeasurementImpl floatMeasurement = new FloatMeasurementImpl();
		return floatMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public DoubleMeasurement createDoubleMeasurement() {
		final DoubleMeasurementImpl doubleMeasurement = new DoubleMeasurementImpl();
		return doubleMeasurement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StatisticsModel createStatisticsModel() {
		final StatisticsModelImpl statisticsModel = new StatisticsModelImpl();
		return statisticsModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public Map.Entry<EObject, StatisticRecord> createEObjectToStatisticsMapEntry() {
		final EObjectToStatisticsMapEntryImpl eObjectToStatisticsMapEntry = new EObjectToStatisticsMapEntryImpl();
		return eObjectToStatisticsMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public Unit createUnit() {
		final UnitImpl unit = new UnitImpl();
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public ComposedUnit createComposedUnit() {
		final ComposedUnitImpl composedUnit = new ComposedUnitImpl();
		return composedUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public SimpleUnit createSimpleUnit() {
		final SimpleUnitImpl simpleUnit = new SimpleUnitImpl();
		return simpleUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public SIUnit createSIUnit() {
		final SIUnitImpl siUnit = new SIUnitImpl();
		return siUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public CustomUnit createCustomUnit() {
		final CustomUnitImpl customUnit = new CustomUnitImpl();
		return customUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public ESIUnitType createESIUnitTypeFromString(final EDataType eDataType, final String initialValue) {
		final ESIUnitType result = ESIUnitType.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertESIUnitTypeToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public EPrefix createEPrefixFromString(final EDataType eDataType, final String initialValue) {
		final EPrefix result = EPrefix.get(initialValue);
		if (result == null) {
			throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		}
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public String convertEPrefixToString(final EDataType eDataType, final Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public StatisticsPackage getStatisticsPackage() {
		return (StatisticsPackage) this.getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static StatisticsPackage getPackage() {
		return StatisticsPackage.eINSTANCE;
	}

} // StatisticsFactoryImpl
