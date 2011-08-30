package kieker.tools.traceAnalysis.plugins.visualization.util.dot;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * This class provides a bunch of useful static functions for use in different
 * places of the plug-in.
 * Along with various constants, there are methods for debug output,
 * for mathematics, for String manipulation, and for file system access.
 * 
 * @author Nina
 */
public class Util {

	/**
	 * The LEVEL constants let control the level of detail for debug messaging.
	 * CRITICAL gives the least output, VERBOSE gives the most.
	 */
	static final int LEVEL_CRITICAL = 0;
	static final int LEVEL_ERROR = 1;
	static final int LEVEL_WARNING = 2;
	static final int LEVEL_INFO = 3;
	static final int LEVEL_VERBOSE = 4;

	/**
	 * The STRUCTURE constants help to select the hierarchy level in the dependency structure.
	 */
	public static final int STRUCTURE_OPERATION = 0;
	public static final int STRUCTURE_COMPONENT = 1;
	public static final int STRUCTURE_DEPLOYMENTCONTEXT = 2;

	/**
	 * The MIN, MAX, and DIFF constants are used to identify special array fields.
	 */
	static final int MIN = 0;
	static final int MAX = 1;
	static final int DIFF = 2;

	/**
	 * The MEAN constants help to select a method for mean calculation.
	 */
	static final int MEAN_POWERMEAN = 0;
	static final int MEAN_MEDIAN = 1;
	static final int MEAN_MAXIMUM = 2;

	public static final int INVALID = -1;

	private static int debugLevel = LEVEL_INFO; // init with INFO to help debug initialization problems
	private static int logLevel = LEVEL_VERBOSE;
	private static PrintStream debugOut = System.out;
	private static PrintWriter logFile = null;

	/**
	 * Private empty constructor to mark this class as not useful to instantiate because is has only static members.
	 */
	private Util() {
	}

	/**
	 * Sets the plug-in-global debug level and log level to the specified values,
	 * and defines a stream as well as a log file name to be targets for the
	 * output on screen resp. on file system.
	 * Usage of the pre-defined LEVEL constants is highly recommended.
	 * 
	 * @param debug debug detail level
	 * @param log log detail level
	 * @param debugOutput stream to
	 * @param logFilename stream to write lo
	 * @throws Exception if the log file can't be initialized
	 */
	static void initOutput(int debug, int log, PrintStream debugOutput, String logFilename) throws Exception {
		debugLevel = debug;
		logLevel = log;
		if (debugOutput != null) {
			debugOut = debugOutput;
		}
		if (!canWriteToPathOfFile(logFilename)) {
			throw new IOException("Cannot write to path of file: " + logFilename);
		}
		logFile = new PrintWriter(new FileOutputStream(logFilename, true));
		logFile.println();
		logFile.println("Log file opened at " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + ".");
	}

