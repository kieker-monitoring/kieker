/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.framework.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A helper class to clone bean like objects with default constructors, getters, and setters.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class Cloner {

	private Cloner() {}

	/**
	 * Clones the given object by creating a new instance of the class. The method invokes all getter methods on the original and stores the resulting values in the
	 * clone by invoking the corresponding setter methods. Properties without setter or getter methods are ignored. The resulting clone is always a new instance,
	 * hence original != clone holds. It is assumed that the given instance provides a default constructor without any parameters.
	 * 
	 * @param original
	 *            The original instance to clone.
	 * 
	 * @return A clone of the given instance.
	 * 
	 * @throws Exception
	 *             If the given instance does not provide a default constructor, the getters or setters are not accessible, the getters and setters are not
	 *             compatible, or any of the methods threw an exception.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T cloneObject(final T original) throws Exception {
		// Comment for 1.7: The three exceptions, thrown by this called method, have the same parent exception since 1.7
		final T clone = (T) original.getClass().newInstance();

		Cloner.cloneAllProperties(original, clone);

		return clone;
	}

	private static <T> void cloneAllProperties(final T original, final T clone) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		final Collection<GetterSetterPair> getterSetterPairs = Cloner.getGetterSetterPairs(original.getClass());

		for (final GetterSetterPair getterSetterPair : getterSetterPairs) {
			final Object value = getterSetterPair.getGetter().invoke(original);
			getterSetterPair.getSetter().invoke(clone, value);
		}
	}

	private static Collection<GetterSetterPair> getGetterSetterPairs(final Class<?> clazz) {
		final Collection<GetterSetterPair> properties = new ArrayList<GetterSetterPair>();

		final Map<String, Method> foundGetters = new HashMap<String, Method>();
		final Map<String, Method> foundSetters = new HashMap<String, Method>();

		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			if (Cloner.isGetter(method)) {
				final String propertyName = method.getName().substring(3);
				if (foundSetters.containsKey(propertyName)) {
					properties.add(new GetterSetterPair(method, foundSetters.remove(propertyName)));
				} else {
					foundGetters.put(propertyName, method);
				}
			} else if (Cloner.isSetter(method)) {
				final String propertyName = method.getName().substring(3);
				if (foundGetters.containsKey(propertyName)) {
					properties.add(new GetterSetterPair(foundGetters.remove(propertyName), method));
				} else {
					foundSetters.put(propertyName, method);
				}
			}
		}

		return properties;
	}

	private static boolean isSetter(final Method method) {
		return ((method.getReturnType() == Void.TYPE) && (method.getParameterTypes().length == 1) && (Modifier.isPublic(method.getModifiers())) && method.getName()
				.matches("set[A-Z].*"));
	}

	private static boolean isGetter(final Method method) {
		return ((method.getReturnType() != Void.TYPE) && (method.getParameterTypes().length == 0) && (Modifier.isPublic(method.getModifiers())) && method.getName()
				.matches("get[A-Z].*"));
	}

	private static class GetterSetterPair {

		private final Method getter;
		private final Method setter;

		public GetterSetterPair(final Method getter, final Method setter) {
			this.getter = getter;
			this.setter = setter;
		}

		public Method getGetter() {
			return this.getter;
		}

		public Method getSetter() {
			return this.setter;
		}

	}

}
