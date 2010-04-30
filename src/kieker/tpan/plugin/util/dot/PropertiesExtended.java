package kieker.tpan.plugin.util.dot;

import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * This class is an extended version of the standard {@link java.util.Properties}.
 * Additional methods are provided to fetch Integer and Double objects,
 * and to check them for consistency.
 *
 * @author Nina
 */
class PropertiesExtended {

/**
 * The PROPERTY_TYPE constants can be used to select the type of property.
 * A color is a string of length 7, starting with a sharp, as in &quot;#FFCC66&quot;.
 */
static final int PROPERTY_TYPE_STRING = 0;
static final int PROPERTY_TYPE_COLOR  = 1;
static final int PROPERTY_TYPE_INT    = 2;
static final int PROPERTY_TYPE_DOUBLE = 3;

private Properties props = new Properties();
private Map<String, Double> propsDouble;
private Map<String, Integer> propsInt;

/**
 * Loads and stores properties from the specified file.
 * Priority values override default values.
 * @param filename path to properties file
 * @param priority properties to take high priority values from
 */
PropertiesExtended( String filename, Properties priority ){
    FileReader reader = null;
    InputStream stream = null;
    try{
        File file = new File( filename );
        if( file.isAbsolute() && file.isFile() ){
            reader = new FileReader( file );
            props.load( reader );
        }
        else{
            stream = ClassLoader.getSystemClassLoader().getResourceAsStream( filename );
            props.load( stream );
        }
    }
    catch( Exception ex ){
        throw new RuntimeException( "Failed loading properties from file " + filename + " -- Exception: " + ex.getMessage() );
    }
    finally{
        try{
            reader.close();
        }
        catch( Exception ex ){
            /* ignore */
        }
        try{
            stream.close();
        }
        catch( Exception ex ){
            /* ignore */
        }
    }
    loadExtended( priority );
}

/**
 * Imports properties.
 * Priority values override default values.
 * @param properties properties to load defaults from
 * @param priority properties to take high priority values from
 */
PropertiesExtended( Properties properties, Properties priority ){
    this.props = properties;
    loadExtended( priority );
}

/**
 * Fetches double and int properties from the general properties into separate tables.
 * @param priority properties to take high priority values from
 */
private void loadExtended( Properties priority ){
    propsDouble = new HashMap<String, Double>();
    propsInt = new HashMap<String, Integer>();
    String value;
	for( String key : props.stringPropertyNames() ){
        value = priority == null ? null : priority.getProperty( key );
        if( value == null ){
            value = props.getProperty( key );   // no priority value for this key; load normal
        }
        else{
            props.setProperty( key, value );    // priority override!
        }
		try{
            propsDouble.put( key, Double.valueOf( value ) );    // order (1. dbl, 2. int) is important here !
            propsInt.put( key, Integer.valueOf( value ) );      // because we expect NumberFormatExceptions !
        }
        catch( NumberFormatException ex ){
            continue;                           // try next
		}
	}
}

/**
 * Fetches the value of a Double property.
 * @param key key
 * @return value; Double.NaN if not found
 */
double getDoubleProp( String key ){
	Double dbl = propsDouble.get( key );
	return dbl == null ? Double.NaN : dbl.doubleValue();
}

/**
 * Fetches the value of an Integer property.
 * @param key key
 * @return value; Integer.MIN_VALUE if not found
 */
int getIntProp( String key ){
	Integer itg = propsInt.get( key );
	return itg == null ? Integer.MIN_VALUE : itg.intValue();
}

/**
 * Fetches the value of a String property.
 * @param key key
 * @return value; null if not found
 */
String getStringProp( String key ){
    return props.getProperty( key );
}

/**
 * Check a single property for consistency.
 * @param type type that the property should be interpreted as
 * @param key key to the property
 * @return false if property value violates consistency, true else
 */
boolean checkProperty( int type, String key ){
    if( props.getProperty( key ) == null || props.getProperty( key ).isEmpty() ){
        Util.writeOut( Util.LEVEL_ERROR, "< ERROR: Failed to read property: " + key );
        return false;
    }
    switch( type ){
        case PROPERTY_TYPE_STRING:
            // nothing to do
            break;
        case PROPERTY_TYPE_COLOR:
            if( props.getProperty( key ).length() != 7 || !props.getProperty( key ).startsWith( "#" ) ){
                Util.writeOut( Util.LEVEL_ERROR, "< ERROR: Failed to read color property: " + key );
                return false;
            }
            break;
        case PROPERTY_TYPE_INT:
            if( getIntProp( key ) == Integer.MIN_VALUE ){
                Util.writeOut( Util.LEVEL_ERROR, "< ERROR: Failed to read int property: " + key );
                return false;
            }
            break;
        case PROPERTY_TYPE_DOUBLE:
            if( Double.isNaN( getDoubleProp( key ) ) ){
                Util.writeOut( Util.LEVEL_ERROR, "< ERROR: Failed to read double property: " + key );
                return false;
            }
            break;
        default:
            Util.writeOut( Util.LEVEL_WARNING, "< WARNING: Property type not recognized: type " + type );
    }
    return true;
}

}
