/**
 * 
 */
package jabara.rakeup.web.ui;

import jabara.rakeup.service.DI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.wicket.Page;
import org.apache.wicket.guice.GuiceComponentInjector;
import org.apache.wicket.protocol.http.WebApplication;

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
     * @see org.apache.wicket.protocol.http.WebApplication#init()
     */
    @Override
    protected void init() {
        super.init();

        mountPages();
        initializeEncoding();
        initializeInjection();
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

    private void mountPages() {
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
