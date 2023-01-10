/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.settings;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.converters.BooleanConverter;
import com.beust.jcommander.converters.FloatConverter;
import com.beust.jcommander.converters.IntegerConverter;
import com.beust.jcommander.converters.NoConverter;

import kieker.common.configuration.Configuration;

/**
 * Parse configuration settings and fill them into a settings object.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ConfigurationParser {

	private final Object settings;
	private final String prefix;

	public ConfigurationParser(final String prefix, final Object settings) {
		this.settings = settings;
		this.prefix = prefix + ".";
	}

	public void parse(final Configuration configuration) throws ParameterException {
		for (final Field field : this.settings.getClass().getDeclaredFields()) {
			final Setting annotation = field.getAnnotation(Setting.class);
			if (annotation != null) {
				final String value = configuration.getStringProperty(this.prefix + field.getName());
				if (!"".equals(value)) {
					if (annotation.variableArity()) {
						this.processList(field, annotation, value);
					} else {
						final Object resultValue = this.processValue(field.getType(), field.getName(), annotation, value);
						this.validateValue(field.getName(), annotation.validators(), resultValue);
						this.setValue(field, resultValue);
					}
				} else {
					if (annotation.required()) {
						throw new ParameterException(String.format("Parameter %s missing.", field.getName()));
					}
				}
			}
		}
	}

	private void validateValue(final String name, final Class<? extends IValueValidator>[] validators, final Object resultValue) throws ParameterException {
		for (final Class<? extends IValueValidator> validatorClass : validators) {
			try {
				final IValueValidator<Object> validator = validatorClass.newInstance();
				validator.validate(name, resultValue);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ParameterException(String.format("Validator %s cannot be found.", validatorClass.getCanonicalName()), e);
			}
		}
	}

	private void processList(final Field field, final Setting annotation, final String value) {
		final String[] values = value.split(File.pathSeparator);
		final List<Object> result = new ArrayList<>();
		for (final String part : values) {
			final Object object = this.processValue(field.getType(), field.getName(), annotation, part);
			this.validateValue(field.getName(), annotation.validators(), result);
			result.add(object);
		}
		this.setValue(field, result);
	}

	private Object processValue(final Class<?> clazz, final String name, final Setting annotation, final String value) throws ParameterException { // NOPMD
		if (annotation.converter().equals(NoConverter.class)) {
			if (clazz.isAssignableFrom(String.class)) {
				return value;
			} else if (clazz.isAssignableFrom(Boolean.class)) {
				return new BooleanConverter(name).convert(value);
			} else if (clazz.isAssignableFrom(Short.class) || clazz.isAssignableFrom(short.class)) {
				return new ShortConverter(name).convert(value);
			} else if (clazz.isAssignableFrom(Integer.class) || clazz.isAssignableFrom(int.class)) {
				return new IntegerConverter(name).convert(value);
			} else if (clazz.isAssignableFrom(Long.class) || clazz.isAssignableFrom(long.class)) {
				return new LongConverter(name).convert(value);
			} else if (clazz.isAssignableFrom(Character.class) || clazz.isAssignableFrom(char.class)) {
				return new CharConverter(name).convert(value);
			} else if (clazz.isAssignableFrom(Float.class) || clazz.isAssignableFrom(float.class)) {
				return new FloatConverter(name).convert(value);
			} else if (clazz.isAssignableFrom(Double.class) || clazz.isAssignableFrom(double.class)) {
				return new DoubleConverter(name).convert(value);
			} else {
				return null;
			}
		} else {
			try {
				final IStringConverter<?> converter = annotation.converter().newInstance();
				return converter.convert(value);
			} catch (InstantiationException | IllegalAccessException e) {
				throw new ParameterException(String.format("Converter for setting %s cannot by instantiated. Value is %s", name, value), e);
			}
		}
	}

	private void setValue(final Field field, final Object value) throws ParameterException {
		try {
			field.setAccessible(true);
			field.set(this.settings, value);
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new ParameterException(String.format("Value cannot be set to setting %s", field.getName()), e);
		}
	}
}
