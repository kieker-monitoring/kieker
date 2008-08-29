package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.util.HashMap;
import java.util.Random;

/**
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 */
public aspect TpmonMonitorFullInstServlet  {

		 
 	HashMap sessionThreadMatcher = new HashMap();
	HashMap requestThreadMatcher = new HashMap();


	pointcut servletCommand(HttpServletRequest request, HttpServletResponse response): execution(* *.do*(..)) && args(request,response);

	pointcut toplevelServletCommand(HttpServletRequest request, HttpServletResponse response): servletCommand(request,response) && !cflowbelow(servletCommand(HttpServletRequest,HttpServletResponse));

	Object around(HttpServletRequest request, HttpServletResponse response): toplevelServletCommand(request,response) {

	//make the sessionId accessable for all advices in the same thread
	synchronized(this){
	String requestId = ""+(new Random()).nextLong();
	String sessionId = request.getSession(true).getId();
	Long threadId = Thread.currentThread().getId();
	sessionThreadMatcher.put(threadId,sessionId);
	requestThreadMatcher.put(threadId,requestId);
	if (TpmonController.debug) System.out.println("Execution of Servlet threadId:"+threadId+" sessionId:"+sessionId);
	}

	Object toReturn = proceed(request,response);
	
	//empty the sessionId 
	synchronized(this){
		Long threadId = Thread.currentThread().getId();
		sessionThreadMatcher.remove(threadId); /* closedRequest should never be in the monitoring databased */
		requestThreadMatcher.remove(threadId);
	}

	return toReturn;
	}

	pointcut probeClassMethod(): execution(* *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* FileSystemWriter.*(..)) && !execution(* TpmonController.*(..)) && !execution(@TpmonInternal * *.*(..));
        
	/**
	  * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
	  * response times. The response time is send to tpmon.TpmonController (a static class).
	  * Set debug = on for verbose debugging messages send to the command line.
	  *
	  * @todo: It's not (?) thread safe yet, therefore this monitoring instrumentation might connect traces
	  * that are not connected.
	  * 
	  */
	Object around(): probeClassMethod() {
		
		/*
		boolean isJoinpointAtStaticMethod = thisJoinPoint.getSignature().toLongString().toLowerCase().contains("static");
		if (isJoinpointAtStaticMethod) {
			if (TpmonController.debug) System.out.println("tpmonLTW: Monitoring a static method (method of a class)");
		} else {
			if (TpmonController.debug) System.out.println("tpmonLTW: Monitoring a object method (method of a object, non-static)");
		}
		*/

		boolean isEntryPoint = false;

		synchronized(this) {
			Long threadId = Thread.currentThread().getId();
			String currentSessionId,currentRequestId;
			Object sessionIdObject = sessionThreadMatcher.get(threadId);
			if (sessionIdObject == null) { /* then its an entry point since the threadId is not registered */
				currentSessionId = "null";
				sessionThreadMatcher.put(threadId,currentSessionId);
				isEntryPoint = true;
			} 
			Object requestIdObject = requestThreadMatcher.get(threadId);
			if(requestIdObject == null) {
				currentRequestId = TpmonController.getUniqueIdentifierForThread(threadId);
                                requestThreadMatcher.put(threadId,currentRequestId);
			}
		}		
		long startTime = TpmonController.getTime();


		// isEntryPoint and starttime might be overwritten because they are not thread-save
		// However, it could be a large restriction to span a synchronized around the proceed() 
		/* execution of the instrumented method: */
        Object toreturn=proceed();
        
		synchronized(this) {
			Long threadId = Thread.currentThread().getId();
			String currentSessionId = "Error";
			String currentRequestId = "bar";
			Object sessionIdObject, requestIdObject;
			if (isEntryPoint) { // its removed to have it clean for the next usage of the threadid
				sessionIdObject = sessionThreadMatcher.remove(threadId);
				requestIdObject = requestThreadMatcher.remove(threadId);
			}
			else {
				sessionIdObject = sessionThreadMatcher.get(threadId);
				requestIdObject = requestThreadMatcher.get(threadId);
			}

                        if (sessionIdObject == null) { // should never happen
                            //System.out.println("Kieker.tpmon -- Error: No sessionidobject ");         	
                            currentSessionId = "sessionIdError";
                        }

                        if (requestIdObject == null) { // should never happen
                            //System.out.println("Kieker.tpmon -- Error: No requestidobject ");         	
                            currentSessionId = "requestIdError";
                        }

                        if (! (sessionIdObject == null || requestIdObject == null) ) {
                        	try {
                                    currentSessionId = (String)sessionIdObject;
                                    currentRequestId = (String)requestIdObject;
				} catch(Exception ex){}
			}

			// componentName = z.B. com.test.Main
                    String componentName = thisJoinPoint.getSignature().getDeclaringTypeName();				
                    String methodName = thisJoinPoint.getSignature().toLongString();
                    if (TpmonController.debug)  System.out.println("tpmonLTW: component:"+componentName+" method:"+methodName+" at:"+startTime);        	
                    long endTime = TpmonController.getTime();
                    //String traceid=TpmonController.getUniqueIdentifierForThread(threadId);
                    TpmonController.insertMonitoringDataNow(componentName, methodName, currentSessionId, currentRequestId, startTime, endTime);
                    if (TpmonController.debug) System.out.println(""+componentName+","+currentSessionId+","+startTime);
		}	
		return toreturn;
	}
}
