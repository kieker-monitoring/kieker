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
 */
public class SessionRegistrationFilter implements Filter {

    private static final TpmonController ctrlInst = TpmonController.getInstance();

    public void init(FilterConfig config) throws ServletException { }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        if (request instanceof HttpServletRequest) {
            ctrlInst.registerSessionIdentifier(((HttpServletRequest) request).getSession().getId(),
                    Thread.currentThread().getId());
        }
        chain.doFilter(request, response);
    }

    public void destroy() { }
}
