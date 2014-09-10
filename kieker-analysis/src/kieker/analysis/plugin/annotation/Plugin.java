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

package kieker.analysis.plugin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This type annotation can be used to mark plugins and to describe the corresponding output ports.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Plugin {

	/**
	 * The default value to show that a plugin has no name.
	 */
	public static final String NO_NAME = "";

	/**
	 * This flag determines whether the annotated plugin should only be used for programmatic purposes or not. This can for example be used by tools to ignore
	 * specific plugins.
	 */
	boolean programmaticOnly() default false;

	/**
	 * The human-readable description of this plugin.
	 * 
	 * @return The description for this plugin.
	 */
	String description() default "";

	/**
	 * This field can contain an additional text to give an impression of the needed dependencies.
	 * 
	 * @return The dependencies for the current plugin as a human-readable description.
	 */
	String dependencies() default "";

	/**
	 * The name which is used to identify this plugin.
	 * 
	 * @return The name of this plugin.
	 */
	String name() default NO_NAME;

	/**
	 * The output ports which the current plugin has.
	 * 
	 * @return The output ports of this annotation.
	 */
	OutputPort[] outputPorts() default {};

	/**
	 * The output ports which the current plugin has.
	 * 
	 * @return The output ports of this annotation.
	 */
	RepositoryPort[] repositoryPorts() default {};

	/**
	 * The list of possible properties for this plugin.
	 * 
	 * @return A list of properties.
	 */
	Property[] configuration() default {};
}
