package kieker.tpmon.aspects;

import kieker.tpmon.*;
import kieker.tpmon.annotations.*;
import kieker.tpmon.asyncDbconnector.*;
import javax.servlet.*;
import javax.servlet.http.*;

/**
 * @author matthias
 * Based on parts of Thilo Focke's Monitoring Framework
 *
 * The Performance monitor aspect identifies all methods that have an MonitoringProbe annotation.
 * An around advice adds performance measuring code and registers mbeans for measuring points.
 * History:
 * 2008/09/01: Removed a lot "synchronized" from the Aspects
 */
public aspect TpmonMonitorAnnotationServlet {	
        private final static TpmonController ctrlInst = TpmonController.getInstance();

	pointcut servletCommand(HttpServletRequest request, HttpServletResponse response): execution(* *.do*(..)) && args(request,response);

	pointcut toplevelServletCommand(HttpServletRequest request, HttpServletResponse response): servletCommand(request,response) && !cflowbelow(servletCommand(HttpServletRequest,HttpServletResponse));

        Object around(HttpServletRequest request, HttpServletResponse response): toplevelServletCommand(request,response) {
             if (!ctrlInst.isMonitoringEnabled()){
                return proceed(request, response);
            }

            long traceId = ctrlInst.getAndStoreUniqueThreadLocalTraceId();
            String sessionId = request.getSession(true).getId();
            ctrlInst.storeThreadLocalSessionId(sessionId);
               
            if (ctrlInst.isDebug()) System.out.println("Execution of Servlet traceId:"+traceId+" sessionId:"+sessionId);

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
            ctrlInst.unsetThreadLocalTraceId();
            ctrlInst.unsetThreadLocalSessionId();
            //}
            return toReturn;
	}

	pointcut probeClassMethod(): (execution(@TpmonMonitoringProbe * *.*(..)) || execution(* *.doGet(..)) || execution(* *.doPost(..))) && !execution(* Dbconnector.*(..)) && !execution(* AsyncDbconnector.*(..)) && !execution(* DbWriter.*(..)) && !execution(* TpmonController.*(..)) 
	&& !execution(* FileSystemWriter.*(..)) && !execution(@TpmonInternal * *.*(..));

	/**
         * If we have a servlet entry method, we only want the outer one.
         */
	pointcut probeClassMethodAndStrutsEntryPoint(): probeClassMethod() && ( !(execution(* *.doGet(..)) || execution(* *.doPost(..))) || !cflowbelow(probeClassMethod()));

	/**
	  * Aspect Advice for class-methods (static) and object methods (non-static) to collect 
	  * response times. The response time is send to tpmon.TpmonController (a static class).
	  * Set debug = on for verbose debugging messages send to the command line.
	  *
	  *
	  */
	Object around(): probeClassMethodAndStrutsEntryPoint() {	
                if (!ctrlInst.isMonitoringEnabled()){
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
                long traceId = ctrlInst.recallThreadLocalTraceId();
                String sessionId = ctrlInst.recallThreadLocalSessionId();

                if (traceId == -1) { // then its an entry point since the traceId is not registered
                    traceId = ctrlInst.getAndStoreUniqueThreadLocalTraceId();
                    isEntryPoint = true;
		}
		
                if (sessionId == null) { 
                    sessionId = "unknown";
                    ctrlInst.storeThreadLocalSessionId(sessionId);
		}
		
		long startTime = ctrlInst.getTime();
	
                /* execution of the instrumented method: */
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
		
                    if (isEntryPoint) {                            
                        ctrlInst.unsetThreadLocalTraceId();
                        ctrlInst.unsetThreadLocalSessionId();
                    }

                    ctrlInst.insertMonitoringDataNow(componentName, opname, sessionId, traceId, startTime, endTime);
                    if (ctrlInst.isDebug())  System.out.println("tpmonLTW: component:"+componentName+" method:"+opname+" at:"+startTime);
                //}
		return toReturn;
	}
}