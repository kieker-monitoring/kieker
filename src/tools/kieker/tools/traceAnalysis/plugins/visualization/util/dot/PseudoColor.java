/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.tools.traceAnalysis.plugins.visualization.util.dot;

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

private static double[] minima = new double[3]; /** @todo store these somewhere else, maybe static per level. See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/133*/
private static double[] maxima = new double[3];
private static double[] diffs = new double[3];

private static double min, max, diff;  // temporary variables

private static boolean stretchToFullSpectrum = false;

static {
    COLOR_DIFF = PseudoColor.COLOR_MAX - PseudoColor.COLOR_MIN;
}

/**
 * Private empty constructor to mark this class as not useful to instantiate because is has only static members.
 */
private PseudoColor(){
}

//See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/132
///**
// * Initialization for color stretching.
// * The parameters can be created by using <code>Util.extendExtrema</code>.
// * @param op smallest and largest expected value for operation level, as well as their difference
// * @param comp smallest and largest expected value for component level, as well as their difference
// * @param dc smallest and largest expected value for deployment context level, as well as their difference
// * @throws IllegalArgumentException
// */
//static void init( final double[] op, final double[] comp, final double[] dc ){
//	// FIXME: Dead code (ticket #82)
//    if( (PseudoColor.COLOR_MAX < PseudoColor.COLOR_MIN) || (PseudoColor.COLOR_MIN < 0) || (PseudoColor.COLOR_MAX > 255) ){
//        throw new RuntimeException( "Error in internal PseudoColor configuration." );
//    }
//    for( int level = Util.STRUCTURE_OPERATION; level <= Util.STRUCTURE_DEPLOYMENTCONTEXT; level++ ){
//        switch( level ){
//            case Util.STRUCTURE_OPERATION:
//                PseudoColor.minima[level] = op[Util.MIN];
//                PseudoColor.maxima[level] = op[Util.MAX];
//                PseudoColor.diffs[level] = op[Util.DIFF];
//                break;
//            case Util.STRUCTURE_COMPONENT:
//                PseudoColor.minima[level] = comp[Util.MIN];
//                PseudoColor.maxima[level] = comp[Util.MAX];
//                PseudoColor.diffs[level] = comp[Util.DIFF];
//                break;
//            case Util.STRUCTURE_DEPLOYMENTCONTEXT:
//                PseudoColor.minima[level] = dc[Util.MIN];
//                PseudoColor.maxima[level] = dc[Util.MAX];
//                PseudoColor.diffs[level] = dc[Util.DIFF];
//                break;
//            default:
//                throw new IndexOutOfBoundsException( "Invalid level: " + level + " (This should not happen.)" );
//        }
//        if( PseudoColor.maxima[level] < PseudoColor.minima[level] ){
//            throw new IllegalArgumentException( "Minimum value (" + PseudoColor.minima[level] + ") must not be greater than maximum value (" + PseudoColor.maxima[level] + ")" );
//        }
//        if( (PseudoColor.minima[level] < -1.0) || (PseudoColor.maxima[level] > 1.0) ){
//            throw new IllegalArgumentException( "Argument(s) out of range: " + PseudoColor.minima[level] + ", " + PseudoColor.maxima[level] + "  (must be in [-1 1])" );
//        }
//    }
//}

/**
 * Sets the flag whether to stretch colors to full spectrum, so that at least
 * one element is full green, and at least one other is full red.
 * Otherwise it is possible that many elements with similar ratings
 * get similar colors in the middle of the spectrum.
 * @param stretch
 */
static void setColorStretching( final boolean stretch ){
    PseudoColor.stretchToFullSpectrum = stretch;
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
static Color getColor( final int level, double value ){
    PseudoColor.min = level == -1 ? -1.0 : PseudoColor.minima[level];
    PseudoColor.max = level == -1 ? 1.0 : PseudoColor.maxima[level];
    PseudoColor.diff = level == -1 ? 2.0 : PseudoColor.diffs[level];
    if( (value < PseudoColor.min) || (value > PseudoColor.max) ){
        throw new IllegalArgumentException( "Argument out of range: " + value + "  (must be in [" + PseudoColor.min + " " + PseudoColor.max + "])" );
    }
    if( PseudoColor.stretchToFullSpectrum ){
        value = ( value - PseudoColor.min ) / PseudoColor.diff;                                 // stretch to [0,1]
        value = Util.scalePercentToRating( value );                     // stretch to [-1,1]
    }
    int color;
    if( value > 0.0 ){              // upper half -- yellow..orange..red -- decrease green
        color = PseudoColor.COLOR_MIN + (int) ( PseudoColor.COLOR_DIFF * ( 1.0 - value ) );     // stretch to [COLOR_MIN,COLOR_MAX]
        return new Color( PseudoColor.COLOR_MAX, color, PseudoColor.COLOR_MIN );
    }
    else{                           // lower half -- yellow..green -- decrease red
        color = PseudoColor.COLOR_MIN + (int) ( PseudoColor.COLOR_DIFF * ( 1.0 + value ) );     // stretch to [COLOR_MIN,COLOR_MAX]
        return new Color( color, PseudoColor.COLOR_MAX, PseudoColor.COLOR_MIN );
    }
}

/**
 * Converts a <code>Color</code> object to hexadecimal string.
 * @param color color to convert
 * @return hexadecimal RGB color string, e.g. "#de583e"
 */
static String getHexColorString( final Color color ){
    return "#" + PseudoColor.intColorToHex( color.getRed() ) + PseudoColor.intColorToHex( color.getGreen() ) + PseudoColor.intColorToHex( color.getBlue() );
}

/**
 * Converts a monochrome color value from integer [0,255] to hexadecimal string ["0","FF"].
 * @param color single color value
 * @return hexadecimal string
 */
private static String intColorToHex( final int color ){
	return color < 0x10 ? "0" + Integer.toHexString( color ) : Integer.toHexString( color );
}

/**
 * Returns constant hex string RGB values.
 */
//private static String booleanToRed( final boolean value ){
//	return value ? "#ff5f5f" : "#ffffff";
//}
//See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/132

/**
 * Maps a percent double value to a hex value that can be used in a color string.
 * E.g. 49% is mapped to 7f -- the higher, the darker.
 */
//private static String percentToColor( final double value ){
//	final int color = PseudoColor.COLOR_MIN + (int)( PseudoColor.COLOR_DIFF * ( 1.0 - value ) );
//	return color < 0x10 ? "0" + Integer.toHexString( color ) : Integer.toHexString( color );
//}
//See ticket http://samoa.informatik.uni-kiel.de:8000/kieker/ticket/132


}
