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
package kieker.analysis.architecture.recovery.events;

/**
 * Represent one operation event.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationEvent extends AbstractElementEvent {

	private final String operationSignature;

	public OperationEvent(final String hostname, final String componentSignature, final String operationSignature) {
		super(hostname, componentSignature);
		this.operationSignature = operationSignature;
	}

	public String getOperationSignature() {
		return this.operationSignature;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof OperationEvent) {
			final OperationEvent operation = (OperationEvent) obj;
			return this.compare(this.getComponentSignature(), operation.getComponentSignature())
					&& this.compare(this.operationSignature, operation.getOperationSignature())
					&& this.compare(this.getHostname(), operation.getHostname());
		} else {
			return super.equals(obj);
		}
	}

	private boolean compare(final String a, final String b) {
		if (a == null && b == null) {
			return true;
		} else if (a != null) {
			return a.equals(b);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public String toString() {
		return String.format("%s>%s:%s", this.getHostname(), this.getComponentSignature(), this.operationSignature);
	}
}
