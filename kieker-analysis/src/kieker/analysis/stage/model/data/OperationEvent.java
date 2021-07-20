/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.model.data;

/**
 * Represent one operation event.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationEvent {

	private final String componentSignature;
	private final String operationSignature;
	private final String hostname;

	public OperationEvent(final String hostname, final String componentSignature, final String operationSignature) {
		this.hostname = hostname;
		this.componentSignature = componentSignature;
		this.operationSignature = operationSignature;
	}

	public String getHostname() {
		return this.hostname;
	}

	public String getComponentSignature() {
		return this.componentSignature;
	}

	public String getOperationSignature() {
		return this.operationSignature;
	}

	@Override
	public String toString() {
		return String.format("%s:%s@%s", this.componentSignature, this.operationSignature, this.hostname);
	}
}
