/* 
 * 
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

    public void init(FilterConfig arg0) throws ServletException { }

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
