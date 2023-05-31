/**
 */
package kieker.model.analysismodel.statistics.util;

import java.util.Map;

import kieker.model.analysismodel.statistics.*;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.statistics.StatisticsPackage
 * @generated
 */
public class StatisticsSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static StatisticsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public StatisticsSwitch() {
		if (modelPackage == null) {
			modelPackage = StatisticsPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case StatisticsPackage.STATISTIC_RECORD: {
				StatisticRecord statisticRecord = (StatisticRecord)theEObject;
				T result = caseStatisticRecord(statisticRecord);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.EPROPERTY_TYPE_TO_VALUE: {
				@SuppressWarnings("unchecked") Map.Entry<String, Object> ePropertyTypeToValue = (Map.Entry<String, Object>)theEObject;
				T result = caseEPropertyTypeToValue(ePropertyTypeToValue);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.MEASUREMENT: {
				Measurement measurement = (Measurement)theEObject;
				T result = caseMeasurement(measurement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.SCALAR_MEASUREMENT: {
				ScalarMeasurement scalarMeasurement = (ScalarMeasurement)theEObject;
				T result = caseScalarMeasurement(scalarMeasurement);
				if (result == null) result = caseMeasurement(scalarMeasurement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.VECTOR_MEASUREMENT: {
				VectorMeasurement vectorMeasurement = (VectorMeasurement)theEObject;
				T result = caseVectorMeasurement(vectorMeasurement);
				if (result == null) result = caseMeasurement(vectorMeasurement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.INT_MEASUREMENT: {
				IntMeasurement intMeasurement = (IntMeasurement)theEObject;
				T result = caseIntMeasurement(intMeasurement);
				if (result == null) result = caseScalarMeasurement(intMeasurement);
				if (result == null) result = caseMeasurement(intMeasurement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.LONG_MEASUREMENT: {
				LongMeasurement longMeasurement = (LongMeasurement)theEObject;
				T result = caseLongMeasurement(longMeasurement);
				if (result == null) result = caseScalarMeasurement(longMeasurement);
				if (result == null) result = caseMeasurement(longMeasurement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.FLOAT_MEASUREMENT: {
				FloatMeasurement floatMeasurement = (FloatMeasurement)theEObject;
				T result = caseFloatMeasurement(floatMeasurement);
				if (result == null) result = caseScalarMeasurement(floatMeasurement);
				if (result == null) result = caseMeasurement(floatMeasurement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.DOUBLE_MEASUREMENT: {
				DoubleMeasurement doubleMeasurement = (DoubleMeasurement)theEObject;
				T result = caseDoubleMeasurement(doubleMeasurement);
				if (result == null) result = caseScalarMeasurement(doubleMeasurement);
				if (result == null) result = caseMeasurement(doubleMeasurement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.STATISTICS_MODEL: {
				StatisticsModel statisticsModel = (StatisticsModel)theEObject;
				T result = caseStatisticsModel(statisticsModel);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY: {
				@SuppressWarnings("unchecked") Map.Entry<EObject, StatisticRecord> eObjectToStatisticsMapEntry = (Map.Entry<EObject, StatisticRecord>)theEObject;
				T result = caseEObjectToStatisticsMapEntry(eObjectToStatisticsMapEntry);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.UNIT: {
				Unit unit = (Unit)theEObject;
				T result = caseUnit(unit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.COMPOSED_UNIT: {
				ComposedUnit composedUnit = (ComposedUnit)theEObject;
				T result = caseComposedUnit(composedUnit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.SIMPLE_UNIT: {
				SimpleUnit simpleUnit = (SimpleUnit)theEObject;
				T result = caseSimpleUnit(simpleUnit);
				if (result == null) result = caseUnit(simpleUnit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.SI_UNIT: {
				SIUnit siUnit = (SIUnit)theEObject;
				T result = caseSIUnit(siUnit);
				if (result == null) result = caseSimpleUnit(siUnit);
				if (result == null) result = caseUnit(siUnit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case StatisticsPackage.CUSTOM_UNIT: {
				CustomUnit customUnit = (CustomUnit)theEObject;
				T result = caseCustomUnit(customUnit);
				if (result == null) result = caseSimpleUnit(customUnit);
				if (result == null) result = caseUnit(customUnit);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Statistic Record</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Statistic Record</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatisticRecord(StatisticRecord object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EProperty Type To Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EProperty Type To Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEPropertyTypeToValue(Map.Entry<String, Object> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMeasurement(Measurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Scalar Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Scalar Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScalarMeasurement(ScalarMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vector Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vector Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVectorMeasurement(VectorMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Int Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Int Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIntMeasurement(IntMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Long Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Long Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLongMeasurement(LongMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Float Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Float Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFloatMeasurement(FloatMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Double Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Double Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDoubleMeasurement(DoubleMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnit(Unit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Composed Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Composed Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComposedUnit(ComposedUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleUnit(SimpleUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>SI Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>SI Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSIUnit(SIUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Custom Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Custom Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCustomUnit(CustomUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatisticsModel(StatisticsModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject To Statistics Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject To Statistics Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEObjectToStatisticsMapEntry(Map.Entry<EObject, StatisticRecord> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //StatisticsSwitch
