package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;
import javax.servlet.*;
import javax.servlet.http.*;
import kieker.tpmon.annotations.*;
import java.util.HashMap;
import java.util.Random;

/**
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 */
public aspect TpmonRandomPartialInstumentationServletRemote  {

        private final static TpmonController ctrlInst = TpmonController.getInstance();		  

	pointcut servletCommand(HttpServletRequest request, HttpServletResponse response): execution(* *.do*(..)) && args(request,response);

	pointcut toplevelServletCommand(HttpServletRequest request, HttpServletResponse response): servletCommand(request,response) && !cflowbelow(servletCommand(HttpServletRequest,HttpServletResponse));

	Object around(HttpServletRequest request, HttpServletResponse response): toplevelServletCommand(request,response) {
         if (!ctrlInst.isMonitoringEnabled()){
            return proceed(request, response);
        }

	//make the sessionId accessable for all advices in the same thread
	synchronized(this){
	String requestId = ""+(new Random()).nextLong();
	String sessionId = request.getSession(true).getId();
	Long threadId = Thread.currentThread().getId();
	ctrlInst.sessionThreadMatcher.put(threadId,sessionId);
	ctrlInst.requestThreadMatcher.put(threadId,requestId);
	if (ctrlInst.isDebug()) System.out.println("Execution of Servlet threadId:"+threadId+" sessionId:"+sessionId);
	}

        Object toReturn = null;
            //try {
                 // executing the intercepted method call
                toReturn = proceed(request,response);
            //} catch (Exception e) {
                    //TODO: don't know how but exceptions need to be rethrown!
                    //throw e; // doesn't work!
            //        System.out.println("tpmon ERROR: Catched exception in aspect but cannot rethrow!" + e);
            //}
            //finally {	
                //empty the sessionId 
                synchronized(this){
                    Long threadId = Thread.currentThread().getId();
                    ctrlInst.sessionThreadMatcher.remove(threadId); /* closedRequest should never be in the monitoring databased */
                    ctrlInst.requestThreadMatcher.remove(threadId);
                }
            //}
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
                 if (!ctrlInst.isMonitoringEnabled()){
                    return proceed();
                }

                if(!TpmonRandomInstrumentationController.isMonitored(thisJoinPoint.getSignature().toLongString())) {
                    return proceed();
                }


		/*
		boolean isJoinpointAtStaticMethod = thisJoinPoint.getSignature().toLongString().toLowerCase().contains("static");
		if (isJoinpointAtStaticMethod) {
			if (ctrlInst.isDebug()) System.out.println("tpmonLTW: Monitoring a static method (method of a class)");
		} else {
			if (ctrlInst.isDebug()) System.out.println("tpmonLTW: Monitoring a object method (method of a object, non-static)");
		}
		*/

		boolean isEntryPoint = false;

		synchronized(this) {
			Long threadId = Thread.currentThread().getId();
			String currentSessionId,currentRequestId;
			Object sessionIdObject = ctrlInst.sessionThreadMatcher.get(threadId);
			if (sessionIdObject == null) { /* then its an entry point since the threadId is not registered */
				currentSessionId = "null";
				ctrlInst.sessionThreadMatcher.put(threadId,currentSessionId);
				isEntryPoint = true;
			} 
			Object requestIdObject = ctrlInst.requestThreadMatcher.get(threadId);
			if(requestIdObject == null) {
				currentRequestId = ctrlInst.getUniqueIdentifierForThread(threadId);
                                ctrlInst.requestThreadMatcher.put(threadId,currentRequestId);
			}
		}		
		long startTime = ctrlInst.getTime();


		// isEntryPoint and starttime might be overwritten because they are not thread-save
		// However, it could be a large restriction to span a synchronized around the proceed() 
    		/* execution of the instrumented method: */
               Object toReturn = null;
               //try {
                // executing the intercepted method call
                    toReturn = proceed();
               //} catch (Exception e) {
                    //TODO: don't know how but exceptions need to be rethrown!
                    //throw e; // doesn't work!
               //     System.out.println("tpmon ERROR: Catched exception in aspect but cannot rethrow!" + e);
               //}
               //finally {	
                    synchronized(this) {
                        Long threadId = Thread.currentThread().getId();
                        String currentSessionId = "Error";
                        String currentRequestId = "bar";
                        Object sessionIdObject, requestIdObject;
                        if (isEntryPoint) { // its removed to have it clean for the next usage of the threadid
                        	sessionIdObject = ctrlInst.sessionThreadMatcher.remove(threadId);
                        	requestIdObject = ctrlInst.requestThreadMatcher.remove(threadId);
                        }
                        else {
                            sessionIdObject = ctrlInst.sessionThreadMatcher.get(threadId);
                            requestIdObject = ctrlInst.requestThreadMatcher.get(threadId);
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
                        if (ctrlInst.isDebug())  System.out.println("tpmonLTW: component:"+componentName+" method:"+methodName+" at:"+startTime);        	
                        long endTime = ctrlInst.getTime();
                        //String traceid=ctrlInst.getUniqueIdentifierForThread(threadId);
                        ctrlInst.insertMonitoringDataNow(componentName, methodName, currentSessionId, currentRequestId, startTime, endTime);
                        if (ctrlInst.isDebug()) System.out.println(""+componentName+","+currentSessionId+","+startTime);
                    }
		//}	
		return toReturn;
	}
}
