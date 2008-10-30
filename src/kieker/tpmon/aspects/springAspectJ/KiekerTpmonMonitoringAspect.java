/*
 * Experimental Aspect for the Spring frameworks @AspectJ Style 
 * for Aspect Oriented Programming (AOP)
 * 
 * Current limitations: 
 *  - no session ids are stored
 *  - no tracing in distributed system 
 * 
 */
package kieker.tpmon.aspects.springAspectJ;

import kieker.tpmon.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * New Aspect written in the newer @Aspect style, which can be edited and managed in 
 * IDEs without extensions for the traditional .aj aspectj files (those had keywords
 * such as pointcut etc, which are now annotations). This kind of aspects are close
 * to the spring frameworks variant of AspectJ.
 * 
 * Todo: Monitoring in distributed systems isn't implemented for this
 * Aspect.
 * 
 * @author matthias
 */
@Aspect
public class KiekerTpmonMonitoringAspect {

    private static final TpmonController ctrlInst = TpmonController.getInstance();

    @Pointcut("execution(* *.*(..)) && !execution(* kieker.tpmon.aspects.springAspectJ.KiekerTpmonMonitoringAspect.*(..)) && !execution(* org.aspectj..*(..)) && !execution(* java..*(..)) && !execution(* sun..*(..)) && !execution(* kieker.tpmon..*(..)) && !execution(* org.apache..*(..)) && !execution(* javax..*(..))")
    public void monitoredMethod() {
    }

    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }

        String methodname = thisJoinPoint.getSignature().getName();
        // e.g. "getBook"

        // toLongString provides e.g. "public kieker.tests.springTest.Book kieker.tests.springTest.CatalogService.getBook()"
        String paramList = thisJoinPoint.getSignature().toLongString();
        int paranthIndex = paramList.lastIndexOf('(');
        paramList = paramList.substring(paranthIndex);
        // paramList is now e.g.,  "()"


        String opname = methodname + paramList;
        // e.g., "getBook()"
        //System.out.println("opname:"+opname);

        String componentName = thisJoinPoint.getSignature().getDeclaringTypeName();
        // e.g., kieker.tests.springTest.Book
        //System.out.println("componentName:"+componentName);

        long traceId = ctrlInst.recallThreadLocalTraceId();
        String sessionId = ctrlInst.recallThreadLocalSessionId(); // may be null
        
        boolean isEntryPoint = false;
        if (traceId == -1) { // its a new trace AND this is an entry point!            
            traceId = ctrlInst.getAndStoreUniqueThreadLocalTraceId();
            isEntryPoint = true;
        }
        
        long tin = ctrlInst.getTime(); // startint stopwatch    

        Object retVal;
        try {
            // executing the intercepted method call
            retVal = thisJoinPoint.proceed();
        } catch (Exception e) {
            throw e; // exceptions are forwarded
        } finally {
            long tout = ctrlInst.getTime();
            //checking whether this is an entry point in the trace
            if (isEntryPoint) {
                // it is an entry point -> threadid needs to be invalidated
                ctrlInst.unsetThreadLocalSessionId();
                ctrlInst.unsetThreadLocalTraceId();
            // therefore, the thread may be reused by the next trace (an issue of thread pools)
            }
            //TpmonController.insertMonitoringDataNow(componentName, opname, traceid, tin, tout);               
            ctrlInst.insertMonitoringDataNow(KiekerExecutionRecord.getInstance(componentName, opname, sessionId, traceId, tin, tout));
        }
        // returning the result of the intercepted method call
        return retVal;
    }
}
