package kieker.tpmon.probe.aspectJ.executions;

import javax.servlet.http.HttpServletRequest;

import kieker.tpmon.core.SessionRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * kieker.tpmon.aspects.AbstractKiekerTpmonExecutionProbeServlet
 *
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
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
 * @author Andre van Hoorn
 */
@Aspect
public abstract class AbstractKiekerTpmonExecutionProbeServlet extends AbstractKiekerTpmonExecutionProbe {

    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();

    
    public Object doServletEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        HttpServletRequest req = (HttpServletRequest)thisJoinPoint.getArgs()[0];
        String sessionId = (req!=null) ? req.getSession(true).getId() : null;
        Object retVal = null;
        sessionRegistry.storeThreadLocalSessionId(sessionId);
        try{
            retVal = thisJoinPoint.proceed(); // does pass the args!
        } catch (Exception exc){
            throw exc;
        } finally {
            sessionRegistry.unsetThreadLocalSessionId();
        }
        return retVal;
    }
}
