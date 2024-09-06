/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.utilities;

/**
 * Stores the data of an operation start, to later on produce an execution record
 * 
 * @author David Georg Reichelt
 */
public class OperationStartData {
	private final boolean entrypoint;
	private final long traceId;
	private final long tin;
	private final int eoi;
	private final int ess;

	public OperationStartData(final boolean entrypoint, final long traceId, final long tin, final int eoi, final int ess) {
		this.entrypoint = entrypoint;
		this.traceId = traceId;
		this.tin = tin;
		this.eoi = eoi;
		this.ess = ess;
	}

	public boolean isEntrypoint() {
		return this.entrypoint;
	}

	public long getTraceId() {
		return this.traceId;
	}

	public long getTin() {
		return this.tin;
	}

	public int getEoi() {
		return this.eoi;
	}

	public int getEss() {
		return this.ess;
	}
}
