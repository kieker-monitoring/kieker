package kieker.monitoring.probe.spring.executions;

import kieker.monitoring.core.MonitoringController;

import kieker.monitoring.core.ControlFlowRegistry;
import kieker.monitoring.core.SessionRegistry;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * kieker.tpmon.aspects.springAspectJ.KiekerTpmonRequestRegistrationInterceptor
 *
 * ==================LICENCE=========================
 * Copyright 2006-2009 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 *
 * @author Andre van Hoorn
 */
public class KiekerTpmonRequestRegistrationInterceptor implements WebRequestInterceptor {

    private static final MonitoringController ctrlInst = MonitoringController.getInstance();
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
