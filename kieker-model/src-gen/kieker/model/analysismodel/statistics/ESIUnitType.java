/**
 */
package kieker.model.analysismodel.statistics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>ESI Unit Type</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getESIUnitType()
 * @model
 * @generated
 */
public enum ESIUnitType implements Enumerator {
	/**
	 * The '<em><b>Meter</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #METER_VALUE
	 * @generated
	 * @ordered
	 */
	METER(0, "meter", "m"),

	/**
	 * The '<em><b>Gram</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GRAM_VALUE
	 * @generated
	 * @ordered
	 */
	GRAM(1, "gram", "g"),

	/**
	 * The '<em><b>Ton</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TON_VALUE
	 * @generated
	 * @ordered
	 */
	TON(2, "ton", "ton"),

	/**
	 * The '<em><b>Second</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SECOND_VALUE
	 * @generated
	 * @ordered
	 */
	SECOND(3, "second", "s"),

	/**
	 * The '<em><b>Ampere</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #AMPERE_VALUE
	 * @generated
	 * @ordered
	 */
	AMPERE(4, "ampere", "A"),

	/**
	 * The '<em><b>Kelvin</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #KELVIN_VALUE
	 * @generated
	 * @ordered
	 */
	KELVIN(5, "kelvin", "K"),

	/**
	 * The '<em><b>Mole</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MOLE_VALUE
	 * @generated
	 * @ordered
	 */
	MOLE(6, "mole", "mol"),

	/**
	 * The '<em><b>Candela</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CANDELA_VALUE
	 * @generated
	 * @ordered
	 */
	CANDELA(7, "candela", "ca"),

	/**
	 * The '<em><b>Pascal</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PASCAL_VALUE
	 * @generated
	 * @ordered
	 */
	PASCAL(8, "pascal", "Pa"),

	/**
	 * The '<em><b>Joul</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #JOUL_VALUE
	 * @generated
	 * @ordered
	 */
	JOUL(9, "Joul", "J");

	/**
	 * The '<em><b>Meter</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #METER
	 * @model name="meter" literal="m"
	 * @generated
	 * @ordered
	 */
	public static final int METER_VALUE = 0;

	/**
	 * The '<em><b>Gram</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #GRAM
	 * @model name="gram" literal="g"
	 * @generated
	 * @ordered
	 */
	public static final int GRAM_VALUE = 1;

	/**
	 * The '<em><b>Ton</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #TON
	 * @model name="ton"
	 * @generated
	 * @ordered
	 */
	public static final int TON_VALUE = 2;

	/**
	 * The '<em><b>Second</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #SECOND
	 * @model name="second" literal="s"
	 * @generated
	 * @ordered
	 */
	public static final int SECOND_VALUE = 3;

	/**
	 * The '<em><b>Ampere</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #AMPERE
	 * @model name="ampere" literal="A"
	 * @generated
	 * @ordered
	 */
	public static final int AMPERE_VALUE = 4;

	/**
	 * The '<em><b>Kelvin</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #KELVIN
	 * @model name="kelvin" literal="K"
	 * @generated
	 * @ordered
	 */
	public static final int KELVIN_VALUE = 5;

	/**
	 * The '<em><b>Mole</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #MOLE
	 * @model name="mole" literal="mol"
	 * @generated
	 * @ordered
	 */
	public static final int MOLE_VALUE = 6;

	/**
	 * The '<em><b>Candela</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #CANDELA
	 * @model name="candela" literal="ca"
	 * @generated
	 * @ordered
	 */
	public static final int CANDELA_VALUE = 7;

	/**
	 * The '<em><b>Pascal</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PASCAL
	 * @model name="pascal" literal="Pa"
	 * @generated
	 * @ordered
	 */
	public static final int PASCAL_VALUE = 8;

	/**
	 * The '<em><b>Joul</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #JOUL
	 * @model name="Joul" literal="J"
	 * @generated
	 * @ordered
	 */
	public static final int JOUL_VALUE = 9;

	/**
	 * An array of all the '<em><b>ESI Unit Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final ESIUnitType[] VALUES_ARRAY =
		new ESIUnitType[] {
			METER,
			GRAM,
			TON,
			SECOND,
			AMPERE,
			KELVIN,
			MOLE,
			CANDELA,
			PASCAL,
			JOUL,
		};

	/**
	 * A public read-only list of all the '<em><b>ESI Unit Type</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<ESIUnitType> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>ESI Unit Type</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ESIUnitType get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ESIUnitType result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>ESI Unit Type</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ESIUnitType getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			ESIUnitType result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>ESI Unit Type</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static ESIUnitType get(int value) {
		switch (value) {
			case METER_VALUE: return METER;
			case GRAM_VALUE: return GRAM;
			case TON_VALUE: return TON;
			case SECOND_VALUE: return SECOND;
			case AMPERE_VALUE: return AMPERE;
			case KELVIN_VALUE: return KELVIN;
			case MOLE_VALUE: return MOLE;
			case CANDELA_VALUE: return CANDELA;
			case PASCAL_VALUE: return PASCAL;
			case JOUL_VALUE: return JOUL;
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
	private ESIUnitType(int value, String name, String literal) {
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
	
} //ESIUnitType
