package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.annotations.*;
import kieker.tpmon.asyncDbconnector.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Random;

/**
 * This aspect is to instrument all methods that have an TpmonMonitoringProbe annotation.
 */
public aspect TpmonAnnotationRemoteInstrumentationServlet  {		  	

    private final static TpmonController ctrlInst = TpmonController.getInstance();

    pointcut servletCommand(HttpServletRequest request, HttpServletResponse response): execution(* *.do*(..)) && args(request,response);

    pointcut toplevelServletCommand(HttpServletRequest request, HttpServletResponse response): servletCommand(request,response) && !cflowbelow(servletCommand(HttpServletRequest,HttpServletResponse));

    Object around(HttpServletRequest request, HttpServletResponse response): toplevelServletCommand(request,response) {
         if (!ctrlInst.isMonitoringEnabled()){
            return proceed(request, response);
        }

            //make the sessionId accessable for all advices in the same thread                          
            String sessionId = request.getSession(true).getId();
            Long threadId = Thread.currentThread().getId();            
            ctrlInst.sessionThreadMatcher.put(threadId,sessionId);					
               
            if (ctrlInst.isDebug()) System.out.println("Execution of Servlet threadId:"+threadId+" sessionId:"+sessionId);

        Object toReturn = proceed(request,response);
	
            //empty the sessionId
            ctrlInst.sessionThreadMatcher.remove(threadId); /* closedRequest should never be in the monitoring databased */            
            return toReturn;
    }


    pointcut probeClassMethod(): execution(@TpmonMonitoringProbe * *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* FileSystemWriter.*(..)) && !execution(* TpmonController.*(..)) && !execution(@TpmonInternal * *.*(..));
        
    /**
     * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
     * response times. The response time is send to tpmon.TpmonController (a static class).
     * Set debug = on for verbose debugging messages send to the command line.	 
     */
    Object around(): probeClassMethod() {
         if (!ctrlInst.isMonitoringEnabled()){
            return proceed();
        }
		           
    /* prior to the execution of the instrumented method */
        
        boolean isEntryPoint = false;		
	Long threadId = Thread.currentThread().getId();
	String currentRequestId = ctrlInst.requestThreadMatcher.get(threadId);
        int executionOrderIndex = 0; /* this is executionOrderIndex-th execution in this trace */
        int executionStackSize = 0; /* this is the hight in the dynamic call tree of this execution */
	if(currentRequestId == null) {  /* its an entry point since the threadId is not registered */
            currentRequestId = ctrlInst.getUniqueIdentifierForThread(threadId);
            ctrlInst.requestThreadMatcher.put(threadId,currentRequestId);
            ctrlInst.executionOrderIndexMatcher.put(currentRequestId,0);
            ctrlInst.executionStackSizeMatcher.put(currentRequestId,1);
            isEntryPoint = true;
	} else {
            Integer executionOrderIndexObject = ctrlInst.executionOrderIndexMatcher.get(currentRequestId);
            if (executionOrderIndexObject == null)
                throw new RuntimeException("TpmonAnnotationRemoteInstrumentation.aj Critical Error: executionOrderIndexMatcher not synced");
            executionOrderIndex = executionOrderIndexObject.intValue();
            executionOrderIndex++;
            ctrlInst.executionOrderIndexMatcher.put(currentRequestId,executionOrderIndex);

            Integer executionStackSizeObject = ctrlInst.executionStackSizeMatcher.get(currentRequestId);
            if (executionStackSizeObject == null) 
                throw new RuntimeException("TpmonAnnotationRemoteInstrumentation.aj Critical Error: executionStackSizeMatcher not synced");
            executionStackSize = executionStackSizeObject.intValue();
            ctrlInst.executionStackSizeMatcher.put(currentRequestId,executionStackSize + 1);
        }

        String currentSessionId = ctrlInst.sessionThreadMatcher.get(threadId);
        if (currentSessionId == null) { /* then its an entry point since the threadId is not registered */
            currentSessionId = "unknwn"; // do not put this in sessionThreadMatcher
        }
			
        
	long startTime = ctrlInst.getTime();
        
    if (ctrlInst.isDebug()) System.out.println("Pre "+thisJoinPoint.getSignature().toShortString()+" eoi:"+executionOrderIndex+" ess:"+executionStackSize);
    /* execution of the instrumented method: */
Object toreturn=proceed();
    if (ctrlInst.isDebug()) System.out.println("Post "+thisJoinPoint.getSignature().toShortString()+" eoi:"+executionOrderIndex+" ess:"+executionStackSize);

         long endTime = ctrlInst.getTime();

        /* after the execution of the instrumented method */
	if (isEntryPoint) { // remove it to distinguish the next usage of the threadid            
            ctrlInst.requestThreadMatcher.remove(threadId);
            ctrlInst.executionOrderIndexMatcher.remove(currentRequestId);
            ctrlInst.executionStackSizeMatcher.remove(currentRequestId);
        } else {
            ctrlInst.executionStackSizeMatcher.put(currentRequestId,executionStackSize); // one less ...
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
       
        ctrlInst.insertMonitoringDataNow(componentName, opname, currentSessionId, currentRequestId, startTime, endTime, executionOrderIndex, executionStackSize);
        if (ctrlInst.isDebug()) System.out.println("Log "+thisJoinPoint.getSignature().toShortString()+" eoi:"+executionOrderIndex+" ess:"+executionStackSize);
	return toreturn;
    }
}