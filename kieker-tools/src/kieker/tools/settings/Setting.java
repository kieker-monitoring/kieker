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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import com.beust.jcommander.IStringConverter;
import com.beust.jcommander.IValueValidator;
import com.beust.jcommander.converters.NoConverter;
import com.beust.jcommander.validators.NoValueValidator;

/**
 * Mark settings which should be set by the corresponding named value from a configuration file.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD, ElementType.METHOD })
public @interface Setting {

	Class<? extends IStringConverter<?>> converter() default NoConverter.class;

	/**
	 * @return true if this parameter has a variable arity. See @{IVariableArity}
	 */
	boolean variableArity() default false;

	/**
	 * Whether this option is required.
	 */
	boolean required() default false;

	/**
	 * Validate the value for this parameter.
	 */
	Class<? extends IValueValidator>[] validators() default NoValueValidator.class;
}
