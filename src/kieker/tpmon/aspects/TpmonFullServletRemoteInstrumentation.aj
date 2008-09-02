package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;
import kieker.tpmon.annotations.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Random;

/**
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 */
public aspect TpmonFullServletRemoteInstrumentation  {
        TpmonController ctrlInst = TpmonController.getInstance();

	pointcut servletCommand(HttpServletRequest request, HttpServletResponse response): execution(* *.do*(..)) && args(request,response);

	pointcut toplevelServletCommand(HttpServletRequest request, HttpServletResponse response): servletCommand(request,response) && !cflowbelow(servletCommand(HttpServletRequest,HttpServletResponse));

	Object around(HttpServletRequest request, HttpServletResponse response): toplevelServletCommand(request,response) {       


            String requestId = ""+(new Random()).nextLong();
            String sessionId = request.getSession(true).getId();
            Long threadId = Thread.currentThread().getId();



            //make the sessionId accessable for all advices in the same thread
            ctrlInst.sessionThreadMatcher.put(threadId,sessionId);
            ctrlInst.requestThreadMatcher.put(threadId,requestId);

            if (ctrlInst.isDebug()) System.out.println("Execution of Servlet threadId:"+threadId+" sessionId:"+sessionId);

            Object toReturn = proceed(request,response);

            //empty the sessionId
            ctrlInst.sessionThreadMatcher.remove(threadId); /* closedRequest should never be in the monitoring databased */
            ctrlInst.requestThreadMatcher.remove(threadId);


            return toReturn;
	}

	pointcut probeClassMethod(): execution(* *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* FileSystemWriter.*(..)) && !execution(* TpmonController.*(..)) && !execution(@TpmonInternal * *.*(..));
        
	/**
	  * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
	  * response times. The response time is send to tpmon.TpmonController (a static class).
	  * Set debug = on for verbose debugging messages send to the command line.
	  *
	  * 
	  */
	Object around(): probeClassMethod() {
		           

		/*
		boolean isJoinpointAtStaticMethod = thisJoinPoint.getSignature().toLongString().toLowerCase().contains("static");
		if (isJoinpointAtStaticMethod) {
			if (ctrlInst.isDebug()) System.out.println("tpmonLTW: Monitoring a static method (method of a class)");
		} else {
			if (ctrlInst.isDebug()) System.out.println("tpmonLTW: Monitoring a object method (method of a object, non-static)");
		}
		*/

		boolean isEntryPoint = false;                
                Long threadId = Thread.currentThread().getId();
                String currentSessionId,currentRequestId;
                currentSessionId = ctrlInst.sessionThreadMatcher.get(threadId);
                if (currentSessionId == null) { /* then its an entry point since the threadId is not registered */
				currentSessionId = "null";
				ctrlInst.sessionThreadMatcher.put(threadId,currentSessionId);
				isEntryPoint = true;
                }
			
		currentRequestId = ctrlInst.requestThreadMatcher.get(threadId);
		if(currentRequestId == null) {
                    currentRequestId = ctrlInst.getUniqueIdentifierForThread(threadId);
                    ctrlInst.requestThreadMatcher.put(threadId,currentRequestId);
		}
				
                long startTime = ctrlInst.getTime();

	/* execution of the instrumented method: */

                Object toReturn;
                try {
                    // executing the intercepted method call
                    toReturn = proceed(request,response);
                } catch (Exception e) {
                    throw e; // exceptions are forwarded
                }
                finally {
                    toreturn=proceed();

                long endTime = ctrlInst.getTime();
        
		if (isEntryPoint) { // its removed to have it clean for the next usage of the threadid
                    ctrlInst.sessionThreadMatcher.remove(threadId);
                    ctrlInst.requestThreadMatcher.remove(threadId);
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


                ctrlInst.insertMonitoringDataNow(componentName, opname, currentSessionId, currentRequestId, startTime, endTime);
                if (ctrlInst.isDebug())  System.out.println("tpmonLTW: component:"+componentName+" opname:"+opname+" at:"+startTime);
                if (ctrlInst.isDebug()) System.out.println(""+componentName+","+currentSessionId+","+startTime);			
	return toreturn;
	}
}