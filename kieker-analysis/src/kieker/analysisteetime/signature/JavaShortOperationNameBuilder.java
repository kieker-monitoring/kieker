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

/**
 * This {@link OperationNameBuilder} creates a operation names in a short Java style. That means, it returns
 * the actual name followed by {@code ()} or {@code (..)} depending on whether this operation has parameters
 * or not.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class JavaShortOperationNameBuilder implements OperationNameBuilder {

	public JavaShortOperationNameBuilder() {
		// create builder
	}

	@Override
	public String build(final Collection<String> modifiers, final String returnType, final String name, final Collection<String> parameterTypes) {
		return name + '(' + (!parameterTypes.isEmpty() ? ".." : "") + ')'; // NOCS (declarative)
	}

}
