/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

/**
 * This annotation can be used to describe a single property for a plugin or a repository.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.6
 */
public @interface Property {

	/**
	 * The name of the property.
	 * 
	 * @return The name of the property.
	 */
	String name();

	/**
	 * The default value for the property.
	 * 
	 * @return The default value for the property.
	 */
	String defaultValue();

	/**
	 * This field can be used for a (short) description of the property's purpose.
	 * 
	 * @return The description of this property.
	 */
	String description() default "";
	
	/**
	 * The definition whether a property can be updated while the plugin is used
	 * 
	 * @return True if updateable on-the-fly, else false
	 */
	boolean updateable() default false;
}
