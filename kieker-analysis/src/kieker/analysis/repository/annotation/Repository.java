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

package kieker.analysis.repository.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import kieker.analysis.plugin.annotation.Property;

/**
 * This type annotation can be used to mark repositories.
 *
 * @author Nils Christian Ehmke, Andre van Hoorn
 *
 * @since 1.5
 * @deprecated since 1.15.1 old plugin api
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Inherited
public @interface Repository {

	/** This constant can be used as a value to show that the component has no name. */
	public static final String NO_NAME = "";

	/**
	 * This flag determines whether the annotated repository should only be used for programmatic purposes or not. This can for example be used by tools to ignore
	 * specific repositories.
	 *
	 * @return true if a repository can be used only for programmatic reasons (what ever that is)
	 */
	boolean programmaticOnly() default false;

	/**
	 * The human-readable description of this repository type.
	 *
	 * @return The description for this repository type.
	 */
	String description() default "";

	/**
	 * This field can contain an additional text to give an impression of the needed dependencies.
	 *
	 * @return The dependencies for the current repository as a human-readable description.
	 */
	String dependencies() default "";

	/**
	 * The name which is used to identify this repository type.
	 *
	 * @return The name of this repository type.
	 */
	String name() default NO_NAME;

	/**
	 * The list of possible properties for this repository.
	 *
	 * @return A list of properties.
	 */
	Property[] configuration() default {};
}
