package kieker.tpmon.probe.tomcat;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import kieker.tpmon.annotation.TpmonInternal;
import kieker.tpmon.core.ControlFlowRegistry;
import kieker.tpmon.core.SessionRegistry;

import kieker.tpmon.core.TpmonController;
import kieker.tpmon.monitoringRecord.executions.KiekerExecutionRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * kieker.tpmon.probe.tomcat.KiekerTraceRegistrationFilter
 *
 * ==================LICENCE=========================
 * Copyright 2006-2008 Matthias Rohr and the Kieker Project
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
 * Register session id of incoming request
 * 
 * Servlet filter used to register session ids within the TpmonController.
 * It can be integrated into the web.xml as follows:
 * 
 * <filter>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <filter-class>kieker.tpmon.probe.tomcat.KiekerTraceRegistrationFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 *
 * @author Marco Luebcke
 */
public class KiekerTraceRegistrationFilter implements Filter {

    private static final String componentName = KiekerTraceRegistrationFilter.class.getName();
    private static final String opName = "init(FilterConfig config)";
    private static final Log log = LogFactory.getLog(KiekerTraceRegistrationFilter.class);
    private static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    private static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    private static final TpmonController ctrlInst = TpmonController.getInstance();
    private static final String vmName = ctrlInst.getVmname();

    @TpmonInternal()
    public void init(FilterConfig config) throws ServletException {
        /*        String tpmonEnabledAsString = config.getInitParameter("tpmonEnabled");
        if (tpmonEnabledAsString != null && tpmonEnabledAsString.toLowerCase().equals("true")) {
        String tpmonConfig = config.getInitParameter("tpmonConfigLocation");
        if (tpmonConfig != null && !"".equals(tpmonConfig)) {
        // following system property is needed to customise the configuration of the TpmonController
        System.setProperty("tpmon.configuration", tpmonConfig);
        }
        }*/
    }

    @TpmonInternal()
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        KiekerExecutionRecord execData = null;
        int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
        int ess = 0; /* this is the height in the dynamic call tree of this execution */
        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpReq = (HttpServletRequest) request;
            String sessionId = httpReq.getSession().getId();
            sessionRegistry.storeThreadLocalSessionId(sessionId);
            execData = KiekerExecutionRecord.getInstance(
                    componentName,
                    opName,
                    cfRegistry.getAndStoreUniqueThreadLocalTraceId() /* traceId, -1 if entry point*/);
            execData.isEntryPoint = false;
            execData.sessionId = sessionId;
            cfRegistry.storeThreadLocalEOI(0); // current execution's eoi is 0
            cfRegistry.storeThreadLocalESS(1); // *current* execution's ess is 0
            execData.vmName = vmName;
            execData.experimentId = ctrlInst.getExperimentId();
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if (session != null) {
                sessionRegistry.storeThreadLocalSessionId(session.getId());
            }
            cfRegistry.storeThreadLocalEOI(0);
            cfRegistry.storeThreadLocalESS(1);
            execData.tin = ctrlInst.getTime();
        }
        try {
            chain.doFilter(request, response);
        } finally {
            if (execData != null) {
                execData.tout = ctrlInst.getTime();
                execData.eoi = eoi;
                execData.ess = ess;
                ctrlInst.logMonitoringRecord(execData);
            }
            cfRegistry.unsetThreadLocalTraceId();
            sessionRegistry.unsetThreadLocalSessionId();
            cfRegistry.unsetThreadLocalEOI();
            cfRegistry.unsetThreadLocalESS();
        }
    }

    @TpmonInternal()
    public void destroy() {
    }
}
