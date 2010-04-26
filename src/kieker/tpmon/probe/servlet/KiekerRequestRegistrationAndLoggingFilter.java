package kieker.tpmon.probe.servlet;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;

import kieker.tpmon.core.ControlFlowRegistry;
import kieker.tpmon.core.SessionRegistry;

import kieker.tpmon.core.TpmonController;
import kieker.common.record.OperationExecutionRecord;
import kieker.tpmon.probe.IMonitoringProbe;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2009 Kieker Project
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
*/

/**
 * Register session and trace id for incoming request.
 * This probe also logs activations of this probe as execution records.
 * 
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
public class KiekerRequestRegistrationAndLoggingFilter implements Filter, IMonitoringProbe {

    private static final String componentName = KiekerRequestRegistrationAndLoggingFilter.class.getName();
    private static final String opName = "doFilter(ServletRequest request, ServletResponse response, FilterChain chain)";
    private static final Log log = LogFactory.getLog(KiekerRequestRegistrationAndLoggingFilter.class);
    private static final SessionRegistry sessionRegistry = SessionRegistry.getInstance();
    private static final ControlFlowRegistry cfRegistry = ControlFlowRegistry.getInstance();
    private static final TpmonController ctrlInst = TpmonController.getInstance();
    private static final String vmName = ctrlInst.getVmName();

    private static final String NULL_SESSION_STR = "NULL-SERVLETFILTER";

    
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

    /** 
     * Returns the session ID from request @r or null if no session in @r.
     * 
     */
    public final String getSessionId (HttpServletRequest httpReq) {
            HttpSession session = (httpReq).getSession(false);
            if (session != null) {
                return session.getId();
            } else {
                return null;
            }
    }

    
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        OperationExecutionRecord execData = null;
        int eoi = 0; /* this is executionOrderIndex-th execution in this trace */
        int ess = 0; /* this is the height in the dynamic call tree of this execution */
        if (request instanceof HttpServletRequest) {
            execData = new OperationExecutionRecord(
                    componentName,
                    opName,
                    cfRegistry.getAndStoreUniqueThreadLocalTraceId() /* traceId, -1 if entry point*/);
            execData.sessionId = getSessionId((HttpServletRequest) request);
            if (execData.sessionId == null) {
                execData.sessionId = NULL_SESSION_STR;
            }
            sessionRegistry.storeThreadLocalSessionId(execData.sessionId);
            execData.isEntryPoint = true; // of course (however, we never evaluate it here)!
            cfRegistry.storeThreadLocalEOI(0); // current execution's eoi is 0
            cfRegistry.storeThreadLocalESS(1); // *current* execution's ess is 0
            execData.vmName = vmName;
            execData.experimentId = ctrlInst.getExperimentId();
            execData.tin = ctrlInst.getTime();
        }
        try {
            chain.doFilter(request, response);
        } finally {
            if (execData != null) {
                execData.tout = ctrlInst.getTime();
                execData.eoi = eoi;
                execData.ess = ess;
                //if execData.sessionId == null, try again to fetch it (should exist after being within the application logic)
                if (execData.sessionId == null){
                    //log.info("TraceID" + execData.traceId + "had no sessionId so far. Now?");
                    execData.sessionId = getSessionId((HttpServletRequest) request);
                    //log.info("New sessionId? " + execData.sessionId);
                }
                // TOOD: ?only log record if cfRegistry.recallThreadLocalEOI > 0?
                ctrlInst.newMonitoringRecord(execData);
            }
            // since we are in an entry point:
            cfRegistry.unsetThreadLocalTraceId();
            sessionRegistry.unsetThreadLocalSessionId();
            cfRegistry.unsetThreadLocalEOI();
            cfRegistry.unsetThreadLocalESS();
        }
    }

    
    public void destroy() {
    }
}
