/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.RakeUpEnv;

import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.http.HttpServletRequest;

import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.cycle.RequestCycle;

/**
 * @author jabaraster
 */
public class RakeUpSession extends WebSession {
    private static final long   serialVersionUID = 3081227060164973430L;

    private final AtomicBoolean authenticated    = new AtomicBoolean(false);

    /**
     * @param pRequest リクエスト.
     */
    public RakeUpSession(final Request pRequest) {
        super(pRequest);
    }

    /**
     * @param pPassword 入力されたパスワード.
     * @throws FailAuthentication 認証に失敗した場合.
     */
    public void authenticate(final String pPassword) throws FailAuthentication {
        if (RakeUpEnv.getPassword().equals(pPassword)) {
            this.authenticated.set(true);
            return;
        }
        throw new FailAuthentication();
    }

    /**
     * @see org.apache.wicket.protocol.http.WebSession#invalidate()
     */
    @Override
    public void invalidate() {
        super.invalidate();
        invalidateHttpSession();
    }

    /**
     * @see org.apache.wicket.Session#invalidateNow()
     */
    @Override
    public void invalidateNow() {
        super.invalidateNow();
        invalidateHttpSession();
    }

    /**
     * @return 認証済みセッションならtrue.
     */
    public boolean isAuthenticated() {
        return this.authenticated.get();
    }

    private static void invalidateHttpSession() {
        // Memcahcedによるセッション管理を行なっていると、Session.get()ではセッションが破棄されない.
        // なぜだ・・・
        ((HttpServletRequest) RequestCycle.get().getRequest().getContainerRequest()).getSession().invalidate();
    }
}
