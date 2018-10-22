/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.util.classpath;

/**
 * @param <T>
 *            the type that is used to cast a type that was found in the class path
 *
 * @author Christian Wulf
 * @since 1.11
 */
public class ClassForNameResolver<T> {

	private final Class<T> classToCast;

	/**
	 * Create new class resolver.
	 *
	 * @param classToCast
	 *            class to cast
	 */
	public ClassForNameResolver(final Class<T> classToCast) {
		this.classToCast = classToCast;
	}

	/**
	 * This method tries to find a class with the given name.
	 *
	 * @param classname
	 *            The name of the class.
	 *
	 * @return A {@link Class} instance corresponding to the given name, if it exists.
	 * @throws ClassNotFoundException
	 *             when the class is not known
	 *
	 */
	public final Class<? extends T> classForName(final String classname) throws ClassNotFoundException {
		final Class<?> clazz = Class.forName(classname);
		return clazz.asSubclass(this.classToCast);
	}
}
