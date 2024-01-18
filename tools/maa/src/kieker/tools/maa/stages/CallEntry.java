/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.maa.stages;

/**
 * Call entry.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CallEntry {

	private final String callerComponent;
	private final String caller;
	private final String calleeComponent;
	private final String callee;
	private final int numOfCalls;

	public CallEntry(final String callerComponent, final String caller, final String calleeComponent,
			final String callee, final int numOfCalls) {
		this.callerComponent = callerComponent;
		this.caller = caller;
		this.calleeComponent = calleeComponent;
		this.callee = callee;
		this.numOfCalls = numOfCalls;
	}

	public String getCallerComponent() {
		return this.callerComponent;
	}

	public String getCaller() {
		return this.caller;
	}

	public String getCalleeComponent() {
		return this.calleeComponent;
	}

	public String getCallee() {
		return this.callee;
	}

	public int getNumOfCalls() {
		return this.numOfCalls;
	}

}