	/**
	 * Closes the log file after appending time stamp.
	 */
	static void closeLogFile() {
		if (logFile != null) {
			logFile.println("Log file closed at " + (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(new Date()) + ".");
			logFile.println();
			logFile.close();
		}
	}

	/**
	 * Flushes the log file.
	 */
	static void flushLogFile() {
		if (logFile != null) {
			logFile.flush();
		}
	}

	/**
	 * Writes status messages to the output stream and to the log file
	 * considering the debug level specified in the properties file.
	 * Usage of the pre-defined LEVEL constants is highly recommended.
	 * 
	 * <pre>
	 * 0 critical,
	 * 1 error,
	 * 2 warning,
	 * 3 info,
	 * 4 verbose.
	 * </pre>
	 * 
	 * @param level debug level
	 * @param msg message to write
	 */
	static void writeOut(int level, String msg) {
		if (level <= debugLevel) {
			debugOut.println(msg);
		}
		if (level <= logLevel && logFile != null) {
			logFile.println(msg);
		}
	}

	/**
	 * Converts a value in [-1.0,1.0] to a value in [0.0,1.0].
	 * 
	 * @param rating rating in [-1,1]
	 * @return percentage in [0,1]
	 * @throws IllegalArgumentException if the input is out of range
	 */
	static double scaleRatingToPercent(double rating) {
		if (rating < -1.0 || rating > 1.0)
			throw new IllegalArgumentException("Argument out of range [-1,1]: " + rating);
		return rating / 2.0 + 0.5;
	}

	/**
	 * Converts a value in [0.0,1.0] to a value in [-1.0,1.0] - usually a <em>rating</em>.
	 * 
	 * @param percent percentage in [0,1]
	 * @return value in [-1,1]
	 * @throws IllegalArgumentException if the input is out of range
	 */
	static double scalePercentToRating(double percent) {
		if (percent < 0.0 || percent > 1.0)
			throw new IllegalArgumentException("Argument out of range [0,1]: " + percent);
		return percent * 2.0 - 1.0;
	}

	/**
	 * Calls Math.pow(a,b) but retains the original sign of a
	 * so that <code>powRetainSign( -2, 2 ) == -4</code>.
	 * 
	 * @param a the base
	 * @param b the exponent
	 * @return the value <code>sig(a) * abs(a)<sup>b</sup></code>
	 */
	static double powRetainSign(double a, double b) {
		return Math.signum(a) * Math.pow(Math.abs(a), b);
	}

	/**
	 * Tests if all values in the specified array are equal. <code>Double.NaN</code> values are ignored.
	 * 
	 * @param array array of double values to be compared with each other
	 * @return true if all values are equal
	 */
	static boolean allEqual(double[] array) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] != array[0] && !Double.isNaN(array[i])) { // search for unequal values
				return false;
			}
		}
		return true;
	}

	/**
	 * Creates a new array that contains each element from data
	 * as many times as given in the respective factor.
	 * Example: <code>{ 1, 2, 3 } { 2, 2, 1 }</code> will create <code>{ 1, 1, 2, 2, 3 }</code>
	 */
	@Deprecated
	private static double[] arrayMultiplyCopy(double[] data, int[] factors) {
		int i, j, k = 0, factorSum = 0;
		for (int factor : factors) {
			factorSum += factor;
		}
		double[] result = new double[factorSum];
		for (i = 0; i < data.length; i++, k += j) {
			for (j = 0; j < factors[i]; j++) {
				result[k + j] = data[i];
			}
		}
		return result;
	}

	/**
	 * Converts an array from double to int values, rounding them up or down.
	 * This is needed in preparation for the weighted median expansion.
	 * 
	 * @param doubles array of double values
	 * @return array of int values
	 */
	private static int[] doubleArrayToIntArray(double[] doubles) {
		int[] ints = new int[doubles.length];
		for (int i = 0; i < doubles.length; i++) {
			ints[i] = (int) (doubles[i] + 0.5);
		}
		return ints;
	}

	/**
	 * Copies the values of a collection of <code>Long</code>s to a new list of <code>Double</code>s.
	 * Initially needed for the creation of histograms from response times.
	 * 
	 * @param longs collection of <code>Long</code> values
	 * @return list of <code>Double</code> values
	 */
	static List<Double> longListToDoubleList(Collection<Long> longs) {
		List<Double> doubles = new ArrayList<Double>(longs.size());
		for (Long l : longs) {
			doubles.add(l.doubleValue());
		}
		return doubles;
	}

	/**
	 * Creates a copy of the specified array that is two elements longer.
	 * The first and the last element of the new array are initialized with 0.0.
	 * 
	 * @param values array to copy
	 * @return array with each a zero at first and at last
	 */
	private static double[] padWithZeroes(double[] values) {
		double[] result = new double[values.length + 2];
		// result[ 0 ] = result[ values.length + 1 ] = 0.0; // does FindBugs yell on this? ;-)
		for (int i = 0; i < values.length; i++) {
			result[i + 1] = values[i];
		}
		return result;
	}

	/**
	 * Calculates the median of an array of samples.
	 * The median is the number separating the higher half form the lower half.
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Median">Median - Wikipedia</a>
	 * @param samples array of sample values
	 * @param weights array of weights for each sample; can be null; otherwise length must be equal to samples length.
	 *            The weights should be significantly greater than 1.0, because they are converted to integers.
	 * @return the median of the sample values
	 */
	@Deprecated
	static double calculateMedianOld(double[] samples, double[] weights) {
		if (samples.length == 0) {
			throw new IllegalArgumentException("Array length 0 not supported by median calculation.");
		}
		if (samples.length == 1 || allEqual(samples)) {
			return samples[0]; // no need for further calculation
		}
		if (weights != null) {
			if (weights.length != samples.length) {
				throw new IllegalArgumentException("Array lengths must be equal in median calculation.");
			}
			if (!allEqual(weights)) {
				samples = arrayMultiplyCopy(samples, doubleArrayToIntArray(weights)); // expand them
			}
		}
		Arrays.sort(samples);
		if (samples.length % 2 == 0) {
			return (samples[samples.length / 2 - 1] + samples[samples.length / 2]) / 2.0;
		}
		return samples[samples.length / 2];
	}

	/**
	 * Calculates the weighted median of an array of samples.
	 * The median is the number separating the higher half form the lower half.
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Median">Median - Wikipedia</a>
	 * @param samples array of sample values
	 * @param weights array of weights for each sample; can be null; otherwise length must be equal to samples length.
	 *            The weights should be significantly greater than 1.0, because they are converted to integers.
	 * @return weighted median of the sample values
	 */
	static double calculateMedian(double[] samples, double[] weights) {
		if (samples.length == 0) {
			throw new IllegalArgumentException("Array length 0 not supported by median calculation.");
		}
		if (samples.length == 1 || allEqual(samples)) {
			return samples[0]; // no need for further calculation
		}
		if (weights != null) {
			if (weights.length != samples.length) {
				throw new IllegalArgumentException("Array lengths must be equal in median calculation.");
			}
			if (!allEqual(weights)) {
				SampleAndWeight[] saw = new SampleAndWeight[samples.length];
				for (int i = 0; i < samples.length; i++) {
					saw[i] = new SampleAndWeight();
					saw[i].sample = samples[i];
					saw[i].weight = weights[i];
				}
				Arrays.sort(saw);
				double[] cumulativeWeightSums = new double[samples.length];
				cumulativeWeightSums[0] = saw[0].weight;
				for (int i = 1; i < samples.length; i++) {
					cumulativeWeightSums[i] = cumulativeWeightSums[i - 1] + saw[i].weight;
				}
				double half = cumulativeWeightSums[samples.length - 1] / 2.0;
				for (int i = 0; i < samples.length; i++) {
					int cmp = Double.compare(cumulativeWeightSums[i], half);
					if (cmp == 0) {
						assert i + 1 < samples.length;
						return (saw[i].sample + saw[i + 1].sample) / 2.0; // average of two values
					}
					if (cmp > 0) {
						return saw[i].sample;
					}
				}
				assert false : "Execution should never reach this point.";
			}
		}
		// we have no weights (or they are all equal) -- perform simple median
		Arrays.sort(samples);
		if (samples.length % 2 == 0) { // length even? -> calculate average of the two middle values
			double left = samples[samples.length / 2 - 1];
			double right = samples[samples.length / 2];
			return (left + right) / 2.0;
		}
		return samples[samples.length / 2];
	}

	/**
	 * Calculates the power mean of an array of samples.
	 * An exponent of 1 means arithmetic mean, while 2 means root mean square.
	 * Values greater than 1 emphasize outliers, while values smaller than 1 reduce their influence.
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Generalized_mean">Generalized mean - Wikipedia</a>
	 * @see <a href="http://en.wikipedia.org/wiki/Root_mean_square">Root mean square - Wikipedia</a>
	 * @param samples array of sample values
	 * @param weights array of weights for each sample; can be null; otherwise length must be equal to samples length
	 * @param exp positive exponent for power mean
	 * @return the power mean of the sample values
	 */
	static double calculatePowerMean(double[] samples, double[] weights, double exp) {
		if (samples.length == 0) {
			throw new IllegalArgumentException("Array length 0 not supported by power mean calculation.");
		}
		if (exp <= 0.0) {
			throw new IllegalArgumentException("Power mean exponent must be positive.");
		}
		double dividend = 0.0;
		double divisor = 0.0;
		if (weights == null) {
			for (int i = 0; i < samples.length; i++) {
				dividend += powRetainSign(samples[i], exp);
			}
			divisor = samples.length;
		} else {
			if (weights.length != samples.length) {
				throw new IllegalArgumentException("Array lengths must be equal in power mean calculation.");
			}
			for (int i = 0; i < samples.length; i++) {
				dividend += weights[i] * powRetainSign(samples[i], exp);
				divisor += weights[i];
			}
		}
		return powRetainSign(dividend / divisor, 1.0 / exp);
	}

	/**
	 * Wrapper method for unweighted power mean with exponent 1.0.
	 * 
	 * @param samples array of sample values
	 * @return the arighmetic mean of the samples
	 */
	static double calculateArithmeticMean(double[] samples) {
		return calculatePowerMean(samples, null, 1.0);
	}

	/**
	 * Determines the smallest value in an array of sample values.
	 * 
	 * @param samples array of sample values
	 * @return minimum of samples
	 */
	static double getMin(double[] samples) {
		if (samples.length == 0) {
			throw new IllegalArgumentException("Array length 0 not supported by minimum calculation.");
		}
		double min = Double.POSITIVE_INFINITY;
		for (double sample : samples) {
			if (sample < min) {
				min = sample;
			}
		}
		return min;
	}

	/**
	 * Determines the largest value in an array of sample values.
	 * 
	 * @param samples array of sample values
	 * @return maximum of samples
	 */
	static double getMax(double[] samples) {
		if (samples.length == 0) {
			throw new IllegalArgumentException("Array length 0 not supported by maximum calculation.");
		}
		double max = Double.NEGATIVE_INFINITY;
		for (double sample : samples) {
			if (sample > max) {
				max = sample;
			}
		}
		return max;
	}

	/**
	 * Determines the smallest value in a <code>List</code> of sample values.
	 * 
	 * @param samples array of sample values
	 * @return minimum of samples
	 */
	static double getMin(Collection<Double> samples) {
		if (samples.isEmpty()) {
			throw new IllegalArgumentException("Array length 0 not supported by minimum calculation.");
		}
		double min = Double.POSITIVE_INFINITY;
		for (Double sample : samples) {
			if (sample < min) {
				min = sample.doubleValue();
			}
		}
		return min;
	}

	/**
	 * Determines the largest value in a <code>List</code> of sample values.
	 * 
	 * @param samples array of sample values
	 * @return maximum of samples
	 */
	static double getMax(Collection<Double> samples) {
		if (samples.isEmpty()) {
			throw new IllegalArgumentException("Array length 0 not supported by maximum calculation.");
		}
		double max = Double.NEGATIVE_INFINITY;
		for (Double sample : samples) {
			if (sample > max) {
				max = sample.doubleValue();
			}
		}
		return max;
	}

	/**
	 * Constructs a histogram from the specified list of numbers.
	 * Effectively sorts the numbers into an array of buckets according to their frequency.
	 * The values are then stretched to [0.0,1.0].
	 * 
	 * @param data list of numbers to sort
	 * @param buckets number of buckets (columns in the histogram)
	 * @return array of percentages [0.0,1.0]
	 * @throws IllegalArgumentException if data list is empty, or if number of buckets is smaller than 1
	 */
	static double[] createHistogram(List<Double> data, int buckets) {
		double[] histogram = new double[buckets];
		if (data.isEmpty() || buckets < 1) {
			throw new IllegalArgumentException("Data list empty, or number of buckets smaller than 1.");
		}
		int column;
		double perc;
		double min = getMin(data);
		double max = getMax(data);
		double diff = max - min;
		for (Double value : data) {
			perc = (value - min) / diff; // stretch to [ 0.0, 1.0 ] horizontally
			column = (int) (perc * (buckets - 1)); // find column in [ 0, n-1 ]
			histogram[column]++; // increase column
		}
		double maxValue = getMax(histogram);
		for (column = 0; column < buckets; column++) {
			histogram[column] /= maxValue; // stretch to [ 0.0, 1.0 ] vertically
		}
		return histogram;
	}

	/**
	 * Calculates the clearness function $clear(r_{1..n}) := \frac{r_1}{\sum_{i=2}^n \frac{r_i}{i}+1}$
	 * that is a measure for the quality of the analysis.
	 * 
	 * @param samples list of sample values, e.g. cause rating percents
	 * @return degree of clearness, &gt; 0
	 */
	static double calculateClearness(double[] samples) {
		if (samples == null || samples.length < 1) {
			throw new IllegalArgumentException("Array has to contain at least one element.");
		}
		double[] ratings = removeNaN(samples);
		Arrays.sort(ratings); // sort ascending
		double sum = 1; // +1 to prevent div by zero
		for (int i = 0; i < ratings.length - 1; i++) {
			sum += ratings[i] / (ratings.length - i); // add rating/index
		}
		return ratings[ratings.length - 1] / sum;
	}

	/**
	 * Copies the specified array, but omits all NaN values.
	 * 
	 * @param samples array of sample values
	 * @return cleaned array, may be smaller than original array
	 */
	private static double[] removeNaN(double[] samples) {
		double[] result = new double[samples.length];
		int i, j = 0;
		for (i = 0; i < samples.length; i++) {
			if (!Double.isNaN(samples[i])) {
				result[j] = samples[i];
				j++;
			}
		}
		return Arrays.copyOf(result, j);
	}

	/**
	 * Expands special fields of the specicied array, if the value is greater than the
	 * up-to-now greatest value resp. smaller than the smallest value in the array.
	 * This method can be repeatedly invoked for a large number of values (and the same array)
	 * to get their smalles and greatest overall values.
	 * 
	 * @param value value to test the array against
	 * @param limits array to be changed if necessary
	 */
	static void extendLimits(double value, double[] limits) {
		if (value < limits[MIN]) {
			limits[MIN] = value;
		}
		if (value > limits[MAX]) {
			limits[MAX] = value;
		}
	}

	/**
	 * Inhibition function based on Michaelis-Menten kinetics.
	 * The path of the curve starts almost linear, then converges to a horizontal at 1.0.
	 * Complexer variant: I(v) = v^a / ( v^a + b^a )
	 * 
	 * <pre>
	 * inhibit( x ) &lt; x
	 * inhibit( 0.001 ) == 0.000999
	 * inhibit( 0.1 ) == 0.090909
	 * inhibit( 1 ) == 0.5
	 * inhibit( 2 ) == 0.66667
	 * inhibit( 1000 ) == 0.9990
	 * </pre>
	 * 
	 * @see <a href="http://en.wikipedia.org/wiki/Michaelis-Menten_kinetics">Michaelis-Menten kinetics - Wikipedia</a>
	 */
	static double inhibit(double v) {
		if (v < 0) {
			throw new IllegalArgumentException("Inhibit requires non-negative operand.");
		}
		return v / (v + 1.0);
	}

	/**
	 * Calculates a sigmoid function with hard-coded form parameters.
	 * 
	 * @param v value in [0.0,1.0]
	 * @return result in [0.0,1.0]
	 */
	static double sigmoid(double v) {
		if (v < 0.0 || v > 1.0) {
			throw new IllegalArgumentException("Parameter out of range [0.0,1.0]: " + v);
		}
		// return 1.022 / ( 1 + 90.0 * Math.exp( v * -9.0 ) ) - 0.011;
		return 1.104 / (1 + 20.0 * Math.exp(v * -6.0)) - 0.0524; // sinus-like
	}

	/**
	 * Calculates a sigmoid-like function that hits exaxtly [0,1].
	 * 
	 * @param v value in [0.0,1.0]
	 * @return result in [0.0,1.0]
	 */
	static double sinusFragment(double v) {
		return scaleRatingToPercent(Math.sin(Math.PI * (v - 0.5)));
	}

	/**
	 * Reduces the parameters of an operation signature
	 * to each their last component.
	 * Example: <code>operation(java.lang.String,int,int)</code> is reduced to <code>operation(String,int,int)</code>
	 * 
	 * @param signature operation signature
	 * @return shortened signature
	 */
	static String reduceParameters(String signature) {
		if (signature == null || signature.isEmpty() || !signature.endsWith(")")) {
			return signature;
		}
		int openingBracket = signature.lastIndexOf("(") + 1;
		if (openingBracket == signature.length() - 1) {
			return signature;
		}
		String[] parameters = signature.substring(openingBracket, signature.length() - 1).split(",");
		StringBuilder newParameters = new StringBuilder();
		for (String par : parameters) {
			if (!par.isEmpty()) {
				newParameters.append("," + par.substring(par.lastIndexOf(".") + 1));
			}
		}
		return signature.substring(0, openingBracket) + newParameters.substring(1) + ")";
	}

	/**
	 * Creates a prefix needed on Windows command line to run commands in system path.
	 * 
	 * @return "cmd /c " if run on Windows; the empty string else
	 */
	static String osPrefix() {
		return System.getProperty("os.name").toLowerCase().contains("windows") ? "cmd /c " : "";
	}

	/**
	 * Checks if file name is absolute. If not, appends it to specified directory.
	 * 
	 * @param filename file name to check
	 * @param dir directory to append file name to, if necessary
	 * @return absolute file name
	 */
	static String getAbsoluteFilename(String filename, File dir) {
		if (!(new File(filename)).isAbsolute() && dir.isDirectory()) {
			return dir.getAbsolutePath() + System.getProperty("file.separator") + filename;
		}
		return filename;
	}

	/**
	 * Tests if the specified file name's directory can be written to, avoiding common exeptions.
	 * 
	 * @param filename name of file to test; must not exist yet, as only its parent (directory) is tested
	 * @return true if filename has a valid parent directory, and this can be written to
	 */
	static boolean canWriteToPathOfFile(String filename) {
		if (filename == null) {
			return false;
		}
		File parent = new File(filename).getAbsoluteFile().getParentFile();
		if (parent == null || !parent.canWrite()) {
			return false;
		}
		return true;
	}

	/**
	 * Writes text to a file.
	 * 
	 * @param filename name of the file to write to; will be overwritten if it exists before.
	 * @param content textual content to be written
	 * @return length of the finished file in bytes
	 * @throws java.io.IOException if there is any problem writing the file
	 */
	static long writeTextFile(String filename, CharSequence content) throws IOException {
		if (!canWriteToPathOfFile(filename)) {
			throw new IOException("Cannot write to path of file: " + filename);
		}
		File file = new File(filename);
		PrintWriter writer = null;
		try {
			writer = new PrintWriter(file);
			writer.print(content);
		} catch (FileNotFoundException ex) {
			throw new IOException("An error occurred while creating, opening, or writing to file: " + filename, ex);
		} finally {
			try {
				writer.close();
			} catch (Exception ex) {
				/* ignore */
			}
		}
		return file.length();
	}

	/**
	 * Writes an histogram image of the specified data to a file.
	 * The height of the image is doubled, and the upper half filled with the specified background color.
	 * The background color shades to white to the bottom.
	 * <strong>Developer's note:</strong> To get the whole thing working on all systems,
	 * vou have to use PNG with image type ARGB, but not really use transparency.
	 * 
	 * @param filename name of the file to be written, should contain supported image suffix, e.g. <code>.png</code>
	 * @param columns percent value for each column
	 * @param height height of the target image in pixel
	 * @param upperBgColor background color for upper half of image
	 * @param lowerBgColor background color for lower half of image (will be blended with upper color)
	 * @throws java.io.IOException if there is any problem writing the file
	 */
	static void writeHistogramImageFile(String filename, double[] columns, int height, Color upperBgColor, Color lowerBgColor) throws IOException {
		boolean root = Double.compare(columns[0], 0.0) == 0 && allEqual(columns);
		columns = padWithZeroes(columns);
		int stretchedHeight = height * 2; // stretched to fill operation box
		BufferedImage image = new BufferedImage(columns.length, stretchedHeight, BufferedImage.TYPE_INT_ARGB); // transparency does not always work as desired
																												// ...
		int col, row, size, color, bgshade;
		double percent;
		for (col = 0; col < columns.length; col++) {
			size = stretchedHeight - (int) (columns[col] * height);
			percent = (double) col / (columns.length - 1); // stretch to [0.0,1.0]
			color = PseudoColor.getColor(-1, scalePercentToRating(percent)).getRGB();
			for (row = 0; row < height; row++) { // upper half
			// image.setRGB( col, row, shade( upperBgColor, lowerBgColor, sigmoid( (double) row / stretchedHeight ) ).getRGB() );
				image.setRGB(col, row, upperBgColor.getRGB());
			}
			for (row = height; row < stretchedHeight; row++) { // lower half
				bgshade = shade(upperBgColor, lowerBgColor, sinusFragment((double) row / stretchedHeight - 0.5)).getRGB(); // alternative: * 2.0 - 1.0 to use
																															// full bgcolor
				image.setRGB(col, row, row < size || root ? bgshade : color);
			}
		}
		String type = filename.substring(filename.lastIndexOf('.') + 1);
		ImageIO.write(image, type, new File(filename));
	}

	/**
	 * Shifts a color to another color, simulating a transparency.
	 * 
	 * @param color source color to shift from
	 * @param shade target color to shift to
	 * @param ratio strength of the shift, in [0,1]
	 * @return source color if ratio==0.0, target color if ratio==1.0, shade in between
	 */
	private static Color shade(Color color, Color shade, double ratio) {
		if (ratio < 0.0 || ratio > 1.0) {
			throw new IllegalArgumentException("Parameter out of range [0.0,1.0]: " + ratio);
		}
		int red = color.getRed();
		int grn = color.getGreen();
		int blu = color.getBlue();
		red += (int) (ratio * (shade.getRed() - red));
		grn += (int) (ratio * (shade.getGreen() - grn));
		blu += (int) (ratio * (shade.getBlue() - blu));
		return new Color(red, grn, blu);
	}

}

/**
 * Struct for sample and weight.
 * Two doubles are sticked together to allow combined ordering by one of them.
 * Used by new experimental median calculation.
 * 
 * @author Nina
 */
class SampleAndWeight implements Comparable<SampleAndWeight> {
	double sample, weight;

	public int compareTo(SampleAndWeight other) {
		return Double.compare(this.sample, other.sample);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SampleAndWeight other = (SampleAndWeight) obj;
		return compareTo(other) == 0;
	}

	@Override
	public int hashCode() {
		// returns the hashCode of the sample part
		return Double.valueOf(this.sample).hashCode();
	}
}

/**
 * This helper class stores some attributes for remote anomaly mean calculation
 * to be used in a Map which would otherwise require a multi-column table.
 * 
 * @author Nina
 */
class DistanceAndWeight {
	int dist, weight;
	double weightRelative;

	public DistanceAndWeight(int dist, int weight, double weightRelative) {
		this.dist = dist;
		this.weight = weight;
		this.weightRelative = weightRelative;
	}
}
