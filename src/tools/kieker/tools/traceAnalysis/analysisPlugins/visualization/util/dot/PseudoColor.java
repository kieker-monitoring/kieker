package kieker.tools.traceAnalysis.analysisPlugins.visualization.util.dot;

import java.awt.Color;

/**
 * This class provides methods to manage pseudo colors.
 * Pseudo colors are calculated by mapping the values of data sets to a pre-defined spectrum of colors.
 * For example, a height map could be visualized in greyscale,
 * or a temperature map could be drawn in cold-to-warm colors.
 * In the <code>Correlator</code>, the color is based on the anomaly cause rating of the structure elements.
 * @see <a href="http://en.wikipedia.org/wiki/False-color">False-color - Wikipedia</a>
 * 
 * @author Nina
 */
class PseudoColor {

private static final int COLOR_MIN = 95;        // lower this to get higher contrast
private static final int COLOR_MAX = 255;       // lower this to get lower intensity (you won't want this)
private static final int COLOR_DIFF;

private static double[] minima = new double[3]; /** @todo store these somewhere else, maybe static per level */
private static double[] maxima = new double[3];
private static double[] diffs = new double[3];

private static double min, max, diff;  // temporary variables

private static boolean stretchToFullSpectrum = false;

static {
    COLOR_DIFF = COLOR_MAX - COLOR_MIN;
}

/**
 * Private empty constructor to mark this class as not useful to instantiate because is has only static members.
 */
private PseudoColor(){
}

/**
 * Initialization for color stretching.
 * The parameters can be created by using <code>Util.extendExtrema</code>.
 * @param op smallest and largest expected value for operation level, as well as their difference
 * @param comp smallest and largest expected value for component level, as well as their difference
 * @param dc smallest and largest expected value for deployment context level, as well as their difference
 * @throws IllegalArgumentException
 */
static void init( double[] op, double[] comp, double[] dc ){
    if( COLOR_MAX < COLOR_MIN || COLOR_MIN < 0 || COLOR_MAX > 255 ){
        throw new RuntimeException( "Error in internal PseudoColor configuration." );
    }
    for( int level = Util.STRUCTURE_OPERATION; level <= Util.STRUCTURE_DEPLOYMENTCONTEXT; level++ ){
        switch( level ){
            case Util.STRUCTURE_OPERATION:
                minima[level] = op[Util.MIN];
                maxima[level] = op[Util.MAX];
                diffs[level] = op[Util.DIFF];
                break;
            case Util.STRUCTURE_COMPONENT:
                minima[level] = comp[Util.MIN];
                maxima[level] = comp[Util.MAX];
                diffs[level] = comp[Util.DIFF];
                break;
            case Util.STRUCTURE_DEPLOYMENTCONTEXT:
                minima[level] = dc[Util.MIN];
                maxima[level] = dc[Util.MAX];
                diffs[level] = dc[Util.DIFF];
                break;
            default:
                throw new IndexOutOfBoundsException( "Invalid level: " + level + " (This should not happen.)" );
        }
        if( maxima[level] < minima[level] ){
            throw new IllegalArgumentException( "Minimum value (" + minima[level] + ") must not be greater than maximum value (" + maxima[level] + ")" );
        }
        if( minima[level] < -1.0 || maxima[level] > 1.0 ){
            throw new IllegalArgumentException( "Argument(s) out of range: " + minima[level] + ", " + maxima[level] + "  (must be in [-1 1])" );
        }
    }
}

/**
 * Sets the flag whether to stretch colors to full spectrum, so that at least
 * one element is full green, and at least one other is full red.
 * Otherwise it is possible that many elements with similar ratings
 * get similar colors in the middle of the spectrum.
 * @param stretch
 */
static void setColorStretching( boolean stretch ){
    stretchToFullSpectrum = stretch;
}

/**
 * Maps a rating in <code>[-1,1]</code> to a color from green over yellow and orange to red.
 * The range is limited by hierarchy level minima and maxima.
 * <pre>
 *  yellow..orange..red:	R 255		G 255..0	B 0
 *  yellow..green:			R 255..0	G 255		B 0
 *   more light: higher low values
 *   less satur: higher low values & lower high values -- e.g. 63..222
 * </pre>
 * @param level hierarchy level in graph as defined by the STRUCTURE constants in {@link Util};
 *      use -1 for unlimited boundaries [-1,1]
 * @param value rating in level's boundaries, or [-1,1] if level==-1
 * @return color object
 */
static Color getColor( int level, double value ){
    min = level == -1 ? -1.0 : minima[level];
    max = level == -1 ? 1.0 : maxima[level];
    diff = level == -1 ? 2.0 : diffs[level];
    if( value < min || value > max ){
        throw new IllegalArgumentException( "Argument out of range: " + value + "  (must be in [" + min + " " + max + "])" );
    }
    if( stretchToFullSpectrum ){
        value = ( value - min ) / diff;                                 // stretch to [0,1]
        value = Util.scalePercentToRating( value );                     // stretch to [-1,1]
    }
    int color;
    if( value > 0.0 ){              // upper half -- yellow..orange..red -- decrease green
        color = COLOR_MIN + (int) ( COLOR_DIFF * ( 1.0 - value ) );     // stretch to [COLOR_MIN,COLOR_MAX]
        return new Color( COLOR_MAX, color, COLOR_MIN );
    }
    else{                           // lower half -- yellow..green -- decrease red
        color = COLOR_MIN + (int) ( COLOR_DIFF * ( 1.0 + value ) );     // stretch to [COLOR_MIN,COLOR_MAX]
        return new Color( color, COLOR_MAX, COLOR_MIN );
    }
}

/**
 * Converts a <code>Color</code> object to hexadecimal string.
 * @param color color to convert
 * @return hexadecimal RGB color string, e.g. "#de583e"
 */
static String getHexColorString( Color color ){
    return "#" + intColorToHex( color.getRed() ) + intColorToHex( color.getGreen() ) + intColorToHex( color.getBlue() );
}

/**
 * Converts a monochrome color value from integer [0,255] to hexadecimal string ["0","FF"].
 * @param color single color value
 * @return hexadecimal string
 */
private static String intColorToHex( int color ){
	return color < 0x10 ? "0" + Integer.toHexString( color ) : Integer.toHexString( color );
}

/**
 * Returns constant hex string RGB values.
 */
private static String booleanToRed( boolean value ){
	return value ? "#ff5f5f" : "#ffffff";
}

/**
 * Maps a percent double value to a hex value that can be used in a color string.
 * E.g. 49% is mapped to 7f -- the higher, the darker.
 */
private static String percentToColor( double value ){
	int color = COLOR_MIN + (int)( COLOR_DIFF * ( 1.0 - value ) );
	return color < 0x10 ? "0" + Integer.toHexString( color ) : Integer.toHexString( color );
}

}
