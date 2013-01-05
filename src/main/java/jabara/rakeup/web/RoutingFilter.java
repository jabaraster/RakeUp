/**
 * 
 */
package jabara.rakeup.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author jabaraster
 */
public class RoutingFilter implements Filter {

    /**
     * 
     */
    public static final String PATH_UI_ROOT    = "/ui";             //$NON-NLS-1$
    /**
     * 
     */
    public static final String PATH_LOGIN_PAGE = PATH_UI_ROOT + "/"; //$NON-NLS-1$

    /**
     * @see javax.servlet.Filter#destroy()
     */
    @Override
    public void destroy() {
        // 処理なし.
    }

    /**
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(final ServletRequest pRequest, final ServletResponse pResponse, final FilterChain pChain) throws IOException,
            ServletException {

        final HttpServletRequest request = (HttpServletRequest) pRequest;
        final HttpServletResponse response = (HttpServletResponse) pResponse;

        final String requestUri = request.getRequestURI();

        if ("/".equals(requestUri)) { //$NON-NLS-1$
            redirectToLoginPage(response);
            return;
        }
        pChain.doFilter(pRequest, pResponse);
    }

    /**
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(@SuppressWarnings("unused") final FilterConfig pFilterConfig) {
        // 処理なし
    }

    private static void redirectToLoginPage(final HttpServletResponse pResponse) throws IOException {
        pResponse.sendRedirect(PATH_LOGIN_PAGE);
    }

}
