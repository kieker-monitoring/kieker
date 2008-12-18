package kieker.tpmon.filters;

import java.io.IOException;
import javax.servlet.*;
import javax.servlet.http.*;
import kieker.tpmon.TpmonController;

/**
 * kieker.tpmon.filters.KiekerTraceRegistrationFilter
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
 * <filter-class>kieker.tpmon.filters.KiekerTraceRegistrationFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 *
 * @author Marco Luebcke
 */
public class KiekerTraceRegistrationFilter implements Filter {

    private static final TpmonController ctrlInst = TpmonController.getInstance();

    public void init(FilterConfig config) throws ServletException {
        String tpmonEnabledAsString = config.getInitParameter("tpmonEnabled");
        if (tpmonEnabledAsString != null && tpmonEnabledAsString.toLowerCase().equals("true")) {
            String tpmonConfig = config.getInitParameter("tpmonConfigLocation");
            if (tpmonConfig != null && !"".equals(tpmonConfig)) {
                // following system property is needed to customise the configuration of the TpmonController
                System.setProperty("tpmon.configuration", tpmonConfig);
            }
        }
    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            ctrlInst.getAndStoreUniqueThreadLocalTraceId();
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if (session != null) {
                ctrlInst.storeThreadLocalSessionId(session.getId());
            }
            ctrlInst.storeThreadLocalEOI(0);
            ctrlInst.storeThreadLocalESS(1);
        }
        try {
            chain.doFilter(request, response);
        } finally {
            ctrlInst.unsetThreadLocalTraceId();
            ctrlInst.unsetThreadLocalSessionId();
            ctrlInst.unsetThreadLocalEOI();
            ctrlInst.unsetThreadLocalESS();
        }
    }

    public void destroy() {
    }
}
