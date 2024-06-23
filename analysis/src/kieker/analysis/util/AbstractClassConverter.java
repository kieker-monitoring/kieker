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
package kieker.analysis.util;

import com.beust.jcommander.IStringConverter;

import kieker.common.util.classpath.InstantiationFactory;

/**
 * Process a string parameter as a class name and return the class.
 *
 * @param <T>
 *            base class type
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public abstract class AbstractClassConverter<T> implements IStringConverter<T> {

	private final Class<T> baseClass;

	public AbstractClassConverter(final Class<T> baseClass) {
		this.baseClass = baseClass;
	}

	@Override
	public T convert(final String className) {
		return InstantiationFactory.getInstance(null).create(this.baseClass, className, null);
	}

}
