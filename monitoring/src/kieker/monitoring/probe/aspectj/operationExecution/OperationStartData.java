/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.operationExecution;

final class OperationStartData {
	private final boolean entrypoint;
	private final String sessionId;
	private final long traceId;
	private final long tin;
	private final String hostname;
	private final int eoi;
	private final int ess;

	public OperationStartData(final boolean entrypoint, final String sessionId, final long traceId, final long tin, final String hostname,
			final int eoi, final int ess) {
		this.entrypoint = entrypoint;
		this.sessionId = sessionId;
		this.traceId = traceId;
		this.tin = tin;
		this.hostname = hostname;
		this.eoi = eoi;
		this.ess = ess;
	}

	public boolean isEntrypoint() {
		return this.entrypoint;
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
