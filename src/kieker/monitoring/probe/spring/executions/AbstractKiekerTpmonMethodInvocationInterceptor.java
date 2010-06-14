package kieker.monitoring.probe.spring.executions;

import kieker.common.record.OperationExecutionRecord;
import kieker.monitoring.core.MonitoringController;

import kieker.monitoring.core.ControlFlowRegistry;
import kieker.monitoring.core.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
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
 * @author Marco Luebcke
 */
public abstract class AbstractKiekerTpmonMethodInvocationInterceptor implements MethodInterceptor, IMonitoringProbe {

    private static final Log log = LogFactory.getLog(AbstractKiekerTpmonMethodInvocationInterceptor.class);

    protected static final MonitoringController tpmonController = MonitoringController.getInstance();
    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    protected static final String vmName = tpmonController.getVmName();

    /** Iff true, the name of the runtime class is used,
    iff false, the name of the declaring class (interface) is used */
    protected boolean useRuntimeClassname = true;

    
    public boolean getUseRuntimeClassname() {
        return useRuntimeClassname;
    }

    
    public void setUseRuntimeClassname(boolean useRuntimeClassname) {
        this.useRuntimeClassname = useRuntimeClassname;
    }

    
    protected OperationExecutionRecord initExecutionData(MethodInvocation invocation) {
        long traceId = cfRegistry.recallThreadLocalTraceId();

        StringBuilder sb = new StringBuilder().append(invocation.getMethod().getName());
        sb.append("(");
        boolean first = true;
        for (Class<?> clazz : invocation.getMethod().getParameterTypes()) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append(clazz.getSimpleName());
        }
        sb.append(")");
        String opname = sb.toString();

        String componentName;
        if (this.useRuntimeClassname) {
            /* Use the name of the runtime class */
            componentName = invocation.getThis().getClass().getName();
        } else {
            /* Use the name of the declaring class or the interface */
            componentName = invocation.getMethod().getDeclaringClass().getName();
        }

        OperationExecutionRecord execData = new OperationExecutionRecord(
                componentName /* component */,
                opname /* operation */,
                traceId /* -1 if entry point*/);
        execData.isEntryPoint = false;
        //execData.traceId = ctrlInst.recallThreadLocalTraceId(); // -1 if entry point
        if (execData.traceId == -1) {
            execData.traceId = cfRegistry.getAndStoreUniqueThreadLocalTraceId();
            execData.isEntryPoint = true;
        }
        // here we can collect the sessionid, which may for instance be registered before by
        // a explicity call registerSessionIdentifier(String sessionid, long threadid) from a method
        // that knowns the request object (e.g. a servlet or a spring MVC controller).
        execData.sessionId = sessionRegistry.recallThreadLocalSessionId();
        execData.experimentId = tpmonController.getExperimentId();
        execData.vmName = vmName;
        return execData;
    }

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    
    public abstract Object invoke(MethodInvocation invocation) throws Throwable;

    
    protected void proceedAndMeasure(MethodInvocation invocation,
            OperationExecutionRecord execData) throws Throwable {
        execData.tin = tpmonController.getTime();
        try {
            // executing the intercepted method call
            execData.retVal = invocation.proceed();
        } catch (Exception e) {
            throw e; // exceptions are forwarded
        } finally {
            execData.tout = tpmonController.getTime();
            if (execData.isEntryPoint) {
                cfRegistry.unsetThreadLocalTraceId();
            }
        }
    }
}
