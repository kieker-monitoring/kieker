/**
 */
package kieker.model.analysismodel.statistics.util;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Switch;

import kieker.model.analysismodel.statistics.ComposedUnit;
import kieker.model.analysismodel.statistics.CustomUnit;
import kieker.model.analysismodel.statistics.DoubleMeasurement;
import kieker.model.analysismodel.statistics.FloatMeasurement;
import kieker.model.analysismodel.statistics.IntMeasurement;
import kieker.model.analysismodel.statistics.LongMeasurement;
import kieker.model.analysismodel.statistics.Measurement;
import kieker.model.analysismodel.statistics.SIUnit;
import kieker.model.analysismodel.statistics.ScalarMeasurement;
import kieker.model.analysismodel.statistics.SimpleUnit;
import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.statistics.Unit;
import kieker.model.analysismodel.statistics.VectorMeasurement;

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
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage
 * @generated
 */
public class StatisticsSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	protected static StatisticsPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
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
	 *
	 * @param ePackage
	 *            the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(final EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(final int classifierID, final EObject theEObject) {
		switch (classifierID) {
		case StatisticsPackage.STATISTIC_RECORD: {
			final StatisticRecord statisticRecord = (StatisticRecord) theEObject;
			T result = this.caseStatisticRecord(statisticRecord);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.EPROPERTY_TYPE_TO_VALUE: {
			@SuppressWarnings("unchecked")
			final Map.Entry<String, Object> ePropertyTypeToValue = (Map.Entry<String, Object>) theEObject;
			T result = this.caseEPropertyTypeToValue(ePropertyTypeToValue);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.MEASUREMENT: {
			final Measurement measurement = (Measurement) theEObject;
			T result = this.caseMeasurement(measurement);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.SCALAR_MEASUREMENT: {
			final ScalarMeasurement scalarMeasurement = (ScalarMeasurement) theEObject;
			T result = this.caseScalarMeasurement(scalarMeasurement);
			if (result == null) {
				result = this.caseMeasurement(scalarMeasurement);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.VECTOR_MEASUREMENT: {
			final VectorMeasurement vectorMeasurement = (VectorMeasurement) theEObject;
			T result = this.caseVectorMeasurement(vectorMeasurement);
			if (result == null) {
				result = this.caseMeasurement(vectorMeasurement);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.INT_MEASUREMENT: {
			final IntMeasurement intMeasurement = (IntMeasurement) theEObject;
			T result = this.caseIntMeasurement(intMeasurement);
			if (result == null) {
				result = this.caseScalarMeasurement(intMeasurement);
			}
			if (result == null) {
				result = this.caseMeasurement(intMeasurement);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.LONG_MEASUREMENT: {
			final LongMeasurement longMeasurement = (LongMeasurement) theEObject;
			T result = this.caseLongMeasurement(longMeasurement);
			if (result == null) {
				result = this.caseScalarMeasurement(longMeasurement);
			}
			if (result == null) {
				result = this.caseMeasurement(longMeasurement);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.FLOAT_MEASUREMENT: {
			final FloatMeasurement floatMeasurement = (FloatMeasurement) theEObject;
			T result = this.caseFloatMeasurement(floatMeasurement);
			if (result == null) {
				result = this.caseScalarMeasurement(floatMeasurement);
			}
			if (result == null) {
				result = this.caseMeasurement(floatMeasurement);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.DOUBLE_MEASUREMENT: {
			final DoubleMeasurement doubleMeasurement = (DoubleMeasurement) theEObject;
			T result = this.caseDoubleMeasurement(doubleMeasurement);
			if (result == null) {
				result = this.caseScalarMeasurement(doubleMeasurement);
			}
			if (result == null) {
				result = this.caseMeasurement(doubleMeasurement);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.STATISTICS_MODEL: {
			final StatisticsModel statisticsModel = (StatisticsModel) theEObject;
			T result = this.caseStatisticsModel(statisticsModel);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.EOBJECT_TO_STATISTICS_MAP_ENTRY: {
			@SuppressWarnings("unchecked")
			final Map.Entry<EObject, StatisticRecord> eObjectToStatisticsMapEntry = (Map.Entry<EObject, StatisticRecord>) theEObject;
			T result = this.caseEObjectToStatisticsMapEntry(eObjectToStatisticsMapEntry);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.UNIT: {
			final Unit unit = (Unit) theEObject;
			T result = this.caseUnit(unit);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.COMPOSED_UNIT: {
			final ComposedUnit composedUnit = (ComposedUnit) theEObject;
			T result = this.caseComposedUnit(composedUnit);
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.SIMPLE_UNIT: {
			final SimpleUnit simpleUnit = (SimpleUnit) theEObject;
			T result = this.caseSimpleUnit(simpleUnit);
			if (result == null) {
				result = this.caseUnit(simpleUnit);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.SI_UNIT: {
			final SIUnit siUnit = (SIUnit) theEObject;
			T result = this.caseSIUnit(siUnit);
			if (result == null) {
				result = this.caseSimpleUnit(siUnit);
			}
			if (result == null) {
				result = this.caseUnit(siUnit);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		case StatisticsPackage.CUSTOM_UNIT: {
			final CustomUnit customUnit = (CustomUnit) theEObject;
			T result = this.caseCustomUnit(customUnit);
			if (result == null) {
				result = this.caseSimpleUnit(customUnit);
			}
			if (result == null) {
				result = this.caseUnit(customUnit);
			}
			if (result == null) {
				result = this.defaultCase(theEObject);
			}
			return result;
		}
		default:
			return this.defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Statistic Record</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Statistic Record</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatisticRecord(final StatisticRecord object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EProperty Type To Value</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EProperty Type To Value</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEPropertyTypeToValue(final Map.Entry<String, Object> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMeasurement(final Measurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Scalar Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Scalar Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseScalarMeasurement(final ScalarMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Vector Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Vector Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVectorMeasurement(final VectorMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Int Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Int Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIntMeasurement(final IntMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Long Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Long Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLongMeasurement(final LongMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Float Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Float Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFloatMeasurement(final FloatMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Double Measurement</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Double Measurement</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseDoubleMeasurement(final DoubleMeasurement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseUnit(final Unit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Composed Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Composed Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseComposedUnit(final ComposedUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Simple Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Simple Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSimpleUnit(final SimpleUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>SI Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>SI Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseSIUnit(final SIUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Custom Unit</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Custom Unit</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCustomUnit(final CustomUnit object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatisticsModel(final StatisticsModel object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject To Statistics Map Entry</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject To Statistics Map Entry</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEObjectToStatisticsMapEntry(final Map.Entry<EObject, StatisticRecord> object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 *
	 * @param object
	 *            the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(final EObject object) {
		return null;
	}

} // StatisticsSwitch
