/*
 *
 */
package kieker.tpmon.aspects;

import kieker.tpmon.KiekerExecutionRecord;
import kieker.tpmon.*;
import kieker.tpmon.annotations.*;
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
public class KiekerTpmonMonitoringAnnotationRemote extends AbstractKiekerTpmonMonitoring { 

    @Pointcut("execution(@TpmonMonitoringProbe * *.*(..)) && !execution(@TpmonInternal * *.*(..))")
    public void monitoredMethod() {
    }
   
    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }

        KiekerExecutionRecord execData = this.initExecutionData(thisJoinPoint);        
        int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
        int ess = 0; /* this is the height in the dynamic call tree of this execution */
        if (execData.isEntryPoint){
            ctrlInst.storeThreadLocalEOI(0);
            ctrlInst.storeThreadLocalESS(1);
        } else {
            eoi = ctrlInst.incrementAndRecallThreadLocalEOI();
            ess = ctrlInst.recallAndIncrementThreadLocalESS();
        }
        try{
            this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            execData.eoi = eoi;
            execData.ess = ess;
            ctrlInst.insertMonitoringDataNow(execData);
            if (execData.isEntryPoint){
                ctrlInst.unsetThreadLocalEOI();
                ctrlInst.unsetThreadLocalESS();
            } else {
                ctrlInst.storeThreadLocalESS(ess);
            }
        }
        return execData.retVal;
    }
}
