/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

/**
 * This {@link IComponentNameBuilder} creates a component names in a full Java style. That means, it returns
 * the package name followed by a dot and the actual name. E.g., {@code my.package.name.MyClass}.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class JavaFullComponentNameBuilder implements IComponentNameBuilder {

	public JavaFullComponentNameBuilder() {
		// create builder
	}

	@Override
	public String build(final String packageName, final String name) {
		return packageName + '.' + name;
	}

}
