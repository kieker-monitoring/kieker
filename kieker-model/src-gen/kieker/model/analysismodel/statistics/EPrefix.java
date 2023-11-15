/**
 */
package kieker.model.analysismodel.statistics;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>EPrefix</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 *
 * @see kieker.model.analysismodel.statistics.StatisticsPackage#getEPrefix()
 * @model
 * @generated
 */
public enum EPrefix implements Enumerator {
	/**
	 * The '<em><b>No P</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #NO_P_VALUE
	 * @generated
	 * @ordered
	 */
	NO_P(0, "noP", "noP"),

	/**
	 * The '<em><b>Yotta</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #YOTTA_VALUE
	 * @generated
	 * @ordered
	 */
	YOTTA(24, "yotta", "Y"),

	/**
	 * The '<em><b>Zetta</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ZETTA_VALUE
	 * @generated
	 * @ordered
	 */
	ZETTA(21, "zetta", "Z"),

	/**
	 * The '<em><b>Exa</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #EXA_VALUE
	 * @generated
	 * @ordered
	 */
	EXA(18, "exa", "E"),

	/**
	 * The '<em><b>Peta</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #PETA_VALUE
	 * @generated
	 * @ordered
	 */
	PETA(15, "peta", "P"),

	/**
	 * The '<em><b>Tera</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #TERA_VALUE
	 * @generated
	 * @ordered
	 */
	TERA(12, "tera", "T"),

	/**
	 * The '<em><b>Giga</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #GIGA_VALUE
	 * @generated
	 * @ordered
	 */
	GIGA(9, "giga", "G"),

	/**
	 * The '<em><b>Mega</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #MEGA_VALUE
	 * @generated
	 * @ordered
	 */
	MEGA(6, "mega", "M"),

	/**
	 * The '<em><b>Kilo</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #KILO_VALUE
	 * @generated
	 * @ordered
	 */
	KILO(3, "kilo", "k"),

	/**
	 * The '<em><b>Hecto</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #HECTO_VALUE
	 * @generated
	 * @ordered
	 */
	HECTO(2, "hecto", "h"),

	/**
	 * The '<em><b>Deca</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DECA_VALUE
	 * @generated
	 * @ordered
	 */
	DECA(1, "deca", "da"),

	/**
	 * The '<em><b>Deci</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DECI_VALUE
	 * @generated
	 * @ordered
	 */
	DECI(-1, "deci", "d"),

	/**
	 * The '<em><b>Centi</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #CENTI_VALUE
	 * @generated
	 * @ordered
	 */
	CENTI(-2, "centi", "c"),

	/**
	 * The '<em><b>Mili</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #MILI_VALUE
	 * @generated
	 * @ordered
	 */
	MILI(-3, "mili", "m"),

	/**
	 * The '<em><b>Micro</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #MICRO_VALUE
	 * @generated
	 * @ordered
	 */
	MICRO(-6, "micro", "mu"),

	/**
	 * The '<em><b>Nano</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #NANO_VALUE
	 * @generated
	 * @ordered
	 */
	NANO(-9, "nano", "n"),

	/**
	 * The '<em><b>Pico</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #PICO_VALUE
	 * @generated
	 * @ordered
	 */
	PICO(-12, "pico", "p"),

	/**
	 * The '<em><b>Femto</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #FEMTO_VALUE
	 * @generated
	 * @ordered
	 */
	FEMTO(-15, "femto", "f"),

	/**
	 * The '<em><b>Atto</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ATTO_VALUE
	 * @generated
	 * @ordered
	 */
	ATTO(-18, "atto", "a"),

	/**
	 * The '<em><b>Zepto</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ZEPTO_VALUE
	 * @generated
	 * @ordered
	 */
	ZEPTO(-21, "zepto", "z"),

	/**
	 * The '<em><b>Yocto</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #YOCTO_VALUE
	 * @generated
	 * @ordered
	 */
	YOCTO(-24, "yocto", "y");

	/**
	 * The '<em><b>No P</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #NO_P
	 * @model name="noP"
	 * @generated
	 * @ordered
	 */
	public static final int NO_P_VALUE = 0;

	/**
	 * The '<em><b>Yotta</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #YOTTA
	 * @model name="yotta" literal="Y"
	 * @generated
	 * @ordered
	 */
	public static final int YOTTA_VALUE = 24;

	/**
	 * The '<em><b>Zetta</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ZETTA
	 * @model name="zetta" literal="Z"
	 * @generated
	 * @ordered
	 */
	public static final int ZETTA_VALUE = 21;

	/**
	 * The '<em><b>Exa</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #EXA
	 * @model name="exa" literal="E"
	 * @generated
	 * @ordered
	 */
	public static final int EXA_VALUE = 18;

	/**
	 * The '<em><b>Peta</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #PETA
	 * @model name="peta" literal="P"
	 * @generated
	 * @ordered
	 */
	public static final int PETA_VALUE = 15;

	/**
	 * The '<em><b>Tera</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #TERA
	 * @model name="tera" literal="T"
	 * @generated
	 * @ordered
	 */
	public static final int TERA_VALUE = 12;

	/**
	 * The '<em><b>Giga</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #GIGA
	 * @model name="giga" literal="G"
	 * @generated
	 * @ordered
	 */
	public static final int GIGA_VALUE = 9;

