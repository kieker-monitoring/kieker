package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;
import javax.servlet.*;
import kieker.tpmon.annotations.*;
import javax.servlet.http.*;
import java.util.HashMap;

/**
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 */
public aspect TpmonMonitorFullInstrumentation {

		 
 	HashMap sessionThreadMatcher = new HashMap();

        TpmonController ctrlInst = TpmonController.getInstance();
	
// execution(* *.*(..)) 
// Add here the list of methods to monitor:
	pointcut probeClassMethod(): execution(* *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* FileSystemWriter.*(..)) && !execution(* TpmonController.*(..)) && !execution(@TpmonInternal * *.*(..));
        
	/**
	  * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
	  * response times. The response time is send to tpmon.TpmonController (a static class).
	  * Set debug = on for verbose debugging messages send to the command line.
	  *
	  * @todo: It's not thread safe yet, therefore this monitoring instrumentation might connect traces
	  * that are not connected.
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

		synchronized(this) {
			Long threadId = Thread.currentThread().getId();
			String currentSessionId;
			Object sessionIdObject = sessionThreadMatcher.get(threadId);
			if (sessionIdObject == null) { /* then its an entry point since the threadId is not registered */
				currentSessionId = ctrlInst.getUniqueIdentifierForThread(threadId);
				sessionThreadMatcher.put(threadId,currentSessionId);
				isEntryPoint = true;
			} 
		}


		//long startTime = System.currentTimeMillis();
		long startTime = ctrlInst.getTime();


		// isEntryPoint and starttime might be overwritten because they are not thread-save
		// However, it could be a large restriction to span a synchronized around the proceed() 
		/* execution of the instrumented method: */
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
		
			// methodName = Main.main(..)
			// String methodName = thisJoinPoint.getSignature().toShortString();
			// methodName = Main.getBook(boolean)
			String methodName = thisJoinPoint.getSignature().toLongString();



        	if (ctrlInst.isDebug())  System.out.println("tpmonLTW: component:"+componentName+" method:"+methodName+" at:"+startTime);        	
			long endTime = ctrlInst.getTime();

        	ctrlInst.insertMonitoringDataNow(componentName, methodName, currentSessionId, startTime, endTime);
			if (ctrlInst.isDebug()) System.out.println(""+componentName+","+currentSessionId+","+startTime);
		}
		
		return toreturn;

	}

}
