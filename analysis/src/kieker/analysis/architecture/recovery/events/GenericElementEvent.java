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
package kieker.analysis.architecture.recovery.events;

/**
 * Abstract event type for operation and storage events.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class GenericElementEvent {

	private final String hostname;
	private final String componentSignature;

	public GenericElementEvent(final String hostname, final String componentSignature) {
		this.hostname = hostname;
		this.componentSignature = componentSignature;
	}

	public String getHostname() {
		return this.hostname;
	}

	public String getComponentSignature() {
		return this.componentSignature;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof OperationEvent) {
			final OperationEvent operation = (OperationEvent) obj;
			return this.compare(this.componentSignature, operation.getComponentSignature())
					&& this.compare(this.hostname, operation.getHostname());
		} else {
			return super.equals(obj);
		}
	}

	private boolean compare(final String a, final String b) {
		if ((a == null) && (b == null)) {
			return true;
		} else if (a != null) {
			return a.equals(b);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode() ^ this.hostname.hashCode() ^ this.componentSignature.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s>%s", this.hostname, this.componentSignature);
	}
}
