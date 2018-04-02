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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @param <T>
 *            the type that is used to cast a type that was found in the class path
 *
 * @author Christian Wulf
 * @since 1.11
 */
public class CachedClassForNameResolver<T> {

	private final ConcurrentMap<String, Class<? extends T>> cachedClasses = new ConcurrentHashMap<String, Class<? extends T>>(); // NOCS
	private final ClassForNameResolver<T> classForNameResolver;

	/**
	 * Create a cached class name resolver.
	 *
	 * @param classForNameResolver
	 *            plain class name resolver
	 */
	public CachedClassForNameResolver(final ClassForNameResolver<T> classForNameResolver) {
		this.classForNameResolver = classForNameResolver;
	}

	/**
	 * This method tries to find a class with the given name.
	 *
	 * @param classname
	 *            The name of the class.
	 *
	 * @return A {@link Class} instance corresponding to the given name, if it exists.
	 *
	 * @throws ClassNotFoundException
	 *             when the specified {@code classname} does not refer to a known class
	 */
	public final Class<? extends T> classForName(final String classname) throws ClassNotFoundException {
		Class<? extends T> clazz = this.cachedClasses.get(classname);
		if (clazz == null) {
			clazz = this.classForNameResolver.classForName(classname);
			final Class<? extends T> previousClass = this.cachedClasses.putIfAbsent(classname, clazz);
			if (null != previousClass) {
				clazz = previousClass;
			}
		}
		return clazz;
	}
}
