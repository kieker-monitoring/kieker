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

package kieker.analysisteetime.signature;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * This {@link OperationNameBuilder} creates a operation names in a full Java style. That means, it returns
 * the name in the form of {@code private final MyReturnType doSomething(MyParameter, MyNextParameter)}.
 *
 * @author Sören Henning
 *
 * @since 1.13
 */
public class JavaFullOperationNameBuilder implements OperationNameBuilder {

	public JavaFullOperationNameBuilder() {
		// create builder
	}

	@Override
	public String build(final Collection<String> modifiers, final String returnType, final String name, final Collection<String> parameterTypes) {
		final StringBuilder builder = new StringBuilder();
		if (!modifiers.isEmpty()) {
			builder.append(modifiers.stream().collect(Collectors.joining(" ", "", " ")));
		}
		builder.append(returnType)
				.append(' ')
				.append(name)
				.append(parameterTypes.stream().collect(Collectors.joining(" ,", "(", ")")));
		return builder.toString();
	}

}
