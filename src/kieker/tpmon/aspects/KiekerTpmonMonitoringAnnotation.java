/*
 *
 */
package kieker.tpmon.aspects;

import kieker.tpmon.ExecutionData;
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
public class KiekerTpmonMonitoringAnnotation extends AbstractKiekerTpmonMonitoring { 

    @Pointcut("execution(@TpmonMonitoringProbe * *.*(..)) ")
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
             * exception! */
            ctrlInst.insertMonitoringDataNow(execData.componentName, 
                    execData.opname, execData.traceId, 
                    execData.tin, execData.tout);
        }
        return execData.retVal;
    }
}
