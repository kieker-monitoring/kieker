/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpmon.aspects.springAspectJ;

import javax.lang.model.element.ExecutableElement;
import kieker.tpmon.KiekerExecutionRecord;
import kieker.tpmon.TpmonController;
import kieker.tpmon.annotations.*;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * TpmonMethodInvocationInterceptor is ... 
 * 
 * @author Marco Luebcke
 */
public class KiekerTpmonMethodInvocationInterceptor implements MethodInterceptor {
    private static final TpmonController tpmonController = TpmonController.getInstance();

    /* (non-Javadoc)
     * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
     */
    @TpmonInternal()
    public Object invoke(MethodInvocation invocation) throws Throwable {
        long traceId = tpmonController.recallThreadLocalTraceId();
        // Only go on if a traceId has been registered before
        if (traceId == -1 || !tpmonController.isMonitoringEnabled()) {
            return invocation.proceed();
        }
        
        StringBuilder sb = new StringBuilder("").append(invocation.getMethod().getName());
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
