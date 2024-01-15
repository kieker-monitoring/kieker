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
package kieker.analysis.behavior.events;

/**
 * Entry call events with request data.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class EntryCallEvent {

	/** property declarations. */
	private final long entryTime;
	private final long exitTime;
	private String operationSignature;
	private String classSignature;
	private final String sessionId;
	private final String hostname;

	private final String[] parameters;
	private final String[] values;
	private final int requestType;

	public EntryCallEvent(final long entryTime, final long exitTime, final String operationSignature, final String classSignature,
			final String sessionId, final String hostname,
			final String[] parameters, final String[] values, final int requestType) { // NOPMD array stored directly
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.operationSignature = operationSignature;
		this.classSignature = classSignature;
		this.sessionId = sessionId;
		this.hostname = hostname;
		this.parameters = parameters;
		this.values = values;
		this.requestType = requestType;
	}

	public String getOperationSignature() {
		return this.operationSignature;
	}

	public void setOperationSignature(final String operationSignature) {
		this.operationSignature = operationSignature;
	}

	public String getClassSignature() {
		return this.classSignature;
	}

	public void setClassSignature(final String classSignature) {
		this.classSignature = classSignature;
	}

	public long getEntryTime() {
		return this.entryTime;
	}

	public long getExitTime() {
		return this.exitTime;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getHostname() {
		return this.hostname;
	}

	public String[] getParameters() {
		return this.parameters; // NOPMD exposing array
	}

	public String[] getValues() {
		return this.values; // NOPMD exposing array
	}

	public int getRequestType() {
		return this.requestType;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this.getClass().equals(obj.getClass())) {
			final EntryCallEvent otherCall = (EntryCallEvent) obj;
			return (this.compareString(this.classSignature, otherCall.classSignature)
					&& this.compareString(this.operationSignature, otherCall.operationSignature)
					&& this.compareString(this.hostname, otherCall.hostname)
					&& this.compareString(this.sessionId, otherCall.sessionId)
					&& (this.entryTime == otherCall.entryTime)
					&& (this.exitTime == otherCall.exitTime)
					&& this.compareArray(this.parameters, otherCall.parameters)
					&& this.compareArray(this.values, otherCall.values));
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return (int) (super.hashCode() + this.classSignature.hashCode() + this.entryTime + this.exitTime + this.hostname.hashCode() + this.sessionId.hashCode());
	}

	private boolean compareArray(final String[] left, final String[] right) {
		if ((left == null) && (right == null)) {
			return true;
		} else if ((left != null) && (right != null)) {
			if (left.length == right.length) {
				for (int i = 0; i < left.length; i++) {
					if (!this.compareString(left[i], right[i])) {
						return false;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean compareString(final String left, final String right) {
		if ((left == null) && (right == null)) {
			return true;
		} else if (left != null) {
			return left.equals(right);
		} else {
			return false;
		}
	}

	@Override
	public String toString() {
		return String.format("%s::%s %d:%d -- %d @%s[%s] %s",
				this.classSignature,
				this.operationSignature,
				this.entryTime, this.exitTime,
				this.requestType,
				this.hostname, this.sessionId,
				this.parameterValues());
	}

	private String parameterValues() {
		if ((this.parameters == null) && (this.values == null)) {
			return "no-parameters";
		}
		String result = null;
		for (int i = 0; i < this.parameters.length; i++) {
			if (result == null) {
				result = this.parameters[i] + "=" + this.values[i];
			} else {
				result += ", " + this.parameters[i] + "=" + this.values[i];
			}
		}
		if (result == null) {
			return "{}";
		} else {
			return "{ " + result + " }";
		}
	}

}
