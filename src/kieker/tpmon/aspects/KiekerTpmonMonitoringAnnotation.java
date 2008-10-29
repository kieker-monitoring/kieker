/*
 *
 */
package kieker.tpmon.aspects;

import kieker.tpmon.*;

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

        ExecutionData execData = this.initExecutionData(thisJoinPoint);
        try{
            this.proceedAndMeasure(thisJoinPoint, execData);
        } catch (Exception e){
            throw e; // exceptions are forwarded          
        } finally {
            /* note that proceedAndMeasure(...) even sets the variable name
             * in case the execution of the joint point resulted in an
             * exception! */
            ctrlInst.insertMonitoringDataNow(execData.componentName, 
                    execData.opname, execData.traceId, 
                    execData.tin, execData.tout);
        }
        return execData.retVal;
    }
}
