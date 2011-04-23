package kieker.monitoring.probe.aspectJ.operationExecution;

import javax.servlet.http.HttpServletRequest;

import kieker.monitoring.core.registry.SessionRegistry;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;

/**
 * @author Andre van Hoorn
 */
@Aspect
public abstract class AbstractOperationExecutionAspectServlet extends AbstractOperationExecutionAspect {

    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();

    
    public Object doServletEntryProfiling(final ProceedingJoinPoint thisJoinPoint) throws Throwable {
        final HttpServletRequest req = (HttpServletRequest)thisJoinPoint.getArgs()[0];
        final String sessionId = (req!=null) ? req.getSession(true).getId() : null;
        Object retVal = null;
        AbstractOperationExecutionAspectServlet.sessionRegistry.storeThreadLocalSessionId(sessionId);
        try{
            retVal = thisJoinPoint.proceed(); // does pass the args!
        } catch (final Exception exc){
            throw exc;
        } finally {
            AbstractOperationExecutionAspectServlet.sessionRegistry.unsetThreadLocalSessionId();
        }
        return retVal;
    }
}
