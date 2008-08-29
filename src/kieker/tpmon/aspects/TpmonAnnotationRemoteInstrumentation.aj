package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Random;

/**
 * This aspect is to instrument all methods that have an TpmonMonitoringProbe annotation.
 */
public aspect TpmonAnnotationRemoteInstrumentation  {		  	

    pointcut probeClassMethod(): execution(@TpmonMonitoringProbe * *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* FileSystemWriter.*(..)) && !execution(* TpmonController.*(..)) && !execution(@TpmonInternal * *.*(..));
        
    /**
     * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
     * response times. The response time is send to tpmon.TpmonController (a static class).
     * Set debug = on for verbose debugging messages send to the command line.	 
     */
    Object around(): probeClassMethod() {
		           
    /* prior to the execution of the instrumented method */
        boolean isEntryPoint = false;		
	Long threadId = Thread.currentThread().getId();
	String currentRequestId;	
        Object requestIdObject = TpmonController.requestThreadMatcher.get(threadId);
        int executionOrderIndex = 0; /* this is executionOrderIndex-th execution in this trace */
        int executionStackSize = 0; /* this is the hight in the dynamic call tree of this execution */
	if(requestIdObject == null) {  /* its an entry point since the threadId is not registered */            
            currentRequestId = TpmonController.getUniqueIdentifierForThread(threadId);
            TpmonController.requestThreadMatcher.put(threadId,currentRequestId);
            TpmonController.executionOrderIndexMatcher.put(currentRequestId,0);
            TpmonController.executionStackSizeMatcher.put(currentRequestId,1);
            isEntryPoint = true;
	} else {
            currentRequestId = (String)requestIdObject;
            Object executionOrderIndexObject = TpmonController.executionOrderIndexMatcher.get(currentRequestId);
            if (executionOrderIndexObject == null) 
                throw new RuntimeException("TpmonAnnotationRemoteInstrumentation.aj Critical Error: executionOrderIndexMatcher not synced");
            executionOrderIndex = (Integer)executionOrderIndexObject;
            executionOrderIndex++;
            TpmonController.executionOrderIndexMatcher.put(currentRequestId,executionOrderIndex);

            Object executionStackSizeObject = TpmonController.executionStackSizeMatcher.get(currentRequestId);
            if (executionStackSizeObject == null) 
                throw new RuntimeException("TpmonAnnotationRemoteInstrumentation.aj Critical Error: executionStackSizeMatcher not synced");
            executionStackSize = (Integer)executionStackSizeObject;
            TpmonController.executionStackSizeMatcher.put(currentRequestId,executionStackSize + 1);
        }				
        
	long startTime = TpmonController.getTime();
        
      //  System.out.println("Pre "+thisJoinPoint.getSignature().toShortString()+" eoi:"+executionOrderIndex+" ess:"+executionStackSize);
	
    /* execution of the instrumented method: */
        Object toreturn=proceed();

      //  System.out.println("Post "+thisJoinPoint.getSignature().toShortString()+" eoi:"+executionOrderIndex+" ess:"+executionStackSize);
       
    /* after the execution of the instrumented method */					
	if (isEntryPoint) { // remove it to distinguish the next usage of the threadid            
            TpmonController.requestThreadMatcher.remove(threadId);
            TpmonController.executionOrderIndexMatcher.remove(currentRequestId);
            TpmonController.executionStackSizeMatcher.remove(currentRequestId);
        } else {
            TpmonController.executionStackSizeMatcher.put(currentRequestId,executionStackSize); // one less ...
        }
        
	// componentName = z.B. com.test.Main
        String componentName = thisJoinPoint.getSignature().getDeclaringTypeName();				
        String methodName = thisJoinPoint.getSignature().toLongString();
        if (TpmonController.debug)  System.out.println("tpmonLTW: component:"+componentName+" method:"+methodName+" at:"+startTime);        	
        long endTime = TpmonController.getTime();
        TpmonController.insertMonitoringDataNow(componentName, methodName, "null", currentRequestId, startTime, endTime, executionOrderIndex, executionStackSize);
        if (TpmonController.debug) System.out.println(""+componentName+","+currentRequestId+","+startTime);
        
        // System.out.println("Log "+thisJoinPoint.getSignature().toShortString()+" eoi:"+executionOrderIndex+" ess:"+executionStackSize);
	return toreturn;
    }
}