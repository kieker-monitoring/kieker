/*
 *
 */
package kieker.tpmon.aspects;

import kieker.tpmon.ExecutionData;
import kieker.tpmon.*;
import kieker.tpmon.annotations.TpmonInternal;
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
    
    @TpmonInternal()
    public ExecutionData initExecutionData(ProceedingJoinPoint thisJoinPoint){
       // e.g. "getBook" 
        String methodname = thisJoinPoint.getSignature().getName();
        // toLongString provides e.g. "public kieker.tests.springTest.Book kieker.tests.springTest.CatalogService.getBook()"
        String paramList = thisJoinPoint.getSignature().toLongString();
        int paranthIndex = paramList.lastIndexOf('(');
        paramList = paramList.substring(paranthIndex); // paramList is now e.g.,  "()"

        ExecutionData execData = ExecutionData.getInstance(
                thisJoinPoint.getSignature().getDeclaringTypeName() /* component */, 
                methodname + paramList /* operation */, 
                ctrlInst.recallThreadLocalTraceId() /* traceId, -1 if entry point*/);
        
        execData.isEntryPoint = false;
        execData.traceId = ctrlInst.recallThreadLocalTraceId(); // -1 if entry point
        if (execData.traceId == -1) { 
            execData.traceId = ctrlInst.getAndStoreUniqueThreadLocalTraceId();
            execData.isEntryPoint = true;
        }
        return execData;
    }
    
    @TpmonInternal()
    public abstract Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable;
    
    @TpmonInternal()
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
