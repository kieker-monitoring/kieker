/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.signature;

import java.util.Collection;

import kieker.analysisteetime.model.analysismodel.type.OperationType;

/**
 * A {@link OperationNameBuilder} can be used to create the operation name from a list of
 * modifiers, a return type, the actual name and a list of parameter types.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public interface OperationNameBuilder {

	/**
	 * @since 1.14
	 */
	public String build(final Collection<String> modifiers, final String returnType, final String name, final Collection<String> parameterTypes);

	/**
	 * @since 1.14
	 */
	public default String build(final OperationType operationType) {
		return this.build(operationType.getModifiers(), operationType.getReturnType(), operationType.getName(), operationType.getParameterTypes());
	}

}
