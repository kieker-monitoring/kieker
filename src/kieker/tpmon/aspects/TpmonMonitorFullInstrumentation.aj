package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.asyncDbconnector.*;
import kieker.tpmon.annotations.*;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

/**
 * @author matthias, andre
 * Based on parts of Thilo Focke's Monitoring Framework
 *
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 *
 * History:
 * 2008/09/01: Removed a lot "synchronized" from the Aspects
 */
public aspect TpmonMonitorFullInstrumentation {	
 	Map<Long,String> requestThreadMatcher = new ConcurrentHashMap<Long,String>();
        private final static TpmonController ctrlInst = TpmonController.getInstance();

	pointcut probeClassMethod(): execution(* *.*(..)) && !execution(@TpmonInternal * *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* TpmonController.*(..))
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
			if (ctrlInst.isDebug()) System.out.println("tpmonLTW: Monitoring a static method (method of a class)");
		} else {
			if (ctrlInst.isDebug()) System.out.println("tpmonLTW: Monitoring a object method (method of a object, non-static)");
		}
		*/

		boolean isEntryPoint = false;
        	Long threadId = Thread.currentThread().getId();
                String currentRequestId  = requestThreadMatcher.get(threadId);
                    if (currentRequestId == null) { /* then its an entry point since the threadId is not registered */
                    currentRequestId = ctrlInst.getUniqueIdentifierForThread(threadId);
                    requestThreadMatcher.put(threadId,currentRequestId);
                    isEntryPoint = true;
		}

		long startTime = ctrlInst.getTime();

                Object toReturn = null;
                //try {
                     // executing the intercepted method call
                     toReturn = proceed();
                //} catch (Exception e) {
                    //TODO: don't know how but exceptions need to be rethrown!
                    //throw e; // doesn't work!
                //    System.out.println("tpmon ERROR: Catched exception in aspect but cannot rethrow!" + e);
                //}
                //finally {
                    long endTime = ctrlInst.getTime();

                    if (isEntryPoint)
                        requestThreadMatcher.remove(threadId);

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

                    ctrlInst.insertMonitoringDataNow(componentName, opname, currentRequestId, startTime, endTime);
                    if (ctrlInst.isDebug())  System.out.println("tpmonLTW: component:"+componentName+" method:"+opname+" at:"+startTime);
                //}
                return toReturn;
	}
}
