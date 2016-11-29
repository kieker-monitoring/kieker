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

package kieker.analysisteetime.modeltests.hardcoded;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class ComponentType {

	private final Map<String, OperationType> operationTypes = new HashMap<>();
	private String signature;

	public boolean containsOperationType(final String operationType) {
		return this.operationTypes.containsKey(operationType);
	}

	public void addOperationType(final OperationType operationType) {
		this.operationTypes.put("", operationType); // TODO how to compute key?!
		// TODO add opposite
	}

	public Collection<OperationType> getOperationTypes() {
		return this.operationTypes.values();
	}

	public String getSignature() {
		return this.signature;
	}

	public void setSignature(final String signature) {
		this.signature = signature;
	}

}
