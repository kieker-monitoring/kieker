/* 
 * Servlet filter used to register session ids within 
 * the TpmonController.
 * It can be integrated into the web.xml as follows:
 * 
 * <filter>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <filter-class>kieker.tpmon.filters.SessionRegistrationFilter</filter-class>
 * </filter>
 * <filter-mapping>
 * <filter-name>sessionRegistrationFilter</filter-name>
 * <url-pattern>/*</url-pattern>
 * </filter-mapping>
 */
package kieker.tpmon.filters;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.*;

import kieker.tpmon.TpmonController;

/**
 * Register session id of incoming request
 * @author Marco Luebcke
 */
public class SessionRegistrationFilter implements Filter {

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

    public

     void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            // TODO: Move ThreadLocal<String> traceid to the TpmonController
            ctrlInst.getAndStoreUniqueThreadLocalTraceId();
            HttpSession session = ((HttpServletRequest) request).getSession(false);
            if(session!=null) {
                ctrlInst.storeThreadLocalSessionId(session.getId());
            }
        }
        try {
            chain.doFilter(request, response);
            // TODO: Move ThreadLocal<String> traceid to the TpmonController
        } finally { 
            ctrlInst.unsetThreadLocalTraceId();
            ctrlInst.unsetThreadLocalSessionId();
        }
    }

    public void destroy() {
    }
}
