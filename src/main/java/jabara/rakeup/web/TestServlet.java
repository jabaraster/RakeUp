/**
 * 
 */
package jabara.rakeup.web;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.wicket.util.io.IOUtils;

/**
 * @author jabaraster
 */
@WebServlet(urlPatterns = { "/test" })
public class TestServlet extends HttpServlet {
    private static final long serialVersionUID = -2695953427396806696L;

    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(final HttpServletRequest pReq, @SuppressWarnings("unused") final HttpServletResponse pResp) throws IOException {
        for (final Enumeration<String> headerNames = pReq.getHeaderNames(); headerNames.hasMoreElements();) {
            final String name = headerNames.nextElement();
            System.out.println(name + "\t" + pReq.getHeader(name)); //$NON-NLS-1$
        }

        final byte[] body = IOUtils.toByteArray(pReq.getInputStream());
        System.out.println(new String(body, "UTF-8")); //$NON-NLS-1$
    }
}
