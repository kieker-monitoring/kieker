/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package kieker.tpmon.aspects.springAspectJ;

import kieker.tpmon.TpmonController;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 *
 * @author Andre van Hoorn
 */
public class KiekerTpmonSessionRegistrationInterceptor implements WebRequestInterceptor {

    public void preHandle(WebRequest request) throws Exception {
        TpmonController.getInstance().getAndStoreUniqueThreadLocalTraceId();
        TpmonController.getInstance().storeThreadLocalSessionId(request.getSessionId());
    }

    public void postHandle(WebRequest request, ModelMap map) throws Exception {
        TpmonController.getInstance().unsetThreadLocalTraceId();
        TpmonController.getInstance().unsetThreadLocalSessionId();
    }

    public void afterCompletion(WebRequest request, Exception map) throws Exception {
    }
}


