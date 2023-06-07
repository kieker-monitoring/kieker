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

package kieker.analysis.architecture.recovery.signature;

import kieker.model.analysismodel.type.OperationType;

/**
 * A {@link IOperationSignatureExtractor} sets the list of modifiers, the return type, the name and
 * the parameters of a {@link OperationType} based on its signature.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public interface IOperationSignatureExtractor {

	/**
	 * Extract the properties of a {@link OperationType} from its signature and set the attributes accordingly.
	 *
	 * @param operationType
	 *            operation to be processed
	 * @since 1.14
	 */
	void extract(final OperationType operationType);

}
