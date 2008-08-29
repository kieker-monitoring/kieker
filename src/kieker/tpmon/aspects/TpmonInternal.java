package kieker.tpmon.aspects;

/**
 * kieker.tpmon.aspects.TpmonInternal.java
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 * ==================================================
 *
 * This annotation marks methods that shouldn't be monitored, mainly
 * because they are part of tpmon. Even if in the aop.xml a class is
 * within the scope of the aspectjweaver, it won't be monitored during 
 * runtime if annotated with @TpmonInternal(). 
 * 
 * This annotation also deactivates monitoring for methods that would
 * be monitored by the full instrumentation mode. 
 * 
 * @author Matthias Rohr
 *
 * History:
 * 2008/01/14: Refactoring for the first release of
 *             Kieker and publication under an open source licence
 * 2007/12/12: Initial
 */

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface TpmonInternal {
	// String context();
}