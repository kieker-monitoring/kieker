package kieker.tpmon.aspects;

import kieker.tpmon.asyncDbconnector.*;
import kieker.tpmon.*;
import kieker.tpmon.annotations.*;

/**
 * @author matthias, andre
 * Based on parts of Thilo Focke's Monitoring Framework
 *
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 *
 * History:
 * 2008/09/24: Changed to new ThreadLocal interface of the controller
 * 2008/09/01: Removed a lot "synchronized" from the Aspects
 */
public aspect TpmonMonitorAnnotation {	
        private final static TpmonController ctrlInst = TpmonController.getInstance();

	pointcut probeClassMethod(): execution(@TpmonMonitoringProbe * *.*(..)) && !execution(@TpmonInternal * *.*(..)) && !execution(* Dbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* TpmonController.*(..)) 
	&& !execution(* FileSystemWriter.*(..));
        
	/**
	  * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
	  * response times. The response time is send to tpmon.TpmonController (a static class).
	  * Set debug = on for verbose debugging messages send to the command line.
	  *	  
	  */
	Object around(): probeClassMethod()  {
                 if (!ctrlInst.isMonitoringEnabled()){
                    return proceed();
                }

		boolean isEntryPoint = false;
                long traceId = ctrlInst.recallThreadLocalTraceId();
                if (traceId == -1) { // then its an entry point since the traceId is not registered
                    traceId = ctrlInst.getAndStoreUniqueThreadLocalTraceId();
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
                    //System.out.println("tpmon ERROR: Catched exception in aspect but cannot rethrow!" + e);
                //}
                //finally {
                    long endTime = ctrlInst.getTime();
	
                    if (isEntryPoint)
                        ctrlInst.unsetThreadLocalTraceId();
	
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
                    
                    ctrlInst.insertMonitoringDataNow(componentName, opname, traceId, startTime, endTime);
                    if (ctrlInst.isDebug())  System.out.println("tpmonLTW: component:"+componentName+" method:"+opname+" at:"+startTime);
                //}
            	return toReturn;
	}
}
