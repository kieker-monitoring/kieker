/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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
 * This annotation can be used to mark methods as input ports. The corresponding methods should only get an instance of the class <code>Object</code> and nothing
 * else.<br>
 * Furthermore the port allows to specify the event types for this port and the name. There is also a
 * field for a human-readable description available.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.5
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Inherited
public @interface InputPort {

	/**
	 * The human-readable description of this port.
	 * 
	 * @return The description for this port.
	 */
	String description() default "Input Port";

	/**
	 * The name which is used to identify this port. It should be unique within the class.
	 * 
	 * @return The name of this port.
	 */
	String name();

	/**
	 * The event types which are used for this port. If this is empty, everything can be received through the port.
	 * 
	 * @return The event types for this class.
	 */
	Class<?>[] eventTypes() default {};

}
