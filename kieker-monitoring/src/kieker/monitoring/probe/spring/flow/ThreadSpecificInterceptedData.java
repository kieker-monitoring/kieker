/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.spring.flow;

/**
 * Data storage object that is used to store thread-specific data during an interception in the {@link RestInInterceptor}.
 *
 * @author Thomas F. Duellmann
 *
 * @since 1.13
 */
public class ThreadSpecificInterceptedData {

	private final String signature;
	private final String sessionId;
	private final long traceId;
	private final long tin;
	private final String hostname;
	private final int eoi;
	private final int ess;

	/**
	 * Constructor initializing all the variables.
	 *
	 * @param signature
	 *            signature
	 * @param sessionId
	 *            session Id
	 * @param traceId
	 *            trace id
	 * @param tin
	 *            entry point time
	 * @param hostname
	 *            hostname
	 * @param eoi
	 *            eoi
	 * @param ess
	 *            ess
	 */
	public ThreadSpecificInterceptedData(final String signature, final String sessionId, final long traceId, final long tin, final String hostname, final int eoi,
			final int ess) {
		this.signature = signature;
		this.sessionId = sessionId;
		this.traceId = traceId;
		this.tin = tin;
		this.hostname = hostname;
		this.eoi = eoi;
		this.ess = ess;
	}

	public String getSignature() {
		return this.signature;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public long getTraceId() {
		return this.traceId;
	}

	public long getTin() {
		return this.tin;
	}

	public String getHostname() {
		return this.hostname;
	}

	public int getEoi() {
		return this.eoi;
	}

	public int getEss() {
		return this.ess;
	}
}
