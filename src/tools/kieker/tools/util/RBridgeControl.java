/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.math.R.Rsession;
import org.rosuda.REngine.REXPDouble;
import org.rosuda.REngine.REXPLogical;
import org.rosuda.REngine.REXPString;
import org.rosuda.REngine.REXPVector;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class RBridgeControl {

	private static final Log LOG = LogFactory.getLog(RBridgeControl.class);
	private static final Log RSERVELOG = LogFactory.getLog("RSERVE");

	Rsession rCon;

	// TODO make a better singleton, later
	public static RBridgeControl INSTANCE = null;

	private RBridgeControl(final boolean silent) {

		OutputStream out = System.out;

		if (true == silent) {
			out = new OutputStream() {

				@Override
				public void write(final int arg0) throws IOException {}
			};
		} else {
			out = new OutputStream() {

				private final int lineEnd = '\n';
				private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

				@Override
				public void write(final int b) throws IOException {
					if (b == this.lineEnd) {
						RSERVELOG.info(this.baos.toString());
						this.baos.reset();
					} else {
						this.baos.write(b);
					}
				}

			};
		}

		this.rCon = Rsession.newLocalInstance(new PrintStream(out), null);
	}

	public static RBridgeControl getInstance(final File root) {
		if (null == RBridgeControl.INSTANCE) {

			// TODO make this configurabe?!?
			RBridgeControl.INSTANCE = new RBridgeControl(false);
			RBridgeControl.INSTANCE.e("OPAD_CONTEXT <<- TRUE");
			// TODO: test if this is needed every time
			// TODO outsource this into a packaged text file, declare the
			// functions at runtime
			// TODO use REngine rather? RServe is not needed any more

			INSTANCE.e("setwd('" + root.getAbsolutePath() + "')");
			// RBridgeControl.INSTANCE
			// .e("sink(file = 'rsink.log', append = TRUE, type = c('output', 'message'),split = FALSE)");
			// INSTANCE.e("source('includes.r', local = FALSE, echo = TRUE)");
			INSTANCE.e("source('plotting2.r', local = FALSE, echo = TRUE)");
			INSTANCE.e("initTS");

			// INSTANCE.e("print( getwd() )");
			// INSTANCE.e("source('basic.r', local = FALSE, echo = TRUE)");
			// INSTANCE.e("dprint('from opad') ");
			// INSTANCE.e("source('plotting.r', local = FALSE, echo = TRUE)");
			// INSTANCE.e("source('opad_functions.r', echo = TRUE, verbose = TRUE)");
			// INSTANCE.e("source('plotting.r', echo = TRUE, verbose = TRUE)");
			// INSTANCE.e("library('logging')");
			// INSTANCE.e("initOPADfunctions()");
		}

		return RBridgeControl.INSTANCE;
	}

	/**
	 * wraps the execution of an arbitrary R expression. Logs result and error
	 * 
	 * @param input
	 * @return
	 */
	public Object e(final String input) {
		Object out = null;
		try {
			out = this.rCon.eval(input);

			Object output = out;

			if (out instanceof REXPString) {
				output = ((REXPString) out).asString();
			}
			if (out instanceof REXPLogical) {
				output = ((REXPLogical) out).toDebugString();
			}

			RBridgeControl.LOG.info("> REXP: " + input + " return: " + output);

		} catch (final Exception exc) {
			RBridgeControl.LOG.error("Error R expr.: " + input + " Cause: "
					+ exc);
			exc.printStackTrace();
		}
		return out;
	}

	public double eDbl(final String input) {
		try {
			// TODO make it error save
			return ((REXPDouble) this.e(input)).asDouble();
		} catch (final Exception exc) {
			RBridgeControl.LOG.error("Error casting value from R: " + input
					+ " Cause: " + exc);
			return -666.666;
		}
	}

	public String eString(final String input) {
		try {
			// TODO make it error save
			final REXPString str = (REXPString) this.e(input);
			return str.toString();
		} catch (final Exception e) {
			return "";
		}
	}

	public double[] eDblArr(final String input) {
		try {
			// TODO make it error save
			final REXPVector res = (REXPVector) this.e(input);
			return res.asDoubles();
		} catch (final Exception e) {
			return new double[0];
		}
	}

	public void assign(final String variable, final double[] values) {
		try {
			final StringBuffer buf = new StringBuffer();
			buf.append(variable + " <<- c(");
			boolean first = true;
			for (final double item : values) {
				if (!first) {
					buf.append(",");
				} else {
					first = false;
				}
				buf.append(item);
			}
			buf.append(")");
			this.e(buf.toString());
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	// TODO DRY violated!
	public void assign(final String variable, final Double[] values) {
		try {
			final StringBuffer buf = new StringBuffer();
			buf.append(variable + " <<- c(");
			boolean first = true;
			for (final Double item : values) {
				if (!first) {
					buf.append(",");
				} else {
					first = false;
				}
				if ((null == item) || item.isNaN()) {
					buf.append("NA");
				} else {
					buf.append(item);
				}
			}
			buf.append(")");
			this.e(buf.toString());
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	public void assign(final String variable, final Long[] values) {
		try {
			final StringBuffer buf = new StringBuffer();
			buf.append(variable + " <<- c(");
			boolean first = true;
			for (final Long item : values) {
				if (!first) {
					buf.append(",");
				} else {
					first = false;
				}
				buf.append(item);
			}
			buf.append(")");
			this.e(buf.toString());
		} catch (final Exception e) {
			e.printStackTrace();
		}

	}

	private final static AtomicInteger nextVarId = new AtomicInteger(1);

	/**
	 * Returns a globally unique variable name.
	 * 
	 * @param prefix
	 *            may be null
	 * @return
	 */
	public static String uniqueVarname() {
		return String.format("var_%s",
				RBridgeControl.nextVarId.getAndIncrement());
	}
}
