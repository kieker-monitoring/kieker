/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package kieker.tpmon.aspects;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 *
 * @author avanhoorn
 */
@Aspect
public abstract class AbstractKiekerTpmonMonitoringServlet extends AbstractKiekerTpmonMonitoring {

   public Object doServletEntryProfiling(ProceedingJoinPoint thisJoinPoint) throws Throwable {
        if (!ctrlInst.isMonitoringEnabled()) {
            return thisJoinPoint.proceed();
        }
        
        HttpServletRequest req = (HttpServletRequest)thisJoinPoint.getArgs()[0];
        String sessionId = (req!=null)?req.getSession(true).getId():null;
        Object retVal = null;
        ctrlInst.storeThreadLocalSessionId(sessionId);
        try{
            retVal = thisJoinPoint.proceed(); // does pass the args!
        } catch (Exception exc){
            throw exc;
        } finally {
            ctrlInst.unsetThreadLocalSessionId();
        }
        return retVal;
    }
}
