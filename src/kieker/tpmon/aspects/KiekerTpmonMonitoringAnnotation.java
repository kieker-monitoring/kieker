/*
 *
 */
package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 *
 * @author Andre
 */
@Aspect
public class KiekerTpmonMonitoringAnnotation {

    private static final TpmonController ctrlInst = TpmonController.getInstance();

    @Pointcut("execution(@TpmonMonitoringProbe * *.*(..)) " +
    "&& !execution(@TpmonInternal * *.*(..)) " +
    "&& !execution(* Dbconnector.*(..)) " +
    "&& !execution(* DbWriter.*(..)) " +
    "&& !execution(* AsyncDbconnector.*(..)) " +
    "&& !execution(* TpmonController.*(..)) " +
    "&& !execution(* FileSystemWriter.*(..))")
    public void monitoredMethod() {
    }

    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }

        ExecutionData execData = new ExecutionData();
        try{
            execData = this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* not that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * execpetion! */
            ctrlInst.insertMonitoringDataNow(execData.componentName, 
                    execData.opname, execData.traceId, 
                    execData.tin, execData.tout);
        }
        return execData.retVal;
    }

    private ExecutionData proceedAndMeasure(ProceedingJoinPoint thisJoinPoint, 
            ExecutionData execData) throws Throwable{       
       // e.g. "getBook" 
        String methodname = thisJoinPoint.getSignature().getName();
        // toLongString provides e.g. "public kieker.tests.springTest.Book kieker.tests.springTest.CatalogService.getBook()"
        String paramList = thisJoinPoint.getSignature().toLongString();
        int paranthIndex = paramList.lastIndexOf('(');
        paramList = paramList.substring(paranthIndex);
        // paramList is now e.g.,  "()"
        execData.opname = methodname + paramList;
        execData.componentName = thisJoinPoint.getSignature().getDeclaringTypeName();
        
        execData.isEntryPoint = false;
        execData.traceId = ctrlInst.recallThreadLocalTraceId(); // -1 if entry point
        if (execData.traceId == -1) { 
            execData.traceId = ctrlInst.getAndStoreUniqueThreadLocalTraceId();
            execData.isEntryPoint = true;
        }
        
        execData.tin = ctrlInst.getTime(); // startint stopwatch    
        try {
            execData.retVal = thisJoinPoint.proceed();
        } catch (Exception e) {
            throw e; // exceptions are forwarded
        } finally {
            execData.tout = ctrlInst.getTime();
            if (execData.isEntryPoint) {
                ctrlInst.unsetThreadLocalTraceId();
            }
        }
        return execData;
    }
}

class ExecutionData{
    String opname = null;
    String componentName = null;
    long tin = -1;
    long tout = -1;
    boolean isEntryPoint = false;
    long traceId = -1;
    Object retVal = null;
}
