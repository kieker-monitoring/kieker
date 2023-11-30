/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mop.stages;

import org.csveed.bean.conversion.AbstractConverter;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class ComponentTypeConverter extends AbstractConverter<ComponentType> {

	public ComponentTypeConverter() {
		super(ComponentType.class);
	}

	public ComponentTypeConverter(final Class<ComponentType> clazz) {
		super(clazz);
	}

	@Override
	public ComponentType fromString(final String text) throws Exception {
		final ComponentType componentType = TypeFactory.eINSTANCE.createComponentType();
		componentType.setSignature(text);
		return componentType;
	}

	@Override
	public String toString(final ComponentType value) throws Exception {
		return value.getSignature();
	}

}
