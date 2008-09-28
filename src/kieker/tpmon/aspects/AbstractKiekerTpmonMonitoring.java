/*
 *
 */
package kieker.tpmon.aspects;

import kieker.tpmon.ExecutionData;
import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 * @author Andre
 */
@Aspect
public abstract class AbstractKiekerTpmonMonitoring {

    protected static final TpmonController ctrlInst = TpmonController.getInstance();
    
    public ExecutionData initExecutionData(ProceedingJoinPoint thisJoinPoint){
        ExecutionData execData = new ExecutionData();
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
        return execData;
    }
    
    public void proceedAndMeasure(ProceedingJoinPoint thisJoinPoint, 
            ExecutionData execData) throws Throwable{       
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
    }
}
