package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;
import java.util.HashMap;

/**
 * @author matthias
 * Based on parts of Thilo Focke's Monitoring Framework
 *
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 */
public aspect TpmonMonitorAnnotationCORRECTED4BENCHMARK {	
 	HashMap sessionThreadMatcher = new HashMap();

	pointcut probeClassMethod(): execution(@TpmonMonitoringProbe * *.*(..)) && !execution(@TpmonInternal * *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* TpmonController.*(..)) 
	&& !execution(* FileSystemWriter.*(..));
        
	/**
	  * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
	  * response times. The response time is send to tpmon.TpmonController (a static class).
	  * Set debug = on for verbose debugging messages send to the command line.
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
			String currentSessionId;
			Object sessionIdObject = sessionThreadMatcher.get(threadId);
			if (sessionIdObject == null) { /* then its an entry point since the threadId is not registered */
				currentSessionId = TpmonController.getUniqueIdentifierForThread(threadId);
				sessionThreadMatcher.put(threadId,currentSessionId);
				isEntryPoint = true;
			} 
		}

		long startTime = TpmonController.getTime();

        Object toreturn=proceed();
        
		synchronized(this) {
			Long threadId = Thread.currentThread().getId();
			String currentSessionId = "Error";
			Object sessionIdObject;
			if (isEntryPoint)
				sessionIdObject = sessionThreadMatcher.remove(threadId);
			else 
				sessionIdObject = sessionThreadMatcher.get(threadId);

			if (sessionIdObject == null) { /* hmm there should be something! */
				currentSessionId = "traceIdError!";
			} else {
				try {
				currentSessionId = (String)sessionIdObject;
				} catch(Exception ex){}
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

                        if (TpmonController.debug)  System.out.println("tpmonLTW: component:"+componentName+" op:"+opname+" at:"+startTime);                        
			long endTime = TpmonController.getTime();
                        TpmonController.insertMonitoringDataNow(componentName, opname, currentSessionId, startTime, endTime);
			if (TpmonController.debug) System.out.println(""+componentName+","+currentSessionId+","+startTime);
		}	
		return toreturn;
	}
}
