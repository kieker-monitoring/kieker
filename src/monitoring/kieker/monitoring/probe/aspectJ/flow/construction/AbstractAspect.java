/***************************************************************************
 * Copyright 2011 by
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

package kieker.monitoring.probe.aspectJ.flow.construction;

import kieker.common.record.flow.ConstructionEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.aspectJ.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Jan Waller
 */
@Aspect
public abstract class AbstractAspect extends AbstractAspectJProbe {
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = AbstractAspect.CTRLINST.getTimeSource();

	@Pointcut
	public abstract void monitoredConstructor();

	@AfterReturning("monitoredConstructor() && this(this_) && notWithinKieker()")
	public void afterConstruction(final Object this_, final JoinPoint.StaticPart jp) {
		final ConstructionEvent crecord = new ConstructionEvent(AbstractAspect.TIME.getTime(), jp.getSignature().getDeclaringTypeName(), this_.toString());
		AbstractAspect.CTRLINST.newMonitoringRecord(crecord);
	}
}
