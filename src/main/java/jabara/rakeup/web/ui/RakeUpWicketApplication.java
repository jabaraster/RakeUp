/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.service.DI;
import jabara.rakeup.web.ui.component.PageLink;
import jabara.rakeup.web.ui.page.EditEntryPage;
import jabara.rakeup.web.ui.page.IndexPage;
import jabara.rakeup.web.ui.page.LoginPage;
import jabara.rakeup.web.ui.page.NewEntryPage;
import jabara.rakeup.web.ui.page.RakeUpWebPageBase;
import jabara.rakeup.web.ui.page.ShowEntryPage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.Page;
import org.apache.wicket.RestartResponseAtInterceptPageException;
import org.apache.wicket.Session;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authorization.IAuthorizationStrategy;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;
import org.apache.wicket.request.component.IRequestableComponent;
import org.apache.wicket.request.cycle.RequestCycle;
import org.apache.wicket.request.mapper.parameter.PageParameters;

/**
 * 
 * @author jabaraster
 */
public class RakeUpWicketApplication extends WebApplication {

    private static final String         ENC              = "UTF-8";                //$NON-NLS-1$

    private static final List<PageLink> NAVIGATION_LINKS = createNavigationLinks();

    /**
     * @see org.apache.wicket.Application#getHomePage()
     */
    @Override
    public Class<? extends Page> getHomePage() {
        return IndexPage.class;
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#newSession(org.apache.wicket.request.Request, org.apache.wicket.request.Response)
     */
    @Override
    public Session newSession(final Request pRequest, @SuppressWarnings("unused") final Response pResponse) {
        return new RakeUpSession(pRequest);
    }

    /**
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        mountPages();
        initializeEncoding();
        initializeInjection();
        initializeSecurity();
    }

    private void initializeEncoding() {
        getMarkupSettings().setDefaultMarkupEncoding(ENC);
        getRequestCycleSettings().setResponseRequestEncoding(getMarkupSettings().getDefaultMarkupEncoding());
    }

    private void initializeInjection() {
        // GuiceによるDI機能をWicketに対して有効にする仕込み.
        // これによりDaoBaseクラスを継承したクラスのpublicメソッドはトランザクションが自動ではられるようになる.
        getComponentInstantiationListeners().add(new GuiceComponentInjector(this, DI.getGuiceInjector()));
    }

    private void initializeSecurity() {
        getSecuritySettings().setAuthorizationStrategy(new IAuthorizationStrategy() {

            /**
             * @see org.apache.wicket.authorization.IAuthorizationStrategy#isActionAuthorized(org.apache.wicket.Component,
             *      org.apache.wicket.authorization.Action)
             */
            @Override
            public boolean isActionAuthorized(@SuppressWarnings("unused") final Component pComponent, @SuppressWarnings("unused") final Action pAction) {
                return true;
            }

            /**
             * @see org.apache.wicket.authorization.IAuthorizationStrategy#isInstantiationAuthorized(java.lang.Class)
             */
            @Override
            public <T extends IRequestableComponent> boolean isInstantiationAuthorized(final Class<T> pComponentClass) {
                // Pageに載っているUI部品は常に表示OKにする.
                // どうせPage自体を表示NGにすれば部品も表示されないので、これでいい.
                if (!WebPage.class.isAssignableFrom(pComponentClass)) {
                    return true;
                }
                final RakeUpSession session = (RakeUpSession) Session.get();

                // 認証済みなのにログインページを表示しようとした場合、メインページにリダイレクトさせる.
                if (LoginPage.class.equals(pComponentClass)) {
                    if (session.isAuthenticated()) {
                        throw new RestartResponseAtInterceptPageException(IndexPage.class);
                    }
                    return true;
                }

                // 認証不要なページ（ログインページとか）は表示する.
                if (!RakeUpWebPageBase.class.isAssignableFrom(pComponentClass)) {
                    return true;
                }

                // 認証済みの場合は表示する.
                if (session.isAuthenticated()) {
                    return true;
                }

                // ログインページにリダイレクトする.
                final Request request = RequestCycle.get().getRequest();
                final PageParameters parameters = new PageParameters();

                if (!request.getUrl().getPath().isEmpty()) {
                    final String requestPath = request.getContextPath() + request.getFilterPath() + "/" + request.getUrl().getPath(); //$NON-NLS-1$
                    parameters.add("u", requestPath); //$NON-NLS-1$
                }
                throw new RestartResponseAtInterceptPageException(LoginPage.class, parameters);
            }
        });
    }

    private void mountPages() {
        this.mountPage("/login", LoginPage.class); //$NON-NLS-1$

        this.mountPage("/entry/edit/new", NewEntryPage.class); //$NON-NLS-1$
        this.mountPage("/entry/edit", EditEntryPage.class); //$NON-NLS-1$
        this.mountPage("/entry", ShowEntryPage.class); //$NON-NLS-1$
    }

    /**
     * @return ナビゲーション部分のリンク一覧.
     */
    public static List<PageLink> getNavigationLinks() {
        return NAVIGATION_LINKS;
    }

    private static List<PageLink> createNavigationLinks() {
        final List<PageLink> ret = new ArrayList<PageLink>();
        ret.add(new PageLink(IndexPage.class, "一覧")); //$NON-NLS-1$
        ret.add(new PageLink(NewEntryPage.class, "新規投稿")); //$NON-NLS-1$
        return Collections.unmodifiableList(ret);
    }
}
