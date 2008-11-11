package kieker.tpmon.aspects.springAspectJ;

import kieker.tpmon.KiekerExecutionRecord;
import kieker.tpmon.TpmonController;
import kieker.tpmon.annotations.TpmonInternal;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

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
 * @author Marco Luebcke
 */
public class KiekerTpmonMethodInvocationInterceptor implements MethodInterceptor {
    private static final TpmonController tpmonController = TpmonController.getInstance();

    /**
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @TpmonInternal()
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long traceId = tpmonController.recallThreadLocalTraceId();
        // Only go on if a traceId has been registered before
        if (traceId == -1 || !tpmonController.isMonitoringEnabled()) {
            return invocation.proceed();
        }
        
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

        String componentName = invocation.getThis().getClass().getName();

        long tin = tpmonController.getTime(); // startint stopwatch
        Object retVal;
        try {
            // executing the intercepted method call
            retVal = invocation.proceed();
        } finally {
            long tout = tpmonController.getTime();
            // here we can collect the sessionid, which may for instance be registered before by
            // a explicity call registerSessionIdentifier(String sessionid, long threadid) from a method
            // that knowns the request object (e.g. a servlet or a spring MVC controller).
            String sessionid = tpmonController.recallThreadLocalSessionId();
            // TpmonController.insertMonitoringDataNow(componentName, opname, traceid, tin, tout);
            tpmonController.insertMonitoringDataNow(KiekerExecutionRecord.getInstance(componentName, opname, sessionid, traceId, tin, tout));
        }
        // returning the result of the intercepted method call
        return retVal;
    }
}
