/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.monitoring.probe.aspectj;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import kieker.monitoring.probe.IMonitoringProbe;

/**
 * @author Jan Waller
 */
@Aspect
public abstract class AbstractAspectJProbe implements IMonitoringProbe {

	// Pointcuts should not be final!

	@Pointcut("!within(kieker.common..*) && !within(kieker.monitoring..*) && !within(kieker.analysis..*) && !within(kieker.tools..*)")
	public void notWithinKieker() {} // NOPMD (Aspect)

	@Pointcut("execution(void set*(..)) || call(void set*(..))")
	public void setter() {} // NOPMD (Aspect)

	@Pointcut("execution(* get*(..)) || call(* get*(..)) || execution(boolean is*(..)) || call(boolean is*(..))")
	public void getter() {} // NOPMD (Aspect)

	@Pointcut("!getter() && !setter()")
	public void noGetterAndSetter() {} // NOPMD (Aspect)
}
