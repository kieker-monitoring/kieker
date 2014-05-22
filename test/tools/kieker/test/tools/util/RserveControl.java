/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.tools.util;

import java.io.IOException;

import org.math.R.StartRserve;
import org.rosuda.REngine.Rserve.RConnection;
import org.rosuda.REngine.Rserve.RserveException;

/**
 * Controls a Rserve instance for testing purposes.
 * Precondition: R with Rserve installed.
 * 
 * @author Thomas Düllmann
 */

public class RserveControl {

	private static RConnection stRConnection;
	/**
	 * Full path to the R executable
	 * (e.g. "/opt/R/bin/R")
	 * If R is in the classpath, a single "R" is enough (default).
	 */
	private final String rServeExecutablePath;
	private final int rServePort = 6311;
	private final String rArgs = "--no-save --slave";
	private final String rServeArgs = "--no-save --slave --RS-port "
			+ String.valueOf(this.rServePort);
	private boolean running = false;
	private RConnection rConnection;

	/**
	 * Default constructor using the R executable set in the classpath.
	 */
	public RserveControl() {
		this.rServeExecutablePath = "R";
	}

	/**
	 * Constructor using the given R executable.
	 * 
	 * @param rExecPath
	 *            Path of the R executable (e.g. "/opt/R/bin/R")
	 */
	public RserveControl(final String rExecPath) {
		this.rServeExecutablePath = rExecPath;
	}

	/**
	 * Returns the state of the currently controlled Rserve instance.
	 * 
	 * @return true if Rserve is running, else false
	 */
	public boolean isRserveRunning() {
		return this.running;
	}

	public static RConnection getRConnection() {
		return stRConnection;
	}

	/**
	 * Starts a Rserve instance.
	 * 
	 * @return true if start was successful, else false
	 */
	public boolean start() {
		final boolean launched = StartRserve.launchRserve(this.rServeExecutablePath, this.rArgs, this.rServeArgs, false);

		try {
			this.rConnection = new RConnection("localhost", this.rServePort);
			stRConnection = this.rConnection;
		} catch (final RserveException e) {
			e.printStackTrace();
			return false;
		}
		this.running = launched; // && (this.rConnection != null);
		return this.running;
	}

	/**
	 * Terminates a previously started Rserve instance.
	 */
	public void terminate() {
		if (this.running) {
			try {
				this.rConnection.shutdown();
			} catch (final RserveException e) {
				e.printStackTrace();
			} finally {
				this.rConnection = null;
				stRConnection = null;
				this.running = false;
			}
		}

		// Just in case anything went wrong when we tried to shutdown Rserve gracefully
		// .. we kill the Rserve process
		try {
			final Process p = Runtime.getRuntime().exec("pkill Rserve");
			p.waitFor();
		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}
	}
}
