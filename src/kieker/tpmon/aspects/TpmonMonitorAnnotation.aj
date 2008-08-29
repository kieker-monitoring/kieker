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
public aspect TpmonMonitorAnnotation {	
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

			// componentName = z.B. com.test.Main
		 	String componentName = thisJoinPoint.getSignature().getDeclaringTypeName();
		
			// methodeName = Main.main(..)
			// String methodName = thisJoinPoint.getSignature().toShortString();
			// methodeName = Main.getBook(boolean)
			String methodName = thisJoinPoint.getSignature().toLongString();
                        // System.out.println("kiek ii"+thisJoinPoint.getSignature().toLongString());

                        if (TpmonController.debug)  System.out.println("tpmonLTW: component:"+componentName+" method:"+methodName+" at:"+startTime);                        
			long endTime = TpmonController.getTime();
                        TpmonController.insertMonitoringDataNow(componentName, methodName, currentSessionId, startTime, endTime);
			if (TpmonController.debug) System.out.println(""+componentName+","+currentSessionId+","+startTime);
		}	
		return toreturn;
	}
}
