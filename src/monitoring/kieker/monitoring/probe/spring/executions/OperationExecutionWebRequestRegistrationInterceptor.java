package kieker.monitoring.probe.spring.executions;

import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @author Andre van Hoorn
 */
public class OperationExecutionWebRequestRegistrationInterceptor implements WebRequestInterceptor {

    protected static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    protected static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();


    
    public void preHandle(WebRequest request) throws Exception {
        cfRegistry.getAndStoreUniqueThreadLocalTraceId();
        sessionRegistry.storeThreadLocalSessionId(request.getSessionId());
        cfRegistry.storeThreadLocalEOI(0);
        cfRegistry.storeThreadLocalESS(1);
    }

    
    public void postHandle(WebRequest request, ModelMap map) throws Exception {
        cfRegistry.unsetThreadLocalTraceId();
        sessionRegistry.unsetThreadLocalSessionId();
        cfRegistry.unsetThreadLocalEOI();
        cfRegistry.unsetThreadLocalESS();
    }

    
    public void afterCompletion(WebRequest request, Exception map) throws Exception {
    }
}
