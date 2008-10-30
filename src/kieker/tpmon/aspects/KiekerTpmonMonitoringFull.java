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
public class KiekerTpmonMonitoringFull extends AbstractKiekerTpmonMonitoring { 

    @Pointcut("execution(* *.*(..)) && !execution(@TpmonInternal * *.*(..))")
    public void monitoredMethod() {
    }
   
    @Around("monitoredMethod()")
    public Object doBasicProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }

        KiekerExecutionRecord execData = this.initExecutionData(thisJoinPoint);
        try{
            this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            ctrlInst.insertMonitoringDataNow(execData);
        }
        return execData.retVal;
    }
}
