/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpmon.interceptors;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import kieker.tpmon.TpmonController;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * TpmonMethodInvocationInterceptor is ... 
 */
public class TpmonMethodInvocationInterceptor implements MethodInterceptor {

  public static Map<Long,String> uniqueThreadIds = Collections.synchronizedMap(new HashMap<Long,String>());
  private static final TpmonController ctrlInst = TpmonController.getInstance();
  
  /* (non-Javadoc)
   * @see org.aopalliance.intercept.MethodInterceptor#invoke(org.aopalliance.intercept.MethodInvocation)
   */
  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    //    System.out.println("Hi tpmon!");
    
    StringBuilder sb = new StringBuilder("").append(invocation.getMethod().getName());
    sb.append("(");
    boolean first = true;
    for (Class<?> clazz : invocation.getMethod().getParameterTypes()) {
      if (!first) {
        sb.append(",");
      } else {
        first = false;
      }
      sb.append(clazz.getSimpleName());
    }
    sb.append(")");
    String opname = sb.toString();
    
    String componentName = invocation.getThis().getClass().getName();
    
    long threadid = Thread.currentThread().getId();        
    String traceid = uniqueThreadIds.get(threadid);
    boolean isEntryPoint = false;
    if (traceid == null) { // its a new trace AND this is an entry point!            
        traceid = ctrlInst.getUniqueIdentifierForThread(threadid);
        uniqueThreadIds.put(threadid, traceid);
        isEntryPoint = true;            
    }            
    long tin = ctrlInst.getTime(); // startint stopwatch    

    Object retVal;
    try {
    // executing the intercepted method call
    retVal = invocation.proceed();

    } finally {
        long tout = ctrlInst.getTime();
        //checking whether this is an entry point in the trace
        if (isEntryPoint) {
            // it is an entry point -> threadid needs to be invalidated
            uniqueThreadIds.remove(threadid);               
            // therefore, the thread may be reused by the next trace (an issue of thread pools)
        }
        // here we can collect the sessionid, which may for instance be registered before by
        // a explicity call registerSessionIdentifier(String sessionid, long threadid) from a method
        // that knowns the request object (e.g. a servlet or a spring MVC controller).
        String sessionid = ctrlInst.getSessionIdentifier(threadid); 
        //TpmonController.insertMonitoringDataNow(componentName, opname, traceid, tin, tout);               
        ctrlInst.insertMonitoringDataNow(componentName, opname, sessionid, traceid,tin, tout);
    }
    // returning the result of the intercepted method call
    return retVal;
    
  }

}