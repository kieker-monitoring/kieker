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
package kieker.panalysis.framework.core;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class Cloner {

	private Cloner() {}

	public static IStage cloneStage(final IStage stage) throws ReflectiveOperationException {
		final IStage clone = stage.getClass().newInstance();

		Cloner.cloneAllProperties(stage, clone);

		return clone;
	}

	private static void cloneAllProperties(final IStage original, final IStage clone) throws ReflectiveOperationException {
		final Collection<Property> properties = Cloner.getProperties(original.getClass());

		for (final Property property : properties) {
			final Object value = property.getGetter().invoke(original);
			property.getSetter().invoke(clone, value);
		}
	}

	private static Collection<Property> getProperties(final Class<?> clazz) {
		final Collection<Property> properties = new ArrayList<>();

		final Map<String, Method> foundGetters = new HashMap<>();
		final Map<String, Method> foundSetters = new HashMap<>();

		final Method[] methods = clazz.getMethods();
		for (final Method method : methods) {
			if (Cloner.isGetter(method)) {
				final String propertyName = method.getName().substring(3);
				if (foundSetters.containsKey(propertyName)) {
					properties.add(new Property(method, foundSetters.remove(propertyName)));
				} else {
					foundGetters.put(propertyName, method);
				}
			} else if (Cloner.isSetter(method)) {
				final String propertyName = method.getName().substring(3);
				if (foundGetters.containsKey(propertyName)) {
					properties.add(new Property(foundGetters.remove(propertyName), method));
				} else {
					foundSetters.put(propertyName, method);
				}
			}
		}

		return properties;
	}

	private static boolean isSetter(final Method method) {
		return method.getName().matches("set[A-Z].*") && (method.getReturnType() == Void.TYPE) && (method.getParameterTypes().length == 1);
	}

	private static boolean isGetter(final Method method) {
		return method.getName().matches("get[A-Z].*") && (method.getReturnType() != Void.TYPE) && (method.getParameterTypes().length == 0);
	}

	private static class Property {

		private final Method getter;
		private final Method setter;

		public Property(final Method getter, final Method setter) {

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
