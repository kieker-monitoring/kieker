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

import kieker.model.analysismodel.type.ComponentType;

/**
 * A {@link IComponentNameBuilder} can be used to create the component name from a package name and a component name.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public interface IComponentNameBuilder {

	/**
	 * @param packageName
	 *            name of the package
	 * @param name
	 *            name of the component
	 *
	 * @return compoent name
	 * @since 1.14
	 */
	String build(String packageName, String name);

	/**
	 * @param componentType
	 *            component type object
	 * @return component type string
	 * @since 1.14
	 */
	default String build(final ComponentType componentType) {
		return this.build(componentType.getPackage(), componentType.getName());
	}

}
