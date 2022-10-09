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
package kieker.analysis.behavior.data;

/**
 * Entry call events with request data.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
// TODO rename class
public class PayloadAwareEntryCallEvent extends EntryCallEvent {

	/** property declarations. */
	private final String[] parameters;
	private final String[] values;
	private final int requestType;

	public PayloadAwareEntryCallEvent(final long entryTime, final long exitTime, final String operationSignature, final String classSignature,
			final String sessionId, final String hostname,
			final String[] parameters, final String[] values, final int requestType) {
		super(entryTime, exitTime, operationSignature, classSignature, sessionId, hostname);
		this.parameters = parameters;
		this.values = values;
		this.requestType = requestType;
	}

	public String[] getParameters() {
		return this.parameters;
	}

	public String[] getValues() {
		return this.values;
	}

	public int getRequestType() {
		return this.requestType;
	}

}
