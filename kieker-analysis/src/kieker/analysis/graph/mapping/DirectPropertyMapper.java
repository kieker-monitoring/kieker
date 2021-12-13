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

package kieker.analysis.graph.mapping;

import java.util.function.Function;

import kieker.analysis.graph.IElement;

/**
 * This function maps a graph element by a passed property key to the
 * corresponding property value and returns it as string.
 *
 * @param <T>
 *            Type of elements
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class DirectPropertyMapper<T extends IElement> implements Function<T, String> {

	private final String propertyKey;

	private DirectPropertyMapper(final String propertyKey) {
		this.propertyKey = propertyKey;
	}

	@Override
	public String apply(final T element) {
		return element.getProperty(this.propertyKey);
	}

	public static <T extends IElement> DirectPropertyMapper<T> of(final String propertyKey) { // NOPMD (method name is declarative)
		return new DirectPropertyMapper<>(propertyKey);
	}

}