	/**
	 * The '<em><b>Mega</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #MEGA
	 * @model name="mega" literal="M"
	 * @generated
	 * @ordered
	 */
	public static final int MEGA_VALUE = 6;

	/**
	 * The '<em><b>Kilo</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #KILO
	 * @model name="kilo" literal="k"
	 * @generated
	 * @ordered
	 */
	public static final int KILO_VALUE = 3;

	/**
	 * The '<em><b>Hecto</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #HECTO
	 * @model name="hecto" literal="h"
	 * @generated
	 * @ordered
	 */
	public static final int HECTO_VALUE = 2;

	/**
	 * The '<em><b>Deca</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DECA
	 * @model name="deca" literal="da"
	 * @generated
	 * @ordered
	 */
	public static final int DECA_VALUE = 1;

	/**
	 * The '<em><b>Deci</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #DECI
	 * @model name="deci" literal="d"
	 * @generated
	 * @ordered
	 */
	public static final int DECI_VALUE = -1;

	/**
	 * The '<em><b>Centi</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #CENTI
	 * @model name="centi" literal="c"
	 * @generated
	 * @ordered
	 */
	public static final int CENTI_VALUE = -2;

	/**
	 * The '<em><b>Mili</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #MILI
	 * @model name="mili" literal="m"
	 * @generated
	 * @ordered
	 */
	public static final int MILI_VALUE = -3;

	/**
	 * The '<em><b>Micro</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #MICRO
	 * @model name="micro" literal="mu"
	 * @generated
	 * @ordered
	 */
	public static final int MICRO_VALUE = -6;

	/**
	 * The '<em><b>Nano</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #NANO
	 * @model name="nano" literal="n"
	 * @generated
	 * @ordered
	 */
	public static final int NANO_VALUE = -9;

	/**
	 * The '<em><b>Pico</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #PICO
	 * @model name="pico" literal="p"
	 * @generated
	 * @ordered
	 */
	public static final int PICO_VALUE = -12;

	/**
	 * The '<em><b>Femto</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #FEMTO
	 * @model name="femto" literal="f"
	 * @generated
	 * @ordered
	 */
	public static final int FEMTO_VALUE = -15;

	/**
	 * The '<em><b>Atto</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ATTO
	 * @model name="atto" literal="a"
	 * @generated
	 * @ordered
	 */
	public static final int ATTO_VALUE = -18;

	/**
	 * The '<em><b>Zepto</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #ZEPTO
	 * @model name="zepto" literal="z"
	 * @generated
	 * @ordered
	 */
	public static final int ZEPTO_VALUE = -21;

	/**
	 * The '<em><b>Yocto</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @see #YOCTO
	 * @model name="yocto" literal="y"
	 * @generated
	 * @ordered
	 */
	public static final int YOCTO_VALUE = -24;

	/**
	 * An array of all the '<em><b>EPrefix</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private static final EPrefix[] VALUES_ARRAY = new EPrefix[] {
		NO_P,
		YOTTA,
		ZETTA,
		EXA,
		PETA,
		TERA,
		GIGA,
		MEGA,
		KILO,
		HECTO,
		DECA,
		DECI,
		CENTI,
		MILI,
		MICRO,
		NANO,
		PICO,
		FEMTO,
		ATTO,
		ZEPTO,
		YOCTO,
	};

	/**
	 * A public read-only list of all the '<em><b>EPrefix</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	public static final List<EPrefix> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>EPrefix</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param literal
	 *            the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EPrefix get(final String literal) {
		for (final EPrefix result : VALUES_ARRAY) {
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EPrefix</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param name
	 *            the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EPrefix getByName(final String name) {
		for (final EPrefix result : VALUES_ARRAY) {
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>EPrefix</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @param value
	 *            the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static EPrefix get(final int value) {
		switch (value) {
		case NO_P_VALUE:
			return NO_P;
		case YOTTA_VALUE:
			return YOTTA;
		case ZETTA_VALUE:
			return ZETTA;
		case EXA_VALUE:
			return EXA;
		case PETA_VALUE:
			return PETA;
		case TERA_VALUE:
			return TERA;
		case GIGA_VALUE:
			return GIGA;
		case MEGA_VALUE:
			return MEGA;
		case KILO_VALUE:
			return KILO;
		case HECTO_VALUE:
			return HECTO;
		case DECA_VALUE:
			return DECA;
		case DECI_VALUE:
			return DECI;
		case CENTI_VALUE:
			return CENTI;
		case MILI_VALUE:
			return MILI;
		case MICRO_VALUE:
			return MICRO;
		case NANO_VALUE:
			return NANO;
		case PICO_VALUE:
			return PICO;
		case FEMTO_VALUE:
			return FEMTO;
		case ATTO_VALUE:
			return ATTO;
		case ZEPTO_VALUE:
			return ZEPTO;
		case YOCTO_VALUE:
			return YOCTO;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	private EPrefix(final int value, final String name, final String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public int getValue() {
		return this.value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String getLiteral() {
		return this.literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 *
	 * @generated
	 */
	@Override
	public String toString() {
		return this.literal;
	}

} // EPrefix
