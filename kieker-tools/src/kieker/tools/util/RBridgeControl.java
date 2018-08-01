/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.util;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.atomic.AtomicInteger;

import org.math.R.Rsession;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPMismatchException;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REXPVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Tillmann Carlos Bielefeld
 *
 * @since 1.10
 */
public final class RBridgeControl {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBridgeControl.class);
	private static final AtomicInteger NEXTVARID = new AtomicInteger(1);

	private Rsession rCon;

	protected RBridgeControl() {
		final OutputStream out = new OutputStream2StandardLog();
		try {
			this.rCon = Rsession.newLocalInstance(new PrintStream(out, true, "UTF-8"), null);
		} catch (final UnsupportedEncodingException e) {
			LOGGER.error(e.toString(), e);
		}
	}

	/**
	 * Wraps the execution of an arbitrary R expression. Both errors and results are logged.
	 *
	 * @param input
	 *            The R expression to evaluate.
	 *
	 * @return The result or the error of the evaluation of the given R expression. The method tries to convert it into a string, if possible.
	 */
	public Object evalWithR(final String input) throws InvalidREvaluationResultException {
		Object out = null;

		try {
			out = this.rCon.eval(input);

			Object output = null;

			if (out instanceof REXPString) {
				output = ((REXPString) out).asString();
			} else if (out instanceof REXPLogical) {
				output = ((REXPLogical) out).toDebugString();
			} else if (out != null) {
				output = out;
			} else {
				throw new InvalidREvaluationResultException("Got a null result for evaluation input: \"" + input + "\"");
			}

			RBridgeControl.LOGGER.trace("> REXP: {} return: {}", input, output);
		} catch (final REXPMismatchException exc) {
			RBridgeControl.LOGGER.error("Error R expr.: {} Cause: {}", input, exc.getMessage(), exc);
		}

		return out;
	}

	/**
	 *
	 * @param variable
	 *            variable to R
	 * @throws InvalidREvaluationResultException
	 */
	public void toTS(final String variable) throws InvalidREvaluationResultException {
		if (variable != null) {
			final StringBuffer buf = new StringBuffer();
			buf.append(variable)
					.append(" <- ts(")
					.append(variable)
					.append(')');
			this.evalWithR(buf.toString());
		}
	}

	/**
	 *
	 * @param variable
	 *            variable to R
	 * @param frequency
	 *            frequency to R
	 * @throws InvalidREvaluationResultException
	 */
	public void toTS(final String variable, final long frequency) throws InvalidREvaluationResultException {
		if (variable != null) {
			final StringBuffer buf = new StringBuffer(21);
			buf.append(variable)
					.append(" <- ts(")
					.append(variable)
					.append(", frequency=")
					.append(frequency)
					.append(')');
			this.evalWithR(buf.toString());
		}
	}

	/**
	 *
	 * @param input
	 *            string
	 * @return {@link Double#NaN} in case of error, else dbvalue
	 */
	public double eDbl(final String input) {
		final double resultOnFailure = Double.NaN;

		try {
			final Object evaluationResult = this.evalWithR(input);
			final REXPDouble doubleResult = (REXPDouble) evaluationResult;
			return doubleResult.asDouble();

		} catch (final REXPMismatchException exc) {
			RBridgeControl.LOGGER.error("Error casting value from R: {} Cause: {}", input, exc);
			return resultOnFailure;
		} catch (final InvalidREvaluationResultException exc) {
			RBridgeControl.LOGGER.error(exc.getMessage(), exc);
			return resultOnFailure;
		}
	}

	/**
	 *
	 * @param input
	 *            inputstring
	 * @return Rdata
	 */
	public String eString(final String input) {
		final String resultOnFailure = "";

		try {
			final Object evaluationResult = this.evalWithR(input);
			final REXPString stringResult = (REXPString) evaluationResult;

			return stringResult.toString();
		} catch (final InvalidREvaluationResultException exc) {
			RBridgeControl.LOGGER.error(exc.getMessage(), exc);
			return resultOnFailure;
		}
	}

	/**
	 *
	 * @param input
	 *            inputstring
	 * @return Rdata
	 */
	public double[] eDblArr(final String input) {
		final double[] resultOnFailure = new double[0];

		try {
			final Object evaluationResult = this.evalWithR(input);
			final REXPVector vectorResult = (REXPVector) evaluationResult;
			return vectorResult.asDoubles();
		} catch (final REXPMismatchException e) {
			return resultOnFailure;
		} catch (final InvalidREvaluationResultException e) {
			RBridgeControl.LOGGER.error(e.getMessage(), e);
			return resultOnFailure;
		}
	}

	/**
	 *
	 * @param variable
	 *            string
	 *
	 * @param values
	 *            assign value
	 * @throws InvalidREvaluationResultException
	 */
	public void assign(final String variable, final double[] values) throws InvalidREvaluationResultException {
		final StringBuffer buf = new StringBuffer();
		buf.append(variable)
				.append(" <- c(");
		boolean first = true;
		for (final double item : values) {
			if (!first) {
				buf.append(',');
			} else {
				first = false;
			}
			buf.append(item);
		}
		buf.append(')');
		this.evalWithR(buf.toString());
	}

	/**
	 *
	 * @param variable
	 *            string
	 * @param values
	 *            assign vaules
	 * @throws InvalidREvaluationResultException
	 */
	public void assign(final String variable, final Double[] values) throws InvalidREvaluationResultException {
		final StringBuffer buf = new StringBuffer();
		buf.append(variable)
				.append(" <- c(");
		boolean first = true;
		for (final Double item : values) {
			if (!first) {
				buf.append(',');
			} else {
				first = false;
			}
			if ((null == item) || item.isNaN()) {
				buf.append("NA");
			} else {
				buf.append(item);
			}
		}
		buf.append(')');
		this.evalWithR(buf.toString());
	}

	/**
	 *
	 * @param variable
	 *            string
	 * @param values
	 *            assign vaules
	 * @throws InvalidREvaluationResultException
	 */
	public void assign(final String variable, final Long[] values) throws InvalidREvaluationResultException {
		final StringBuffer buf = new StringBuffer();
		buf.append(variable)
				.append(" <- c(");
		boolean first = true;
		for (final Long item : values) {
			if (!first) {
				buf.append(',');
			} else {
				first = false;
			}
			buf.append(item);
		}
		buf.append(',');
		this.evalWithR(buf.toString());
	}

	/**
	 * Returns a globally unique variable name, even during the access of multiple threads.
	 *
	 * @return A unique variable name of the form {@code var_1, var_2, ...}.
	 */
	public static String uniqueVarname() {
		return String.format("var_%s", RBridgeControl.NEXTVARID.getAndIncrement());
	}

	/**
	 * Delivers the singleton instance of this class.
	 *
	 * @return The singleton instance.
	 */
	public static final RBridgeControl getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * This is a helper class, holding the singleton instance of {@link RBridgeControl} and making sure that the object is lazily created.
	 *
	 * @author Tillmann Carlos Bielefeld
	 *
	 * @since 1.10
	 */
	private static final class LazyHolder { // NOCS

		static final RBridgeControl INSTANCE; // NOPMD (private modifier would require an additional getter method)

		static {
			INSTANCE = new RBridgeControl();
		}

	}
}
