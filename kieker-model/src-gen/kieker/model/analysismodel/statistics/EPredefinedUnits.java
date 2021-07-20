/**
 */
package kieker.model.analysismodel.statistics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>EPredefined Units</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getEPredefinedUnits()
 * @model
 * @generated
 */
public enum EPredefinedUnits implements Enumerator {
	/**
	 * The '<em><b>CPU UTIL</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CPU_UTIL_VALUE
	 * @generated
	 * @ordered
	 */
	CPU_UTIL(1, "CPU_UTIL", "CPU_UTIL"),

	/**
	 * The '<em><b>RESPONSE TIME</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RESPONSE_TIME_VALUE
	 * @generated
	 * @ordered
	 */
	RESPONSE_TIME(2, "RESPONSE_TIME", "RESPONSE_TIME"), /**
	 * The '<em><b>INVOCATION</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INVOCATION_VALUE
	 * @generated
	 * @ordered
	 */
	INVOCATION(3, "INVOCATION", "INVOCATION");

	/**
	 * The '<em><b>CPU UTIL</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CPU_UTIL
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int CPU_UTIL_VALUE = 1;

	/**
	 * The '<em><b>RESPONSE TIME</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #RESPONSE_TIME
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int RESPONSE_TIME_VALUE = 2;

	/**
	 * The '<em><b>INVOCATION</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #INVOCATION
	 * @model
	 * @generated
	 * @ordered
	 */
	public static final int INVOCATION_VALUE = 3;

	/**
	 * An array of all the '<em><b>EPredefined Units</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final EPredefinedUnits[] VALUES_ARRAY =
		new EPredefinedUnits[] {
			CPU_UTIL,
			RESPONSE_TIME,
			INVOCATION,
		};

	/**
	 * A public read-only list of all the '<em><b>EPredefined Units</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<EPredefinedUnits> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>EPredefined Units</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EPredefinedUnits get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EPredefinedUnits result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EPredefined Units</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EPredefinedUnits getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			EPredefinedUnits result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EPredefined Units</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EPredefinedUnits get(int value) {
		switch (value) {
			case CPU_UTIL_VALUE: return CPU_UTIL;
			case RESPONSE_TIME_VALUE: return RESPONSE_TIME;
			case INVOCATION_VALUE: return INVOCATION;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EPredefinedUnits(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //EPredefinedUnits
