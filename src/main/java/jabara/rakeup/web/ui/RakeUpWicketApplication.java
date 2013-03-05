/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.model.DI;
import jabara.rakeup.web.ui.component.PageLink;
import jabara.rakeup.web.ui.page.EditEntryPage;
import jabara.rakeup.web.ui.page.IndexPage;
import jabara.rakeup.web.ui.page.LoginPage;
import jabara.rakeup.web.ui.page.LogoutPage;
import jabara.rakeup.web.ui.page.NewEntryPage;
import jabara.rakeup.web.ui.page.RestrictedPageBase;
import jabara.rakeup.web.ui.page.ShowEntryPage;
import jabara.wicket.LoginPageInstantiationAuthorizer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.Session;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.request.Request;
import org.apache.wicket.request.Response;

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
        getSecuritySettings().setAuthorizationStrategy(new LoginPageInstantiationAuthorizer() {

            @Override
            protected Class<? extends Page> getFirstPageType() {
                return IndexPage.class;
            }

            @Override
            protected Class<? extends Page> getLoginPageType() {
                return LoginPage.class;
            }

            @Override
            protected Class<? extends Page> getRestictedPageType() {
                return RestrictedPageBase.class;
            }

            @Override
            protected boolean isAuthenticated() {
                return RakeUpSession.get().isAuthenticated();
            }
        });
    }

    @SuppressWarnings("nls")
    private void mountPages() {
        this.mountPage("/login", LoginPage.class);
        this.mountPage("/logout", LogoutPage.class);

        this.mountPage("/entry/edit/new", NewEntryPage.class);
        this.mountPage("/entry/edit", EditEntryPage.class);
        this.mountPage("/entry", ShowEntryPage.class);
    }

    /**
     * @return ナビゲーション部分のリンク一覧.
     */
    public static List<PageLink> getNavigationLinks() {
        return NAVIGATION_LINKS;
    }

    private static List<PageLink> createNavigationLinks() {
        final List<PageLink> ret = new ArrayList<PageLink>();
        ret.add(new PageLink(IndexPage.class, "一覧", "icon-list")); //$NON-NLS-1$ //$NON-NLS-2$
        ret.add(new PageLink(NewEntryPage.class, "新規投稿", "icon-plus")); //$NON-NLS-1$ //$NON-NLS-2$
        ret.add(new PageLink(LogoutPage.class, "ログアウト", "icon-exit")); //$NON-NLS-1$ //$NON-NLS-2$
        return Collections.unmodifiableList(ret);
    }
}
