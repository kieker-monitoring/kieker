package kieker.tpmon.probe.spring.executions;

import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import kieker.tpmon.annotation.TpmonInternal;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.aspects.springAspectJ.KiekerTpmonMethodInvocationInterceptor
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
 * This annotation marks methods that are exit points for remote calls
 * that go to an other virtual machine. The annotation tries to ensure
 * that the trace id is propergated to an other instance of tpmon in
 * the other virtual machine.
 *
 * It provides the boolean property useRuntimeClassname to select whether
 * to use the declaring or the runtime classname of the instrumented methods.
 * 
 * @author Andre van Hoorn
 */
public class KiekerTpmonMethodInvocationInterceptorRemote extends AbstractKiekerTpmonMethodInvocationInterceptor {

    private static final Log log = LogFactory.getLog(KiekerTpmonMethodInvocationInterceptorRemote.class);

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @TpmonInternal()
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long traceId = cfRegistry.recallThreadLocalTraceId();
        // Only go on if a traceId has been registered before
        if (traceId == -1 || !tpmonController.isMonitoringEnabled()) {
            return invocation.proceed();
        }

        KiekerExecutionRecord execData = this.initExecutionData(invocation);
        execData.eoi = cfRegistry.incrementAndRecallThreadLocalEOI(); /* this is executionOrderIndex-th execution in this trace */
        execData.ess = cfRegistry.recallAndIncrementThreadLocalESS(); /* this is the height in the dynamic call tree of this execution */

        try {
            this.proceedAndMeasure(invocation, execData);
            if (execData.eoi== -1 || execData.ess == -1) {
                log.fatal("eoi and/or ess have invalid values:" +
                        " eoi == " + execData.eoi +
                        " ess == " + execData.ess);
                log.fatal("Terminating Tpmon!");
                tpmonController.terminateMonitoring();
            }
        } catch (Exception e) {
            throw e; // exceptions are forwarded
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            tpmonController.logMonitoringRecord(execData);
            cfRegistry.storeThreadLocalESS(execData.ess);
        }
        return execData.retVal;
    }
}
